package com.example.moneymanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class moneyExpanded extends AppCompatActivity {

    Button btnAddExepense, btnAddIncome, nextDay, btnPrevious;
    ListView lvItems;
    TextView tvToday;
    public final int requestCodeActivityAddIncome = 1;
    public final int requestCodeActivityAddExpense = 2;
    Calendar calendar = Calendar.getInstance();

    ArrayList items = new ArrayList();
    ArrayList<Income> incomes = new ArrayList<>();
    ArrayList<Expense> expenses = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_expanded);

        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        btnAddExepense = findViewById(R.id.btnAddExpense);
        btnAddIncome = findViewById(R.id.btnAddIncome);
        btnPrevious = findViewById(R.id.btnPrevious);
        nextDay = findViewById(R.id.nextDay);
        lvItems = findViewById(R.id.lvItems);
        tvToday = findViewById(R.id.tvToday);

        tvToday.setText(LocalDate.of(year, month + 1, day).toString());

        final MyApplication app = (MyApplication) this.getApplication();

        try {
            ExpensesDB db = new ExpensesDB(moneyExpanded.this);
            db.open();
            incomes = db.getIncomesByDate(String.valueOf(day), String.valueOf(month), String.valueOf(year));
            expenses = db.getExpensesByDate(String.valueOf(day), String.valueOf(month), String.valueOf(year));
            db.close();

            for (int i = 0; i < incomes.size(); i++) {
                items.add(incomes.get(i));
            }
            for (int i = 0; i < expenses.size(); i++) {
                items.add(expenses.get(i));
            }


            app.setItems(items);

        } catch (SQLException e) {
            Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        final ItemsAdapter adapter = new ItemsAdapter(moneyExpanded.this, items);
        lvItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringTokenizer tokens = new StringTokenizer(tvToday.getText().toString().trim(), "-");
                int yearToModify = Integer.parseInt(tokens.nextToken());
                int monthToModify = Integer.parseInt(tokens.nextToken());
                int dayToModify = Integer.parseInt(tokens.nextToken()) + 1;
                tvToday.setText(LocalDate.of(year, month + 1, dayToModify).toString());
                try {
                    ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                    db.open();
                    incomes = db.getIncomesByDate(String.valueOf(dayToModify), String.valueOf(month), String.valueOf(year));
                    expenses = db.getExpensesByDate(String.valueOf(dayToModify), String.valueOf(month), String.valueOf(year));
                    db.close();

                    adapter.clear();

                    for (int i = 0; i < incomes.size(); i++) {
                        items.add(incomes.get(i));
                    }
                    for (int i = 0; i < expenses.size(); i++) {
                        items.add(expenses.get(i));
                    }

                    adapter.notifyDataSetChanged();

                } catch (SQLException e) {
                    Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringTokenizer tokens = new StringTokenizer(tvToday.getText().toString().trim(), "-");
                int yearToModify = Integer.parseInt(tokens.nextToken());
                int monthToModify = Integer.parseInt(tokens.nextToken());
                int dayToModify = Integer.parseInt(tokens.nextToken()) - 1;
                tvToday.setText(LocalDate.of(year, month + 1, dayToModify).toString());
                try {
                    ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                    db.open();
                    incomes = db.getIncomesByDate(String.valueOf(dayToModify), String.valueOf(month), String.valueOf(year));
                    expenses = db.getExpensesByDate(String.valueOf(dayToModify), String.valueOf(month), String.valueOf(year));
                    db.close();

                    adapter.clear();
                    adapter.notifyDataSetChanged();

                    for (int i = 0; i < incomes.size(); i++) {
                        items.add(incomes.get(i));
                    }
                    for (int i = 0; i < expenses.size(); i++) {
                        items.add(expenses.get(i));
                    }


                } catch (SQLException e) {
                    Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                if (adapter.getItemAtPosition(i) instanceof Income) {
                    Income income = (Income) adapter.getItemAtPosition(i);
                    String type = income.getType();
                    double sum = income.getSum();
                    Intent intent = new Intent(moneyExpanded.this,
                            com.example.moneymanager.ShowIncome.class);
                    intent.putExtra("type", type);
                    intent.putExtra("sum", sum);
                    intent.putExtra("day", income.getDayIncome());
                    intent.putExtra("month", income.getMonthIncome());
                    intent.putExtra("year", income.getYearIncome());
                    intent.putExtra("rowId", i);
                    startActivity(intent);
                } else if (adapter.getItemAtPosition(i) instanceof Expense) {
                    Expense expense = (Expense) adapter.getItemAtPosition(i);
                    String product = expense.getProduct();
                    int cantity = expense.getCantity();
                    double price = expense.getPrice();
                    double amountSpent = expense.getSpent();

                    Intent intent = new Intent(moneyExpanded.this,
                            com.example.moneymanager.ShowExpense.class);
                    intent.putExtra("product", product);
                    intent.putExtra("cantity", cantity);
                    intent.putExtra("price", price);
                    intent.putExtra("amountSpent", amountSpent);
                    intent.putExtra("day", expense.getDayExpense());
                    intent.putExtra("month", expense.getMonthExpense());
                    intent.putExtra("year", expense.getYearExpense());
                    startActivity(intent);
                }
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            MyApplication app = (MyApplication) moneyExpanded.this.getApplication();

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i) instanceof Income) {
                    final Income income = (Income) adapterView.getItemAtPosition(i);
                    AlertDialog.Builder builder = new AlertDialog.Builder(moneyExpanded.this);
                    builder.setMessage("Do you want to remove the selected item?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                            db.open();
//                            app.deleteIncomeFromItems(income);
                            db.deleteEntryIncome(income.getId());
                            db.close();
                            adapter.remove(income);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(moneyExpanded.this, "The item has been removed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();

                } else if (adapterView.getItemAtPosition(i) instanceof Expense) {
                    final Expense expense = (Expense) adapterView.getItemAtPosition(i);
                    AlertDialog.Builder builder = new AlertDialog.Builder(moneyExpanded.this);
                    builder.setMessage("Do you want to remove the selected item?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                ExpensesDB db = new ExpensesDB(moneyExpanded.this);
                                db.open();
//                                app.deleteExpenseFromItems(expense);
                                db.deleteEntryExpense(expense.getId());
                                db.close();
                            } catch (SQLException e) {
                                Toast.makeText(moneyExpanded.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            adapter.remove(expense);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(moneyExpanded.this, "The item has been removed", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }

            void DateDialog() {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        tvToday.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                };


                DatePickerDialog dpDialog = new DatePickerDialog(moneyExpanded.this, listener, year, month, day);
                dpDialog.show();
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

    }

}
