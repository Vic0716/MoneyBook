package com.viccchaangcw.moneybook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class Utility
{
    public static final  int ACTIVITY_RESULT_ADD_ITEM = 10001;
    public static final  int ACTIVITY_RESULT_CHANGE_CATEGORY = 10002;

    public static final String DATE_PATTERN = "yyyy-MM-dd_HH:mm:ss";

    public static long getCurrentUnixTime()
    {
        return System.currentTimeMillis();
    }

    public static String convertUnixTimeToString(long aUnixTime,String aPattern)
    {
        return convertUnixTimeToString(aUnixTime,aPattern,TimeZone.getTimeZone("UTC"));
    }

    public static String convertUnixTimeToString(long aUnixTime,String aPattern,TimeZone aTargetTime)
    {
        return covertDateToString(new Date(aUnixTime),aPattern,aTargetTime);
    }


    public static String covertDateToString(Date aDate, String aPattern, TimeZone aTargetZone)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(aPattern, Locale.US);
        dateFormat.setTimeZone(aTargetZone);
        return dateFormat.format(aDate);
    }

    public static String getUUID()
    {
        String tempUUID = UUID.randomUUID().toString();
        String[] buffer = tempUUID.split("-");
        String uuid = "";

        for(int i=0;i<buffer.length;i++)
        {
            uuid+=buffer[i];
        }

        String returnUUID = uuid.substring(0,20);
        return returnUUID;
    }

}
