package cn.zhudai.zin.zhudaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.entity.LoadScheduleResponse;
import cn.zhudai.zin.zhudaibao.utils.LogUtils;
import cn.zhudai.zin.zhudaibao.utils.TimeUtils;

/**
 * Created by ZIN on 2016/3/31.
 */
public class LoadScheduleRVAdapter extends RecyclerView.Adapter<LoadScheduleRVAdapter.ItemViewHolder> {



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
    private List<LoadScheduleResponse.Result> list;
    private Context mContext;


    public LoadScheduleRVAdapter(Context context, List<LoadScheduleResponse.Result> list) {
        this.list = list;
        this.mContext = context;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycleview_loadshedule, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(v, (LoadScheduleResponse.Result) view.getTag());//注意这里使用getTag方法获取数据
                }
            }
        });
        return vh;
    }

    public LoadScheduleResponse.Result getItem(int position) {

        return list.get(position);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        LoadScheduleResponse.Result itemdata = getItem(position);
        String customerTruename = itemdata.getTruename();
        String customerLoanMoney = itemdata.getLoan_money();
        String customerMobile = itemdata.getMobile();
        String customerLoadTime = TimeUtils.timeFormatUnix(itemdata.getTime());
       /* Long t=new Long(customerLoadTime);
        String timeString= TimeUtils.dateFormatShowAll(new Long(customerLoadTime));*/
        String managerNickname = itemdata.getNickname();
        String managerMobile = itemdata.getM_mobile();
        if (TextUtils.isEmpty(managerNickname)){
            managerNickname="";
        }
        if (TextUtils.isEmpty(managerMobile)){
            managerMobile="";
        }
        LogUtils.i("customerTruename="+customerTruename);
        LogUtils.i("customerLoanMoney="+customerLoanMoney);
        LogUtils.i("customerMobile="+customerMobile);
        LogUtils.i("customerLoadTime="+customerLoadTime);
        LogUtils.i("managerNickname="+managerNickname);
        LogUtils.i("managerMobile="+managerMobile);
        LogUtils.i("status="+itemdata.getStatus());


        int status = Integer.valueOf(itemdata.getStatus());
        holder.tvCustomer.setText(customerTruename+","+"贷款"+customerLoanMoney+"W"
            +"，"+"联系方式:"+customerMobile+"\n"+customerLoadTime);
        holder.tvUsermanager.setText("客户经理："+managerNickname+"  "+"手机："
            +managerMobile);
        //状态（0已受理 1资质不符 2待签约 3已签约 4已放款 5银行已拒绝
        // 6审核中7待跟进 9 捣乱申请
        switch (status){
            case 0:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_load_status_01);
                break;
            case 1:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_state_bufuhe);
                break;
            case 2:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_load_status_03);
                break;
            case 3:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_load_status_05);
                break;
            case 4:
               // holder.ivLoadStatus.setImageResource(R.drawable.ic_load_status_07);
                holder.ivLoadStatus.setImageResource(R.drawable.ic_state_yifangkuan);
                break;
            case 5:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_load_status_10);
                break;
            case 6:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_load_status_06);
                break;
            case 7:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_load_status_02);
                break;
            case 8:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_state_yishangme);
                break;
            case 9:
                holder.ivLoadStatus.setImageResource(R.drawable.ic_load_status_09);
                break;
        }
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(getItem(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_load_status)
        ImageView ivLoadStatus;
        @Bind(R.id.tv_customer)
        TextView tvCustomer;
        @Bind(R.id.tv_usermanager)
        TextView tvUsermanager;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * RecyclerView的Item的监听器接口
     */
    public interface OnItemClickListener {
        void OnItemClick(View view, LoadScheduleResponse.Result data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }




}