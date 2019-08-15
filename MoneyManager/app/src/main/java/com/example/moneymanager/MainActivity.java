package com.example.moneymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String INCOME_ARRAYLIST = "incomes";
    public static final int waitForCancel = 1;

    TextView tvAccount ;
    ArrayList<Income> incomes = new ArrayList<>();
    ArrayList<Expense> expenses = new ArrayList<>();
    ArrayList items = new ArrayList();

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
            expenses = db.getAllExpenseValues();
            for(int i = 0; i <incomes.size(); i++){
                items.add(incomes.get(i));
            }
            for(int i=0;i<expenses.size();i++){
                items.add(expenses.get(i));
            }
            double totalAccount = 0;

            for(int i =0; i<items.size();i++){
            if(items.get(i) instanceof Income){
                totalAccount = totalAccount + ((Income) items.get(i)).getSum();
            }else {
                totalAccount = totalAccount -  ((Expense) items.get(i)).getSpent();
            }
        }

            MyApplication app = (MyApplication) this.getApplication();
            app.setItems(items);

            db.close();
            tvAccount.setText(String.valueOf(totalAccount));

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
                startActivityForResult(intent,waitForCancel);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }
}

