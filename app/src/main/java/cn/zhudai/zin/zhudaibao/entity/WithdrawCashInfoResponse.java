package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/6/1.
 */
public class WithdrawCashInfoResponse {
    private int code;

    private Result result;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setResult(Result result){
        this.result = result;
    }
    public Result getResult(){
        return this.result;
    }
    public class Result{
        private double tixianmoney;

        private double totalmoney;

        private int invitemoney;

        public void setTixianmoney(double tixianmoney){
            this.tixianmoney = tixianmoney;
        }
        public double getTixianmoney(){
            return this.tixianmoney;
        }
        public void setTotalmoney(double totalmoney){
            this.totalmoney = totalmoney;
        }
        public double getTotalmoney(){
            return this.totalmoney;
        }
        public void setInvitemoney(int invitemoney){
            this.invitemoney = invitemoney;
        }
        public int getInvitemoney(){
            return this.invitemoney;
        }

    }
}
