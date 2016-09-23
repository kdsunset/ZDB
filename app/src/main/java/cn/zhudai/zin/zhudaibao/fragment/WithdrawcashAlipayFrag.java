package cn.zhudai.zin.zhudaibao.fragment;

import android.view.View;

/**
 * Created by admin on 2016/6/1.
 */
public class WithdrawcashAlipayFrag extends WithdrawcashBaseFrag{
    @Override
    protected void initView() {
        etUserRealname.setHint("请输入真实姓名");
        etUserAccount.setHint("请输入支付宝账号");
        etWithdrawAmount.setHint("请输入提现金额，不能小于1000元！");
        etBankName.setVisibility(View.GONE);

        etUserPhone.setHint("请输入联系电话");
    }

    @Override
    public int getWithdrawCashMethod() {
        return WITHDRAWCASH_BYALIPAY;
    }
}
