package com.example.inz.model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Currency;
import java.util.Date;

/**
 * Created by dom on 29/10/14.
 */
public class Expense
{
    @DatabaseField(generatedId = true)
    long expenseId;
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

    public long getExpenseId()
    {
        return expenseId;
    }

    public void setExpenseId(long expenseId)
    {
        this.expenseId = expenseId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getCashAmmountId()
    {
        return cashAmmountId;
    }

    public void setCashAmmountId(long cashAmmountId)
    {
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
        this.cash = cash;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Expense(Category category, CashAmmount cash, String name, String description, Date date)
    {
        this.category = category;
        this.cash = cash;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public Expense()
    {
    }
}
