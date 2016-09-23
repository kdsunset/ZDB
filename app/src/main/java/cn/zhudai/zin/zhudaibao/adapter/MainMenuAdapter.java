package cn.zhudai.zin.zhudaibao.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.activity.CheckPrivilegeActivity;
import cn.zhudai.zin.zhudaibao.activity.PerfectInformationActivity;
import cn.zhudai.zin.zhudaibao.entity.MainMenuData;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by ZIN on 2016/3/31.
 */
public class MainMenuAdapter extends RecyclerView.Adapter<MainMenuAdapter.ItemViewHolder> {


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


    public MainMenuAdapter(Context context,List<MainMenuData> list) {
        this.list = list;
        this.mContext=context;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recyclerview_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
       /* view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemClick(v, (int) view.getTag());//注意这里使用getTag方法获取数据
                }
            }
        });*/
        vh.btItem.setOnClickListener(new View.OnClickListener() {
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
        String title = getItem(position).getTitle();
        String detail = getItem(position).getDetail();
        int imageId = getItem(position).getImageResId();
        CharSequence charSequenceTitle = Html.fromHtml(title);
        CharSequence charSequenceDatail = Html.fromHtml(detail);

        holder.tvItemTitle.setText(charSequenceTitle);
        holder.tvItemDetail.setText(charSequenceDatail);
        holder.btItem.setImageResource(imageId);
       /* holder.btItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0){
                    if (userStatus==0){
                        mContext.startActivity(new Intent(mContext, SubmitCustomerInfoParentActivity.class));
                    }else {
                        showHandupDataDialog();
                    }
                }else if (position==1){

                }else if (position==2){
                    mContext.startActivity(new Intent(mContext,CheckLoanScheduleActivity.class));
                }else if (position==3){
                    mContext.startActivity(new Intent(mContext, WithdrawCashMenuActivity.class));
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
        @Bind(R.id.tv_item_title)
        TextView tvItemTitle;
        @Bind(R.id.tv_item_detail)
        TextView tvItemDetail;
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
    private  void showHandUpDialog(){
        final MaterialDialog mMaterialDialog = new MaterialDialog(mContext);
      //  View view = UIUtils.inflate(R.layout.dialog_handup_data);
        View view=LayoutInflater.from(mContext).inflate(R.layout.dialog_handup_data,null);
        mMaterialDialog
                //.setTitle("亲，补充个人资料后才能提交客户哦~")

                .setView(view)
               /* .setMessage("亲，补充个人资料后才能提交客户哦~")
                //mMaterialDialog.setBackgroundResource(R.drawable.background);
                .setPositiveButton("现在补充", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent=new Intent(mContext, PerfectInformationActivity.class);

                        mContext.startActivity(intent);

                        mMaterialDialog.dismiss();
                        ((Activity)mContext).finish();

                    }
                })
                .setNegativeButton("查看个人特权",
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                Intent intent=new Intent(mContext, CheckPrivilegeActivity.class);

                                mContext.startActivity(intent);

                                mMaterialDialog.dismiss();
                                ((Activity)mContext).finish();

                            }
                        })*/
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
   /* private void showHandupDataDialog(){
        *//*
          这里使用了 android.support.v7.app.AlertDialog.Builder
          可以直接在头部写 import android.support.v7.app.AlertDialog
          那么下面就可以写成 AlertDialog.Builder
         *//*
      //  final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        final android.support.v7.app.AlertDialog dialog=new android.support.v7.app.AlertDialog.Builder(mContext).create();
        // builder.setTitle("Material Design Dialog");
        View view=LayoutInflater.from(mContext).inflate(R.layout.dialog_handup_data,null);
        dialog.setView(view);
        View tvPerfectNow = view.findViewById(R.id.tv_perfect_now);
        View tvCheckNow = view.findViewById(R.id.tv_check_now);

        tvPerfectNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(mContext, PerfectInformationActivity.class);

                mContext.startActivity(intent);


            }
        });

        tvCheckNow.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(mContext, CheckPrivilegeActivity.class);

                mContext.startActivity(intent);



            }
        });


      /*  builder.setMessage("亲，补充个人资料后才能提交客户哦~");
        builder.setNegativeButton("现在补充", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(mContext, PerfectInformationActivity.class);

                mContext.startActivity(intent);

                ((Activity)mContext).finish();
            }
        });

        builder.setPositiveButton("查看个人特权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(mContext, CheckPrivilegeActivity.class);

                mContext.startActivity(intent);

                ((Activity)mContext).finish();
            }
        });

        dialog.show();
    }*/
    private void showInviteFriendsDialog(){

        final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();

        // builder.setTitle("Material Design Dialog");
        View view=LayoutInflater.from(mContext).inflate(R.layout.dialog_handup_data,null);
        dialog.setView(view);
        View tvPerfectNow = view.findViewById(R.id.tv_perfect_now);
        View tvCheckNow = view.findViewById(R.id.tv_check_now);

        tvPerfectNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(mContext, PerfectInformationActivity.class);

                mContext.startActivity(intent);


            }
        });

        tvCheckNow.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(mContext, CheckPrivilegeActivity.class);

                mContext.startActivity(intent);



            }
        });



        dialog.show();
    }


}