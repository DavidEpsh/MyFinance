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
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ModelParse {

    //EXPENSE
    public static final String EXPENSE_OBJ = "Expense";
    public static final String USER_NAME = "username";
    public static final String IS_SAVED = "isSaved";
    public static final String UPDATED_AT = "updatedAt";
    public static final String TIMESTAMP = "expenseId";
    public static final String IMAGE_NAME = "imageName";
    public static final String IMAGES = "images";
    public static final String IMAGE_FILE = "imageFile";


    //USERS SHEETS
    public static final String USERS_SHEETS_TABLE = "UsersSheetsTable";
    public static final String SHEET_ID = "sheetId";
    public static final String SHEET = "Sheet";
    public static final String SHEET_NAME = "sheetName";

    Context context;
    HashMap<String, Object> params;

    public void init(Context context) {
        Parse.initialize(context, "asIB0JfInjKjiQV7kElOvPIBAAFYIu60f9MZrlx6", "9xZgwSeofzVrze8NJGKUUW1EpiNYu1qZRl3We0Z4");
        this.context = context;
    }

    public void getAllExpensesOrUpdateAsync(final boolean update, final Model.GetAllExpensesOrUpdateAsync listener){

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(USERS_SHEETS_TABLE);
        query.whereEqualTo(USER_NAME, ParseUser.getCurrentUser().getUsername());

        final Collection<String> collection = new ArrayList<>();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(ParseObject parseObject : objects){
                        String sheetId = parseObject.getString(SHEET_ID);

                        Model.instance().addUserSheets(sheetId, ParseUser.getCurrentUser().getUsername());
                        collection.add(sheetId);
                    }
                    getExpensesForSheetIds(update, collection);
                    getAllSheets(update,collection);
                }
                listener.onResult();
            }
        });

    }

    public void getExpensesForSheetIds(final boolean update, Collection<String> collection){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(EXPENSE_OBJ);
        query.whereContainedIn(SHEET_ID, collection);

        if(update){
            query.whereGreaterThan(UPDATED_AT, getLastUpdateTime(false));
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(ParseObject parseObject : objects){
                        Long id = parseObject.getLong(TIMESTAMP);
                        String name = parseObject.getString("name");
                        boolean isRepeating = parseObject.getBoolean("repeating");
                        String date = parseObject.getString("date");
                        String category = parseObject.getString("category");
                        String imageName = parseObject.getString(IMAGE_NAME);
                        Double amount = parseObject.getDouble("amount");
                        String sheetId = parseObject.getString(SHEET_ID);

                        Expense expense = new Expense(name, isRepeating, date, imageName, amount, category, id, sheetId);
                        Model.instance().updateOrAddExpense(expense, false);
                    }
                }
            }
        });
    }
    public void getAllSheets(final boolean update, Collection<String> collection){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(SHEET);
        query.whereContainedIn(SHEET_ID, collection);

        if(update){
            query.whereGreaterThan(UPDATED_AT, getLastUpdateTime(false));
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    for(ParseObject parseObject : objects){
                        String sheetId = parseObject.getString(SHEET_ID);
                        String sheetName = parseObject.getString(SHEET_NAME);

                        Model.instance().addSheets(sheetId, sheetName, false);
                    }
                }
            }
        });
    }



    public void updateOrDelete(final Expense expense, final boolean doDeleteExpense){

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(EXPENSE_OBJ);
        query.whereEqualTo(TIMESTAMP, expense.getTimeStamp());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseObject newObject = objects.get(0);
                    newObject.put(USER_NAME, ParseUser.getCurrentUser().getUsername());
                    newObject.put(TIMESTAMP, expense.getTimeStamp());
                    newObject.put("name", expense.getExpenseName());
                    newObject.put("repeating", expense.isRepeatingExpenseBool());

                    if (expense.getExpenseImage() != null) {
                        newObject.put(IMAGE_NAME, expense.getExpenseImage());
                    }
                    newObject.put("date", expense.getDateSql());
                    newObject.put("category", expense.getCategory());
                    newObject.put("amount", expense.getExpenseAmount());
                    newObject.put(SHEET_ID, expense.getSheetId());

                    if (doDeleteExpense) {
                        newObject.put(IS_SAVED, 0);
                    } else {
                        newObject.put(IS_SAVED, 1);
                    }


                    newObject.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.d("Parse", "Unable to save" + expense.getExpenseName());
                            }
                        }
                    });
                }
            }
        });
    }

    public void addExpenseAsync(final Expense expense) {
        final ParseObject newObject = new ParseObject(EXPENSE_OBJ);
        newObject.put(USER_NAME, ParseUser.getCurrentUser().getUsername());
        newObject.put(TIMESTAMP, expense.getTimeStamp());
        newObject.put("name", expense.getExpenseName());
        newObject.put("repeating", expense.isRepeatingExpenseBool());

        if(expense.getExpenseImage() != null) {
            newObject.put(IMAGE_NAME, expense.getExpenseImage());
        }
        newObject.put("date", expense.getDateSql());
        newObject.put("category", expense.getCategory());
        newObject.put("amount", expense.getExpenseAmount());
        newObject.put(IS_SAVED, 1);
        newObject.put(SHEET_ID, expense.getSheetId());

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
            query = new ParseQuery<ParseObject>(EXPENSE_OBJ);
            query.whereContains(USER_NAME, ParseUser.getCurrentUser().getUsername());
            query.whereEqualTo(IS_SAVED, 1);
        }else{
            query = new ParseQuery<ParseObject>(EXPENSE_OBJ);
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
                        Long id = po.getLong(TIMESTAMP);
                        String name = po.getString("name");
                        boolean isRepeating = po.getBoolean("repeating");
                        String date = po.getString("date");
                        String category = po.getString("category");
                        String imageName = po.getString(IMAGE_NAME);
                        Double amount = po.getDouble("amount");
                        String sheetId = po.getString(SHEET_ID);

                        Expense expense = new Expense(name, isRepeating, date, imageName, amount, category, id, sheetId);
                        Expenses.add(expense);
                    }
                }
                listener.onResult(Expenses);
            }
        });

        changeLastUpdateTime(new Model.ChangeTimeListener() {
            @Override
            public void onResult() {

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
        ParseObject images = new ParseObject(IMAGES);
        images.put(IMAGE_NAME, imageName);
        images.put(IMAGE_FILE, file);
        try {
            images.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadImage(String imageName) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(IMAGES);
        query.whereEqualTo(IMAGE_NAME, imageName);
        try {
            List<ParseObject> list = query.find();
            if (list.size() > 0) {
                ParseObject po = list.get(0);
                ParseFile pf = po.getParseFile(IMAGE_FILE);
                byte[] data = pf.getData();
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                return bmp;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void addUsersSheet(String sheetId, String userName){

        final ParseObject newObject = new ParseObject(USERS_SHEETS_TABLE);
        newObject.put(SHEET_ID, sheetId);
        newObject.put(USER_NAME, userName);

        newObject.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("Parse", "Unable to start a new account");
                }
            }
        });
    }

    public void addSheet(String id, String sheetName){

        final ParseObject newObject = new ParseObject(SHEET);
        newObject.put(SHEET_ID, id);
        newObject.put(SHEET_NAME, sheetName);

        newObject.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("Parse", "Unable to start a new account");
                }
            }
        });
    }

    public void changeLastUpdateTime(final Model.ChangeTimeListener listener){

        final String getTime = "getServerTime";

        params = new HashMap<String, Object>();
        ParseCloud.callFunctionInBackground(getTime, params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                String lastUpdate = object;
                SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.shared_prefs), Context.MODE_PRIVATE).edit();
                editor.putString(context.getString(R.string.last_update_time_parse), lastUpdate);
                editor.putString(context.getString(R.string.last_update_time), GregorianCalendar.getInstance().getTime().toString());
                editor.apply();

                listener.onResult();
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


























//    public List<Expense> getAllExpenses() {
//        List<Expense> Expenses = new LinkedList<Expense>();
//        ParseQuery query = new ParseQuery(EXPENSE_OBJ);
//        query.whereContains(USER_NAME, ParseUser.getCurrentUser().getUsername());
//
//        try {
//            List<ParseObject> data = query.find();
//            for (ParseObject po : data) {
//                Long id = po.getLong(TIMESTAMP);
//                String name = po.getString("name");
//                boolean isRepeating = po.getBoolean("repeating");
//                String date = po.getString("date");
//                String category = po.getString("category");
//                String imageName = po.getString(IMAGE_NAME);
//                Double amount = po.getDouble("amount");
//                long userSheetId = po.getLong(USERS_SHEETS_ID);
//                Expense expense = new Expense(name, isRepeating, date, imageName, amount, category,id, userSheetId);
//                Expenses.add(expense);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return Expenses;
//        }
//
//        changeLastUpdateTime();
//        return Expenses;
//    }

//    public List<Expense> getAllExpensesSinceLastUpdate() {
//        ParseQuery query;
//        List<Expense> expenses;
//
//        if (getLastUpdateTime(true) == null) {
//            expenses = new LinkedList<Expense>();
//            query = new ParseQuery(EXPENSE_OBJ);
//        }else{
//            expenses = new LinkedList<Expense>();
//            query = new ParseQuery(EXPENSE_OBJ);
//            query.whereGreaterThan(UPDATED_AT, getLastUpdateTime(false));
//        }
//        try {
//            List<ParseObject> data = query.find();
//            for (ParseObject po : data) {
//                Long id = po.getLong(TIMESTAMP);
//                String name = po.getString("name");
//                boolean isRepeating = po.getBoolean("repeating");
//                String date = po.getString("date");
//                String category = po.getString("category");
//                String imageName = po.getString(IMAGE_NAME);
//                Double amount = po.getDouble("amount");
//                long userSheetId = po.getLong(USERS_SHEETS_ID);
//
//                Expense expense = new Expense(name, isRepeating, date, imageName, amount, category,id,userSheetId);
//                expenses.add(expense);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return expenses;
//        }
//
//        changeLastUpdateTime();
//        return expenses;
//    }

    public void getExpenseById(Long id, final Model.GetExpense listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(EXPENSE_OBJ);
        query.whereEqualTo(TIMESTAMP, id);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Expense expense = null;
                if (e == null && parseObjects.size() > 0) {
                    ParseObject po = parseObjects.get(0);
                    Long id = po.getLong(TIMESTAMP);
                    String name = po.getString("name");
                    boolean isRepeating = po.getBoolean("repeating");
                    String date = po.getString("date");
                    String category = po.getString("category");
                    String imageName = po.getString(IMAGE_NAME);
                    Double amount = po.getDouble("amount");
                    String sheetId = po.getString(SHEET_ID);

                    expense = new Expense(name, isRepeating, date, imageName, amount, category, id, sheetId);
                }
                listener.onResult(expense);
            }
        });
    }










//    public void getAllUsersSheetsAndSync(final Model.GetAllUsersSheetsListener listener) {
//        ParseQuery query = new ParseQuery(USERS_SHEETS_TABLE);
//        query.whereEqualTo(USER_NAME, ParseUser.getCurrentUser().getUsername());
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> parseObjects, ParseException e) {
//                if (e == null) {
//                    for (ParseObject po : parseObjects) {
//                        String userSheetId = po.getString(SHEET_ID);
//
//                        Model.instance().addUserSheets(userSheetId, ParseUser.getCurrentUser().getUsername());
//
//                        getAllSheetsAndSync(userSheetId, new Model.GetAllSheetsListener() {
//                            @Override
//                            public void onResult() {
//
//                            }
//                        });
//
//                        getRelevantExpenses(usersSheetsId, new Model.GetRelevantExpensesListener() {
//                            @Override
//                            public void onResult() {
//
//                            }
//                        });
//
//                    }
//                }
//                listener.onResult();
//            }
//        });
//    }

    public void getAllSheetsAndSync(String sheetsId, final Model.GetAllSheetsListener listener) {
        ParseQuery query = new ParseQuery(SHEET);
        query.whereEqualTo(SHEET_ID, sheetsId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject po : parseObjects) {
                        String sheetsId = po.getString(SHEET_ID);
                        String sheetName = po.getString(SHEET_NAME);

                        Model.instance().addSheets(sheetsId, sheetName, false);
                    }
                }
                listener.onResult();
            }
        });
    }

    public void getRelevantExpenses(long usersSheetId, final Model.GetRelevantExpensesListener listener) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(EXPENSE_OBJ);
        query.whereEqualTo(SHEET_ID, usersSheetId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0) {
                    ParseObject po = parseObjects.get(0);
                    Long id = po.getLong(TIMESTAMP);
                    String name = po.getString("name");
                    boolean isRepeating = po.getBoolean("repeating");
                    String date = po.getString("date");
                    String category = po.getString("category");
                    String imageName = po.getString(IMAGE_NAME);
                    Double amount = po.getDouble("amount");
                    String sheetId = po.getString(SHEET_ID);

                    Model.instance().addExpense(new Expense(name, isRepeating, date, imageName, amount, category, id, sheetId));
                }
                listener.onResult();
            }
        });
    }


}
