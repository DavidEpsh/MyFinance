package com.example.davide.myfinance.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Spinner;

import com.example.davide.myfinance.activities.MainActivity;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private static final String USER_NAME = "USER_NAME";
    private static final String IS_SAVED =  "IS_SAVED";

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

    public static int updateExpense(ModelSql.MyOpenHelper dbHelper, Expense expense) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, expense.getExpenseName());
        values.put(CATEGORY, expense.getCategory());
        values.put(REPEATING, expense.isRepeatingExpense());
        values.put(DATE, expense.getDateSql());
        values.put(IMAGE_PATH, expense.getExpenseImage());
        values.put(EXPENSE_AMOUNT, expense.getExpenseAmount());

        db.update(TABLE, values, TIMESTAMP + " = '" + expense.getTimeStamp() + "'", null);
        return 0;
    }

    public static int batchUpdateExpense(ModelSql.MyOpenHelper dbHelper, List<Expense> expenses) {
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
        return 0;
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

    public static void syncSqlWithParse(final ModelSql.MyOpenHelper dbHelper, Model.SyncSqlWithParseListener listener){
        Model.instance().modelParse.getAllExpensesAsynch(new Model.GetExpensesListener() {
            @Override
            public void onResult(List<Expense> expenses) {
                batchUpdateExpense(dbHelper, expenses);
            }
        });

        listener.onResult();
    }

    public static void create(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + TIMESTAMP + " LONG PRIMARY KEY," +
                NAME + " TEXT," + CATEGORY + " TEXT, " +
                IMAGE_PATH + " TEXT," + DATE + " DATETIME," + REPEATING + " INTEGER, " + USER_NAME + " TEXT, " +
                IS_SAVED + " INTEGER, " + EXPENSE_AMOUNT + " DOUBLE " + ")");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE EXPENSES");
    }
}
