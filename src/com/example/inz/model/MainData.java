package com.example.inz.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import com.example.inz.MainApp;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.*;


public class MainData
	extends OrmLiteSqliteOpenHelper
{
	private final static String DATABASE_NAME = "main_data.sqlite";
	private final static int DATABASE_VERSION = 8;

    private final ExpenseData expenseData;
    private final CategoryData categoryData;
    private final CashAmmountData cashAmmountData;
    private final CurrencyExchangeRateData exchangeRateData;

	public MainData()
	{
		super(MainApp.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);

        this.expenseData = new ExpenseData(this);
        this.categoryData = new CategoryData(this);
        this.cashAmmountData = new CashAmmountData(this);
        this.exchangeRateData = new CurrencyExchangeRateData(this);

    }

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTable(connectionSource, Expense.class);
			TableUtils.createTable(connectionSource, CashAmmount.class);
			TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, CurrencyExchangeRate.class);

            addPredefinedCategories();
            addPredefinedExchangeRates();

        }
		catch (SQLException ex)
		{
		    throw new RuntimeException(ex);
		}
	}

    private void addPredefinedExchangeRates() throws SQLException
    {
        Date nov2014 = new Date(1414877852000l);
        CurrencyExchangeRate rate1 = new CurrencyExchangeRate(nov2014,Currency.PLN,Currency.EUR,0.238325964);
        CurrencyExchangeRate rate2 = new CurrencyExchangeRate(nov2014,Currency.PLN,Currency.GPB,0.1848687936);
        CurrencyExchangeRate rate3 = new CurrencyExchangeRate(nov2014,Currency.PLN,Currency.USD,0.29981191);
        CurrencyExchangeRate rate4 = new CurrencyExchangeRate(nov2014,Currency.EUR,Currency.GPB,0.7558287258);
        CurrencyExchangeRate rate5 = new CurrencyExchangeRate(nov2014,Currency.EUR,Currency.USD,1.18895);
        CurrencyExchangeRate rate6 = new CurrencyExchangeRate(nov2014,Currency.GPB,Currency.USD,1.469214);

        Date dec2014 = new Date(1417469852000l);
        CurrencyExchangeRate rate7 = new CurrencyExchangeRate(dec2014,Currency.PLN,Currency.EUR,0.238155964);
        CurrencyExchangeRate rate8 = new CurrencyExchangeRate(dec2014,Currency.PLN,Currency.GPB,0.188687936);
        CurrencyExchangeRate rate9 = new CurrencyExchangeRate(dec2014,Currency.PLN,Currency.USD,0.296191);
        CurrencyExchangeRate rate10 = new CurrencyExchangeRate(dec2014,Currency.EUR,Currency.GPB,0.792287258);
        CurrencyExchangeRate rate11 = new CurrencyExchangeRate(dec2014,Currency.EUR,Currency.USD,1.243685);
        CurrencyExchangeRate rate12 = new CurrencyExchangeRate(dec2014,Currency.GPB,Currency.USD,1.56974);


        getExchangeRateData().storeRate(rate1);
        getExchangeRateData().storeRate(rate2);
        getExchangeRateData().storeRate(rate3);
        getExchangeRateData().storeRate(rate4);
        getExchangeRateData().storeRate(rate5);
        getExchangeRateData().storeRate(rate6);
        getExchangeRateData().storeRate(rate7);
        getExchangeRateData().storeRate(rate8);
        getExchangeRateData().storeRate(rate9);
        getExchangeRateData().storeRate(rate10);
        getExchangeRateData().storeRate(rate11);
        getExchangeRateData().storeRate(rate12);
    }

    private void addPredefinedCategories() throws SQLException
    {
        Category cat0 = new Category("Mieszkanie","#12aaee",null,false);
        Category cat1 = new Category("Transport","#aacc44",null,false);
        Category cat2 = new Category("Jedzenie","#a14897",null,false);
        Category cat3 = new Category("Rozrywka","#13a412",null,false);
        Category cat4 = new Category("Edukacja","#658732",null,false);
        Category cat5 = new Category("Rachunki","#98563f",null,false);


        getCategoryData().storeCategory(cat0);
        getCategoryData().storeCategory(cat1);
        getCategoryData().storeCategory(cat2);
        getCategoryData().storeCategory(cat3);
        getCategoryData().storeCategory(cat4);
        getCategoryData().storeCategory(cat5);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2)
    {
        try
        {
            TableUtils.dropTable(connectionSource, Expense.class,true);
            TableUtils.dropTable(connectionSource, CashAmmount.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, CurrencyExchangeRate.class, true);
        }
        catch (SQLException ex)
        {
            throw new RuntimeException(ex);
        }

        onCreate(sqLiteDatabase, connectionSource);
    }


    public ExpenseData getExpenseData()
    {
        return expenseData;
    }

    public CategoryData getCategoryData()
    {
        return categoryData;
    }

    public CashAmmountData getCashAmmountData()
    {
        return cashAmmountData;
    }

    public CurrencyExchangeRateData getExchangeRateData()
    {
        return exchangeRateData;
    }

    public List<Pair<Category,CashAmmount>> getCategoryGroupedExpenses(Date startDate, int numberOfCategories)
    {
        try
        {

            List<Pair<Category,CashAmmount>> result = new ArrayList<Pair<Category, CashAmmount>>();
            List<Category> categories = getCategoryData().getExpenseCategories();

            for(Category singleCategory : categories)
            {
                result.add(new Pair<Category, CashAmmount>(singleCategory,new CashAmmount(0,Currency.getDefault())));
            }

            int allSum = 0;
            for(Expense single : getExpenseData().getExpenses())
            {
                if(single.getCash()!=null)
                {
                    allSum += single.getCash().getPennies();
                }

                if(single.getDate().after(startDate))
                {

                    for(Pair<Category,CashAmmount> singlePair : result)
                    {
                        if(singlePair.first.equals(single.getCategory()))
                            {
                                singlePair.second.addCash(single.getCash());

                            }
                    }
                    //Currency.getDefault().
                }
            }


            List<Pair<Category,CashAmmount>> bigests = new ArrayList<Pair<Category,CashAmmount>>(numberOfCategories);
            Pair<Category,CashAmmount> smallest = null;

            for(Pair<Category,CashAmmount> singlePair : result)
            {
                if(bigests.size()>=numberOfCategories)
                {
                    //wyznaczenie obecnie najmniejszej
                    for (Pair<Category, CashAmmount> oneOfCurrentBigests : bigests)
                    {
                        if(smallest == null)
                            smallest = oneOfCurrentBigests;

                        if(smallest.second.getPennies()< oneOfCurrentBigests.second.getPennies())
                        {
                            smallest = oneOfCurrentBigests;
                        }
                    }

                    if(singlePair.second.getPennies() < smallest.second.getPennies())
                    {
                        bigests.remove(smallest);
                        bigests.add(singlePair);
                    }

                }
                else
                {
                    bigests.add(singlePair);
                }

            }

            Collections.sort(bigests, new Comparator<Pair<Category, CashAmmount>>()
            {
                @Override
                public int compare(Pair<Category, CashAmmount> lhs, Pair<Category, CashAmmount> rhs)
                {
                   return rhs.second.getPennies()-lhs.second.getPennies();
                }
            });

            int biggestSum = 0;
            for(Pair<Category,CashAmmount> singlePair :bigests)
            {
                biggestSum += singlePair.second.getPennies();
            }

            if(allSum-biggestSum != 0)
            {
                Pair<Category, CashAmmount> restPair = new Pair<Category, CashAmmount>(Category.getRestCategory(), new CashAmmount(allSum - biggestSum, Currency.getDefault()));
                bigests.add(restPair);
            }

            return bigests;

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
