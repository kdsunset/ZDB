package cn.zhudai.zin.zhudaibao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.application.MyApplication;


public class LoginSelectActivity extends AppCompatActivity {

    @Bind(R.id.bt_login)
    Button btLogin;
    @Bind(R.id.bt_register)
    Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_select);
        ButterKnife.bind(this);
        checkLoginStatus();
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginSelectActivity.this,RegisterActivity.class));
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginSelectActivity.this,LoginActivity.class));
            }
        });
    }
    private void checkLoginStatus(){
        boolean loginStatus = MyApplication.getLoginStatus();
        if (loginStatus){
            startActivity(new Intent(LoginSelectActivity.this,MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLoginStatus();
    }
}
