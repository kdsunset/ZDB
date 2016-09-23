package cn.zhudai.zin.zhudaibao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.entity.NoticeListResponse;
import cn.zhudai.zin.zhudaibao.utils.StringUtils;

/**
 * Created by ZIN on 2016/3/31.
 */
public class NoticeRVAdapter extends RecyclerView.Adapter<NoticeRVAdapter.ItemViewHolder> {




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
    private List<NoticeListResponse.Result> mList;
    private Context mContext;


    public NoticeRVAdapter(Context context, List<NoticeListResponse.Result> list) {
        this.mList = list;
        this.mContext = context;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_notice, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(v, (NoticeListResponse.Result) view.getTag());//注意这里使用getTag方法获取数据
                }
            }
        });
        return vh;
    }

    public NoticeListResponse.Result getItem(int position) {

        return mList.get(position);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        NoticeListResponse.Result itemData = getItem(position);
        String title = itemData.getTitle();
        String addtime = itemData.getAddtime();
        String[] split = StringUtils.split(addtime, " ");
        String date=split[0];
        holder.tvTitle.setText(title);
        holder.tvDate.setText(date);
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(itemData);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_date)
        TextView tvDate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * RecyclerView的Item的监听器接口
     */
    public interface OnItemClickListener {
        void OnItemClick(View view, NoticeListResponse.Result data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


}