package com.example.davide.myfinance.fragments;


import android.app.DatePickerDialog;
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
import android.widget.Toast;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.models.Expense;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment {

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private Button mSaveButton;
    private ImageButton mPictureButton;
    private EditText mExpenseAmount;
    private int[] mExpenseDate = new int[3];
    int mYear;
    int mMonth;
    int mDay;

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
        mButtonExpenseDate = (Button) mRootView.findViewById(R.id.expense_date_button);
        mSaveButton = (Button) mRootView.findViewById(R.id.save_button);
        mPictureButton = (ImageButton) mRootView.findViewById(R.id.pictureImageButton);
        mIsRepeatedExpense = (CheckBox)mRootView.findViewById(R.id.checkbox_set_as_repeated_event);
        mExpenseAmount = (EditText)mRootView.findViewById(R.id.edit_text_expense_amount);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mButtonExpenseDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);

        final DatePickerDialog datePicker = new DatePickerDialog(mRootView.getContext(), new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int newYear = year;
                int newMonth = monthOfYear;
                int newDay = dayOfMonth;

                mButtonExpenseDate.setText(newDay + "/" + (newMonth + 1) + "/" + newYear);
                mExpenseDate[0] = dayOfMonth;
                mExpenseDate[1] = monthOfYear;
                mExpenseDate[2] = year;
            }

        },mYear,mMonth,mDay);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (requiredFieldCompleted()) {
                    saveExpense();
                }
            }
        });

        mButtonExpenseDate.setOnClickListener(new View.OnClickListener() {
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
        mStudent.setRepeatingEvent(mIsRepeatedExpense.isChecked());
        mStudent.setExpenseDate(mExpenseDate);

        //todo Save to database

        resetFields();

        Toast.makeText(getActivity(), mStudent.getExpenseName() + " saved", Toast.LENGTH_SHORT).show();
    }

    private void resetFields() {

        mNameOfExpense.setText("");
        mButtonExpenseDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        mExpenseAmount.setText("");

    }

}
