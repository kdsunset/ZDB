package cn.zhudai.zin.zhudaibao.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.customshare.ShareDialog;
import cn.sharesdk.customshare.ShareDialogListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.adapter.CustomNavigationViewAdapter;
import cn.zhudai.zin.zhudaibao.adapter.MainMenuAdapter;
import cn.zhudai.zin.zhudaibao.application.MyApplication;
import cn.zhudai.zin.zhudaibao.entity.BaseErrorResponse;
import cn.zhudai.zin.zhudaibao.entity.MainMenuData;
import cn.zhudai.zin.zhudaibao.entity.MainPageDataResponse;
import cn.zhudai.zin.zhudaibao.entity.ShareInfoResponse;
import cn.zhudai.zin.zhudaibao.entity.ZDBURL;
import cn.zhudai.zin.zhudaibao.ui.DividerItemDecoration;
import cn.zhudai.zin.zhudaibao.ui.widget.banner.BannerView;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.LoginStatusSharePreHelper;
import cn.zhudai.zin.zhudaibao.utils.MainDataSharePreHelper;
import cn.zhudai.zin.zhudaibao.utils.OKhttpUtils;
import cn.zhudai.zin.zhudaibao.utils.StringUtils;
import cn.zhudai.zin.zhudaibao.utils.SystemUtils;
import cn.zhudai.zin.zhudaibao.utils.ToastUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;
import me.drakeet.materialdialog.MaterialDialog;

/**/

public class MainActivity extends AppCompatActivity {

   /* @Bind(R.id.iv_back)
    ImageView ivMenu;
    @Bind(R.id.tv_title)
    TextView tvTitle;*/
    @Bind(R.id.bannerView)
    BannerView bannerView;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.tl_custom)
    Toolbar tbCustom;
    @Bind(R.id.navigation_view)
   /* NavigationView navigationView;*/
            ListView navigationView;
    @Bind(R.id.dl_left)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.sv_main)
    ScrollView scrollView;
    private List<MainMenuData> mainMenuDatas;
    private Context mContext;
    private boolean hasNet;
    private boolean isOpen=false;
    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200: {
                    MainPageDataResponse respone = (MainPageDataResponse) msg.obj;
                    final MainPageDataResponse.Result result = respone.getResult();
                    rate = result.getRebate_rate();
                    first_parter_rebate_rate = result.getFirst_parter_rebate_rate();
                    second_parter_rebater_rate = result.getSecond_parter_rebater_rate();
                    statu1 = result.getStatu1();
                    statu2 = result.getStatu2();
                    yongjing = result.getYongjing();
                    userInfoStatus = result.getResult();
                    LogUtils.i("userInfoStatus1=" + userInfoStatus);
                    mainMenuDatas.clear();
                    mainMenuDatas=setRecyclerData();
                    mAdapter=new MainMenuAdapter(mContext,mainMenuDatas);
                    mAdapter.setOnItemClickListener(new MainMenuAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int data) {

                                int position = data;
                                if (position == 0) {
                                    if (userInfoStatus == 0) {
                                        mContext.startActivity(new Intent(mContext, SubmitCustomerInfoParentActivity.class));
                                    } else if (userInfoStatus == 1) {
                                        showHandupDataDialog();
                                    } else if (userInfoStatus == 2) {
                                        ToastUtils.showToast(mContext, "个人资料正在审核中，请稍候！");
                                    }
                                } else if (position == 1) {
                                    getShareInfo();
                                } else if (position == 2) {
                                    mContext.startActivity(new Intent(mContext, CheckLoanScheduleActivity.class));
                                } else if (position == 3) {
                                    mContext.startActivity(new Intent(mContext, WithdrawCashMenuActivity.class));
                                }


                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    MainDataSharePreHelper helper=new MainDataSharePreHelper(mContext);

                    helper.saveIntValue(MainDataSharePreHelper.RATE,rate);
                    helper.saveDoubleValue(MainDataSharePreHelper.FIRST_PARTER_REBATE_RATE,first_parter_rebate_rate );
                    helper.saveDoubleValue(MainDataSharePreHelper.SECOND_PARTER_REBATE_RATE, second_parter_rebater_rate);
                    helper.saveStringValue(MainDataSharePreHelper.STATU1,statu1);
                    helper.saveStringValue(MainDataSharePreHelper.STATU2,statu2);
                    helper.saveDoubleValue(MainDataSharePreHelper.YONGJING, yongjing);
                    helper.saveIntValue(MainDataSharePreHelper.USER_INFO_STATUS,userInfoStatus);

                   /* mainMenuDatas = setRecyclerData(rate, first_parter_rebate_rate,
                            second_parter_rebater_rate, statu1, statu2, yongjing);
                    MainMenuAdapter mAdapter = new MainMenuAdapter(MainActivity.this, mainMenuDatas);
                    mAdapter.setOnItemClickListener(new MainMenuAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int data) {
                            int position=data;
                            if (position==0){
                                if (userInfoStatus ==0){
                                    mContext.startActivity(new Intent(mContext, SubmitCustomerInfoParentActivity.class));
                                }else if (userInfoStatus ==1){
                                    showHandupDataDialog();
                                }else if (userInfoStatus ==2){
                                    ToastUtils.showToast(mContext,"个人资料正在审核中，请稍候！");
                                }
                            }else if (position==1){
                                getShareInfo();
                            }else if (position==2){
                                mContext.startActivity(new Intent(mContext,CheckLoanScheduleActivity.class));
                            }else if (position==3){
                                mContext.startActivity(new Intent(mContext, WithdrawCashMenuActivity.class));
                            }

                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);*/



                    break;
                }
                case 2:
                    Exception e = (Exception) msg.obj;
                    if (e instanceof java.net.UnknownHostException ) {
                        ToastUtils.showToast(mContext, "连接错误，请检查网络！");
                       // finish();
                    }

                    break;
                case 400:
                    BaseErrorResponse errorResponse= (BaseErrorResponse) msg.obj;
                    ToastUtils.showToast(mContext,errorResponse.getMsg());
                    break;
                case 2200: {
                    ShareInfoResponse respone = (ShareInfoResponse) msg.obj;
                    final ShareInfoResponse.Result result = respone.getResult();
                    String title = result.getTitle()+"。\n";
                    String sharedesc = result.getSharedesc();
                    String link = result.getLink();
                    String imgurl = result.getImgurl();
                    String QQimgurl = result.getQQimg();
                    LogUtils.i("title="+title);
                    LogUtils.i("sharedesc="+sharedesc);
                    LogUtils.i("link="+link);
                    LogUtils.i("imgurl="+imgurl);
                    LogUtils.i("QQimgurl="+QQimgurl);
                    showShare(title,sharedesc, link,imgurl,QQimgurl);
                    //showCustomShare(title,sharedesc, link,imgurl,QQimgurl);


                    break;
                }
                case 2400:
                    break;

            }
        }
    };
    private ActionBarDrawerToggle mDrawerToggle;
    private int userInfoStatus;
    private int rate;
    private double first_parter_rebate_rate;
    private double second_parter_rebater_rate;
    private String statu1;
    private String statu2;
    private double yongjing;
    private MainMenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ShareSDK.initSDK(this);
        mContext = this;
        hasNet= SystemUtils.checkNetworkState(mContext);
        initView();

    }

    private void initView() {
       // initMyToolBar();
        mRecyclerView.setFocusable(false);
        navigationView.setFocusable(false);
        scrollView.smoothScrollTo(0,20);
        setRecyclerView();
        initActionBar();
        setDrawerLayout();
       // setupDrawerContent(navigationView);
        setCustomNavigationView();
        getDataFromNet();

    }

    private void setCustomNavigationView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        navigationView.setFocusable(false);
        View viewHead = inflater.inflate(R.layout.view_navigation_header, navigationView, false);
        TextView tvName = (TextView) viewHead.findViewById(R.id.tv_user_name);
        tvName.setText(MyApplication.user.getRealName()+"先生"+"(邀请码："+MyApplication.user.getUid()+")");
        //
        View viewfoot=inflater.inflate(R.layout.view_button_loginout,navigationView,false);
        View line = viewfoot.findViewById(R.id.line_top);
        int sdkVersion=SystemUtils.getVersionCode();
        LogUtils.i(sdkVersion+">>>>>>>>>>>>");
        if (sdkVersion<22){
            line.setVisibility(View.VISIBLE);
        }
        Button loginOut= (Button) viewfoot.findViewById(R.id.bt_login_out);
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginOutDialog();
            }
        });
        /*
        * 该listview在Android4.4下分割线显示灰色，在Android5.0/魅族下显示纯黑色，需要再设置成灰色，但是
        * 在布局文件里设置android:divider="#FFF"失效，故用以下代码设置*/
        navigationView.setDivider(new ColorDrawable(UIUtils.getColorFromRes(R.color.colorGray)));
        navigationView.setDividerHeight(UIUtils.dp2px(1));

        navigationView.addHeaderView(viewHead);
        navigationView.addFooterView(viewfoot);

        navigationView.setAdapter(new CustomNavigationViewAdapter(this));
        navigationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.i(position+"");

                    if (position==1){
                        if (userInfoStatus==2){
                            // ToastUtils.showToast(mContext,"个人资料正在审核中，请稍候！");
                            showCheckingDialog();
                        }else {
                            startActivity(new Intent(mContext,ShowQRCodeActivity.class));
                        }

                    }else if(position==2){
                        if (userInfoStatus==2){
                            // ToastUtils.showToast(mContext,"个人资料正在审核中，请稍候！");
                            showCheckingDialog();
                        }else {
                            startActivity(new Intent(mContext,PersonalInfomationActivity.class));
                        }
                    }else if (position==3){
                        startActivity(new Intent(mContext,MyPrerogativeActivity.class));
                    }else if (position==4){
                        if (userInfoStatus==2){
                            // ToastUtils.showToast(mContext,"个人资料正在审核中，请稍候！");
                            showCheckingDialog();
                        }else {
                            startActivity(new Intent(mContext,ManagerCommentListActivity.class));
                        }

                    }else if (position==5){
                        if (userInfoStatus==2){
                            // ToastUtils.showToast(mContext,"个人资料正在审核中，请稍候！");
                            showCheckingDialog();
                        }else {
                            startActivity(new Intent(mContext,MyPartnerActivity.class));
                        }

                    }else if (position==6){
                        startActivity(new Intent(mContext,FeedBackActivity.class));
                    }else if (position==7){
                        if (userInfoStatus==2){
                            // ToastUtils.showToast(mContext,"个人资料正在审核中，请稍候！");
                            showCheckingDialog();
                        }else {
                            getShareInfo();
                            LogUtils.i("showshare");
//                            showShareJustOne();
                        }

                    }else if (position==8){
                        startActivity(new Intent(mContext,NoticeListActivity.class));
                    }else if (position==9){
                        startActivity(new Intent(mContext,AboutAppActivity.class));
                    }


            }
        });
    }

    /*  private void initMyToolBar() {
          RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(50, 50);
          layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
          //ivMenu.setPadding(20,ivMenu.getPaddingTop(),ivMenu.getPaddingRight(),ivMenu.getPaddingBottom());
          layoutParams.setMargins(10, 0, 0, 0);
          ivMenu.setLayoutParams(layoutParams);

          ivMenu.setBackgroundResource(R.drawable.tiaozhuang1);

          tvTitle.setText("助贷网");
          ivMenu.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  LogUtils.i("点击了菜单按钮");
                  startActivity(new Intent(MainActivity.this, MenuActivity.class));
              }
          });

      }*/
  private void setDrawerLayout() {
      getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //创建返回键，并实现打开关/闭监听
      mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, tbCustom, R.string.open, R.string.close) {
          @Override
          public void onDrawerOpened(View drawerView) {
              super.onDrawerOpened(drawerView);
          }
          @Override
          public void onDrawerClosed(View drawerView) {
              super.onDrawerClosed(drawerView);
          }
      };
      mDrawerToggle.syncState();
      tbCustom.setNavigationIcon(R.drawable.ic_menu_xxh);

      mDrawerLayout.setDrawerListener(mDrawerToggle);
        /*//设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);*/
      //设置NavigationView点击事件

  }

    private void initActionBar() {

      //  tbCustom.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        tbCustom.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
       // tbCustom.setTitleTextColor(Color.parseColor("#0000ff")); //设置标题颜色*/
        setSupportActionBar(tbCustom);
       /* // toolbar.setSubtitle("副标题");
                setSupportActionBar(mToolbar);
        *//* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 *//*
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.drawable.ic_launcher);

        *//* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过Activity的onOptionsItemSelected回调方法来处理 *//*
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "action_settings", 0).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this, "action_share", 0).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/

    }
    //设置NavigationView点击事件
    private void setupDrawerContent(NavigationView navigationView) {
        // View navheader = navigationView.inflateHeaderView(R.layout.navigation_header);
       /* View navheader = LayoutInflater.from(this).inflate(R.layout.navigation_header,navigationView );

        ivProfile = (CircleImageView) navheader.findViewById(R.id.profile_image);
        tvProfile = (TextView) navheader.findViewById(R.id.profile_text);
        ImageView ivLoginOut= (ImageView) navheader.findViewById(R.id.iv_login_out);
        if (MyApplication.getLoginStatus()) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.headimg__login_default);
            ivProfile.setImageBitmap(bitmap);
            UserSharePreHelper helper=new UserSharePreHelper(UIUtils.getContext());
            String username=helper.getUserName();
            tvProfile.setText(username);
        }
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  startActivity(new Intent(MainActivity.this,LoginActivity.class));
                jumpToPage();

            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(MainActivity.this,LoginActivity.class));
                jumpToPage();


            }
        });
        ivLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginOutDialog();
            }
        });*/

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nv_item_personalinfo:
                                startActivity(new Intent(MainActivity.this,PersonalInfomationActivity.class));
                                break;
                            case R.id.nv_item_myprerogative:
                                // switchToBlog();
                                startActivity(new Intent(MainActivity.this,MyPrerogativeActivity.class));
                                break;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void setRecyclerView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);

        //每个item高度一致，可设置为true，提高性能
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        /*private int userInfoStatus;
            private int rate=1;
            private double first_parter_rebate_rate=0.22;
            private double second_parter_rebater_rate=0.22;
            private String statu1;
            private String statu2;
            private Double yongjing;
*/

        MainDataSharePreHelper helper=new MainDataSharePreHelper(mContext);

        first_parter_rebate_rate = helper.getDoubleValue(MainDataSharePreHelper.FIRST_PARTER_REBATE_RATE,0.2f);
        second_parter_rebater_rate =  helper.getDoubleValue(MainDataSharePreHelper.SECOND_PARTER_REBATE_RATE,0.08f);
        statu1 = helper.getStringValue(MainDataSharePreHelper.STATU1,7+"");
        statu2 =helper.getStringValue(MainDataSharePreHelper.STATU2,8+"");
        yongjing = helper.getDoubleValue(MainDataSharePreHelper.YONGJING,9999);
        userInfoStatus =helper.getIntValue(MainDataSharePreHelper.USER_INFO_STATUS,0);
        rate = helper.getIntValue(MainDataSharePreHelper.RATE,1);
        mainMenuDatas=new ArrayList<>();
        mainMenuDatas = setRecyclerData();
        mAdapter = new MainMenuAdapter(MainActivity.this, mainMenuDatas);

        mAdapter.setOnItemClickListener(new MainMenuAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int data) {

                int position = data;
                if (position == 0) {
                    if (userInfoStatus == 0) {
                        mContext.startActivity(new Intent(mContext, SubmitCustomerInfoParentActivity.class));
                    } else if (userInfoStatus == 1) {
                        showHandupDataDialog();
                    } else if (userInfoStatus == 2) {
                        ToastUtils.showToast(mContext, "个人资料正在审核中，请稍候！");
                    }
                } else if (position == 1) {
                    getShareInfo();
//                    showShareJustOne();
                } else if (position == 2) {
                    mContext.startActivity(new Intent(mContext, CheckLoanScheduleActivity.class));
                } else if (position == 3) {
                    mContext.startActivity(new Intent(mContext, WithdrawCashMenuActivity.class));
                }


            }
        });



        //分隔线

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, mLinearLayoutManager.getOrientation(),
                UIUtils.dp2px(10), getResources().getColor(R.color.colorLightGray)));
      /*  mRecyclerView.addItemDecoration(new RecycleViewDivider(
                getApplicationContext(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.colorLightGray)));*/

        ///为每个item增加响应事件
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<MainMenuData> setRecyclerData() {
        String titile1 = "提交资料，获取返佣";
        String titile2 = "邀请好友，躺着赚钱";
        String titile3 = "贷款进度，随时掌控";
        String titile4 = "贷款成功，极速提现";
        String detail1 = "客户贷款金额 <font color=\"#fa3733\">" + rate +"%"+ "</font> 的返佣，<br>" +
                "30秒快速完成提交。";
        String detail2 = "一级合伙人客户贷款金额的<font color=\"#fa3733\">" + first_parter_rebate_rate + "%" + "</font>，<br>" +
                "二级合伙人客户贷款金额的<font color=\"#fa3733\">" + second_parter_rebater_rate + "%" + "</font>，<br>" +
                "将成为你的收益。";
        String detail3 = "<font color=\"#fa3733\">" + statu1 + "</font>个客户正在审批当中，<br>" +
                "<font color=\"#fa3733\">" + statu2 + "</font> 个客户已贷款成功。";
        String detail4 = "<font color=\"#fa3733\">" + StringUtils.leftoutZero(yongjing) + "</font> 元佣金待您提取。";
        int imageid1 = R.drawable.ic_handup_240;
        int imageid2 = R.drawable.ic_invite_240;
        int imageid3 = R.drawable.ic_checked_240;
        int imageid4 = R.drawable.ic_withdraw_240;
        MainMenuData data1 = new MainMenuData(titile1, detail1, imageid1);
        MainMenuData data2 = new MainMenuData(titile2, detail2, imageid2);
        MainMenuData data3 = new MainMenuData(titile3, detail3, imageid3);
        MainMenuData data4 = new MainMenuData(titile4, detail4, imageid4);
        List<MainMenuData> datas = new ArrayList<>();
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        datas.add(data4);
        return datas;
    }

    private void getDataFromNet() {
        LogUtils.i("获取数据请求");
        String uid = "";
        if (MyApplication.user != null) {
            uid = MyApplication.user.getUid();
        }
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("uid", uid)
                .build();
        final Request request = new Request.Builder()
                .url(ZDBURL.MAINPAGE_DATA)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr = response.body().string();
                Gson gson = new Gson();
                BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                Message message = handler.obtainMessage();
                if (errorResponse.getCode() == 400) {
                    message.what = 400;
                    message.obj = errorResponse;

                } else {
                    MainPageDataResponse loginResponse = gson.fromJson(htmlStr, MainPageDataResponse.class);
                    LogUtils.i("返回结果" + htmlStr);
                    if (loginResponse.getCode() == 200) {
                        message.what = 200;
                        message.obj = loginResponse;
                    }

                }
                handler.sendMessage(message);
            }
        });

    }
    private void showLoginOutDialog(){
        final MaterialDialog mMaterialDialog=new MaterialDialog(this);
        mMaterialDialog.setTitle("提示信息")
                .setMessage("你确定退出吗")
                //mMaterialDialog.setBackgroundResource(R.drawable.background);
                .setPositiveButton("是的", new View.OnClickListener() {
                    @Override public void onClick(View v) {

                        LoginStatusSharePreHelper helper=new LoginStatusSharePreHelper(UIUtils.getContext());
                        Long lastLoginTime = helper.getLastLoginTime();
                        //非空判断：当该用户登录且没有保存密码时，没有数据写到sp，则会报空指针
                        // helper.updateLastLoginTime(0L)：当用户退出时，将上次登录时间清空
                        if (lastLoginTime!=null){
                            helper.updateLastLoginTime(0L);
                        }

                        MyApplication.setLoginStatus(false);
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        mMaterialDialog.dismiss();
                        finish();

                    }
                })
                .setNegativeButton("取消",
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        })
                .setCanceledOnTouchOutside(true)
                // You can change the message anytime.
                // mMaterialDialog.setTitle("提示");
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                .show();

    }
    private void showShareJustOne(){
        LogUtils.i("isopen="+String.valueOf(isOpen));
        if (!isOpen){
            LogUtils.i("isopen="+"T");
            isOpen=true;
            getShareInfo();

        }else {
            LogUtils.i("isopen="+"F");
        }
    }
    private void showShare(final String title, final String dec, String url, final String imgUrl, final String imgUrl4QQ) {

        ShareSDK.initSDK(MainActivity.this);
        final OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTheme(OnekeyShareTheme.Custom);
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));



        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        // title标题：微信、QQ（新浪微博不需要标题）
        oks.setTitle(title);

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);

        // text是分享文本，所有平台都需要这个字段
       // oks.setText("我是分享文本");
        oks.setText(dec);


        //网络图片的url：所有平台
        oks.setImageUrl(imgUrl);//网络图片rul

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        //oks.setUrl(url);

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");

        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            //自定义分享的回调想要函数
            @Override
            public void onShare(Platform platform,
                                cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
             /*// 点击微信好友
                if("Wechat".equals(platform.getName())){
                    //微信分享应用 ,此功能需要微信绕开审核，需要使用项目中的wechatdemo.keystore进行签名打包
                    //由于Onekeyshare没有关于应用分享的参数如setShareType等，我们需要通过自定义 分享来实现
                    //比如下面设置了setTitle,可以覆盖oks.setTitle里面的title值
                    paramsToShare.setTitle("标题");
                    paramsToShare.setText("内容");
                    paramsToShare.setShareType(Platform.SHARE_APPS);
                    paramsToShare.setExtInfo("应用信息");
                    paramsToShare.setFilePath("xxxxx.apk");
                    paramsToShare.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
                }*/
                //点击新浪微博
                if(SinaWeibo.NAME.equals(platform.getName())){
                    /*//限制微博分享的文字不能超过20
                    if(paramsToShare.getText().length() > 20){
                        Toast.makeText(mContext, "分享长度不能超过20个字", Toast.LENGTH_SHORT).show();
                    }*/
                    paramsToShare.setText(title+dec);
                    LogUtils.i(SinaWeibo.NAME);
                }
                //点击QQ平台
                if(QQ.NAME.equals(platform.getName())){
                    paramsToShare.setImageUrl(imgUrl4QQ);//网络图片rul
                }
                if(TencentWeibo.NAME.equals(platform.getName())){
                    paramsToShare.setText(title+dec);
                }
            }
        });
        // 启动分享GUI
        oks.show(MainActivity.this);
        isOpen=false;
    }
    private void  showCustomShare(final String title, final String dec, final String url, final String imgUrl, final String imgUrl4QQ){
        final ShareDialog shareDialog  = new ShareDialog(this);
        shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                shareDialog.dismiss();

            }
        });
        shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
                ShareDialogListener listener=new ShareDialogListener(mContext);

                if (item.get("ItemText").equals("新浪微博")) {

                    //2、设置分享内容
                    ShareParams sp = new ShareParams();
                    sp.setText(title+dec); //分享文本
                    sp.setImageUrl(imgUrl);//网络图片rul

                    //3、非常重要：获取平台对象
                    Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                    sinaWeibo.setPlatformActionListener(listener); // 设置分享事件回调
                    // 执行分享
                    sinaWeibo.share(sp);

                } else if (item.get("ItemText").equals("微信好友")) {
                    //Toast.makeText(MainActivity.this, "您点中了" + item.get("ItemText"), Toast.LENGTH_LONG).show();

                    //2、设置分享内容
                    ShareParams sp = new ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                    sp.setTitle(title);  //分享标题
                    sp.setText(dec);   //分享文本
                    sp.setImageUrl(imgUrl);//网络图片rul
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情

                    //3、非常重要：获取平台对象
                    Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                    wechat.setPlatformActionListener(listener); // 设置分享事件回调
                    // 执行分享
                    wechat.share(sp);


                } else if (item.get("ItemText").equals("微信朋友圈")) {
                    //2、设置分享内容
                    ShareParams sp = new ShareParams();
                    sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                    sp.setTitle(title);  //分享标题
                    sp.setText(dec);   //分享文本
                    sp.setImageUrl(imgUrl);//网络图片rul
                    sp.setUrl(url);   //网友点进链接后，可以看到分享的详情

                    //3、非常重要：获取平台对象
                    Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                    wechatMoments.setPlatformActionListener(listener); // 设置分享事件回调
                    // 执行分享
                    wechatMoments.share(sp);

                } else if (item.get("ItemText").equals("QQ")) {
                    //2、设置分享内容
                    ShareParams sp = new ShareParams();
                    sp.setTitle(title);
                    sp.setText(dec);
                    sp.setImageUrl(imgUrl4QQ);//网络图片rul
                    sp.setTitleUrl(url);  //网友点进链接后，可以看到分享的详情
                    //3、非常重要：获取平台对象
                    Platform qq = ShareSDK.getPlatform(QQ.NAME);
                    qq.setPlatformActionListener(listener); // 设置分享事件回调
                    // 执行分享
                    qq.share(sp);

                }else if(item.get("ItemText").equals("QQ空间")){
                    //2、设置分享内容
                    ShareParams sp = new ShareParams();
                    sp.setTitle(title);
                    sp.setText(dec);
                    sp.setImageUrl(imgUrl4QQ);//网络图片rul
                    sp.setTitleUrl(url);  //网友点进链接后，可以看到分享的详情
                    //3、非常重要：获取平台对象
                    Platform qZone = ShareSDK.getPlatform(QZone.NAME);
                    qZone.setPlatformActionListener(listener); // 设置分享事件回调
                    // 执行分享
                    qZone.share(sp);
                }else if (item.get("ItemText").equals("腾讯微博")) {

                    //2、设置分享内容
                    ShareParams sp = new ShareParams();
                    sp.setText(title+dec); //分享文本
                    sp.setImageUrl(imgUrl);//网络图片rul

                    //3、非常重要：获取平台对象
                    Platform tencentWeibo = ShareSDK.getPlatform(TencentWeibo.NAME);
                    tencentWeibo.setPlatformActionListener(listener); // 设置分享事件回调
                    // 执行分享
                    tencentWeibo.share(sp);

                }else  if (item.get("ItemText").equals("信息")) {

                    //2、设置分享内容
                    ShareParams sp = new ShareParams();
                    sp.setText(title+dec); //分享文本
                    sp.setImageUrl(imgUrl);//网络图片rul

                    //3、非常重要：获取平台对象
                    Platform shortMessage = ShareSDK.getPlatform(ShortMessage.NAME);
                    shortMessage.setPlatformActionListener(listener); // 设置分享事件回调
                    // 执行分享
                    shortMessage.share(sp);

                }


                shareDialog.dismiss();


            }
        });
    }



    private void getShareInfo() {
        LogUtils.i("获取数据请求");
        String uid = "";
        if (MyApplication.user != null) {
            uid = MyApplication.user.getUid();
        }
        //http://www.zhudai.cn/index.php/app/share/invite?uid=用户ID&type=分享类型
        String url=ZDBURL.SHARE_INFO+"?"+"uid="+uid;
        LogUtils.i(url);
        // OkHttpClient client = new OkHttpClient();
        OkHttpClient client = OKhttpUtils.getOkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        Call call = client.newCall(request);
        //请求加入调度
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure" + e.toString());
                Message message = handler.obtainMessage();
                message.obj=e;
                message.what = 2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String htmlStr = (response.body().string()).replace("\\r\\n\\t\\t\\t\\t","").trim();

                Gson gson = new Gson();
                BaseErrorResponse errorResponse = gson.fromJson(htmlStr, BaseErrorResponse.class);
                Message message = handler.obtainMessage();
                if (errorResponse.getCode() == 400) {
                    message.what = 2400;
                    message.obj = errorResponse;

                } else {
                    ShareInfoResponse loginResponse = gson.fromJson(htmlStr, ShareInfoResponse.class);

                    LogUtils.i("返回结果" + htmlStr);
                    if (loginResponse.getCode() == 200) {
                        message.what = 2200;
                        message.obj = loginResponse;
                        LogUtils.i(2200+"fa");
                    }

                }
                handler.sendMessage(message);

            }
        });


    }
    private void showHandupDataDialog() {
        /*
          这里使用了 android.support.v7.app.AlertDialog.Builder
          可以直接在头部写 import android.support.v7.app.AlertDialog
          那么下面就可以写成 AlertDialog.Builder
         */
        //  final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();
        // builder.setTitle("Material Design Dialog");
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_handup_data, null);
        dialog.setView(view);
        View tvPerfectNow = view.findViewById(R.id.tv_perfect_now);
        View tvCheckNow = view.findViewById(R.id.tv_check_now);
        tvPerfectNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, PerfectInformationActivity.class);
                mContext.startActivity(intent);
            }
        });

        tvCheckNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, MyPrerogativeActivity.class);
                mContext.startActivity(intent);
            }
        });
        dialog.show();
    }
    private void showCheckingDialog(){
        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();
        View view = UIUtils.inflate(R.layout.dialog_checking);
        Button btPositive = (Button) view.findViewById(R.id.bt_positive);
        btPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);

        dialog.show();
/*
        // You can change the message anytime. before show
        materialDialog.setTitle("提示");
        materialDialog.show();
        // You can change the message anytime. after show
        materialDialog.setMessage("你好，世界~");*/
    }
}
