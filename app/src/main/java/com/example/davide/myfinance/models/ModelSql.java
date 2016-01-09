package com.example.davide.myfinance.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.davide.myfinance.activities.MainActivity;

import java.util.HashMap;
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
    public void deleteExpense(Long expense) {
        ExpenseSql.deleteExpense(dbHelper, expense);
    }

    @Override
    public Expense getExpense(Long id) {
        return  ExpenseSql.getExpense(dbHelper, id);
    }

    @Override
    public void updateOrAddExpense(Expense expense) {
        ExpenseSql.updateOrAddExpense(dbHelper, expense);
    }

    @Override
    public void batchUpdateExpenses(List<Expense> expenses, Model.BatchUpdateListener listener) {
        ExpenseSql.batchUpdateExpense(dbHelper, expenses, listener);
    }

    @Override
    public List<Expense> getAllExpensesAsynch() {
        return null;
    }

    @Override
    public List<Expense> getExpenses() {
        return ExpenseSql.getExpenses(dbHelper);
    }

    @Override
    public List<String> getCategories() {
        return ExpenseSql.getCategories(dbHelper);
    }

    @Override
    public List<Expense> getExpensesByCategory(String category,String fromDate, String toDate) {
        return ExpenseSql.getExpensesByCategory(dbHelper, category, fromDate, toDate);
    }

    @Override
    public void addUserSheets(String sheetId, String userName) {
        ModelUsersAndAccountsSql.addUserSheets(dbHelper, sheetId, userName);
    }

    @Override
    public void addSheets(String id, String userName) {
        ModelUsersAndAccountsSql.addSheets(dbHelper, id, userName);
    }

    @Override
    public HashMap<String, Double> getUsersAndSums(String sheetId) {
        return ModelUsersAndAccountsSql.getUsersAndSum(dbHelper, sheetId);
    }

    @Override
    public Double getSumByCategory(String category,String fromDate, String toDate) {
        return ExpenseSql.getSumByCategory(dbHelper, category, fromDate, toDate);
    }

    @Override
    public void changeLastUpdateTime(Model.ChangeTimeListener listener) {
        changeLastUpdateTime(listener);
    }

    @Override
    public String getExistingUsersSheet(String id){
        return ModelUsersAndAccountsSql.getExistingUsersSheet(id, dbHelper);
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
            ModelUsersAndAccountsSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ExpenseSql.drop(db);
            ModelUsersAndAccountsSql.drop(db);
            onCreate(db);
        }
    }
}
