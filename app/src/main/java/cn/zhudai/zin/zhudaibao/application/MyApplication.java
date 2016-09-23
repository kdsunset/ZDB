package cn.zhudai.zin.zhudaibao.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.okhttp.OkHttpClient;

import cn.zhudai.zin.zhudaibao.entity.User;
import cn.zhudai.zin.zhudaibao.utils.LoginStatusSharePreHelper;

public class MyApplication extends Application {
	//全局上下文环境
	private static Context mContext;
	//全局的handler
	private static Handler mHandler;
	//主线程
	private static Thread mMainThread;
	//主线程id
	private static int mMainThreadId;
	private static boolean loginStatus;
	public static User user;
	public static OkHttpClient gOkHttpClient;
	//此方法在其余代码运行前,就会调用,所有在此处去构建开发过程中需要用到的常见对象,并且提供方法用于获取
	@Override
	public void onCreate() {

		mContext = getApplicationContext();
		
		mHandler = new Handler();
		
		//MyApplication运行在主线程中,所以拿当前线程对象即可
		mMainThread = Thread.currentThread();
		
		//主线程id,就是MyApplication(主线程)线程id,获取当前线程id
		mMainThreadId = android.os.Process.myTid();
		gOkHttpClient=new OkHttpClient();
		initImageLoader();
		getUserFromLocal();
		initLoginStatus();

		super.onCreate();
	}

	public static Context getContext() {
		return mContext;
	}
	public static OkHttpClient getgOkHttpClient(){

		return gOkHttpClient;
	}
	public static Handler getHandler() {
		return mHandler;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}
	public static boolean getLoginStatus() {

		return loginStatus;
	}

	public static void setLoginStatus(boolean loginStatus) {
		MyApplication.loginStatus = loginStatus;
	}

	private void initImageLoader(){

		//创建默认的ImageLoader配置参数
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration
				.createDefault(this);

		//Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
	}
	private void initLoginStatus(){
		LoginStatusSharePreHelper helper=new LoginStatusSharePreHelper(this);
		Long time=helper.getLastLoginTime();
		if (time==0){
			if (user.getLastLoginTime()!=null){
				time=user.getLastLoginTime();
			}
		}
		loginStatus=helper.isLoginStatusValid(time);
	}
	public void getUserFromLocal(){
		LoginStatusSharePreHelper helper=new LoginStatusSharePreHelper(this);
		String phone=helper.getUserPhone();
		String uid=helper.getUid();
		String name=helper.getUserRealName();
		Long lastLoginTime=helper.getLastLoginTime();
		user=new User(phone,name,uid,lastLoginTime);
	}
	public static void setUserAfterLogin(User u){
		user=u;
	}
}
