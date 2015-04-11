package com.example.inz;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Pair;
import com.example.inz.model.Category;
import com.example.inz.model.MainData;
import com.example.inz.services.CurrencyExchangeDownloadService;
import com.example.inz.services.ExchangeCurrencyCallback;

import java.util.*;

/**
 * Created by dom on 15/11/14.
 */
public class MainApp extends Application
{
    private static Context context;
    public final static String CHANGE_EXPENSE_BROADCAST = "CHANGE_EXPENSE_FILTER";
    public final static android.content.IntentFilter CHANGE_EXPENSE_FILTER = new IntentFilter(CHANGE_EXPENSE_BROADCAST);


    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();

        startService(new Intent(context, CurrencyExchangeDownloadService.class));

        CurrencyExchangeDownloadService.downloadExchangeRatesFrom2LastYears();



//        new MainData().dataBaseExporter.backup();
    }

    public static Context getAppContext()
    {
        return context;
    }

    public static Date getFirstDayOfMonth()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }


}
