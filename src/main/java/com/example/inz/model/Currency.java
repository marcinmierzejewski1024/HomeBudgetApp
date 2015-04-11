package com.example.inz.model;

import java.io.Serializable;

/**
 * Created by dom on 29/10/14.
 */
public enum Currency implements Serializable
{
    PLN("zł", true,"PLN",100),
    USD("$", false,"USD",100),
    GBP("£", false,"GBP",100),
    EUR("€", true,"EUR",100);

    public String abrevation;
    String name;
    public int penniesSum;
    public boolean afterAmount;

    Currency(String abr, boolean afterAmount,String name,int pennies)
    {
        this.abrevation = abr;
        this.afterAmount = afterAmount;
        this.name = name;
        this.penniesSum = pennies;
    }



    public static Currency getDefault()
    {
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
