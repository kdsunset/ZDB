package cn.zhudai.zin.zhudaibao.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.adapter.LoadScheduleRVAdapter;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.LoadScheduleResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.ui.DividerItemDecoration;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

public class CheckLoanScheduleActivity extends Activity {

    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_load_schedule)
    RecyclerView rvLoadSchedule;
    @Bind(R.id.tv_tip)
    TextView tvTip;
    @Bind(R.id.et_search_name)
    EditText etSearchName;
    @Bind(R.id.et_search_phone)
    EditText etSearchPhone;
    @Bind(R.id.et_search_sum)
    EditText etSearchSum;
    @Bind(R.id.et_search_state)
    EditText etSearchState;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    @Bind(R.id.pb_loading)
    ProgressBar pbLoading;
    private Context mContext;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 200: {
                    LogUtils.i("case 200");
                    pbLoading.setVisibility(View.GONE);
                    LoadScheduleResponse respone = (LoadScheduleResponse) msg.obj;
                    List<LoadScheduleResponse.Result> dataList = respone.getResult();
                    LoadScheduleRVAdapter adapter = new LoadScheduleRVAdapter(mContext, dataList);
                    rvLoadSchedule.setAdapter(adapter);
                    /*adapter.setOnItemClickListener(new LoadScheduleRVAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, LoadScheduleResponse.Result data) {

                        }
                    });*/
                    if (dataList == null || dataList.size() == 0) {
                        ToastUtils.showToast(mContext, "没有查询到符合的数据！");
                    }

                    break;
                }
                case 2200:
                    LogUtils.i("case 2200");
                    pbLoading.setVisibility(View.GONE);
                    LoadScheduleResponse respone = (LoadScheduleResponse) msg.obj;
                    List<LoadScheduleResponse.Result> dataList = respone.getResult();
                    LoadScheduleRVAdapter adapter = new LoadScheduleRVAdapter(mContext, dataList);
                    rvLoadSchedule.setAdapter(adapter);
                    /*adapter.setOnItemClickListener(new LoadScheduleRVAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, LoadScheduleResponse.Result data) {

                        }
                    });*/

                    break;
                case 2:
                    Exception e = (Exception) msg.obj;
                    pbLoading.setVisibility(View.GONE);
                    if (e instanceof UnknownHostException) {
                        ToastUtils.showToast(mContext, "连接错误，请检查网络！");
                        finish();
                    }

                    break;
                case 400:
                    pbLoading.setVisibility(View.GONE);
                    BaseErrorResponse errorResponse = (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext, errorResponse.getMsg());
                    break;


                case -1://空数据
                    pbLoading.setVisibility(View.GONE);
                    LogUtils.i("msg=" + msg.what);
                    tvTip.setVisibility(View.VISIBLE);
                    rvLoadSchedule.setVisibility(View.GONE);
                    tvTip.setText("您还没有提交过客户，请尽快进行提交吧，\n感谢对助贷宝的支持！");
                    break;

            }
        }
    };
    private String mSearchName;
    private String mSearchPhone;
    private String mSearchSum;
    private String mSearchState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_loan_schedule);
        ButterKnife.bind(this);
        mContext = this;

        initView();
        setRecyclerView();
        getLoadSchedule();
    }

    private void initView() {
        initMyToolBar();
        //  setRecyclerView();
        etSearchState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToastUtils.showToast(mContext,"点击");
                selectState();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputInfo();
                attemptAction();
            }
        });


    }

    private void getInputInfo() {
        mSearchName = UIUtils.getEditTextContent(etSearchName);
        mSearchPhone = UIUtils.getEditTextContent(etSearchPhone);
        mSearchSum = UIUtils.getEditTextContent(etSearchSum);
        if (mSearchState == null) {
            mSearchState = 0 + "";
        }
        LogUtils.i("UID：" + MyApplication.user.getUid());
        LogUtils.i("真实姓名：" + mSearchName);
        LogUtils.i("联系方式：" + mSearchPhone);
        LogUtils.i("贷款金额：" + mSearchSum);
        LogUtils.i("状态：" + mSearchState);


    }

    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("查看贷款进度");

    }

    private void setRecyclerView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        // MainMenuAdapter mAdapter = new MainMenuAdapter(mContext,mainMenuDatas,userInfoStatus);
        /*mAdapter.setOnItemClickListener(new MainMenuAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, MainMenuData data) {

            }
        });
        rvLoadSchedule.setAdapter(mAdapter);*/

        //每个item高度一致，可设置为true，提高性能
        rvLoadSchedule.setHasFixedSize(true);
        rvLoadSchedule.setLayoutManager(mLinearLayoutManager);


        //分隔线

        rvLoadSchedule.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL,
                UIUtils.dp2px(10), getResources().getColor(R.color.colorLightGray)));

        ///为每个item增加响应事件

    }

    private void getLoadSchedule() {
        LogUtils.i("getLoadSchedule");
        String uid = "";
        if (MyApplication.user != null) {
            uid = MyApplication.user.getUid();
        }
        LogUtils.i("uid4654=" + uid);
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", uid)
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.LOAN_SCHEDULE)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.what = 2;
                message.obj = e;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr = response.body().string();
                Message message = handler.obtainMessage();
                if (!TextUtils.isEmpty(htmlStr)) {
                    LogUtils.i("返回结果" + htmlStr);
                    Gson gson = new Gson();
                    BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);

                    if (errorResponse.getCode() == 400) {
                        message.what = 400;
                        message.obj = errorResponse;

                    } else {
                        LoadScheduleResponse loginResponse = gson.fromJson(htmlStr, LoadScheduleResponse.class);

                        if (loginResponse.getCode() == 200) {
                            message.what = 200;
                            message.obj = loginResponse;
                        }

                    }

                } else {
                    message.what = -1;

                }
                handler.sendMessage(message);
            }
        });

    }

    private void attemptAction() {
        search();
       /* // Reset errors.
        etSearchName.setError(null);
        etSearchPhone.setError(null);
        etSearchSum.setError(null);
        etSearchState.setError(null);
        // etUserAge.setError(null);
        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.

        if (TextUtils.isEmpty(mSearchPhone)) {            //如果账号为空
            etSearchPhone.setError("手机号码不能为空！");
            focusView = etSearchPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(mSearchName)) {            //如果账号为空
            etSearchName.setError("姓名不能为空！");
            focusView = etSearchName;
            cancel = true;
        }
        if (TextUtils.isEmpty(mSearchSum)) {            //如果账号为空
            etSearchSum.setError("金额不能为空！");
            focusView = etSearchSum;
            cancel = true;
        }
        if (TextUtils.isEmpty(mSearchState)) {            //如果账号为空
            etSearchState.setError("状态不能为空！");
            focusView = etSearchState;
            cancel = true;
        }
       *//* if (TextUtils.isEmpty(mCustomerAge)) {            //如果账号为空
            etUserAge.setError("年龄不能为空！");
            focusView = etUserAge;
            cancel = true;
        }*//*

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           *//* showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*//*
            search();
        }*/
    }

    private void selectState() {
        //0已受理1资质不符2待签约3已签约4已放款5银行已拒绝6审核中7待跟进 9 捣乱申请
        final String[] stateList = new String[]{
                // "审核中", "已受理", "待跟进", "待签约", "已签约", "已放款", "资质不符", "银行已拒绝", "捣乱申请"
                "请选择处理状态","已受理", "贷款资质不符", "待签约", "已签约", "银行已放款", "银行已拒绝", "审核中", "待跟进", "已上门","捣乱申请"
        };
        final OptionPicker picker = new OptionPicker(this, stateList);

        picker.setOffset(2);
        picker.setSelectedIndex(1);
        picker.setTextSize(16);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                //ToastUtils.showToast(mContext, option);
                //1.输入框显示选中的文字
                // 2.获取选中选项对象的序号
                int index;
                for(index=0;index<stateList.length;index++){
                    if (option.equals(stateList[index])) {
                       break;
                    }
                }
                if (index==0){
                    etSearchState.setText("");
                    mSearchState = "";
                }else {
                    etSearchState.setText(option);
                    mSearchState =( index -1)+ "";
                }



            }
        });
        picker.show();
    }

    private void search() {
        LogUtils.i("执行搜索请求");
        pbLoading.setVisibility(View.VISIBLE);
        LogUtils.i("uid=" + MyApplication.user.getUid());
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", MyApplication.user.getUid())
                .add("name", mSearchName)
                .add("mobile", mSearchPhone)
                .add("money", mSearchSum)
                .add("status", mSearchState)
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.LOAN_SCHEDULE_SEARCH)
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
                LogUtils.i(htmlStr);

                /*BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                Message message = handler.obtainMessage();
                if (errorResponse.getCode() == 400) {
                    message.what = 400;
                    message.obj = errorResponse;

                } else {
                    LoadScheduleResponse loginResponse = gson.fromJson(htmlStr, LoadScheduleResponse.class);
                    LogUtils.i("登录返回结果" + htmlStr);
                    if (loginResponse.getCode() == 2200) {
                        message.what = 2200;
                        message.obj = loginResponse;
                    }

                }
                handler.sendMessage(message);*/
                Message message = handler.obtainMessage();
                try {
                    LoadScheduleResponse loginResponse = gson.fromJson(htmlStr, LoadScheduleResponse.class);

                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }

                } catch (JsonSyntaxException exception) {
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

    private String convertState(String stateS) {
        //0已受理1资质不符2待签约3已签约4已放款5银行已拒绝6审核中7待跟进 9 捣乱申请
        int stateI = 0;
        if ("审核中".equals(stateS)) {
            stateI = 6;
        } else if ("已受理".equals(stateS)) {
            stateI = 0;
        } else if ("待跟进".equals(stateS)) {
            stateI = 2;
        } else if ("待签约".equals(stateS)) {
            stateI = 3;
        } else if ("已签约".equals(stateS)) {
            stateI = 4;
        } else if ("已放款".equals(stateS)) {
            stateI = 5;
        } else if ("资质不符".equals(stateS)) {
            stateI = 6;
        } else if ("银行已拒绝".equals(stateS)) {
            stateI = 7;
        } else if ("捣乱申请".equals(stateS)) {
            stateI = 6;
        }
        return stateI + "";
    }

}
