package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.fragment.ShowQRCodeFrag;

public class ShowQRCodeActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.fl_content)
    FrameLayout flContent;
    private Context mContext;
    private ShowQRCodeFrag showQRCodeFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);
        ButterKnife.bind(this);
        mContext=this;
        initView();
    }

    private void initView() {
        initMyToolBar();
        setDefaultFragment();

    }
    private void initMyToolBar() {
        llBackArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("贷款申请专属二维码");
    }
    private void setDefaultFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm
                .beginTransaction();
        showQRCodeFrag = new ShowQRCodeFrag();
        transaction.replace(R.id.fl_content, showQRCodeFrag);
        transaction.commit();
    }
}
