package cn.zhudai.zin.zhudaibao.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2016/5/25.
 */
public class ToastUtils {
    public static void showToast(Context context,String string){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show();
    }
    public static void showToastLong(Context context,String string){
        Toast.makeText(context, string,Toast.LENGTH_LONG).show();
    }
}
