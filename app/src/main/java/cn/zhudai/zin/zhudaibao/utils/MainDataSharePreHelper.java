package cn.zhudai.zin.zhudaibao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZIN on 2016/4/19.
 */
public class MainDataSharePreHelper {

    Context mContex;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public MainDataSharePreHelper(Context mContex) {
        this.mContex = mContex;
        //私有数据
            sharedPreferences = mContex. getSharedPreferences("ACTIVITY_MAIN_DATA", Context.MODE_PRIVATE);
        //获取编辑器
        editor = sharedPreferences.edit();
    }

    /* private int userInfoStatus;
    private int rate=1;
    private double first_parter_rebate_rate=0.22;
    private double second_parter_rebater_rate=0.22;
    private String statu1;
    private String statu2;
    private Double yongjing;*/
    public static final String USER_INFO_STATUS="USER_INFO_STATUS";
    public static final String RATE="RATE";
    public static final String FIRST_PARTER_REBATE_RATE="FIRST_PARTER_REBATE_RATE";
    public static final String SECOND_PARTER_REBATE_RATE="SECOND_PARTER_REBATE_RATE";
    public static final String STATU1="STATU1";
    public static final String STATU2="STATU2";
    public static final String YONGJING="YONGJING";

    public int getIntValue(String key,int defValue){
        return sharedPreferences.getInt(key,defValue);
    }
    public void saveIntValue(String key,int value){
        editor.putInt(key,value);
        editor.commit();
    }
    public String getStringValue(String key,String defValue){
        return sharedPreferences.getString(key,defValue);
    }
    public void saveStringValue(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }
    public double getDoubleValue(String key, double defValue){
        try {
            String s = String.valueOf(defValue);
            String string = sharedPreferences.getString(key, s);
            return Double.valueOf(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }

    }
    public void saveDoubleValue(String key, double value){
        try {
            String s = String.valueOf(value);
            editor.putString(key,s);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public  void cleanLoginStatus(){
        editor.clear();
        editor.commit();
    }
}
