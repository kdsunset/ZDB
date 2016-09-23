package cn.zhudai.zin.zhudaibao.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
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
import cn.zhudai.zin.zhudaibao.adapter.MyPartnerRVAdapter;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.MyPartnerResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;

public class MyPartnerActivity extends Activity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_lever1_partner_sum)
    TextView tvLever1PartnerSum;
    @Bind(R.id.rv_lever1_partner)
    RecyclerView rvLever1Partner;
    @Bind(R.id.tv_lever2_partner_sum)
    TextView tvLever2PartnerSum;
    @Bind(R.id.rv_lever2_partner)
    RecyclerView rvLever2Partner;
    private Context mContext;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    LogUtils.i("case:200");
                    MyPartnerResponse respone = (MyPartnerResponse) msg.obj;
                    MyPartnerResponse.Result result = respone.getResult();
                    List<MyPartnerResponse.Partner> first_partner = result.getFirst_partner();
                    List<MyPartnerResponse.Partner> second_partner = result.getSecond_partner();
                    tvLever1PartnerSum.setText(first_partner.size()+"位");
                    tvLever2PartnerSum.setText(second_partner.size()+"位");
                    MyPartnerRVAdapter adapter1=new MyPartnerRVAdapter(mContext,first_partner);
                    MyPartnerRVAdapter adapter2=new MyPartnerRVAdapter(mContext,second_partner);
                    rvLever1Partner.setAdapter(adapter1);
                    rvLever2Partner.setAdapter(adapter2);
                    rvLever1Partner.setEnabled(false);
                    rvLever2Partner.setEnabled(false);
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
        setContentView(R.layout.activity_my_partner);
        ButterKnife.bind(this);
        mContext=this;
        initView();

        getMyPartnerList();
    }
    private void initView() {
        initMyToolBar();
        initRecyclerView(rvLever1Partner);
        initRecyclerView(rvLever2Partner);

    }
    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("我的合伙人");
    }
    private void initRecyclerView(RecyclerView recyclerView) {
        //LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
       // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);


    }
    private void getMyPartnerList() {
        LogUtils.i("执行请求");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", MyApplication.user.getUid())
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.MY_PARTNER)
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
                LogUtils.i("登录返回结果" + htmlStr);
                Gson gson = new Gson();
                Message message = handler.obtainMessage();
                try {
                    MyPartnerResponse loginResponse = gson.fromJson(htmlStr, MyPartnerResponse.class);

                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }

                }catch (JsonSyntaxException exception){
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
