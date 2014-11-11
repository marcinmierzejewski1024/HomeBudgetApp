package com.example.inz.model;

import java.io.Serializable;

/**
 * Created by dom on 29/10/14.
 */
public enum Currency implements Serializable
{
    PLN("zł", true),
    USD("$", false),
    GPB("£", false),
    EUR("€", true);

    public String abrevation;
    public boolean afterAmount;

    Currency(String abr, boolean afterAmount)
    {
        this.abrevation = abr;
        this.afterAmount = afterAmount;
    }


}