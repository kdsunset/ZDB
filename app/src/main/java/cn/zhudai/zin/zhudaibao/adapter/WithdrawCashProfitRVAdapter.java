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
import cn.zhudai.zin.zhudaibao.entity.WithdrawCashProfitResponse;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.StringUtils;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

/**
 * Created by ZIN on 2016/3/23.
 */
public class WithdrawCashProfitRVAdapter extends RecyclerView.Adapter<WithdrawCashProfitRVAdapter.ItemViewHolder> {



    private List<WithdrawCashProfitResponse.Tixianmsg> datas;
    private OnItemClickListener mListener;
    public static final int TYPE_HEADER = Integer.MIN_VALUE;
    public static final int TYPE_ITEM = 0;
    private Context mContext;

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview_profit, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {if (mListener != null) {
                        mListener.OnItemClick(v, (String) view.getTag());//注意这里使用getTag方法获取数据
                    }
                }
            });
        return vh;



    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (position==0) {

            holder.llItem.setBackgroundColor(UIUtils.getColorFromRes(R.color.colorLightGray));
            holder.headBorderTop.setVisibility(View.VISIBLE);
            holder.tvItemWithdrawdashCustomer.setText("客户");
            holder.tvItemWithdrawdashAmount.setText("放贷金额");
            holder.tvItemWithdrawdashRebate.setText("返点");
            holder.tvItemWithdrawdashReward.setText("成单奖励");
            holder.tvItemWithdrawdashProfit.setText("所获收益");

        } else {

            WithdrawCashProfitResponse.Tixianmsg data = getDataItem(position);
            holder.tvItemWithdrawdashCustomer.setText(data.getCname());
            holder.tvItemWithdrawdashAmount.setText(StringUtils.leftoutZero(data.getCmoney()));
            holder.tvItemWithdrawdashRebate.setText(data.getRebate_rate());
            holder.tvItemWithdrawdashReward.setText(StringUtils.leftoutZero(data.getSingle_award()));
            holder.tvItemWithdrawdashProfit.setText(StringUtils.leftoutZero(data.getPrize_money()));
            //将数据保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(getDataItem(position));
        }


    }

    public WithdrawCashProfitResponse.Tixianmsg getDataItem(int positon) {
        return datas.get(positon - 1);
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;//0
        }

    }

    private int getNoamlItemCount() {
        return datas.size();
    }

    @Override
    public int getItemCount() {
        return getNoamlItemCount() + 1;
    }

    /**
     * @param datas 展示的数据
     */
    public WithdrawCashProfitRVAdapter(Context context, List<WithdrawCashProfitResponse.Tixianmsg> datas) {
        this.datas = datas;
        this.mContext = context;
        LogUtils.i("HeaderRecyclerViewAdapter");
    }

    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_item_withdrawdash_customer)
        TextView tvItemWithdrawdashCustomer;
        @Bind(R.id.tv_item_withdrawdash_amount)
        TextView tvItemWithdrawdashAmount;
        @Bind(R.id.tv_item_withdrawdash_rebate)
        TextView tvItemWithdrawdashRebate;
        @Bind(R.id.tv_item_withdrawdash_reward)
        TextView tvItemWithdrawdashReward;
        @Bind(R.id.tv_item_withdrawdash_profit)
        TextView tvItemWithdrawdashProfit;
        @Bind(R.id.ll_item)
        LinearLayout llItem;
        @Bind(R.id.head_border_top)
        View headBorderTop;
        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }



    /**
     * RecyclerView的Item的监听器接口
     */
    public interface OnItemClickListener {
        void OnItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    private String convertStatus(int staustInt) {
        /* "status": "1" //状态（1:正在审核，2：已发放，3：拒绝申请）*/
        String s = "";
        switch (staustInt) {
            case 1:
                s = "正在审核";
                break;
            case 2:
                s = "已发放";
                break;
            case 3:
                s = "拒绝申请";
                break;
        }
        return s;
    }
}
