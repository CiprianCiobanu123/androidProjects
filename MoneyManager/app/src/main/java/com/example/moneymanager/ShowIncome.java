package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.LocalDate;

public class ShowIncome extends AppCompatActivity {

    TextView tvShowIncome, tvShowType, tvShowDate;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_income);

        tvShowIncome  =  findViewById(R.id.tvShowIncome);
        tvShowType  =  findViewById(R.id.tvShowType);
        tvShowDate  =  findViewById(R.id.tvShowDate);
        btnDelete  =  findViewById(R.id.btnDelete);


        String type = getIntent().getStringExtra("type");
        double sum = getIntent().getDoubleExtra("sum",0);
        int day = getIntent().getIntExtra("day",0);
        int month = getIntent().getIntExtra("month",0);
        int year = getIntent().getIntExtra("year",0);
        final String rowId = String.valueOf(getIntent().getIntExtra("rowId",0));

        LocalDate date = LocalDate.of(year,month,day);

        tvShowIncome.setText(String.format("+%s", sum));
        tvShowType.setText(String.format("Type: %s", type));
        tvShowDate.setText(String.format("Date: %s", date));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    MyApplication app = (MyApplication) getApplication();
//                    app.deleteIncomeFromItems(Integer.valueOf(rowId));

                    ExpensesDB db = new ExpensesDB(ShowIncome.this);
                    db.open();
                    db.deleteEntryIncome(rowId);

                    db.close();
                    Toast.makeText(ShowIncome.this, "Sucessfully deleted", Toast.LENGTH_SHORT).show();
                    throw new  SQLException();
                }catch(SQLException e){
                    Toast.makeText(ShowIncome.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
