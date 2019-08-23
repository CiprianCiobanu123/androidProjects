package com.example.moneymanager;

public class Currency {
    private String currency;
    private String id;

    public Currency(String currency, String id){
        this.currency = currency;
        this.id = id;
    }

    public Currency() {

    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
