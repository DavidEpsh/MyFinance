package com.example.davide.myfinance.fragments;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.davide.myfinance.R;
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
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shared_account, container, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        PieChart chart = (PieChart) v.findViewById(R.id.chart_overview_test);

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

    public void setData(List<String> categories, List<Double> expenses){
        this.categories = categories;
        this.expenses = expenses;
    }
}
