package cn.zhudai.zin.zhudaibao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.CheckCodeRespond;
import cn.zhudai.zin.zhudaibao.entity.GetCodeBean;
import cn.zhudai.zin.zhudaibao.entity.ReSetPwdResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;

public class ForgetPwdActivity extends Activity {

    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_user_phone)
    EditText etUserPhone;
    @Bind(R.id.et_txt_identifying_code)
    EditText etTxtIdentifyingCode;
    @Bind(R.id.bt_get_identifying_code)
    Button btGetIdentifyingCode;
    @Bind(R.id.et_user_pwd)
    EditText etUserPwd;
    @Bind(R.id.et_user_pwd_confirm)
    EditText etUserPwdConfirm;
    @Bind(R.id.bt_register)
    Button btRegister;
    private String phone;
    private static final int type=2;
    private int mCodeFromNet;
    private String mUserPhone;
    private String mCode;
    private String mPwd;
    private String mPwdConfirm;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        mContext=this;
        //  getInputInfo();
        initView();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 102: {
                    String htmlStr = (String) msg.obj;
                    Gson gson = new Gson();
                    ReSetPwdResponse registerResultBean;
                    LogUtils.i("执行102");
                    registerResultBean = gson.fromJson(htmlStr, ReSetPwdResponse.class);
                    if (registerResultBean.getCode() == 200) {
                        ToastUtils.showToast(mContext, "重设密码成功！");
                        LogUtils.i(registerResultBean.toString());
                        startActivity(new Intent(ForgetPwdActivity.this, MainActivity.class));
                        finish();
                    }else if (registerResultBean.getCode()==400){
                        LogUtils.i("执行102catch");

                            ToastUtils.showToast(mContext,"重设密码失败！");
                    }
                    /*try {
                        LogUtils.i("执行102try");
                        registerResultBean = gson.fromJson(htmlStr, LoginResponse.class);

                    }catch (Exception e){

                    }*/

                    break;
                }
                case 2:
                    Toast.makeText(ForgetPwdActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case 100:
                    GetCodeBean getCodeBean= (GetCodeBean) msg.obj;
                    ToastUtils.showToast(mContext,getCodeBean.getMsg());
                    LogUtils.i(getCodeBean.getIs_register()+"code");
                    if (getCodeBean.getCode()==200){
                        initTimer();

                    }else if (getCodeBean.getCode()==400){
                    }


                    break;
                case 101: {//第一次确认
                    String htmlStr = (String) msg.obj;
                    Gson gson = new Gson();
                    CheckCodeRespond checkCodeRespond;
                    checkCodeRespond = gson.fromJson(htmlStr, CheckCodeRespond.class);
                    LogUtils.i(checkCodeRespond.getCode() + "," + checkCodeRespond.getResult().getIs_register());
                    if (checkCodeRespond.getCode() == 200) {
                        //  ToastUtils.showToast(mContext, checkCodeRespond.getResult().getIs_register());
                        postUserInfo();
                    } else {
                        BaseErrorResponse baseErrorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                        if(baseErrorResponse.getCode()==400){
                            ToastUtils.showToast(mContext, baseErrorResponse.getMsg());
                        }
                    }

                    break;
                }
            }
        }
    };
    private String phoneNumber;



    private void initView() {
        initMyToolbar();
        btGetIdentifyingCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = getEditTextContent(etUserPhone);
                LogUtils.i(phoneNumber);

                getCode(phoneNumber);
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputInfo();
                attemptLogin();
            }
        });

    }

    private void initTimer(){
        CountDownTimer timer = new CountDownTimer(1*60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String result =millisUntilFinished/1000+"";
                btGetIdentifyingCode.setText(result);
                btGetIdentifyingCode.setEnabled(false);
            }
            @Override
            public void onFinish() {
                btGetIdentifyingCode.setEnabled(true);
                btGetIdentifyingCode.setText("获取验证码");
            }
        };
        timer.start();
        // startMillis = TimeUtils.getCurrentMillis();
    }

    private void checkCode( ){
       // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uname", mUserPhone)
                .add("verifycode", mCode)
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.CHECKCODE)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure"+e.toString());

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr =  response.body().string();

                LogUtils.i("账号验证访问结果2"+htmlStr);
                Message message=handler.obtainMessage();
                message.what=101;
                message.obj=htmlStr;
                handler.sendMessage(message);
            }
        });


    }
    private void getCode(final String phoneNumber) {
        LogUtils.i("获取验证码方法");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("phone", phoneNumber)
                .add("type", type+"")
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.GETCODE)
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
                LogUtils.i("获取验证码访问结果"+htmlStr);
                Gson gson=new Gson();
                GetCodeBean result = gson.fromJson(htmlStr, GetCodeBean.class);
                Message message=handler.obtainMessage();
                message.what=100;
                message.obj=result;
                handler.sendMessage(message);


            }
        });
    }

    private void initMyToolbar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("忘记密码");
    }

    private void getInputInfo(){
        mUserPhone = getEditTextContent(etUserPhone);
        mCode = getEditTextContent(etTxtIdentifyingCode);
        mPwd = getEditTextContent(etUserPwd);
        mPwdConfirm = getEditTextContent(etUserPwdConfirm);
        LogUtils.i("输入的手机号："+mUserPhone);
        LogUtils.i("输入的验证码："+mCode);
        LogUtils.i("输入的密码："+mPwd);
        LogUtils.i("输入的确认密码："+mPwdConfirm);
        /*cbAgreeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAgree = isChecked==true?1:0;
            }
        });*/


    }

    private String getEditTextContent(EditText e){
        return e.getText().toString().trim();

    }

    private void attemptLogin() {
       /* if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        etUserPhone.setError(null);
        etTxtIdentifyingCode.setError(null);
        etUserPwd.setError(null);
        etUserPwdConfirm.setError(null);
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
        if (TextUtils.isEmpty(mCode)) {            //如果账号为空
            etTxtIdentifyingCode.setError("验证码不能为空！");
            focusView = etTxtIdentifyingCode;
            cancel = true;
        } /*else if (!mCode.equals(mCodeFromNet)) {        //账号非空但是不合法
            etTxtIdentifyingCode.setError("验证码错误！");
            focusView = etTxtIdentifyingCode;
            cancel = true;
        }*/
        if (TextUtils.isEmpty(mPwd)) {            //如果账号为空
            etUserPwd.setError("密码不能为空！");
            focusView = etUserPwd;
            cancel = true;
        } /*else if (!mCode.equals(mCodeFromNet)) {        //账号非空但是不合法
            etUserPwd.setError("密码包含非法字符！");
            focusView = etUserPwd;
            cancel = true;
        }*/
        if (TextUtils.isEmpty(mPwdConfirm)) {            //如果账号为空
            etUserPwdConfirm.setError("确认密码不能为空！");
            focusView = etUserPwdConfirm;
            cancel = true;
        } /*else if (!mCode.equals(mPwdConfirm)) {        //账号非空但是不合法
            etUserPwdConfirm.setError("密码包含非法字符！");
            focusView = etUserPwdConfirm;
            cancel = true;
        }*/
        if (!mPwd.equals(mPwdConfirm)) {            //如果账号为空
            etUserPwdConfirm.setError("两次密码输入不一致！");
            focusView = etUserPwdConfirm;
            cancel = true;
        }
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
        LogUtils.i("重设密码请求方法");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uname", mUserPhone)
                .add("verifycode", mCode)
                .add("passwd", mPwd)
                .add("confirmpasswd",mPwdConfirm)
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.DOFORGET)
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

                LogUtils.i("重设密码请求方法"+htmlStr);
                Message message=handler.obtainMessage();
                message.what=102;
                message.obj=htmlStr;
                handler.sendMessage(message);

            }
        });


    }
}
