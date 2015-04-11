package com.mierzejewski.inzynierka.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.Pair;
import com.mierzejewski.inzynierka.MainApp;
import com.mierzejewski.inzynierka.model.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dom on 29/10/14.
 */
public class CurrencyExchangeDownloadService extends IntentService
{


    public static final String DATE_KEY = "dateKey";
    private static final String FILENAME_KEY = "filename_key";


    public static final String SHARED_PREF_KEY = "CurrencyExchangeDownloadService";
    public static final String DIR_KEY = "dirKey";
    public static final String DIR_UPDATE_KEY = "dirUpdateKey";

    private static final String XML_URL = "http://www.nbp.pl/Kursy/xml/LastA.xml";
    private static final String BASE_URL = "http://www.nbp.pl/kursy/xml/";
    private static final String DIR_URL = "http://www.nbp.pl/kursy/xml/dir.txt";
    private static final String STORED_KEY = "storedKey";

    private final CurrencyExchangeRateData dao;
    private static final int UPDATE_FREQUENCY_IN_DAYS = 1;


    public CurrencyExchangeDownloadService()
    {
        super("CurrencyExchangeDownloadService");
        dao = new MainData().getExchangeRateData();
    }

    public CurrencyExchangeDownloadService(String name)
    {
        super(name);
        dao = new MainData().getExchangeRateData();
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        Date exchangeDate = null;
        String name = null;

        if(shouldUpdateDir())
        {
            updateDir();
        }

        BufferedInputStream stream;

        if(intent.getSerializableExtra(DATE_KEY)!= null && intent.getStringExtra(FILENAME_KEY)!= null)
        {
            exchangeDate = (Date) intent.getSerializableExtra(DATE_KEY);
            name = intent.getStringExtra(FILENAME_KEY);

            if(wasStoredBefore(name))
            {
                return;
            }

            stream = getCurrencyXmlStreamFromName(name);
        }
        else
        {
            if(!shouldStart())
                return;

            stream = getLastCurrencyXmlStream();
        }



        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();

            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, "utf-8");

            ArrayList<CurrencyExchangeRate> list = parseXML(myParser, Currency.PLN, exchangeDate);
            Log.wtf("tag","list:"+list);


            dao.storeRates(list);
            setStored(name);

        }
        catch (Exception e)
        {
            Log.e("CurrencyExchangeService","e",e);
        }

    }

    private void setStored(String filename)
    {
        SharedPreferences sharedpreferences = MainApp.getAppContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String previous = sharedpreferences.getString(STORED_KEY, "");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(STORED_KEY, previous+"\n"+filename );
        editor.commit();
    }
    private boolean wasStoredBefore(String filename)
    {
        SharedPreferences sharedpreferences = MainApp.getAppContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        String previous = sharedpreferences.getString(STORED_KEY, "");

        return previous.contains(filename);
    }



    private ArrayList<CurrencyExchangeRate> parseXML(XmlPullParser parser,Currency to,Date dateIfNullCurrent) throws XmlPullParserException,IOException
    {
        ArrayList<CurrencyExchangeRate> CurrencyExchangeRates = null;
        int eventType = parser.getEventType();
        CurrencyExchangeRate currentCurrencyExchangeRate = null;

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    CurrencyExchangeRates = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if (name.equalsIgnoreCase("pozycja"))
                    {
                        currentCurrencyExchangeRate = new CurrencyExchangeRate();
                        if(dateIfNullCurrent != null)
                        {
                           currentCurrencyExchangeRate.setExchangeDate(dateIfNullCurrent);
                        }
                        else
                        {
                            currentCurrencyExchangeRate.setExchangeDate(new Date());
                        }
                        currentCurrencyExchangeRate.setTo(to);
                    }
                    else if (currentCurrencyExchangeRate != null)
                    {
                        if (name.equals("kod_waluty"))
                        {
                            currentCurrencyExchangeRate.setFrom(Currency.getFromAbbr(parser.nextText()));
                        }
                        else if (name.equals("kurs_sredni"))
                        {
                            currentCurrencyExchangeRate.setRate(Double.parseDouble(parser.nextText().replace(',', '.')));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("pozycja") && currentCurrencyExchangeRate.getTo()!= null)
                    {
                        if(currentCurrencyExchangeRate.getFrom()!=null && currentCurrencyExchangeRate.getTo() != null )
                            CurrencyExchangeRates.add(currentCurrencyExchangeRate);
                    }
                    break;
            }
            eventType = parser.next();
        }

        return CurrencyExchangeRates;
    }

    private BufferedInputStream getLastCurrencyXmlStream()
    {
        try
        {
            URL xmlUrl = new URL(XML_URL);
            HttpURLConnection http = openGetConnection(xmlUrl);

            int responseCode = http.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedInputStream inputStream = new BufferedInputStream(http.getInputStream());
                return inputStream;
            }
            else
            {
                throw new Exception("response code is not ok:"+responseCode);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedInputStream getEntriesDirStream()
    {
        try
        {
            URL xmlUrl = new URL(DIR_URL);
            HttpURLConnection http = openGetConnection(xmlUrl);

            int responseCode = http.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedInputStream inputStream = new BufferedInputStream(http.getInputStream());
                return inputStream;
            }
            else
            {
                throw new Exception("response code is not ok:"+responseCode);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    private BufferedInputStream getCurrencyXmlStreamFromName(String filename)
    {
        try
        {
            String xmlUrl = (BASE_URL + filename + ".xml");

            Log.wtf("service","getCurrencyXmlStreamFromDAte"+xmlUrl);
            URL url = new URL(xmlUrl);
            HttpURLConnection http = openGetConnection(url);

            int responseCode = http.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedInputStream inputStream = new BufferedInputStream(http.getInputStream());
                return inputStream;
            }
            else
            {
                throw new Exception("response code is not ok:"+responseCode);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static HttpURLConnection openGetConnection(URL url) throws IOException
    {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.setConnectTimeout(15000);
        return http;
    }

    private boolean shouldStart()
    {
        try
        {
            Date now = new Date();
            Date lastUpdate = dao.getLastUpdateDate();

            if (now.getTime() < lastUpdate.getTime() + (UPDATE_FREQUENCY_IN_DAYS * 24 * 60 * 60 * 1000))
                return false;

            return true;
        }
        catch (Exception e)
        {
            Log.e("shouldStart","exception",e);
            return true;
        }
    }


    public static void requestExchangeRatesFromFilename(String filename,Date exchangeDate)
    {
        Context context = MainApp.getAppContext();
        Intent serviceIntent = new Intent(context,CurrencyExchangeDownloadService.class);
        serviceIntent.putExtra(FILENAME_KEY,filename);
        serviceIntent.putExtra(DATE_KEY,exchangeDate);
        context.startService(serviceIntent);
    }

    public static void updateDir()
    {
        try
        {

        BufferedInputStream stream = getEntriesDirStream();
        String streamText = convertStreamToString(stream);
        SharedPreferences sharedpreferences = MainApp.getAppContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DIR_KEY, streamText );
        editor.putLong(DIR_UPDATE_KEY,new Date().getTime());
        editor.commit();

        }
        catch (Exception e)
        {
            Log.e("ceds","updateDir",e);
        }
    }

    public static boolean shouldUpdateDir()
    {
        SharedPreferences sharedpreferences = MainApp.getAppContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        try
        {
            long lastUpdate = sharedpreferences.getLong(DIR_UPDATE_KEY, 0);
            Date now = new Date();
            if (now.getTime() < lastUpdate + (UPDATE_FREQUENCY_IN_DAYS * 24 * 60 * 60 * 1000))
                return false;

            return true;
        }
        catch (Exception e)
        {
            Log.e("shouldStart","exception",e);
            return true;
        }
     }

    public static String getDirText()
    {
        SharedPreferences sharedpreferences = MainApp.getAppContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        return sharedpreferences.getString(DIR_KEY,"");

    }


    public static String getExchangeFileName(Date date)
    {
        String text = getDirText();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String year = String.format("%02d",(cal.get(Calendar.YEAR)-2000));
        String month = String.format("%02d",(1+cal.get(Calendar.MONTH)));
        String dayOfMonth = String.format("%02d",cal.get(Calendar.DAY_OF_MONTH));

        String searchSuffix = year+month+dayOfMonth;

        Pattern p = Pattern.compile("a.*"+searchSuffix);
        Matcher m = p.matcher(text);

        while (m.find())
        {

            Log.i("isExchangeAvalible()","Found"+m.group());
            return m.group();
        }

        Log.i("isExchangeAvalible()","not Found"+searchSuffix);

        return null;


    }


    public static ArrayList<Pair<String,Date>> getMonthAvalibleFilenames(int year)
    {
        ArrayList<Pair<String,Date>> avalibleDates = new ArrayList<Pair<String,Date>>(12);

        for(int month=0;month<12;month++)
        {
            int day=1;
            Date date = new GregorianCalendar(year, month, day).getTime();
            String filename = getExchangeFileName(date);

            while(filename == null)
            {
                filename = getExchangeFileName(date);
                day++;
                date = new GregorianCalendar(year, month, day).getTime();

                if(day>31)
                    break;
            }

            avalibleDates.add(new Pair<String, Date>(filename,date));

        }

        return avalibleDates;
    }





    public static ArrayList<Pair<String,Date>> getLastDaysFilenames(int days)
    {
        ArrayList<Pair<String,Date>> avalibleDates = new ArrayList<Pair<String,Date>>();

        Date date = new Date();


        for(int i = 0; i <= days; i++)
        {
            String filename = getExchangeFileName(date);

            avalibleDates.add(new Pair<String, Date>(filename,date));

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE,-1);
            date = cal.getTime();
        }
        return avalibleDates;
    }

    static String convertStreamToString(java.io.InputStream is) {
    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
}

    public static void downloadExchangeRatesFrom2LastYears()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);



        for(Pair<String,Date> filenameAndDate : CurrencyExchangeDownloadService.getLastDaysFilenames(30))
        {
            CurrencyExchangeDownloadService.requestExchangeRatesFromFilename(filenameAndDate.first,filenameAndDate.second);
        }
        for(Pair<String,Date> filenameAndDate : CurrencyExchangeDownloadService.getMonthAvalibleFilenames(year))
        {
            CurrencyExchangeDownloadService.requestExchangeRatesFromFilename(filenameAndDate.first,filenameAndDate.second);
        }
        for(Pair<String,Date> filenameAndDate : CurrencyExchangeDownloadService.getMonthAvalibleFilenames(year-1))
        {
            CurrencyExchangeDownloadService.requestExchangeRatesFromFilename(filenameAndDate.first,filenameAndDate.second);
        }

    }

}

