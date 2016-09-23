package cn.zhudai.zin.zhudaibao.activity;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import cn.zhudai.zin.zhudaibao.fragment.SubmitCustomerInfoFrag;

public class SubmitCustomerInfoParentActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_submit)
    LinearLayout llSubmit;
    @Bind(R.id.ll_qrcode)
    LinearLayout llQrcode;
    @Bind(R.id.fl_content)
    FrameLayout flContent;
    Fragment submitInfoFrag;
    Fragment showQRCodeFrag;
    private FragmentManager mFm;
    private FragmentTransaction mTransaction;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_customer_info_parent);
        ButterKnife.bind(this);
        mContext=this;
        initView();
    }

    private void initView() {
        initMyToolBar();
        setDefaultFragment();
        initSelectItem();

    }

    private void initSelectItem() {
        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm
                        .beginTransaction();
                        /*.setCustomAnimations(
                                R.anim.fragment_slide_right_out, R.anim.fragment_slide_left_out,
                                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);*/
                llSubmit.setBackgroundColor(Color.WHITE);
                llQrcode.setBackgroundColor(Color.parseColor("#F2F2F2"));
                tvTitle.setText("提交客户资料");
                if (submitInfoFrag == null)
                {
                    submitInfoFrag = new SubmitCustomerInfoFrag();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.fl_content, submitInfoFrag);
                transaction.commit();
            }
        });
        llQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llQrcode.setBackgroundColor(Color.WHITE);
                llSubmit.setBackgroundColor(Color.parseColor("#F2F2F2"));
                tvTitle.setText("贷款申请专属二维码");
                FragmentManager fm = getSupportFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm
                        .beginTransaction();
                       /* .setCustomAnimations(
                                R.anim.fragment_slide_right_out, R.anim.fragment_slide_left_out,
                                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);*/

                if (showQRCodeFrag == null)
                {
                    showQRCodeFrag = new ShowQRCodeFrag();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.fl_content, showQRCodeFrag);
                transaction.commit();
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
        tvTitle.setText("提交客户资料");
    }
    private void setDefaultFragment()
    {
        mFm = getSupportFragmentManager();
        mTransaction = mFm.beginTransaction();
        submitInfoFrag = new SubmitCustomerInfoFrag();
        mTransaction.replace(R.id.fl_content, submitInfoFrag);
        mTransaction.commit();
    }

}
