// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;


// Referenced classes of package com.softspb.weather.model:
//			Forecast, WeatherParameterValue

public class ForecastBuilder
{

	private int cityId;
	private int cloud;
	private int dateLocal;
	private WeatherParameterValue maxHeatIndex;
	private WeatherParameterValue maxHumidity;
	private WeatherParameterValue maxPress;
	private WeatherParameterValue maxTemp;
	private WeatherParameterValue maxWindSpeed;
	private WeatherParameterValue minHeatIndex;
	private WeatherParameterValue minHumidity;
	private WeatherParameterValue minPress;
	private WeatherParameterValue minTemp;
	private WeatherParameterValue minWindSpeed;
	private int precip;
	private int timeLocal;
	private long timestamp;
	private WeatherParameterValue windDirection;

	public ForecastBuilder()
	{
	}

	public Forecast build()
	{
		Forecast forecast = new Forecast();
		forecast.cityId = cityId;
		forecast.dateLocal = dateLocal;
		forecast.timeLocal = timeLocal;
		forecast.cloud = cloud;
		forecast.precip = precip;
		forecast.minPress = minPress;
		forecast.maxPress = maxPress;
		forecast.minTemp = minTemp;
		forecast.maxTemp = maxTemp;
		forecast.minWindSpeed = minWindSpeed;
		forecast.maxWindSpeed = maxWindSpeed;
		forecast.windDirection = windDirection;
		forecast.minHumidity = minHumidity;
		forecast.maxHumidity = maxHumidity;
		forecast.minHeatIndex = minHeatIndex;
		forecast.maxHeatIndex = maxHeatIndex;
		forecast.timestamp = timestamp;
		return forecast;
	}

	public ForecastBuilder withCityId(int i)
	{
		cityId = i;
		return this;
	}

	public ForecastBuilder withCloudiness(int i)
	{
		cloud = i;
		return this;
	}

	public ForecastBuilder withDateLocal(int i)
	{
		dateLocal = i;
		return this;
	}

	public ForecastBuilder withMaxHeatIndexC(float f)
	{
		maxHeatIndex = WeatherParameterValue.createTemperatureCelsius((int)f);
		return this;
	}

	public ForecastBuilder withMaxHeatIndexDefaultUnits(float f)
	{
		maxHeatIndex = WeatherParameterValue.createTemperatureDefaultUnits((int)f);
		return this;
	}

	public ForecastBuilder withMaxHumidityDefaultUnits(float f)
	{
		maxHumidity = WeatherParameterValue.createRelHumidityDefaultUnits(f);
		return this;
	}

	public ForecastBuilder withMaxHumidityPercents(float f)
	{
		maxHumidity = WeatherParameterValue.createRelHumidityPercents(f);
		return this;
	}

	public ForecastBuilder withMaxPressDefaultUnits(float f)
	{
		maxPress = WeatherParameterValue.createPressureDefaultUnits(f);
		return this;
	}

	public ForecastBuilder withMaxPressMm(float f)
	{
		maxPress = WeatherParameterValue.createPressureDefaultUnits(f);
		return this;
	}

	public ForecastBuilder withMaxTempC(int i)
	{
		maxTemp = WeatherParameterValue.createTemperatureCelsius(i);
		return this;
	}

	public ForecastBuilder withMaxTempDefaultUnits(int i)
	{
		maxTemp = WeatherParameterValue.createTemperatureDefaultUnits(i);
		return this;
	}

	public ForecastBuilder withMaxWindSpeedDefaultUnits(float f)
	{
		maxWindSpeed = WeatherParameterValue.createWindSpeedDefaultUnits(f);
		return this;
	}

	public ForecastBuilder withMaxWindSpeedMps(float f)
	{
		maxWindSpeed = WeatherParameterValue.createWindSpeedMps(f);
		return this;
	}

	public ForecastBuilder withMinHeatIndexC(float f)
	{
		minHeatIndex = WeatherParameterValue.createTemperatureCelsius((int)f);
		return this;
	}

	public ForecastBuilder withMinHeatIndexDefaultUnits(float f)
	{
		minHeatIndex = WeatherParameterValue.createTemperatureDefaultUnits((int)f);
		return this;
	}

	public ForecastBuilder withMinHumidityDefaultUnits(float f)
	{
		minHumidity = WeatherParameterValue.createRelHumidityDefaultUnits(f);
		return this;
	}

	public ForecastBuilder withMinHumidityPercents(float f)
	{
		minHumidity = WeatherParameterValue.createRelHumidityPercents(f);
		return this;
	}

	public ForecastBuilder withMinPressDefaultUnits(float f)
	{
		minPress = WeatherParameterValue.createPressureDefaultUnits(f);
		return this;
	}

	public ForecastBuilder withMinPressMm(float f)
	{
		minPress = WeatherParameterValue.createPressureMm(f);
		return this;
	}

	public ForecastBuilder withMinTempC(int i)
	{
		minTemp = WeatherParameterValue.createTemperatureCelsius(i);
		return this;
	}

	public ForecastBuilder withMinTempDefaultUnits(int i)
	{
		minTemp = WeatherParameterValue.createTemperatureDefaultUnits(i);
		return this;
	}

	public ForecastBuilder withMinWindSpeedDefaultUnits(float f)
	{
		minWindSpeed = WeatherParameterValue.createWindSpeedDefaultUnits(f);
		return this;
	}

	public ForecastBuilder withMinWindSpeedMps(float f)
	{
		minWindSpeed = WeatherParameterValue.createWindSpeedMps(f);
		return this;
	}

	public ForecastBuilder withPrecipitation(int i)
	{
		precip = i;
		return this;
	}

	public ForecastBuilder withTimeLocal(int i)
	{
		timeLocal = i;
		return this;
	}

	public ForecastBuilder withTimestamp(long l)
	{
		timestamp = l;
		return this;
	}

	public ForecastBuilder withWindDirectionDefaultUnits(String s)
	{
		windDirection = WeatherParameterValue.createWindDirectionDefaultValues(s);
		return this;
	}
}
