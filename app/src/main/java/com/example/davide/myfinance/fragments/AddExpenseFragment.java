package com.example.davide.myfinance.fragments;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.davide.myfinance.MainActivity;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.models.Expense;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment {

    private EditText mNameOfExpense;
    private Button mExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private Button mSaveButton;
    private ImageButton mPictureButton;

    private View mRootView;

    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_add_expense, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        mNameOfExpense = (EditText) mRootView.findViewById(R.id.edit_text_name_of_expense);
        mExpenseDate = (Button) mRootView.findViewById(R.id.event_date_button);
        mSaveButton = (Button) mRootView.findViewById(R.id.save_button);
        mPictureButton = (ImageButton) mRootView.findViewById(R.id.pictureImageButton);
        mIsRepeatedExpense = (CheckBox)mRootView.findViewById(R.id.checkbox_set_as_repeated_event);

        final DatePickerDialog datePicker = new DatePickerDialog(mRootView.getContext(), new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }

        },2015,1,11);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (requiredFieldCompleted()) {
                    saveExpense();
                }
            }
        });

        mExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });
    }

    private boolean requiredFieldCompleted() {
        boolean isFilled = false;

        if (mNameOfExpense.getText() != null && !mNameOfExpense.getText().toString().isEmpty()) {
            isFilled = true;
        } else {
            mNameOfExpense.setError(getString(R.string.required_fields_empty));
        }


        return isFilled;
    }

    private void saveExpense() {

        Expense mStudent = new Expense();
        mStudent.setExpenseName(mNameOfExpense.getText().toString());

        //todo Save to database

        resetFields();

        Toast.makeText(getActivity(), mStudent.getExpenseName() + " saved", Toast.LENGTH_SHORT).show();
    }

    private void resetFields() {

        mNameOfExpense.setText("");
        mExpenseDate.setText("");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getActivity().getFragmentManager(),"date_picker");
    }

}
