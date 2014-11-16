package com.example.inz.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * Created by dom on 29/10/14.
 */
public class CashAmmount extends AbsDatabaseItem
{
    @DatabaseField(generatedId = true)
    long cashAmmountId;
    @DatabaseField()
    int pennies;
    @DatabaseField(dataType= DataType.SERIALIZABLE)
    Currency currency;

    public CashAmmount(int pennies, Currency currency)
    {
        this.pennies = pennies;
        this.currency = currency;
    }

    public CashAmmount()
    {
    }

    @Override
    public String toString()
    {
        String ammountInCurrency = formatDecimal(pennies/100f);

        if(currency.afterAmount)
            return ammountInCurrency+" "+currency.abrevation;
        else
            return currency.abrevation + " " + ammountInCurrency;

    }

    private String formatDecimal(float number)
    {
        float epsilon = 0.004f; // 4 tenths of a cent
        if (Math.abs(Math.round(number) - number) < epsilon) {
            return String.format("%10.0f", number); // sdb
        } else {
            return String.format("%10.2f", number); // dj_segfault
        }
    }

    public long getCashAmmountId()
    {
        return cashAmmountId;
    }

    public void setCashAmmountId(long cashAmmountId)
    {
        setChanged();
        this.cashAmmountId = cashAmmountId;
    }

    public int getPennies()
    {
        return pennies;
    }

    public void setPennies(int pennies)
    {
        setChanged();
        this.pennies = pennies;
    }

    public void addPennies(int p)
    {
        setChanged();
        this.pennies += p;
    }

    public Currency getCurrency()
    {
        return currency;
    }

    public void setCurrency(Currency currency)
    {
        setChanged();
        this.currency = currency;
    }

    public boolean isZero()
    {
        return pennies==0;
    }

}
