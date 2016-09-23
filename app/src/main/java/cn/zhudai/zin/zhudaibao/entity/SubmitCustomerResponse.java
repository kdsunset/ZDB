package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/31.
 */
public class SubmitCustomerResponse {
    private int code;

    public SubmitCustomerResponse() {
    }

    public SubmitCustomerResponse(int code, String result) {

        this.code = code;
        this.result = result;
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private String result;
}
