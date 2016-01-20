// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import android.content.Context;
import android.content.res.Resources;
import android.preference.*;
import com.softspb.updateservice.UpdateService;
import com.softspb.weather.model.WeatherParameter;

// Referenced classes of package com.softspb.weather.core:
//			WeatherApplicationPreferences

public class WeatherPreferencesHelper
	implements android.preference.Preference.OnPreferenceChangeListener
{

	private final WeatherApplicationPreferences appPrefs;
	private final PreferenceActivity prefActivity;

	public WeatherPreferencesHelper(PreferenceActivity preferenceactivity)
	{
		prefActivity = preferenceactivity;
		appPrefs = new WeatherApplicationPreferences(preferenceactivity);
	}

	public void dispose()
	{
		appPrefs.dispose();
	}

	public void initLaunchModeWidgetPreference(ListPreference listpreference)
	{
		listpreference.setTitle(R.string.weather_config_launch_mode_widget_title);
		listpreference.setSummary(prefActivity.getResources().getStringArray(R.array.weather_launch_mode_choices)[appPrefs.getLaunchMode()]);
		listpreference.setDefaultValue(Integer.toString(2));
		listpreference.setKey("weather-launch-mode-widget");
		listpreference.setEntries(R.array.weather_launch_mode_choices);
		listpreference.setEntryValues(R.array.weather_launch_mode_values);
		listpreference.setOnPreferenceChangeListener(this);
		listpreference.setEnabled(true);
	}

	public void initUpdatePeriodPreference(ListPreference listpreference)
	{
		PreferenceActivity preferenceactivity = prefActivity;
		listpreference.setTitle(R.string.weather_config_update_period_title);
		String s = Long.toString(appPrefs.getUpdateIntervalMs());
		listpreference.setEntryValues(R.array.weather_update_period_values);
		int i = listpreference.findIndexOfValue(s);
		listpreference.setSummary(preferenceactivity.getResources().getStringArray(R.array.weather_update_period_choices)[i]);
		listpreference.setDefaultValue(Long.toString(appPrefs.getDefaultUpdateInterval(preferenceactivity)));
		listpreference.setKey("update-interval");
		listpreference.setEntries(R.array.weather_update_period_choices);
		listpreference.setOnPreferenceChangeListener(this);
		listpreference.setEnabled(true);
	}

	public void initWeatherParameterUnitsPreference(ListPreference listpreference, WeatherParameter weatherparameter)
	{
		boolean flag = true;
		PreferenceActivity preferenceactivity = prefActivity;
		String as[] = weatherparameter.getUnits(preferenceactivity);
		String as1[] = weatherparameter.getUnitValues(preferenceactivity);
		listpreference.setTitle(weatherparameter.getUnitsTitle(preferenceactivity));
		listpreference.setSummary(as[appPrefs.getUnits(weatherparameter)]);
		listpreference.setDefaultValue(weatherparameter.getInitialUnits(preferenceactivity));
		listpreference.setKey(weatherparameter.getName());
		listpreference.setEntries(as);
		listpreference.setEntryValues(as1);
		listpreference.setOnPreferenceChangeListener(this);
		if (as1.length <= 1)
			flag = false;
		listpreference.setEnabled(flag);
	}

	public void initWifiPreference(CheckBoxPreference checkboxpreference)
	{
		checkboxpreference.setChecked(appPrefs.isUseOnlyWifi());
		checkboxpreference.setTitle(R.string.weather_use_only_wifi_title);
		checkboxpreference.setSummaryOn(0x1040013);
		checkboxpreference.setSummaryOff(0x1040009);
		checkboxpreference.setOnPreferenceChangeListener(this);
		checkboxpreference.setEnabled(true);
	}

	public boolean onPreferenceChange(Preference preference, Object obj)
	{
		String s;
		if (preference instanceof ListPreference)
		{
			ListPreference listpreference = (ListPreference)preference;
			int i = listpreference.findIndexOfValue((String)obj);
			listpreference.setSummary(listpreference.getEntries()[i]);
		}
		s = preference.getKey();
		if (!"update-interval".equals(s)) 
			{
				if ("use-only-wifi".equals(s))
				{
					boolean flag = appPrefs.isUseOnlyWifi();
					UpdateService.setUseOnlyWifiPreference(prefActivity, flag);
					appPrefs.setUseOnlyWifi(flag);
				} else
				if (preference instanceof ListPreference)
					appPrefs.setValue(s, obj);
			}
		else 
			{
				((ListPreference)preference).setValue((String)obj);
				long l = Long.valueOf((String)obj).longValue();
				appPrefs.setUpdateIntervalMs(l);
			}
		return true;
	}
}
