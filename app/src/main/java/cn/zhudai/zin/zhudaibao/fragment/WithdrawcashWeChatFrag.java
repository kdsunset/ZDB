package cn.zhudai.zin.zhudaibao.fragment;

import android.view.View;

/**
 * Created by admin on 2016/6/1.
 */
public class WithdrawcashWeChatFrag extends WithdrawcashBaseFrag{
    @Override
    protected void initView() {
        etUserRealname.setVisibility(View.GONE);
        etUserAccount.setHint("请输入微信号");

        etWithdrawAmount.setHint("请输入提现金额，不能小于1000元！");
        etBankName.setVisibility(View.GONE);
        etUserPhone.setHint("请输入联系电话");
    }

    @Override
    public int getWithdrawCashMethod() {
        return WITHDRAWCASH_BYWECHAT;
    }
}
