package com.example.inz.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;


public class MainData
	extends OrmLiteSqliteOpenHelper
{
	private final static String DATABASE_NAME = "main_data.sqlite";
	private final static int DATABASE_VERSION = 1;

	private final UserData userData;
    private final ExpenseData expenseData;
    private final CategoryData categoryData;
    private final CashAmmountData cashAmmountData;

	public MainData(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		this.userData = new UserData(this);
        this.expenseData = new ExpenseData(this);
        this.categoryData = new CategoryData(this);
        this.cashAmmountData = new CashAmmountData(this);

    }

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{

			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, Expense.class);
			TableUtils.createTable(connectionSource, CashAmmount.class);
			TableUtils.createTable(connectionSource, Category.class);
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

    public UserData getUserData()
    {
        return userData;
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
}
