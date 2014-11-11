package com.example.inz;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.example.inz.model.CashAmmount;
import com.example.inz.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dom on 11/11/14.
 */
public class ExpenseFragment extends CommonFragment
{
    private static final int NAMED_CATEGORIES = 5;

    View rootView;
    List<Pair<Category,CashAmmount>> data = new ArrayList<Pair<Category, CashAmmount>>();

    private AdapterView.OnItemSelectedListener rangeListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            //TODO:zmiana danych

            drawChart();
            drawChartLegend();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.expense_fragment, container, false);

        Spinner range= (Spinner) rootView.findViewById(R.id.spinnerRange);
        range.setOnItemSelectedListener(rangeListener);

        drawChart();
        drawChartLegend();

        return rootView;
    }

    private void drawChart()
    {
        PieGraph pg = (PieGraph)rootView.findViewById(R.id.graph);

        for(Pair<Category,CashAmmount> singleItem:data)
        {
            PieSlice slice = new PieSlice();
            slice.setColor(Color.parseColor(singleItem.first.getHexColor()));
            slice.setGoalValue(singleItem.second.getPennies());
            slice.setValue(1);
            slice.setTitle(singleItem.first.getName());
            pg.addSlice(slice);
        }

        pg.setDuration(1000);//default if unspecified is 300 ms
        pg.setInterpolator(new AccelerateDecelerateInterpolator());
        pg.animateToGoalValues();
    }

    private void drawChartLegend()
    {
        ListView legend = (ListView) rootView.findViewById(R.id.legendListView);
        LegendAdapter adapter = new LegendAdapter(getActivity(), data);
        legend.setAdapter(adapter);
    }
}
