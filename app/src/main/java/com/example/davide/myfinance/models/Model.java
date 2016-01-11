package com.example.davide.myfinance.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Model {

    interface ModelInterface{
        public void addExpense(Expense expense);
        public void deleteExpense(String expense);
        public Expense getExpense(String id);
        public List<Expense> getExpenses();
        public List<Expense> getExpensesByCategory(String category, String fromDate, String toDate);
        public Double getSumByCategory(String category, String fromDate, String toDate);
        public List<String> getCategories();
        public void updateOrAddExpense(Expense expense);
        public void batchUpdateExpenses(List<Expense> expenses, BatchUpdateListener listener);
        public void addUserSheets(String sheetId, String userName);
        public void addSheets(String id, String userName);
        public List<Expense> getAllExpensesAsynch();
        public HashMap<String, Double> getUsersAndSums(String sheetId);
        public String getExistingUsersSheet(String id, String userName);
        public void changeLastUpdateTime(ChangeTimeListener listener);
        public HashMap<String, String> returnMySheets();
    }

    private static final Model instance = new Model();
    private ModelInterface modelImpl;
    ModelParse modelParse = new ModelParse();
    ModelUsersAndAccountsSql modelUsersAndAccountsSql = new ModelUsersAndAccountsSql();
    Context context;

    private Model(){

    }

    public void init(Context applicationContext) {
        this.context = applicationContext;
        modelImpl = new ModelSql(applicationContext);
    }

    public void initParse(Context applicationContext) {
        modelParse.init(applicationContext);
    }

    public static Model instance(){
        return instance;
    }

    public void changeLastUdateTime(Collection collection, ChangeTimeListener listener){
        modelParse.changeLastUpdateTime(collection, listener);
    }

    public void addExpense(Expense expense){
        modelImpl.addExpense(expense);
        modelParse.addExpenseAsync(expense);
    }

    public void deleteExpense(String expense){
        modelImpl.deleteExpense(expense);
        modelParse.updateOrDelete(getExpense(expense), true);
    }

    public void getAllExpensesOrUpdateAsync(boolean isUpdate, GetAllExpensesOrUpdateAsync listener){
        modelParse.getAllExpensesOrUpdateAsync(isUpdate, listener);
    }

    public Expense getExpense(String id){
        return modelImpl.getExpense(id);
    }

    public List<Expense> getExpenses(){
        return modelImpl.getExpenses();
    }

    public void updateOrAddExpense(Expense expense, boolean doDeleteExpense){
        modelImpl.updateOrAddExpense(expense);
        modelParse.updateOrDelete(expense, doDeleteExpense);
    }

    public void batchUpdateExpenses(List<Expense> expenses, BatchUpdateListener listener){
        modelImpl.batchUpdateExpenses(expenses, listener);
    }

    public List<String> getCategories(){
        return modelImpl.getCategories();
    }

    public List<Expense> getExpensesByCategory(String category, String fromDate, String toDate){
        return modelImpl.getExpensesByCategory(category, fromDate, toDate);
    }

    public Double getSumByCategory(String category, String fromDate, String toDate){
        return modelImpl.getSumByCategory(category, fromDate, toDate);
    }

    public interface GetExpense{
        public void onResult(Expense expense);
    }

    public interface GetExpensesListener{
        public void onResult(List<Expense> expenses);
    }

    public interface SyncSqlWithParseListener{
        public void onResult();
    }

    public interface ChangeTimeListener{
        public void onResult();
    }

    public interface BatchUpdateListener{
        public void onResult();
    }

    public interface GetAllUsersSheetsListener{
        public void onResult();
    }

    public interface GetAllSheetsListener{
        public void onResult();
    }

    public interface GetAllExpensesOrUpdateAsync{
        public void onResult();
    }

    public String getLastUpdateTime(boolean isSql){
        return modelParse.getLastUpdateTime(isSql);
    }

    public boolean checkUpdateInterval(){
        return modelParse.checkUpdateInterval();
    }

    public void addUserSheets(String sheetId, String userName, boolean withParse) {
        modelImpl.addUserSheets(sheetId, userName);
        if(withParse) {
            modelParse.addUsersSheet(sheetId, userName);
        }
    }

    public void addSheets(String id, String sheetName, boolean withParseAdd){
        modelImpl.addSheets(id, sheetName);

        if(withParseAdd) {
            modelParse.addSheet(id, sheetName);
        }
    }

    public HashMap<String, Double> getUsersAndSums(String sheetId){
        return modelImpl.getUsersAndSums(sheetId);
    }

    public void getAllSheetsAndSync(String usersSheetsId, GetAllSheetsListener listener){
        modelParse.getAllSheetsAndSync(usersSheetsId, listener);
    }

    public String getExistingUsersSheet(String id, String userName){
        return modelImpl.getExistingUsersSheet(id, userName);
    }

    public HashMap<String, String> returnMySheets(){
        return modelImpl.returnMySheets();
    }



    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        saveImageToFile(imageBitmap, imageName); // synchronously save image locally
        Thread d = new Thread(new Runnable() {  // asynchronously save image to parse
            @Override
            public void run() {
                modelParse.saveImage(imageBitmap,imageName);
            }
        });
        d.start();
    }

    public interface LoadImageListener{
        public void onResult(Bitmap imageBmp);
    }

    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bmp = loadImageFromFile(imageName);              //first try to fin the image on the device
                if (bmp == null) {                                      //if image not found - try downloading it from parse
                    bmp = modelParse.loadImage(imageName);
                    if (bmp != null) saveImageToFile(bmp,imageName);    //save the image locally for next time
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(result);
            }
        };
        task.execute();
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        FileOutputStream fos;
        OutputStream out = null;
        try {
            File dir = context.getExternalFilesDir(null);
            out = new FileOutputStream(new File(dir,imageFileName));
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String fileName){
        String str = null;
        Bitmap bitmap = null;
        try {
            File dir = context.getExternalFilesDir(null);
            InputStream inputStream = new FileInputStream(new File(dir,fileName));
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}


















