package com.example.davide.myfinance.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.ViewExpenseActivity;
import com.example.davide.myfinance.models.Expense;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ViewHolder>{
    private List<Expense> mExpenseList;
    private Context _context;
    private View rowView;

    //inflate the custom row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_list_row, parent, false);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int)v.getTag();
                Log.i("dfs",Integer.toString(pos));

                Intent intent = new Intent(_context, ViewExpenseActivity.class);
                intent.putExtra("item", pos);
                _context.startActivity(intent);
            }
        });

        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Expense selectedExpense = mExpenseList.get(position);

        holder.expenseName.setText(selectedExpense.getExpenseName());
        holder.repeatingEvent.setChecked(selectedExpense.isRepeatingExpense());
        holder.parent.setTag(position);
        holder.expenseAmount.setText(Double.toString(selectedExpense.getExpenseAmount()));

        int[] temp = selectedExpense.getExpenseDate();
        String tempText = Integer.toString(temp[0]) + "/" + Integer.toString(temp[1] + 1) + "/" + Integer.toString(temp[2]);
        holder.expenseDate.setText(tempText);

        if(selectedExpense.getExpenseImage() == null) {
            Picasso.with(_context).load(selectedExpense.getTempExpenseImage()).into(holder.expenseImage);
        }else{

            setPic(holder, selectedExpense.getExpenseImage());
            //Picasso.with(_context).load(new File(selectedExpense.getExpenseImage())).into(holder.expenseImage);
        }
    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView expenseImage;
        TextView expenseName, expenseDate;
        CheckBox repeatingEvent;
        TextView expenseAmount;
        View parent;

        public ViewHolder(View itemView) {
            super(itemView);
            expenseImage = (ImageView) itemView.findViewById(R.id.image_view_expense_image);
            expenseName = (TextView)itemView.findViewById(R.id.text_view_expense_name_row);
            expenseDate = (TextView)itemView.findViewById(R.id.text_view_expense_date_row);
            expenseAmount = (TextView)itemView.findViewById(R.id.text_expense_amount_row);
            repeatingEvent = (CheckBox)itemView.findViewById(R.id.checkbox_row);
            parent = itemView.findViewById(R.id.item_parent_row);
        }
    }

    public ExpenseListAdapter(List<Expense> expenseList, Context context){
        mExpenseList = expenseList;
        _context = context;
    }

    private void setPic(ViewHolder holder, String photoPath) {
        // Get the dimensions of the View
        int targetW = holder.expenseImage.getMaxWidth();
        int targetH = holder.expenseImage.getMaxHeight();

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
        holder.expenseImage.setImageBitmap(bitmap);
    }
}
