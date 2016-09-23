package cn.zhudai.zin.zhudaibao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZIN on 2016/4/19.
 */
public class LoginStatusSharePreHelper {

    Context mContex;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public LoginStatusSharePreHelper(Context mContex) {
        this.mContex = mContex;
        //私有数据
            sharedPreferences = mContex. getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
        //获取编辑器
        editor = sharedPreferences.edit();
    }

    public boolean getIsRemember(){
        return sharedPreferences.getBoolean("ISREMEMBER",false);
    }
    public void saveIsRemember(boolean isRemember){
        editor.putBoolean("ISREMEMBER", isRemember);

        editor.commit();//提交修改
    }

    public String  getUserPhone(){

        return sharedPreferences.getString("USERPHONE","");
    }
    public void saveUserPhone(String phone){
        editor.putString("USERPHONE", phone);

        editor.commit();//提交修改
    }
    public String getUid(){
        return sharedPreferences.getString("UID","");
    }
    public void saveUid(String uid){
        editor.putString("UID",uid);
        editor.commit();
    }
    public String getUserRealName(){
        return sharedPreferences.getString("REALNAME","");
    }
    public void saveUserRealName(String name){
        editor.putString("REALNAME",name);
        editor.commit();
    }
    public String getUserInvitationCode(){
        return sharedPreferences.getString("INVITATIONCODE","");
    }
    public void saveUserInvitationCode(String code){
        editor.putString("INVITATIONCODE",code);
        editor.commit();
    }
    public void  saveUserPwd(String pwd){
        editor.putString("USERPWD", pwd);

        editor.commit();//提交修改
    }
    public String  getUserPwd(){

        return sharedPreferences.getString("USERPWD","");
    }

    public void  saveheadImg(String headImg){
        editor.putString("USERNAME", headImg);

        editor.commit();//提交修改
    }

    public void  updateLastLoginTime(Long lastLoginTime){
       // long lastLoginTime=TimeUtils.getCurrentMillis();
        editor.putLong("LASTLOGINTIME", lastLoginTime);
        editor.commit();//提交修改
        LogUtils.i("LASTLOGINTIME保存值"+lastLoginTime);
       // Toast.makeText(mContex,"LASTLOGINTIME取出值"+lastLoginTime,Toast.LENGTH_SHORT).show();
    }
    public Long  getLastLoginTime(){
        long lastlogintime = sharedPreferences.getLong("LASTLOGINTIME", 0);
        LogUtils.i("LASTLOGINTIME取出值"+lastlogintime);
       // Toast.makeText(mContex,"LASTLOGINTIME取出值"+lastlogintime,Toast.LENGTH_SHORT).show();
        return sharedPreferences.getLong("LASTLOGINTIME",Integer.MAX_VALUE);
    }

    public String  getheadImg(){

        return sharedPreferences.getString("HEADIMG","");
    }



    public  boolean isLoginStatusValid( Long lastlogintime){
            boolean loginstatus;
            Long currentMillis = TimeUtils.getCurrentMillis();

           // long lastlogintime = this.getLastLoginTime();
            Long days=(currentMillis-lastlogintime)/1000/60/60/24;
            if (days<15){           //
                loginstatus=  true;
            }else {
                loginstatus=  false;
            }
        boolean s=loginstatus;
        LogUtils.i(""+Boolean.toString(s));
        return loginstatus;
    }

    public  void cleanLoginStatus(){
        editor.clear();
        editor.commit();
    }
}
