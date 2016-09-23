package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
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
import cn.zhudai.zin.zhudaibao.adapter.RebateRVAdapter;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.MyPrerogativeResponse;
import cn.zhudai.zin.zhudaibao.entity.RebateBean;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.StringUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;

public class MyPrerogativeActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_Loan_amount_sum)
    TextView tvLoanAmountSum;
    @Bind(R.id.tv_rebate)
    TextView tvRebate;
    @Bind(R.id.tv_reward)
    TextView tvReward;
    @Bind(R.id.lv_laodamount)
    RecyclerView lvLaodamount;
    private Context mContext;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    MyPrerogativeResponse respone= (MyPrerogativeResponse) msg.obj;
                    final MyPrerogativeResponse.Result result = respone.getResult();
                    String rate = result.getRebate_rate();
                    String money = result.getSingle_award();

                    double moneyInt=Double.valueOf(money);
                    LogUtils.i("getSingle_award="+money);
                    TextPaint tp = tvLoanAmountSum.getPaint();
                    tp.setFakeBoldText(true);
                    tvLoanAmountSum.setText(result.getLoan_total());
                    tvRebate.setText(StringUtils.leftoutZero(rate)+"%");
                    tvReward.setText(StringUtils.leftoutZero(money)+"元");



                    break;
                case 2:
                    Exception e = (Exception) msg.obj;
                    if (e instanceof java.net.UnknownHostException ) {
                        ToastUtils.showToast(mContext, "连接错误，请检查网络！");
                        //finish();
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
        setContentView(R.layout.activity_my_prerogative);
        ButterKnife.bind(this);
        mContext=this;
        initView();

    }

    private void initView() {
        initMyToolBar();
        initRecyclerView();
        getMyPrerogative();



    }
    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("我的特权");
    }
    private void getMyPrerogative() {
        LogUtils.i("获取我的特权的请求");
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
                .url(ZDBURL.PERSONAL_PRIVILEGE)
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
                    MyPrerogativeResponse loginResponse = gson.fromJson(htmlStr, MyPrerogativeResponse.class);
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
    private void initRecyclerView( ) {
       LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        lvLaodamount.setHasFixedSize(true);
        lvLaodamount.setLayoutManager(mLinearLayoutManager);
        /*recyclerView.addItemDecoration(new di(mContext, LinearLayout.VERTICAL,
                UIUtils.dip2px(1), UIUtils.getColorFromRes(R.color.colorGray)));*/
        List<RebateBean> data=new ArrayList<>();
        RebateBean r0=new RebateBean("贷款成功客户级","返点");
        RebateBean r1= new RebateBean("自己提交的客户","1%");
        RebateBean r2= new RebateBean("一级合伙人提交的客户","0.3%");
        RebateBean r3 =new RebateBean("二级合伙人提交的客户","0.09%");
        data.add(r0);
        data.add(r1);
        data.add(r2);
        data.add(r3);
        lvLaodamount.setAdapter(new RebateRVAdapter(mContext,data));
        lvLaodamount.setEnabled(false);

    }
}
