package com.example.inz.model;

import android.location.Location;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
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

                expense.setCashAmmountId(getMainData().get().getCashAmmountData().storeCashAmmount(expense.getCash()).getCashAmmountId());
                expense.setCategoryId(getMainData().get().getCategoryData().storeCategory(expense.getCategory()).getCategoryId());

                getDao().createOrUpdate(expense);
                expense.setStoredInDb(true);
                return expense;
            }
        });
    }

    public Expense updateExpense(final Expense expense) throws SQLException
    {
        return TransactionManager.callInTransaction(mainData.get().getConnectionSource(), new Callable<Expense>()
        {
            @Override
            public Expense call() throws Exception
            {

                expense.setCashAmmountId(getMainData().get().getCashAmmountData().updateCashAmmount(expense.getCash()).getCashAmmountId());

                getDao().update(expense);
                expense.setStoredInDb(true);
                return expense;
            }
        });
    }

    public Expense getExpenseById(long id) throws SQLException
    {
        final QueryBuilder<Expense, Long> qb = getDao().queryBuilder();

        qb.where().eq("expenseId", id);

        Expense result = qb.queryForFirst();
        result.setCategory(getMainData().get().getCategoryData().getCategoryById(result.getCategoryId()));
        result.setCash(getMainData().get().getCashAmmountData().getCashAmmountById(result.getCashAmmountId()));
        result.setStoredInDb(true);
        return result;
    }

    public List<Expense> getExpenses() throws SQLException
    {

        final QueryBuilder<Expense, Long> qb = getDao().queryBuilder();

        List<Expense> results =  qb.query();

        for(Expense result : results)
        {
            result.setCategory(getMainData().get().getCategoryData().getCategoryById(result.getCategoryId()));
            result.setCash(getMainData().get().getCashAmmountData().getCashAmmountById(result.getCashAmmountId()));
            result.setStoredInDb(true);
        }
        return results;
    }

    public List<Expense> getExpensesByCategoryId(Long categoryId) throws SQLException
    {
        final QueryBuilder<Expense, Long> qb = getDao().queryBuilder();
        qb.where().eq("categoryId",categoryId);

        List<Expense> results =  qb.query();

        for(Expense result : results)
        {
            result.setCategory(getMainData().get().getCategoryData().getCategoryById(result.getCategoryId()));
            result.setCash(getMainData().get().getCashAmmountData().getCashAmmountById(result.getCashAmmountId()));
            result.setStoredInDb(true);
        }
        return results;
    }

    public boolean deleteExpense(long expenseId)
    {
        try
        {
            final DeleteBuilder<Expense, Long> qb = getDao().deleteBuilder();
            qb.where().eq("expenseId", expenseId);
            qb.delete();
            return true;
        }
        catch (Exception e)
        {
            Log.e("ExpenseData",e.toString());
            return false;
        }
    }
}
