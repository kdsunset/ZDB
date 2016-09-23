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
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.adapter.NoticeRVAdapter;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.NoticeListResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.ui.DividerItemDecoration;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

public class NoticeListActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_notices)
    RecyclerView rvNotices;
    private Context mContext;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 200:
                    NoticeListResponse respone= (NoticeListResponse) msg.obj;
                    List<NoticeListResponse.Result> noticeList = respone.getResult();

                    NoticeRVAdapter adapter=new NoticeRVAdapter(mContext,noticeList);
                    rvNotices.setAdapter(adapter);
                    adapter.setOnItemClickListener(new NoticeRVAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, NoticeListResponse.Result data) {
                            String id=data.getId();
                            Intent intent=new Intent(mContext,NoticeDetailActivity.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }
                    });


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
        setContentView(R.layout.activity_notice_list);
        ButterKnife.bind(this);
        mContext=this;
        initView();
        getDataFromNet();
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
        tvTitle.setText("通知");
    }
    private void initRecyclerView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
       /* MainMenuAdapter mAdapter = new MainMenuAdapter(mContext ，mainMenuDatas, userInfoStatus);
        mAdapter.setOnItemClickListener(new MainMenuAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, MainMenuData data) {

            }
        });
        rvNotices.setAdapter(mAdapter);
        */
        //每个item高度一致，可设置为true，提高性能
        rvNotices.setHasFixedSize(true);
        rvNotices.setLayoutManager(mLinearLayoutManager);
        //分隔线
        rvNotices.addItemDecoration(new DividerItemDecoration(mContext, LinearLayout.VERTICAL,
                UIUtils.dp2px(1), getResources().getColor(R.color.colorLightGray)));
        ///为每个item增加响应事件
    }
    private void getDataFromNet() {
        LogUtils.i("获取通知列表");
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        String url= ZDBURL.NOTICE_LIST;
        LogUtils.i(url);
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call

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
                    NoticeListResponse loginResponse = gson.fromJson(htmlStr, NoticeListResponse.class);

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
