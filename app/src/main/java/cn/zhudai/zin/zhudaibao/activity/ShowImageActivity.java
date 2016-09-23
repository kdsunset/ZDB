package cn.zhudai.zin.zhudaibao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.utils.ImageUtils;

public class ShowImageActivity extends Activity {

    @Bind(R.id.iv_show_image)
    ImageView ivShowImage;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;

    /*除此之外还需在清单文件的Activity中添加android:theme="@android:style/Theme.Translucent"*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            int tag = intent.getExtras().getInt("tag");
            String path = intent.getStringExtra("path");
            if (!TextUtils.isEmpty(path)) {
                if (tag == 1) {
                    ImageUtils.setImg4ViewFromNet(ivShowImage, path);
                } else if (tag == 2) {
                    ImageUtils.setImg4ViewFromLocal(ivShowImage, path);
                }

            }
        }

        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
