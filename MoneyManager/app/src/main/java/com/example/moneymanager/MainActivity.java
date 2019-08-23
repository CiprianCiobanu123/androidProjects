package com.example.moneymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    public static final String INCOME_ARRAYLIST = "incomes";
    public static final int waitForCancel = 1;

    TextView tvAccount, tvCurrency;
    Spinner spinnerCurrency;
    ArrayList<Income> incomes = new ArrayList<>();
    ArrayList<Expense> expenses = new ArrayList<>();
    ArrayList items = new ArrayList();
    private static final String[] paths = {"LEU", "USD", "EURO"};

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAccount = findViewById(R.id.tvAccount);
        tvCurrency = findViewById(R.id.tvCurrency);
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        spinnerCurrency.setVisibility(GONE);

        prefs = getSharedPreferences("com.mycompany.MoneyManager", MODE_PRIVATE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);


        tvCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putBoolean("firstrun", true).apply();
            }
        });

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                tvCurrency.setText(adapterView.getItemAtPosition(position).toString().trim());
                prefs.edit().putString("currency", tvCurrency.getText().toString().trim()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {
            ExpensesDB db = new ExpensesDB(this);
            db.open();
            incomes = db.getAllIncomeValues();
            expenses = db.getAllExpenseValues();
            db.close();
            for (int i = 0; i < incomes.size(); i++) {
                items.add(incomes.get(i));
            }
            for (int i = 0; i < expenses.size(); i++) {
                items.add(expenses.get(i));
            }

            MyApplication app = (MyApplication) MainActivity.this.getApplication();
            app.setItems(items);

            double totalAccount = 0;

            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) instanceof Income) {
                    totalAccount = totalAccount + ((Income) items.get(i)).getSum();
                } else {
                    totalAccount = totalAccount - ((Expense) items.get(i)).getSpent();
                }
            }

            if (totalAccount == 0) {
                tvAccount.setText("0");
            } else if (totalAccount > 0) {
                tvAccount.setText(String.valueOf("+ " + totalAccount));
                tvAccount.setTextColor(Color.parseColor("#388e3c"));
            } else {
                tvAccount.setText(String.valueOf("" + totalAccount));
                tvAccount.setTextColor(Color.parseColor("#b91400"));
            }

        } catch (SQLException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        com.example.moneymanager.moneyExpanded.class);
                startActivityForResult(intent, waitForCancel);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            spinnerCurrency.setVisibility(View.VISIBLE);

            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        } else {
            tvCurrency.setText(prefs.getString("currency", ""));
        }
    }
}

