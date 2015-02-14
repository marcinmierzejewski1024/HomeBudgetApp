package com.example.inz;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import com.example.inz.services.CurrencyExchangeDownloadService;

/**
 * Created by dom on 15/11/14.
 */
public class MainApp extends Application
{
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();

        startService(new Intent(context, CurrencyExchangeDownloadService.class));

    }

    public static Context getAppContext()
    {
        return context;
    }
}
