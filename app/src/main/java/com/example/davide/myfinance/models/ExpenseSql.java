package com.example.davide.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Spinner;

import java.util.LinkedList;
import java.util.List;

public class ExpenseSql {
    private static final String TABLE = "EXPENSES";
    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String IMAGE_PATH = "IMAGE_PATH";
    private static final String REPEATING = "REPEATING";
    private static final String DATE = "DATE";
    private static final String TIMESTAMP = "TIMESTAMP";
    private static final String EXPENSE_AMOUNT = "EXPENSE_AMOUNT";

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

        db.insert(TABLE, TIMESTAMP, values);
    }

    public static void deleteExpense(ModelSql.MyOpenHelper dbHelper, Expense st) {

    }

    public static Expense getExpense(ModelSql.MyOpenHelper dbHelper, String selectedDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE + " WHERE " + DATE + " = " + selectedDate;
        Cursor cursor = db.rawQuery(query, null);

        Long timeStamp = cursor.getLong(cursor.getColumnIndex(TIMESTAMP));
        String expenseName = cursor.getString(cursor.getColumnIndex(NAME));
        String category = cursor.getString(cursor.getColumnIndex(CATEGORY));
        String imagePath = cursor.getString(cursor.getColumnIndex(IMAGE_PATH));
        String date = cursor.getString(cursor.getColumnIndex(DATE));
        int repeating = cursor.getInt(cursor.getColumnIndex(REPEATING));
        double amount = cursor.getDouble(cursor.getColumnIndex(EXPENSE_AMOUNT));

        boolean isRepeating;
        if (repeating == 1){
            isRepeating = true;
        }else{
            isRepeating = false;
        }
        // TODO: 23/12/2015 - ADD BOOLEAN
        Expense expense = new Expense(expenseName, isRepeating, date, imagePath, amount, category, timeStamp);

        return expense;
    }

    public static List<Expense> getExpenses(ModelSql.MyOpenHelper dbHelper) {
        List<Expense> data = new LinkedList<Expense>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //    public Cursor query(String table, String[] columns, String selection,
//        String[] selectionArgs, String groupBy, String having,
//                String orderBy) {
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);

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
                // TODO: 23/12/2015 - ADD BOOLEAN
                Expense expense = new Expense(expenseName, isRepeating, date, imagePath, amount, category, timeStamp);
                data.add(expense);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public static List<Expense> getExpensesByCategory(ModelSql.MyOpenHelper dbHelper, String selectedCategory) {
        List<Expense> data = new LinkedList<Expense>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //    public Cursor query(String table, String[] columns, String selection,
//        String[] selectionArgs, String groupBy, String having,
//                String orderBy) {
        String query = "SELECT * FROM " + TABLE + " WHERE " + CATEGORY + " = " +"'" + selectedCategory + "'";
        Cursor cursor = db.rawQuery(query, null);
//        String[] col = new String[1];
//        col[0] = CATEGORY;

        //Cursor cursor = db.query(TABLE, null, "'Travel'", null, null, null, null);


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
                // TODO: 23/12/2015 - ADD BOOLEAN
                Expense expense = new Expense(expenseName, isRepeating, date, imagePath, amount, category, timeStamp);
                data.add(expense);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + TIMESTAMP + " LONG PRIMARY KEY," + NAME + " TEXT," + CATEGORY + " TEXT, " +
                IMAGE_PATH + " TEXT," + DATE + " DATETIME," + REPEATING + " INTEGER, " + EXPENSE_AMOUNT + " DOUBLE" + ")");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE EXPENSES");
    }
}
