package com.viccchaangcw.moneybook;

public class DayStructure
{
    //m = member value成員變數，全域變數
    public String mDayNumber;

    //判斷這個Day是否被使用者所點選
    public boolean mIsFocus;

    //判斷這個日期有沒有被記帳過
    public boolean mHasItems;

    //判斷目前這個Day是屬於哪一個月，上個月、這個月、下個月
    public int mWhichMonth;

    //跟類別名稱相同的方法就叫做建構子，建構子在類別被產生成物件的時候會被呼叫  arguments = 參數
    public DayStructure(String aDayNumber,boolean aHasItems,boolean aIsFocus,int aWhichMonth)
    {
        mDayNumber = aDayNumber;
        mHasItems = aHasItems;
        mIsFocus = aIsFocus;
        mWhichMonth = aWhichMonth;
    }
}
