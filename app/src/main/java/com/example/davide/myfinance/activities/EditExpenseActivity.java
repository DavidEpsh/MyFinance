package com.example.davide.myfinance.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.content.CursorLoader;
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
import android.widget.Toast;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.models.Model;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditExpenseActivity extends AppCompatActivity {

    static final int DELETED_EXPENSE = 1;
    static final int SAVED_EXPENSE = 2;

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private ImageButton mPictureButton;
    private EditText mExpenseAmount;

    String imagePath;
    String category;
    Long timeStamp;
    String userSheetId;
    String dateSql;
    GregorianCalendar cal = new GregorianCalendar();
    String imageFileName;

    Long itemId;
    private FloatingActionsMenu fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_edit_expense);

        fab = (FloatingActionsMenu)findViewById(R.id.fab_menu);

        mNameOfExpense = (EditText) findViewById(R.id.edit_text_name_of_expense_edit_activity);
        mButtonExpenseDate = (Button) findViewById(R.id.expense_date_button_edit_activity);
        mPictureButton = (ImageButton) findViewById(R.id.pictureImageButton_edit_activity);
        mIsRepeatedExpense = (CheckBox)findViewById(R.id.checkbox_set_as_repeated_event_edit_activity);
        mExpenseAmount = (EditText)findViewById(R.id.edit_text_expense_amount_edit_activity);

        mNameOfExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.collapse();
            }
        });

        mExpenseAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.collapse();
            }
        });

        if(getIntent() != null){
            itemId = getIntent().getLongExtra(MainActivity.SHEET_ID, 0);
            Expense currExpense  = Model.instance().getExpense(itemId);

            timeStamp = currExpense.getTimeStamp();
            dateSql = currExpense.getDateSql();
            category = currExpense.getCategory();
            mNameOfExpense.setText(currExpense.getExpenseName());
            mIsRepeatedExpense.setChecked(currExpense.isRepeatingExpenseBool());
            mExpenseAmount.setText(Double.toString(currExpense.getExpenseAmount()));
            userSheetId = currExpense.getSheetId();

            Model.instance().loadImage(currExpense.getExpenseImage(), new Model.LoadImageListener() {
                @Override
                public void onResult(Bitmap imageBmp) {
                    mPictureButton.setImageBitmap(imageBmp);
                }
            });
        }

        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, MainActivity.REQUEST_IMAGE_CAPTURE);
                }
            }
        });


        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setCalendar();

        com.getbase.floatingactionbutton.FloatingActionButton fabSaveChanges = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_save);
        com.getbase.floatingactionbutton.FloatingActionButton fabDelete = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_delete);

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance().deleteExpense(timeStamp);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", DELETED_EXPENSE);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        fabSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mPictureButton.setImageBitmap(imageBitmap);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageFileName = "JPEG_" + timeStamp + ".jpeg";
            Model.instance().saveImage(imageBitmap, imageFileName);
        }
    }



    private void saveExpense() {

        Expense mExpense = new Expense(mNameOfExpense.getText().toString(), mIsRepeatedExpense.isChecked(), dateSql, imagePath, Double.valueOf(mExpenseAmount.getText().toString()), category, timeStamp, userSheetId);
        Model.instance().updateOrAddExpense(mExpense, false);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", SAVED_EXPENSE);
        setResult(this.RESULT_OK, returnIntent);
        finish();

        Toast.makeText(this, "Expense Updated", Toast.LENGTH_SHORT).show();

    }

    private void setCalendar(){

        cal = new GregorianCalendar();

        try {
            cal.setTimeInMillis(MainActivity.sdf.parse(dateSql).getTime());
            Log.i("MyLog", "Converting from u.Date to s.Date Success");
        } catch (ParseException e) {
            Log.i("MyLog","Converting from u.Date to s.Date Error");
        }

        mButtonExpenseDate.setText(MainActivity.sdfShort.format(cal.getTime()));

        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mButtonExpenseDate.setText(dayOfMonth + "/" + (monthOfYear+ 1) + "/" + year);
                cal.set(year, monthOfYear, dayOfMonth);
                dateSql = MainActivity.sdf.format(cal.getTime());

            }

        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));



        mButtonExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.collapse();
                datePicker.show();
            }
        });
    }

}
