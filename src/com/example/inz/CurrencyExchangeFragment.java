package com.example.inz;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.example.inz.common.CommonFragment;
import com.example.inz.model.Currency;
import com.example.inz.model.CurrencyExchangeRate;
import com.example.inz.model.CurrencyExchangeRateData;
import com.example.inz.model.MainData;

import java.util.Date;
import java.util.List;

/**
 * Created by dom on 07/12/14.
 */
public class CurrencyExchangeFragment extends CommonFragment
{
    private static int MAX_NUMBER_OF_RECORDS_ON_GRAPH = 12;

    CurrencyExchangeRateData dao = new MainData().getExchangeRateData();
    Currency currency = Currency.USD;

    View rootView;
    LineGraph graph;
    private Spinner currencySpinner;


    private void drawGraph()
    {
        Line l = new Line();

        Pair<Float, Float> minMax = dao.getMinAndMax(Currency.getDefault(),currency,50);

        Date since = new Date(1414877852000l);
        Date now = new Date();

        List<CurrencyExchangeRate> rates = dao.getRatingsFrom(since,Currency.getDefault(),currency,MAX_NUMBER_OF_RECORDS_ON_GRAPH);

        if(rates != null)
        {
            int i = 0;
            for (CurrencyExchangeRate rate : rates)
            {
                LinePoint p = new LinePoint();
                p.setX(rate.getExchangeDate().getTime()/10000.0f);
                p.setY(rate.getRate());
                l.addPoint(p);

                Log.wtf("CurrencyExchangeFragment", " " + rate);
            }
        }
        else
        {
            Toast.makeText(getActivity(),R.string.no_rates_found,Toast.LENGTH_SHORT).show();
        }
        l.setColor(Color.parseColor("#FFBB33"));

        //if(graph.getLines()!=null && graph.getLines().size()!=0)
        //    graph.removeAllLines();

        graph.addLine(l);

        if(minMax != null)
            graph.setRangeY(minMax.first,minMax.second);

        graph.setRangeX(since.getTime()/10000.0f,now.getTime()/10000.0f);


        graph.setLineToFill(0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.currency_exchange_fragment, container, false);
        graph = (LineGraph) rootView.findViewById(R.id.graph);
        currencySpinner = (Spinner) rootView.findViewById(R.id.currencySpinner);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, Currency.values());
        currencySpinner.setAdapter(spinnerArrayAdapter);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                currency = Currency.values()[position];
                drawGraph();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        drawGraph();

        return rootView;
    }
}
