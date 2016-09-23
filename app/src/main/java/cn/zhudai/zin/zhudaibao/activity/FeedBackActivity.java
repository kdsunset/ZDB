package cn.zhudai.zin.zhudaibao.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;

public class FeedBackActivity extends Activity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.bt_submit)
    Button btSubmit;
    @Bind(R.id.et_comment)
    EditText etComment;
    private float mRating;
    private String mComment;

    private Context mContext;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    BaseErrorResponse respone = (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext, respone.getMsg());
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
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        mContext=this;
        initView();

    }


    private void initView() {
        initMyToolBar();
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputData();
                postComment();
            }
        });

    }

    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("意见反馈");
    }

    private void getInputData() {

        mComment = etComment.getText().toString().trim();

    }
    private void postComment() {
        LogUtils.i("执行请求");

        LogUtils.i("mcomment:"+mComment);

        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", MyApplication.user.getUid())

                .add("content", mComment)

                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.FEEDBACK)
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
                Message message = handler.obtainMessage();
                try {
                    BaseErrorResponse loginResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);

                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }else {   //恰好与BaseErrorResponse相同
                        if (loginResponse.getCode() == 400) {
                            message.what = 400;
                            message.obj = loginResponse;
                        }
                    }

                }catch (JsonSyntaxException exception){

                }


                handler.sendMessage(message);
            }
        });


    }
}
