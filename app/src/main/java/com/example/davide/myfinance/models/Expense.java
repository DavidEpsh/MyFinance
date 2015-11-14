package com.example.davide.myfinance.models;

public class Expense {

    private String expenseName;
    private boolean isRepeatingExpense;
    private int[] expenseDate = new int[3];
    private int expenseImage;
    private double expenseAmount;

    public Expense(String name, Boolean isRepeating, int[] date){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.expenseDate = date;

    }

    public Expense(String name, Boolean isRepeating, int[] date, int expenseImage){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.expenseDate = date;
        this.expenseImage = expenseImage;

    }

    public Expense(String name, Boolean isRepeating, int[] date, int expenseImage, double amount){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.expenseDate = date;
        this.expenseImage = expenseImage;
        this.expenseAmount = amount;

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

    public boolean isRepeatingExpense() {
        return isRepeatingExpense;
    }

    public void setRepeatingEvent(boolean isCheckedIn) {
        this.isRepeatingExpense = isCheckedIn;
    }

    public int getExpenseImage() {
        return expenseImage;
    }

    public void setExpenseImage(int expenseImage) {
        this.expenseImage = expenseImage;
    }

    public double getExpenseAmount(){
        return this.expenseAmount;
    }

    public void setExpenseAmount(double amount){
        this.expenseAmount = amount;
    }

}