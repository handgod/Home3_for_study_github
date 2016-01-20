// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import android.os.Bundle;
import android.preference.*;
import android.view.Window;
import com.softspb.util.IntListPreference;
import com.softspb.weather.model.WeatherParameter;

// Referenced classes of package com.softspb.weather.core:
//			WeatherPreferencesHelper

public class WeatherPreferencesActivity extends PreferenceActivity
{

	private static final WeatherParameter CONFIGURABLE_WEATHER_PARAMETERS[];
	private WeatherPreferencesHelper helper;

	public WeatherPreferencesActivity()
	{
	}

	private Preference createLaunchModeWidgetPreference()
	{
		ListPreference listpreference = new ListPreference(this);
		helper.initLaunchModeWidgetPreference(listpreference);
		return listpreference;
	}

	private Preference createUpdatePeriodPreference()
	{
		IntListPreference intlistpreference = new IntListPreference(this);
		helper.initUpdatePeriodPreference(intlistpreference);
		return intlistpreference;
	}

	private Preference createWeatherParameterUnitsPreference(WeatherParameter weatherparameter)
	{
		ListPreference listpreference = new ListPreference(this);
		helper.initWeatherParameterUnitsPreference(listpreference, weatherparameter);
		return listpreference;
	}

	protected void onCreate(Bundle bundle)
	{
		getWindow().requestFeature(1);
		super.onCreate(bundle);
		helper = new WeatherPreferencesHelper(this);
		PreferenceManager preferencemanager = getPreferenceManager();
		preferencemanager.setSharedPreferencesName("weather");
		PreferenceScreen preferencescreen = preferencemanager.createPreferenceScreen(this);
		preferencescreen.addPreference(createUpdatePeriodPreference());
		preferencescreen.addPreference(createLaunchModeWidgetPreference());
		WeatherParameter aweatherparameter[] = CONFIGURABLE_WEATHER_PARAMETERS;
		int i = aweatherparameter.length;
		for (int j = 0; j < i; j++)
			preferencescreen.addPreference(createWeatherParameterUnitsPreference(aweatherparameter[j]));

		setPreferenceScreen(preferencescreen);
	}

	protected void onDestroy()
	{
		helper.dispose();
		super.onDestroy();
	}

	static 
	{
		WeatherParameter aweatherparameter[] = new WeatherParameter[4];
		aweatherparameter[0] = WeatherParameter.TEMPERATURE;
		aweatherparameter[1] = WeatherParameter.PRESSURE;
		aweatherparameter[2] = WeatherParameter.WIND_DIRECTION;
		aweatherparameter[3] = WeatherParameter.WIND_SPEED;
		CONFIGURABLE_WEATHER_PARAMETERS = aweatherparameter;
	}
}
