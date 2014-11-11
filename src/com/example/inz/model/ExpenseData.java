package com.example.inz.model;

import android.location.Location;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by dom on 09/11/14.
 */
public class ExpenseData extends BaseData

{
    public ExpenseData(MainData mainData)
    {
        super(mainData);
    }


    private Dao<Expense, Long> getDao()
            throws SQLException
    {
        return mainData.get().getDao(Expense.class);
    }


    public Expense storeExpense(final Expense expense) throws SQLException
    {
        return TransactionManager.callInTransaction(mainData.get().getConnectionSource(), new Callable<Expense>()
        {
            @Override
            public Expense call() throws Exception
            {
                getDao().createOrUpdate(expense);
                getMainData().get().getCashAmmountData().storeCashAmmount(expense.getCash());

                return expense;
            }
        });
    }

    public Expense getExpenseById(long id) throws SQLException
    {
        final QueryBuilder<Expense, Long> qb = getDao().queryBuilder();

        qb.where().eq("expenseId", id);

        return qb.queryForFirst();
    }

    public List<Expense> getExpensesByUserId(long id) throws SQLException
    {
        final QueryBuilder<Expense, Long> qb = getDao().queryBuilder();

        qb.where().eq("userId", id);

        return qb.query();
    }
}
