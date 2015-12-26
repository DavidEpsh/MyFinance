package com.example.davide.myfinance.models;

import com.example.davide.myfinance.R;

public class Expense{

    private String expenseName;
    private boolean isRepeatingExpense;
    private int isRepeatingExpenseSql;
    private int[] expenseDate = new int[3];
    private String expenseImage;
    private int tempExpenseImage;
    private double expenseAmount;
    private String category;
    private Long timeStamp;

    String dateSql;


    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Expense(String name, Boolean isRepeating, String date, String expenseImage, double amount, String category, Long timeStamp){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;

        if (isRepeating){
            isRepeatingExpenseSql = 1;
        }else {
            isRepeatingExpenseSql = 0;
        }

        this.dateSql = date;

        if(expenseImage != null) {
            this.expenseImage = expenseImage;
        }else{
            tempExpenseImage = R.mipmap.ic_launcher;
        }
        this.expenseAmount = amount;
        this.category = category;
        this.timeStamp = timeStamp;
    }


    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public int[] getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(int[] expenseDate) {
        this.expenseDate = expenseDate;
    }

    public int isRepeatingExpense() {
        return isRepeatingExpenseSql;
    }

    public boolean isRepeatingExpenseBool() {
        return isRepeatingExpense;
    }

    public void setRepeatingEvent(boolean isCheckedIn) {
        this.isRepeatingExpense = isCheckedIn;
    }

    public String getExpenseImage() {
        return expenseImage;
    }

    public void setExpenseImage(String expenseImage) {
        this.expenseImage = expenseImage;
    }

    public double getExpenseAmount(){
        return this.expenseAmount;
    }

    public void setExpenseAmount(double amount){
        this.expenseAmount = amount;
    }

    public int getTempExpenseImage() {
        return tempExpenseImage;
    }

    public void setTempExpenseImage(int tempExpenseImage) {
        this.tempExpenseImage = tempExpenseImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateSql() {
        return dateSql;
    }

    public void setDateSql(String dateSql) {
        this.dateSql = dateSql;
    }

}