package cn.zhudai.zin.zhudaibao.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.UpdateInfoResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.AppManager;
import cn.zhudai.zin.zhudaibao.utils.FileUtils;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.ll_root)
    LinearLayout llRoot;
    //private UpdateInfo info;
    private static final int GET_INFO_SUCCESS = 10;
    private static final int SERVER_ERROR = 11;
    private static final int SERVER_URL_ERROR = 12;
    private static final int IO_ERROR = 13;
    private static final int XML_PARSER_ERROR = 14;
    protected static final String TAG = "SplashActivity";
    private Context mContext;
    private  Object  mCancelTag=new String("downloanApp");
    private UpdateInfoResponse.Result mUpdateIno;

    private String apkUrl = "";
    private Dialog downloadDialog;
    /* 下载包安装路径 */
    /*private static final String savePath = "/sdcard/zhudaibao/";

    private static final String saveFileName = savePath + "zhuidai.apk";*/
    String savePath;
    String saveFileName;

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;
    private static final int DOWN_CANCEL=4;
    private static final int DOWN_ERROR_NOSDCARD=7;
    private boolean isInstallTag=true;
    private int progress;


    private boolean interceptFlag = false;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 404:
                    String s= (String) msg.obj;
                    LogUtils.i(s);
                   // ToastUtils.showToast(mContext,s);
                    redirectTo();
                    break;
                case 200:
                    UpdateInfoResponse response = (UpdateInfoResponse) msg.obj;
                    mUpdateIno = response.getResult();
                    int netVersionCode = mUpdateIno.getVersionCode();
                    int localVersionCode = getVersionCode(); // 取得当前应用的版本号
                    if (netVersionCode>localVersionCode){
                        showNewVersionDialog();
                    }else {
                        redirectTo();
                    }

                    break;
                case 400:

                    redirectTo();// 相同进入
                    break;
                case DOWN_OVER:
                    if (isInstallTag){
                        String path= (String) msg.obj;
                        installApk(path);
                    }
                    break;
                case DOWN_ERROR_NOSDCARD:
                    redirectTo();
                    break;
                default:
                    redirectTo();// 相同进入
                    break;
            }
        }

    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case 1024:
                    LogUtils.i("1024");
                    int tol = (int) msg.arg1;
                    int pro = (int) msg.arg2;
                   LogUtils.i("pro="+pro);
                    mProgressDialog.setMax((int) tol);
                    mProgressDialog.setProgress((int) pro);
             /*   case DOWN_OVER:
                    String path= (String) msg.obj;
                    installApk(path);
                    break;*/
                default:
                    break;
            }
        }
    };
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        mContext = this;
        // 添加界面的渐变动画 效果
        Animation animation = new AlphaAnimation(0.3f, 1f);
        animation.setDuration(2000);
        // 启动动画
        llRoot.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               // checkVesion();
               // getVersion();
              //  checkUpdate();
                getUpdateInfoFromNet();
            }
        });
    }



    private void getUpdateInfoFromNet() {
        LogUtils.i("检查更新的请求");
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        final Request request = new Request.Builder()
                .url(ZDBURL.CHECK_VERSION)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.what = 404;
                message.obj=e.toString();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr = response.body().string();
                LogUtils.i("返回结果" + htmlStr);
                Gson gson = new Gson();
                Message message = handler.obtainMessage();
                try {
                    UpdateInfoResponse loginResponse = gson.fromJson(htmlStr, UpdateInfoResponse.class);
                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }

                }catch (JsonSyntaxException exception){
                    BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                    if (errorResponse.getCode() == 400) {
                        message.what = 400;
                        message.obj = errorResponse;
                    }
                }
                handler.sendMessage(message);
            }

        });

    }

    // 显示升级对话框
    protected void showNewVersionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this); // 实例化对话框
        builder.setTitle("升级提示");// 添加标题
        builder.setCancelable(false);
        String tip="";
        if (!TextUtils.isEmpty(mUpdateIno.getDescription())){
           tip=mUpdateIno.getDescription();
        }
        CharSequence html =  Html.fromHtml(tip);

        builder.setMessage(html); // 添加内容
        builder.setPositiveButton("升级", new DialogInterface.OnClickListener() { // 点击升级时的操作方法
            @Override
            public void onClick(DialogInterface dialog, int which) {
                apkUrl = mUpdateIno.getDownload_url();
              //  showDownloadDialog();
              //  showDownloadProgerssDialog();
                showDownloadProgerssDialog();
            }
        });
        builder.setNegativeButton("忽略", new DialogInterface.OnClickListener() { // 点击取消时的操作方法
            @Override
            public void onClick(DialogInterface dialog, int which) {
                redirectTo();
            }
        });
        builder.create().show(); // 显示对话框

    }


    /*private class CheckVersionTask implements Runnable {
        @Override
        public void run() {
            Message msg = Message.obtain();
            // 1、取得服务器地址
            String serverUrl = ZDBURL.CHECK_VERSION; // 取得服务器地址
            // 2、连接服务器
            try {
                URL url = new URL(serverUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream is = conn.getInputStream(); // 取得服务器返回的内容
                    info = UpdateInfoParser.getUpdateInfo(is); // 调用自己编写的方法，将输入流转换为UpdateInfo对象
                    msg.what = GET_INFO_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    msg.what = SERVER_ERROR;
                    handler.sendMessage(msg);
                }
            } catch (MalformedURLException e) {
                msg.what = SERVER_URL_ERROR;
                handler.sendMessage(msg);
                handler.sendMessage(msg);
            } catch (IOException e) {
                msg.what = IO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                msg.what = XML_PARSER_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }*/

    /**
     * 取得应用的版本号
     *
     * @return
     */
    public int getVersionCode() {
        PackageManager pm = getPackageManager(); // 取得包管理器的对象，这样就可以拿到应用程序的管理对象
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0); // 得到应用程序的包信息对象
            LogUtils.i("versionName"+info.versionCode);
            return info.versionCode; // 取得应用程序的版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // 此异常不会发生
            return -1;
        }
    }


    // 取消下载
    public boolean intercept() {
        return this.interceptFlag;
    }

   /* public void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件版本更新");
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_update_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        builder.setView(v);
        builder.setPositiveButton("后台下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                redirectTo();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                redirectTo();
                interceptFlag = true;
            }
        });

        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }*/

   /* private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };*/




    private void showDownloadProgerssDialog(){
        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        mProgressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        mProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        mProgressDialog.setTitle("正在下载...");
        //mProgressDialog.setMessage("正在下载...");
        mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "后台下载",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        redirectTo();

                    }
                });
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        OkHttpUtils.getInstance().cancelTag(mContext);
                        isInstallTag=false;
                        dialog.dismiss();
                        redirectTo();
                    }
                });
        mProgressDialog.show();
        downloadFile(mUpdateIno.getDownload_url());
    }
    /**
     * 下载文件
     */
    public void downloadFile(final String url) {
        LogUtils.i("下载文件&url=" + url);
        saveFileName = "zhudaibao" + mUpdateIno.getVersionName() + ".apk";
        if (FileUtils.isSDCardExist()) {
            savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/apks";
        } else {
            LogUtils.i("getExternalStorageState/" + "内存卡不可用");
            ToastUtils.showToast(mContext, "SD卡不可用!");
            //savePath=mContext.getApplicationContext().getFilesDir().getAbsolutePath();
            Message message=handler.obtainMessage();
            message.what=DOWN_ERROR_NOSDCARD;
            handler.sendMessage(message);
            return;
        }
        OkHttpUtils//
                .get()//
                .url(url)//
                .tag(mCancelTag)
                .build()//
                .execute(new FileCallBack(savePath, saveFileName)//
                {

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        //super.inProgress(progress, total, id);
                        LogUtils.i("执行inProgress ");
                        LogUtils.i("进度>>>> " + ((int) (total * progress)));
                        LogUtils.i("total>>>> " + total);
                        LogUtils.i("id>>>> " + id);
                        /*setmax 和setprogree是为了计算百分比，显示进度使用setProgressNumberFormat*/
                        mProgressDialog.setMax((int) total);
                        mProgressDialog.setProgress((int) (total * progress));
                        float all = (float) (total / 1024.0 / 1024.0);
                        float percent = total * progress / 1024 / 1024;
                        LogUtils.i("float_all=" + all);
                        LogUtils.i("float_per=" + percent);
                        mProgressDialog.setProgressNumberFormat(String.format("%.2f MB/%.2f MB", percent, all));


                        /*String tol= Formatter.formatFileSize(mContext,total);
                        String pro=Formatter.formatFileSize(mContext,(long) progress);*/
                       /* mProgressDialog.setMax((int) total);
                        mProgressDialog.setProgress((int)(total* progress));*/
                      /*  Message message=mHandler.obtainMessage();
                        message.arg1= (int) total;
                        message.arg2= (int) progress;
                        message.what=1024;
                        mHandler.sendMessage(message);*/

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        mProgressDialog.dismiss();
                        LogUtils.i("执行onResponse");
                        LogUtils.i("onResponse :" + file.getAbsolutePath());
                        Message message = handler.obtainMessage();
                        message.what = DOWN_OVER;
                        message.obj = file.getAbsolutePath();
                        handler.sendMessage(message);
                    }

                });


    }
   /* public void downloadApk() {
       *//* downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();*//*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apkUrl);

                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (numread <= 0) {
                            // 下载完成通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!interceptFlag);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
*/
    /**
     * 安装apk
     *
     */
    private void installApk(String path) {
      //  File apkfile = new File(saveFileName);
        File apkfile = new File(path);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        startActivityForResult(i, 0);// 通过此方式启动Activity，会回调onActivitResult()方法

    }

    // 进入
    private void redirectTo() {
        startActivity(new Intent(getApplicationContext(),
                LoginSelectActivity.class));
        AppManager.getAppManager().finishActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (AppManager.getAppManager().currentActivity() == this) {
                redirectTo();
            }
        }
    }
}
