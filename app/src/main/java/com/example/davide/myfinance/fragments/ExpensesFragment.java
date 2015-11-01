package com.example.davide.myfinance.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davide.myfinance.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesFragment extends Fragment {
    private View mRootView;

    public ExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.expense_list_fragment, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (!fab.isShown())
            fab.show();
    }
}
