package cn.zhudai.zin.zhudaibao.ui.widget.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

/**
 * Created by ZIN on 2016/4/5.
 */
public class BannerView extends RelativeLayout {


    private  Context mContext;
    // 声明控件
    private MyViewPager mViewPager;
    /** 存放banner的图片素材的List*/
    private List<ImageView> mlist;

    /** 提示文字*/
    private TextView mTextView;
    /** 指示器（小圆点）*/
    private LinearLayout mLinearLayout;
    private List<String> bannerTextList;
    private List<String> bannerImgUrlList;
    BannerPageChangeListener bannerListener;
    /** 圆圈标志位*/
    private int pointIndex = 0;
    /** 线程标志*/
    private static final String LOG_TAG = "MainActivity";
    private ImageHandler handler = new ImageHandler(new WeakReference<BannerView>(this));
    View bannerLayout;
    public BannerView(Context context) {
        super(context);
    }
    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initUI();
    }
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public  void  initUI(){
        bannerImgUrlList=new ArrayList<>();
        bannerTextList=new ArrayList<>();
        bannerImgUrlList.add("http://www.zhudai.cn/www/static/images/banner_01.jpg");
        bannerImgUrlList.add("http://www.zhudai.cn/www/static/images/banner_02.jpg");
        bannerImgUrlList.add("http://www.zhudai.cn/www/static/images/banner_03.jpg");

        bannerTextList.add("");
        bannerTextList.add("");
        bannerTextList.add("");


        initView();

        createBannerView();

        mViewPager.setAdapter(new BannerAdapter(mlist));
         bannerListener=new BannerPageChangeListener();
        mViewPager.setOnPageChangeListener(bannerListener);
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2);//默认在中间，使用户看不到边界

        setDotStyleBig(mLinearLayout.getChildAt(0));
        mTextView.setText(bannerTextList.get(0));
       // mViewPager.setCurrentItem(0);//
        //开始轮播效果
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_START_SCROLL, ImageHandler.MSG_DELAY);
    }


    public void setContenxt(Context context) {
        this.mContext=context;
    }


    public void setBannerImgUrlList(List<String> bannerImgUrlList) {
        for (int i = 0; i < bannerImgUrlList.size(); i++)
        {
            this.bannerImgUrlList.add(bannerImgUrlList.get(i));

        }
    }

    public void setBannerTextList(List<String> bannerTextList) {
        for (int i = 0; i < bannerTextList.size(); i++)
        {
            this.bannerTextList.add(bannerTextList.get(i));

        }
    }

    private class  BannerPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        //配合Adapter的currentItem字段进行设置。
        @Override
        public void onPageSelected(int position) {
            int newPosition = position % mlist.size();
            String title = bannerTextList.get(newPosition);
            title=TextUtils.isEmpty(title)?"":title;
            mTextView.setText(title);
            mLinearLayout.getChildAt(newPosition).setEnabled(true);
            setDotStyleBig(mLinearLayout.getChildAt(newPosition));
            setDotStyleNormal(mLinearLayout.getChildAt(pointIndex));
            // 更新标志位
            pointIndex = newPosition;
            Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED,position,0).sendToTarget();
        }
        //覆写该方法实现轮播效果的暂停和恢复
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                    break;
                default:
                    break;
            }
        }
    }

    private  void setBackGroundImageFromNet(List<ImageView> lists){
        // new  SetBGImageTask().execute(list);
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
               // .showImageOnLoading(R.drawable.loading_anim)
                .showImageOnFail(R.drawable.load_img_fail)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        for (int i=0;i<lists.size();i++){
            ImageView imageView=lists.get(i);
            ImageLoader.getInstance().displayImage(bannerImgUrlList.get(i), imageView,options);
        }
    }



    private static class ImageHandler extends Handler {

        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_START_SCROLL= 0;
        protected static final int MSG_UPDATE_IMAGE  = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT   = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT  = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED  = 4;

        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;

        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<BannerView> weakReference;
        private int currentItem = 0;

        protected ImageHandler(WeakReference<BannerView> wk){
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(LOG_TAG, "receive message " + msg.what);
            BannerView banner = weakReference.get();
            if (banner==null){
                //Activity已经回收，无需再处理UI了
                return ;
            }
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (banner.handler.hasMessages(MSG_UPDATE_IMAGE)){
                banner.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    banner.mViewPager.setCurrentItem(currentItem);

                    //准备下次播放
                    banner.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    banner.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    break;
                case MSG_START_SCROLL:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem++;
                    banner.mViewPager.setCurrentItem(currentItem);
                    break;
                default:
                    break;
            }
        }
    }


    public List<String> getBannerTextList() {
        return bannerTextList;
    }

    public List<String> getBannerImgUrlList() {
        return bannerImgUrlList;
    }

    /**
     * 初始化数据,根据图片数量生成Lis<ImageView>和布置小圆点的LinearLayout
     */

    private  void  createBannerView(){
        mlist = new ArrayList<>();

        View view;
        for (int i = 0; i < bannerImgUrlList.size(); i++) {
            // 设置广告图
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //  imageView.setBackgroundResource(bannerImages[i]);
            mlist.add(imageView);
            // 设置圆圈点
            view = new View(mContext);
            view.setBackgroundResource(R.drawable.dot_bg);
            setDotStyleNormal(view);
            mLinearLayout.addView(view);
        }
        setBackGroundImageFromNet(mlist);


    }
    private void setDotStyleBig(View view){
        LinearLayout.LayoutParams paramsBig;
        paramsBig=new LinearLayout.LayoutParams(UIUtils.dp2px(25), UIUtils.dp2px(3));
        paramsBig.leftMargin=UIUtils.dp2px(10);
        paramsBig.gravity= Gravity.CENTER_VERTICAL;
        view.setEnabled(true);
        view.setLayoutParams(paramsBig);


    }
    private void setDotStyleNormal(View view){
        LinearLayout.LayoutParams paramsNormal;
        paramsNormal=new LinearLayout.LayoutParams(UIUtils.dp2px(25), UIUtils.dp2px(3));
        paramsNormal.leftMargin=UIUtils.dp2px(10);
        paramsNormal.gravity= Gravity.CENTER_VERTICAL;
        view.setEnabled(false);
        view.setLayoutParams(paramsNormal);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }



    /**
     * 初始化View操作
     */
    private void initView() {

       // bannerLayout = UIUtils.inflate(R.layout.view_banner);
       /* bannerLayout= LayoutInflater.from(mContext).inflate(R.layout.view_banner,null);
        mViewPager = (MyViewPager) bannerLayout.findViewById(R.id.viewpager);
        mTextView = (TextView) bannerLayout.findViewById(R.id.tv_bannertext);
        mLinearLayout = (LinearLayout) bannerLayout.findViewById(R.id.points);
        this.addView(bannerLayout);*/

       // LayoutInflater.from(mContext).inflate(R.layout.view_banner,this,true);

      LayoutInflater.from(mContext).inflate(R.layout.view_banner, this,true);


       // this.addView(inflate);


        mViewPager = (MyViewPager) findViewById(R.id.viewpager);
        mTextView = (TextView) findViewById(R.id.tv_bannertext);
        mLinearLayout = (LinearLayout)findViewById(R.id.points);

    }

    /**
     * 配置ImageLoder
     */
  /*  private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //.showStubImage(R.drawable.icon_stub) // 设置图片下载期间显示的图片
                //.showImageForEmptyUri(R.drawable.icon_empty) // 设置图片Uri为空或是错误的时候显示的图片
                // .showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }*/

}
