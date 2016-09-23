package cn.zhudai.zin.zhudaibao.utils;

import com.squareup.okhttp.OkHttpClient;

import cn.zhudai.zin.zhudaibao.application.MyApplication;

/**
 * Created by admin on 2016/6/23.
 */
/*最简单的封装*/
public class OKhttpUtils {
    /**
     * @return	OkHttp官方文档并不建议我们创建多个OkHttpClient，因此全局使用一个
     */
    public static OkHttpClient getOkHttpClient(){
        return MyApplication.getgOkHttpClient();
    }
}
