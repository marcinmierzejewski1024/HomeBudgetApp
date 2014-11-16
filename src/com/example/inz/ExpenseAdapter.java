package com.example.inz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.inz.model.CashAmmount;
import com.example.inz.model.Category;
import com.example.inz.model.Expense;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    private final Context context;
    private Expense[] values;

    public ExpenseAdapter(Context context, Expense[] values)
    {
        super(context, R.layout.expense_item, values);
        this.context = context;
        this.values = values;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.expense_item, parent, false);

        rowView.setTag(values[position].getExpenseId());

        TextView name = (TextView)rowView.findViewById(R.id.name);
        TextView date = (TextView)rowView.findViewById(R.id.date);
        TextView cash = (TextView)rowView.findViewById(R.id.cash);

        try
        {

            name.setText(values[position].getName());
            date.setText(values[position].getDate().toString());
            cash.setText(values[position].getCash().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return rowView;
    }
}