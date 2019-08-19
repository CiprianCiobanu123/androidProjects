package com.example.moneymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class moneyExpanded extends AppCompatActivity {

    Button btnAddExepense, btnAddIncome;
    ListView lvItems;
    public final int requestCodeActivityAddIncome = 1;
    public final int requestCodeActivityAddExpense = 2;
    ArrayList items = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_expanded);

        btnAddExepense = findViewById(R.id.btnAddExpense);
        btnAddIncome = findViewById(R.id.btnAddIncome);
        lvItems = findViewById(R.id.lvItems);

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

        MyApplication app = (MyApplication) this.getApplication();
        ArrayList items = app.getItems();
//        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);

        final ItemsAdapter adapter = new ItemsAdapter(moneyExpanded.this, items);

        lvItems.setAdapter(adapter);
//        lvItems.setAdapter(arrayAdapter);

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
                            app.deleteIncomeFromItems(income);
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
                                app.deleteExpenseFromItems(expense);
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

    }

}
