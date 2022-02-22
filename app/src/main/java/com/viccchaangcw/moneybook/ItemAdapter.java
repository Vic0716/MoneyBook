package com.viccchaangcw.moneybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ItemAdapter extends BaseAdapter
{
    private ArrayList<DailyItems> mDailyItems;

    private DailyItemsView mDailyItemsView;

    private Context mContext;

    private LayoutInflater mInflater;

    /**
     * 物件建構子,再產生物件時,一定會呼叫的第一個方法
     * */
    public ItemAdapter(Context aContext)
    {
        mContext = aContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDailyItems(ArrayList<DailyItems> aDailyItems)
    {
        mDailyItems = aDailyItems;
    }

    /**
     * 告訴listView我們有多少筆資料。
     * */
    @Override
    public int getCount()
    {
        return mDailyItems.size();
    }

    /**
     *
     * */
    @Override
    public Object getItem(int index)
    {
        return mDailyItems.get(index);
    }

    @Override
    public long getItemId(int index)
    {
        return index;
    }

    /**
     * 真正listView顯示畫面的方法，回傳畫面的View
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.listview_item_layout,null);
            mDailyItemsView = new DailyItemsView(
                    (ImageView) convertView.findViewById(R.id.imageView_item),
                    (TextView) convertView.findViewById(R.id.textview_category),
                    (TextView) convertView.findViewById(R.id.textview_spend_detail),
                    (TextView) convertView.findViewById(R.id.textview_money),
                    (TextView) convertView.findViewById(R.id.textview_spend_way),
                    (ImageView) convertView.findViewById(R.id.imageView_more));
            convertView.setTag(mDailyItemsView);
        }
        else{
            mDailyItemsView = (DailyItemsView)convertView.getTag();
        }

        String type = getRecordType(mDailyItems.get(position).getImgType());
        String detail = mDailyItems.get(position).getSpendDetail();

        if(detail.isEmpty())
        {
            detail = "自己";
        }
        else{
            detail = "自己/"+detail;
        }

        mDailyItemsView.mCategoryIdTV.setText(type);

        mDailyItemsView.mMoneyTV.setText(String.valueOf(mDailyItems.get(position).getMoney()));
        mDailyItemsView.mSpendDetailTV.setText(detail);

        switch (mDailyItems.get(position).getAction())
        {
            case AddItemActivity.ACTION_PAY:
                {
                    mDailyItemsView.mMoneyTV.setTextColor(mContext.getResources().getColor(R.color.black));
                }
                break;
            case AddItemActivity.ACTION_INCOME:
                {
                    mDailyItemsView.mMoneyTV.setTextColor(mContext.getResources().getColor(R.color.red));
                }
                break;
        }

        mDailyItemsView.mImgTypeIV.setImageDrawable(mContext.getResources().
                getDrawable(RecordTypes.values()[mDailyItems.get(position).getImgType()].getIconResource()));

        return convertView;
    }

    private String getRecordType(int aValue)
    {
        String type="";

        type = mContext.getString(RecordTypes.values()[aValue].getName());

        return type;
    }
}
