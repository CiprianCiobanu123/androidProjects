package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Calendar;


public class AddIncome extends AppCompatActivity {

    EditText etType, etSum;
    Button btnAdd, btnCancel, btnDate;
    private Calendar myCalendar = Calendar.getInstance();
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        btnCancel = findViewById(R.id.btnCancel);
        btnAdd = findViewById(R.id.btnAdd);
        etType = findViewById(R.id.etType);
        etSum = findViewById(R.id.etSum);
        btnDate = findViewById(R.id.btnDate);

        day = myCalendar.get(Calendar.DAY_OF_MONTH);
        year = myCalendar.get(Calendar.YEAR);
        month = myCalendar.get(Calendar.MONTH);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                AddIncome.this.finish();
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

                DatePickerDialog dpDialog = new DatePickerDialog(AddIncome.this, listener, year, month, day);
                dpDialog.show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etType.getText().toString().isEmpty()) {
                    Toast.makeText(AddIncome.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else if (btnDate.getText().toString().isEmpty()) {
                    Toast.makeText(AddIncome.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else if (etSum.getText().toString().isEmpty()) {
                    Toast.makeText(AddIncome.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    double sum = Double.parseDouble(etSum.getText().toString().trim());
                    String type = etType.getText().toString().trim();
                    LocalDate date = LocalDate.of(year,month, day);
                    try{
                        ExpensesDB db = new ExpensesDB(AddIncome.this);
                        db.open();
                        db.createEntryIncome(type,sum,date);
                        db.close();
                        Toast.makeText(AddIncome.this, "Succesfully saved", Toast.LENGTH_SHORT).show();
                    }catch(SQLException e){
                        Toast.makeText(AddIncome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                    Intent intent = new Intent();
                    //Sending sum and type
                    intent.putExtra("sum", sum);
                    intent.putExtra("type", type);

//                    //Sending data for DATE
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("year", year);

                    setResult(RESULT_OK, intent);
                    AddIncome.this.finish();
                }
            }
        });
    }
}
