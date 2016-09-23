package cn.zhudai.zin.zhudaibao.ui.widget.banner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by ZIN on 2016/4/4.
 */
public class BannerAdapter extends PagerAdapter {
    /**
     *   * ViewPager适配器
     *   *
     *
     */

    //数据源
    private List<ImageView> mImageList;

    public BannerAdapter(List<ImageView> list) {
        this.mImageList = list;

    }


    /**
     * 正常情况下让它返回的是数据源的长度大小，但这里需要实现"无限循环"的效果，
     * 这么可以返回一个比较大的是比如：Integer.MAX_VALUE，这个数值可是20亿，
     * 用户再怎么滑到也不会滑到上亿次级别的吧。然后避免出现空指针异常，
     * 在下面addView和removeView的时候就不能再直接使用position去索引资源了，
     * 应该取余item的总数量，这样索引位置就不会超过资源数据的数量，例如1%777=1,1%999=1。
     */

    @Override
    public int getCount() {
        //1.设置成最大整数，使用户看不到边界，实现无限循环效果
        return Integer.MAX_VALUE;

    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        /*container.addView(mImageList.get(position % mImageList.size()));
        return mImageList.get(position % mImageList.size());*/

         /*
        * 通常是直接container.addView(mImageList.get(position % mImageList.size()));
                return mImageList.get(position % mImageList.size());
        *
        *
        *
        * */



        // Integer.MAX_VALUE，因此这个position的取值范围很大很大，
        // 但我们实际要显示的内容肯定没这么多（往往只有几项）， 所以这里肯定会有求模操作.
        position %= mImageList.size();
        //但是，简单的求模会出现问题：考虑用户向左滑的情形，
        // 则position可能会出现负值。所以我们需要对负值再处理一次，使其落在正确的区间内
        if (position<0){
            position = mImageList.size()+position;
        }

        ImageView view = mImageList.get(position);
        //通常我们会直接container.addView(view)，但这里如果直接这样写，则会抛出IllegalStateException。
        // 假设一共有三个view，则当用户滑到第四个的时候就会触发这个异常，
        // 原因是我们试图把一个有父组件的View添加到另一个组件

        // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        //但是，如果直接写成下面这样：
        //(ViewGroup)view.getParent().removeView(view);
       // 则又会因为一开始的时候组件并没有父组件而抛出NullPointerException。因此，需要进行一次判断。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        // add listeners here if necessary
        return view;



    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView(mImageList.get(position % mImageList.size()));
        //  由于我们在instantiateItem()方法中已经处理了remove的逻辑，
        // 因此这里并不需要处理。实际上，实验表明这里如果加上了remove的调用，
        // 则会出现ViewPager的内容为空的情况

    }


}
