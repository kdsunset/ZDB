package cn.zhudai.zin.zhudaibao.utils;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import cn.zhudai.zin.zhudaibao.application.MyApplication;


public class UIUtils {
	/**
	 * @return	全局的上下文环境
	 */
	public static Context getContext(){
		return MyApplication.getContext();
	}
	
	/**
	 * @return	全局的hander对象
	 */
	public static Handler getHandler(){
		return MyApplication.getHandler();
	}
	
	/**
	 * @return	返回主线程方法
	 */
	public static Thread getMainThread(){
		return MyApplication.getMainThread();
	}
	
	/**
	 * @return	返回主线程id方法
	 */
	public static int getMainThreadId(){
		return MyApplication.getMainThreadId();
	}
	
	/**
	 * 布局文件转换成view对象方法
	 * @param layoutId	布局文件id
	 * @return	布局文件转换成的view对象
	 */
	public static View inflate(int layoutId){
		return View.inflate(getContext(), layoutId, null);

	}
	
	/**
	 * @return	返回资源文件夹对象方法
	 */
	public static Resources getResources(){
		return getContext().getResources();
	}
	
	/**
	 * @param stringId	字符串在xml中对应R文件中的id
	 * @return	string.xml某节点,对应的值
	 */
	public static String getString(int stringId){
		return getResources().getString(stringId);
	}
	
	/**
	 * @param stringArrayId	字符串数组在xml中对应R文件中的id
	 * @return	节点对应的内容
	 */
	public static String[] getStringArray(int stringArrayId){
		return getResources().getStringArray(stringArrayId);
	}
	
	//像素密度决定比例关系
	//1:0.75
	//1:1
	//1:1.5
	//1:2
	//1:3
	//dp2px()  dip---->px
	/**
	 * 根据手机的分辨率从 px 的单位 转成为 dp
	 *
	 * dpi = 分辨率/英寸(px/inch)
	 * density= dpi / 160
	 * px = density * dp
	 */
	public static int px2dp(int px){
		//1,获取当前手机的dip和px的转换关系比例值,不同的手机执行此段代码的时候,得到的比例值可能不一致
		float density = getResources().getDisplayMetrics().density;
		Log.i("density=",density+"");
		//2,将dp转换成px
		return (int)(px/density+0.5);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px
	 *
	 * dpi = 分辨率/英寸(px/inch)
	 * density= dpi / 160
	 * px = density * dp
	 */
	public static int dp2px(int dp){
		//1,获取当前手机的dip和px的转换关系比例值,不同的手机执行此段代码的时候,得到的比例值可能不一致
		float density = getResources().getDisplayMetrics().density;
		Log.i("density=",density+"");
		//2,将dp转换成px
		return (int)(dp*density+0.5);
	}

	//将任务(可能在主线程中,也可能在子线程中)放置在主线程中运行的方法
	/**
	 * @param runnable	将任务保证在主线程中运行的方法
	 */
	public static void runInMainThread(Runnable runnable){
		//获取调用此方法所在的线程
		if(android.os.Process.myTid() == getMainThreadId()){
			//如果上诉的runnable就是在主线程中要去执行的任务,则直接运行即可
			runnable.run();
		}else{
			//如果上诉的runnable运行在子线程中,将其传递到主线程中去做执行
			getHandler().post(runnable);
		}
	}

	public static Drawable getDrawable(int drawableId) {
		return getResources().getDrawable(drawableId);
	}

	/**
	 * 获取一个颜色选择器的对象	
	 * @param mTabTextColorResId	颜色选择器id
	 * @return
	 */
	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return getResources().getColorStateList(mTabTextColorResId);
	}
	public static int getColorFromHex(String hex){
		return Color.parseColor(hex);
	}
	public static int getColorFromRes(int resColor){
		return getResources().getColor(resColor);
	}
	public static String getEditTextContent(EditText e){
		return e.getText().toString().trim();

	}

}
