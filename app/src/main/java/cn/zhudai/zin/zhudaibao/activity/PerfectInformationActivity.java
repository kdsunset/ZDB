package cn.zhudai.zin.zhudaibao.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhotoFragmentActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.LoginResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.ui.widget.CustomFlowLayout;
import cn.zhudai.zin.zhudaibao.utils.FileUtils;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.SystemUtils;
import cn.zhudai.zin.zhudaibao.utils.TimeUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;
import okhttp3.Call;
import okhttp3.MultipartBody;

public class PerfectInformationActivity extends TakePhotoFragmentActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_user_realname)
    EditText etUserRealname;
    @Bind(R.id.rg_sex)
    RadioGroup rgSex;
    @Bind(R.id.et_user_city)
    EditText etUserCity;
    @Bind(R.id.et_invitation_code)
    EditText etInvitationCode;
    @Bind(R.id.tv_explain)
    TextView tvExplain;
    @Bind(R.id.bt_add_pic)
    Button btAddPic;
    @Bind(R.id.ll_pictures)
    CustomFlowLayout llPictures;
    @Bind(R.id.bt_handup_data)
    Button btHandupData;
    @Bind(R.id.rb_sex_male)
    RadioButton rbSexMale;
    @Bind(R.id.rb_sex_female)
    RadioButton rbSexFemale;
    private Context mContext;
    private String explain;
    private List<Bitmap> mPictureList;
    private List<String> mPicturePathList;
    private List<File> mFileList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:{
                    BaseErrorResponse respone = (BaseErrorResponse) msg.obj;
                    mProgressDialog.dismiss();
                    ToastUtils.showToastLong(mContext,respone.getMsg());
                    LogUtils.i("200");
                    break;
                }
                case 400: {
                    BaseErrorResponse respone = (BaseErrorResponse) msg.obj;
                    mProgressDialog.dismiss();
                    ToastUtils.showToastLong(mContext, respone.getMsg());
                    LogUtils.i("400");
                    break;
                }
            }
        }
    };
    private String mRealName;
    private String mUserCity;
    private String mInvitationCode;
    private int mSex;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_information);
        ButterKnife.bind(this);
        mContext = this;


        initView();

    }

    private void initView() {
        initMyToolBar();
        initProgressDialog();
        explain = "<font color=\"#ff0000\">*&nbsp;&nbsp;</font>上传资质（请至少上传如下资质的一件，" +
                "多传<br>&nbsp;&nbsp;&nbsp;&nbsp;有助于通过审核）<br>" +
                "1、银行、保险或房产中介工作证明<br>" +
                "2、营业执照<br>" +
                "3、购房合同<br>" +
                "4、购车合同";
        CharSequence charSequenceDatail = Html.fromHtml(explain);
        tvExplain.setText(charSequenceDatail);
        btAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPicDialog();
            }
        });
        btHandupData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputInfo();
                if (mPicturePathList!=null&&mPicturePathList.size()!=0){
                    uploadInfo();
                }else {
                    ToastUtils.showToast(mContext,"亲，请上传资质！");
                }



            }
        });

    }

    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("个人资料完善");
    }

    private void addPictiure() {

    }

    private void getInputInfo() {
        mRealName = UIUtils.getEditTextContent(etUserRealname);
        mUserCity = UIUtils.getEditTextContent(etUserCity);
        mInvitationCode = UIUtils.getEditTextContent(etInvitationCode);
        if (rbSexMale.isChecked()){
            mSex = 1;
        }else if (rbSexFemale.isChecked()){
            mSex=2;
        }
        LogUtils.i("输入的手机号：" + mRealName);
        LogUtils.i("输入的mUserCity：" + mUserCity);
        LogUtils.i("输入的mInvitationCode：" + mInvitationCode);
        LogUtils.i("输入的mSex：" + mSex);


    }
    private void initProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
        mProgressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        mProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        mProgressDialog.setTitle("提示");
        mProgressDialog.setMessage("正在上传...");
    }
    private void showAddPicDialog() {
        /*
          这里使用了 android.support.v7.app.AlertDialog.Builder
          可以直接在头部写 import android.support.v7.app.AlertDialog
          那么下面就可以写成 AlertDialog.Builder
         */
        //  final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        // builder.setTitle("Material Design Dialog");
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_picture, null);
        dialog.setView(view);

        String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/download";
        String fileName = "zdb_upload"+ TimeUtils.getCurrentMillis()+".jpg";
        File file=new File(dir,fileName);
        //File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        final Uri imageUri = Uri.fromFile(file);

        View tvSelectFromAlbum = view.findViewById(R.id.tv_select_from_album);
        View tvTakePhoto = view.findViewById(R.id.tv_take_photo);
        tvSelectFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getTakePhoto().onEnableCompress(new CompressConfig.Builder().setMaxSize(1920 * 1920)
                        .setMaxPixel(1920).create(), true).onPicSelectOriginal();
            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getTakePhoto().onEnableCompress(new CompressConfig.Builder().setMaxSize(1920 * 1920)
                        .setMaxPixel(1920).create(), true).onPicTakeOriginal(imageUri);

            }
        });
        dialog.show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(String msg) {
        super.takeFail(msg);
    }

    @Override
    public void takeSuccess(String imagePath) {
        super.takeSuccess(imagePath);
        SystemUtils.scanPhotos(mContext,imagePath);
        BitmapFactory.Options option = new BitmapFactory.Options();
        // option.inSampleSize=2;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, option);
        showImg(imagePath, bitmap);
      //  saveImageToLocal(bitmap);

        if (mPictureList == null) {
            mPictureList = new ArrayList<>();
            mPicturePathList=new ArrayList<>();
            mFileList=new ArrayList<>();

        }
        mPictureList.add(bitmap);
        mPicturePathList.add(imagePath);



    }

    private void showImg(final String imagePath, Bitmap bitmap) {
        /*BitmapFactory.Options option=new BitmapFactory.Options();
       // option.inSampleSize=2;
        Bitmap bitmap=BitmapFactory.decodeFile(imagePath,option);*/
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(UIUtils.dp2px(65), UIUtils.dp2px(65));
        layoutParams.setMargins(UIUtils.dp2px(0), UIUtils.dp2px(0), UIUtils.dp2px(20), UIUtils.dp2px(10));
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LogUtils.i("点击了图片");
                Intent intent = new Intent(mContext, ShowImageActivity.class);
                intent.putExtra("tag", 2);
                intent.putExtra("path", imagePath);
                startActivity(intent);
            }
        });
        llPictures.addView(imageView, 0);

    }

    private void saveImageToLocal(Bitmap bitmap) {
        String rootPath;
        boolean sdCardExist = FileUtils.isSDCardExist();
        if (sdCardExist) {
            rootPath = FileUtils.getSDCardRootPath();
        } else {
            rootPath = FileUtils.getAppFilePath(mContext);
        }
        String savePath = rootPath + TimeUtils.getCurrentMillis() + ".jpg";
        File file = new File(rootPath,
                System.currentTimeMillis() + ".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }



    private void uploadInfo() {
        mProgressDialog.show();
        LogUtils.i("执行获取个人信息请求");
        String uid = "";
        if (MyApplication.user != null) {
            uid = MyApplication.user.getUid();
        }
        okhttp3.MediaType MEDIA_TYPE_PNG = okhttp3.MediaType.parse("image/jpg");
        okhttp3. OkHttpClient client = new okhttp3.OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("uid", uid);
        builder.addFormDataPart("realname", mRealName);
        builder.addFormDataPart("sex", mSex+"");
        builder.addFormDataPart("city",mUserCity);
        for (int i = 0; i < mPicturePathList.size(); i++) {
            File f = new File(mPicturePathList.get(i));
            if (f.exists()) {
                builder.addFormDataPart("image[]", f.getName(), okhttp3.RequestBody.create(MEDIA_TYPE_PNG,f));
                LogUtils.i("imagefilepath="+f.getAbsolutePath());
            }
        }
        MultipartBody requestBody = builder.build();
        //构建请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ZDBURL.SUMBMIT_PERSONAL_INFO)//地址
                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.what = 2;
                message.obj=e;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String htmlStr = response.body().string();
                if (!TextUtils.isEmpty(htmlStr)){
                    LogUtils.i("htmlStr="+htmlStr);
                }else {
                    LogUtils.i("response.body().string()为空");
                }
                Gson gson = new Gson();
                BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                Message message = handler.obtainMessage();
                if (errorResponse.getCode()==200){
                    message.what=200;
                    message.obj=errorResponse;
                }else if (errorResponse.getCode() == 400) {
                    message.what = 400;
                    message.obj = errorResponse;

                }
                handler.sendMessage(message);
            }

        });

    }

    private void uploadInfo2(){
        String uid = "";
        if (MyApplication.user != null) {
            uid = MyApplication.user.getUid();
        }
        LogUtils.i("执行登录请求");
        OkHttpClient client = new OkHttpClient();
        okhttp3.MediaType MEDIA_TYPE_PNG = okhttp3.MediaType.parse("image/jpg");
        File file = new File(mPicturePathList.get(0));

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"uid\""),
                        RequestBody.create(null, uid))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"realname\""),
                        RequestBody.create(null, mRealName))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"sex\""),
                        RequestBody.create(null, mSex+""))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"city\""),
                        RequestBody.create(null, mUserCity))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"image\";"+
                        file.getName()), fileBody)

                .build();

        Request request = new Request.Builder()
                .url(ZDBURL.SUMBMIT_PERSONAL_INFO)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure"+e.toString());
                Message message=handler.obtainMessage();
                message.what=2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                String htmlStr = response.body().string();
                if (!TextUtils.isEmpty(htmlStr)){
                    LogUtils.i(htmlStr);
                }else {
                    LogUtils.i("response.body().string()为空");
                }
                Gson gson = new Gson();
                BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                Message message = handler.obtainMessage();
                if (errorResponse.getCode()==200){
                    message.what=200;
                    message.obj=errorResponse;
                }else if (errorResponse.getCode() == 400) {
                    message.what = 400;
                    message.obj = errorResponse;

                }
                handler.sendMessage(message);
            }
        });


    }
    private void uploadInfo3(){
        mProgressDialog.show();
        String uid = "";
        if (MyApplication.user != null) {
            uid = MyApplication.user.getUid();
        }
        LogUtils.i("执行登录请求");
        OkHttpClient client = new OkHttpClient();
        okhttp3.MediaType MEDIA_TYPE_PNG = okhttp3.MediaType.parse("image/jpg");
        File file = new File(mPicturePathList.get(0));
        OkHttpUtils.post()//
                .addParams("uid",uid)
                .addParams("realname",mRealName)
                .addParams("sex",mSex+"")
                .addParams("city",mUserCity)
                .addFile("image", file.getName(), file)//

                .url(ZDBURL.SUMBMIT_PERSONAL_INFO)

               // .headers(headers)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String htmlStr = response;
                        if (!TextUtils.isEmpty(htmlStr)){
                            LogUtils.i(htmlStr);
                        }else {
                            LogUtils.i("response.body().string()为空");
                        }
                        Gson gson = new Gson();
                        BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                        Message message = handler.obtainMessage();
                        if (errorResponse.getCode()==200){
                            message.what=200;
                            message.obj=errorResponse;
                        }else if (errorResponse.getCode() == 400) {
                            message.what = 400;
                            message.obj = errorResponse;

                        }
                        handler.sendMessage(message);
                    }
                });


    }
    private void postUserInfo() {
        String uid = "";
        if (MyApplication.user != null) {
            uid = MyApplication.user.getUid();
        }
        LogUtils.i("执行登录请求");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", uid)
                .add("realname", mRealName)
                .add("sex", mSex+"")
                .add("city", mUserCity)

                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.LOGININ)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure"+e.toString());
                Message message=handler.obtainMessage();
                message.what=2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr =  response.body().string();
                Gson gson=new Gson();
                BaseErrorResponse errorResponse=gson.fromJson(htmlStr,BaseErrorResponse.class);
                Message message=handler.obtainMessage();
                if (errorResponse.getCode()==400){
                    message.what=400;
                    message.obj=errorResponse;

                }else {
                    LoginResponse loginResponse = gson.fromJson(htmlStr, LoginResponse.class);
                    LogUtils.i("登录返回结果" + htmlStr);
                    if (loginResponse.getCode()==200){
                        message.what = 200;
                        message.obj = loginResponse;
                    }

                }
                handler.sendMessage(message);
            }
        });


    }
}
