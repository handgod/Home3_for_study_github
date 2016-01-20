// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import android.content.Context;
import android.content.res.Resources;
import com.softspb.updateservice.UpdatePreferences;
import com.softspb.util.CrossProcessusPreferences;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.softspb.weather.model.WeatherParameter;
import java.util.*;

// Referenced classes of package com.softspb.weather.core:
//			WeatherPreferences, WeatherParameterPreferences, ScheduleInfo

public class WeatherApplicationPreferences extends CrossProcessusPreferences
	implements WeatherPreferences, WeatherParameterPreferences, UpdatePreferences
{

	private static final long DEFAULT_UPDATE_PERIOD = 0x6ddd00L;
	public static final String PREFERENCE_ALL_CITY_IDS = "weather-all-city-ids";
	public static final String PREFERENCE_LAUNCH_MODE_WIDGET = "weather-launch-mode-widget";
	public static final String PREFERENCE_LOCAL_CITY_ID = "weather-local-city-id";
	public static final String PREFERENCE_PREFERRED_WIDGET_ID = "weather-preferred-widget-id";
	public static final String PREFERENCE_SCHEDULED_CITY_IDS = "weather-scheduled-ids";
	public static final String PREFERENCE_SCHEDULED_INTERVAL = "weather-scheduled-interval";
	public static final String PREFERENCE_SCHEDULED_TIMESTAMP_TOKEN = "weather-scheduled-timestamp-token";
	public static final String WEATHER_PREFERENCES_NAME = "weather";
	public static final int WIDGET_ID_NOT_SET = 0x80000000;
	private static final Logger logger = Loggers.getLogger(WeatherApplicationPreferences.class.getName());

	public WeatherApplicationPreferences(Context context)
	{
		super(context, context.getPackageName(), "weather");
	}

	private ArrayList loadCityIds()
	{
		return parseCommaSeparatedInts(getString("weather-all-city-ids", ""));
	}

	private static void logd(String s)
	{
		logger.d(s);
	}

	private ArrayList parseCommaSeparatedInts(String s)
	{
		ArrayList arraylist = new ArrayList();
		String as[] = s.split(",");
		int i = as.length;
		int j = 0;
		while (j < i) 
		{
			String s1 = as[j];
			try
			{
				arraylist.add(Integer.valueOf(Integer.parseInt(s1)));
			}
			catch (NumberFormatException numberformatexception) { }
			j++;
		}
		return arraylist;
	}

	private void saveCityIds(ArrayList arraylist)
	{
		edit().putString("weather-all-city-ids", toCommaSeparatedLine(arraylist)).commit();
	}

	private String toCommaSeparatedLine(List list)
	{
		StringBuilder stringbuilder = new StringBuilder();
		Integer integer;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); stringbuilder.append(integer))
		{
			integer = (Integer)iterator.next();
			if (stringbuilder.length() > 0)
				stringbuilder.append(",");
		}

		return stringbuilder.toString();
	}

	public void addCityId(int i)
	{
		logd((new StringBuilder()).append("addCityId: cityId=").append(i).toString());
		ArrayList arraylist = loadCityIds();
		if (!arraylist.contains(Integer.valueOf(i)))
		{
			arraylist.add(Integer.valueOf(i));
			saveCityIds(arraylist);
		}
		logd((new StringBuilder()).append("Result: allIds=").append(Arrays.toString(getAllCityIds().toArray())).append(", currentId=").append(i).toString());
	}

	public void addCityId(int i, int j)
	{
		logd((new StringBuilder()).append("addCityId: cityId=").append(i).append(", afterCityId=").append(j).toString());
		ArrayList arraylist = loadCityIds();
		if (!arraylist.contains(Integer.valueOf(i)))
		{
			arraylist.add(1 + arraylist.indexOf(Integer.valueOf(j)), Integer.valueOf(i));
			saveCityIds(arraylist);
		}
	}

	public List getAllCityIds()
	{
		return loadCityIds();
	}

	public int getCurrentCityId()
	{
		return getInt("weather-current-city-id", 0x80000000);
	}

	public int getCurrentLocationCityId()
	{
		return getInt("weather-local-city-id", 0x80000000);
	}

	public long getDefaultUpdateInterval(Context context)
	{
		int i = context.getResources().getInteger(R.integer.weather_default_update_period);
		long l = i;
		return l;
	}

	public int getLastUsedScreen()
	{
		return 1;
	}

	public int getLaunchMode()
	{
		String s = getString("weather-launch-mode-widget", null);
		int i;
		if (s == null)
			i = 2;
		else
			i = Integer.parseInt(s);
		return i;
	}

	public int getPreferredWidgetId()
	{
		int i = getInt("weather-preferred-widget-id", 0x80000000);
		logd((new StringBuilder()).append("getPreferredWidgetId: returning ").append(i).toString());
		return i;
	}

	public ScheduleInfo getScheduleInfo()
	{
		logd("getSchedultInfo");
		ScheduleInfo scheduleinfo = null;
		String s = getString("weather-scheduled-ids", null);
		if (s != null)
		{
			scheduleinfo = new ScheduleInfo();
			scheduleinfo.scheduledIds = parseCommaSeparatedInts(s);
			scheduleinfo.scheduledInterval = getLong("weather-scheduled-interval", 0L);
			scheduleinfo.scheduledTimestampToken = getLong("weather-scheduled-timestamp-token", 0L);
		}
		logd((new StringBuilder()).append("getScheduleInfo: info=").append(scheduleinfo).toString());
		return scheduleinfo;
	}

	public int getUnits(WeatherParameter weatherparameter)
	{
		String s;
		s = getString(weatherparameter.getName(), null);
		if (s == null)
			s = weatherparameter.getInitialUnits(targetContext);
		int j = Integer.parseInt(s);
		int i = j;
		return i;
	}

	public long getUpdateIntervalMs()
	{
		logd("getUpdateIntervalMs >>>");
		long l = getLong("update-interval", -1L);
		if (l == -1L)
			l = getDefaultUpdateInterval(targetContext);
		logd((new StringBuilder()).append("getUpdateIntervalMs <<< ").append(l).toString());
		return l;
	}

	public boolean isUseOnlyWifi()
	{
		logd("isUseOnlyWifi >>>");
		boolean flag = getBoolean("use-only-wifi", false);
		logd((new StringBuilder()).append("isUseOnlyWifi <<< ").append(flag).toString());
		return flag;
	}

	public void removeCityId(int i)
	{
		logd((new StringBuilder()).append("removeCityId: cityId=").append(i).toString());
		ArrayList arraylist = loadCityIds();
		int j = arraylist.indexOf(Integer.valueOf(i));
		if (j != -1)
		{
			arraylist.remove(j);
			saveCityIds(arraylist);
		}
	}

	public void removeScheduleInfo()
	{
		android.content.SharedPreferences.Editor editor = edit();
		editor.remove("weather-scheduled-ids");
		editor.remove("weather-scheduled-interval");
		editor.remove("weather-scheduled-timestamp-token");
		editor.commit();
	}

	public void setCurrentCityId(int i)
	{
		logd((new StringBuilder()).append("setCurrentCityId: cityId=").append(i).toString());
		edit().putInt("weather-current-city-id", i).commit();
		logd((new StringBuilder()).append("Result: currentId=").append(getCurrentCityId()).toString());
	}

	public void setLastUsedScreen(int i)
	{
	}

	public void setPreferredWidgetId(int i)
	{
		logd((new StringBuilder()).append("setPreferredWidgetId: widgetId=").append(i).toString());
		edit().putInt("weather-preferred-widget-id", i).commit();
	}

	public void setScheduleInfo(ScheduleInfo scheduleinfo)
	{
		logd((new StringBuilder()).append("setScheduleInfo: ").append(scheduleinfo).toString());
		android.content.SharedPreferences.Editor editor = edit();
		editor.putString("weather-scheduled-ids", toCommaSeparatedLine(scheduleinfo.scheduledIds));
		editor.putLong("weather-scheduled-interval", scheduleinfo.scheduledInterval);
		editor.putLong("weather-scheduled-timestamp-token", scheduleinfo.scheduledTimestampToken);
		editor.commit();
	}

	public void setUnits(WeatherParameter weatherparameter, int i)
	{
		android.content.SharedPreferences.Editor editor = edit();
		editor.putInt(weatherparameter.getName(), i);
		editor.commit();
	}

	public void setUpdateIntervalMs(long l)
	{
		logd((new StringBuilder()).append("setUpdateIntervalMs: ").append(l).toString());
		edit().putLong("update-interval", l).commit();
	}

	public void setUseOnlyWifi(boolean flag)
	{
		logd((new StringBuilder()).append("setUseOnlyWifi: ").append(flag).toString());
		edit().putBoolean("use-only-wifi", flag).commit();
	}

	public void setValue(String s, Object obj)
	{
		String s1 = getString(s, null);
		String s2 = obj.toString();
		if (s2 != null && !s2.equals(s1))
			edit().putString(s, obj.toString()).commit();
	}

}
