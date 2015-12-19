package com.example.davide.myfinance.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.davide.myfinance.ExpenseDB;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.MainActivity;
import com.example.davide.myfinance.activities.ViewExpenseActivity;
import com.example.davide.myfinance.adapters.AdapterExpenseList;
import com.example.davide.myfinance.models.Expense;

import java.util.List;


public class FragmentExpenseList extends Fragment {

    private View mRootView;
    private ListView studentList;
    private AdapterExpenseList mAdapter;
    private List<Expense> mStudentListDB;

    public FragmentExpenseList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_expense_list, container, false);
        studentList = (ListView) mRootView.findViewById(R.id.expensetListView);

        if (mStudentListDB == null ) {
            mStudentListDB = ExpenseDB.getInstance().getList();
        }

        mAdapter = new AdapterExpenseList(mStudentListDB, getActivity());
        studentList.setAdapter(mAdapter);

        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "row " + position + " selected");
                Intent intent = new Intent(mRootView.getContext(), ViewExpenseActivity.class);
                intent.putExtra(MainActivity.ITEM_IN_LIST, position);
                startActivityForResult(intent, MainActivity.RESULT_FINISHED_EDITING);
            }
        });

        return mRootView;
    }

    public void setCustomList(List expenses){
        this.mStudentListDB = expenses;
    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

}