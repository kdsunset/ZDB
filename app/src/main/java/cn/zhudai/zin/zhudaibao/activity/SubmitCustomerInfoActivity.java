package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import cn.qqtheme.framework.picker.OptionPicker;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.SubmitCustomerResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;

public class SubmitCustomerInfoActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_user_realname)
    EditText etUserRealname;
    /* @Bind(R.id.rg_sex)
     RadioGroup rgSex;*/
    @Bind(R.id.et_user_phone)
    EditText etUserPhone;
    @Bind(R.id.et_user_city)
    EditText etUserCity;
    @Bind(R.id.et_user_oan_amount)
    EditText etUserOanAmount;
    /*@Bind(R.id.et_user_age)
    EditText etUserAge;*/
  /*  @Bind(R.id.rg_hashouse)
    RadioGroup rgHashouse;
    @Bind(R.id.rg_hascar)
    RadioGroup rgHascar;*/
    @Bind(R.id.cb_ischeck)
    CheckBox cbIscheck;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    /* @Bind(R.id.rb_sex_male)
     RadioButton rbSexMale;
     @Bind(R.id.rb_sex_female)*/
    RadioButton rbSexFemale;
  /*  @Bind(R.id.rb_hashouse_yes)
    RadioButton rbHashouseYes;
    @Bind(R.id.rb_hashouse_no)
    RadioButton rbHashouseNo;
    @Bind(R.id.rb_hascar_yes)
    RadioButton rbHascarYes;
    @Bind(R.id.rb_hascar_no)
    RadioButton rbHascarNo;*/
    @Bind(R.id.sn_sex)
    Spinner snSex;
    @Bind(R.id.sn_hashouse)
    Spinner snHashouse;
    @Bind(R.id.sn_hascar)
    Spinner snHascar;

    private Context mContext;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    SubmitCustomerResponse respone = (SubmitCustomerResponse) msg.obj;
                    ToastUtils.showToast(mContext, respone.getResult());
                    /*Timer timer=new Timer();
                    TimerTask task=new TimerTask(){
                        public void run(){
                            finish();
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                    };
                    timer.schedule(task, 2000);*/

                    break;
                case 400:
                    BaseErrorResponse errorResponse = (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext, errorResponse.getMsg());
                    break;

            }
        }
    };
    private String mCustomerPhone;
    private String mCustomerRealName;
    private String mCustomerCity;
    private String mCustomerLoadAmount;
    private String mCustomerAge;
    private String mSex;
    private String mHasHouse;
    private String mHasCar;
    private String mIsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_customer_info);
        ButterKnife.bind(this);
        mContext = this;
        initView();
    }

    private void initView() {
        initMyToolBar();
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputInfo();
                attemptAction();
            }
        });
        setSexSpinner();
        setHasHouseSpinner();
        setHasCarSpinner();
    }

    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("提交客户资料");
    }

    private String getEditTextContent(EditText e) {
        return e.getText().toString().trim();

    }

    private void getInputInfo() {
        mCustomerPhone = getEditTextContent(etUserPhone);
        mCustomerRealName = getEditTextContent(etUserRealname);
        mCustomerCity = getEditTextContent(etUserCity);
        mCustomerLoadAmount = getEditTextContent(etUserOanAmount);
        // mCustomerAge = getEditTextContent(etUserAge);
       /* if (rbSexMale.isChecked()) {
            mSex = "1";
        } else if (rbSexFemale.isChecked()) {
            mSex = "2";
        }*/
       /* if (rbHashouseYes.isChecked()) {
            mHasHouse = "1";
        } else if (rbHashouseNo.isChecked()) {
            mHasHouse = "2";
        }
        if (rbHascarYes.isChecked()) {
            mHasCar = "1";
        } else if (rbHascarNo.isChecked()) {
            mHasCar = "2";
        }*/
        if (cbIscheck.isChecked()) {
            mIsChecked = "1";
        } else if (cbIscheck.isChecked()) {
            mIsChecked = "2";
        }
        LogUtils.i("UID：" + MyApplication.user.getUid());
        LogUtils.i("真实姓名：" + mCustomerRealName);
        LogUtils.i("联系方式：" + mCustomerPhone);
        LogUtils.i("所在城市：" + mCustomerCity);
        LogUtils.i("贷款金额：" + mCustomerLoadAmount);
        LogUtils.i("性别：" + mSex);
        LogUtils.i("年龄：" + mCustomerAge);
        LogUtils.i("房产：" + mHasHouse);
        LogUtils.i("车：" + mHasCar);
        LogUtils.i("同意条款：" + mIsChecked);


    }

    private void attemptAction() {
       /* if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        etUserRealname.setError(null);
        etUserPhone.setError(null);
        etUserCity.setError(null);
        etUserOanAmount.setError(null);
        // etUserAge.setError(null);
        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.

        if (TextUtils.isEmpty(mCustomerPhone)) {            //如果账号为空
            etUserPhone.setError("手机号码不能为空！");
            focusView = etUserPhone;
            cancel = true;
        } /*else if (!mCustomerPhone.matches("^1[3|4|5|8]\\d{9}$")) {        //账号非空但是不合法
            etUserPhone.setError("不合法的手机号码！");
            focusView = etUserPhone;
            cancel = true;
        }*/

        if (TextUtils.isEmpty(mCustomerRealName)) {            //如果账号为空
            etUserRealname.setError("姓名不能为空！");
            focusView = etUserRealname;
            cancel = true;
        } /*else if (!mCode.equals(mCodeFromNet)) {        //账号非空但是不合法
            etUserPwd.setError("密码包含非法字符！");
            focusView = etUserPwd;
            cancel = true;
        }*/
        if (TextUtils.isEmpty(mCustomerCity)) {            //如果账号为空
            etUserCity.setError("城市不能为空！");
            focusView = etUserCity;
            cancel = true;
        }
        if (TextUtils.isEmpty(mCustomerLoadAmount)) {            //如果账号为空
            etUserOanAmount.setError("城市不能为空！");
            focusView = etUserOanAmount;
            cancel = true;
        }
       /* if (TextUtils.isEmpty(mCustomerAge)) {            //如果账号为空
            etUserAge.setError("年龄不能为空！");
            focusView = etUserAge;
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

    private void setSexSpinner() {

        // 建立数据源
        final String[] mItems = new String[]{"先生", "女士"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        snSex.setAdapter(adapter);
        snSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                // Toast.makeText(mContext, "你点击的是:" + mItems[pos], Toast.LENGTH_SHORT).show();
                if (pos == 0) {
                    mSex = "1";
                } else if (pos == 1) {
                    mSex = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
    private void setHasHouseSpinner() {

        // 建立数据源
        final String[] mItems = new String[]{"有", "无"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        snHashouse.setAdapter(adapter);
        snHashouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                // Toast.makeText(mContext, "你点击的是:" + mItems[pos], Toast.LENGTH_SHORT).show();
                if (pos == 0) {
                    mHasHouse = "1";
                } else if (pos == 1) {
                    mHasHouse = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }
    private void setHasCarSpinner() {

        // 建立数据源
        final String[] mItems = new String[]{"有", "无"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        snHascar.setAdapter(adapter);
        snHascar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                // Toast.makeText(mContext, "你点击的是:" + mItems[pos], Toast.LENGTH_SHORT).show();
                if (pos == 0) {
                    mHasCar = "1";
                } else if (pos == 1) {
                    mHasCar = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

    private void postUserInfo() {
        LogUtils.i("执行登录请求");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", MyApplication.user.getUid())
                .add("cname", mCustomerRealName)
                .add("cphone", mCustomerPhone)
                .add("city", mCustomerCity)
                .add("money", mCustomerLoadAmount)
                .add("sex", mSex)
                .add("hashouse", mHasHouse)
                .add("hascar", mHasCar)
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.SUBMITCUSTOMERINFO)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.what = 2;
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
                    SubmitCustomerResponse loginResponse = gson.fromJson(htmlStr, SubmitCustomerResponse.class);
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
    private void selectStateSpnner(){
        OptionPicker picker = new OptionPicker(this, new String[]{
                "第一项", "第二项", "这是一个很长很长很长很长很长很长很长很长很长的很长很长的很长很长的项"
        });
        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(11);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {

            }
        });
        picker.show();
    }
}
