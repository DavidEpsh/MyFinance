package com.example.davide.myfinance.fragments;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.adapters.TabsPagerAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class FragmentSharedAccount extends Fragment {

    public static boolean isFirstLaunch = true;
    public static boolean needsUpdatingChart = false;
    private Typeface tf;

    private PieChart mChart;
    List<String> categories;
    List<Double> expenses;
    String fromDate, toDate;

    private static final String ARG_PAGE_NUMBER = "page_number";

    public FragmentSharedAccount() {
    }

    public static FragmentSharedAccount newInstance(int page) {
        FragmentSharedAccount fragment = new FragmentSharedAccount();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shared_account, container, false);
        int page = getArguments().getInt(ARG_PAGE_NUMBER, -1);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getActivity().getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        PieChart chart = (PieChart) v.findViewById(R.id.pieChart_shared_accounts_fragment);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if(!fab.isShown()) {
            fab.show();
        }
        chart.setData(generatePieData());
        chart.animateY(2000);

        return v;
    }

    public PieData generatePieData(){

        int count = this.categories.size();

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

    public void setFragmentData(List<String> categories, List<Double> expenses, String fromDate, String toDate){
        this.categories = categories;
        this.expenses = expenses;
        this.fromDate = fromDate;
        this.toDate = toDate;

    }
}
