package com.viccchaangcw.moneybook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.viccchaangcw.moneybook.database.databaseTableFiled.DailyRecord;

public class DataBase extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "MoneyBookDB";
    public static final String DAILY_RECORD = "DailyRecord";

    private static final int DATABASE_VERSION =1;

    private static DataBase mInstance;

    //建構子方法，也是一種方法
    public DataBase(Context aContext)
    {
        super(aContext, DATABASE_NAME,null,DATABASE_VERSION);
    }

    public static DataBase getInstance(Context aContext)
    {
        if(mInstance==null)
        {
            mInstance = new DataBase(aContext);
        }

        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE "+ DAILY_RECORD+"("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DailyRecord.ID + " not null,"
                + DailyRecord.DATES +" TEXT not null,"
                + DailyRecord.MONEY + " INTEGER not null,"
                + DailyRecord.ACTIONS + " INTEGER not null,"
                + DailyRecord.DESCRIPTION+ " TEXT not null,"
                + DailyRecord.RECORD_TYPE + " INTEGER not null,"
                + DailyRecord.CREATE_TIME +" TEXT not null,"
                + DailyRecord.MODIFY_TIME +" TEXT" + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
