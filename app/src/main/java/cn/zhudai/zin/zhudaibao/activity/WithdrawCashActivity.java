package cn.zhudai.zin.zhudaibao.activity;

import android.graphics.Color;
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
import cn.zhudai.zin.zhudaibao.fragment.WithdrawcashAlipayFrag;
import cn.zhudai.zin.zhudaibao.fragment.WithdrawcashBankFrag;
import cn.zhudai.zin.zhudaibao.fragment.WithdrawcashWeChatFrag;

public class WithdrawCashActivity extends AppCompatActivity {

    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.ll_item_bankcard)
    LinearLayout llItemBankcard;
    @Bind(R.id.ll_item_alipay)
    LinearLayout llItemAlipay;
    @Bind(R.id.ll_item_wechat)
    LinearLayout llItemWechat;
    @Bind(R.id.fl_content)
    FrameLayout flContent;
    private WithdrawcashBankFrag withdrawcashBankFrag;
    private WithdrawcashAlipayFrag withdrawcashAlipayFrag;
    private WithdrawcashWeChatFrag withdrawcashWeChatFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_cash);
        ButterKnife.bind(this);
        initView();
        setDefaultFragment();
        initSelectItem();
    }

    private void initView() {
        initMyToolBar();

    }

    private void initSelectItem() {
        llItemBankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm
                        .beginTransaction();
                        /*.setCustomAnimations(
                                R.anim.fragment_slide_right_out, R.anim.fragment_slide_left_out,
                                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);*/
                llItemBankcard.setBackgroundColor(Color.WHITE);
                llItemAlipay.setBackgroundColor(Color.parseColor("#F2F2F2"));
                llItemWechat.setBackgroundColor(Color.parseColor("#F2F2F2"));
                if (withdrawcashBankFrag == null)
                {
                    withdrawcashBankFrag = new WithdrawcashBankFrag();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.fl_content, withdrawcashBankFrag);
                transaction.commit();
            }
        });
        llItemAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llItemAlipay.setBackgroundColor(Color.WHITE);
                llItemBankcard.setBackgroundColor(Color.parseColor("#F2F2F2"));
                llItemWechat.setBackgroundColor(Color.parseColor("#F2F2F2"));
                FragmentManager fm = getSupportFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm
                        .beginTransaction();
                       /* .setCustomAnimations(
                                R.anim.fragment_slide_right_out, R.anim.fragment_slide_left_out,
                                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);*/

                if (withdrawcashAlipayFrag == null)
                {
                    withdrawcashAlipayFrag = new WithdrawcashAlipayFrag();
                }
                transaction.replace(R.id.fl_content, withdrawcashAlipayFrag);
                transaction.commit();
            }
        });
        llItemWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llItemWechat.setBackgroundColor(Color.WHITE);
                llItemBankcard.setBackgroundColor(Color.parseColor("#F2F2F2"));
                llItemAlipay.setBackgroundColor(Color.parseColor("#F2F2F2"));
                FragmentManager fm = getSupportFragmentManager();
                // 开启Fragment事务
                FragmentTransaction transaction = fm
                        .beginTransaction();
                       /* .setCustomAnimations(
                                R.anim.fragment_slide_right_out, R.anim.fragment_slide_left_out,
                                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);*/

                if (withdrawcashWeChatFrag == null)
                {
                    withdrawcashWeChatFrag = new WithdrawcashWeChatFrag();
                }
                transaction.replace(R.id.fl_content, withdrawcashWeChatFrag);
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
        tvTitle.setText("申请提现");
    }
    private void setDefaultFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        withdrawcashBankFrag = new WithdrawcashBankFrag();
        transaction.replace(R.id.fl_content, withdrawcashBankFrag);
        transaction.commit();
    }
}
