package com.example.inz.model;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by dom on 13/12/14.
 */
public class IncomeData extends BaseData

{
    public IncomeData(MainData mainData)
    {
        super(mainData);
    }

    private Dao<Income, Long> getDao()
            throws SQLException
    {
        return mainData.get().getDao(Income.class);
    }


    public Income storeIncome(final Income income) throws SQLException
    {
        return TransactionManager.callInTransaction(mainData.get().getConnectionSource(), new Callable<Income>()
        {
            @Override
            public Income call() throws Exception
            {

                income.setCashAmmountId(getMainData().get().getCashAmmountData().storeCashAmmount(income.getCash()).getCashAmmountId());
                income.setCategoryId(getMainData().get().getCategoryData().storeCategory(income.getCategory()).getCategoryId());

                getDao().createOrUpdate(income);
                income.setStoredInDb(true);
                return income;
            }
        });
    }

    public Income updateIncome(final Income Income) throws SQLException
    {
        return TransactionManager.callInTransaction(mainData.get().getConnectionSource(), new Callable<Income>()
        {
            @Override
            public Income call() throws Exception
            {

                Income.setCashAmmountId(getMainData().get().getCashAmmountData().updateCashAmmount(Income.getCash()).getCashAmmountId());

                getDao().update(Income);
                Income.setStoredInDb(true);
                return Income;
            }
        });
    }

    public Income getIncomeById(long id) throws SQLException
    {
        final QueryBuilder<Income, Long> qb = getDao().queryBuilder();

        qb.where().eq("IncomeId", id);

        Income result = qb.queryForFirst();
        result.setCategory(getMainData().get().getCategoryData().getCategoryById(result.getCategoryId()));
        result.setCash(getMainData().get().getCashAmmountData().getCashAmmountById(result.getCashAmmountId()));
        result.setStoredInDb(true);
        return result;
    }

    public List<Income> getIncomes() throws SQLException
    {

        final QueryBuilder<Income, Long> qb = getDao().queryBuilder();

        List<Income> results =  qb.query();

        for(Income result : results)
        {
            result.setCategory(getMainData().get().getCategoryData().getCategoryById(result.getCategoryId()));
            result.setCash(getMainData().get().getCashAmmountData().getCashAmmountById(result.getCashAmmountId()));
            result.setStoredInDb(true);
        }
        return results;
    }

    public List<Income> getIncomesByCategoryId(Long categoryId) throws SQLException
    {
        final QueryBuilder<Income, Long> qb = getDao().queryBuilder();
        qb.where().eq("categoryId",categoryId);

        List<Income> results =  qb.query();

        for(Income result : results)
        {
            result.setCategory(getMainData().get().getCategoryData().getCategoryById(result.getCategoryId()));
            result.setCash(getMainData().get().getCashAmmountData().getCashAmmountById(result.getCashAmmountId()));
            result.setStoredInDb(true);
        }
        return results;
    }

    public boolean deleteIncome(long IncomeId)
    {
        try
        {
            final DeleteBuilder<Income, Long> qb = getDao().deleteBuilder();
            qb.where().eq("IncomeId", IncomeId);
            qb.delete();
            return true;
        }
        catch (Exception e)
        {
            Log.e("IncomeData", e.toString());
            return false;
        }
    }
}
