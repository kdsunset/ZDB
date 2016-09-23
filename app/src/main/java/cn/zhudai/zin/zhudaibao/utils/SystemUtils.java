package cn.zhudai.zin.zhudaibao.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by admin on 2016/6/24.
 */
public class SystemUtils {
    /**
     * 扫描、刷新相册
     */
    public static void scanPhotos(Context context,String filePath) {
        Intent intent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
    /**
     * 检测网络是否连接
     * @return
     */
    public static boolean checkNetworkState(Context context) {
        boolean flag=false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            //去进行判断网络是否连接
            Log.i("net","去进行判断网络是否连接");
            if (manager.getActiveNetworkInfo() != null) {
                flag = manager.getActiveNetworkInfo().isAvailable();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    //版本号
    public static int getVersionCode( ) {
        return android.os.Build.VERSION.SDK_INT;
    }

}
