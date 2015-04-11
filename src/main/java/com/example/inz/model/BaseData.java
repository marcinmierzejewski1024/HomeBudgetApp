package com.example.inz.model;

import android.util.Pair;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class BaseData
{
	protected final MainData mainData;
	
	public BaseData(MainData mainData)
	{
		this.mainData =  mainData;
	}

    public MainData getMainData()
    {
        return mainData;
    }


}
