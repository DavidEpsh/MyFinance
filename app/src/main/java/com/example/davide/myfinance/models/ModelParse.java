package com.example.davide.myfinance.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ModelParse {


    public void init(Context context) {
        Parse.initialize(context, "asIB0JfInjKjiQV7kElOvPIBAAFYIu60f9MZrlx6", "9xZgwSeofzVrze8NJGKUUW1EpiNYu1qZRl3We0Z4");
    }

    public List<Expense> getAllExpenses() {
        List<Expense> Expenses = new LinkedList<Expense>();
        ParseQuery query = new ParseQuery("expense");
        HashMap<String, Object> params = new HashMap<String, Object>();
        ParseCloud.callFunctionInBackground("getServerTime", params);
        try {
            List<ParseObject> data = query.find();
            for (ParseObject po : data) {
                Long id = po.getLong("expenseId");
                String name = po.getString("name");
                boolean isRepeating = po.getBoolean("repeating");
                String date = po.getString("date");
                String category = po.getString("category");
                String imageName = po.getString("imageName");
                Double amount = po.getDouble("amount");
                Expense expense = new Expense(name, isRepeating, date, imageName, amount, category,id);
                Expenses.add(expense);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return Expenses;
        }
        return Expenses;
    }

    public void getExpenseById(String id, final Model.GetExpense listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("expense");
        query.whereEqualTo("expenseId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Expense expense = null;
                if (e == null && parseObjects.size() > 0) {
                    ParseObject po = parseObjects.get(0);
                    Long id = po.getLong("timeStamp");
                    String name = po.getString("name");
                    boolean isRepeating = po.getBoolean("repeating");
                    String date = po.getString("date");
                    String category = po.getString("category");
                    String imageName = po.getString("imageName");
                    Double amount = po.getDouble("amount");
                    expense = new Expense(name, isRepeating, date, imageName, amount, category,id);
                }
                listener.onResult(expense);
            }
        });
    }


    public void add(Expense expense) {
        ParseObject newObject = new ParseObject("Expense");
        newObject.put("expenseId", expense.getTimeStamp());
        newObject.put("name", expense.getExpenseName());
        newObject.put("repeating", expense.isRepeatingExpenseBool());
        newObject.put("imageName", expense.getExpenseImage());
        newObject.put("date", expense.getExpenseDate());
        newObject.put("category", expense.getCategory());
        newObject.put("amount", expense.getExpenseAmount());
        try {
            newObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getAllExpensesAsynch(final Model.GetExpensesListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Expense");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                List<Expense> Expenses = new LinkedList<Expense>();
                if (e == null) {
                    for (ParseObject po : parseObjects) {
                        Long id = po.getLong("expenseId");
                        String name = po.getString("name");
                        boolean isRepeating = po.getBoolean("repeating");
                        String date = po.getString("date");
                        String category = po.getString("category");
                        String imageName = po.getString("imageName");
                        Double amount = po.getDouble("amount");
                        Expense expense = new Expense(name, isRepeating, date, imageName, amount, category,id);
                        Expenses.add(expense);
                    }
                }
                listener.onResult(Expenses);
            }
        });
    }

    public void saveImage(Bitmap imageBitmap, String imageName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        ParseFile file = new ParseFile(imageName, byteArray);
        try {
            file.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseObject images = new ParseObject("images");
        images.put("name", imageName);
        images.put("image", file);
        try {
            images.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadImage(String imageName) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("images");
        query.whereEqualTo("name", imageName);
        try {
            List<ParseObject> list = query.find();
            if (list.size() > 0) {
                ParseObject po = list.get(0);
                ParseFile pf = po.getParseFile("image");
                byte[] data = pf.getData();
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                return bmp;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
