package com.viccchaangcw.moneybook;

/**
 * 負責記錄記帳類別的:
 * 1.字串
 * 2.資源圖片
 * */
public enum RecordTypes
{
    FOOD (R.string.Com_category_food,R.drawable.food), //飲食
    DAILY_ITEM(R.string.Com_category_daily_item,R.drawable.daily_item), //日常用品
    TRANSPORTATION(R.string.Com_category_transportation,R.drawable.transportation) , //交通
    GAS (R.string.Com_category_gas,R.drawable.gas), //水電瓦斯
    PHONE(R.string.Com_category_phone,R.drawable.phone), //電話網路
    HOME(R.string.Com_category_home,R.drawable.home), //居家
    CLOTHE(R.string.Com_category_clothe,R.drawable.cloth) , //服飾
    CAR ( R.string.Com_category_car,R.drawable.car), //汽車
    ENTERTAINMENT (R.string.Com_category_entertainment,R.drawable.entertainment), //娛樂
    HAIR(R.string.Com_category_hair,R.drawable.hair), //美容美髮
    SOCIAL(R.string.Com_category_social,R.drawable.social), //交際應酬
    LEARN(R.string.Com_category_learn,R.drawable.learn) , //學習深造
    INSURANCE (R.string.Com_category_Insurance,R.drawable.insurance), //保險
    TAX (R.string.Com_category_tax,R.drawable.tax), //稅金
    MEDICAL(R.string.Com_category_medical,R.drawable.medical), //醫療保健
    EDUCATION(R.string.Com_category_education,R.drawable.education) , //教育
    CREDIT_CARD(R.string.Com_category_credit_card,R.drawable.credit_card) , //卡費
    HOSPITAL(R.string.Com_category_hospital,R.drawable.hospital) , //醫療
    HANDLING_FEE(R.string.Com_category_handing_fee,R.drawable.handing_fee), //手續費
    THREE_C(R.string.Com_category_three_c,R.drawable.three_c), //3C
    OIL(R.string.Com_category_oil,R.drawable.oil) , //加油
    MOTO (R.string.Com_category_moto,R.drawable.moton), //機車
    CAR_LOAN (R.string.Com_category_car_load,R.drawable.car_loan), //汽車貸款
    SCHOOL_LOAN(R.string.Com_category_school_loan,R.drawable.student_loan), //學貸
    RENT(R.string.Com_category_rent,R.drawable.rent), //房租
    PARKING_FEE (R.string.Com_category_parking_fee,R.drawable.parked_car), //停車費
    TOLLS(R.string.Com_category_tolls,R.drawable.tolls), //過路費
    INCOME(R.string.Com_category_income,R.drawable.tax); //收入

    private int mName;
    private int mIconResource;

    //方法名稱與類別名稱相同的方法，就是建構子
     RecordTypes(int aName,int aIconResource)
     {
         mName = aName;
         mIconResource = aIconResource;
     }

     public int getName()
     {
         return mName;
     }

     public int getIconResource()
     {
         return mIconResource;
     }
}
