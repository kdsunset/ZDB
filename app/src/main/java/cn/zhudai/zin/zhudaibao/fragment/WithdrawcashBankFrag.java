package cn.zhudai.zin.zhudaibao.fragment;

import android.text.InputType;

/**
 * Created by admin on 2016/6/1.
 */
public class WithdrawcashBankFrag extends WithdrawcashBaseFrag {

    @Override
    protected void initView() {
        etUserRealname.setHint("请输入真实姓名");
        etWithdrawAmount.setHint("请输入提现金额，不能小于1000元！");
        etBankName.setHint("请输入银行名称");
        etUserAccount.setHint("请输入银行卡卡号");
        etUserAccount.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
        etUserPhone.setHint("请输入联系电话");


    }

    @Override
    public int getWithdrawCashMethod() {
        return WITHDRAWCASH_BYBANK;
    }
}
