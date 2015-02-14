package com.example.inz.model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by dom on 13/12/14.
 */
public class Income extends AbsDatabaseItem
{
    @DatabaseField(generatedId = true)
    long incomeId;
    @DatabaseField()
    String name;
    @DatabaseField()
    String description;
    @DatabaseField()
    Date date;
    @DatabaseField()
    long cashAmmountId;
    @DatabaseField()
    long categoryId;

    CashAmmount cash;
    Category category;

    public long getIncomeId()
    {
        return incomeId;
    }

    public void setIncomeId(long incomeId)
    {
        this.incomeId = incomeId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        setChanged();
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        setChanged();
        this.description = description;
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

    public long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(long categoryId)
    {
        this.categoryId = categoryId;
    }

    public CashAmmount getCash()
    {
        return cash;
    }

    public void setCash(CashAmmount cash)
    {
        setChanged();
        this.cash = cash;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        setChanged();
        this.category = category;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        setChanged();
        this.date = date;
    }



    public Income(String name, String description, Date date, CashAmmount cash, Category category)
    {
        this.name = name;
        this.description = description;
        this.date = date;
        this.cash = cash;
        this.category = category;
    }

    public Income()
    {
    }
}
