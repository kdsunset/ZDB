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
import android.widget.RatingBar;
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

public class WriteCommentActivity extends Activity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rbar_score)
    RatingBar rbarScore;
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
                case 400:
                    BaseErrorResponse errorResponse = (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext, errorResponse.getMsg());
                    break;

            }
        }
    };
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        ButterKnife.bind(this);
        mContext=this;
        initView();
        id = getIntent().getStringExtra("id");

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
        tvTitle.setText("客户经理评价");
    }

    private void getInputData() {
        mRating = rbarScore.getRating();
        mComment = etComment.getText().toString().trim();

    }
    private void postComment() {
        LogUtils.i("执行请求");
        LogUtils.i("mrationg:"+mRating);
        LogUtils.i("mcomment:"+mComment);
        LogUtils.i("id:"+id);
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", MyApplication.user.getUid())
                .add("grade", (int)mRating+"")
                .add("content", mComment)
                .add("id", id+"")
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.SUBMIT_MANAGER_COMMENT)
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
