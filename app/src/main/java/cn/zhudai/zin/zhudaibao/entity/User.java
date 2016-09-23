package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/30.
 */
public class User {
    private String phone;
    private String realName;
    private String uid;
    private long lastLoginTime;

    public User(String phone, String realName, String uid, Long lastLoginTime) {
        this.phone = phone;
        this.realName = realName;
        this.uid = uid;
        this.lastLoginTime = lastLoginTime;
    }

    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
