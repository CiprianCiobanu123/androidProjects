package com.example.moneymanager;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Income implements Parcelable {
    private double sum;
    private String type;
    private int dayIncome;
    private int monthIncome;
    private int yearIncome;
    private Date date;
    public Income (double sum, String type, int dayIncome, int monthIncome, int yearIncome){
        this.sum = sum;
        this.type= type;
        this.dayIncome= dayIncome;
        this.monthIncome=monthIncome;
        this.yearIncome=yearIncome;
        date = new Date(yearIncome,monthIncome,dayIncome);
    }


    protected Income(Parcel in) {
        sum = in.readDouble();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(sum);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Income> CREATOR = new Creator<Income>() {
        @Override
        public Income createFromParcel(Parcel in) {
            return new Income(in);
        }

        @Override
        public Income[] newArray(int size) {
            return new Income[size];
        }
    };

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Income{" +
                "sum=" + sum +
                ", type='" + type + '\'' +
                ", dayIncome=" + dayIncome +
                ", monthIncome=" + monthIncome +
                ", yearIncome=" + yearIncome +
                ", date=" + date +
                '}';
    }

    public int getDayIncome() {
        return dayIncome;
    }

    public void setDayIncome(int dayIncome) {
        this.dayIncome = dayIncome;
    }

    public int getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(int monthIncome) {
        this.monthIncome = monthIncome;
    }

    public int getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(int yearIncome) {
        this.yearIncome = yearIncome;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
