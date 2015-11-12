package com.example.davide.myfinance.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.davide.myfinance.ExpenseDB;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.models.Expense;

import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private ImageButton mPictureButton;
    private EditText mExpenseAmount;
    private int[] mExpenseDate = new int[3];
    int mYear;
    int mMonth;
    int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mNameOfExpense = (EditText) findViewById(R.id.edit_text_name_of_expense);
        mButtonExpenseDate = (Button) findViewById(R.id.expense_date_button);
        mPictureButton = (ImageButton) findViewById(R.id.pictureImageButton);
        mIsRepeatedExpense = (CheckBox)findViewById(R.id.checkbox_set_as_repeated_event);
        mExpenseAmount = (EditText)findViewById(R.id.edit_text_expense_amount);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mButtonExpenseDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);

        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
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

        fab.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return super.onOptionsItemSelected(item);

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
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

        Expense mExpense = new Expense(mNameOfExpense.getText().toString(),mIsRepeatedExpense.isChecked(),mExpenseDate,R.mipmap.ic_launcher);
        ExpenseDB.getInstance().addExpense(mExpense);
        finish();

        Toast.makeText(this, mExpense.getExpenseName() + " saved", Toast.LENGTH_SHORT).show();
    }

    private void resetFields() {

        mNameOfExpense.setText("");
        mButtonExpenseDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        mExpenseAmount.setText("");

    }
}
