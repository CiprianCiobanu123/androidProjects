package com.example.moneymanager;

import java.time.LocalDate;
import java.util.Date;

public class Expense {

    private String product;
    private String id;
    private double price;
    private int cantity;
    private LocalDate date;
    private double spent;
    private int dayExpense;
    private int monthExpense;
    private int yearExpense;

    @Override
    public String toString() {
        return "Expense{" +
                "product='" + product + '\'' +
                ", price=" + price +
                ", quantity=" + cantity +
                ", date=" + date +
                ", spent=" + spent +
                ", dayExpense=" + dayExpense +
                ", monthExpense=" + monthExpense +
                ", yearExpense=" + yearExpense +
                '}';
    }

    public int getDayExpense() {
        return dayExpense;
    }

    public void setDayExpense(int dayExpense) {
        this.dayExpense = dayExpense;
    }

    public int getMonthExpense() {
        return monthExpense;
    }

    public void setMonthExpense(int monthExpense) {
        this.monthExpense = monthExpense;
    }

    public int getYearExpense() {
        return yearExpense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setYearExpense(int yearExpense) {
        this.yearExpense = yearExpense;
    }

    public Expense(String product, double price, int cantity, int dayExpense, int monthExpense, int yearExpense, String id){
        this.product = product;
        this.price = price;
        this.cantity = cantity;
        this.id = id;
        this.spent = cantity * price;
        this.dayExpense = dayExpense;
        this.monthExpense = monthExpense;
        this.yearExpense=yearExpense;
        date = LocalDate.of(yearExpense,monthExpense,dayExpense);
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
