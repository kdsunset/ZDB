package cn.zhudai.zin.zhudaibao.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.zhudai.zin.zhudaibao.R;
import cn.zhudai.zin.zhudaibao.utils.UIUtils;

/**
 * Created by admin on 2016/5/30.
 */
public class RebateListviewAdapter extends BaseAdapter
{
    private final int mIconSize;
    private LayoutInflater mInflater;
    private Context mContext;

    public RebateListviewAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        mIconSize = context.getResources().getDimensionPixelSize(R.dimen.drawer_icon_size);//24dp
    }

    private List<LvMenuItem> mItems = new ArrayList<>(
            Arrays.asList(
                    new LvMenuItem(2,"贷款成功客户级","返点"),
                    new LvMenuItem(2,"自己提交的客户","1%"),
                    new LvMenuItem(2,"一级合伙人提交的客户","0.3%"),
                    new LvMenuItem(2,"二级合伙人提交的客户","0.09%")

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
                    convertView = mInflater.inflate(R.layout.item_listview_loadamount, parent,
                            false);
                }
                //TextView itemView = (TextView) convertView;
                final TextView itemKey= (TextView) convertView.findViewById(R.id.tv_item_key);
                final TextView itemVal= (TextView) convertView.findViewById(R.id.tv_item_value);
                LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll_item);
                itemKey.setText(item.getKey());
                itemVal.setText(item.getValue());

                if (position==0){
                    ll.setBackgroundColor(UIUtils.getColorFromRes(R.color.colorLightGray));
                    itemKey.setBackgroundColor(UIUtils.getColorFromRes(R.color.colorLightGray));
                    itemVal.setBackgroundColor(UIUtils.getColorFromRes(R.color.colorLightGray));
                }

                break;
            case LvMenuItem.TYPE_TITLE:
                /*if (convertView == null)
                {
                    convertView = mInflater.inflate(R.layout.design_drawer_item_subheader,
                            parent, false);
                }
                TextView subHeader = (TextView) convertView;
                subHeader.setText(item.name);*/
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
        public LvMenuItem(int type,String key, String value)
        {
            this.key = key;
            this.value = value;
            this.type=type;

        }



        public LvMenuItem()
        {

        }


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
        private static final int TYPE_TITLE = 1;
        public static final int TYPE_NORMAL = 2;
        int type;
        String value;
        String key;

    }
}