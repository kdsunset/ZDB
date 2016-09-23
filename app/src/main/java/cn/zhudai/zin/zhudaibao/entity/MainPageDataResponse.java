package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/31.
 */
public class MainPageDataResponse {
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
        private int result;

        private int rebate_rate;

        private double first_parter_rebate_rate;

        private double second_parter_rebater_rate;

        private String statu1;

        private String statu2;

        private double yongjing;

        public void setResult(int result){
            this.result = result;
        }
        public int getResult(){
            return this.result;
        }
        public void setRebate_rate(int rebate_rate){
            this.rebate_rate = rebate_rate;
        }
        public int getRebate_rate(){
            return this.rebate_rate;
        }
        public void setFirst_parter_rebate_rate(double first_parter_rebate_rate){
            this.first_parter_rebate_rate = first_parter_rebate_rate;
        }
        public double getFirst_parter_rebate_rate(){
            return this.first_parter_rebate_rate;
        }
        public void setSecond_parter_rebater_rate(double second_parter_rebater_rate){
            this.second_parter_rebater_rate = second_parter_rebater_rate;
        }
        public double getSecond_parter_rebater_rate(){
            return this.second_parter_rebater_rate;
        }
        public void setStatu1(String statu1){
            this.statu1 = statu1;
        }
        public String getStatu1(){
            return this.statu1;
        }
        public void setStatu2(String statu2){
            this.statu2 = statu2;
        }
        public String getStatu2(){
            return this.statu2;
        }
        public void setYongjing(double yongjing){
            this.yongjing = yongjing;
        }
        public double getYongjing(){
            return this.yongjing;
        }
    }
}
