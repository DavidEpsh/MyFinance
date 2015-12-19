package com.example.davide.myfinance.models;

public class Expense {

    private String expenseName;
    private boolean isRepeatingExpense;
    private int[] expenseDate = new int[3];
    private String expenseImage;
    private int tempExpenseImage;
    private double expenseAmount;
    private String category;


    public Expense(String name, Boolean isRepeating, int[] date, int tempExpenseImage, double amount, String category) {
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.expenseDate = date;
        this.tempExpenseImage = tempExpenseImage;
        this.expenseAmount = amount;
        this.category = category;
    }

    public Expense(String name, Boolean isRepeating, int[] date, int tempExpenseImage, double amount) {
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.expenseDate = date;
        this.tempExpenseImage = tempExpenseImage;
        this.expenseAmount = amount;
    }

    public Expense(String name, Boolean isRepeating, int[] date, String expenseImage, double amount){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.expenseDate = date;
        this.expenseImage = expenseImage;
        this.expenseAmount = amount;

    }

    public Expense(String name, Boolean isRepeating, int[] date, String expenseImage, double amount, String category){
        this.expenseName = name;
        this.isRepeatingExpense = isRepeating;
        this.expenseDate = date;
        this.expenseImage = expenseImage;
        this.expenseAmount = amount;
        this.category = category;
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


}