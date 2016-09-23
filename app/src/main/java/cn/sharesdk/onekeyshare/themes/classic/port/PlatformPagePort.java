/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic.port;

import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPage;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPageAdapter;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

/** 竖屏的九宫格页面 */
public class PlatformPagePort extends PlatformPage {

	public PlatformPagePort(OnekeyShareThemeImpl impl) {
		super(impl);
	}

	public void onCreate() {
		requestPortraitOrientation();
		super.onCreate();
	}

	@Override
	protected void addCustomView(final LinearLayout llPanel, final Animation animHide) {
		//增加一个取消按钮
		Button btCancle=new Button(activity);
		LinearLayout.LayoutParams btCancleLp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		btCancleLp.setMargins(UIUtils.dp2px(10),UIUtils.dp2px(10) , UIUtils.dp2px(10), UIUtils.dp2px(10));
		btCancle.setLayoutParams(btCancleLp);
		btCancle.setText("取  消");
		btCancle.setTextSize(16);
		btCancle.setTextColor(Color.BLUE);
		btCancle.setBackgroundResource(cn.zhudai.zin.zhudaibao.R.drawable.et_border_shape);
		btCancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				llPanel.clearAnimation();
				llPanel.setAnimation(animHide);
				//llPanel.setVisibility(View.GONE);
				finish();
			}
		});
		llPanel.addView(btCancle);
	}

	protected PlatformPageAdapter newAdapter(ArrayList<Object> cells) {
		return new PlatformPageAdapterPort(this, cells);
	}

}
