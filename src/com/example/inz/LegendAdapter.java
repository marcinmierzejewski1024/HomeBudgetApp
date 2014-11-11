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

import java.util.List;

public class LegendAdapter extends ArrayAdapter<Pair<Category,CashAmmount>> {
    private final Context context;
    private Pair<Category,CashAmmount>[] values;

    public LegendAdapter(Context context, List<Pair<Category,CashAmmount>> values)
    {
        super(context, R.layout.legend_item, values);
        this.context = context;
        this.values = values.toArray(this.values);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.legend_item, parent, false);

        TextView categoryName = (TextView) rowView.findViewById(R.id.categoryName);
        View colorView = rowView.findViewById(R.id.color);

        categoryName.setText(values[position].first.getName());
        colorView.setBackground(new ColorDrawable(Color.parseColor(values[position].first.getHexColor())));


        return rowView;
    }
}