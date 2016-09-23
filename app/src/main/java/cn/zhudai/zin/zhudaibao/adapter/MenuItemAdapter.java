package cn.zhudai.zin.zhudaibao.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.activity.MyPrerogativeActivity;
import cn.zhudai.zin.zhudaibao.activity.PersonalInfomationActivity;

/**
 * Created by admin on 2016/5/30.
 */
public class MenuItemAdapter extends BaseAdapter
{
    private final int mIconSize;
    private LayoutInflater mInflater;
    private Context mContext;

    public MenuItemAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        mIconSize = context.getResources().getDimensionPixelSize(R.dimen.drawer_icon_size);//24dp
    }

    private List<LvMenuItem> mItems = new ArrayList<>(
            Arrays.asList(
                    new LvMenuItem(R.drawable.main_icon_03, "个人资料"),
                    new LvMenuItem(R.drawable.main_icon_05, "我的特权"),
                    new LvMenuItem(R.drawable.main_icon_08, "客户经理评价"),
                    new LvMenuItem(R.drawable.main_icon_10, "我的合伙人"),
                    new LvMenuItem(R.drawable.main_icon_12, "意见反馈"),
                    new LvMenuItem(R.drawable.main_icon_14, "推荐给好友"),
                    new LvMenuItem(R.drawable.main_icon_16, "通知"),
                    new LvMenuItem(R.drawable.main_icon_18, "关于助贷宝")
            ));


    @Override
    public int getCount()
    {
        return mItems.size();
    }


    @Override
    public Object getItem(int position)
    {
        return mItems.get(position);
    }


    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getViewTypeCount()
    {
        return 3;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mItems.get(position).type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LvMenuItem item = mItems.get(position);
        switch (item.type)
        {
            case LvMenuItem.TYPE_NORMAL:
                if (convertView == null)
                {
                    convertView = mInflater.inflate(R.layout.design_drawer_item, parent,
                            false);
                }
                //TextView itemView = (TextView) convertView;
                final TextView itemView= (TextView) convertView.findViewById(R.id.tv_menu_item);

                itemView.setText(item.name);
                Drawable icon = mContext.getResources().getDrawable(item.icon);
               // setIconColor(icon);
                if (icon != null)
                {
                    icon.setBounds(0, 0, mIconSize, mIconSize);
                    TextViewCompat.setCompoundDrawablesRelative(itemView, icon, null, null, null);
                }
                final int pos=position;
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // itemView.setTextColor(Color.BLUE);
                        if (pos==0){
                            mContext.startActivity(new Intent((Activity)mContext,PersonalInfomationActivity.class));
                        }else if (pos==1){
                            mContext.startActivity(new Intent(mContext, MyPrerogativeActivity.class));
                        }
                    }
                });

                break;
            case LvMenuItem.TYPE_NO_ICON:
                if (convertView == null)
                {
                    convertView = mInflater.inflate(R.layout.design_drawer_item_subheader,
                            parent, false);
                }
                TextView subHeader = (TextView) convertView;
                subHeader.setText(item.name);
                break;
            case LvMenuItem.TYPE_SEPARATOR:
                if (convertView == null)
                {
                    convertView = mInflater.inflate(R.layout.design_drawer_item_separator,
                            parent, false);
                }
                break;
        }

        return convertView;
    }

    public void setIconColor(Drawable icon)
    {
        int textColorSecondary = android.R.attr.textColorSecondary;
        TypedValue value = new TypedValue();
        if (!mContext.getTheme().resolveAttribute(textColorSecondary, value, true))
        {
            return;
        }
        int baseColor = mContext.getResources().getColor(value.resourceId);
        icon.setColorFilter(baseColor, PorterDuff.Mode.MULTIPLY);
    }

    public class LvMenuItem
    {
        public LvMenuItem(int icon, String name)
        {
            this.icon = icon;
            this.name = name;

            if (icon == NO_ICON && TextUtils.isEmpty(name))
            {
                type = TYPE_SEPARATOR;
            } else if (icon == NO_ICON)
            {
                type = TYPE_NO_ICON;
            } else
            {
                type = TYPE_NORMAL;
            }

            if (type != TYPE_SEPARATOR && TextUtils.isEmpty(name))
            {
                throw new IllegalArgumentException("you need set a name for a non-SEPARATOR item");
            }

            L.e(type + "");


        }

        public LvMenuItem(String name)
        {
            this(NO_ICON, name);
        }

        public LvMenuItem()
        {
            this(null);
        }

        private static final int NO_ICON = 0;
        public static final int TYPE_NORMAL = 0;
        public static final int TYPE_NO_ICON = 1;
        public static final int TYPE_SEPARATOR = 2;

        int type;
        String name;
        int icon;

    }
}