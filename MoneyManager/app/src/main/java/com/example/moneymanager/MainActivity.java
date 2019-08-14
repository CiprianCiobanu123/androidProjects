package com.example.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    public static final String INCOME_ARRAYLIST = "incomes";

    TextView tvAccount ;
    ArrayList<Income> incomes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAccount = findViewById(R.id.tvAccount);
//        File file = getApplicationContext().getFileStreamPath("Incomes.txt");
//
//        if(file.exists())
//        {
//            String lineFromFile ;
//            try{
//                BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("Incomes.txt")));
//
//                while((lineFromFile = reader.readLine())!=null){
//                    StringTokenizer tokens = new StringTokenizer(lineFromFile,",");
//                    double sum = Double.parseDouble(tokens.nextToken());
//                    String type =  tokens.nextToken();
//                    StringTokenizer dateTokens = new StringTokenizer(tokens.nextToken());
//
//                    int day = Integer.parseInt(dateTokens.nextToken());
//                    int month = Integer.parseInt(dateTokens.nextToken());
//                    int year = Integer.parseInt(dateTokens.nextToken());
//                    LocalDate date = LocalDate.of(year, month, day);
//
//                    Income income = new Income(sum, type, date);
//                    incomes.add(income);
//
//                }
//
//                reader.close();
//                String text = "";
//                for(int i =0 ; i<incomes.size(); i++){
//                    text = text + incomes.get(i).getSum() + " " + incomes.get(i).getType() + " " + incomes.get(i).getDate()+"\n";
//                }
//                tvAccount.setText(text);
//
//
//            }
//                catch(IOException e){
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//
//        }
        tvAccount = findViewById(R.id.tvAccount);




        try
        {
            ExpensesDB db = new ExpensesDB(this);
            db.open();
            incomes =  db.getAllIncomeValues();
            db.close();

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
