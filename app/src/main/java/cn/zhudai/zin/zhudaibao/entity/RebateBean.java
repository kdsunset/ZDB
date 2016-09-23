package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/6/21.
 */
public class RebateBean {
    private String key;
    private String val;

    public RebateBean(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
