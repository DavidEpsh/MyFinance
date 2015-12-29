package com.example.davide.myfinance.models;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

public class Model {

    interface ModelInterface{
        public void addExpense(Expense expense);
        public void deleteExpense(Expense expense);
        public Expense getExpense(Long id);
        public List<Expense> getExpenses();
        public List<Expense> getExpensesByCategory(String category, String fromDate, String toDate);
        public Double getSumByCategory(String category, String fromDate, String toDate);
        public List<String> getCategories();
        public int updateExpense(Expense expense);
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

    public Expense getExpense(Long id){
        return modelImpl.getExpense(id);
    }

    public List<Expense> getExpenses(){
        return modelImpl.getExpenses();
    }

    public int updateExpense(Expense expense){
        return modelImpl.updateExpense(expense);
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
}

















