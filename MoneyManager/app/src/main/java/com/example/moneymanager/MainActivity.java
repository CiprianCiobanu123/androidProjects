package com.example.moneymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static android.view.View.GONE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SHORT;

public class MainActivity extends AppCompatActivity {

    public static final int waitForCancel = 1;

    TextView tvAccount, tvCurrency, tvDailyMonthlyYearly;
    Button btnSort, btnChangeCurrency;
    Spinner spinnerCurrency, spinnerMonthly;
    LinearLayout llAccount, hlForBackground;
    ArrayList<Income> incomes = new ArrayList<>();
    ArrayList<Expense> expenses = new ArrayList<>();
    ArrayList items = new ArrayList();
    SharedPreferences prefs = null;
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);

    private static final String[] paths = {"RON",
            "USD",
            "EUR",
            "AFN",
            "ALL",
            "DZD",
            "AOA",
            "ARS",
            "BSD",
            "BOB",
            "BGN",
            "BIF",
            "CNY",
            "CUP",
            "JPY",
            "KWD",
    };

    private static final String[] valuesToShowAccount = {"Yearly", "Monthly", "Daily"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAccount = findViewById(R.id.tvAccount);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvDailyMonthlyYearly = findViewById(R.id.tvDailyMonthlyYearly);
        btnSort = findViewById(R.id.btnSort);
        btnChangeCurrency = findViewById(R.id.btnChangeCurrency);
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        spinnerMonthly = findViewById(R.id.spinnerMonthly);
        llAccount = findViewById(R.id.llAccount);
        hlForBackground = findViewById(R.id.hlForBackground);
        spinnerCurrency.setVisibility(GONE);
        spinnerMonthly.setVisibility(GONE);

        tvDailyMonthlyYearly.setText("Yearly");
        tvDailyMonthlyYearly.setTextColor(Color.parseColor("#790e8b"));
        tvCurrency.setTextColor(Color.parseColor("#790e8b"));

        prefs = getSharedPreferences("com.mycompany.MoneyManager", 0);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, paths);

        final ArrayAdapter<String> adapterMonthly = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, valuesToShowAccount);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);
        spinnerMonthly.setAdapter(adapterMonthly);
        Arrays.sort(paths);


        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        final AlertDialog.Builder b1 = new AlertDialog.Builder(this);
        b.setTitle("Change Currency");
        b1.setTitle("Sort");

        b.setItems(paths, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                prefs.edit().putString("currency", spinnerCurrency.getAdapter().getItem(which).toString()).apply();
                tvCurrency.setText(spinnerCurrency.getAdapter().getItem(which).toString());
            }
        });

        b1.setItems(valuesToShowAccount, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvDailyMonthlyYearly.setText(spinnerMonthly.getAdapter().getItem(which).toString().trim());
                dialog.dismiss();

            }
        });

        btnChangeCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.show();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.show();
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

        spinnerMonthly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                tvDailyMonthlyYearly.setText(adapterView.getItemAtPosition(position).toString().trim());

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
            e.printStackTrace();
        }

        llAccount.setOnClickListener(new View.OnClickListener() {
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

