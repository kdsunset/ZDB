package cn.zhudai.zin.zhudaibao.entity;

/**
 * Created by admin on 2016/5/26.
 */
public class RegisterResultBean extends BaseBean {

    private String uid;

    private String username;

    private String sex;

    private String realname;

    private String is_ban;

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

    @Override
    public String toString() {
        return "RegisterResultBean{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", realname='" + realname + '\'' +
                ", is_ban='" + is_ban + '\'' +
                '}';
    }
}
