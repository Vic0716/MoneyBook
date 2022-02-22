package com.viccchaangcw.moneybook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.viccchaangcw.moneybook.database.databaseTableFiled.DailyRecord;

import java.util.ArrayList;

public class DatabaseManager
{
    private Context mContext = null;

    private DataBase mDataBase = null;

    private SQLiteDatabase mReadableDatabase = null;
    private SQLiteDatabase mWritableDatabase = null;

    private static DatabaseManager mInstance = null;

    public DatabaseManager(Context aContext)
    {
        mContext = aContext;
        init();
    }

    public enum DBSuccess
    {
        SUCCESS,
        ERROR
    }

    public static DatabaseManager getInstance(Context aContext)
    {
        if(mInstance == null)
        {
            mInstance = new DatabaseManager(aContext);
        }

        return mInstance;
    }

    private boolean init()
    {
        try
        {

            if(openDatabase() != DBSuccess.SUCCESS)
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private DBSuccess openDatabase()
    {
        try
        {
            if(mContext!=null && mDataBase == null)
                mDataBase = DataBase.getInstance(mContext);

            if(mDataBase == null)
            {
                return DBSuccess.ERROR;
            }

            mReadableDatabase = mDataBase.getReadableDatabase();//讀取資料庫的內容物件
            mWritableDatabase = mDataBase.getWritableDatabase();//寫入資料庫的內容物件

            if(mReadableDatabase==null || mWritableDatabase ==null)
            {
                return DBSuccess.ERROR;
            }


            return DBSuccess.SUCCESS;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return DBSuccess.ERROR;
        }
    }

    //新增一筆記帳
    public DBSuccess addDailyRecord(DailyRecord aDailyRecord)
    {
        DBSuccess success;

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DailyRecord.ID,aDailyRecord.mId);
            contentValues.put(DailyRecord.DATES,aDailyRecord.mDates);
            contentValues.put(DailyRecord.MONEY,aDailyRecord.mMoney);
            contentValues.put(DailyRecord.ACTIONS,aDailyRecord.mAction);
            contentValues.put(DailyRecord.DESCRIPTION,aDailyRecord.mDescription);
            contentValues.put(DailyRecord.RECORD_TYPE,aDailyRecord.mRecordType);
            contentValues.put(DailyRecord.CREATE_TIME,aDailyRecord.mCreateTime);
            contentValues.put(DailyRecord.MODIFY_TIME,aDailyRecord.mModifyTime);

            long successWrite =  mWritableDatabase.insert(DataBase.DAILY_RECORD,null,contentValues);
            if(successWrite == -1)
            {
                success = DBSuccess.ERROR;
            }
            else{
                success = DBSuccess.SUCCESS;
            }
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
            success = DBSuccess.ERROR;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            success = DBSuccess.ERROR;
        }

        return success;
    }

    //排序ASC:由小到大，DESC:由大到小
    public DBSuccess getDailyRecord(ArrayList<DailyRecord> aDailyRecord,String aDate)
    {
        DBSuccess success;
        Cursor cursor=null;

        try
        {
            cursor = mReadableDatabase.query(DataBase.DAILY_RECORD,null,DailyRecord.DATES+" = '"+aDate+"'",
                    null,null,null,DailyRecord.MODIFY_TIME+" desc");

            //大於0代表此日有記過帳
            if(cursor.getCount()>0)
            {
                DailyRecord dailyRecord;
                while(cursor.moveToNext())
                {
                    dailyRecord = new DailyRecord();
                    dailyRecord.mId = cursor.getString(1);
                    dailyRecord.mDates = cursor.getString(2);
                    dailyRecord.mMoney = cursor.getInt(3);
                    dailyRecord.mAction = cursor.getInt(4);
                    dailyRecord.mDescription = cursor.getString(5);
                    dailyRecord.mRecordType = cursor.getInt(6);
                    dailyRecord.mCreateTime = cursor.getString(7);
                    dailyRecord.mModifyTime = cursor.getString(8);
                    aDailyRecord.add(dailyRecord);
                }
            }
            success = DBSuccess.SUCCESS;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            success = DBSuccess.ERROR;
        }

       return success;

    }

    //排序ASC:由小到大，DESC:由大到小
    public DailyRecord getDailyRecordFromId(String aId)
    {
        Cursor cursor=null;
        DailyRecord dailyRecord = null;
        try
        {
            cursor = mReadableDatabase.query(DataBase.DAILY_RECORD,null,DailyRecord.ID+" = '"+aId+"'",
                    null,null,null,DailyRecord.MODIFY_TIME+" desc");
            //大於0代表此日有記過帳
            if(cursor.getCount()>0) {
                while(cursor.moveToNext())
                {
                    dailyRecord = new DailyRecord();
                    dailyRecord.mId = cursor.getString(1);
                    dailyRecord.mDates = cursor.getString(2);
                    dailyRecord.mMoney = cursor.getInt(3);
                    dailyRecord.mAction = cursor.getInt(4);
                    dailyRecord.mDescription = cursor.getString(5);
                    dailyRecord.mRecordType = cursor.getInt(6);
                    dailyRecord.mCreateTime = cursor.getString(7);
                    dailyRecord.mModifyTime = cursor.getString(8);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return dailyRecord;
    }

    //更新記帳項目
    public DBSuccess updateDailyRecord(DailyRecord aDailyRecord)
    {
        DBSuccess success;

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DailyRecord.ID,aDailyRecord.mId);
            contentValues.put(DailyRecord.DATES,aDailyRecord.mDates);
            contentValues.put(DailyRecord.MONEY,aDailyRecord.mMoney);
            contentValues.put(DailyRecord.ACTIONS,aDailyRecord.mAction);
            contentValues.put(DailyRecord.DESCRIPTION,aDailyRecord.mDescription);
            contentValues.put(DailyRecord.RECORD_TYPE,aDailyRecord.mRecordType);
            contentValues.put(DailyRecord.CREATE_TIME,aDailyRecord.mCreateTime);
            contentValues.put(DailyRecord.MODIFY_TIME,aDailyRecord.mModifyTime);

            long successWrite = mWritableDatabase.update(DataBase.DAILY_RECORD,contentValues,
                    DailyRecord.ID+" = '"+aDailyRecord.mId+"'",null);

            if(successWrite == -1)
            {
                success = DBSuccess.ERROR;
            }
            else
            {
                success =DBSuccess.SUCCESS;
            }
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
            success = DBSuccess.ERROR;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            success = DBSuccess.ERROR;
        }

       return success;
    }


    //刪除記帳資料透過UUID
    public DBSuccess deleteDailyRecord(String aDailyRecordId)
    {

        DBSuccess aSucceess = DBSuccess.SUCCESS;

        try
        {
            long lSuccess = mWritableDatabase.delete(DataBase.DAILY_RECORD,DailyRecord.ID+" = '"+aDailyRecordId+"'",null);

            if(lSuccess == -1)
            {
                aSucceess = DBSuccess.ERROR;
            }
        }
        catch (OutOfMemoryError error)
        {
            aSucceess = DBSuccess.ERROR;
        }
        catch (Exception e)
        {
            aSucceess = DBSuccess.ERROR;
        }

        return aSucceess;
    }

    //檢查此日是否有記帳資料，為了給予日曆顯示日期下面的點
    public boolean checkDateHaveRecord(String aDate)
    {
        boolean hasRecord = false;
        Cursor cursor = null;

        try {
            cursor = mReadableDatabase.query(DataBase.DAILY_RECORD,null,
                    DailyRecord.DATES+" = '"+aDate+"'",null,null,null,DailyRecord.MODIFY_TIME+" desc");

            if(cursor.getCount()>0)
            {
                hasRecord = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

      return hasRecord;
    }

    public DBSuccess getMonthDailyRecord(ArrayList<DailyRecord> aDailyRecord,String aStartDates,String aEndDates) {
        DBSuccess success;
        Cursor cursor = null;
        cursor = mReadableDatabase.query(DataBase.DAILY_RECORD,null,
                DailyRecord.DATES+" BETWEEN '" + aStartDates + "'"+" AND '"+ aEndDates + "'",null,null,null,DailyRecord.MODIFY_TIME+" desc");
        try{
            if(cursor.getCount()>0)
            {
                DailyRecord dailyRecord;
                while(cursor.moveToNext())
                {
                    dailyRecord = new DailyRecord();
                    dailyRecord.mId = cursor.getString(1);
                    dailyRecord.mDates =cursor.getString(2);
                    dailyRecord.mMoney = cursor.getInt(3);
                    dailyRecord.mAction = cursor.getInt(4);
                    dailyRecord.mDescription = cursor.getString(5);
                    dailyRecord.mRecordType = cursor.getInt(6);
                    dailyRecord.mCreateTime = cursor.getString(7);
                    dailyRecord.mModifyTime = cursor.getString(8);
                    aDailyRecord.add(dailyRecord);
                }
            }
            success = DBSuccess.SUCCESS;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            success = DBSuccess.ERROR;
        }
        return success;
    }

}

