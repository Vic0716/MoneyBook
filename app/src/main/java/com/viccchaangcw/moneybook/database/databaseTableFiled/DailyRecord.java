package com.viccchaangcw.moneybook.database.databaseTableFiled;

public class DailyRecord
{
    //↓資料庫欄位名
    public static final String ID = "RecordID";
    public static final String DATES = "Dates";

    public static final String MONEY = "Money";
    public static final String ACTIONS = "Actions";

    public static final String DESCRIPTION = "Description";
    public static final String RECORD_TYPE = "RecordType";
    public static final String CREATE_TIME = "CreateTime";
    public static final String MODIFY_TIME = "ModifyTime";
    //↑資料庫欄位名


    public int mMoney;
    public int mAction;
    public int mRecordType;

    public String mId;
    public String mDates;
    public String mDescription;
    public String mCreateTime;
    public String mModifyTime;

    public DailyRecord()
    {

    }

    public DailyRecord(String aId,String aDates,int aMoney,
                       int aAction,String aDescription,
                       int aRecordType,String aCreateTime,
                       String aModifyTime)
    {
        mId = aId;
        mDates = aDates;
        mMoney = aMoney;
        mAction = aAction;
        mDescription = aDescription;
        mRecordType = aRecordType;
        mCreateTime = aCreateTime;
        mModifyTime = aModifyTime;
    }

}
