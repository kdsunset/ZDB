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
import cn.zhudai.zin.zhudaibao.entity.WithdrawCashRecordResponse;

/**
 * Created by ZIN on 2016/3/31.
 */
public class WithdrawCashRecordRVAdapter extends RecyclerView.Adapter<WithdrawCashRecordRVAdapter.ItemViewHolder> {




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
    private List<WithdrawCashRecordResponse.Result> mList;
    private Context mContext;
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    public WithdrawCashRecordRVAdapter(Context context, List<WithdrawCashRecordResponse.Result> list) {
        this.mList = list;
        this.mContext = context;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ItemViewHolder(mHeaderView);
        }else {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview_record, parent, false);
            ItemViewHolder vh = new ItemViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.OnItemClick(v, (WithdrawCashRecordResponse.Result) view.getTag());//注意这里使用getTag方法获取数据
                    }
                }
            });
            return vh;
        }
    }

    public WithdrawCashRecordResponse.Result getItem(int position) {

        return mList.get(position);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder){

        }else {
            WithdrawCashRecordResponse.Result dataItem = getItem(position);
            String invitetime = dataItem.getInvitetime();
            String cardnumber = dataItem.getCardnumber();
            String money = dataItem.getMoney();
            String status = dataItem.getStatus();



            //将数据保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(getItem(position));
        }


    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getHeaderView() {
        return mHeaderView;
    }
    public void addDatas(List<WithdrawCashRecordResponse.Result> datas) {
        mList.addAll(datas);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }


    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item_withdrawdash_time)
        TextView tvItemWithdrawdashTime;
        @Bind(R.id.tv_item_withdrawdash_account)
        TextView tvItemWithdrawdashAccount;
        @Bind(R.id.tv_item_withdrawdash_amount)
        TextView tvItemWithdrawdashAmount;
        @Bind(R.id.tv_item_withdrawdash_status)
        TextView tvItemWithdrawdashStatus;
        @Bind(R.id.ll_item)
        LinearLayout llItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * RecyclerView的Item的监听器接口
     */
    public interface OnItemClickListener {
        void OnItemClick(View view, WithdrawCashRecordResponse.Result data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


}