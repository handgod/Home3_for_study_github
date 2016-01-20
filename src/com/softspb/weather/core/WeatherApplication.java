// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.os.SystemClock;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.Arrays;
import java.util.List;

public class WeatherApplication
{

	public static final String ACTION_CHANGE_PREFERENCES = "com.softspb.toshiba.weather.action.ChangePrefs";
	public static final IntentFilter FILTER_CHANGE_PREFERENCES = new IntentFilter("com.softspb.toshiba.weather.action.ChangePrefs");
	public static final String PREFERENCE_CITY_ID = "city_id";
	private static final long REGULAR_UPDATES_DELAY_MS = 0x493e0L;
	public static final int SKIN_DEFAULT = 2;
	public static final int SKIN_LARGE = 3;
	public static final int SKIN_MEDIUM = 2;
	public static final int SKIN_SIMPLE = 0;
	public static final int SKIN_SMALL_1 = 1;
	public static final int SKIN_SMALL_2 = 4;
	public static final int SKIN_XLARGE = 5;
	public static final String WEATHER_PREFERENCES = "weather_prefs";
	private static Logger logger = Loggers.getLogger(WeatherApplication.class.getName());
	public static final IntentFilter weatherUpdateStatusIntentFilter = new IntentFilter("com.softspb.weather.updateservice.action.WeatherUpdateStatus");

	public WeatherApplication()
	{
	}

	public static void cancelWeatherUpdates(Context context)
	{
		AlarmManager alarmmanager = (AlarmManager)context.getSystemService("alarm");
		alarmmanager.cancel(PendingIntent.getService(context, 0, new Intent("com.softspb.weather.updateservice.action.UpdateForecast"), 0x10000000));
		alarmmanager.cancel(PendingIntent.getService(context, 0, new Intent("com.softspb.weather.updateservice.action.UpdateCurrent"), 0x10000000));
	}

	private static Intent createUpdateCurrentIntent(int ai[], Context context)
	{
		Intent intent = new Intent("com.softspb.weather.updateservice.action.UpdateCurrent");
		intent.setPackage(context.getPackageName());
		intent.putExtra("update_ids", ai);
		return intent;
	}

	private static Intent createUpdateForecastIntent(int ai[], Context context)
	{
		Intent intent = new Intent("com.softspb.weather.updateservice.action.UpdateForecast");
		intent.setPackage(context.getPackageName());
		intent.putExtra("update_ids", ai);
		return intent;
	}

	private static int[] intArrayOnlyPositive(List list)
	{
		int i;
		int ai[];
		if (list == null)
			i = 0;
		else
			i = list.size();
		if (i == 0)
		{
			ai = new int[0];
		} else
		{
			int j = 0;
			for (int k = 0; k < i; k++)
				if (((Integer)list.get(k)).intValue() > 0)
					j++;

			ai = new int[j];
			int l = 0;
			int i1 = 0;
			while (l < i) 
			{
				int j1 = ((Integer)list.get(l)).intValue();
				int k1;
				if (j1 > 0)
				{
					k1 = i1 + 1;
					ai[i1] = j1;
				} else
				{
					k1 = i1;
				}
				l++;
				i1 = k1;
			}
		}
		return ai;
	}

	private static void logTrace()
	{
		Exception exception = new Exception();
		exception.fillInStackTrace();
		StackTraceElement astacktraceelement[] = exception.getStackTrace();
		for (int i = 1; i < astacktraceelement.length; i++)
			logd((new StringBuilder()).append("|  ").append(astacktraceelement[i]).toString());

	}

	private static void logd(String s)
	{
		logger.d(s);
	}

	public static void scheduleWeatherUpdates(long l, List list, Context context)
	{
		logd((new StringBuilder()).append("scheduleWeatherUpdates: intervalAlarmMgr=").append(l).append(" cityIds=").append(Arrays.toString(list.toArray())).toString());
		logTrace();
		scheduleWeatherUpdates(l, intArrayOnlyPositive(list), context);
	}

	private static void scheduleWeatherUpdates(long l, int ai[], Context context)
	{
		AlarmManager alarmmanager = (AlarmManager)context.getSystemService("alarm");
		PendingIntent pendingintent = PendingIntent.getService(context, 0, createUpdateForecastIntent(ai, context), 0x10000000);
		alarmmanager.setInexactRepeating(2, 0x493e0L + SystemClock.elapsedRealtime(), l, pendingintent);
		PendingIntent pendingintent1 = PendingIntent.getService(context, 0, createUpdateCurrentIntent(ai, context), 0x10000000);
		alarmmanager.setInexactRepeating(2, 0x493e0L + SystemClock.elapsedRealtime(), l, pendingintent1);
	}

	public static void updateWeather(List list, Context context)
	{
		updateWeather(intArrayOnlyPositive(list), context, false);
	}

	public static void updateWeather(List list, Context context, boolean flag)
	{
		updateWeather(intArrayOnlyPositive(list), context, flag);
	}

	private static void updateWeather(int ai[], Context context, boolean flag)
	{
		logd((new StringBuilder()).append("updateWeather: cityIds=").append(Arrays.toString(ai)).toString());
		logTrace();
		Intent intent = createUpdateCurrentIntent(ai, context);
		intent.putExtra("force_update", flag);
		logd((new StringBuilder()).append("Starting service: action=").append(intent.getAction()).append(", update_ids=").append(Arrays.toString(ai)).toString());
		context.startService(intent);
		Intent intent1 = createUpdateForecastIntent(ai, context);
		intent1.putExtra("force_update", flag);
		logd((new StringBuilder()).append("Starting service: action=").append(intent1.getAction()).append(", update_ids=").append(Arrays.toString(ai)).toString());
		context.startService(intent1);
	}

}
