package com.example.davide.myfinance.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davide.myfinance.R;
import com.example.davide.myfinance.activities.EditExpenseActivity;
import com.example.davide.myfinance.activities.ViewExpenseActivity;
import com.example.davide.myfinance.models.Expense;
import com.squareup.picasso.Picasso;

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

        Picasso.with(_context).load(selectedExpense.getExpenseImage()).into(holder.expemseImage);
    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView expemseImage;
        TextView expenseName, expenseDate;
        CheckBox repeatingEvent;
        TextView expenseAmount;
        View parent;

        public ViewHolder(View itemView) {
            super(itemView);
            expemseImage = (ImageView) itemView.findViewById(R.id.image_view_expense_image);
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

}
