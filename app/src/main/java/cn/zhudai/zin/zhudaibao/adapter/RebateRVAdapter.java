package cn.zhudai.zin.zhudaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.entity.RebateBean;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

/**
 * Created by ZIN on 2016/3/31.
 */
public class RebateRVAdapter extends RecyclerView.Adapter<RebateRVAdapter.ItemViewHolder> {







    /*
     *1.继承RecyclerViewAdapter类，需重写 onCreateViewHolder，onBindViewHolder，getItemCount
     * 三个方法
     */
    /**
     * 为RecyclerView添加item的点击事件
     * 1.在MyAdapter中定义接口,模拟ListView的OnItemClickListener：
     * 2. 声明一个这个接口的变量( OnItemClickListener mListener;)，并注册监听（ itemView.setOnClickListener）
     * 3.接口的onItemClick()中的v.getTag()方法，这需要在onBindViewHolder()方法中设置和item相关的数据
     * 4.最后暴露给外面的调用者，定义一个设置Listener的方法（setOnItemClickListener）：
     */

    private OnItemClickListener mListener;
    private List<RebateBean> mList;
    private Context mContext;
    private int row=2;
    private View mHeaderView;

    public RebateRVAdapter(Context context, List<RebateBean> list) {
        this.mList = list;
        this.mContext = context;



    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview_loadamount, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(v, (RebateBean) view.getTag());//注意这里使用getTag方法获取数据
                }
            }
        });
        return vh;

    }

    public RebateBean getItem(int position) {

        return mList.get(position);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder,  int position) {
        setBorder(holder, position);
        RebateBean dataItem = getItem(position);
        String key = dataItem.getKey();
        String val = dataItem.getVal();
        holder.tvItemKey.setText(key);
        holder.tvItemValue.setText(val);
       if (position==0){
           holder.llItem.setBackgroundColor(UIUtils.getColorFromRes(R.color.colorLightGray));
       }

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(getItem(position));


    }


    @Override
    public int getItemCount() {

        return mList.size();
    }


    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.border_top)
        View borderTop;
        @Bind(R.id.border_left)
        View borderLeft;
        @Bind(R.id.tv_item_key)
        TextView tvItemKey;
        @Bind(R.id.tv_item_value)
        TextView tvItemValue;
        @Bind(R.id.border_right)
        View borderRight;
        @Bind(R.id.ll_item)
        LinearLayout llItem;
        @Bind(R.id.border_bottom)
        View borderBottom;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * RecyclerView的Item的监听器接口
     */
    public interface OnItemClickListener {
        void OnItemClick(View view,RebateBean data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    private void setBorder(ItemViewHolder holder, int position) {
        int index = position+1;
        LogUtils.i("position="+position);
        LogUtils.i("size="+mList.size());
        LinearLayout.LayoutParams verticalLineParams = new LinearLayout.LayoutParams(UIUtils.dp2px(1), ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams horizonLineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dp2px(1));
        holder.borderRight.setVisibility(View.VISIBLE);
        holder.borderRight.setLayoutParams(verticalLineParams);
       /* if (index % row == 0) {
            holder.borderRight.setLayoutParams(verticalLineParams);
            holder.borderRight.setBackgroundColor(Color.BLUE);
        }*/

        if (index == mList.size()) {
            holder.borderBottom.setVisibility(View.VISIBLE);
            holder.borderBottom.setLayoutParams(horizonLineParams);
            //  holder.borderBottom.setBackgroundColor(Color.GREEN);

        }


    }

}