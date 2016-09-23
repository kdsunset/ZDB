package cn.zhudai.zin.zhudaibao.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import cn.zhudai.zin.zhudaibao.entity.SimpleResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.StringUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;

/**
 * Created by admin on 2016/6/1.
 */
public abstract class WithdrawcashBaseFrag extends Fragment {
    @Bind(R.id.et_realname_bankcard)
    EditText etUserRealname;
    @Bind(R.id.et_withdraw_amount_bankcard)
    EditText etWithdrawAmount;
    /* @Bind(R.id.sn_bank_name_bankcard)
     Spinner snBankNameBankcard;*/
    @Bind(R.id.et_bank_ID_bankcard)
    EditText etUserAccount;
    @Bind(R.id.et_phone_bankcard)
    EditText etUserPhone;
    @Bind(R.id.bt_action_bankcard)
    Button btAction;
    @Bind(R.id.ll_root_bankcard)
    LinearLayout llRoot;
    @Bind(R.id.et_bank_Name_bankcard)
    EditText etBankName;
    private Context mContext;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    SimpleResponse respone = (SimpleResponse) msg.obj;
                    LogUtils.i(StringUtils.Unicode2Chz(respone.getResult()));
                    ToastUtils.showToastLong(mContext, respone.getResult());


                    break;
                case 2:
                    Exception e = (Exception) msg.obj;
                    if (e instanceof java.net.UnknownHostException ) {
                        ToastUtils.showToast(mContext, "连接错误，请检查网络！");
                       // ((Activity)mContext).finish();
                    }

                    break;
                case 400:
                    BaseErrorResponse errorResponse= (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext,errorResponse.getMsg());
                    break;

            }
        }
    };
    private String mUserRealName;
    private String mCashAmount;
    private String mBankName;
    private String mUserAccount;
    private String mUserPhone;
    private int withdrawCashMethode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdrawcash_base, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    protected abstract void initView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        withdrawCashMethode=getWithdrawCashMethod();
        btAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputInfo();
                attemptAction();
            }
        });

    }

    private String getEditTextContent(EditText e) {
        return e.getText().toString().trim();

    }

    private void getInputInfo() {
        mUserRealName = getEditTextContent(etUserRealname);
        mCashAmount = getEditTextContent(etWithdrawAmount);
        mBankName = getEditTextContent(etBankName);
        mUserAccount = getEditTextContent(etUserAccount);
        mUserPhone = getEditTextContent(etUserPhone);

        LogUtils.i("UID：" + MyApplication.user.getUid());
        LogUtils.i("真实姓名：" + mUserRealName);
        LogUtils.i("联系方式：" + mUserPhone);
        LogUtils.i("银行名称：" + mBankName);
        LogUtils.i("提现金额：" + mCashAmount);
        LogUtils.i("卡号：" + mUserAccount);
        LogUtils.i("汇款方式：" + withdrawCashMethode);


    }

    /* private void setBankListSpinner() {

         // 建立数据源
         final String[] mItems = new String[]{"先生", "女士"};
         // 建立Adapter并且绑定数据源
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mItems);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         //绑定 Adapter到控件
         snBankNameBankcard.setAdapter(adapter);
         snBankNameBankcard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
     }*/
    private void attemptAction() {
       /* if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        etUserRealname.setError(null);
        etUserPhone.setError(null);
        etBankName.setError(null);
        etUserAccount.setError(null);
        // etUserAge.setError(null);
        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.

        if (withdrawCashMethode!=WITHDRAWCASH_BYWECHAT){
            if (TextUtils.isEmpty(mUserRealName)) {            //如果账号为空
                etUserRealname.setError("姓名不能为空！");
                focusView = etUserRealname;
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(mCashAmount)) {            //如果账号为空
            etWithdrawAmount.setError("金额不能为空！");
            focusView = etWithdrawAmount;
            cancel = true;
        }
        if (withdrawCashMethode==WITHDRAWCASH_BYBANK){
            if (TextUtils.isEmpty(mBankName)) {            //如果账号为空
                etBankName.setError("银行名称不能为空！");
                focusView = etBankName;
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(mUserAccount)) {           //如果账号为空
            if (withdrawCashMethode==WITHDRAWCASH_BYBANK){
                etUserAccount.setError("卡号不能为空！");
            }else if (withdrawCashMethode==WITHDRAWCASH_BYALIPAY){
                etUserAccount.setError("支付宝账号不能为空！");
            }else if (withdrawCashMethode==WITHDRAWCASH_BYWECHAT){
                etUserAccount.setError("微信号不能为空！");
            }
            focusView = etUserAccount;
            cancel = true;
        }
        if (TextUtils.isEmpty(mUserPhone)) {            //如果账号为空
            etUserPhone.setError("联系电话不能为空！");
            focusView = etUserPhone;
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
            postWithdrawCash();
        }
    }



    private void postWithdrawCash() {
        LogUtils.i("执行请求");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", MyApplication.user.getUid())
                .add("method", withdrawCashMethode+"")
                .add("realname", mUserRealName)
                .add("money", mCashAmount)
                .add("bank", mBankName)
                .add("cardnumber", mUserAccount)
                .add("phone", mUserPhone)
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.WITHDRAWCASH)
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
                LogUtils.i("返回结果" + htmlStr);
                Gson gson = new Gson();
                BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                Message message = handler.obtainMessage();
                if (errorResponse.getCode() == 400) {
                    message.what = 400;
                    message.obj = errorResponse;

                } else {
                    SimpleResponse loginResponse = gson.fromJson(htmlStr, SimpleResponse.class);
                    LogUtils.i("返回结果" + htmlStr);
                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }

                }
                handler.sendMessage(message);
               /* Message message = handler.obtainMessage();
                try {
                    LogUtils.i("try");
                    BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                    if (errorResponse.getCode() == 400) {
                        message.what = 400;
                        message.obj = errorResponse;
                    }
                }catch (JsonSyntaxException exception){
                    LogUtils.i("cath");
                    SimpleResponse loginResponse = gson.fromJson(htmlStr, SimpleResponse.class);

                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }
                }


                handler.sendMessage(message);*/
            }
        });


    }

    public abstract int getWithdrawCashMethod() ;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    public static final int WITHDRAWCASH_BYBANK=1;
    public static final int WITHDRAWCASH_BYALIPAY=2;
    public static final int WITHDRAWCASH_BYWECHAT=3;
}
