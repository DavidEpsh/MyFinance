package com.example.davide.myfinance.activities;

import android.app.DatePickerDialog;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.davide.myfinance.ExpenseDB;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.utils.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AddExpenseActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_ID = 5;
    private Uri outputFileUri;

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private ImageButton mPictureButton;
    private ImageView mExpenseImage;
    private EditText mExpenseAmount;
    private int[] mExpenseDate = new int[3];
    int mYear;
    int mMonth;
    int mDay;
    Bitmap bitmap;
    File photoFile;
    Uri mCapturedImageURI;


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

        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                galleryIntent.putExtra("isCamera", false);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                cameraIntent.putExtra("isCamera",true);

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

    String mCurrentPhotoPath;

    private File createImageFileAndSave(Intent data) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName =timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean isCamera = data.getBooleanExtra("isCamera", false);
        if (resultCode == RESULT_OK) {

            if(isCamera) {
                try {
                    photoFile = createImageFileAndSave(data);
                    Uri savedImage = Uri.fromFile(photoFile);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
            }

            if (data.getData() != null) {
                try {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }

                    InputStream stream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                    mPictureButton.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                bitmap = (Bitmap) data.getExtras().get("data");
                mPictureButton.setImageBitmap(bitmap);
            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
