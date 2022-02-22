package com.viccchaangcw.moneybook;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.viccchaangcw.moneybook.database.DatabaseManager;
import com.viccchaangcw.moneybook.database.databaseTableFiled.DailyRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity
{
    public static final int THE_MONTH = 0;
    public static final int LAST_MONTH = 1;
    public static final int NEXT_MONTH = 2;

    public static int mScreenWidth;
    public static int mScreenHeight;
    public static int mScreenHeightHalf;

    public int mFocusDay;
    public int mFocusPosition;

    private String mNowYear;
    private String mNowMonth;
    private String mNowDay;

    private Date mNowDate;

    //日曆Adapter的資料
    private ArrayList<DayStructure> mDayStructureArrayList;

    private ImageView mLeftIV,mRightIV;

    private TextView mDateTitleTV;

    private GridView mDateGV;

    private DateAdapter mDateAdapter;

    private ItemAdapter mItemAdapter;

    //每日記帳的Adapter的資料
    private ArrayList<DailyItems> mDailyItemsArrayList;

    private DatabaseManager mDatabaseManager;

    private ImageView mAddItemIV;

    private ListView mItemLV;

    private AlertDialog mDeleteItemAD;

    private FrameLayout mExportLL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenHeightHalf= (int)(displayMetrics.heightPixels/3.5);

        init();
        initBaseAdapter();
        initClick();
        searchDailyItem();
        checkPermission("請給予程式讀取外部資料的權限", Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void init()
    {
        mDatabaseManager = DatabaseManager.getInstance(MainActivity.this);

        mAddItemIV = findViewById(R.id.imageview_add);
        mItemLV = findViewById(R.id.listview_items);

        mLeftIV = findViewById(R.id.imageview_left);
        mRightIV = findViewById(R.id.imageview_right);
        mDateTitleTV = findViewById(R.id.textview_date);

        mDateGV = findViewById(R.id.gridview_date);
        mExportLL = findViewById(R.id.framelayout_export_csv);

        mDateAdapter = new DateAdapter(MainActivity.this,mDateGV);
        mDayStructureArrayList = new ArrayList<>();

        mDateAdapter.setDayStructure(mDayStructureArrayList);

        getNowDate();
        getTheMonthDays(mNowDate);

        mDateTitleTV.setText(getTheYearAndMonth());

        mDateGV.setAdapter(mDateAdapter);

        mDateGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                if(mDayStructureArrayList.get(position).mDayNumber.contains("週"))
                    return;

                /**
                 * 1.改變點選的日期變更時，需要先確定她是點選哪一個月份.
                 * 2.若是上一月或下一月，都必須要先切換月份在focus.
                 * */
                switch (mDayStructureArrayList.get(position).mWhichMonth)
                {
                    case THE_MONTH:
                    {
                        mDayStructureArrayList.get(mFocusPosition).mIsFocus = false;
                        mDayStructureArrayList.get(position).mIsFocus = true;
                        mFocusPosition = position;
                        mFocusDay = Integer.parseInt(mDayStructureArrayList.get(position).mDayNumber);
                        mDateAdapter.notifyDataSetChanged();

                        String titleDate = mDateTitleTV.getText().toString();
                        String[] splitYear = titleDate.split("年");
                        String year = splitYear[0];
                        String month = splitYear[1].split("月")[0];

                        if(month.length()<2)
                        {
                            month ="0"+month;
                        }

                        String day = mDayStructureArrayList.get(position).mDayNumber;

                        if(day.length()<2)
                        {
                            day = "0"+day;
                        }

                        String date = year+"-"+month+"-"+day;

                        try
                        {
                            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                            mNowDate = ft.parse(date);
                        }
                        catch(ParseException e)
                        {
                            e.printStackTrace();
                        }

                        searchDailyItem();
                    }
                    break;
                    case LAST_MONTH:
                    {
                        mFocusDay = Integer.parseInt(mDayStructureArrayList.get(position).mDayNumber);
                        toLastMonth();
                    }
                    break;
                    case NEXT_MONTH:{
                        mFocusDay = Integer.parseInt(mDayStructureArrayList.get(position).mDayNumber);
                        toNextMonth();
                    }
                    break;
                }

            }
        });
    }

    private void initClick()
    {
        mExportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportCSV();
            }
        });

        mItemLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemId = mDailyItemsArrayList.get(position).getDailyItemsID();


                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                String date = ft.format(mNowDate);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("date",date);
                bundle.putString("id",itemId);

                intent.setClass(MainActivity.this,AddItemActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,Utility.ACTIVITY_RESULT_ADD_ITEM);
            }
        });

        mItemLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                createDeleteAlertDialog(position);
                return false;
            }
        });


        mAddItemIV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                String date = ft.format(mNowDate);

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("date",date);

                intent.setClass(MainActivity.this,AddItemActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,Utility.ACTIVITY_RESULT_ADD_ITEM);
            }
        });

        mLeftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLastMonth();
            }
        });

        mRightIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNextMonth();
            }
        });
    }

    public void initBaseAdapter()
    {
        mItemAdapter = new ItemAdapter(MainActivity.this);

        mDailyItemsArrayList = new ArrayList<>();

        mItemAdapter.setDailyItems(mDailyItemsArrayList);

        mItemLV.setAdapter(mItemAdapter);
    }

    //去資料庫搜尋mNowDate日期資料
    private void searchDailyItem()
    {
        ArrayList<DailyRecord> dailyRecord = new ArrayList<>();

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String date = ft.format(mNowDate);

        if(mDatabaseManager.getDailyRecord(dailyRecord,date) == DatabaseManager.DBSuccess.SUCCESS)
        {
            if(mDailyItemsArrayList!=null)
            {
                mDailyItemsArrayList.clear();
            }

            for(int i=0;i<dailyRecord.size();i++)
            {
                 int recordType = dailyRecord.get(i).mRecordType;
                 String dailyItemsId = dailyRecord.get(i).mId;
                 String description = dailyRecord.get(i).mDescription;
                 int money = dailyRecord.get(i).mMoney;
                 int action = dailyRecord.get(i).mAction;
                 int spendWay = SpendWay.CASH;

                DailyItems dailyItems = new DailyItems();
                dailyItems.setImgType(recordType);
                dailyItems.setDailyItemsID(dailyItemsId);
                dailyItems.setSpendDetail(description);
                dailyItems.setMoney(money);
                dailyItems.setSpendWay(spendWay);
                dailyItems.setAction(action);

                mDailyItemsArrayList.add(dailyItems);
            }
            mItemAdapter.notifyDataSetChanged();
        }
    }

    private void toNextMonth()
    {
        String nextDate = "";

        int nowMonth = Integer.parseInt(mNowMonth);

        if(nowMonth==12)
        {
            nowMonth = 1;
            int nowYear = Integer.parseInt(mNowYear);
            nowYear = nowYear+1;
            mNowYear = String.valueOf(nowYear);
        }
        else{
            nowMonth = nowMonth+1;
        }

        mNowMonth = String.valueOf(nowMonth);

        if(mNowMonth.length()<2)
        {
            mNowMonth = "0"+mNowMonth;
        }

        String day = String.valueOf(mFocusDay);
        if(day.length()<2)
        {
            day = "0"+day;
        }
        nextDate = mNowYear + "-" +mNowMonth+"-"+day;

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            mNowDate = ft.parse(nextDate);

        }catch(ParseException e)
        {
            e.printStackTrace();
        }

        getTheMonthDays(mNowDate);
        mDateTitleTV.setText(getTheYearAndMonth());
        mDateAdapter.notifyDataSetChanged();
        searchDailyItem();
    }

    private void toLastMonth()
    {
        String lastDate = "";

        int nowMonth = Integer.parseInt(mNowMonth);

        if(nowMonth==1)
        {
            nowMonth = 12;
            int nowYear = Integer.parseInt(mNowYear);
            nowYear = nowYear-1;
            mNowYear = String.valueOf(nowYear);
        }
        else{
            nowMonth = nowMonth-1;
        }

        mNowMonth = String.valueOf(nowMonth);

        if(mNowMonth.length()<2)
        {
            mNowMonth = "0"+mNowMonth;
        }

        String day = String.valueOf(mFocusDay);
        if(day.length() < 2)
        {
            day = "0"+day;
        }
        lastDate = mNowYear+"-"+mNowMonth+"-"+day;

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            mNowDate = ft.parse(lastDate);

        }catch(ParseException e)
        {
            e.printStackTrace();
        }

        getTheMonthDays(mNowDate);
        mDateTitleTV.setText(getTheYearAndMonth());
        mDateAdapter.notifyDataSetChanged();
        searchDailyItem();
    }

    private String getTheYearAndMonth()
    {
        return mNowYear+"年"+mNowMonth+"月";
    }


    private void getNowDate()
    {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR); //當前年份
        int month = now.get(Calendar.MONTH)+1;//當前月份，注意必須+1
        int day = now.get(Calendar.DATE); //當前日Date

        String years = String.valueOf(year);
        String months = String.valueOf(month);
        String days = String.valueOf(day);

        mNowYear = years;
        mNowMonth = months;
        mNowDay = days;

        mFocusDay = day;

        //單月前面必須補0
        if(months.length() == 1)
        {
            months = "0"+months;
        }

        if(days.length() == 1)
        {
            days = "0"+days;
        }

        String date = years + "-"+ months + "-" + days;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        Log.d("20211222Louis","date: "+date);

        try
        {
            mNowDate = ft.parse(date);
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 整個日曆的核心流程：
     *
     * 1.先找出目前的年月日
     * 2.記錄目前是幾月
     * 3.得出目前月份有幾日
     * 4.得出這月份1號是星期幾
     * 5.得出上個月份有幾日
     * 6.得出下個月份1號是星期幾
     * */
    private void getTheMonthDays(Date aDate)
    {
        if(mDayStructureArrayList!=null)
            mDayStructureArrayList.clear();

        mDayStructureArrayList.add(new DayStructure("週日",false,false,THE_MONTH));
        mDayStructureArrayList.add(new DayStructure("週一",false,false,THE_MONTH));
        mDayStructureArrayList.add(new DayStructure("週二",false,false,THE_MONTH));
        mDayStructureArrayList.add(new DayStructure("週三",false,false,THE_MONTH));
        mDayStructureArrayList.add(new DayStructure("週四",false,false,THE_MONTH));
        mDayStructureArrayList.add(new DayStructure("週五",false,false,THE_MONTH));
        mDayStructureArrayList.add(new DayStructure("週六",false,false,THE_MONTH));

        //為了查詢資料庫下個月份的日子是否紀錄
        int nextMonth;
        int nextYear;

        int lastMonth = 0;
        int lastYear = 0;

        int lastDayOfMonth = 30;

        //取得今天的年月日
        Calendar now = Calendar.getInstance();
        now.setTime(aDate);

        //取得這個月份的天數
        int dayOfMonth = now.getActualMaximum(Calendar.DAY_OF_MONTH);

        //取得當月1日是星期幾，1~7代表日12345六
        int firstWeekNumber = 0,nextWeekFirstNumber=0;

        //為了得到當月的1日是禮拜幾
        Calendar temps = (Calendar)now.clone();
        temps.set(now.get(Calendar.YEAR),now.get(Calendar.MONTH),1);

        //取得當月的1號的星期
        firstWeekNumber = temps.get(Calendar.DAY_OF_WEEK);

        switch(aDate.getMonth())//0~11 月
        {
            //11代表當月是12月，因此必須跨一個年度
            case 11:
            {
                nextYear = now.get(Calendar.YEAR)+1;
                nextMonth = 0;
                //為了得到下個月的1日是禮拜幾
                temps.set(now.get(Calendar.YEAR)+1,0,1);
                //取的下個月1號的星期
                nextWeekFirstNumber = temps.get(Calendar.DAY_OF_WEEK);
            }
            break;
            default:{
                nextYear = now.get(Calendar.YEAR);
                nextMonth = now.get(Calendar.MONTH)+1;
                temps.set(now.get(Calendar.YEAR),now.get(Calendar.MONTH)+1,1);
                nextWeekFirstNumber = temps.get(Calendar.DAY_OF_WEEK);
            }
        }

        //mNowMonth => 1~12
        int nowMonth = Integer.parseInt(mNowMonth);

        String firstDay = "01";
        String lastDate = "";

        /**
         * 取得上一個月的天數。
         * */
        //這個月如果是1月
        if((nowMonth-1) == 0)
        {
            lastMonth = 12;
            lastYear = now.get(Calendar.YEAR)-1;
        }
        else{
            lastMonth = (nowMonth-1);
            lastYear = now.get(Calendar.YEAR);
        }

        //處理單月補零問題
        if(lastMonth<10)
        {
            lastDate =  lastYear + "-" +"0"+String.valueOf(lastMonth) + "-" + firstDay;
        }
        else{
            lastDate =  lastYear + "-" +String.valueOf(lastMonth) + "-" + firstDay;
        }

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        try
        {
            Date date = ft.parse(lastDate);
            now.setTime(date);
            //取得上一個月的天數
            lastDayOfMonth = now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        //找出當月1號星期前，從週日開始的上個月日期號碼，firstWeekNumber:1~7，一個禮拜是７天，扣除掉1號跟最吼起始的這天所以要+2回來
        int startGapDateOfMonth = lastDayOfMonth-firstWeekNumber+2;

        String lastMonthForSearching = String.valueOf(lastMonth);

        if(lastMonth<10)
            lastMonthForSearching = "0"+String.valueOf(lastMonth);

        //填滿上個月到這個月的日期，扣除自己必須減1
        for(int i=0;i<firstWeekNumber-1;i++)
        {
            String lastMonthDays = String.valueOf(startGapDateOfMonth);
            if(lastMonthDays.length()<2)
            {
                lastMonthDays = "0"+lastMonthDays;
            }

            String lastMonthDay =  lastYear + "-" +lastMonthForSearching + "-" + lastMonthDays;
            boolean hasRecord = mDatabaseManager.checkDateHaveRecord(lastMonthDay);

            DayStructure temp = new DayStructure(String.valueOf(startGapDateOfMonth),hasRecord,false,LAST_MONTH);

            mDayStructureArrayList.add(temp);
            startGapDateOfMonth++;
        }

        String nowMonthForSearch = mNowMonth;
        if(mNowMonth.length()<2)
            nowMonthForSearch = "0"+nowMonthForSearch;

        //將這個月的日加入進去
        for(int i=1;i<=dayOfMonth;i++)
        {
            String searchDay = String.valueOf(i);

            if(i<10)
                searchDay = "0"+searchDay;

            String theMonthDay =  mNowYear + "-" +nowMonthForSearch + "-" + searchDay;
            boolean hasRecord = mDatabaseManager.checkDateHaveRecord(theMonthDay);

            DayStructure temp = null;

            if(mFocusDay == i)
            {
                temp = new DayStructure(String.valueOf(i),hasRecord,true,THE_MONTH);
            }
            else{
                temp = new DayStructure(String.valueOf(i),hasRecord,false,THE_MONTH);
            }

            mDayStructureArrayList.add(temp);

            //lesson 9
            if(temp.mIsFocus)
                mFocusPosition = mDayStructureArrayList.size()-1;
        }

        String nextMonthForSearch = String.valueOf(nextMonth+1);
        String nextYearForSearch =  String.valueOf(nextYear);
        if(nextMonthForSearch.length()<2)
            nextMonthForSearch = "0"+nextMonthForSearch;

        int startNumber = 1;
        //填滿下個月到這個月的日期，包含自己那一天所以是0~7
        for(int i=0;i<8-nextWeekFirstNumber;i++)
        {
            String searchDay = String.valueOf(startNumber);

            if(searchDay.length()<2)
                searchDay = "0"+searchDay;

            String theMonthDay =  nextYearForSearch + "-" +nextMonthForSearch + "-" + searchDay;
            boolean hasRecord = mDatabaseManager.checkDateHaveRecord(theMonthDay);

            DayStructure temp = new DayStructure(String.valueOf(startNumber),hasRecord,false,NEXT_MONTH);
            mDayStructureArrayList.add(temp);
            startNumber++;
        }
    }

    private void createDeleteAlertDialog(final int aPosition)
    {
        if(mDeleteItemAD == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("刪除項目")
                    .setMessage("是否刪除記帳項目?")
                    .setPositiveButton("刪除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatabaseManager.deleteDailyRecord(mDailyItemsArrayList.get(aPosition).getDailyItemsID());
                            mDailyItemsArrayList.remove(aPosition);
                            mItemAdapter.notifyDataSetChanged();

                            searchDailyItem();
                            getTheMonthDays(mNowDate);
                            mDateAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            mDeleteItemAD = builder.create();
        }

        if(!mDeleteItemAD.isShowing())
            mDeleteItemAD.show();
    }

    private void exportCSV()
    {
        ArrayList<String[]> csvData = new ArrayList<>();
        String[] title = new String[]{"Primary Key","收入或支出"
                ,"年月日","金錢","消費說明","類別","創建時間","修改時間"};
        csvData.add(title);

        //取得目前年月日
        Calendar now = Calendar.getInstance();
        now.setTime(mNowDate);

        //取得這個月份的天數
        int dayOfMonth = now.getActualMaximum(Calendar.DAY_OF_MONTH);

        //mNowMonth: 1~12
        int nowMonth = Integer.parseInt(mNowMonth);

        if(mNowMonth.length()<2)
        {
            mNowMonth = "0" +mNowMonth;
        }

        String startDate = mNowYear+"-"+mNowMonth+"-"+"01";
        String endDate = mNowYear+"-"+mNowMonth+"-"+dayOfMonth;

        ArrayList<DailyRecord> dailyRecords = new ArrayList<>();

        mDatabaseManager.getMonthDailyRecord(dailyRecords,startDate,endDate);

        for(int i=0;i<dailyRecords.size();i++)
        {
            String[] daily = new String[8];
            daily[0]=dailyRecords.get(i).mId;
            daily[1]=String.valueOf(dailyRecords.get(i).mAction);
            daily[2]=dailyRecords.get(i).mDates;
            daily[3]=String.valueOf(dailyRecords.get(i).mMoney);
            daily[4]=dailyRecords.get(i).mDescription;
            daily[5]=String.valueOf(dailyRecords.get(i).mRecordType);
            daily[6]=dailyRecords.get(i).mCreateTime;
            daily[7]=dailyRecords.get(i).mModifyTime;
            csvData.add(daily);
        }

        String nextLine="\n";
        String dot=",";

        try {
            FileOutputStream output = new FileOutputStream("/sdcard/"+mNowYear+"-"+mNowMonth+".csv");
            for(int i=0;i<csvData.size();i++)
            {
                String[] temp = csvData.get(i);
                for(int j=0;j<temp.length;j++)
                {
                    output.write(temp[j].getBytes());
                    if(j==7)
                    {
                        output.write(nextLine.getBytes());
                    }
                    else{
                        output.write(dot.getBytes());
                    }
                }
            }
            output.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        shareFile();
    }

    private void shareFile()
    {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        String title = "MoneyBook"+"-"+mNowMonth+"月"+"記帳表";
        Uri uri = null;
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        for(int i=0;i<1;i++)
        {
            uri = FileProvider.getUriForFile(MainActivity.this
                    ,"com.viccchaangcw.moneybook.fileproider",new File("/sdcard/"+mNowYear+"-"+mNowMonth+".csv"));
            uriArrayList.add(uri);
        }

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uriArrayList);
        intent.putExtra(Intent.EXTRA_SUBJECT,title);
        intent.setType("FIle/*");
        startActivity(intent);
    }

    private void checkPermission(final String aMessage,final String aPermission)
    {
        //詢問使用者請提供權限
        if(ContextCompat.checkSelfPermission(MainActivity.this,aPermission)!= PackageManager.PERMISSION_GRANTED)
        {
            //第1次被拒絕，第2次進入程式時需要自己自訂視窗詢問使用者權限
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,aPermission))
            {
                showMessageOkCancel(aMessage,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{aPermission},1002);
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this,"請自行至設定開啟應用程式權限",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else{//第一次詢問權限
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{aPermission},1002);
            }
        }
        else{//已經同意權限
            //不需要處理任何事情
        }
    }

    private void showMessageOkCancel(String aMessage, DialogInterface.OnClickListener aOkListener,
                                     DialogInterface.OnClickListener aCancelListener)
    {
        new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(aMessage)
                .setPositiveButton("確認",aOkListener)
                .setNegativeButton("取消",aCancelListener)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1002:
            {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                }
                else if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(MainActivity.this,"請自行至設定開啟應用程式權限",Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },2000);
                }

            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Utility.ACTIVITY_RESULT_ADD_ITEM)
        {
            if(resultCode == RESULT_OK)
            {
                //重新搜尋資料庫
                searchDailyItem();
                getTheMonthDays(mNowDate);
                mDateAdapter.notifyDataSetChanged();
            }
        }

    }
}