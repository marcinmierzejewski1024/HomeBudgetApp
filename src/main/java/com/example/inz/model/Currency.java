package com.example.inz.model;

import java.io.Serializable;

/**
 * Created by dom on 29/10/14.
 */
public enum Currency implements Serializable
{
    PLN("zł", true,"PLN"),
    USD("$", false,"USD"),
    GPB("£", false,"GPB"),
    EUR("€", true,"EUR");

    public String abrevation;
    String name;
    public boolean afterAmount;

    Currency(String abr, boolean afterAmount,String name)
    {
        this.abrevation = abr;
        this.afterAmount = afterAmount;
        this.name = name;
    }



    public static Currency getDefault()
    {
        //STUB TODO:
        return PLN;
    }

    public static Currency getFromAbbr(String abbr)
    {
        try
        {
            return valueOf(abbr);
        }
        catch (Exception e)
        {
            return null;
        }
    }


    @Override
    public String toString()
    {
        return name;
    }
}
