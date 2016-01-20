// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import android.content.*;
import android.net.NetworkInfo;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.softspb.weather.core:
//			WeatherApplicationPreferences, WeatherDataCache, WeatherApplication

public class WeatherConnectivityReceiver extends BroadcastReceiver
{

	private static final Logger logger = Loggers.getLogger(WeatherConnectivityReceiver.class);

	public WeatherConnectivityReceiver()
	{
	}

	public void onReceive(Context context, Intent intent)
	{
		WeatherApplicationPreferences weatherapplicationpreferences;
		WeatherDataCache weatherdatacache;
		List list1;
		logger.d("ConnectivityChangeReceiver.onReceive");
		weatherapplicationpreferences = new WeatherApplicationPreferences(context);
		List list = weatherapplicationpreferences.getAllCityIds();
		weatherdatacache = WeatherDataCache.getInstance(context);
		list1 = weatherdatacache.resolveCurrentLocationCityIds(list);
		NetworkInfo networkinfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
		if (networkinfo.isAvailable())
		{
			if (!(networkinfo.getType() != 1 || !weatherapplicationpreferences.isUseOnlyWifi()))
			{	
			logger.d("connected to Wi-Fi network, doing update");
			WeatherApplication.updateWeather(weatherapplicationpreferences.getAllCityIds(), context);
			}
		}
		
		logger.d((new StringBuilder()).append("Consider updating cities: ").append(list1).toString());
		Iterator iterator = list1.iterator();
		while (iterator.hasNext()) 
			weatherdatacache.considerWeatherUpdate(((Integer)iterator.next()).intValue());
		weatherapplicationpreferences.dispose();

	}

}
