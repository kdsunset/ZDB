package cn.sharesdk.customshare;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by admin on 2016/6/22.
 */

public class ShareDialogListener implements View.OnClickListener, PlatformActionListener {
    private Context mContext;

    public ShareDialogListener(Context mContext) {
        this.mContext = mContext;
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(mContext.getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 2:
                    Toast.makeText(mContext.getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(mContext.getApplicationContext(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(mContext.getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;

                case 5:
                    Toast.makeText(mContext.getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(mContext.getApplicationContext(), "分享失败啊" + msg.obj, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是新浪微博
            handler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(1);
        } else if (platform.getName().equals(WechatMoments.NAME)) {
            handler.sendEmptyMessage(3);
        } else if (platform.getName().equals(QQ.NAME)) {
            handler.sendEmptyMessage(4);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 6;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(5);
    }
}
