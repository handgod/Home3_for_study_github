// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.updateservice;

import android.content.*;
import com.softspb.updateservice.DownloadClient;
import com.softspb.util.log.Logger;
import com.softspb.weather.model.CurrentConditions;
import com.softspb.weather.model.WeatherParameterValue;

// Referenced classes of package com.softspb.weather.updateservice:
//			WeatherUpdateService

public abstract class CurrentUpdateService extends WeatherUpdateService
{

	public static final String ACTION_UPDATE = "com.softspb.weather.updateservice.action.UpdateCurrent";

	public CurrentUpdateService()
	{
	}

	protected abstract DownloadClient createDownloadClient(Context context);

	protected int getUpdateType()
	{
		return 1;
	}

	protected boolean onDataReceived(CurrentConditions currentconditions)
	{
		logger.d("Current data received.");
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("city_id", Integer.valueOf(currentconditions.getCityId()));
		contentvalues.put("date", Integer.valueOf(currentconditions.getDateUTC()));
		contentvalues.put("time", Integer.valueOf(currentconditions.getTimeUTC()));
		WeatherParameterValue weatherparametervalue = currentconditions.getTemp();
		if (weatherparametervalue != null)
			contentvalues.put("temp", Integer.valueOf(((Number)weatherparametervalue.getValueInDefaultUnits()).intValue()));
		WeatherParameterValue weatherparametervalue1 = currentconditions.getPressure();
		if (weatherparametervalue1 != null)
			contentvalues.put("pressure", Float.valueOf(((Number)weatherparametervalue1.getValueInDefaultUnits()).floatValue()));
		contentvalues.put("sky_icon", Integer.valueOf(currentconditions.getSkyIcon()));
		contentvalues.put("station", currentconditions.getLocation());
		WeatherParameterValue weatherparametervalue2 = currentconditions.getWindDirection();
		if (weatherparametervalue2 != null)
			contentvalues.put("wind_dir", ((Number)weatherparametervalue2.getValueInDefaultUnits()).toString());
		WeatherParameterValue weatherparametervalue3 = currentconditions.getWindSpeed();
		if (weatherparametervalue3 != null)
			contentvalues.put("wind_speed", Float.valueOf(((Number)weatherparametervalue3.getValueInDefaultUnits()).floatValue()));
		WeatherParameterValue weatherparametervalue4 = currentconditions.getRelHumidity();
		if (weatherparametervalue4 != null)
			contentvalues.put("humidity", Float.valueOf(((Number)weatherparametervalue4.getValueInDefaultUnits()).floatValue()));
		WeatherParameterValue weatherparametervalue5 = currentconditions.getDewPoint();
		if (weatherparametervalue5 != null)
			contentvalues.put("dew_point", Float.valueOf(((Number)weatherparametervalue5.getValueInDefaultUnits()).floatValue()));
		getContentResolver().insert(com.softspb.weather.provider.WeatherMetaData.CurrentMetaData.getContentUri(getBaseContext()), contentvalues);
		return true;
	}

	protected  boolean onDataReceived(Object obj)
	{
		return onDataReceived((CurrentConditions)obj);
	}
}
