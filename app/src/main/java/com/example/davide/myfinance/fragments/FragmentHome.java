package com.example.davide.myfinance.fragments;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davide.myfinance.ExpenseDB;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.MainActivity;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.models.Model;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class FragmentHome extends Fragment implements OnChartValueSelectedListener {

    public static boolean isFirstLaunch = true;
    public static boolean needsUpdatingChart = false;
    private Typeface tf;

//    public static Fragment newInstance() {
//        return new FragmentHome();
//    }

    private PieChart mChart;
    List<String> categories;
    List<Double> expenses;
    String fromDate, toDate;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if(!fab.isShown()) {
            fab.show();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String outputDate = MainActivity.sdf.format(calendar.getTime());

        
        mChart = (PieChart) v.findViewById(R.id.pieChart_home_fragment);
        mChart.setDescription("");
        
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        
        mChart.setCenterTextTypeface(tf);
        mChart.setCenterText(generateCenterText(null));
        mChart.setCenterTextSize(10f);
        mChart.setCenterTextTypeface(tf);
         
        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);

        mChart.getLegend().setEnabled(false);
        //Legend l = mChart.getLegend();
        //l.setPosition(LegendPosition.RIGHT_OF_CHART);
        
        mChart.setData(generatePieData());
        mChart.setOnChartValueSelectedListener(this);
        if(isFirstLaunch) {
            mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            isFirstLaunch = false;
        }
        return v;
    }

    private SpannableString generateCenterText(String text) {

        SpannableString s;
        if (text == null) {
            s = new SpannableString("December 2015");
        }else{
            s = new SpannableString(text);
        }

        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        int index = e.getXIndex();
        FragmentExpenseList frag = new FragmentExpenseList();
        frag.setData(categories.get(index), fromDate, toDate);
        openFragment(frag, true);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onResume(){
        super.onResume();
        if(needsUpdatingChart) {
            mChart.notifyDataSetChanged();
            mChart.setData(generatePieData());
            mChart.animateY(1200, Easing.EasingOption.EaseInOutQuad);
            mChart.invalidate();
            needsUpdatingChart = false;
        }
    }

    private void openFragment(final Fragment fragment, boolean addToStack) {

        if (addToStack) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 1) {
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            }
            transaction.replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container,fragment)
                    .commit();
        }

    }

    protected PieData generatePieData() {

        int count = this.categories.size();

        if(count == 0 ){
           mChart.setCenterText("Add your first expense");
           mChart.setCenterTextSize(12f);
        }else{
            mChart.setCenterText(generateCenterText(null));
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

    public void setFragmentData(List<String> categories, List<Double> expenses, String fromDate, String toDate){
        this.categories = categories;
        this.expenses = expenses;
        this.fromDate = fromDate;
        this.toDate = toDate;

    }
}
