package com.viccchaangcw.moneybook;

import android.widget.FrameLayout;
import android.widget.TextView;

public class DayView
{
    public FrameLayout mDateFL;

    public TextView mDayTV;

    public TextView mPointTV;

    public DayView(FrameLayout aDateFL,TextView aDayTV,TextView aPointTV)
    {
        mDateFL = aDateFL;
        mDayTV = aDayTV;
        mPointTV = aPointTV;
    }
}
