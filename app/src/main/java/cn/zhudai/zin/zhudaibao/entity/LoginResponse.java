package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/26.
 */
public class LoginResponse {
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
        private String uid;

        private String username;

        private String sex;

        private String realname;

        private String is_ban;

        private String is_status;

        private String is_applied;

        public void setUid(String uid){
            this.uid = uid;
        }
        public String getUid(){
            return this.uid;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "uid='" + uid + '\'' +
                    ", username='" + username + '\'' +
                    ", sex='" + sex + '\'' +
                    ", realname='" + realname + '\'' +
                    ", is_ban='" + is_ban + '\'' +
                    ", is_status='" + is_status + '\'' +
                    ", is_applied='" + is_applied + '\'' +
                    '}';
        }

        public void setUsername(String username){
            this.username = username;
        }
        public String getUsername(){
            return this.username;
        }
        public void setSex(String sex){
            this.sex = sex;
        }
        public String getSex(){
            return this.sex;
        }
        public void setRealname(String realname){
            this.realname = realname;
        }
        public String getRealname(){
            return this.realname;
        }
        public void setIs_ban(String is_ban){
            this.is_ban = is_ban;
        }
        public String getIs_ban(){
            return this.is_ban;
        }
        public void setIs_status(String is_status){
            this.is_status = is_status;
        }
        public String getIs_status(){
            return this.is_status;
        }
        public void setIs_applied(String is_applied){
            this.is_applied = is_applied;
        }
        public String getIs_applied(){
            return this.is_applied;
        }

    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "code=" + code +
                ", result=" + result.toString() +
                '}';
    }
}
