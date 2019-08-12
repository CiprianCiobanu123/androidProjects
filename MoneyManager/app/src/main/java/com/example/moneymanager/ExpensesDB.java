package com.example.moneymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExpensesDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PRODUCT = "_product";
    public static final String KEY_PRICE = "_price";
    public static final String KEY_CANTITY = "_cantity";
    public static final String KEY_DATE_FOR_EXPENSES = "date_for_expenses";
    public static final String KEY_TYPE = "_type";
    public static final String KEY_SUM = "_sum";
    public static final String KEY_DATE_FOR_INCOMES = "date_for_incomes";

    private final String DATABASE_NAME = "SpendingDB";
    private final String DATABASE_TABLE_EXPENSE = "ExpenseTable";
    private final String DATABASE_TABLE_INCOME = "IncomeTable";
    private final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    public ExpensesDB(Context context) {
        ourContext = context;
    }


    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_TABLE_EXPENSE, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {


            String sqlCodeForExpense = "CREATE TABLE " + DATABASE_TABLE_EXPENSE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_PRODUCT + " TEXT NOT NULL, " +
                    KEY_PRICE + " REAL, " +
                    KEY_CANTITY + " REAL, " +
                    KEY_DATE_FOR_EXPENSES + " DATE)";

            String sqlCodeForIncome = "CREATE TABLE " + DATABASE_TABLE_INCOME + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_TYPE + " TEXT NOT NULL, " +
                    KEY_SUM + " REAL, " +
                    KEY_DATE_FOR_INCOMES + " DATE);";

            sqLiteDatabase.execSQL(sqlCodeForExpense);
            sqLiteDatabase.execSQL(sqlCodeForIncome);



        }

        public ArrayList getAllValues(){
            SQLiteDatabase db = this.getReadableDatabase();
            ArrayList<String> array_list = new ArrayList<String>();
            Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_INCOME,null);
            res.moveToFirst();
            while(!res.isAfterLast()){
                array_list.add(res.getString(res.getColumnIndex("_sum")));
                res.moveToNext();

            }
            return array_list;
        }


        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_INCOME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EXPENSE);
            onCreate(sqLiteDatabase);

        }



    }

    public ExpensesDB open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntryExpense(String product, double price, int cantity, LocalDate dateExpenses) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_PRODUCT, product);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_CANTITY, cantity);
        cv.put(KEY_DATE_FOR_EXPENSES, dateExpenses.toString());

        return ourDatabase.insert(DATABASE_TABLE_EXPENSE, null, cv);

    }

    public long createEntryIncome(String type, double sum, LocalDate dateExpenses) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TYPE, type);
        cv.put(KEY_SUM, sum);
        cv.put(KEY_DATE_FOR_INCOMES, dateExpenses.toString());

        return ourDatabase.insert(DATABASE_TABLE_INCOME, null, cv);

    }

    public String getDataFromExpenseTable() {
        String[] columns = new String[]{KEY_ROWID, KEY_PRODUCT, KEY_PRICE, KEY_CANTITY, KEY_DATE_FOR_EXPENSES};

        Cursor cursor = ourDatabase.query(DATABASE_TABLE_EXPENSE, columns, null, null, null, null, null);

        String result = "";

        int iRowID = cursor.getColumnIndex(KEY_ROWID);
        int iProduct = cursor.getColumnIndex(KEY_PRODUCT);
        int iPrice = cursor.getColumnIndex(KEY_PRICE);
        int iCantity = cursor.getColumnIndex(KEY_CANTITY);
        int iDateForExpenses = cursor.getColumnIndex(KEY_DATE_FOR_EXPENSES);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result = result + cursor.getString(iRowID) + ": " +
                    cursor.getString(iProduct) + ": " +
                    cursor.getString(iPrice) + ": " +
                    cursor.getString(iCantity) + ": " +
                    cursor.getString(iDateForExpenses) + "\n";

        }

        cursor.close();

        return result;
    }

    public String getDataFromIncomeTable() {
        String[] columns = new String[]{KEY_ROWID, KEY_TYPE, KEY_SUM, KEY_DATE_FOR_INCOMES};

        Cursor cursor = ourDatabase.query(DATABASE_TABLE_INCOME, columns, null, null, null, null, null);

        String result = "";

        int iRowID = cursor.getColumnIndex(KEY_ROWID);
        int iType = cursor.getColumnIndex(KEY_TYPE);
        int iSum = cursor.getColumnIndex(KEY_SUM);
        int iDateForIncomes = cursor.getColumnIndex(KEY_DATE_FOR_INCOMES);


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result = result + cursor.getString(iRowID) + ": " +
                    cursor.getString(iType) + ": " +
                    cursor.getString(iSum) + ": " +
                    cursor.getString(iDateForIncomes) + "\n";

        }

        cursor.close();

        return result;

    }

    public long deleteEntryExpense(String rowId) {
        return ourDatabase.delete(DATABASE_TABLE_EXPENSE, KEY_ROWID + "=?", new String[]{rowId});
    }

    public long deleteEntryIncome(String rowId) {
        return ourDatabase.delete(DATABASE_TABLE_INCOME, KEY_ROWID + "=?", new String[]{rowId});
    }

    public long updateEntryExpense(String rowId, String product, double price, int cantity, Date dateExpenses) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_PRODUCT, product);
        contentValues.put(KEY_PRICE, price);
        contentValues.put(KEY_CANTITY, cantity);
        contentValues.put(KEY_DATE_FOR_EXPENSES, dateExpenses.toString());


        return ourDatabase.update(DATABASE_TABLE_EXPENSE, contentValues, KEY_ROWID + "=?", new String[]{rowId});

    }

    public long updateEntryIncome(String rowId, String type, double sum, Date dateIncomes) {
        ContentValues contentValues = new ContentValues();


        contentValues.put(KEY_TYPE, type);
        contentValues.put(KEY_SUM, sum);
        contentValues.put(KEY_DATE_FOR_INCOMES, dateIncomes.toString());

        return ourDatabase.update(DATABASE_TABLE_INCOME, contentValues, KEY_ROWID + "=?", new String[]{rowId});

    }


}
