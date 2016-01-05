package com.example.davide.myfinance.models;

import android.content.Context;

import java.util.List;

public class Model {

    interface ModelInterface{
        public void addExpense(Expense expense);
        public void deleteExpense(Long expense);
        public Expense getExpense(Long id);
        public List<Expense> getExpenses();
        public List<Expense> getExpensesByCategory(String category, String fromDate, String toDate);
        public Double getSumByCategory(String category, String fromDate, String toDate);
        public List<String> getCategories();
        public void updateExpense(Expense expense);
        public void batchUpdateExpenses(List<Expense> expenses, BatchUpdateListener listener);
        public void syncSqlWithParse(SyncSqlWithParseListener listener);
        public List<Expense> getAllExpensesAsynch();
    }

    private static final Model instance = new Model();
    private ModelInterface modelImpl;
    ModelParse modelParse = new ModelParse();
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

    public void addExpense(Expense expense){
        modelImpl.addExpense(expense);
        modelParse.addOrUpdateAsync(expense);
    }

    public void deleteExpense(Long expense){
        modelImpl.deleteExpense(expense);
        modelParse.updateOrDelete(getExpense(expense), true);
    }

    public Expense getExpense(Long id){
        return modelImpl.getExpense(id);
    }

    public List<Expense> getExpenses(){
        return modelImpl.getExpenses();
    }

    public void updateExpense(Expense expense, boolean doDeleteExpense){
        modelImpl.updateExpense(expense);
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

    public interface BatchUpdateListener{
        public void onResult();
    }

    public String getLastUpdateTime(boolean isSql){
        return modelParse.getLastUpdateTime(isSql);
    }

    public boolean checkUpdateInterval(){
        return modelParse.checkUpdateInterval();
    }

    public void syncSqlWithParse(SyncSqlWithParseListener listener){
        modelImpl.syncSqlWithParse(listener);
    }

}

















