package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.LoginResponse;
import cn.zhudai.zin.zhudaibao.entity.User;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.LoginStatusSharePreHelper;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.TimeUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_user_phone)
    EditText etUserPhone;
    @Bind(R.id.et_user_pwd)
    EditText etUserPwd;
    @Bind(R.id.bt_login)
    Button btLogin;
    @Bind(R.id.rb_save_pwd)
    CheckBox cbSavePwd;
    @Bind(R.id.tv_forgot_pwd)
    TextView tvForgotPwd;
    private String mUserPhone;
    private String mUserPwd;
    private Context mContext;
    private LoginStatusSharePreHelper loginStatusSharePreHelper;
    private boolean isRemember;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    LoginResponse response= (LoginResponse) msg.obj;
                   // ToastUtils.showToastLong(mContext,response.toString());

                    LoginResponse.Result userInfo = response.getResult();
                    String uid=userInfo.getUid();
                    String userName=userInfo.getUsername();
                    String realName=userInfo.getRealname();
                    Long time= TimeUtils.getCurrentMillis();
                    User user= new User(userName,realName,uid,time);
                    MyApplication.setUserAfterLogin(user);
                    if (isRemember){

                        MyApplication.setLoginStatus(true);
                        loginStatusSharePreHelper.saveUid(uid);
                        loginStatusSharePreHelper.saveUserPhone(userName);
                        loginStatusSharePreHelper.saveUserRealName(realName);
                        loginStatusSharePreHelper.updateLastLoginTime(time);
                        loginStatusSharePreHelper.saveUserPwd(mUserPwd);

                    }
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext=this;
        initView();
    }
    private void initLoginStatus(){
        loginStatusSharePreHelper = new LoginStatusSharePreHelper(this);
        isRemember = loginStatusSharePreHelper.getIsRemember();
        cbSavePwd.setChecked(isRemember);
        if (isRemember){
            String phone = loginStatusSharePreHelper.getUserPhone();
            String pwd=loginStatusSharePreHelper.getUserPwd();
            if ((!TextUtils.isEmpty(phone))&&(!TextUtils.isEmpty(pwd))){
                etUserPhone.setText(phone);
                etUserPwd.setText(pwd);
            }

        }
        cbSavePwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loginStatusSharePreHelper.cleanLoginStatus();
                loginStatusSharePreHelper.saveIsRemember(isChecked);
                if (isChecked){
                    LogUtils.i("复选框被选中");
                    isRemember=true;

                }else {
                    loginStatusSharePreHelper.cleanLoginStatus();
                    isRemember=false;
                }
            }
        });
    }
    private void initView() {
        initMyToolBar();
        initLoginStatus();
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbSavePwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        loginStatusSharePreHelper.saveIsRemember(isRemember);
                        if (isChecked){
                            loginStatusSharePreHelper.saveUserPhone(mUserPhone);

                        }
                    }
                });

                getInputInfo();
                attemptLogin();

            }
        });
        tvForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPwdActivity.class));
                finish();
            }
        });




    }
    private String getEditTextContent(EditText e){
        return e.getText().toString().trim();

    }
    private void getInputInfo(){
        mUserPhone = getEditTextContent(etUserPhone);
        mUserPwd = getEditTextContent(etUserPwd);
        LogUtils.i("输入的手机号："+ mUserPhone);
        LogUtils.i("输入的验证码："+ mUserPwd);



    }
    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("助贷宝登录");
    }
    private void attemptLogin() {
       /* if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        etUserPhone.setError(null);
        etUserPwd.setError(null);
        boolean cancel = false;
        View focusView = null;



        // Check for a valid email address.

        if (TextUtils.isEmpty(mUserPhone)) {            //如果账号为空
            etUserPhone.setError("手机号码不能为空！");
            focusView = etUserPhone;
            cancel = true;
        } else if (!mUserPhone.matches("^1[3|4|5|8]\\d{9}$")) {        //账号非空但是不合法
            etUserPhone.setError("不合法的手机号码！");
            focusView = etUserPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(mUserPwd)) {            //如果账号为空
            etUserPwd.setError("密码不能为空！");
            focusView = etUserPwd;
            cancel = true;
        } /*else if (!mCode.equals(mCodeFromNet)) {        //账号非空但是不合法
            etUserPwd.setError("密码包含非法字符！");
            focusView = etUserPwd;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           /* showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
            postUserInfo();
        }
    }

    private void postUserInfo() {
        LogUtils.i("执行登录请求");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uname", mUserPhone)
                .add("passwd", mUserPwd)
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
