package com.example.inz.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by dom on 29/10/14.
 */
public class User
{
    @DatabaseField(generatedId = true)
    long userId;
    @DatabaseField()
    String name;

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public User(String name)
    {
        this.name = name;
    }

    public User()
    {
    }
}
