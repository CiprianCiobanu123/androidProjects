package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;

public class AddExpense extends AppCompatActivity {

    EditText etProduct, etPrice, etCantity;
    Button btnAdd, btnCancel, btnDate;
    private Calendar myCalendar = Calendar.getInstance();
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        btnDate = findViewById(R.id.btnDate);
        etPrice = findViewById(R.id.etPrice);
        etProduct = findViewById(R.id.etProduct);
        etCantity = findViewById(R.id.etCantity);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                AddExpense.this.finish();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }

            void DateDialog() {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        btnDate.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                };

                DatePickerDialog dpDialog = new DatePickerDialog(AddExpense.this, listener, year, month, day);
                dpDialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPrice.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddExpense.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (etProduct.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddExpense.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (btnDate.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddExpense.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (etCantity.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddExpense.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else {

                    String product = etProduct.getText().toString().trim();
                    int cantity = Integer.parseInt(etCantity.getText().toString().trim());
                    double price = Double.parseDouble(etPrice.getText().toString().trim());


                    try {
                        ExpensesDB db = new ExpensesDB(AddExpense.this);
                        db.open();
                        db.createEntryExpense(product, price, cantity, day, month, year);
                        MyApplication app = (MyApplication) AddExpense.this.getApplication();
                        app.addExpenseToItems(new Expense(product, price, cantity, day, month, year));
                        db.close();
                        Toast.makeText(AddExpense.this, "Succesfully Saved", Toast.LENGTH_SHORT).show();
                        throw new SQLException();
                    } catch (SQLException e) {
                        Toast.makeText(AddExpense.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent();
                    intent.putExtra("cantity", cantity);
                    intent.putExtra("price", price);
                    intent.putExtra("product", product);
                    intent.putExtra("day", day);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    setResult(RESULT_OK, intent);
                    AddExpense.this.finish();

                }
            }
        });

    }


}
