package com.viccchaangcw.moneybook;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryItemView
{
    public FrameLayout mBackgroundFL;

    public ImageView mIconIV;

    public TextView mNameTV;

    public CategoryItemView(FrameLayout aBackgroundFL, ImageView aIconIV,TextView aNameTV )
    {
        mBackgroundFL = aBackgroundFL;
        mIconIV = aIconIV;
        mNameTV = aNameTV;
    }
}
