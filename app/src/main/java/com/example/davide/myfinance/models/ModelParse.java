package com.example.davide.myfinance.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.models.Model.GetExpensesListener;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ModelParse {

    private static final String USER_NAME = "username";
    public static final String IS_SAVED = "isSaved";
    public static final String UPDATED_AT = "updatedAt";

    Context context;
    HashMap<String, Object> params;

    public void init(Context context) {
        Parse.initialize(context, "asIB0JfInjKjiQV7kElOvPIBAAFYIu60f9MZrlx6", "9xZgwSeofzVrze8NJGKUUW1EpiNYu1qZRl3We0Z4");
        this.context = context;
    }

    public List<Expense> getAllExpenses() {
        List<Expense> Expenses = new LinkedList<Expense>();
        ParseQuery query = new ParseQuery("expense");

        query.whereContains(USER_NAME, ParseUser.getCurrentUser().getUsername());

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

        changeLastUpdateTime();
        return Expenses;
    }

    public List<Expense> getAllExpensesSinceLastUpdate() {
        ParseQuery query;
        List<Expense> expenses;

        if (getLastUpdateTime(true) == null) {
            expenses = new LinkedList<Expense>();
            query = new ParseQuery("expense");
        }else{
            expenses = new LinkedList<Expense>();
            query = new ParseQuery("expense");
            query.whereGreaterThan(UPDATED_AT, getLastUpdateTime(false));
        }
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
                expenses.add(expense);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return expenses;
        }

        changeLastUpdateTime();
        return expenses;
    }

    public void getExpenseById(Long id, final Model.GetExpense listener) {
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
                    expense = new Expense(name, isRepeating, date, imageName, amount, category, id);
                }
                listener.onResult(expense);
            }
        });
    }

    public void updateOrDelete(Expense expense, boolean doDeleteExpense){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");

        query.
// Retrieve the object by id
        query.getInBackground("xWMyZ4YEGZ", new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    gameScore.put("score", 1338);
                    gameScore.put("cheatMode", true);
                    gameScore.saveInBackground();
                }
            }
        });



        if(doDeleteExpense) {
            newObject.put(IS_SAVED, 0);
        }else{
            newObject.put(IS_SAVED, 1);
        }
    }

    public void addOrUpdateAsync(final Expense expense) {
        ParseObject newObject = new ParseObject("Expense");
        newObject.put(USER_NAME, ParseUser.getCurrentUser().getUsername());
        newObject.put("expenseId", expense.getTimeStamp());
        newObject.put("name", expense.getExpenseName());
        newObject.put("repeating", expense.isRepeatingExpenseBool());
        newObject.setACL(new ParseACL(ParseUser.getCurrentUser()));

        if(expense.getExpenseImage() != null) {
            newObject.put("imageName", expense.getExpenseImage());
        }
        newObject.put("date", expense.getDateSql());
        newObject.put("category", expense.getCategory());
        newObject.put("amount", expense.getExpenseAmount());



        newObject.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("Parse", "Unable to save" + expense.getExpenseName());
                }
            }
        });

    }

    public void getAllExpensesAsynch(final GetExpensesListener listener) {
        ParseQuery<ParseObject> query;

        if(getLastUpdateTime(true) == null) {
            query = new ParseQuery<ParseObject>("Expense");
            query.whereContains(USER_NAME, ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo(IS_SAVED, 1);

        }else{
            query = new ParseQuery<ParseObject>("Expense");
            query.whereGreaterThan(UPDATED_AT, getLastUpdateTime(false));
            query.whereContains(USER_NAME, ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo(IS_SAVED, 1);
        }
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
                        Expense expense = new Expense(name, isRepeating, date, imageName, amount, category, id);
                        Expenses.add(expense);
                    }
                }
                listener.onResult(Expenses);
            }
        });

        changeLastUpdateTime();
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

    public void changeLastUpdateTime(){

        final String getTime = "getServerTime";

        params = new HashMap<String, Object>();
        ParseCloud.callFunctionInBackground(getTime, params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                String lastUpdate = object;
                SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.shared_prefs),Context.MODE_PRIVATE).edit();
                editor.putString(context.getString(R.string.last_update_time_parse), lastUpdate);
                editor.putString(context.getString(R.string.last_update_time), GregorianCalendar.getInstance().getTime().toString());
                editor.apply();

            }
        });
    }

    public String getLastUpdateTime(boolean isSql){

        String lastUpdate;
        if(isSql) {
            SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.shared_prefs), Context.MODE_PRIVATE);
            lastUpdate = prefs.getString(context.getString(R.string.last_update_time), null);

        }else{
            SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.shared_prefs), Context.MODE_PRIVATE);
            lastUpdate = prefs.getString(context.getString(R.string.last_update_time_parse), null);
        }

        return  lastUpdate;
    }

    public boolean checkUpdateInterval(){

        java.util.Date dateInMemory, currentDate;
        Long difference;
        currentDate = GregorianCalendar.getInstance().getTime();

        dateInMemory = new java.util.Date(Model.instance().getLastUpdateTime(true));
        difference = Math.abs(dateInMemory.getTime() - currentDate.getTime());


        return(difference / (24 * 60 * 60 * 1000) > 0.25);
    }
}
