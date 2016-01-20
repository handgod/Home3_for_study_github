// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;

import android.content.Context;

// Referenced classes of package com.softspb.weather.model:
//			WeatherParameter

public class WeatherParameterValue
{

	static final String WIND_DIRECTION_VARIABLE_CODE = "VRB";
	private int mUnits;
	private Object mValue;
	private WeatherParameter paramDescr;

	private WeatherParameterValue(Object obj, WeatherParameter weatherparameter, int i)
	{
		mValue = obj;
		paramDescr = weatherparameter;
		mUnits = i;
	}

	public static WeatherParameterValue createDewPointCelsius(int i)
	{
		return new WeatherParameterValue(Integer.valueOf(i), WeatherParameter.DEW_POINT, 0);
	}

	public static WeatherParameterValue createDewPointDefaultUnits(int i)
	{
		return new WeatherParameterValue(Integer.valueOf(i), WeatherParameter.DEW_POINT, WeatherParameter.TEMPERATURE.getDefaultUnits());
	}

	public static WeatherParameterValue createDewPointFahrenheit(int i)
	{
		return new WeatherParameterValue(Integer.valueOf(i), WeatherParameter.DEW_POINT, 1);
	}

	public static WeatherParameterValue createPressureAtm(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.PRESSURE, 2);
	}

	public static WeatherParameterValue createPressureDefaultUnits(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.PRESSURE, WeatherParameter.PRESSURE.getDefaultUnits());
	}

	public static WeatherParameterValue createPressureHpa(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.PRESSURE, 3);
	}

	public static WeatherParameterValue createPressureIn(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.PRESSURE, 1);
	}

	public static WeatherParameterValue createPressureMm(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.PRESSURE, 0);
	}

	public static WeatherParameterValue createRelHumidityDefaultUnits(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.RELATIVE_HUMIDITY, WeatherParameter.RELATIVE_HUMIDITY.getDefaultUnits());
	}

	public static WeatherParameterValue createRelHumidityPercents(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.RELATIVE_HUMIDITY, 0);
	}

	public static WeatherParameterValue createTemperatureCelsius(int i)
	{
		return new WeatherParameterValue(Integer.valueOf(i), WeatherParameter.TEMPERATURE, 0);
	}

	public static WeatherParameterValue createTemperatureDefaultUnits(int i)
	{
		return new WeatherParameterValue(Integer.valueOf(i), WeatherParameter.TEMPERATURE, WeatherParameter.TEMPERATURE.getDefaultUnits());
	}

	public static WeatherParameterValue createTemperatureFahrenheit(int i)
	{
		return new WeatherParameterValue(Integer.valueOf(i), WeatherParameter.TEMPERATURE, 1);
	}

	static WeatherParameterValue createWindDirection(String s, int i)
	{
		Double double1;
		double1 = Double.valueOf((0.0D / 0.0D));
		if (!(s == null || "VRB".equals(s)))
		{	
			Double double2 = Double.valueOf(Double.parseDouble(s));
			double1 = double2;
		}
		return new WeatherParameterValue(double1, WeatherParameter.WIND_DIRECTION, i);
	}

	public static WeatherParameterValue createWindDirectionDefaultValues(String s)
	{
		return createWindDirection(s, 1);
	}

	public static WeatherParameterValue createWindDirectionDegrees(double d)
	{
		return new WeatherParameterValue(Double.valueOf(d), WeatherParameter.WIND_DIRECTION, 2);
	}

	public static WeatherParameterValue createWindDirectionDegrees(String s)
	{
		return createWindDirection(s, 2);
	}

	public static WeatherParameterValue createWindSpeedDefaultUnits(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.WIND_SPEED, WeatherParameter.WIND_SPEED.getDefaultUnits());
	}

	public static WeatherParameterValue createWindSpeedKmph(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.WIND_SPEED, 3);
	}

	public static WeatherParameterValue createWindSpeedKnots(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.WIND_SPEED, 1);
	}

	public static WeatherParameterValue createWindSpeedMph(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.WIND_SPEED, 0);
	}

	public static WeatherParameterValue createWindSpeedMps(float f)
	{
		return new WeatherParameterValue(Float.valueOf(f), WeatherParameter.WIND_SPEED, 2);
	}

	public String format(int i, Context context)
	{
		Object obj;
		if (i == mUnits)
			obj = mValue;
		else
			obj = paramDescr.convert(mValue, mUnits, i);
		return paramDescr.format(obj, i, context);
	}

	public int getInt1000(int i)
	{
		Object obj = getValue(i);
		int j;
		if (obj instanceof Integer)
			j = 1000 * ((Integer)obj).intValue();
		else
		if (obj instanceof Number)
			j = (int)Math.round(1000D * ((Number)obj).doubleValue());
		else
			throw new NumberFormatException((new StringBuilder()).append("Weather parameter value is not integer: ").append(this).toString());
		return j;
	}

	public Object getValue(int i)
	{
		Object obj;
		if (i == mUnits)
			obj = mValue;
		else
			obj = paramDescr.convert(mValue, mUnits, i);
		return obj;
	}

	public Object getValueInDefaultUnits()
	{
		int i = paramDescr.getDefaultUnits();
		Object obj;
		if (mUnits == i)
			obj = mValue;
		else
			obj = paramDescr.convert(mValue, mUnits, i);
		return obj;
	}

	public WeatherParameter getWeatherParameter()
	{
		return paramDescr;
	}
}
