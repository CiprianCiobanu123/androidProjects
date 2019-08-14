package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String INCOME_ARRAYLIST = "incomes";

    TextView tvAccount ;
    ArrayList<Income> incomes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAccount = findViewById(R.id.tvAccount);

        try
        {
            ExpensesDB db = new ExpensesDB(this);
            db.open();
            incomes =  db.getAllIncomeValues();
            Income incomeTest= incomes.get(2);
            Date date = incomeTest.getDate();

            db.close();
            tvAccount.setText(date.toString());

        }catch(SQLException e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                       com.example.moneymanager.moneyExpanded.class );
                intent.putParcelableArrayListExtra(INCOME_ARRAYLIST, incomes);
                startActivity(intent);
            }
        });
    }
}
