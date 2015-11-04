package com.example.davide.myfinance;

import android.media.Image;

import java.util.ArrayList;

/**
 * Created by David on 11/4/15.
 */
public class ExpenseDB {
    private static ExpenseDB ourInstance = new ExpenseDB();

    public static ExpenseDB getInstance() {
        return ourInstance;
    }

    public static ArrayList<Expense> expensesDB= new ArrayList<>();

    private ExpenseDB() {
        initNewDB();
    }

    public void initNewDB(){
        int[] temp = {1,1,2016};
        Expense newExpense = new Expense("Ice Cream", true, temp);
        expensesDB.add(newExpense);
    }

    public int getSize(){
        return expensesDB.size();
    }

    public void addExpense(Expense expense){
        expensesDB.add(expense);
    }

    public void changeDate(int pos, int[] date){
        Expense temp = expensesDB.get(pos);
        temp.setExpenseDate(date);

        expensesDB.set(pos,temp);
    }

    public static class Expense {

        private String expenseName;
        private boolean isRepeatingExpense;
        private int[] expenseDate = new int[3];
        private String expenseImage;

        public Expense(String name, Boolean isRepeating, int[] date){
            this.expenseName = name;
            this.isRepeatingExpense = isRepeating;
            this.expenseDate = date;

        }

        public Expense(String name, Boolean isRepeating, int[] date, String expenseImage){
            this.expenseName = name;
            this.isRepeatingExpense = isRepeating;
            this.expenseDate = date;
            this.expenseImage = expenseImage;

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

    }
}
