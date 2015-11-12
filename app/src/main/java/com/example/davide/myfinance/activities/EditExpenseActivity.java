package com.example.davide.myfinance.activities;

import android.app.DatePickerDialog;
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

public class EditExpenseActivity extends AppCompatActivity {

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private ImageButton mPictureButton;
    private EditText mExpenseAmount;
    private int[] mExpenseDate = new int[3];
    int mYear;
    int mMonth;
    int mDay;

    int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameOfExpense = (EditText) findViewById(R.id.edit_text_name_of_expense_edit_activity);
        mButtonExpenseDate = (Button) findViewById(R.id.expense_date_button_edit_activity);
        mPictureButton = (ImageButton) findViewById(R.id.pictureImageButton_edit_activity);
        mIsRepeatedExpense = (CheckBox)findViewById(R.id.checkbox_set_as_repeated_event_edit_activity);
        mExpenseAmount = (EditText)findViewById(R.id.edit_text_expense_amount_edit_activity);

        if(getIntent() != null){
            itemPosition = getIntent().getIntExtra("item", 0);
            Expense currExpense  = ExpenseDB.getInstance().getExpense(itemPosition);

            mExpenseDate = currExpense.getExpenseDate();
            mNameOfExpense.setText(currExpense.getExpenseName());
            mIsRepeatedExpense.setChecked(currExpense.isRepeatingExpense());

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);



        final Calendar c = Calendar.getInstance();
        mYear = mExpenseDate[2];
        mMonth = mExpenseDate[1];
        mDay = mExpenseDate[0];

        mButtonExpenseDate.setText(mDay + "/" + (mMonth+1) + "/" + mYear);

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
        ExpenseDB.getInstance().editExpense(mExpense, itemPosition);
        finish();
        Toast.makeText(this, mExpense.getExpenseName() + " saved", Toast.LENGTH_SHORT).show();

    }

}
