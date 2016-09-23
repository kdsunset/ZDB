package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.adapter.MenuItemAdapter;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;

public class MenuActivity extends AppCompatActivity {
    @Bind(R.id.ll_back_area)
    LinearLayout llBackArea;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.lv_menu)
    ListView lvMenu;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        mContext=this;
        initView();
    }

    private void initView() {
        initMyToolbar();
        lvMenu.setAdapter(new MenuItemAdapter(mContext));
       /* lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    startActivity(new Intent(MenuActivity.this,UserInfomationActivity.class));
                }
            }
        });*/
    }

    private void initMyToolbar() {
    }



    /*点击Item时变成蓝色，当返回该页面时还原成原来的颜色*/
    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("onResume");
        //lvMenu.invalidateViews();
       lvMenu.setAdapter(new MenuItemAdapter(mContext));
    }


}
