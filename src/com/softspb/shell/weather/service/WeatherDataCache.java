// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.weather.service;

import android.app.Application;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import com.softspb.util.DateChangedObserver;
import com.spb.cities.service.CurrentLocationClient;
import java.util.Iterator;
import java.util.List;

public class WeatherDataCache extends com.softspb.weather.core.WeatherDataCache
	implements com.softspb.util.DateChangedObserver.DateChangedListener
{

	public WeatherDataCache(Application application, CurrentLocationClient currentlocationclient)
	{
		super(application, currentlocationclient);
	}

	protected String createCurrentLocationCityName(String s)
	{
		return s;
	}

	public void onDateChanged()
	{
		mHandler.post(new Runnable() {

			final WeatherDataCache this$0;

			public void run()
			{
				int i = 0;
			//	i = WeatherDataCache.this.<init>.size();
				Throwable throwable;
				for (int j = 0; j < i; j++)
				{
			//		for (Iterator iterator = ((List)WeatherDataCache.this.<init>.valueAt(j)).iterator(); iterator.hasNext(); ((ContentObserver)iterator.next()).onChange(false));
					return;
				}

			}
			{
				this$0 = WeatherDataCache.this;
//				super();
			}
		});
		mHandler.removeCallbacks(considerUpdateRunnable);
		mHandler.post(considerUpdateRunnable);
	}

	/**
	 * @deprecated Method start is deprecated
	 */

	public synchronized void start()
	{
		super.start();
		DateChangedObserver.getInstance().registerListener(this);
	}

	/**
	 * @deprecated Method stop is deprecated
	 */

	public synchronized void stop()
	{
		DateChangedObserver.getInstance().unregisterListener(this);
		super.stop();
	}


}
