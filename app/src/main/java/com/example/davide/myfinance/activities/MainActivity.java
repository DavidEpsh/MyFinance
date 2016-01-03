package com.example.davide.myfinance.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.fragments.FragmentExpenseList;
import com.example.davide.myfinance.fragments.FragmentHome;
import com.example.davide.myfinance.fragments.FragmentOverview;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.models.Model;
import com.parse.Parse;
import com.parse.ParseUser;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String ITEM_ID = "ID";
    public static int RESULT_FINISHED_EDITING = 1111;
    public static int RESULT_ADD_EXPENSE = 1112;
    public static int RESULT_LOG_IN_SIGN_UP = 1113;
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


        if(ParseUser.getCurrentUser() == null) {
            Intent intentLogIn = new Intent(MainActivity.this, SignUpSignInActivity.class);
            startActivityForResult(intentLogIn, RESULT_LOG_IN_SIGN_UP);

        }else if(checkUpdateInterval()){
            Model.instance().syncSqlWithParse(new Model.SyncSqlWithParseListener() {
                @Override
                public void onResult() {
                    fragmentHome = new FragmentHome();
                    getSqlData(fragmentHome, MainActivity.sdf.format(getStartOfWeek().getTime()), null);
                    openFragment(fragmentHome);
                }
            });

        }else{
            fragmentHome = new FragmentHome();
            getSqlData(fragmentHome, MainActivity.sdf.format(getStartOfWeek().getTime()), null);
            openFragment(fragmentHome);
        }

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)) {
            String name;
            name = intent.getStringExtra(Intent.EXTRA_TEXT);
            Intent intentNew = new Intent(MainActivity.this, AddExpenseActivity.class);
            intentNew.putExtra("name", name);
            startActivityForResult(intentNew, RESULT_ADD_EXPENSE);
        }


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

        } else if (id == R.id.nav_log_out) {
            ParseUser.logOut();
            Intent intentLogIn = new Intent(MainActivity.this, SignUpSignInActivity.class);
            startActivityForResult(intentLogIn, RESULT_LOG_IN_SIGN_UP);

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

    public boolean checkUpdateInterval(){

        java.util.Date dateInMemory, currentDate;
        Long difference;
        currentDate = GregorianCalendar.getInstance().getTime();

        if(Model.instance().getLastUpdateTime() != null) {
            try {
                dateInMemory = sdf.parse(Model.instance().getLastUpdateTime());
                difference = Math.abs(dateInMemory.getTime() - currentDate.getTime());
                if(difference / (24 * 60 * 60 * 1000) > 0.5)
                    return true;

            } catch (ParseException e) {
                e.printStackTrace();

                // TODO: 02/01/2016 remove next line
                Toast.makeText(this, "Unable to parse date in cheackUpdateInterval()", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOG_IN_SIGN_UP) {
            if (resultCode == RESULT_CANCELED) {
                finish();

            } else if (resultCode == RESULT_OK) {
                fragmentHome = new FragmentHome();
                getSqlData(fragmentHome, MainActivity.sdf.format(getStartOfWeek().getTime()), null);
                openFragment(fragmentHome);
            }
        }else {

            if (resultCode == RESULT_OK) {
                getSqlData(fragmentHome, sdf.format(getStartOfWeek().getTime()), null);

            }
            if (resultCode == RESULT_CANCELED) {
                //User pressed back button
            }
        }
    }

}
