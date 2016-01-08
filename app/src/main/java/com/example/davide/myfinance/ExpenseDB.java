package com.example.davide.myfinance;


import com.example.davide.myfinance.activities.MainActivity;
import com.example.davide.myfinance.models.Category;
import com.example.davide.myfinance.models.Expense;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ExpenseDB {

    private List<Expense> expensesDB;
    private List<Category> categoryDB;

    private static ExpenseDB ourInstance = new ExpenseDB();

    public static ExpenseDB getInstance() {
        return ourInstance;
    }

    private ExpenseDB() {
        categoryDB = new ArrayList<>();
        initializeCategories();

        expensesDB = new ArrayList<>();
        //addTestData();
    }

    public int getSize(){
        return expensesDB.size();
    }

    public void addExpense(Expense expense){
        expensesDB.add(expense);

        for(int i=0; i<categoryDB.size(); i++){

            if (expense.getCategory() == categoryDB.get(i).getCategoryName())
                categoryDB.get(i).addExpense(expense);
        }
    }

    public void removeExpense(int position){expensesDB.remove(position);}

    public void editExpense(Expense expense, int position){
        expensesDB.set(position, expense);
    }

    public Expense getExpense(int position){
        return expensesDB.get(position);
    }

    public List getList(){
        return expensesDB;
    }

    public String[] getCategoryNames(){
        String[] temp = new String[categoryDB.size()];
        for(int i=0; i<categoryDB.size(); i++){
            temp[i] = categoryDB.get(i).getCategoryName();
        }
        return temp;
    }

    public Category getCategory(String name){

        for (int i=0; i < categoryDB.size(); i++){
            if(categoryDB.get(i).getCategoryName() == name)
                return categoryDB.get(i);
        }

        return null;
    }

    public Category getCategory(int index){
        return categoryDB.get(index);

    }



//    private void addTestData() {
//        GregorianCalendar temp = new GregorianCalendar(2015,0,1);
//        Expense guest1 = new Expense("aaa", true, MainActivity.sdf.format(temp.getTime()), null, 111.1,"Shopping", GregorianCalendar.getInstance().getTimeInMillis());
//        addExpense(guest1);
//
//        GregorianCalendar temp2 = new GregorianCalendar(2015,1,2);
//        Expense guest2 = new Expense("bbb", true, MainActivity.sdf.format(temp2.getTime()), null, 222.2,"Travel", GregorianCalendar.getInstance().getTimeInMillis());
//        addExpense(guest2);
//
//        GregorianCalendar temp3 = new GregorianCalendar(2015,2,3);
//        Expense guest3 = new Expense("ccc", true, MainActivity.sdf.format(temp3.getTime()), null, 333.3,"Transportation", GregorianCalendar.getInstance().getTimeInMillis());
//        addExpense(guest3);
//    }

    private void initializeCategories(){
        categoryDB.add(new Category("Travel", R.drawable.ic_flight_takeoff_black));
        categoryDB.add(new Category("Shopping", R.drawable.ic_shopping_cart_black));
        categoryDB.add(new Category("Transportation", R.drawable.ic_directions_car_black));
    }
}
