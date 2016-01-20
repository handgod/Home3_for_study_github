// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;


public interface WeatherPreferences
{

	public static final int LAUNCH_MODE_CURRENT = 1;
	public static final int LAUNCH_MODE_DEFAULT = 2;
	public static final int LAUNCH_MODE_FORECAST = 0;
	public static final int LAUNCH_MODE_LAST_USED = 2;
	public static final String PREFERENCE_CURRENT_CITY_ID = "weather-current-city-id";
	public static final String PREFERENCE_LAST_USED_SCREEN = "weather-last-used-screen";
	public static final int SCREEN_CURRENT = 2;
	public static final int SCREEN_FORECAST = 1;

	public abstract int getCurrentCityId();

	public abstract int getCurrentLocationCityId();

	public abstract int getLastUsedScreen();

	public abstract int getLaunchMode();

	public abstract void setCurrentCityId(int i);

	public abstract void setLastUsedScreen(int i);
}
