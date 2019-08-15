package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class moneyExpanded extends AppCompatActivity {

    Button btnCancel, btnAddExepense, btnAddIncome;
    ListView lvItems;
    public final int requestCodeActivityAddIncome = 1;
    public final int requestCodeActivityAddExpense = 2;
    ArrayList items = new ArrayList();

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

        MyApplication app = (MyApplication) this.getApplication();
        ArrayList items = app.getItems();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(arrayAdapter);

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
                        intent.putExtra("day",income.getDate().getDay());
                        intent.putExtra("month",income.getDate().getMonth());
                        intent.putExtra("year",income.getDate().getYear());
                        intent.putExtra("rowId",i);
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
                        intent.putExtra("day",expense.getDate().getDay());
                        intent.putExtra("month",expense.getDate().getMonth());
                        intent.putExtra("year",expense.getDate().getYear());
                        startActivity(intent);
                    }
                }
            });
    }
}
