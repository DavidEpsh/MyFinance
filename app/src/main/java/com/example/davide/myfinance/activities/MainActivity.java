package com.example.davide.myfinance.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.example.davide.myfinance.adapters.AdapterViewPager;
import com.example.davide.myfinance.fragments.FragmentExpenseList;
import com.example.davide.myfinance.fragments.FragmentHome;
import com.example.davide.myfinance.fragments.FragmentSharedAccount;
import com.example.davide.myfinance.models.Model;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String ITEM_ID = "ID";
    public static int RESULT_FINISHED_EDITING = 1111;
    public static int RESULT_ADD_EXPENSE = 1112;
    public static int RESULT_LOG_IN_SIGN_UP = 1113;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss.SSS'Z'");
    public static List<String> allCategories = new ArrayList<>();

    FragmentHome fragmentHome;
    boolean test = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        if(ParseUser.getCurrentUser() == null) {
            Intent intentLogIn = new Intent(MainActivity.this, SignUpSignInActivity.class);
            startActivityForResult(intentLogIn, RESULT_LOG_IN_SIGN_UP);

        }else if(Model.instance().checkUpdateInterval() || Model.instance().getLastUpdateTime(true) == null){

            final Dialog dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.dialog_loading);
            dialog.show();

            Model.instance().syncSqlWithParse(new Model.SyncSqlWithParseListener() {
                @Override
                public void onResult() {
                    fragmentHome = new FragmentHome();
                    fragmentHome.needsUpdatingChart = true;
                    getSqlData(fragmentHome, MainActivity.sdf.format(getStartOfWeek().getTime()), null);
                    dialog.hide();
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
            openFragment(new FragmentSharedAccount());

        } else if (id == R.id.nav_log_out) {
            ParseUser.logOut();
            Intent intentLogIn = new Intent(MainActivity.this, SignUpSignInActivity.class);
            startActivityForResult(intentLogIn, RESULT_LOG_IN_SIGN_UP);

        } else if (id == R.id.nav_shared_accounts) {
//            final Dialog dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//            dialog.setContentView(R.layout.dialog_loading);
//            dialog.show();

            // TODO: 05/01/2016  

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container,fragment)
                .commitAllowingStateLoss();
    }

    public void getSqlData(FragmentHome fragment, String fromDate, String toDate){
        //Getting all the categories that the user has
        this.allCategories = Model.instance().getCategories();
        List<String> usedCategories = new ArrayList<>();
        List<Double> expensesPerCategory = new ArrayList<>();


        for(int i = 0; i < this.allCategories.size(); i++){
            Double temp = Model.instance().getSumByCategory(this.allCategories.get(i), fromDate, null);
            if(temp != null){
                usedCategories.add(this.allCategories.get(i));
                expensesPerCategory.add(temp);
            }

        }

        fragment.setFragmentData(usedCategories, expensesPerCategory, fromDate, toDate);
    }

    public void getSqlData(FragmentSharedAccount fragment, String fromDate, String toDate){
        //Getting all the categories that the user has
        this.allCategories = Model.instance().getCategories();
        List<String> usedCategories = new ArrayList<>();
        List<Double> expensesPerCategory = new ArrayList<>();


        for(int i = 0; i < this.allCategories.size(); i++){
            Double temp = Model.instance().getSumByCategory(this.allCategories.get(i), fromDate, null);
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

        if (requestCode == RESULT_LOG_IN_SIGN_UP) {
            if (resultCode == RESULT_CANCELED) {
                finish();

            } else if (resultCode == RESULT_OK) {
//                fragmentHome = new FragmentHome();
//                fragmentHome.needsUpdatingChart = true;
//                getSqlData(fragmentHome, MainActivity.sdf.format(getStartOfWeek().getTime()), null);
//                openFragment(fragmentHome);

                setTabLayoutTest();
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

    public void setTabLayoutTest(){

        FragmentSharedAccount fragment1 = new FragmentSharedAccount();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        AdapterViewPager adapter = new AdapterViewPager(getSupportFragmentManager());
        adapter.addFragment(fragment1, "ONE");
        adapter.addFragment(fragment1, "TWO");
        adapter.addFragment(fragment1, "THREE");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        getSqlData(fragment1, MainActivity.sdf.format(getStartOfWeek().getTime()), null);
        openFragment(fragment1);

    }

}
