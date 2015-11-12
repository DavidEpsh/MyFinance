package com.example.davide.myfinance;


import com.example.davide.myfinance.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDB {

    private List<Expense> expensesDB;

    private static ExpenseDB ourInstance = new ExpenseDB();

    public static ExpenseDB getInstance() {
        return ourInstance;
    }

    private ExpenseDB() {
        expensesDB = new ArrayList<>();
        addTestData();
    }

    public int getSize(){
        return expensesDB.size();
    }

    public void addExpense(Expense expense){
        expensesDB.add(expense);
    }

    public void editExpense(Expense expense, int position){
        expensesDB.remove(position);
        expensesDB.add(position, expense);
    }

    public Expense getExpense(int position){
        return expensesDB.get(position);
    }

    public void changeDate(int pos, int[] date){
        Expense temp = expensesDB.get(pos);
        temp.setExpenseDate(date);

        expensesDB.set(pos, temp);
    }

    public List getList(){
        return expensesDB;
    }

    private void addTestData() {
        int[] temp = {1, 0, 2015};
        Expense guest1 = new Expense("aaa", true, temp, R.mipmap.ic_launcher);
        expensesDB.add(guest1);

        int[] temp2 = {2, 0, 2015};
        Expense guest2 = new Expense("bbb", true, temp2, R.mipmap.ic_launcher);
        expensesDB.add(guest2);

        int[] temp3 = {3, 0, 2015};
        Expense guest3 = new Expense("ccc", true, temp3, R.mipmap.ic_launcher);
        expensesDB.add(guest3);
    }
}
