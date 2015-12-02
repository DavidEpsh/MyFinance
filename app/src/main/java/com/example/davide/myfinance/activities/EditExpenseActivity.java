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
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import java.util.Calendar;

public class EditExpenseActivity extends AppCompatActivity {

    static final int DELETED_EXPENSE = 1;
    static final int SAVED_EXPENSE = 2;

    private EditText mNameOfExpense;
    private Button mButtonExpenseDate;
    private CheckBox mIsRepeatedExpense;
    private ImageButton mPictureButton;
    private EditText mExpenseAmount;
    private int[] mExpenseDate = new int[3];
    int mYear;
    int mMonth;
    int mDay;
    private String imagePath;

    int itemPosition;
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
            itemPosition = getIntent().getIntExtra("item", 0);
            Expense currExpense  = ExpenseDB.getInstance().getExpense(itemPosition);

            mExpenseDate = currExpense.getExpenseDate();
            mNameOfExpense.setText(currExpense.getExpenseName());
            mIsRepeatedExpense.setChecked(currExpense.isRepeatingExpense());
            mExpenseAmount.setText(Double.toString(currExpense.getExpenseAmount()));

            if(currExpense.getExpenseImage() != null){
                AddExpenseActivity.setPic(mPictureButton, currExpense.getExpenseImage());
            }
        }

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
                startActivityForResult(chooser,5);

                startActivityForResult(chooser, 5);
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
                ExpenseDB.getInstance().removeExpense(itemPosition);

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
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            imagePath =  getRealPathFromURI(selectedImage);

            setPic(mPictureButton, imagePath);

            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void saveExpense() {

        Expense mExpense;
        if(imagePath == null) {
            mExpense = new Expense(mNameOfExpense.getText().toString(), mIsRepeatedExpense.isChecked(), mExpenseDate, R.mipmap.ic_launcher, Double.valueOf(mExpenseAmount.getText().toString()));
        }else {
            mExpense = new Expense(mNameOfExpense.getText().toString(), mIsRepeatedExpense.isChecked(), mExpenseDate, imagePath, Double.valueOf(mExpenseAmount.getText().toString()));
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", SAVED_EXPENSE);
        setResult(this.RESULT_OK, returnIntent);
        finish();

        Toast.makeText(this, "Expense Updated", Toast.LENGTH_SHORT).show();

    }

    private void setCalendar(){

        final Calendar c = Calendar.getInstance();
        mYear = mExpenseDate[2];
        mMonth = mExpenseDate[1];
        mDay = mExpenseDate[0];

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

        mButtonExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.collapse();
                datePicker.show();
            }
        });
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


}
