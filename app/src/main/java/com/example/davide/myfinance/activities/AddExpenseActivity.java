package com.example.davide.myfinance.activities;

import android.app.DatePickerDialog;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.davide.myfinance.ExpenseDB;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.fragments.FragmentHome;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.models.Model;
import com.parse.ParseCloud;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class AddExpenseActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_ID = 5;
    private Uri outputFileUri;

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private ImageButton mPictureButton;
    private ImageView mExpenseImage;
    private EditText mExpenseAmount;
    private String imagePath;
    private Spinner spinnerCategories;

    GregorianCalendar cal;
    String dateSql;

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
        mExpenseImage = (ImageView)findViewById(R.id.imageViewAddExpense);
        spinnerCategories = (Spinner)findViewById(R.id.spinner_category_add_expense);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        Intent intent = getIntent();
        if (intent != null){
            String name = intent.getStringExtra("name");
            mNameOfExpense.setText(name);
        }

        setCalender();
        initializeSpinner();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (requiredFieldCompleted()) {
                    saveExpense();
                }
            }
        });

        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Intent chooser = new Intent(Intent.ACTION_CHOOSER);
                chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
                chooser.putExtra(Intent.EXTRA_TITLE, R.string.pick_image_intent_text);
                Intent[] intentArray =  {cameraIntent};
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooser, PICK_IMAGE_ID);
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

    public void initializeSpinner(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ExpenseDB.getInstance().getCategoryNames());
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(spinnerAdapter);
        //spinnerCategories.setOnItemSelectedListener(this);
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
        Expense mExpense;

        mExpense = new Expense(mNameOfExpense.getText().toString(),
                mIsRepeatedExpense.isChecked(),
                dateSql,
                imagePath,
                Double.valueOf(mExpenseAmount.getText().toString()),
                spinnerCategories.getSelectedItem().toString(),
                GregorianCalendar.getInstance().getTimeInMillis());

        FragmentHome.needsUpdatingChart = true; //Means That the user saved a new expense and the chart should be updated
        Model.instance().addExpense(mExpense);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", MainActivity.RESULT_ADD_EXPENSE);
        setResult(this.RESULT_OK, returnIntent);
        finish();

        Toast.makeText(this, mExpense.getExpenseName() + " saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mPictureButton.setImageBitmap(imageBitmap);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imagePath = "JPEG_" + timeStamp + ".jpeg";

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static void setPic(ImageButton imageButton, String photoPath) {
        // Get the dimensions of the View
        int targetW = imageButton.getMaxWidth();
        int targetH = imageButton.getMaxHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        imageButton.setImageBitmap(bitmap);
    }

    private void setCalender(){
        cal = new GregorianCalendar();
        dateSql = MainActivity.sdf.format(cal.getTime());
        mButtonExpenseDate.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR));

        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mButtonExpenseDate.setText(dayOfMonth + "/" + (monthOfYear+ 1) + "/" + year);
                cal.set(year, monthOfYear,dayOfMonth);

            }

        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));

        dateSql = MainActivity.sdf.format(cal.getTime());

        mButtonExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });
    }

}
