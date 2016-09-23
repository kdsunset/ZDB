package cn.zhudai.zin.zhudaibao.entity;

import java.util.List;

/**
 * Created by admin on 2016/6/3.
 */
public class WithdrawCashRecordResponse {
    private int code;

    private List<Result> result ;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setResult(List<Result> result){
        this.result = result;
    }
    public List<Result> getResult(){
        return this.result;
    }
    public class Result{
        private String money;

        private String cardnumber;

        private String invitetime;

        private String status;

        public void setMoney(String money){
            this.money = money;
        }
        public String getMoney(){
            return this.money;
        }
        public void setCardnumber(String cardnumber){
            this.cardnumber = cardnumber;
        }
        public String getCardnumber(){
            return this.cardnumber;
        }
        public void setInvitetime(String invitetime){
            this.invitetime = invitetime;
        }
        public String getInvitetime(){
            return this.invitetime;
        }
        public void setStatus(String status){
            this.status = status;
        }
        public String getStatus(){
            return this.status;
        }
    }
}
