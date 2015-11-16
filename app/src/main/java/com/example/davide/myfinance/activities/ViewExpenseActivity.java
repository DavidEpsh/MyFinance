package com.example.davide.myfinance.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.davide.myfinance.ExpenseDB;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.models.Expense;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.Calendar;

public class ViewExpenseActivity extends AppCompatActivity {

    static final int DELETED_EXPENSE = 1;

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
        setContentView(R.layout.activity_view_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_view_expense);

        mNameOfExpense = (EditText) findViewById(R.id.edit_text_name_of_expense_view_expense);
        mButtonExpenseDate = (Button) findViewById(R.id.expense_date_button_view_expense);
        mPictureButton = (ImageButton) findViewById(R.id.pictureImageButton_view_expense);
        mIsRepeatedExpense = (CheckBox)findViewById(R.id.checkbox_set_as_repeated_event_view_expense);
        mExpenseAmount = (EditText)findViewById(R.id.edit_text_expense_amount_view_expense);

        if(getIntent() != null){
            itemPosition = getIntent().getIntExtra("item", 0);
            Expense currExpense  = ExpenseDB.getInstance().getExpense(itemPosition);

            mExpenseDate = currExpense.getExpenseDate();
            mNameOfExpense.setText(currExpense.getExpenseName());
            mIsRepeatedExpense.setChecked(currExpense.isRepeatingExpense());
            mExpenseAmount.setText(Double.toString(currExpense.getExpenseAmount()));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewExpenseActivity.this, EditExpenseActivity.class);
                intent.putExtra("item", itemPosition);
                startActivityForResult(intent,1);
                }
        });

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        final Calendar c = Calendar.getInstance();
        mYear = mExpenseDate[2];
        mMonth = mExpenseDate[1];
        mDay = mExpenseDate[0];

        mButtonExpenseDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);

//        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                int newYear = year;
//                int newMonth = monthOfYear;
//                int newDay = dayOfMonth;
//
//                mButtonExpenseDate.setText(newDay + "/" + (newMonth + 1) + "/" + newYear);
//                mExpenseDate[0] = dayOfMonth;
//                mExpenseDate[1] = monthOfYear;
//                mExpenseDate[2] = year;
//            }
//
//        },mYear,mMonth,mDay);
//
//        mButtonExpenseDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePicker.show();
//            }
//        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int result;

        if (requestCode == 1) {
            if(resultCode == EditExpenseActivity.RESULT_OK){
                result = data.getIntExtra("result", -2);
                if(result == EditExpenseActivity.DELETED_EXPENSE){
                    finish();
                }
                if (result == EditExpenseActivity.SAVED_EXPENSE){
                    finish();
                }
            }
            if (resultCode == EditExpenseActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

}
