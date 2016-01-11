package com.example.davide.myfinance.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.davide.myfinance.fragments.FragmentSharedAccount;
import com.example.davide.myfinance.models.Model;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String SHEET_ID = "SHEET_ID";
    public static String USER_ID = "USER_ID";
    public static int RESULT_FINISHED_EDITING = 1111;
    public static int RESULT_ADD_EXPENSE = 1112;
    public static int RESULT_LOG_IN_SIGN_UP = 1113;
    static final int REQUEST_IMAGE_CAPTURE = 1114;
    public static String EXPENSE_ID = "EXPENSE_ID";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss.SSS'Z'");
    public static List<String> allCategories = new ArrayList<>();

    public static FragmentSharedAccount acc1 = FragmentSharedAccount.newInstance();
    public static FragmentSharedAccount acc2 = FragmentSharedAccount.newInstance();
    public static FragmentSharedAccount acc3 = FragmentSharedAccount.newInstance();
    ViewPager viewPager;
    AdapterViewPager viewPagerAdapter;
    FloatingActionsMenu fabMenu;

    Dialog dialog;


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

        if (ParseUser.getCurrentUser() == null) {
            Intent intentLogIn = new Intent(MainActivity.this, SignUpSignInActivity.class);
            startActivityForResult(intentLogIn, RESULT_LOG_IN_SIGN_UP);

        } else if (Model.instance().getLastUpdateTime(true) == null) {
            startUpdate(false);
        } else if (Model.instance().checkUpdateInterval()) {
            startUpdate(true);
        } else {
            setFragmentData();
            setTabLayout();
        }

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String name;
            name = intent.getStringExtra(Intent.EXTRA_TEXT);
            Intent intentNew = new Intent(MainActivity.this, AddExpenseActivity.class);
            intentNew.putExtra("name", name);
            startActivityForResult(intentNew, RESULT_ADD_EXPENSE);
        }

        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            setTabLayout();
            setFragmentData();
            getSupportFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
    }

    public void startUpdate(boolean needUpdate){
        dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.show();

        Model.instance().getAllExpensesOrUpdateAsync(needUpdate, new Model.GetAllExpensesOrUpdateAsync() {
            @Override
            public void onResult() {
                dialog.hide();
                setFragmentData();
                setTabLayout();
            }
        });
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
            dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.dialog_loading);
            dialog.show();

            Model.instance().getAllExpensesOrUpdateAsync(true, new Model.GetAllExpensesOrUpdateAsync() {
                @Override
                public void onResult() {
                    dialog.hide();
                }
            });
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
            setTabLayout();
            setFragmentData();
            getSupportFragmentManager().popBackStack();


        } else if (id == R.id.drawer_expense_list) {
            FragmentExpenseList fragment = new FragmentExpenseList();
            fragment.setData(null, null, null);
            openFragmentBackStack(fragment);
            setTitle("My Expenses");

        } else if (id == R.id.overview) {


        } else if (id == R.id.nav_log_out) {
            ParseUser.logOut();
            Intent intentLogIn = new Intent(MainActivity.this, SignUpSignInActivity.class);
            startActivityForResult(intentLogIn, RESULT_LOG_IN_SIGN_UP);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(final Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss();
    }

    private void openFragmentBackStack(final Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void getSqlDataForMainAccount(FragmentSharedAccount fragment, String fromDate, String toDate){
        //Getting all the categories that the user has
        this.allCategories = Model.instance().getCategories();
        List<String> usedCategories = new ArrayList<>();
        List<Double> expensesPerCategory = new ArrayList<>();
        HashMap<String, Double> map = new HashMap<>();


        for(int i = 0; i < this.allCategories.size(); i++){
            Double temp = Model.instance().getSumByCategory(this.allCategories.get(i), fromDate, null);
            if(temp != null){
                usedCategories.add(this.allCategories.get(i));
                expensesPerCategory.add(temp);
                map.put(this.allCategories.get(i),temp);
            }

        }

        fragment.setFragmentData(map, fromDate, toDate);
    }

    public void setDataForAccount(FragmentSharedAccount fragment, HashMap<String, Double> map) {
        List<String> usedCategories = new ArrayList<>();
        List<Double> expensesPerCategory = new ArrayList<>();
        int size = map.size();

        if (size > 0) {
            for (String name : map.keySet()) {
                usedCategories.add(name);
                expensesPerCategory.add(map.get(name));
            }
        }
        fragment.setFragmentData(map, null, null);
    }

    public static Calendar getStartOfWeek(boolean getPreviousWeek){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if(getPreviousWeek){
            calendar.get(Calendar.WEEK_OF_YEAR);
            //MainActivity.sdf.format(calendar.getTime());
        }

        return calendar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_LOG_IN_SIGN_UP) {
            if (resultCode == RESULT_CANCELED) {
                finish();

            } else if (resultCode == RESULT_OK) {
                setTabLayout();
                setFragmentData();
                viewPagerAdapter.notifyDataSetChanged();

            }
        }else if(requestCode == RESULT_ADD_EXPENSE){
            if (resultCode == RESULT_OK) {
                setFragmentData();
                viewPagerAdapter.notifyDataSetChanged();
                viewPager.setOffscreenPageLimit(1);

            }else if (resultCode == RESULT_CANCELED) {
                //User pressed back button
            }
        }else {
            if (resultCode == RESULT_OK) {
                getSqlDataForMainAccount(acc1, sdf.format(getStartOfWeek(false).getTime()), null);

            }
            if (resultCode == RESULT_CANCELED) {
                //User pressed back button
            }
        }
    }

    public void setTabLayout(){
        HashMap<String, String> map = Model.instance().returnMySheets();

        setTitle(R.string.app_name);
        if(fabMenu != null) {
            fabMenu.setVisibility(View.INVISIBLE);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new AdapterViewPager(getSupportFragmentManager());
        viewPagerAdapter.addFragment(acc1, "My Account");
        acc1.setViewPagerAndAdapter(viewPager, viewPagerAdapter);

        acc2.setSheetId(map.get("Home"));
        viewPagerAdapter.addFragment(acc2, "Home");
        acc2.setViewPagerAndAdapter(viewPager, viewPagerAdapter);

        acc3.setSheetId(map.get("Trip"));
        viewPagerAdapter.addFragment(acc3, "Trip");
        acc3.setViewPagerAndAdapter(viewPager, viewPagerAdapter);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);

        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }

    public void setFragmentData() {

        getSqlDataForMainAccount(acc1, MainActivity.sdf.format(getStartOfWeek(false).getTime()), null);
        setDataForAccount(acc2, Model.instance().getUsersAndSums(MainActivity.acc2.getSheetId()));
        setDataForAccount(acc3, Model.instance().getUsersAndSums(MainActivity.acc3.getSheetId()));
    }

}
