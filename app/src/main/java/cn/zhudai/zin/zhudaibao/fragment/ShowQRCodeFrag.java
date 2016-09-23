package cn.zhudai.zin.zhudaibao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.QRCodeResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.ImageUtils;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.SystemUtils;
import cn.zhudai.zin.zhudaibao.utils.TimeUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import okhttp3.Call;

/**
 * Created by admin on 2016/6/2.
 */
public class ShowQRCodeFrag extends Fragment {
    @Bind(R.id.iv_qrcode)
    ImageView ivQrcode;
    @Bind(R.id.bt_share)
    Button btShare;
    @Bind(R.id.bt_save)
    Button btSave;
   /* @Bind(R.id.tv_dec)
    TextView tvDec;*/
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    private Context mContext;

    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    QRCodeResponse respone = (QRCodeResponse) msg.obj;
                    QRCodeResponse.Result result = respone.getResult();
                    final String shareimgURL = result.getShareimg();

                    final String qrimgURL = result.getQrimg();
                    ImageUtils.setImg4ViewFromNet(ivQrcode, qrimgURL);
                    btShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showShare( shareimgURL);
                        }
                    });
                    btSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadFile(shareimgURL);
                        }
                    });

                    break;
                case 400:
                    BaseErrorResponse errorResponse = (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext, errorResponse.getMsg());
                    break;
                case 2200:
                    String path= (String) msg.obj;
                    ToastUtils.showToastLong(mContext, "二维码图片保存成功！保存路径："+path);
                    //通知系统刷新相册
                    SystemUtils.scanPhotos(mContext,path);
                    break;
                case 2:
                    Exception e = (Exception) msg.obj;
                    if (e instanceof java.net.UnknownHostException ) {
                        ToastUtils.showToast(mContext, "连接错误，请检查网络！");
                        // ((Activity)mContext).finish();
                    }

                    break;

            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showqrcode, container, false);
        ButterKnife.bind(this, view);
       /* String dec = "1、该二维码属于您的专属二维码，扫码后将会打开贷款申请页面；\n" +
                "2、通过该页面提交的贷款申请均属于您提交的客户；\n" +
                "3、贷款成功后的奖励与您亲自提交的客户奖励一致；";

        tvDec.setText(dec);*/
        HideKeyboard(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        getQRCodeFromNet();

    }

    private void getQRCodeFromNet() {
        LogUtils.i("获取二维码请求");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", MyApplication.user.getUid())

                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.SHOW_QRCODE)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.what = 2;
                message.obj=e;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr = response.body().string();
                Gson gson = new Gson();
                BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                Message message = handler.obtainMessage();
                if (errorResponse.getCode() == 400) {
                    message.what = 400;
                    message.obj = errorResponse;

                } else {
                    QRCodeResponse loginResponse = gson.fromJson(htmlStr, QRCodeResponse.class);
                    LogUtils.i("返回结果" + htmlStr);
                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }

                }
                handler.sendMessage(message);
            }
        });


    }
    /**
     * 下载文件
     */
    public void downloadFile(String url) {
        LogUtils.i("下载文件&url="+url);
        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/download";
        String fileName = "zhudaibao"+ TimeUtils.getCurrentMillis()+".jpg";
        OkHttpUtils//
                .get()//
                .url(url)//
                .tag("downloadFile")
                .build()//
                .execute(new FileCallBack(dir,fileName)//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        LogUtils.i("onResponse :" + file.getAbsolutePath());
                        Message message= handler.obtainMessage();
                        message.what=2200;
                        message.obj=file.getAbsolutePath();
                        handler.sendMessage(message);
                    }
                   



                });



    }
    public String saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            Log.i("TAG", "total:" + total);
            long sum = 0;
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/download");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(dir, "zhudaibao"+ TimeUtils.getCurrentMillis()+".jpg");
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
               /* final long finalSum = sum;
               // han.obtainMessage(PROG, String.valueOf(finalSum * 1.0f / total)).sendToTarget();*/
            }
            fos.flush();
            return file.getAbsolutePath();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    private void showShare( String imgUrl) {
        ShareSDK.initSDK(mContext);
        final OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));



        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        // title标题：微信、QQ（新浪微博不需要标题）
       // oks.setTitle(title);

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
       // oks.setTitleUrl(url);

        // text是分享文本，所有平台都需要这个字段
        // oks.setText("我是分享文本");
        //oks.setText(dec);


        //网络图片的url：所有平台
        oks.setImageUrl(imgUrl);//网络图片rul

        // url仅在微信（包括好友和朋友圈）中使用
      //  oks.setUrl(url);
        //oks.setUrl(url);

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
       // oks.setComment("我是测试评论文本");

        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
       // oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(mContext);
    }
    public  void HideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
    }
}
