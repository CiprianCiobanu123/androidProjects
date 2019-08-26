package com.example.moneymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    TextView tvAccount, tvCurrency, tvDailyMonthlyYearly, tvMonthOrYear,
            tvBalanceIncomes, tvBalanceExpense, tvIncomesSum, tvExpenseSum,
            tvCurrencyIncomes, tvCurrencyExpenses;

    Button btnChangeCurrency, btnAddExepense, btnAddIncome;
    Spinner spinnerCurrency, spinnerMonthly;
    LinearLayout llAccount, hlForBackground;
    ArrayList<Income> incomes = new ArrayList<>();
    ArrayList<Expense> expenses = new ArrayList<>();
    ArrayList items = new ArrayList();
    SharedPreferences prefs = null;
    ProgressBar pb;

    double valueIncomes, valueExpenses;
    Calendar calendar = Calendar.getInstance();


    public final int requestCodeActivityAddIncome = 1;
    public final int requestCodeActivityAddExpense = 2;

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

    private static final String[] valuesToShowAccount = {"Yearly", "Monthly"};

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.monthlyYearly:
                Toast.makeText(this, "Yearly", Toast.LENGTH_SHORT).show();
                break;
            case R.id.changeCurrency:
                Toast.makeText(this, "Currency", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        tvAccount = findViewById(R.id.tvAccount);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvBalanceIncomes = findViewById(R.id.tvBalanceIncomes);
        tvBalanceExpense = findViewById(R.id.tvBalanceExpense);
        tvIncomesSum = findViewById(R.id.tvIncomesSum);
        tvExpenseSum = findViewById(R.id.tvExpenseSum);
        tvCurrencyIncomes = findViewById(R.id.tvCurrencyIncomes);
        tvCurrencyExpenses = findViewById(R.id.tvCurrencyExpenses);
        tvDailyMonthlyYearly = findViewById(R.id.tvDailyMonthlyYearly);
        btnChangeCurrency = findViewById(R.id.btnChangeCurrency);
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        spinnerMonthly = findViewById(R.id.spinnerMonthly);
        llAccount = findViewById(R.id.llAccount);
        hlForBackground = findViewById(R.id.hlForBackground);
        tvMonthOrYear = findViewById(R.id.tvMonthOrYear);
        btnAddExepense = findViewById(R.id.btnAddExpense);
        btnAddIncome = findViewById(R.id.btnAddIncome);

        tvMonthOrYear.setText(calendar.getDisplayName(MONTH, Calendar.LONG, Locale.getDefault()));

        tvMonthOrYear.setTextColor(Color.parseColor("#ef9a9a"));
        tvIncomesSum.setTextColor(Color.parseColor("#388e3c"));
        tvExpenseSum.setTextColor(Color.parseColor("#b91400"));
        tvCurrency.setTextColor((Color.BLACK));

        spinnerCurrency.setVisibility(GONE);
        spinnerMonthly.setVisibility(GONE);

        tvDailyMonthlyYearly.setText("Balance");
        tvBalanceIncomes.setText("Income");
        tvBalanceExpense.setText("Expense");

        prefs = getSharedPreferences("com.mycompany.MoneyManager", MainActivity.MODE_PRIVATE);

        btnAddExepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        AddExpense.class);
                startActivityForResult(intent, requestCodeActivityAddExpense);
            }
        });

        btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        AddIncome.class);
                startActivityForResult(intent, requestCodeActivityAddIncome);
            }
        });

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
                prefs.edit().putString("currency", spinnerCurrency.getAdapter().getItem(which).toString()).apply();
                tvCurrency.setText(spinnerCurrency.getAdapter().getItem(which).toString());
                tvCurrencyExpenses.setText(spinnerCurrency.getAdapter().getItem(which).toString());
                tvCurrencyIncomes.setText(spinnerCurrency.getAdapter().getItem(which).toString());
                dialog.dismiss();
            }
        });

        b1.setItems(valuesToShowAccount, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (spinnerMonthly.getAdapter().getItem(which).toString().trim().equals("Yearly")) {
                    tvMonthOrYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
                    prefs.edit().putString("monthlyOrYearly", "Yearly").apply();
                    prefs.edit().putString("year", String.valueOf(calendar.get(Calendar.YEAR))).apply();
                    valueExpenses = 0;
                    valueIncomes = 0;
                    try {
                        ExpensesDB db = new ExpensesDB(MainActivity.this);
                        db.open();

                        incomes = db.getIncomesByyear(String.valueOf(calendar.get(Calendar.YEAR)));
                        expenses = db.getExpensesByYear(String.valueOf(calendar.get(Calendar.YEAR)));

                        db.close();
                        items.clear();
                        for (int i = 0; i < incomes.size(); i++) {
                            items.add(incomes.get(i));
                            valueIncomes = valueIncomes + incomes.get(i).getSum();
                        }
                        for (int i = 0; i < expenses.size(); i++) {
                            items.add(expenses.get(i));
                            valueExpenses = valueExpenses + expenses.get(i).getSpent();
                        }

                        MyApplication app = (MyApplication) MainActivity.this.getApplication();
                        app.setItems(items);
                        tvIncomesSum.setText(valueIncomes + "");
                        tvExpenseSum.setText(valueExpenses + "");

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
                    }
                } else {
                    tvMonthOrYear.setText(calendar.getDisplayName(MONTH, Calendar.LONG, Locale.getDefault()));
                    prefs.edit().putString("monthlyOrYearly", "Monthly").commit();
                    prefs.edit().putString("month", String.valueOf(calendar.get(MONTH))).commit();

                    valueExpenses = 0;
                    valueIncomes = 0;

                    try {
                        ExpensesDB db = new ExpensesDB(MainActivity.this);
                        db.open();

                        incomes = db.getIncomesByMonthAndYear(calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(calendar.get(Calendar.YEAR)));
                        expenses = db.getExpensesByMonthAndYear(calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(calendar.get(Calendar.YEAR)));

                        db.close();
                        items.clear();

                        for (int i = 0; i < incomes.size(); i++) {
                            items.add(incomes.get(i));
                            valueIncomes = valueIncomes + incomes.get(i).getSum();

                        }
                        for (int i = 0; i < expenses.size(); i++) {
                            items.add(expenses.get(i));
                            valueExpenses = valueExpenses + expenses.get(i).getSpent();

                        }

                        MyApplication app = (MyApplication) MainActivity.this.getApplication();
                        app.setItems(items);

                        tvIncomesSum.setText(valueIncomes + "");
                        tvExpenseSum.setText(valueExpenses + "");

                        double totalAccount = 0;

                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i) instanceof Income) {
                                totalAccount = totalAccount + ((Income) items.get(i)).getSum();
                                tvIncomesSum.setText(totalAccount + "");

                            } else {
                                totalAccount = totalAccount - ((Expense) items.get(i)).getSpent();
                                tvExpenseSum.setText(totalAccount + "");

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
                    }
                }
                dialog.dismiss();
            }
        });


        btnChangeCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.show().getWindow().setLayout(1000, 2000);
            }
        });

        tvMonthOrYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.show();
            }
        });

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                tvCurrency.setText(adapterView.getItemAtPosition(position).toString().trim());
                tvCurrencyExpenses.setText(adapterView.getItemAtPosition(position).toString().trim());
                tvCurrencyIncomes.setText(adapterView.getItemAtPosition(position).toString().trim());
                prefs.edit().putString("currency", tvCurrency.getText().toString().trim()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinnerMonthly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        com.example.moneymanager.moneyExpanded.class);
                startActivityForResult(intent, waitForCancel);
            }
        });

        if (prefs.getString("monthlyOrYearly", "").equals("Yearly")) {
            tvMonthOrYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            prefs.edit().putString("year", String.valueOf(calendar.get(Calendar.YEAR)));
            valueExpenses = 0;
            valueIncomes = 0;
            try {
                ExpensesDB db = new ExpensesDB(MainActivity.this);
                db.open();

                incomes = db.getIncomesByyear(String.valueOf(calendar.get(Calendar.YEAR)));
                expenses = db.getExpensesByYear(String.valueOf(calendar.get(Calendar.YEAR)));

                db.close();
                items.clear();
                for (int i = 0; i < incomes.size(); i++) {
                    items.add(incomes.get(i));
                    valueIncomes = valueIncomes + incomes.get(i).getSum();
                }
                for (int i = 0; i < expenses.size(); i++) {
                    items.add(expenses.get(i));
                    valueExpenses = valueExpenses + expenses.get(i).getSpent();
                }

                MyApplication app = (MyApplication) MainActivity.this.getApplication();
                app.setItems(items);
                tvIncomesSum.setText(valueIncomes + "");
                tvExpenseSum.setText(valueExpenses + "");

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
            }
        } else {
            prefs.edit().putString("month", String.valueOf(calendar.get(MONTH)));

            valueExpenses = 0;
            valueIncomes = 0;

            try {
                ExpensesDB db = new ExpensesDB(MainActivity.this);
                db.open();

                incomes = db.getIncomesByMonthAndYear(calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(calendar.get(Calendar.YEAR)));
                expenses = db.getExpensesByMonthAndYear(calendar.getDisplayName(MONTH, SHORT, Locale.getDefault()), String.valueOf(calendar.get(Calendar.YEAR)));

                db.close();
                items.clear();

                for (int i = 0; i < incomes.size(); i++) {
                    items.add(incomes.get(i));
                    valueIncomes = valueIncomes + incomes.get(i).getSum();

                }
                for (int i = 0; i < expenses.size(); i++) {
                    items.add(expenses.get(i));
                    valueExpenses = valueExpenses + expenses.get(i).getSpent();

                }

                MyApplication app = (MyApplication) MainActivity.this.getApplication();
                app.setItems(items);

                tvIncomesSum.setText(valueIncomes + "");
                tvExpenseSum.setText(valueExpenses + "");

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
            }
        }
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
            Calendar calendar = Calendar.getInstance();
            // Do first run stuff here then set 'firstrun' as false
            tvCurrency.setText("EUR");
            tvCurrencyExpenses.setText("EUR");
            tvCurrencyIncomes.setText("EUR");

            prefs.edit().putString("monthlyOrYearly", "Monthly").commit();
            prefs.edit().putString("currency", "EUR").commit();
            prefs.edit().putString("month", String.valueOf(calendar.get(MONTH))).commit();
//            prefs.edit().putString("year", String.valueOf(calendar.get(calendar.get(Calendar.YEAR)))).commit();

            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        } else {
            tvCurrency.setText(prefs.getString("currency", ""));
            tvCurrencyExpenses.setText(prefs.getString("currency", ""));
            tvCurrencyIncomes.setText(prefs.getString("currency", ""));
        }
    }
}

