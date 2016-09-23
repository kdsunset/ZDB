package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
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
import com.google.gson.JsonSyntaxException;
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
import cn.zhudai.zin.zhudaibao.adapter.WithdrawCashProfitRVAdapter;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.WithdrawCashProfitResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.ui.DividerItemDecoration;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.StringUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

public class WithdrawCashTotalProfitActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_withdrawcash_profit)
    RecyclerView rvWithdrawcashRecord;
    @Bind(R.id.tv_became_member_reward)
    TextView tvBecameMemberReward;
    @Bind(R.id.tv_invite_friends_reward)
    TextView tvInviteFriendsReward;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    private Context mContext;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    WithdrawCashProfitResponse respone = (WithdrawCashProfitResponse) msg.obj;
                    WithdrawCashProfitResponse.Result result = respone.getResult();
                    String register_prize_total = result.getRegister_prize_total();
                    String invite_prize_total = result.getInvite_prize_total();
                    String prize_total = result.getPrize_total();
                    List<WithdrawCashProfitResponse.Tixianmsg> datalist = result.getTixianmsg();
                    rvWithdrawcashRecord.setAdapter(new WithdrawCashProfitRVAdapter(mContext, datalist));
                    rvWithdrawcashRecord.setEnabled(false);
                    tvBecameMemberReward.setText("成为会员奖励："+ StringUtils.leftoutZero(register_prize_total)+"元");
                    tvInviteFriendsReward.setText("邀请好友奖励："+StringUtils.leftoutZero(invite_prize_total)+"元");
                    tvTotal.setText("总计："+StringUtils.leftoutZero(prize_total)+"元");
                    break;
                case 400:
                    BaseErrorResponse errorResponse = (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext, errorResponse.getMsg());
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_profit);
        ButterKnife.bind(this);
        mContext = this;
        initView();

        getWithdrawCashProfit();
    }

    private void initView() {
        initMyToolBar();
        initRecyclerView();

    }

    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("收益详情");
    }

    private void initRecyclerView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        // WithdrawCashMenuRVAdapter mAdapter = new WithdrawCashMenuRVAdapter(mContext,mainMenuDatas);
        //rvWithdrawcashMenu.setAdapter(mAdapter);
        //每个item高度一致，可设置为true，提高性能
        rvWithdrawcashRecord.setHasFixedSize(true);
        rvWithdrawcashRecord.setLayoutManager(mLinearLayoutManager);
        rvWithdrawcashRecord.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL,
                UIUtils.dp2px(1), UIUtils.getColorFromRes(R.color.colorGray)));
    }

    private void getWithdrawCashProfit() {
        LogUtils.i("执行请求");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", MyApplication.user.getUid())
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.WITHDRAWCASH_PROFIT)
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
                LogUtils.i("返回结果" + htmlStr);
                Gson gson = new Gson();
                Message message = handler.obtainMessage();
                try {
                    WithdrawCashProfitResponse loginResponse = gson.fromJson(htmlStr, WithdrawCashProfitResponse.class);

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
}
