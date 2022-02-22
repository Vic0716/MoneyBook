package com.viccchaangcw.moneybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class CategoryAdatper extends BaseAdapter
{
    private ArrayList<CategoryStructure> mCategoryStructureList;

    private Context mContext;

    private LayoutInflater mInflater;
    private CategoryItemView mCategoryItemView;

    public CategoryAdatper(Context aContext)
    {
        mContext = aContext;
        mInflater = LayoutInflater.from(aContext);
    }

    public void setCategoryStructureList(ArrayList<CategoryStructure> aCategoryStructureList)
    {
        mCategoryStructureList = aCategoryStructureList;
    }

    @Override
    public int getCount()
    {
        if(mCategoryStructureList == null)
            return 0;
        else
            return mCategoryStructureList.size();
    }

    @Override
    public Object getItem(int position)
    {
        if(mCategoryStructureList==null)
            return null;
         else
            return  mCategoryStructureList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.category_adapter_layout,null);

            convertView.setLayoutParams(new ViewGroup.LayoutParams(MainActivity.mScreenWidth/3,
                    (MainActivity.mScreenHeightHalf*2)/3));
            mCategoryItemView = new CategoryItemView(convertView.findViewById(R.id.framelayout_background),
                    convertView.findViewById(R.id.imageview_icon),convertView.findViewById(R.id.textview_name));

            convertView.setTag(mCategoryItemView);
        }
        else{
            mCategoryItemView = (CategoryItemView)convertView.getTag();
        }

        mCategoryItemView.mNameTV.setText(mCategoryStructureList.get(position).mRecordTypes.getName());

        if(mCategoryStructureList.get(position).mIsSelect)
        {
            mCategoryItemView.mBackgroundFL.setBackground(mContext.getResources().getDrawable(R.drawable.item_selected_background));
        }
        else{
            mCategoryItemView.mBackgroundFL.setBackground(mContext.getResources().getDrawable(R.drawable.item_normal_background));
        }

        mCategoryItemView.mIconIV.setImageDrawable(mContext.getDrawable(mCategoryStructureList.get(position).mRecordTypes.getIconResource()));


        return convertView;
    }
}
