package com.viccchaangcw.moneybook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.viccchaangcw.moneybook.database.DatabaseManager;
import com.viccchaangcw.moneybook.database.databaseTableFiled.DailyRecord;

public class AddItemActivity extends Activity
{

    public static final int ACTION_PAY = 0;
    public static final int ACTION_INCOME =1;

    private int mAction = ACTION_PAY;

    private boolean mIsModify = false;

    private String mItemId="";

    private RecordTypes mRecordTypes = RecordTypes.FOOD;

    private String mDate;

    private Button mPayBT;
    private Button mIncomeBT;
    private Button mSaveBT;

    private EditText mPriceET;
    private EditText mDetailET;

    private TextView mSaveTV;
    private TextView mDateTV;
    private TextView mCategoryArrowTV;

    private ImageView mCategoryArrowIV;
    private ImageView mCategoryIV;

    private ImageView mCloseIV;

    private FrameLayout mDetailFL;
    private RelativeLayout mCategoryRL;

    private DatabaseManager mDatabaseManager;

    private DailyRecord mDailyRecord;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items_layout);
        //隱藏鍵盤
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = AddItemActivity.this.getIntent().getExtras();
        mDate = bundle.getString("date");
        if(bundle.getString("id")==null) //但代表新增資料
        {
            mIsModify = false;
        }
        else{ //代表他要更新資料
            mItemId = bundle.getString("id");
            mIsModify = true;
        }

        init();
        initClick();
    }

    private void init()
    {
        mDatabaseManager = DatabaseManager.getInstance(AddItemActivity.this);

        if(mIsModify)
            mDailyRecord = mDatabaseManager.getDailyRecordFromId(mItemId);

        mCloseIV = findViewById(R.id.imageView_close);

        mPayBT = findViewById(R.id.button_pay);
        mIncomeBT = findViewById(R.id.button_income);
        mSaveBT = findViewById(R.id.button_save);

        mSaveTV = findViewById(R.id.textView_save);
        mDateTV = findViewById(R.id.textview_date);

        mPriceET = findViewById(R.id.editText_price);
        mDetailET = findViewById(R.id.editText_detail);

        mCategoryArrowIV = findViewById(R.id.imageView_arrow);
        mCategoryArrowTV = findViewById(R.id.textView_category);

        mCategoryIV = findViewById(R.id.imageView_category);

        mDetailFL = findViewById(R.id.framelayout_detail);
        mCategoryRL = findViewById(R.id.relativelayout_category);

        String[] splitDate = mDate.split("-");
        mDateTV.setText(splitDate[0]+"年"+splitDate[1]+"月"+splitDate[2]+"日");
        if(mIsModify)
        {
            changePayAnIncomeState(mDailyRecord.mAction);
            mRecordTypes = RecordTypes.values()[mDailyRecord.mRecordType];
            mCategoryIV.setImageDrawable(AddItemActivity.this.getResources().getDrawable(mRecordTypes.getIconResource()));
            mCategoryArrowTV.setText(mRecordTypes.getName());
            mPriceET.setText(String.valueOf(mDailyRecord.mMoney));
            mDetailET.setText(mDailyRecord.mDescription);
        }
    }

    private void initClick()
    {
        mCategoryRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(AddItemActivity.this,CategoryActivity.class);
                startActivityForResult(intent,Utility.ACTIVITY_RESULT_CHANGE_CATEGORY);
            }
        });

        mSaveTV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String dates = mDate;
                String money = mPriceET.getText().toString().trim();
                String detail = mDetailET.getText().toString().trim();

                if(money.isEmpty())
                {
                    return;
                }

                addDailyRecord(dates,Integer.parseInt(money),mAction,detail,mRecordTypes.ordinal());
            }
        });

        mIncomeBT.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                changePayAnIncomeState(ACTION_INCOME);
            }
        });

        mPayBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePayAnIncomeState(ACTION_PAY);
            }
        });

        mCloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mCategoryArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mDetailFL.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //讓Detail EditText物件取得焦點
                mDetailET.requestFocus();
                //取得系統虛擬鍵盤物件
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //顯示虛擬鍵盤
                inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        mSaveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dates = mDate;
                String money = mPriceET.getText().toString().trim();
                String detail = mDetailET.getText().toString().trim();

                if(money.isEmpty())
                {
                    return;
                }

                addDailyRecord(dates,Integer.parseInt(money),mAction,detail,mRecordTypes.ordinal());
            }
        });
    }

    private void changePayAnIncomeState(int aAction)
    {
        switch(aAction)
        {
            case ACTION_PAY:
            {
                mPayBT.setBackgroundColor(getResources().getColor(R.color.white));
                mPayBT.setTextColor(getResources().getColor(R.color.main_color));
                mIncomeBT.setBackgroundColor(getResources().getColor(R.color.main_color));
                mIncomeBT.setTextColor(getResources().getColor(R.color.white));
                mRecordTypes = RecordTypes.FOOD;
            }
            break;
            case ACTION_INCOME:
            {
                mIncomeBT.setBackgroundColor(getResources().getColor(R.color.white));
                mIncomeBT.setTextColor(getResources().getColor(R.color.main_color));
                mPayBT.setBackgroundColor(getResources().getColor(R.color.main_color));
                mPayBT.setTextColor(getResources().getColor(R.color.white));
                mRecordTypes = RecordTypes.INCOME;
            }
            break;
        }
        mCategoryIV.setImageDrawable(AddItemActivity.this.getResources().getDrawable(mRecordTypes.getIconResource()));
        mCategoryArrowTV.setText(mRecordTypes.getName());


        mAction = aAction;
    }

    //新增一筆記帳到資料
    private void addDailyRecord(String aDate,int aMoney,int aAction,String aDescription,int aRecordType)
    {
        DailyRecord dailyRecord = null;
        String createTime = Utility.convertUnixTimeToString(Utility.getCurrentUnixTime(),Utility.DATE_PATTERN);

        if(mIsModify)
        {
            String uuid = mItemId;
            dailyRecord = new DailyRecord(uuid,aDate,aMoney,aAction,aDescription,aRecordType,createTime,createTime);
            DatabaseManager.DBSuccess dbSuccess = mDatabaseManager.updateDailyRecord(dailyRecord);
        }
        else{
            String uuid = Utility.getUUID();
            dailyRecord = new DailyRecord(uuid,aDate,aMoney,aAction,aDescription,aRecordType,createTime,createTime);
            DatabaseManager.DBSuccess dbSuccess = mDatabaseManager.addDailyRecord(dailyRecord);
        }


        //用在StartActivityForResult，代表回傳成功
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Utility.ACTIVITY_RESULT_CHANGE_CATEGORY)
        {
            if(resultCode == RESULT_OK)
            {
                if(data!=null)
                {
                    mRecordTypes = RecordTypes.values()[data.getExtras().getInt("category_item")];
                    mCategoryIV.setImageDrawable(AddItemActivity.this.getResources().getDrawable(mRecordTypes.getIconResource()));
                    mCategoryArrowTV.setText(mRecordTypes.getName());
                }
            }
        }
    }
}
