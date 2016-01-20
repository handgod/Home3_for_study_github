// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import android.content.Context;
import com.softspb.util.CrossProcessusPreferences;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.*;

// Referenced classes of package com.softspb.weather.core:
//			WeatherPreferences, WeatherApplicationPreferences

public class WeatherWidgetPreferences extends CrossProcessusPreferences
	implements WeatherPreferences
{

	public static final String PREFERENCE_SKIN = "weather-skin";
	public static final int SKIN_NOT_SET = 0x80000000;
	private static final String WIDGET_PREFS_NAME_PREFIX = "weather-widget-";
	private static final Logger logger = Loggers.getLogger(WeatherWidgetPreferences.class.getName());
	private String mSharedPrefsName;
	private WeatherApplicationPreferences sharedWeatherPrefs;
	private int widgetId;

	public WeatherWidgetPreferences(Context context, int i)
	{
		super(context, context.getPackageName(), (new StringBuilder()).append("weather-widget-").append(i).toString());
		mSharedPrefsName = (new StringBuilder()).append("weather-widget-").append(i).toString();
		widgetId = i;
		sharedWeatherPrefs = new WeatherApplicationPreferences(context);
		logd("WeatherWidgetPreferences constructor:");
		logd((new StringBuilder()).append("   widgetId=").append(i).toString());
		logd((new StringBuilder()).append("   context=").append(context.getPackageName()).toString());
		logd((new StringBuilder()).append("   ClassLoader=").append(getClass().getClassLoader()).toString());
		logd((new StringBuilder()).append("   this=").append(this).toString());
		logPreferences();
	}

	public void dispose()
	{
		super.dispose();
		sharedWeatherPrefs.dispose();
	}

	public int getCurrentCityId()
	{
		return getInt("weather-current-city-id", 0x80000000);
	}

	public int getCurrentLocationCityId()
	{
		return sharedWeatherPrefs.getCurrentLocationCityId();
	}

	public int getLastUsedScreen()
	{
		return getInt("weather-last-used-screen", -1);
	}

	public int getLaunchMode()
	{
		return sharedWeatherPrefs.getLaunchMode();
	}

	public String getSharedPreferencesName()
	{
		return mSharedPrefsName;
	}

	public int getSkin()
	{
		int i = getInt("weather-skin", 0x80000000);
		logd((new StringBuilder()).append("getSkin: skin=").append(i).toString());
		return i;
	}

	public int getWidgetId()
	{
		return widgetId;
	}

	protected void logPreferences()
	{
		logd("--- Available preferences: ---");
		java.util.Map.Entry entry;
		for (Iterator iterator = getAll().entrySet().iterator(); iterator.hasNext(); logd((new StringBuilder()).append("    ").append((String)entry.getKey()).append("=").append(entry.getValue()).toString()))
			entry = (java.util.Map.Entry)iterator.next();

		logd("---------- end ---------------");
	}

	protected void logd(String s)
	{
		(new StringBuilder()).append("(widgetId=").append(widgetId).append(") ").append(s).toString();
		logger.d(s);
	}

	public void setCurrentCityId(int i)
	{
		logd((new StringBuilder()).append("setCurrentCityId: cityId=").append(i).toString());
		edit().putInt("weather-current-city-id", i).commit();
		logd((new StringBuilder()).append("Result: currentId=").append(getCurrentCityId()).toString());
	}

	public void setLastUsedScreen(int i)
	{
		edit().putInt("weather-last-used-screen", i).commit();
	}

	public void setSkin(int i)
	{
		logd((new StringBuilder()).append("setSkin: skin=").append(i).toString());
		edit().putInt("weather-skin", i).commit();
	}

}
