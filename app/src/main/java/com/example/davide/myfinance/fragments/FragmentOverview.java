package com.example.davide.myfinance.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davide.myfinance.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class FragmentOverview extends Fragment {

    public static boolean isFirstLaunch = true;
    public static boolean needsUpdatingChart = false;
    private Typeface tf;

    private PieChart mChart;
    List<String> categories;
    List<Double> expenses;
    String fromDate, toDate;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        BarChart chart = (BarChart) v.findViewById(R.id.chart_overview);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if(!fab.isShown()) {
            fab.show();
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));

        BarDataSet dataset = new BarDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");



        BarData data = new BarData(labels, dataset);
        chart.setData(data);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setDescription("# of times Alice called Bob");


        chart.animateY(1000);

        return v;
    }

    public void setData(List<String> categories, List<Double> expenses){
        this.categories = categories;
        this.expenses = expenses;
    }
}
