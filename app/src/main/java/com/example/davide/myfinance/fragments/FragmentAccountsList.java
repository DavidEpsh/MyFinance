package com.example.davide.myfinance.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.MainActivity;
import com.example.davide.myfinance.activities.ViewExpenseActivity;
import com.example.davide.myfinance.adapters.AdapterExpenseList;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.models.Model;

import java.util.List;


public class FragmentAccountsList extends Fragment {

    private View mRootView;
    private ListView studentList;
    private AdapterExpenseList mAdapter;
    private List<Expense> mStudentListDB;
    String fromDate, toDate, category;

    public FragmentAccountsList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_expense_list, container, false);
        studentList = (ListView) mRootView.findViewById(R.id.expensetListView);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if(!fab.isShown()) {
            fab.show();
        }

        mStudentListDB = Model.instance().getExpensesByCategory(null, fromDate, toDate);
        mAdapter = new AdapterExpenseList(mStudentListDB, getActivity());
        studentList.setAdapter(mAdapter);

        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "row " + position + " selected");
                Intent intent = new Intent(mRootView.getContext(), ViewExpenseActivity.class);
                intent.putExtra(MainActivity.ITEM_ID, mStudentListDB.get(position).getTimeStamp());
                startActivityForResult(intent, MainActivity.RESULT_FINISHED_EDITING);
            }
        });

        return mRootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        mStudentListDB = Model.instance().getExpensesByCategory(category, fromDate, toDate);
        mAdapter.notifyDataSetInvalidated();
        mAdapter = new AdapterExpenseList(mStudentListDB, getActivity());
        studentList.setAdapter(mAdapter);

    }

    public void setData(String category, String fromDate, String toDate){
        this.category = category;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

}