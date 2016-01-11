package com.example.davide.myfinance.fragments;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.AddExpenseActivity;
import com.example.davide.myfinance.activities.MainActivity;
import com.example.davide.myfinance.adapters.AdapterViewPager;
import com.example.davide.myfinance.models.Model;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


public class FragmentSharedAccount extends Fragment {

    public static boolean isFirstLaunch = true;
    public static boolean needsUpdatingChart = false;
    private Typeface tf;


    public FragmentSharedAccount() {
    }

    public static FragmentSharedAccount newInstance() {
        FragmentSharedAccount fragment = new FragmentSharedAccount();
        return fragment;
    }

    private PieChart mChart;
    List<String> categories = new ArrayList<>();
    List<Double> expenses = new ArrayList<>();
    String fromDate, toDate;
    boolean animate;
    String sheetId;
    HashMap<String, Double> map;
    public ViewPager mPager;
    AdapterViewPager pagerAdapter;
    String fragName;
    View v;
    HashMap<String,Double> accountsAndAmounts;

    FloatingActionButton fab;
    FloatingActionsMenu fabMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_shared_account, container, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        setChartData();

        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fabMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.fab_menu);

        com.getbase.floatingactionbutton.FloatingActionButton fabNewExpense = (com.getbase.floatingactionbutton.FloatingActionButton) getActivity().findViewById(R.id.fab_new_expense);
        final com.getbase.floatingactionbutton.FloatingActionButton fabAddUser = (com.getbase.floatingactionbutton.FloatingActionButton) getActivity().findViewById(R.id.fab_add_user);

        setChartData();
        mChart.setCenterText("This Weeks Expenses");
        if(fromDate != null){
            String date = fromDate.substring(0,10);
            mChart.setCenterText("Expenses since " + date);
        }

        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialog(false); //false - add user
                fabMenu.collapse();
            }
        });

        fabNewExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
                if (mPager.getCurrentItem() == 1) {
                    intent.putExtra(MainActivity.SHEET_ID, MainActivity.acc2.sheetId.toString());
                } else if (mPager.getCurrentItem() == 2) {
                    intent.putExtra(MainActivity.SHEET_ID, MainActivity.acc3.sheetId.toString());
                } else if (mPager.getCurrentItem() == 0) {
                    intent.putExtra(MainActivity.SHEET_ID, MainActivity.acc1.sheetId.toString());
                }
                fabMenu.collapse();
                startActivityForResult(intent, MainActivity.RESULT_ADD_EXPENSE);
            }
        });

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int i) {
                pagerAdapter.notifyDataSetChanged();
                HashMap<String, String> map = Model.instance().returnMySheets();
                String checkHasAcc = map.get(MainActivity.acc1.fragName);
                String checkHasAcc2 = map.get(MainActivity.acc2.fragName);
                String checkHasAcc3 = map.get(MainActivity.acc3.getFragmentName());

                if(i == 0) {
                    if(fromDate != null){
                        String date = fromDate.substring(0,10);
                        mChart.setCenterText("Expenses since " + date);
                    }
                }

                if (i == 0 && MainActivity.acc1.sheetId == null) {
                    showOrHideFabAndFabMenu(true);

                    if (checkHasAcc == null) {
                        MainActivity.acc1.sheetId = ParseUser.getCurrentUser().getUsername();
                        Model.instance().addSheets(sheetId, "My Account", true);
                        Model.instance().addUserSheets(sheetId, ParseUser.getCurrentUser().getUsername(), true);
                    } else {
                        MainActivity.acc1.sheetId = checkHasAcc;
                    }
                } else if (i == 0 && MainActivity.acc1.sheetId != null) {
                    showOrHideFabAndFabMenu(true);

                } else if (i == 1 && MainActivity.acc2.getSheetId() == null) {

                    if (checkHasAcc2 != null) {
                        MainActivity.acc2.sheetId = checkHasAcc2;
                    } else {
                        fabMenu.setVisibility(View.INVISIBLE);
                        fab.hide();
                        buildAlertDialog(true);// true: activate new account
                    }
                } else if (i == 1 && MainActivity.acc2.getSheetId() != null) {
                    showOrHideFabAndFabMenu(false);
                    //setDataForAccount(Model.instance().getUsersAndSums(MainActivity.acc2.getSheetId()));
                    setChartData();
                    pagerAdapter.notifyDataSetChanged();


                } else if (i == 2 && MainActivity.acc3.getSheetId() == null) {
                    if (checkHasAcc3 != null) {
                        MainActivity.acc3.sheetId = checkHasAcc3;
                    } else {
                        fabMenu.setVisibility(View.INVISIBLE);
                        fab.hide();
                        buildAlertDialog(true);// true: activate new account
                    }
                } else if (i == 2 && MainActivity.acc3.getSheetId() != null) {
                    showOrHideFabAndFabMenu(false);
                    //setDataForAccount(Model.instance().getUsersAndSums(MainActivity.acc3.getSheetId()));
                }


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                FragmentExpenseList frag = new FragmentExpenseList();
                float val = e.getVal();
                for(String category : map.keySet()){
                    if(map.get(category) == val){
                        frag.setData(category, fromDate, toDate);
                        openFragmentBackStack(frag);
                        getActivity().setTitle("My " + category + " expenses");
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        return v;
    }


    public void showOrHideFabAndFabMenu(boolean showFab) {
        if (showFab) {
            fabMenu.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.VISIBLE);
        } else {
            fabMenu.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
        }
    }

    private void openFragmentBackStack(final Fragment fragment){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if(getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        transaction.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public PieData generatePieData() {

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();


        int i = 0;
        for (String name : map.keySet()) {
            xVals.add(name);
            double d = map.get(name);
            float f = (float) d;
            entries1.add(new Entry(f, i));
            i++;
        }

        PieDataSet ds1 = new PieDataSet(entries1, "");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.BLACK);
        ds1.setValueTextSize(13f);


        PieData d = new PieData(xVals, ds1);
        d.setValueTypeface(tf);

        return d;
    }


    public void setFragmentData(HashMap<String, Double> map, String fromDate, String toDate) {
        this.map = map;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getSheetId() {
        return this.sheetId;
    }

    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
    }

    public void buildAlertDialog(boolean newAccount) {

        if (!newAccount) {
            final EditText txtUserName = new EditText(getContext());
            txtUserName.setHint("Enter Username");

            new AlertDialog.Builder(getContext())
                    .setTitle("Add a user to this account")
                    .setView(txtUserName)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            if (mPager.getCurrentItem() == 1) {
                                //creating new account - (userSheetsId, sheetsId, userName)
                                Model.instance().addUserSheets(MainActivity.acc2.sheetId, txtUserName.getText().toString(), true);
                            } else {
                                //creating new account - (userSheetsId, sheetsId, userName)
                                Model.instance().addUserSheets(MainActivity.acc3.sheetId, txtUserName.getText().toString(), true);
                            }
                            fabMenu.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(getContext())
                    .setTitle("Activate This Account")
                    .setPositiveButton("Activate", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            GregorianCalendar cal = new GregorianCalendar();
                            Long temp = cal.getTimeInMillis();
                            String id = temp.toString();

                            if (mPager.getCurrentItem() == 1) {
                                MainActivity.acc2.sheetId = id;
                                Model.instance().addSheets(id.toString(), MainActivity.acc2.fragName, true);
                                Model.instance().addUserSheets(id.toString(), ParseUser.getCurrentUser().getUsername(), true);
                            } else {
                                MainActivity.acc3.sheetId = id;
                                Model.instance().addSheets(id.toString(), MainActivity.acc3.fragName, true);
                                Model.instance().addUserSheets(id.toString(), ParseUser.getCurrentUser().getUsername(), true);
                            }
                            fabMenu.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            fabMenu.setVisibility(View.INVISIBLE);
                        }
                    })
                    .show();
        }
    }

    public void setViewPagerAndAdapter(ViewPager viewPager, AdapterViewPager pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
        this.mPager = viewPager;
    }

    public void setFragmentName(String name) {
        this.fragName = name;
    }

    public String getFragmentName() {
        return this.fragName;
    }


    public void setChartData(){

        mChart = (PieChart) v.findViewById(R.id.pieChart_shared_accounts_fragment);
        mChart.setData(generatePieData());
        mChart.animateY(1200);

        if ((categories == null || expenses == null) || map != null && map.size() == 0) {
                mChart.setCenterText("Add Expense or Accounts");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setChartData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if(this.sheetId != null && !this.sheetId.equals(ParseUser.getCurrentUser().getUsername()) ) {
                if(map != null && this.sheetId.equals(MainActivity.acc2.sheetId)) {
                    map = Model.instance().getUsersAndSums(this.sheetId);
                    setChartData();
                    //pagerAdapter.notifyDataSetChanged();
                }else if (map != null){
                    map = Model.instance().getUsersAndSums(this.sheetId);
                }
            }
            if(fabMenu != null) {
                fabMenu.collapse();
            }
        }else{
            // fragment is no longer visible
        }
    }
}