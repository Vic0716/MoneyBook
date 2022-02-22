package com.viccchaangcw.moneybook;

public enum EnumDemo
{
    A(90), //static,他的索引值0 在enum內直接宣告的名稱，就是一個物件
    B(80), //static,他的索引值1
    C(70);  //static,他的索引值2

    int mScope;

    //建構子 = 建構一個物件時，第一個呼叫的方法就叫做建構子
    EnumDemo(int aScope)
    {
        mScope = aScope;
    }



}
