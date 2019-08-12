package com.example.moneymanager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.time.LocalDate;
import java.util.ArrayList;

public class moneyExpanded extends AppCompatActivity {

    Button btnCancel, btnAddExepense, btnAddIncome;
    ListView lvItems;
    public final int requestCodeActivityAddIncome = 1;
    public final int requestCodeActivityAddExpense = 2;
    ArrayList items = new ArrayList();
//    ArrayList expenses = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_expanded);

        btnAddExepense = findViewById(R.id.btnAddExpense);
        btnAddIncome = findViewById(R.id.btnAddIncome);
        btnCancel = findViewById(R.id.btnCancel);
        lvItems = findViewById(R.id.lvItems);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                moneyExpanded.this.finish();
            }
        });

        btnAddExepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(moneyExpanded.this,
                        com.example.moneymanager.AddExpense.class);
                startActivityForResult(intent, requestCodeActivityAddExpense);
            }
        });

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(moneyExpanded.this,
                        com.example.moneymanager.AddIncome.class);
                startActivityForResult(intent, requestCodeActivityAddIncome);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final double amountSpent;
        if (requestCode == requestCodeActivityAddIncome) {
            if (resultCode == RESULT_OK) {
                double sum = data.getDoubleExtra("sum", 0.0);
                int year = data.getIntExtra("year", 0);
                int month = data.getIntExtra("month", 0);
                int day = data.getIntExtra("day", 0);
                String type = data.getStringExtra("type");
                LocalDate date = LocalDate.of(year, month, day);
                Income income = new Income(sum, type, date);
                items.add(income);
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
                lvItems.setAdapter(arrayAdapter);
            }}//END OF IF FOR ACTIVITYINCOME

        if (requestCode == requestCodeActivityAddExpense) {
            if (resultCode == RESULT_OK) {
                double price = data.getDoubleExtra("price",0);
                int cantity = data.getIntExtra("cantity",0);
                int day = data.getIntExtra("day",0);
                int year = data.getIntExtra("year",0);
                int month = data.getIntExtra("month",0);
                String product = data.getStringExtra("product");
                LocalDate date = LocalDate.of(year,month,day);
                amountSpent = cantity * price;
                Expense expense = new Expense(product, price, cantity, date, amountSpent);
                items.add(expense);
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
                lvItems.setAdapter(arrayAdapter);
             }}  // END OF IF FOR ACTIVITYEXPENSE

            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(adapterView.getItemAtPosition(i) instanceof Income){
                        Income income =(Income) adapterView.getItemAtPosition(i);
                        String type = income.getType();
                        double sum = income.getSum();
                        Intent intent = new Intent(moneyExpanded.this,
                                com.example.moneymanager.ShowIncome.class);
                        intent.putExtra("type", type);
                        intent.putExtra("sum", sum);
                        intent.putExtra("day",income.getDate().getDayOfMonth());
                        intent.putExtra("month",income.getDate().getMonthValue());
                        intent.putExtra("year",income.getDate().getYear());
                        startActivity(intent);
                    }else if(adapterView.getItemAtPosition(i) instanceof Expense){
                        Expense expense=(Expense) adapterView.getItemAtPosition(i);
                        String product = expense.getProduct();
                        double cantity = expense.getCantity();
                        double price = expense.getPrice();
                        double amountSpent = expense.getSpent();

                        Intent intent = new Intent(moneyExpanded.this,
                                com.example.moneymanager.ShowExpense.class);
                        intent.putExtra("product",product);
                        intent.putExtra("cantity",cantity);
                        intent.putExtra("price",price);
                        intent.putExtra("amountSpent",amountSpent);
                        intent.putExtra("day",expense.getDate().getDayOfMonth());
                        intent.putExtra("month",expense.getDate().getMonthValue());
                        intent.putExtra("year",expense.getDate().getYear());
                        startActivity(intent);
                    }
                }
            });

    }



}
