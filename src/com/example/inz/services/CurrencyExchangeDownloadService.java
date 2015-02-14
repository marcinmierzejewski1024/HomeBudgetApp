package com.example.inz.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.example.inz.model.Currency;
import com.example.inz.model.CurrencyExchangeRate;
import com.example.inz.model.CurrencyExchangeRateData;
import com.example.inz.model.MainData;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dom on 29/10/14.
 */
public class CurrencyExchangeDownloadService extends IntentService
{
    public static final String XML_URL = "http://www.nbp.pl/Kursy/xml/LastA.xml";
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
        if(!shouldStart())
            return;

        try
        {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            BufferedInputStream stream = getCurrencyXmlStream();

            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, "utf-8");

            ArrayList<CurrencyExchangeRate> list = parseXML(myParser, Currency.PLN);
            Log.wtf("tag","list:"+list);


            dao.storeRates(list);

        }
        catch (Exception e)
        {
            Log.e("CurrencyExchangeService","e",e);
        }

    }

    private ArrayList<CurrencyExchangeRate> parseXML(XmlPullParser parser,Currency to) throws XmlPullParserException,IOException
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
                        currentCurrencyExchangeRate.setTo(to);
                        currentCurrencyExchangeRate.setExchangeDate(new Date());
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
                        CurrencyExchangeRates.add(currentCurrencyExchangeRate);
                    }
                    break;
            }
            eventType = parser.next();
        }

        return CurrencyExchangeRates;
    }

    private BufferedInputStream getCurrencyXmlStream()
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
}
