package com.example.moneymanager;

import java.time.LocalDate;
import java.util.Date;

public class Expense {

    private String product;
    private double price;
    private int cantity;
    private Date date;
    private double spent;

    public Expense(String product, double price, int cantity, Date date, double spent){
        this.product = product;
        this.price = price;
        this.cantity = cantity;
        this.date = date;
        this.spent = spent;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCantity() {
        return cantity;
    }

    public void setCantity(int cantity) {
        this.cantity = cantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "product='" + product + '\'' +
                ", price=" + price +
                ", cantity=" + cantity +
                ", date=" + date +
                ", Total spent=" + spent +
                '}'+'-';
    }
}
