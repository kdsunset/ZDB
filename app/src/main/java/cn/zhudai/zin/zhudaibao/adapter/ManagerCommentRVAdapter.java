package cn.zhudai.zin.zhudaibao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.activity.WriteCommentActivity;
import cn.zhudai.zin.zhudaibao.entity.ManagerCommentResponse;

/**
 * Created by ZIN on 2016/3/31.
 */
public class ManagerCommentRVAdapter extends RecyclerView.Adapter<ManagerCommentRVAdapter.ItemViewHolder> {




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
    private List<ManagerCommentResponse.Result> list;
    private Context mContext;


    public ManagerCommentRVAdapter(Context context, List<ManagerCommentResponse.Result> list) {
        this.list = list;
        this.mContext = context;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_comment, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(v, (ManagerCommentResponse.Result) view.getTag());//注意这里使用getTag方法获取数据
                }
            }
        });
        return vh;
    }

    public ManagerCommentResponse.Result getItem(int position) {

        return list.get(position);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        ManagerCommentResponse.Result itemData = getItem(position);
        String truename = itemData.getTruename();
        String addtime = itemData.getAddtime();
        String mname = itemData.getMname();
        String mphone = itemData.getMphone();
        String content = itemData.getContent();
        String grade = itemData.getGrade();
        int is_review = itemData.getIs_review();
        final String id = itemData.getId();
        if (TextUtils.isEmpty(mname)){
            mname="";
        }
        if (TextUtils.isEmpty(mphone)){
            mphone="";
        }
        holder.tvDatas.setText(truename+"，"+"提交时间："+addtime);
        holder.tvDatas2.setText("客户经理："+mname+","+"手机："+mphone);
        if (!TextUtils.isEmpty(content)){
            holder.tvComment.setText("评价："+content);
        }
        //1:已评价 0:未评价
        if (is_review==0){
            holder.btWriteComment.setVisibility(View.VISIBLE);
            holder.rbarScore.setVisibility(View.GONE);
            holder.tvComment.setVisibility(View.GONE);
            holder.btWriteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, WriteCommentActivity.class);
                    intent.putExtra("id",id);
                    mContext.startActivity(intent);
                }
            });
        }else if (is_review==1){
            holder.btWriteComment.setVisibility(View.GONE);
            holder.rbarScore.setVisibility(View.VISIBLE);
            holder.tvComment.setVisibility(View.VISIBLE);
            holder.rbarScore.setRating((float)Integer.valueOf(grade));

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
        @Bind(R.id.tv_datas)
        TextView tvDatas;
        @Bind(R.id.tv_datas2)
        TextView tvDatas2;
        @Bind(R.id.tv_comment)
        TextView tvComment;
        @Bind(R.id.bt_write_comment)
        Button btWriteComment;
        @Bind(R.id.rbar_score)
        RatingBar rbarScore;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * RecyclerView的Item的监听器接口
     */
    public interface OnItemClickListener {
        void OnItemClick(View view, ManagerCommentResponse.Result data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


}