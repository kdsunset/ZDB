package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/26.
 */
public class BaseErrorResponse {
    private int code;

    private String msg;

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

    @Override
    public String toString() {
        return "BaseErrorResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
