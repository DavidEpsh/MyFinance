package com.example.davide.myfinance.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.davide.myfinance.ExpenseDB;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.adapters.ExpenseListAdapter;
import com.example.davide.myfinance.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListFragment extends Fragment {

    private View mRootView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Expense> mExpenseList;



    public ExpenseListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_expense_list, container, false);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.attendants_recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mExpenseList = ExpenseDB.getInstance().getList();

        mAdapter = new ExpenseListAdapter(mExpenseList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return mRootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

}