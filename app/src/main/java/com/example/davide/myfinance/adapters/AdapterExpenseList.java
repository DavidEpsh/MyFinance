package com.example.davide.myfinance.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.MainActivity;
import com.example.davide.myfinance.models.Expense;
import com.example.davide.myfinance.models.Model;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AdapterExpenseList extends BaseAdapter{

    private List<Expense> mExpenseList;
    private Context _context;
    private View rowView;

    ImageView expenseImage;
    TextView expenseName, expenseDate;
    CheckBox repeatingEvent;
    TextView expenseAmount;

    public AdapterExpenseList(List<Expense> expenseList, Context context){
        mExpenseList = expenseList;
        _context = context;
    }

    @Override
    public int getCount() {
        return mExpenseList.size();
    }

    @Override
    public Object getItem(int position) {
        return Model.instance().getExpense(mExpenseList.get(position).getTimeStamp());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_list_row, parent, false);
        }

        expenseImage = (ImageView) convertView.findViewById(R.id.image_view_expense_image);
        expenseName = (TextView)convertView.findViewById(R.id.text_view_expense_name_row);
        expenseDate = (TextView)convertView.findViewById(R.id.text_view_expense_date_row);
        expenseAmount = (TextView)convertView.findViewById(R.id.text_expense_amount_row);
        repeatingEvent = (CheckBox)convertView.findViewById(R.id.checkbox_row);

        Expense expense = mExpenseList.get(position);

        expenseName.setText(expense.getExpenseName());
        expenseAmount.setText(Double.toString(expense.getExpenseAmount()));
        repeatingEvent.setChecked(expense.isRepeatingExpenseBool());


        GregorianCalendar cal = new GregorianCalendar();
        java.util.Date date = new Date(cal.getTimeInMillis());

        try {
            date = MainActivity.sdf.parse(expense.getDateSql());
            Log.i("MyLog","Converting from u.Date to s.Date Success");
        } catch (ParseException e) {
            Log.i("MyLog","Converting from u.Date to s.Date Error");
        }
        cal.setTimeInMillis(date.getTime());


//        String tempText = Integer.toString(temp[0]) + "/" + Integer.toString(temp[1] + 1) + "/" + Integer.toString(temp[2]);
        expenseDate.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR));

        if(expense.getExpenseImage() == null) {
            Picasso.with(_context).load(expense.getTempExpenseImage()).into(expenseImage);

        }else{
            final ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.rowImageProgressBar);
            progress.setVisibility(View.VISIBLE);
            Model.instance().loadImage(expense.getExpenseImage(),new Model.LoadImageListener() {
                @Override
                public void onResult(Bitmap imageBmp) {
                    if (imageBmp != null) {
                        expenseImage.setImageBitmap(imageBmp);
                    }
                    progress.setVisibility(View.GONE);
                }
            });
        }
        return convertView;
    }
}

