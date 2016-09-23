package cn.zhudai.zin.zhudaibao.entity;

import java.util.List;

/**
 * Created by admin on 2016/6/6.
 */
public class MyPartnerResponse {
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
        private List<Partner> first_partner ;

        private List<Partner> second_partner ;

        public void setFirst_partner(List<Partner> first_partner){
            this.first_partner = first_partner;
        }
        public List<Partner> getFirst_partner(){
            return this.first_partner;
        }
        public void setSecond_partner(List<Partner> second_partner){
            this.second_partner = second_partner;
        }
        public List<Partner> getSecond_partner(){
            return this.second_partner;
        }
    }
    public static class  Partner{
        private String uid;

        private String username;

        private String realname;

        private String sex;

        public void setUid(String uid){
            this.uid = uid;
        }
        public String getUid(){
            return this.uid;
        }
        public void setUsername(String username){
            this.username = username;
        }
        public String getUsername(){
            return this.username;
        }
        public void setRealname(String realname){
            this.realname = realname;
        }
        public String getRealname(){
            return this.realname;
        }
        public void setSex(String sex){
            this.sex = sex;
        }
        public String getSex(){
            return this.sex;
        }

        public Partner(String realname) {
            this.realname = realname;
        }
    }

    public MyPartnerResponse() {
    }
}
