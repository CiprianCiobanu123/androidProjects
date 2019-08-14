package com.example.moneymanager;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Income implements Parcelable {
    private double sum;
    private String type;
    private Date date;

    public Income (double sum, String type, Date date){
        this.sum = sum;
        this.type= type;
        this.date=date;
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
                ", date=" + date +
                '}'+'+';
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
