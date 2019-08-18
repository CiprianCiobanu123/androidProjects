package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;

public class ShowExpense extends AppCompatActivity {

    TextView tvShowExpenseProduct, tvShowExpensePrice, tvShowExpenseCantity, tvShowExpenseDate, tvShowExpenseTotalSpent;
    Button  btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        tvShowExpenseProduct = findViewById(R.id.tvShowExpenseProduct);
        tvShowExpensePrice = findViewById(R.id.tvShowExpensePrice);
        tvShowExpenseCantity = findViewById(R.id.tvShowExpenseCantity);
        tvShowExpenseDate = findViewById(R.id.tvShowExpenseDate);
        tvShowExpenseTotalSpent = findViewById(R.id.tvShowExpenseTotalSpent);
        btnEdit = findViewById(R.id.btnEdit);


       final String product = getIntent().getStringExtra("product");
       final double price = getIntent().getDoubleExtra("price",0);
        final int cantity = getIntent().getIntExtra("cantity",0);
        final double amountSpent = getIntent().getDoubleExtra("amountSpent",0);
        final int day = getIntent().getIntExtra("day",0);
        final int month = getIntent().getIntExtra("month",0);
        final int year = getIntent().getIntExtra("year",0);


        LocalDate date = LocalDate.of(year,month,day);

        tvShowExpenseProduct.setText(String.format("Product: %s", product));
        tvShowExpensePrice.setText(String.format("Price: %s", price));
        tvShowExpenseCantity.setText(String.format("Cantity: %s", cantity));
        tvShowExpenseDate.setText(String.format("Date: %s", date));
        tvShowExpenseTotalSpent.setText("-" + amountSpent);

    }



}
