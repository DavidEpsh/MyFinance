package com.example.davide.myfinance.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.models.Model;

import java.text.ParseException;
import java.util.GregorianCalendar;

public class ViewExpenseActivity extends AppCompatActivity {

    static final int DELETED_EXPENSE = 1;

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private ImageButton mPictureButton;
    private EditText mExpenseAmount;
    private TextView mCategories;
    String itemId;
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
        mCategories = (TextView)findViewById(R.id.text_selected_category_picked);

        if(getIntent() != null) {
            itemId = getIntent().getStringExtra(MainActivity.EXPENSE_ID);
            Expense currExpense = Model.instance().getExpense(itemId);
            dateSql = currExpense.getDateSql();
            mNameOfExpense.setText(currExpense.getExpenseName());
            mIsRepeatedExpense.setChecked(currExpense.isRepeatingExpenseBool());
            mExpenseAmount.setText(Double.toString(currExpense.getExpenseAmount()));
            mCategories.setText(currExpense.getCategory());

            if (currExpense.getExpenseImage() != null) {
                Model.instance().loadImage(currExpense.getExpenseImage(), new Model.LoadImageListener() {
                    @Override
                    public void onResult(Bitmap imageBmp) {
                        mPictureButton.setImageBitmap(imageBmp);
                    }
                });
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewExpenseActivity.this, EditExpenseActivity.class);
                intent.putExtra(MainActivity.EXPENSE_ID, itemId);
                startActivityForResult(intent,MainActivity.RESULT_FINISHED_EDITING);
                }
        });

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTimeInMillis(MainActivity.sdf.parse(dateSql).getTime());
            Log.i("MyLog", "Converting from u.Date to s.Date Success");
        } catch (ParseException e) {
            Log.i("MyLog","Converting from u.Date to s.Date Error");
        }
        //cal.setTime(date);

        mButtonExpenseDate.setText(MainActivity.sdfShort.format(cal.getTime()));

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

        if (resultCode == EditExpenseActivity.RESULT_OK) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", MainActivity.RESULT_OK);
            setResult(this.RESULT_OK, returnIntent);
            finish();
        }
    }
}
