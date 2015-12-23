package com.example.davide.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Spinner;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 02/12/2015.
 */
public class ExpenseSql {
    private static final String TABLE = "EXPENSES";
    private static final String NAME = "NAME";
    private static final String CATEGORY = "CATEGORY";
    private static final String IMAGE_PATH = "IMAGE_PATH";
    //private static final String REPEATING = "REPEATING";
    private static final String DATE_DAY = "DATE_DAY";
    private static final String DATE_MONTH = "DATE_MONTH";
    private static final String DATE_YEAR = "DATE_YEAR";
    private static final String TIMESTAMP = "TIMESTAMP";
    private static final String EXPENSE_AMOUNT = "EXPENSE_AMOUNT";

    public static void addExpense(ModelSql.MyOpenHelper dbHelper, Expense expense) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TIMESTAMP, expense.getTimeStamp());
        values.put(NAME, expense.getExpenseName());
        values.put(CATEGORY, expense.getCategory());
        //values.put(REPEATING, expense.isRepeatingExpense());
        values.put(DATE_DAY, expense.getExpenseDate()[0]);
        values.put(DATE_MONTH, expense.getExpenseDate()[1]);
        values.put(DATE_YEAR, expense.getExpenseDate()[2]);
        values.put(IMAGE_PATH, expense.getExpenseImage());
        values.put(EXPENSE_AMOUNT, expense.getExpenseAmount());

        db.insert(TABLE, TIMESTAMP, values);
    }

    public static void deleteExpense(ModelSql.MyOpenHelper dbHelper, Expense st) {

    }

    public static Expense getStudent(ModelSql.MyOpenHelper dbHelper, String id) {
        return null;
    }

    public static List<Expense> getStudents(ModelSql.MyOpenHelper dbHelper) {
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
            //int repeating_index = cursor.getColumnIndex(REPEATING);
            int image_path_index = cursor.getColumnIndex(IMAGE_PATH);
            int day_index = cursor.getColumnIndex(DATE_DAY);
            int month_index = cursor.getColumnIndex(DATE_MONTH);
            int year_index = cursor.getColumnIndex(DATE_YEAR);
            int amount_index = cursor.getColumnIndex(EXPENSE_AMOUNT);

            do {
                String timeStamp = cursor.getString(id_index);
                String expenseName = cursor.getString(name_index);
                String category = cursor.getString(category_index);
                String imagePath = cursor.getString(image_path_index);
                int day = cursor.getInt(day_index);
                int month = cursor.getInt(month_index);
                int year = cursor.getInt(year_index);
                double amount = cursor.getDouble(amount_index);

                // TODO: 23/12/2015 - ADD BOOLEAN

                int[] tempDate = new int[]{day,month,year};
                Expense expense = new Expense(expenseName, true, tempDate, imagePath, amount, category, timeStamp);
                data.add(expense);
            } while (cursor.moveToNext());
        }

        return data;
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + TIMESTAMP + " TEXT PRIMARY KEY," + NAME + " TEXT," + CATEGORY + " TEXT, " +
                IMAGE_PATH + " TEXT," + DATE_DAY + " TEXT," +DATE_MONTH + " TEXT," + DATE_YEAR + " TEXT," + EXPENSE_AMOUNT + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE EXPENSES");
    }
}
