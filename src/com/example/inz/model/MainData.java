package com.example.inz.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainData
	extends OrmLiteSqliteOpenHelper
{
	private final static String DATABASE_NAME = "main_data.sqlite";
	private final static int DATABASE_VERSION = 3;

    private final ExpenseData expenseData;
    private final CategoryData categoryData;
    private final CashAmmountData cashAmmountData;

	public MainData(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.expenseData = new ExpenseData(this);
        this.categoryData = new CategoryData(this);
        this.cashAmmountData = new CashAmmountData(this);

    }

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTable(connectionSource, Expense.class);
			TableUtils.createTable(connectionSource, CashAmmount.class);
			TableUtils.createTable(connectionSource, Category.class);

            //dodanie podstawowoych kategorii

        }
		catch (SQLException ex)
		{
		    throw new RuntimeException(ex);
		}
	}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2)
    {
        onCreate(sqLiteDatabase,connectionSource);
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

    public List<Pair<Category,CashAmmount>> getCategoryGroupedExpenses(Date startDate)
    {
        List<Pair<Category,CashAmmount>> result = new ArrayList<Pair<Category, CashAmmount>>();
        List<Category> categories = getCategoryData().getCategories();

        for(Category singleCategory : categories)
        {
            result.add(new Pair<Category, CashAmmount>(singleCategory,new CashAmmount(0,Currency.getDefault())));
        }

        try
        {
            for(Expense single : getExpenseData().getExpenses())
            {
                if(single.getDate().after(startDate))
                {

                    for(Pair<Category,CashAmmount> singlePair : result)
                    {
                        if(singlePair.equals(single.getCategory()))
                            {
                                singlePair.second.addPennies(single.getCash().getPennies());

                            }
                    }
                    //Currency.getDefault().
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
