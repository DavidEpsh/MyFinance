package com.example.davide.myfinance.models;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Category{

    ArrayList<Expense> expensesPerCategory;
    private String categoryName;
    private String categoryImage;
    private int categoryImageFromCode;
    private double categoryTotalThisWeek;
    private double categoryTotalThisMonth;
    private double categoryTotal;


    public Category(String categoryName, int categoryImageFromCode){

        expensesPerCategory = new ArrayList<>();
        this.categoryName = categoryName;
        this.categoryImageFromCode = categoryImageFromCode;
        categoryTotal = 0;
    }

    public void addExpense(Expense expense){

        categoryTotal += expense.getExpenseAmount();

        int i = expensesPerCategory.size();
        int[] inList, NewExp;

        NewExp = expense.getExpenseDate();

        for(int y=0; y < i; y++ ){
            inList = expensesPerCategory.get(y).getExpenseDate();

            if(inList[2] < NewExp[2] || (inList[2] == NewExp[2] && inList[1] < NewExp[1]) ||
                    (inList[2] == NewExp[2] && inList[1] == NewExp[1] && inList[0] < NewExp[0])){
                expensesPerCategory.add(y, expense);
                return;
            }
        }

        expensesPerCategory.add(expense);
    }

    public ArrayList<Expense> getExpensesPerCategory() {
        return expensesPerCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String expenseName) {
        this.categoryName = expenseName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public double getCategoryTotalThisWeek() {
        return categoryTotalThisWeek;
    }

    public void setCategoryTotalThisWeek(double categoryTotalThisWeek) {
        this.categoryTotalThisWeek = categoryTotalThisWeek;
    }

    public double getCategoryTotalThisMonth() {
        return categoryTotalThisMonth;
    }

    public void setCategoryTotalThisMonth(double categoryTotalThisMonth) {
        this.categoryTotalThisMonth = categoryTotalThisMonth;
    }

    public double getCategoryTotal() {
        return categoryTotal;
    }

//    public class MyDateComparator implements Comparator<Expense>
//    {
//        @Override
//        public int compare(Expense x, Expense y)
//        {
//            int[] x1, y1;
//
//            x1 = x.getExpenseDate();
//            y1 = y.getExpenseDate();
//
//            if(x1[2] > y1[2]){
//                return 1;
//            }else if(x1[2] < y1[2]){
//                return -1;
//            }
//
//            if(x1[1] > y1[1]){
//                return 1;
//            }else if(x1[2] < y1[2]){
//                return -1;
//            }
//
//            if(x1[0] > y1[0]){
//                return 1;
//            }else if(x1[2] < y1[2]){
//                return -1;
//            }else{
//                return 0;
//            }
//
//        }
//    }

}