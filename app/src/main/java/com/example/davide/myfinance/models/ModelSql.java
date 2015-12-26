package com.example.davide.myfinance.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.davide.myfinance.activities.MainActivity;

import java.util.List;

public class ModelSql implements Model.ModelInterface {
    private MyOpenHelper dbHelper;

    public ModelSql(Context applicationContext) {
        dbHelper = new MyOpenHelper(applicationContext);
    }

    @Override
    public void addExpense(Expense expense) {
        ExpenseSql.addExpense(dbHelper, expense);
    }

    @Override
    public void deleteExpense(Expense expense) {
        ExpenseSql.deleteExpense(dbHelper, expense);
    }

    @Override
    public Expense getExpense(String id) {
        return  ExpenseSql.getExpense(dbHelper, id);
    }

    @Override
    public List<Expense> getExpenses() {
        return ExpenseSql.getExpenses(dbHelper);
    }

    @Override
    public List<Expense> getExpensesByCategory(String category) {
        return ExpenseSql.getExpensesByCategory(dbHelper, category);
    }

    class MyOpenHelper extends SQLiteOpenHelper {
        final static String dbName = "database.db";
        final static int version = 1;

        public MyOpenHelper(Context context) {
            super(context, dbName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            ExpenseSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ExpenseSql.drop(db);
            onCreate(db);
        }
    }
}
