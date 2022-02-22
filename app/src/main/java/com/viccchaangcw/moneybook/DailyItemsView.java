package com.viccchaangcw.moneybook;

import android.widget.ImageView;
import android.widget.TextView;

public class DailyItemsView
{
    public ImageView mImgTypeIV;

    public TextView mCategoryIdTV;

    public TextView mSpendDetailTV;

    public TextView mSpendWayTV;

    public TextView mMoneyTV;

    public ImageView mMoreIV;

    public DailyItemsView(ImageView aImgTypeIV,
                          TextView aCategoryIdTV,
                          TextView aSpendDetailTV,
                          TextView aMoneyTV,
                          TextView aSpendWayTV,
                          ImageView aMoreIV)
    {
         mImgTypeIV = aImgTypeIV;
         mCategoryIdTV = aCategoryIdTV;
         mSpendDetailTV = aSpendDetailTV;
         mMoneyTV = aMoneyTV;
         mSpendWayTV = aSpendWayTV;
         mMoreIV = aMoreIV;
    }
}
