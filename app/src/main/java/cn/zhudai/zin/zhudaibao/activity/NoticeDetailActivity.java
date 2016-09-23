package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.NoticeDetailResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.StringUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;

public class NoticeDetailActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_title_detail)
    TextView tvTitleDetail;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.ll_body)
    LinearLayout llBody;
    private Context mContext;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200: {
                    NoticeDetailResponse respone = (NoticeDetailResponse) msg.obj;
                    NoticeDetailResponse.Result result = respone.getResult();
                    String addtime = result.getAddtime();
                    String author = result.getAuthor();
                    String content = result.getContent();
                    String id = result.getId();
                    String title = result.getTitle();
                    CharSequence charSequenceDetail = Html.fromHtml(content);
                    tvTitleDetail.setText(title);
                    tvContent.setText(charSequenceDetail);
                    String[] split = StringUtils.split(addtime, " ");
                    String date=split[0];
                    tvDate.setText(date);


                    break;
                }
                case 400:
                    BaseErrorResponse response = (BaseErrorResponse) msg.obj;
                    String s = response.getMsg();
                    ToastUtils.showToast(mContext, s);
                    break;
            }
        }
    };
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        mContext = this;
        id = getIntent().getStringExtra("id");
        initView();
        getNoticeDetail();
    }

    private void initView() {
        initMyToolBar();
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

    private void getNoticeDetail() {
        LogUtils.i("获取数据请求");

        //http://www.zhudai.cn/index.php/app/notice/detail?id=通知ID
        String url = ZDBURL.NOTICE_DETAIL + "?" + "id=" + id;
        LogUtils.i(url);
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        Call call = client.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String htmlStr = response.body().string();
                LogUtils.i("返回结果" + htmlStr);
                Gson gson = new Gson();
                Message message = handler.obtainMessage();
                try {
                    NoticeDetailResponse loginResponse = gson.fromJson(htmlStr, NoticeDetailResponse.class);

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
