package com.example.davide.myfinance.fragments;
import android.content.Intent;
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
import com.example.davide.myfinance.activities.AddExpenseActivity;
import com.example.davide.myfinance.activities.MainActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


public class HomeFragment extends SimpleFragment implements OnChartValueSelectedListener {

    public static boolean isFirstLaunch = true;
    public static boolean needsUpdatingChart = false;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    private PieChart mChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        
        mChart = (PieChart) v.findViewById(R.id.pieChart_home_fragment);
        mChart.setDescription("");
        
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        
        mChart.setCenterTextTypeface(tf);
        mChart.setCenterText(generateCenterText());
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

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("December 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        int index = e.getXIndex();
        FragmentExpenseList frag = new FragmentExpenseList();
        frag.setCustomList(ExpenseDB.getInstance().getCategory(index).getExpensesPerCategory());
        openFragment(frag, true);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onResume(){
        super.onResume();
        if(needsUpdatingChart) {
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
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
}
