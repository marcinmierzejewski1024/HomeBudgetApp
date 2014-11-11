package com.example.inz.model;

import java.lang.ref.WeakReference;

public class BaseData
{
	protected final WeakReference<MainData> mainData;
	
	public BaseData(MainData mainData)
	{
		this.mainData = new WeakReference<MainData>(mainData);
	}

    public WeakReference<MainData> getMainData()
    {
        return mainData;
    }
}
