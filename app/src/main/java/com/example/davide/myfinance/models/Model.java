package com.example.davide.myfinance.models;

import android.content.Context;

import java.util.List;

public class Model {

    interface ModelInterface{
        public void addExpense(Expense expense);
        public void deleteExpense(Expense expense);
        public Expense getExpense(String id);
        public List<Expense> getExpenses();
        public List<Expense> getExpensesByCategory(String category);
    }

    private static final Model instance = new Model();
    private ModelInterface modelImpl;

    private Model(){

    }

    public void init(Context applicationContext) {
        modelImpl = new ModelSql(applicationContext);
    }

    public static Model instance(){
        return instance;
    }

    public void addExpense(Expense expense){
        modelImpl.addExpense(expense);
    }

    public void deleteExpense(Expense expense){
        modelImpl.deleteExpense(expense);
    }

    public Expense getExpense(String id){
        return modelImpl.getExpense(id);
    }

    public List<Expense> getExpenses(){
        return modelImpl.getExpenses();
    }

    public List<Expense> getExpensesByCategory(String category){
        return modelImpl.getExpensesByCategory(category);
    }
}

















