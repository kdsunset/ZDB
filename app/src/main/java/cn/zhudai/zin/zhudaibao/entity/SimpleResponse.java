package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/6/3.
 */
public class SimpleResponse {
    private int code;

    private String result;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setResult(String result){
        this.result = result;
    }
    public String getResult(){
        return this.result;
    }
}
