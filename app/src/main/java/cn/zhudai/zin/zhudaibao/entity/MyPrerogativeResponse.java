package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/31.
 */
public class MyPrerogativeResponse {
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
        private String loan_total;

        private String rebate_rate;

        private String single_award;

        public void setLoan_total(String loan_total){
            this.loan_total = loan_total;
        }
        public String getLoan_total(){
            return this.loan_total;
        }
        public void setRebate_rate(String rebate_rate){
            this.rebate_rate = rebate_rate;
        }
        public String getRebate_rate(){
            return this.rebate_rate;
        }
        public void setSingle_award(String single_award){
            this.single_award = single_award;
        }
        public String getSingle_award(){
            return this.single_award;
        }

    }
}
