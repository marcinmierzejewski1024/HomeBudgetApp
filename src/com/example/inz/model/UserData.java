package com.example.inz.model;

import android.location.Location;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Created by dom on 09/11/14.
 */
public class UserData extends BaseData

{
    public UserData(MainData mainData)
    {
        super(mainData);
    }


    private Dao<User, Long> getDao()
            throws SQLException
    {
        return mainData.get().getDao(User.class);
    }


    public User storeUser(final User user) throws SQLException
    {
        return TransactionManager.callInTransaction(mainData.get().getConnectionSource(), new Callable<User>()
        {
            @Override
            public User call() throws Exception
            {
                getDao().createOrUpdate(user);

                return user;
            }
        });
    }

    public User getUserById(long id) throws SQLException
    {
        final QueryBuilder<User, Long> qb = getDao().queryBuilder();

        qb.where().eq("userId", id);

        return qb.queryForFirst();
    }
}
