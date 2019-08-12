package com.example.moneymanager;
import android.app.Application;
public class MyApplication  extends Application{
    double accountMoney = 0.0;

    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }
}
