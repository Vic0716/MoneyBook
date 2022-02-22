package com.viccchaangcw.moneybook;


/**
 * 1.圖片類型
 * 2.類別id
 * 3.消費細節
 * 4.消費金額
 * 5.消費方式
 * 6.收入或支出
 * 
 * 資料模型使用POJO模式
 * */
public class DailyItems
{
    private int mImgType;

    private String mDailyItemsID;

    private String mSpendDetail;

    private int mMoney;

    private int mSpendWay;

    private int mAction;

    /**
     * 方法宣告語法:
     *
     * [修飾字] 回傳值 方法名稱([方法條件參數])
     * {
     *     方法主體
     * }
     *
     * [] = 可有可無
     *
     * */
    public int getImgType()
    {
        return mImgType;
    }

    public void setImgType(int aImgType)
    {
        mImgType = aImgType;
    }

    public String getDailyItemsID()
    {
        return mDailyItemsID;
    }

    public void setDailyItemsID(String aDailyItemsID)
    {
        mDailyItemsID = aDailyItemsID;
    }

    public String getSpendDetail()
    {
        return mSpendDetail;
    }

    public void setSpendDetail(String aSpendDetail)
    {
        mSpendDetail = aSpendDetail;
    }

    public int getMoney()
    {
        return mMoney;
    }

    public void setMoney(int aMoney)
    {
        mMoney = aMoney;
    }

    public int getSpendWay()
    {
        return mSpendWay;
    }

    public void setSpendWay(int aSpendWay)
    {
        mSpendWay = aSpendWay;
    }

    public int getAction(){
        return mAction;
    }

    public void setAction(int aAction)
    {
        mAction = aAction;
    }

}
