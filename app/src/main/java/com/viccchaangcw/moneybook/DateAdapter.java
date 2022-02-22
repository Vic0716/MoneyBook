package com.viccchaangcw.moneybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class DateAdapter  extends BaseAdapter
{
    private final int CLOUMNS = 7;

    private DayView mDayView;

    private ArrayList<DayStructure> mDayStructureArrayList;

    private Context mContext;

    private GridView mGridView;

    private LayoutInflater mInflater;

    public DateAdapter(Context aContext, GridView aGridView)
    {
        mContext = aContext;
        mGridView = aGridView;
        mInflater = LayoutInflater.from(aContext);
    }

    public void setDayStructure(ArrayList<DayStructure> aDayStructureArrayList)
    {
        mDayStructureArrayList = aDayStructureArrayList;
    }

    @Override
    public int getCount()
    {
        if(mDayStructureArrayList == null)
        {
            return 0;
        }
        else{
            return mDayStructureArrayList.size();
        }
    }

    @Override
    public Object getItem(int position)
    {
        if(mDayStructureArrayList == null)
        {
            return null;
        }
        else{
            return mDayStructureArrayList.get(position);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View aConvertView, ViewGroup parent)
    {

        if(aConvertView == null)
        {
            aConvertView = mInflater.inflate(R.layout.gridview_day_layout,null);

            aConvertView.setLayoutParams(new GridView.LayoutParams(MainActivity.mScreenWidth/CLOUMNS,MainActivity.mScreenHeightHalf/6));
            mDayView = new DayView(aConvertView.findViewById(R.id.framelayout_date),aConvertView.findViewById(R.id.textview_day),aConvertView.findViewById(R.id.textview_point));
            aConvertView.setTag(mDayView);
        }
        else{
            mDayView = (DayView)aConvertView.getTag();
        }

        mDayView.mDayTV.setText(mDayStructureArrayList.get(position).mDayNumber);

        //有加記帳過的日期，顯示point
        if(mDayStructureArrayList.get(position).mHasItems)
        {
            mDayView.mPointTV.setVisibility(View.VISIBLE);
        }
        else
        {
            mDayView.mPointTV.setVisibility(View.INVISIBLE);
        }


        //被選定的日期，要改變顏色
        if(mDayStructureArrayList.get(position).mIsFocus)
        {
            mDayView.mDayTV.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else
        {
            mDayView.mDayTV.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        if(mDayStructureArrayList.get(position).mWhichMonth != MainActivity.THE_MONTH)
        {
            mDayView.mDateFL.setBackgroundColor(mContext.getResources().getColor(R.color.last_and_next));
        }
        else{
            mDayView.mDateFL.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }


        return aConvertView;
    }
}
