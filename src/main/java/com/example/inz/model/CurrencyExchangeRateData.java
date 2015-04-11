package com.example.inz.model;

import android.renderscript.Double2;
import android.util.Log;
import android.util.Pair;
import com.example.inz.MainApp;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by dom on 24/11/14.
 */
public class CurrencyExchangeRateData extends BaseData
{

    private Comparator<CurrencyExchangeRate> currencyDateComparator = new Comparator<CurrencyExchangeRate>()
    {
        @Override
        public int compare(CurrencyExchangeRate lhs, CurrencyExchangeRate rhs)
        {

            int i1 = (int) (lhs.getExchangeDate().getTime()/10000);
            int i2 = (int) (rhs.getExchangeDate().getTime()/10000);

            if ( i1 > i2 ) {
                return 1;
            } else if ( i1 < i2 ) {
                return -1;
            } else {
                return 0;
            }
        }
    };


    public class ExchangeRateNotFoundExcepction extends Exception{};


    public CurrencyExchangeRateData(MainData mainData)
    {
        super(mainData);
    }


    public HashMap<Integer, CurrencyExchangeRate> getRatingsFromLastWeek(Currency from, Currency to)
    {

        Calendar monday = Calendar.getInstance();
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);


        try
        {
            final QueryBuilder<CurrencyExchangeRate, Long> qb = getDao().queryBuilder();

            HashMap<Integer, CurrencyExchangeRate> result = new HashMap<Integer, CurrencyExchangeRate>();
            List<CurrencyExchangeRate> resultList,reversedResultList;
            resultList = qb.orderBy("exchangeDate",true).where().eq("from", from).and().eq("to", to).and().gt("exchangeDate", monday.getTime()).query();
            reversedResultList = qb.orderBy("exchangeDate",true).where().eq("from", to).and().eq("to", from).and().gt("exchangeDate", monday.getTime()).query();


            for(CurrencyExchangeRate reversedRate:reversedResultList)
            {
                from = reversedRate.getFrom();
                reversedRate.setFrom(reversedRate.getTo());
                reversedRate.setTo(from);
                reversedRate.setRate(1.0/reversedRate.getRate());
                resultList.add(reversedRate);
            }

            for(CurrencyExchangeRate rate:resultList)
            {
                Calendar fromDate = Calendar.getInstance();
                fromDate.setTime(rate.getExchangeDate());
                result.put(fromDate.get(Calendar.DAY_OF_WEEK)-2,rate);
            }
            return result;
        }
        catch (Exception e)
        {
            Log.e("CurrenctExchangeRateDAta","GetRatingFrom",e);
            return null;
        }
    }

    public HashMap<Integer, CurrencyExchangeRate> getRatingsFromLastMonth(Currency from, Currency to)
    {
        Date monthStart = MainApp.getFirstDayOfMonth();
        try
        {
            final QueryBuilder<CurrencyExchangeRate, Long> qb = getDao().queryBuilder();

            HashMap<Integer, CurrencyExchangeRate> result = new HashMap<Integer, CurrencyExchangeRate>();
            List<CurrencyExchangeRate> resultList,reversedResultList;
            resultList = qb.orderBy("exchangeDate",true).where().eq("from", from).and().eq("to", to).and().gt("exchangeDate", monthStart).query();
            reversedResultList = qb.orderBy("exchangeDate",true).where().eq("from", to).and().eq("to", from).and().gt("exchangeDate", monthStart).query();


            for(CurrencyExchangeRate reversedRate:reversedResultList)
            {
                from = reversedRate.getFrom();
                reversedRate.setFrom(reversedRate.getTo());
                reversedRate.setTo(from);
                reversedRate.setRate(1.0/reversedRate.getRate());
                resultList.add(reversedRate);
            }

            for(CurrencyExchangeRate rate:resultList)
            {
                Calendar date = Calendar.getInstance();
                date.setTime(rate.getExchangeDate());
                result.put(date.get(Calendar.DAY_OF_MONTH),rate);
            }
            return result;
        }
        catch (Exception e)
        {
            Log.e("CurrenctExchangeRateDAta","GetRatingFrom",e);
            return null;
        }
    }

    public HashMap<Integer, CurrencyExchangeRate> getRatingsFromYearMonthly(int year, Currency from, Currency to)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, year);
        Date yearStart = calendar.getTime();
        HashMap<Integer, CurrencyExchangeRate> result = new HashMap<Integer, CurrencyExchangeRate>();


        try
        {
            final QueryBuilder<CurrencyExchangeRate, Long> qb = getDao().queryBuilder();

            List<CurrencyExchangeRate> resultList,reversedResultList;
            resultList = qb.orderBy("exchangeDate",true).where().eq("from", from).and().eq("to", to).and().gt("exchangeDate", yearStart).query();
            reversedResultList = qb.orderBy("exchangeDate",true).where().eq("from", to).and().eq("to", from).and().gt("exchangeDate", yearStart).query();

            for(CurrencyExchangeRate reversedRate:reversedResultList)
            {
                from = reversedRate.getFrom();
                reversedRate.setFrom(reversedRate.getTo());
                reversedRate.setTo(from);
                reversedRate.setRate(1.0/reversedRate.getRate());
                resultList.add(reversedRate);
            }

            Collections.sort(resultList, currencyDateComparator);


            int monthIndex = 0;
            for(CurrencyExchangeRate rate:resultList)
            {
                if(rate.getExchangeDate().getMonth() >= monthIndex)
                {
                    monthIndex = rate.getExchangeDate().getMonth();
                    result.put(monthIndex,rate);
                    monthIndex++;
                }

            }

            Log.wtf("res list",""+result);
            return result;
        }
        catch (Exception e)
        {
            Log.e("CurrenctExchangeRateDAta","GetRatingFrom",e);
            return null;
        }
    }

    public List<CurrencyExchangeRate> getRatingsFrom(Currency from,Currency to)
    {
        try
        {
            List<CurrencyExchangeRate> resultList;
            final QueryBuilder<CurrencyExchangeRate, Long> qb = getDao().queryBuilder();
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

            float minValue,maxValue;
            if(min==null || max == null)
            {
                minValue = 0;
                maxValue = 1;
            }
            else
            {
                minValue = (float) ((100 - percentOfMargin) / 100.0 * min.getRate());
                maxValue = (float) ((100 + percentOfMargin) / 100.0 * max.getRate());
            }

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
                if(rate==null)
                    return null;

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
