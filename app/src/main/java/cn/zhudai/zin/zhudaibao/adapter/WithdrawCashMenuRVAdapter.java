package cn.zhudai.zin.zhudaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.entity.MainMenuData;

/**
 * Created by ZIN on 2016/3/31.
 */
public class WithdrawCashMenuRVAdapter extends RecyclerView.Adapter<WithdrawCashMenuRVAdapter.ItemViewHolder> {



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
    private List<MainMenuData> list;
    private Context mContext;


    public WithdrawCashMenuRVAdapter(Context context, List<MainMenuData> list) {
        this.list = list;
        this.mContext = context;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_withdrawcashmenu, parent, false);
        ImageView btItem = (ImageView) view.findViewById(R.id.bt_item);
        ItemViewHolder vh = new ItemViewHolder(view);
        //这里不是为Item添加点击事件，而是按钮的点击事件
        btItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(v, (int) view.getTag());//注意这里使用getTag方法获取数据

                }
            }
        });
        return vh;
    }

    public MainMenuData getItem(int position) {

        return list.get(position);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        MainMenuData itemData = getItem(position);

        String title = itemData.getTitle();
        String detail = itemData.getDetail();
        int imageId = itemData.getImageResId();


        holder.tvItemKey.setText(title);
        holder.tvItemVal.setText(detail);
        holder.btItem.setBackgroundResource(imageId);
       /* holder.btItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    mContext.startActivity(new Intent(mContext, WithdrawCashActivity.class));
                } else if (position == 1) {
                    mContext.startActivity(new Intent(mContext, WithdrawRecordActivity.class));
                } else if (position == 2) {
                    mContext.startActivity(new Intent(mContext, WithdrawCashTotalProfitActivity.class));
                }
            }
        });*/

        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item_key)
        TextView tvItemKey;
        @Bind(R.id.tv_item_val)
        TextView tvItemVal;
        @Bind(R.id.bt_item)
        ImageView btItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * RecyclerView的Item的监听器接口
     */
    public interface OnItemClickListener {
        void OnItemClick(View view, int data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }








}