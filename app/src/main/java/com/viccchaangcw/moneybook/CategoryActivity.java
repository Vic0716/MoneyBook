package com.viccchaangcw.moneybook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CategoryActivity extends Activity
{
    private int mSelectIndex = RecordTypes.FOOD.ordinal();

    private ImageView mBackIV;
    private GridView mCategoryGV;

    private ArrayList<CategoryStructure> mCategoryStructureArrayList;

    private CategoryAdatper mCategoryAdatper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity_layout);
        init();
        initClick();
    }

    public void init()
    {
        mBackIV = findViewById(R.id.imageview_back);
        mCategoryGV = findViewById(R.id.gridview_category);

        mCategoryAdatper = new CategoryAdatper(CategoryActivity.this);

        mCategoryStructureArrayList = new ArrayList<>();

        createCategoryItem();
        mCategoryAdatper.setCategoryStructureList(mCategoryStructureArrayList);
        mCategoryGV.setAdapter(mCategoryAdatper);
    }

    private void initClick()
    {

        mBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mCategoryGV.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mCategoryStructureArrayList.get(mSelectIndex).mIsSelect = false;

                mCategoryStructureArrayList.get(position).mIsSelect = true;

                mSelectIndex = position;
                mCategoryAdatper.notifyDataSetChanged();

                Bundle bundle = new Bundle();
                bundle.putInt("category_item",mCategoryStructureArrayList.get(position).mRecordTypes.ordinal());
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }


    private void createCategoryItem()
    {
        int index = 0;

        for(RecordTypes recordTypes: RecordTypes.values())
        {
            if(recordTypes.getName() != R.string.Com_category_income)
            {
                CategoryStructure categoryStructure = new CategoryStructure();
                categoryStructure.mRecordTypes = recordTypes;
                if(index == mSelectIndex)
                {
                    categoryStructure.mIsSelect = true;
                    index++;
                }
                mCategoryStructureArrayList.add(categoryStructure);
            }
        }
    }

}
