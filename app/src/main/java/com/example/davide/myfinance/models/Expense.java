package com.example.davide.myfinance.models;

import java.util.Date;

public class Expense {

    private String expenseName;
    private boolean isRepeatingExpense;
    private int[] expenseDate = new int[3];
    private String expenseImage;

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

}
