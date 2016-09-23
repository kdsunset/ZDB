package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.PersonalInfoResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.ui.widget.CustomFlowLayout;
import cn.zhudai.zin.zhudaibao.utils.ImageUtils;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

public class PersonalInfomationActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_user_realname)
    TextView etUserRealname;
    @Bind(R.id.rg_sex)
    RadioGroup rgSex;
    @Bind(R.id.et_user_city)
    TextView etUserCity;
    @Bind(R.id.et_invitation_code)
    TextView etInvitationCode;
    @Bind(R.id.ll_pictures)
    CustomFlowLayout llPictures;
   @Bind(R.id.rb_sex_male)
    RadioButton rbSexMale;
    @Bind(R.id.rb_sex_female)
    RadioButton rbSexFemale;
    private Context mContext;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    PersonalInfoResponse respone= (PersonalInfoResponse) msg.obj;
                    final PersonalInfoResponse.Result userInfo = respone.getResult();
                    etUserRealname.setText(userInfo.getRealname());
                    etUserCity.setText(userInfo.getCity());
                    if (!TextUtils.isEmpty(userInfo.getMy_invite_code())){
                        etInvitationCode.setText(userInfo.getMy_invite_code());
                    }
                    String sexFlag=userInfo.getSex();
                    if (sexFlag!=null){
                        if (sexFlag.equals("1")){
                            rbSexMale.setChecked(true);
                        }else if (sexFlag.equals("2")){
                            rbSexFemale.setChecked(true);
                        }
                    }else {
                        rbSexMale.setChecked(true);
                    }
                    final List<String> imgs=userInfo.getImageArray();
                    for (int i=0;i<imgs.size();i++){
                        final String url = imgs.get(i);
                       // View view= LayoutInflater.from(mContext).inflate(R.layout.view_image,null);
                       // ImageView imageView= (ImageView) view.findViewById(R.id.iv_image);
                        ImageView imageView = new ImageView(mContext);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(UIUtils.dp2px(75), UIUtils.dp2px(75));
                        layoutParams.setMargins(UIUtils.dp2px(2), UIUtils.dp2px(2), UIUtils.dp2px(2), UIUtils.dp2px(2));
                        imageView.setLayoutParams(layoutParams);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageUtils.setImg4ViewFromNet(imageView,url);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               // LogUtils.i("点击了图片");
                                Intent intent=new Intent(PersonalInfomationActivity.this,ShowImageActivity.class);
                                intent.putExtra("tag",1);
                                intent.putExtra("path",url);
                                startActivity(intent);
                            }
                        });
                        llPictures.addView(imageView);
                    }
                    break;
                case 2:
                    Exception e = (Exception) msg.obj;
                    if (e instanceof java.net.UnknownHostException ) {
                        ToastUtils.showToast(mContext, "连接错误，请检查网络！");
                      //  finish();
                    }

                    break;
                case 400:
                    BaseErrorResponse errorResponse= (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext,errorResponse.getMsg());
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_infomation);
        ButterKnife.bind(this);
        mContext=this;
        initView();
        getPersonalInfo();
    }

    private void initView() {
        initMyToolBar();
    }
    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("个人资料");
    }
    private void getPersonalInfo() {
        LogUtils.i("执行获取个人信息请求");
        String uid = "";
        if (MyApplication.user != null) {
            uid = MyApplication.user.getUid();
        }
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", uid)
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.PERSONALINFO)
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
                    PersonalInfoResponse loginResponse = gson.fromJson(htmlStr, PersonalInfoResponse.class);
                    LogUtils.i("登录返回结果" + htmlStr);
                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }

                }
                handler.sendMessage(message);
            }
        });

    }
    private void showImg(final String imagePath, Bitmap bitmap) {
        /*BitmapFactory.Options option=new BitmapFactory.Options();
       // option.inSampleSize=2;
        Bitmap bitmap=BitmapFactory.decodeFile(imagePath,option);*/
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        layoutParams.setMargins(2, 2, 2, 2);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LogUtils.i("点击了图片");
                Intent intent = new Intent(mContext, ShowImageActivity.class);
                intent.putExtra("tag", 1);
                intent.putExtra("path", imagePath);
                startActivity(intent);
            }
        });
        llPictures.addView(imageView, 0);

    }
}
