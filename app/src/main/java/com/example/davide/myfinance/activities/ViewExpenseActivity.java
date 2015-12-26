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
import android.util.Log;
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

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ViewExpenseActivity extends AppCompatActivity {

    static final int DELETED_EXPENSE = 1;

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private ImageButton mPictureButton;
    private EditText mExpenseAmount;
    int itemPosition;
    String dateSql;

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
            itemPosition = getIntent().getIntExtra(MainActivity.ITEM_IN_LIST, 0);
            Expense currExpense  = ExpenseDB.getInstance().getExpense(itemPosition);
            dateSql = currExpense.getDateSql();
            mNameOfExpense.setText(currExpense.getExpenseName());
            mIsRepeatedExpense.setChecked(currExpense.isRepeatingExpenseBool());
            mExpenseAmount.setText(Double.toString(currExpense.getExpenseAmount()));

            if(currExpense.getExpenseImage() != null){
                AddExpenseActivity.setPic(mPictureButton, currExpense.getExpenseImage());
            }
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

        GregorianCalendar cal = new GregorianCalendar();
        java.util.Date date = new Date(cal.getTimeInMillis());
        try {
            date = MainActivity.sdf.parse(dateSql);
            Log.i("MyLog", "Converting from u.Date to s.Date Success");
        } catch (ParseException e) {
            Log.i("MyLog","Converting from u.Date to s.Date Error");
        }
        cal.setTime(date);

        mButtonExpenseDate.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR));

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
                //User pressed back button
            }
        }
    }

}
