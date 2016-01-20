// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.updateservice;

import android.content.*;
import com.softspb.updateservice.UpdateService;
import com.softspb.util.log.Logger;

public abstract class WeatherUpdateService extends UpdateService
{

	public static final String ACTION_WEATHER_UPDATE_STATUS = "com.softspb.weather.updateservice.action.WeatherUpdateStatus";
	private static final String SHARED_PREFERENCES_NAME = "weather_update_service";
	public static final String WEATHER_UPDATE_ID = "weather-update-id";
	public static final String WEATHER_UPDATE_STATUS = "weather-update-status";
	public static final int WEATHER_UPDATE_STATUS_DOWNLOADING = 1;
	public static final int WEATHER_UPDATE_STATUS_IDLE = 2;
	public static final String WEATHER_UPDATE_TAG = "weather-update-tag";
	private static long nextUpdateId = 1L;
	long updateSessionId;
	private final int updateType = getUpdateType();

	public WeatherUpdateService()
	{
		updateSessionId = -1L;
	}

	public static long sendWeatherUpdateStatusDownloading(int i, Context context, String s)
	{

		long l;
		l = nextUpdateId;
		nextUpdateId = 1L + l;

		Intent intent = new Intent("com.softspb.weather.updateservice.action.WeatherUpdateStatus");
		intent.putExtra("city_id", i);
		intent.putExtra("weather-update-status", 1);
		intent.putExtra("weather-update-tag", s);
		intent.putExtra("weather-update-id", l);
		context.sendBroadcast(intent);
		return l;

	}

	public static void sendWeatherUpdateStatusIdle(int i, Context context, String s, long l)
	{
		Intent intent = new Intent("com.softspb.weather.updateservice.action.WeatherUpdateStatus");
		intent.putExtra("city_id", i);
		intent.putExtra("weather-update-status", 2);
		intent.putExtra("weather-update-tag", s);
		intent.putExtra("weather-update-id", l);
		context.sendBroadcast(intent);
	}

	protected SharedPreferences getSharedPreferences()
	{
		return getSharedPreferences("weather_update_service", 0);
	}

	protected abstract int getUpdateType();

	protected void onFinishedUpdate()
	{
		if (updateSessionId != -1L)
			sendWeatherUpdateStatusIdle(0, getBaseContext(), TAG, updateSessionId);
	}

	protected void onFinishedUpdateId(int i, boolean flag)
	{
		int j;
		if (flag)
			j = 1;
		else
			j = 999;
		updateStatus(i, j);
	}

	protected void onStartedUpdate()
	{
		logger.d("Update event, broadcasting update in progress...");
		updateSessionId = sendWeatherUpdateStatusDownloading(0, getBaseContext(), TAG);
	}

	protected void onStartedUpdateId(int i)
	{
		updateStatus(i, 2);
	}

	protected void updateStatus(int i, int j)
	{
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("city_id", Integer.valueOf(i));
		contentvalues.put("status", Integer.valueOf(j));
		contentvalues.put("type", Integer.valueOf(updateType));
		getContentResolver().insert(com.softspb.weather.provider.WeatherMetaData.UpdateStatusMetaData.getContentUri(getBaseContext()), contentvalues);
	}

}
