package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/25.
 */
public class GetCodeBean {
    private int code;

    private String msg;

    private int is_register;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setIs_register(int is_register){
        this.is_register = is_register;
    }
    public int getIs_register(){
        return this.is_register;
    }
}
