package com.example.davide.myfinance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.fragments.FragmentExpenseList;
import com.example.davide.myfinance.fragments.FragmentHome;
import com.example.davide.myfinance.fragments.FragmentOverview;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.models.Model;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String ITEM_ID = "ID";
    public static int RESULT_FINISHED_EDITING = 1111;
    public static int RESULT_ADD_EXPENSE = 1112;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM/yyyy");
    public static List<String> allCategories = new ArrayList<>();

    FragmentHome fragmentHome;
    boolean test = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Model.instance().init(getApplicationContext());


        if(test = true) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            ParseCloud.callFunctionInBackground("getServerTime", params, new FunctionCallback<String>() {
                public void done(String ratings, ParseException e) {
                    if (e == null) {
                        // ratings is 4.5
                        Log.d("Test", "" + ratings);
                    } else
                        Log.d("NO RESPONSE", e + "");
                }
            });
            test = false;
        }
        //addTestSql();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivityForResult(intent, RESULT_ADD_EXPENSE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentHome = new FragmentHome();
        getSqlData(fragmentHome, MainActivity.sdf.format(getStartOfWeek().getTime()), null);
        openFragment(fragmentHome);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_home) {
            FragmentHome fragment = new FragmentHome();
            getSqlData(fragment, sdf.format(getStartOfWeek().getTime()), null);
            openFragment(fragment);
            setTitle("My Finance");

        } else if (id == R.id.drawer_expense_list) {
            FragmentExpenseList fragment = new FragmentExpenseList();
            fragment.setData(null, null, null);
            openFragment(fragment);
            setTitle("My Expenses");

        } else if (id == R.id.overview) {
            openFragment(new FragmentOverview());
            setTitle("Overview");

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,fragment)
                .commit();
    }

    public void addTestSql(){


        Expense expense = new Expense("sql1", true, "2015-04-04 01:01:01", null, 444.4, "Travel", GregorianCalendar.getInstance().getTimeInMillis());
        Expense expense2 = new Expense("sql2", true, "2015-05-05 02:02:02", null, 555.5, "Shopping", GregorianCalendar.getInstance().getTimeInMillis());
        Expense expense3 = new Expense("sql3", true, "2015-06-06 03:03:03", null, 666.6, "Transportation", GregorianCalendar.getInstance().getTimeInMillis());
        Expense expense4 = new Expense("sql4", true, "2015-12-27 12:00:12", null, 111.1, "Travel", GregorianCalendar.getInstance().getTimeInMillis());
        Expense expense5 = new Expense("sql5", true, "2015-12-27 15:00:43", null, 123.1, "Travel", GregorianCalendar.getInstance().getTimeInMillis());
        Model.instance().addExpense(expense5);
//        Model.instance().addExpense(expense2);
//        Model.instance().addExpense(expense3);
//        Model.instance().addExpense(expense4);
//        Model.instance().addExpense(expense5);
    }

    public void getSqlData(FragmentHome fragment, String fromDate, String toDate){
        //Getting all the categories that the user has
        this.allCategories = Model.instance().getCategories();
        List<String> usedCategories = new ArrayList<>();
        List<Double> expensesPerCategory = new ArrayList<>();


        for(int i = 0; i < this.allCategories.size(); i++){
            Double temp = Model.instance().getSumByCategory(this.allCategories.get(i), "2015-04-04 00:01:01", null);
            if(temp != null){
                usedCategories.add(this.allCategories.get(i));
                expensesPerCategory.add(temp);
            }

        }

        fragment.setFragmentData(usedCategories, expensesPerCategory, fromDate, toDate);
    }

    public Calendar getStartOfWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


            if(resultCode == RESULT_OK){
                getSqlData(fragmentHome, sdf.format(getStartOfWeek().getTime()), null);

            }
            if (resultCode == EditExpenseActivity.RESULT_CANCELED) {
                //User pressed back button
            }
        }

}
