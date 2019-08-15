package com.example.moneymanager;
import android.app.Application;

import java.util.ArrayList;

public class MyApplication  extends Application{
    double accountMoney = 0.0;
    private ArrayList items ;

    public void addIncomeToItems(Income income){
        items.add(income);
    }

    public void deleteIncomeFromItems(String income){
        items.remove(income);
    }

    public void deleteExpenseFromItems(Expense expense){
        items.remove(expense);
    }

    public void addExpenseToItems(Expense expense){
        items.add(expense);
    }

    public ArrayList getItems() {
        return items;
    }

    public void setItems(ArrayList items) {
        this.items = items;
    }

    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }
}
