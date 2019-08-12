package com.example.moneymanager;
import java.time.LocalDate;

public class Income{
    private double sum;
    private String type;
    private LocalDate date;

    public Income (double sum, String type, LocalDate date){
        this.sum = sum;
        this.type= type;
        this.date=date;
    }


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

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Income{" +
                "sum=" + sum +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
