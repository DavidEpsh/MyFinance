package com.example.davide.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelUsersAndAccountsSql {
    private static final String TABLE_USER_SHEETS = "USER_SHEETS";
    private static final String TABLE_SHEETS = "SHEETS";
    private static final String SHEET_NAME = "SHEET_NAME";
    private static final String TIMESTAMP = "TIMESTAMP";
    private static final String EXPENSE_AMOUNT = "EXPENSE_AMOUNT";
    private static final String USER_NAME = "USER_NAME";
    public static final String SHEET_ID = "SHEET_ID";
    private static final String EXPENSES = "EXPENSES";
    public static final String USER_SHEET_ID = "USER_SHEET_ID";

    public static void create(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE " + TABLE_USER_SHEETS + " (" + TIMESTAMP + " LONG PRIMARY KEY," +
//                USER_NAME + " TEXT," + SHEET_ID + " LONG FOREIGN KEY " + ")");

        db.execSQL("CREATE TABLE " + TABLE_USER_SHEETS + " (" + TIMESTAMP + " LONG PRIMARY KEY," +
                USER_NAME + " TEXT," + " FOREIGN KEY (" + SHEET_ID + ")" +
                " REFERENCES " + TABLE_SHEETS + "(" +TIMESTAMP +")" + ")");

        db.execSQL("CREATE TABLE " + TABLE_SHEETS + " (" + TIMESTAMP + " LONG PRIMARY KEY," +
                SHEET_NAME + " TEXT" + ")");
    }

    public static void getUsersAndSum(SQLiteDatabase db, long sheetId){

        String query = "SELECT " +  (TABLE_USER_SHEETS + "." + USER_NAME) + "," +
                " SUM(" + (EXPENSES + "." +EXPENSE_AMOUNT) +") " +
                " FROM " + EXPENSES +
                " LEFT INNER JOIN " + TABLE_USER_SHEETS +
                " ON " + (EXPENSES + "." + USER_SHEET_ID) + "=" + (TABLE_USER_SHEETS + "." + TIMESTAMP) +
                " WHERE " + (TABLE_USER_SHEETS + "." + SHEET_ID) + " = " + sheetId +
                " GROUP BY " + (TABLE_USER_SHEETS + "." + USER_NAME);

    }

    public static void addUserSheets(ModelSql.MyOpenHelper dbHelper, long id, long sheetId, String userName){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, id);
        values.put(USER_NAME, userName);
        values.put(SHEET_ID, sheetId);
        db.insert(TABLE_USER_SHEETS, TIMESTAMP, values);
    }

    public static void addSheets(ModelSql.MyOpenHelper dbHelper, long id, String userName){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, id);
        values.put(USER_NAME, userName);
        db.insert(TABLE_SHEETS, TIMESTAMP, values);
    }







/*
    public static void addExpense(ModelSql.MyOpenHelper dbHelper, Expense expense) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMESTAMP, expense.getTimeStamp());
        values.put(NAME, expense.getExpenseName());
        values.put(CATEGORY, expense.getCategory());
        values.put(REPEATING, expense.isRepeatingExpense());
        values.put(DATE, expense.getDateSql());
        values.put(IMAGE_PATH, expense.getExpenseImage());
        values.put(EXPENSE_AMOUNT, expense.getExpenseAmount());
        values.put(USER_NAME, ParseUser.getCurrentUser().getUsername());
        values.put(IS_SAVED, 1);

        db.insert(TABLE, TIMESTAMP, values);
    }

    public static void deleteExpense(ModelSql.MyOpenHelper dbHelper, Long timeStamp) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(IS_SAVED, 0);
        db.update(TABLE, values, TIMESTAMP + " = '" + timeStamp + "'", null);
    }

    public static Expense getExpense(ModelSql.MyOpenHelper dbHelper, Long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE + " WHERE " + TIMESTAMP + " = " + id;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            //Long timeStamp = cursor.getLong(cursor.getColumnIndex(TIMESTAMP));
            String expenseName = cursor.getString(cursor.getColumnIndex(NAME));
            String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
            String imagePath = cursor.getString(cursor.getColumnIndex(IMAGE_PATH));
            String date = cursor.getString(cursor.getColumnIndex(DATE));
            int repeating = cursor.getInt(cursor.getColumnIndex(REPEATING));
            double amount = cursor.getDouble(cursor.getColumnIndex(EXPENSE_AMOUNT));

            boolean isRepeating;
            if (repeating == 1) {
                isRepeating = true;
            } else {
                isRepeating = false;
            }
            // TODO: 23/12/2015 - ADD BOOLEAN
            Expense expense = new Expense(expenseName, isRepeating, date, imagePath, amount, category, id);

            cursor.close();
            return expense;
        }else{
            return null;
        }
    }

    public static void updateExpense(ModelSql.MyOpenHelper dbHelper, Expense expense) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, expense.getExpenseName());
        values.put(CATEGORY, expense.getCategory());
        values.put(REPEATING, expense.isRepeatingExpense());
        values.put(DATE, expense.getDateSql());
        values.put(IMAGE_PATH, expense.getExpenseImage());
        values.put(EXPENSE_AMOUNT, expense.getExpenseAmount());

        db.update(TABLE, values, TIMESTAMP + " = '" + expense.getTimeStamp() + "'", null);
    }

    public static void batchUpdateExpense(ModelSql.MyOpenHelper dbHelper, List<Expense> expenses, Model.BatchUpdateListener listener) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        for(Expense expense : expenses) {
            if (getExpense(dbHelper, expense.getTimeStamp()) == null) {
                addExpense(dbHelper, expense);
            } else {
                ContentValues values = new ContentValues();
                values.put(NAME, expense.getExpenseName());
                values.put(CATEGORY, expense.getCategory());
                values.put(REPEATING, expense.isRepeatingExpense());
                values.put(DATE, expense.getDateSql());
                values.put(IMAGE_PATH, expense.getExpenseImage());
                values.put(EXPENSE_AMOUNT, expense.getExpenseAmount());

                db.update(TABLE, values, TIMESTAMP + " = '" + expense.getTimeStamp() + "'", null);
            }
        }
        listener.onResult();
    }

    public static List<Expense> getExpenses(ModelSql.MyOpenHelper dbHelper) {
        List<Expense> data = new LinkedList<Expense>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //    public Cursor query(String table, String[] columns, String selection,
//        String[] selectionArgs, String groupBy, String having,
//                String orderBy)
        String query = "SELECT * FROM " + TABLE +
                " WHERE " + USER_NAME + " = " + "'" + ParseUser.getCurrentUser().getUsername() + "'" +
                " AND " + IS_SAVED + " = " + " 1 ";
        Cursor cursor = db.rawQuery(query, null);

        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                int id_index = cursor.getColumnIndex(TIMESTAMP);
                int name_index = cursor.getColumnIndex(NAME);
                int category_index = cursor.getColumnIndex(CATEGORY);
                int repeating_index = cursor.getColumnIndex(REPEATING);
                int image_path_index = cursor.getColumnIndex(IMAGE_PATH);
                int date_index = cursor.getColumnIndex(DATE);
                int amount_index = cursor.getColumnIndex(EXPENSE_AMOUNT);

                do {
                    Long timeStamp = cursor.getLong(id_index);
                    String expenseName = cursor.getString(name_index);
                    String category = cursor.getString(category_index);
                    String imagePath = cursor.getString(image_path_index);
                    String date = cursor.getString(date_index);
                    int repeating = cursor.getInt(repeating_index);
                    double amount = cursor.getDouble(amount_index);

                    boolean isRepeating;
                    if (repeating == 1) {
                        isRepeating = true;
                    } else {
                        isRepeating = false;
                    }
                    // TODO: 23/12/2015 - ADD BOOLEAN
                    Expense expense = new Expense(expenseName, isRepeating, date, imagePath, amount, category, timeStamp);
                    data.add(expense);
                } while (cursor.moveToNext());
            }
        }

        return data;
    }

    public static List<Expense> getExpensesByCategory(ModelSql.MyOpenHelper dbHelper, String selectedCategory,String fromDate, String toDate) {
        List<Expense> data = new LinkedList<Expense>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //    public Cursor query(String table, String[] columns, String selection,
//        String[] selectionArgs, String groupBy, String having,
//                String orderBy) {
        Cursor cursor; //= db.rawQuery(null,null);

        if(selectedCategory == null && fromDate == null && toDate == null){
            String query = "SELECT * FROM " + TABLE +
                    " WHERE " + USER_NAME + " = " + "'" + ParseUser.getCurrentUser().getUsername() + "'" +
                    " AND " + IS_SAVED + " = " + " 1 " +
                    " ORDER BY " + DATE + " DESC ";

            cursor = db.rawQuery(query, null);

        }else if(selectedCategory == null && toDate == null){
            String query = "SELECT * FROM " + TABLE +
                    " WHERE " + DATE + " > " + "'" + fromDate + "'" +
                    " AND " + USER_NAME + " = " + "'" + ParseUser.getCurrentUser().getUsername() + "'" +
                    " AND " + IS_SAVED + " = " + " 1 " +
                    " ORDER BY " + DATE +" DESC ";

            cursor = db.rawQuery(query, null);

        }else if(fromDate == null && toDate == null) {
            String query = "SELECT * FROM " + TABLE +
                    " WHERE " + CATEGORY + " = " + "'" + selectedCategory + "'" +
                    " AND " + USER_NAME + " = " + "'" + ParseUser.getCurrentUser().getUsername() + "'" +
                    " AND " + IS_SAVED + " = " + " 1 ";
            cursor = db.rawQuery(query, null);

        }else {
            String query = "SELECT * FROM " + TABLE +
                    " WHERE " + CATEGORY + " = " + "'" + selectedCategory + "'" +
                    " AND " + DATE + " > " + "'" + fromDate + "'" +
                    " AND " + USER_NAME + " = " + "'" + ParseUser.getCurrentUser().getUsername() + "'" +
                    " AND " + IS_SAVED + " = " + " 1 " +
                    " ORDER BY " + DATE +" DESC ";

            cursor = db.rawQuery(query, null);
        }

        if (cursor.moveToFirst()) {
            int id_index = cursor.getColumnIndex(TIMESTAMP);
            int name_index = cursor.getColumnIndex(NAME);
            int category_index = cursor.getColumnIndex(CATEGORY);
            int repeating_index = cursor.getColumnIndex(REPEATING);
            int image_path_index = cursor.getColumnIndex(IMAGE_PATH);
            int date_index = cursor.getColumnIndex(DATE);
            int amount_index = cursor.getColumnIndex(EXPENSE_AMOUNT);

            do {
                Long timeStamp = cursor.getLong(id_index);
                String expenseName = cursor.getString(name_index);
                String category = cursor.getString(category_index);
                String imagePath = cursor.getString(image_path_index);
                String date = cursor.getString(date_index);
                int repeating = cursor.getInt(repeating_index);
                double amount = cursor.getDouble(amount_index);

                boolean isRepeating;
                if (repeating == 1){
                    isRepeating = true;
                }else{
                    isRepeating = false;
                }

                Expense expense = new Expense(expenseName, isRepeating, date, imagePath, amount, category, timeStamp);
                data.add(expense);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public static List<String> getCategories(ModelSql.MyOpenHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> allCategories = new ArrayList<>();

        String query = "SELECT DISTINCT " + CATEGORY + " FROM " + TABLE +
                " WHERE " + USER_NAME + " = " + "'" + ParseUser.getCurrentUser().getUsername() + "'" +
                " AND " + IS_SAVED + " = " + " 1 ";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int category_index = cursor.getColumnIndex(CATEGORY);

            do {
                String category = cursor.getString(category_index);
                allCategories.add(category);
            } while (cursor.moveToNext());
        }
        return allCategories;
    }

    public static Double getSumByCategory(ModelSql.MyOpenHelper dbHelper, String selectedCategory, String fromDate, String toDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> allCategories = new ArrayList<>();

        Cursor cursor;

        String query = "SELECT SUM(" + EXPENSE_AMOUNT + ")" + " FROM " + TABLE +
                " WHERE " + CATEGORY + " = " + "'" + selectedCategory + "'" +
                " AND " + DATE + " > " + "'" + fromDate + "'" +
                " AND " + USER_NAME + " = " + "'" + ParseUser.getCurrentUser().getUsername() + "'" +
                " AND " + IS_SAVED + " = " + " 1 ";

        cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return null;
    }

    public static void syncSqlWithParse(final ModelSql.MyOpenHelper dbHelper, final Model.SyncSqlWithParseListener listener){
        Model.instance().modelParse.getAllExpensesAsynch(new Model.GetExpensesListener() {
            @Override
            public void onResult(List<Expense> expenses) {
                batchUpdateExpense(dbHelper, expenses, new Model.BatchUpdateListener() {
                    @Override
                    public void onResult() {
                        listener.onResult();
                    }
                });
            }
        });
    }

    */

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE_USER_SHEETS + ";");
        db.execSQL("drop table " + TABLE_SHEETS + ";");
    }
}
