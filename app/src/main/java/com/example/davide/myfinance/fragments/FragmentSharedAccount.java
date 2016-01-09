package com.example.davide.myfinance.fragments;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.AddExpenseActivity;
import com.example.davide.myfinance.activities.MainActivity;
import com.example.davide.myfinance.models.Model;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
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
    boolean fabMenuVisible = false;
    boolean animate;
    String sheetId;
    HashMap<String, Double> map;
    public ViewPager mPager;
    String fragName;

    FloatingActionButton fab;
    FloatingActionsMenu fabMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shared_account, container, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");


        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fabMenu = (FloatingActionsMenu)getActivity().findViewById(R.id.fab_menu);

        mChart = (PieChart) v.findViewById(R.id.pieChart_shared_accounts_fragment);
        mChart.setData(generatePieData());
        mChart.animateY(1200);

        if(categories == null || expenses == null){
            mChart.setCenterText("Add Expense or Accounts");
        }

        com.getbase.floatingactionbutton.FloatingActionButton fabNewExpense = (com.getbase.floatingactionbutton.FloatingActionButton) getActivity().findViewById(R.id.fab_new_expense);
        final com.getbase.floatingactionbutton.FloatingActionButton fabAddUser = (com.getbase.floatingactionbutton.FloatingActionButton) getActivity().findViewById(R.id.fab_add_user);

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
                }
                startActivityForResult(intent, MainActivity.RESULT_ADD_EXPENSE);
            }
        });


        if(mPager != null) {
            int i = mPager.getCurrentItem();
            HashMap<String, String> map = Model.instance().returnMySheets();
            String checkHasAcc = map.get(fragName);
            String checkHasAcc2 = map.get(fragName);
            String checkHasAcc3 = map.get(fragName);

            if (i == 0 && MainActivity.acc1.sheetId == null) {
                fabMenu.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                fab.show();

                if (checkHasAcc == null) {
                    MainActivity.acc1.sheetId = ParseUser.getCurrentUser().getUsername();
                    Model.instance().addSheets(sheetId, "My Account", true);
                    Model.instance().addUserSheets(sheetId, ParseUser.getCurrentUser().getUsername());
                } else {
                    MainActivity.acc1.sheetId = checkHasAcc;
                }
            } else if (i == 0) {
                if (MainActivity.acc1.sheetId != null) {
                    fabMenu.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    fab.show();
                }
            } else if (i == 1 && MainActivity.acc2.getSheetId() == null) {

                if (checkHasAcc2 != null) {
                    MainActivity.acc2.sheetId = checkHasAcc2;
                } else {
                    fabMenu.setVisibility(View.INVISIBLE);
                    fab.hide();
                    buildAlertDialog(true);// true: activate new account
                }
            } else if (i == 1 && MainActivity.acc2.getSheetId() != null) {
                fabMenu.setVisibility(View.VISIBLE);
                fab.hide();

            } else if (i == 2 && MainActivity.acc3.getSheetId() == null) {
                if (checkHasAcc2 != null) {
                    MainActivity.acc2.sheetId = checkHasAcc2;
                } else {
                    fabMenu.setVisibility(View.VISIBLE);
                    fab.hide();
                    buildAlertDialog(true);// true: activate new account
                }
            } else if (i == 2 && MainActivity.acc3.getSheetId() != null) {
                fabMenu.setVisibility(View.VISIBLE);
                fab.hide();
            }
        }

        return v;
    }

    public PieData generatePieData(){

        int count;

        if(this.categories != null) {
            count = this.categories.size();
        }else{
            count = 0;
        }

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for(int i = 0; i < count; i++) {
            //xVals.add("entry" + (i+1));
            xVals.add(categories.get(i));
            double d = expenses.get(i);
            float f = (float) d;
            entries1.add(new Entry(f, i));
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


    public void setFragmentData(List<String> categories, List<Double> expenses, String fromDate, String toDate, boolean fabMenuVisible){
        this.categories = categories;
        this.expenses = expenses;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fabMenuVisible = fabMenuVisible;

    }

    public void setDataForAccount(HashMap<String, Double> map, boolean fabMenuVisible) {
        this.map = map;
        this.fabMenuVisible = fabMenuVisible;
        int size = map.size();

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                for (String name : map.keySet()) {
                    this.categories.add(name);
                    this.expenses.add(map.get(name));
                }
            }
        }
    }

    public String getSheetId(){
        return this.sheetId;
    }

    public void setSheetId(String sheetId){
        this.sheetId = sheetId;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void buildAlertDialog(boolean newAccount){

        if(!newAccount) {
            final EditText txtUserName = new EditText(getContext());
            txtUserName.setHint("Enter Username");

            new AlertDialog.Builder(getContext())
                    .setTitle("Add a user to this account")
                    .setView(txtUserName)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            long id = GregorianCalendar.getInstance().getTimeInMillis();
                            if (mPager.getCurrentItem() == 1) {
                                //creating new account - (userSheetsId, sheetsId, userName)
                                Model.instance().addUserSheets(MainActivity.acc2.sheetId.toString(), txtUserName.getText().toString());
                            }else{
                                //creating new account - (userSheetsId, sheetsId, userName)
                                Model.instance().addUserSheets(MainActivity.acc3.sheetId.toString(), txtUserName.getText().toString());
                            }
                            fabMenu.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();
        }else{
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
                                Model.instance().addUserSheets(id.toString(), ParseUser.getCurrentUser().getUsername());
                            }else{
                                MainActivity.acc3.sheetId = id;
                                Model.instance().addSheets(id.toString(), MainActivity.acc3.fragName, true);
                                Model.instance().addUserSheets(id.toString(), ParseUser.getCurrentUser().getUsername());
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

    public void setViewPager(ViewPager viewPager){
        this.mPager = viewPager;
    }

    public void setFragmentName(String name){
        this.fragName = name;
    }

    public String getFragmentName(){
        return this.fragName;
    }
}
