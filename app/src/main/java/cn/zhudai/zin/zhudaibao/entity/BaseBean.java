package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/26.
 */
public class BaseBean {
    private int code;

    private RegisterResultBean result;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setResult(RegisterResultBean result){
        this.result = result;
    }
    public RegisterResultBean getResult(){
        return this.result;
    }
}
