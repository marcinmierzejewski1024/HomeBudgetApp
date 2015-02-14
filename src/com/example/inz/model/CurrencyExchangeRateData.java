package com.example.inz.model;

import android.renderscript.Double2;
import android.util.Log;
import android.util.Pair;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by dom on 24/11/14.
 */
public class CurrencyExchangeRateData extends BaseData
{




    public class ExchangeRateNotFoundExcepction extends Exception{};


    public CurrencyExchangeRateData(MainData mainData)
    {
        super(mainData);
    }

    public List<CurrencyExchangeRate> getRatingsFrom(Date since, Currency from,Currency to, int maxNumberOfRecords)
    {
        try
        {
            List<CurrencyExchangeRate> resultList;
            final QueryBuilder<CurrencyExchangeRate, Long> qb = getDao().queryBuilder();
            if (since != null)
                resultList = qb.where().eq("from", from).and().eq("to", to).and().gt("exchangeDate", since).query();
            else
                resultList = qb.where().eq("from", from).and().eq("to", to).query();
            return resultList;
        }
        catch (Exception e)
        {
            Log.e("CurrenctExchangeRateDAta","GetRatingFrom",e);
            return null;
        }
    }

    public Pair<Float,Float> getMinAndMax(Currency from,Currency to,int percentOfMargin)
    {
        try
        {
            final QueryBuilder<CurrencyExchangeRate, Long> qb = getDao().queryBuilder();
            CurrencyExchangeRate min = qb.orderBy("rate",true).where().eq("from", from).and().eq("to", to).queryForFirst();
            CurrencyExchangeRate max = qb.orderBy("rate",false).where().eq("from", from).and().eq("to", to).queryForFirst();

            float minValue = (float) ((100 - percentOfMargin)/100.0 * min.getRate());
            float maxValue = (float) ((100 + percentOfMargin)/100.0 * max.getRate());

            Pair<Float,Float> result = new Pair<Float, Float>(minValue,maxValue);
            return result;
        }
        catch (Exception e)
        {
            Log.e("CurrenctExchangeRateDAta","GetMinMax",e);
            return null;
        }
    }

    private Dao<CurrencyExchangeRate, Long> getDao()
            throws SQLException
    {
        return mainData.get().getDao(CurrencyExchangeRate.class);
    }

    public double getLastRating(Currency from, Currency to) throws ExchangeRateNotFoundExcepction,SQLException
    {
        if(from==to)
            return 1.0;
        try
        {
            final QueryBuilder<CurrencyExchangeRate, Long> qb = getDao().queryBuilder();

            CurrencyExchangeRate result = qb.where().eq("from", from).and().eq("to", to).queryForFirst();
            if(result == null)
            {
                result = qb.where().eq("from", to).and().eq("to", from).queryForFirst();
                result.setRate(1.0/result.getRate());
            }

            result.setStoredInDb(true);
            return result.getRate();
        }
        catch (Exception e)
        {
            Log.e("CurrencyExchangeRateData", e.toString());
            throw new ExchangeRateNotFoundExcepction();
        }
    }
    public CurrencyExchangeRate storeRate(final CurrencyExchangeRate rate) throws SQLException
    {
        return TransactionManager.callInTransaction(mainData.get().getConnectionSource(), new Callable<CurrencyExchangeRate>()
        {
            @Override
            public CurrencyExchangeRate call() throws Exception
            {
                getDao().create(rate);
                rate.setStoredInDb(true);
                return rate;
            }
        });
    }

    public void storeRates(final List<CurrencyExchangeRate> rates) throws SQLException
    {
        for(CurrencyExchangeRate single : rates)
            storeRate(single);
    }



    public Date getLastUpdateDate() throws SQLException
    {
        final QueryBuilder<CurrencyExchangeRate, Long> qb = getDao().queryBuilder();

        CurrencyExchangeRate result = qb.orderBy("exchangeDate",false).queryForFirst();
        return result.getExchangeDate();
    }
}
