package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/26.
 */
public class CheckCodeRespond {
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
  public  class Result{
        private String uname;

        private String is_register;

        public void setUname(String uname){
            this.uname = uname;
        }
        public String getUname(){
            return this.uname;
        }
        public void setIs_register(String is_register){
            this.is_register = is_register;
        }
        public String getIs_register(){
            return this.is_register;
        }
    }


}



