package cn.zhudai.zin.zhudaibao.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ZIN on 2016/4/8.
 */
public class TimeUtils {

    /**
     *
     * @param mss 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {


        long days = mss / (1000 * 60 * 60 * 24);

        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

       /* if (hours<=9){

           // return "0"+ hours+":"+minutes+":"+seconds;
            if (minutes<=9){
                if (seconds<=9)
                    return "0"+ hours+":"+"0"+minutes+":"+"0"+seconds;
            }
        }*/
        StringBuffer h=new StringBuffer((hours<=9)?("0"+hours):(hours+""));
        StringBuffer m=new StringBuffer((minutes<=9)?("0"+minutes):(minutes+""));
        StringBuffer s=new StringBuffer((seconds<=9)?("0"+seconds):(seconds+""));

        return h+":"+m+":"+s;

    }

public   static class MyTime{
     public    String days;
      public   String hours;
      public  String minutes;
      public  String seconds ;

        public MyTime(String days, String hours, String minutes, String seconds) {
            this.days = days;
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }
    }

    public  static String getCurrentTime(){
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日  HH:mm:");
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        return str;
    }
    public  static  long getCurrentMillis(){
        return  System.currentTimeMillis();
    }
    public  static  long converMillisToMinute(long milis){
        return milis/1000/60;

    }
    public static String dateFormatShowAll(Long millis){
        SimpleDateFormat formatter;
        Date date=new Date(millis);
        formatter = new SimpleDateFormat ("yyyy-MM-dd KK:mm:ss ");
        String ctime = formatter.format(millis);

        return ctime;
    }
    public static String timeFormatUnix(String timestampString){
        String formats= "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString)*1000;
        String date = new SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }
}
