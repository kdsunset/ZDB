package cn.sharesdk.customshare;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.zhudai.zin.zhudaibao.R;

/**
 * Created by admin on 2016/6/22.
 */
public class ShareDialog {




        private android.support.v7.app.AlertDialog dialog;
        private GridView gridView;
        private RelativeLayout cancelButton;
        private SimpleAdapter saImageItems;
        private int[] image = {R.drawable.ssdk_oks_classic_wechat, R.drawable.ssdk_oks_classic_wechatmoments,
                R.drawable.ssdk_oks_classic_qq, R.drawable.ssdk_oks_classic_qzone,R.drawable.ssdk_oks_classic_sinaweibo,
                R.drawable.ssdk_oks_classic_tencentweibo,R.drawable.ssdk_oks_classic_shortmessage};
        private String[] name = {"微信好友", "微信朋友圈", "QQ", "QQ空间","新浪微博","腾讯微博","信息"};

        public ShareDialog(Context context) {

            dialog = new AlertDialog.Builder(context).create();
            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM); // 非常重要：设置对话框弹出的位置
            window.setContentView(R.layout.custom_share_dialog);
            gridView = (GridView) window.findViewById(R.id.share_gridView);
            cancelButton = (RelativeLayout) window.findViewById(R.id.share_cancel);
            List<HashMap<String, Object>> shareList = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < image.length; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("ItemImage", image[i]);//添加图像资源的ID
                map.put("ItemText", name[i]);//按序号做ItemText
                shareList.add(map);
            }

            saImageItems = new SimpleAdapter(context, shareList, R.layout.custom_share_item,
                    new String[]{"ItemImage", "ItemText"}, new int[]{R.id.imageView1, R.id.textView1});
            gridView.setAdapter(saImageItems);
        }

        public void setCancelButtonOnClickListener(View.OnClickListener Listener) {
            cancelButton.setOnClickListener(Listener);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            gridView.setOnItemClickListener(listener);
        }


        /**
         * 关闭对话框
         */
        public void dismiss() {
            dialog.dismiss();
        }

}
