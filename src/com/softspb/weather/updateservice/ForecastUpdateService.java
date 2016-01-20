// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.updateservice;

import android.content.*;
import com.softspb.updateservice.DownloadClient;
import com.softspb.util.log.Logger;
import com.softspb.weather.model.*;

// Referenced classes of package com.softspb.weather.updateservice:
//			WeatherUpdateService

public abstract class ForecastUpdateService extends WeatherUpdateService
{

	public static final String ACTION_UPDATE = "com.softspb.weather.updateservice.action.UpdateForecast";

	public ForecastUpdateService()
	{
	}

	protected abstract DownloadClient createDownloadClient(Context context);

	protected int getUpdateType()
	{
		return 2;
	}

	protected boolean onDataReceived(ForecastArray forecastarray)
	{
		Forecast aforecast[] = forecastarray.getItems();
		int i = aforecast.length;
		int j = forecastarray.getCityID();
		logger.d("onForecastDataReceived...");
		ContentValues acontentvalues[] = new ContentValues[i];
		for (int k = 0; k < i; k++)
		{
			Forecast forecast = aforecast[k];
			ContentValues contentvalues = new ContentValues();
			int l = forecast.getDateLocal();
			int i1 = forecast.getTimeLocal();
			logger.d((new StringBuilder()).append("Updating forecast: cityId=").append(j).append(" date=").append(l).append(" time=").append(i1).toString());
			contentvalues.put("city_id", Integer.valueOf(j));
			contentvalues.put("date", Integer.valueOf(l));
			contentvalues.put("time", Integer.valueOf(i1));
			contentvalues.put("cloud", Integer.valueOf(forecast.getCloud()));
			contentvalues.put("precip", Integer.valueOf(forecast.getPrecip()));
			WeatherParameterValue weatherparametervalue = forecast.getMinHeatIndex();
			if (weatherparametervalue != null)
				contentvalues.put("heat_min", Integer.valueOf(((Number)weatherparametervalue.getValueInDefaultUnits()).intValue()));
			WeatherParameterValue weatherparametervalue1 = forecast.getMaxHeatIndex();
			if (weatherparametervalue1 != null)
				contentvalues.put("heat_max", Integer.valueOf(((Number)weatherparametervalue1.getValueInDefaultUnits()).intValue()));
			WeatherParameterValue weatherparametervalue2 = forecast.getMinTemp();
			if (weatherparametervalue2 != null)
				contentvalues.put("temp_min", Integer.valueOf(((Number)weatherparametervalue2.getValueInDefaultUnits()).intValue()));
			WeatherParameterValue weatherparametervalue3 = forecast.getMaxTemp();
			if (weatherparametervalue3 != null)
				contentvalues.put("temp_max", Integer.valueOf(((Number)weatherparametervalue3.getValueInDefaultUnits()).intValue()));
			WeatherParameterValue weatherparametervalue4 = forecast.getMaxHumidity();
			if (weatherparametervalue4 != null)
				contentvalues.put("humidity_max", Float.valueOf(((Number)weatherparametervalue4.getValueInDefaultUnits()).floatValue()));
			WeatherParameterValue weatherparametervalue5 = forecast.getMinHumidity();
			if (weatherparametervalue5 != null)
				contentvalues.put("humidity_min", Float.valueOf(((Number)weatherparametervalue5.getValueInDefaultUnits()).floatValue()));
			WeatherParameterValue weatherparametervalue6 = forecast.getMinPress();
			if (weatherparametervalue6 != null)
				contentvalues.put("press_min", Float.valueOf(((Number)weatherparametervalue6.getValueInDefaultUnits()).floatValue()));
			WeatherParameterValue weatherparametervalue7 = forecast.getMaxPress();
			if (weatherparametervalue7 != null)
				contentvalues.put("press_max", Float.valueOf(((Number)weatherparametervalue7.getValueInDefaultUnits()).floatValue()));
			WeatherParameterValue weatherparametervalue8 = forecast.getMinWindSpeed();
			if (weatherparametervalue8 != null)
				contentvalues.put("wind_min", Float.valueOf(((Number)weatherparametervalue8.getValueInDefaultUnits()).floatValue()));
			WeatherParameterValue weatherparametervalue9 = forecast.getMaxWindSpeed();
			if (weatherparametervalue9 != null)
				contentvalues.put("wind_max", Float.valueOf(((Number)weatherparametervalue9.getValueInDefaultUnits()).floatValue()));
			WeatherParameterValue weatherparametervalue10 = forecast.getWindDirection();
			if (weatherparametervalue10 != null)
				contentvalues.put("wind_dir", ((Number)weatherparametervalue10.getValueInDefaultUnits()).toString());
			logger.d((new StringBuilder()).append("Updating values: ").append(contentvalues.toString()).toString());
			acontentvalues[k] = contentvalues;
		}

		getContentResolver().bulkInsert(com.softspb.weather.provider.WeatherMetaData.ForecastMetaData.getContentUri(getBaseContext()), acontentvalues);
		return true;
	}

	protected  boolean onDataReceived(Object obj)
	{
		return onDataReceived((ForecastArray)obj);
	}
}
