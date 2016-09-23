package cn.zhudai.zin.zhudaibao.entity;

import java.util.List;

/**
 * Created by admin on 2016/6/1.
 */
public class LoadScheduleResponse {
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
        private String managerid;

        private String kefuid;

        private String cityid;

        private String truename;

        private String loan_money;

        private String mobile;

        private String time;

        private String status;

        private String nickname;

        private String m_mobile;

        public void setManagerid(String managerid){
            this.managerid = managerid;
        }
        public String getManagerid(){
            return this.managerid;
        }
        public void setKefuid(String kefuid){
            this.kefuid = kefuid;
        }
        public String getKefuid(){
            return this.kefuid;
        }
        public void setCityid(String cityid){
            this.cityid = cityid;
        }
        public String getCityid(){
            return this.cityid;
        }
        public void setTruename(String truename){
            this.truename = truename;
        }
        public String getTruename(){
            return this.truename;
        }
        public void setLoan_money(String loan_money){
            this.loan_money = loan_money;
        }
        public String getLoan_money(){
            return this.loan_money;
        }
        public void setMobile(String mobile){
            this.mobile = mobile;
        }
        public String getMobile(){
            return this.mobile;
        }
        public void setTime(String time){
            this.time = time;
        }
        public String getTime(){
            return this.time;
        }
        public void setStatus(String status){
            this.status = status;
        }
        public String getStatus(){
            return this.status;
        }
        public void setNickname(String nickname){
            this.nickname = nickname;
        }
        public String getNickname(){
            return this.nickname;
        }
        public void setM_mobile(String m_mobile){
            this.m_mobile = m_mobile;
        }
        public String getM_mobile(){
            return this.m_mobile;
        }
    }
}
