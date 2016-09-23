package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.adapter.WithdrawCashMenuRVAdapter;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.MainMenuData;
import cn.zhudai.zin.zhudaibao.entity.WithdrawCashInfoResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.ui.DividerItemDecoration;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.StringUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

public class WithdrawCashMenuActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_withdrawcash_menu)
    RecyclerView rvWithdrawcashMenu;
    private Context mContext;

    private Handler handler=new Handler(){



        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    WithdrawCashInfoResponse respone= (WithdrawCashInfoResponse) msg.obj;
                    final WithdrawCashInfoResponse.Result result = respone.getResult();
                    final double keTixianMoney = result.getTixianmoney();
                    final double lishiTixianMoney = result.getInvitemoney();
                    double totalMoney=result.getTotalmoney();
                    List<MainMenuData> mainMenuDatas = setRecyclerData(keTixianMoney,lishiTixianMoney,totalMoney);
                    WithdrawCashMenuRVAdapter mAdapter = new WithdrawCashMenuRVAdapter(mContext,mainMenuDatas);
                    rvWithdrawcashMenu.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new WithdrawCashMenuRVAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            if (position == 0) {
                               if (keTixianMoney!=0){
                                   mContext.startActivity(new Intent(mContext, WithdrawCashActivity.class));
                               }else {
                                   ToastUtils.showToast(mContext,"可提的现金额为0！");
                               }
                            } else if (position == 1) {
                               if (lishiTixianMoney!=0){
                                   mContext.startActivity(new Intent(mContext, WithdrawRecordActivity.class));
                               }else {
                                   ToastUtils.showToast(mContext,"提现记录为空！");
                               }
                            } else if (position == 2) {
                                mContext.startActivity(new Intent(mContext, WithdrawCashTotalProfitActivity.class));
                            }
                        }
                    });
                    break;
                case 2:
                    Exception e = (Exception) msg.obj;
                    if (e instanceof java.net.UnknownHostException ) {
                        ToastUtils.showToast(mContext, "连接错误，请检查网络！");
                        finish();
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
        setContentView(R.layout.activity_withdraw_cash_menu);
        ButterKnife.bind(this);
        mContext=this;
        initView();
        //setRecyclerView();
        getDataFromNet();
    }

    private void initView() {
        initMyToolBar();
        setRecyclerView();
    }
    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("提现");
    }
    private void setRecyclerView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        //每个item高度一致，可设置为true，提高性能
        rvWithdrawcashMenu.setHasFixedSize(true);
        rvWithdrawcashMenu.setLayoutManager(mLinearLayoutManager);
        //分隔线
        rvWithdrawcashMenu.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL,
                UIUtils.dp2px(10), getResources().getColor(R.color.colorLightGray)));
        ///为每个item增加响应事件
    }
    private List<MainMenuData> setRecyclerData(double money1,double money2,double money3){
        String titile1="可提现金额：";
        String titile2="历史共提现：";
        String titile3="总计收益：";
        String detail1= StringUtils.leftoutZero(money1)+"元";
        String detail2=StringUtils.leftoutZero(money2)+"元";
        String detail3=StringUtils.leftoutZero(money3)+"元";

        int imageid1=R.drawable.ic_withdraw_240;
        int imageid2=R.drawable.ic_withdraw_record_240;
        int imageid3=R.drawable.ic_checked_240;
        MainMenuData data1=new MainMenuData(titile1,detail1,imageid1);
        MainMenuData data2=new MainMenuData(titile2,detail2,imageid2);
        MainMenuData data3=new MainMenuData(titile3,detail3,imageid3);
        List<MainMenuData> datas=new ArrayList<>();
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        return datas;
    }
    private void getDataFromNet() {
        LogUtils.i("获取数据请求");
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
                .url(ZDBURL.WITHDRAWCASH_INFO)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.obj=e;
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
                    WithdrawCashInfoResponse loginResponse = gson.fromJson(htmlStr, WithdrawCashInfoResponse.class);
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

}
