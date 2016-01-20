// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;


// Referenced classes of package com.softspb.weather.model:
//			CurrentConditions, WeatherParameterValue

public class CurrentConditionsBuilder
{

	private String airportICAOCode;
	private int cityId;
	private String dataSource;
	private int dateUTC;
	private WeatherParameterValue dewPoint;
	private WeatherParameterValue heatIndex;
	private WeatherParameterValue humidex;
	private String latitude;
	private String location;
	private String longitude;
	private String metar;
	private WeatherParameterValue press;
	private WeatherParameterValue relHumidity;
	private int skyIcon;
	private WeatherParameterValue temp;
	private int timeUTC;
	private long timestamp;
	private WeatherParameterValue visibility;
	private String weather;
	private WeatherParameterValue windChill;
	private WeatherParameterValue windDir;
	private WeatherParameterValue windSpeed;

	public CurrentConditionsBuilder()
	{
	}

	public CurrentConditions build()
	{
		CurrentConditions currentconditions = new CurrentConditions();
		currentconditions.cityId = cityId;
		currentconditions.location = location;
		currentconditions.dateUTC = dateUTC;
		currentconditions.timeUTC = timeUTC;
		currentconditions.skyIcon = skyIcon;
		currentconditions.temp = temp;
		currentconditions.press = press;
		currentconditions.windSpeed = windSpeed;
		currentconditions.relHumidity = relHumidity;
		currentconditions.windDir = windDir;
		currentconditions.dewPoint = dewPoint;
		currentconditions.heatIndex = heatIndex;
		currentconditions.windChill = windChill;
		currentconditions.visibility = visibility;
		currentconditions.humidex = humidex;
		currentconditions.timestamp = timestamp;
		currentconditions.dataSource = dataSource;
		currentconditions.airportICAOCode = airportICAOCode;
		currentconditions.latitude = latitude;
		currentconditions.longitude = longitude;
		currentconditions.metar = metar;
		currentconditions.weather = weather;
		return currentconditions;
	}

	public CurrentConditionsBuilder withAirportICAOCode(String s)
	{
		airportICAOCode = s;
		return this;
	}

	public CurrentConditionsBuilder withCityId(int i)
	{
		cityId = i;
		return this;
	}

	public CurrentConditionsBuilder withDataSource(String s)
	{
		dataSource = s;
		return this;
	}

	public CurrentConditionsBuilder withDateUTC(int i)
	{
		dateUTC = i;
		return this;
	}

	public CurrentConditionsBuilder withDewPointC(int i)
	{
		dewPoint = WeatherParameterValue.createDewPointCelsius(i);
		return this;
	}

	public CurrentConditionsBuilder withDewPointDefaultUnits(int i)
	{
		dewPoint = WeatherParameterValue.createDewPointDefaultUnits(i);
		return this;
	}

	public CurrentConditionsBuilder withDewPointF(int i)
	{
		dewPoint = WeatherParameterValue.createDewPointFahrenheit(i);
		return this;
	}

	public CurrentConditionsBuilder withHeatIndexC(int i)
	{
		heatIndex = WeatherParameterValue.createTemperatureCelsius(i);
		return this;
	}

	public CurrentConditionsBuilder withHeatIndexDefaultUnits(int i)
	{
		heatIndex = WeatherParameterValue.createTemperatureDefaultUnits(i);
		return this;
	}

	public CurrentConditionsBuilder withHeatIndexF(int i)
	{
		heatIndex = WeatherParameterValue.createTemperatureFahrenheit(i);
		return this;
	}

	public CurrentConditionsBuilder withHumidexC(int i)
	{
		humidex = WeatherParameterValue.createTemperatureCelsius(i);
		return this;
	}

	public CurrentConditionsBuilder withHumidexDefaultUnits(int i)
	{
		humidex = WeatherParameterValue.createTemperatureFahrenheit(i);
		return this;
	}

	public CurrentConditionsBuilder withHumidexF(int i)
	{
		humidex = WeatherParameterValue.createTemperatureFahrenheit(i);
		return this;
	}

	public CurrentConditionsBuilder withLatitude(String s)
	{
		latitude = s;
		return this;
	}

	public CurrentConditionsBuilder withLocation(String s)
	{
		location = s;
		return this;
	}

	public CurrentConditionsBuilder withLongitude(String s)
	{
		longitude = s;
		return this;
	}

	public CurrentConditionsBuilder withMetar(String s)
	{
		metar = s;
		return this;
	}

	public CurrentConditionsBuilder withPressureAtm(float f)
	{
		press = WeatherParameterValue.createPressureAtm(f);
		return this;
	}

	public CurrentConditionsBuilder withPressureDefaultUnits(float f)
	{
		press = WeatherParameterValue.createPressureDefaultUnits(f);
		return this;
	}

	public CurrentConditionsBuilder withPressureHpa(float f)
	{
		press = WeatherParameterValue.createPressureHpa(f);
		return this;
	}

	public CurrentConditionsBuilder withPressureIn(float f)
	{
		press = WeatherParameterValue.createPressureIn(f);
		return this;
	}

	public CurrentConditionsBuilder withPressureMm(float f)
	{
		press = WeatherParameterValue.createPressureMm(f);
		return this;
	}

	public CurrentConditionsBuilder withRelativeHumidityDefaultUnits(float f)
	{
		relHumidity = WeatherParameterValue.createRelHumidityDefaultUnits(f);
		return this;
	}

	public CurrentConditionsBuilder withRelativeHumidityPercents(float f)
	{
		relHumidity = WeatherParameterValue.createRelHumidityPercents(f);
		return this;
	}

	public CurrentConditionsBuilder withSkyIcon(int i)
	{
		skyIcon = i;
		return this;
	}

	public CurrentConditionsBuilder withTempC(int i)
	{
		temp = WeatherParameterValue.createTemperatureCelsius(i);
		return this;
	}

	public CurrentConditionsBuilder withTempDefaultUnits(int i)
	{
		temp = WeatherParameterValue.createTemperatureDefaultUnits(i);
		return this;
	}

	public CurrentConditionsBuilder withTempF(int i)
	{
		temp = WeatherParameterValue.createTemperatureFahrenheit(i);
		return this;
	}

	public CurrentConditionsBuilder withTimeUTC(int i)
	{
		timeUTC = i;
		return this;
	}

	public CurrentConditionsBuilder withTimestamp(long l)
	{
		timestamp = l;
		return this;
	}

	public CurrentConditionsBuilder withVisibility(int i)
	{
		throw new UnsupportedOperationException("TODO");
	}

	public CurrentConditionsBuilder withWeather(String s)
	{
		weather = s;
		return this;
	}

	public CurrentConditionsBuilder withWindChillC(int i)
	{
		windChill = WeatherParameterValue.createTemperatureCelsius(i);
		return this;
	}

	public CurrentConditionsBuilder withWindChillDefaultUnits(int i)
	{
		windChill = WeatherParameterValue.createTemperatureDefaultUnits(i);
		return this;
	}

	public CurrentConditionsBuilder withWindChillF(int i)
	{
		windChill = WeatherParameterValue.createTemperatureFahrenheit(i);
		return this;
	}

	public CurrentConditionsBuilder withWindDirDefaultUnits(String s)
	{
		windDir = WeatherParameterValue.createWindDirectionDefaultValues(s);
		return this;
	}

	public CurrentConditionsBuilder withWindDirDegrees(String s)
	{
		windDir = WeatherParameterValue.createWindDirectionDegrees(s);
		return this;
	}

	public CurrentConditionsBuilder withWindSpeedDefaultUnits(float f)
	{
		windSpeed = WeatherParameterValue.createWindSpeedDefaultUnits(f);
		return this;
	}

	public CurrentConditionsBuilder withWindSpeedKmph(float f)
	{
		windSpeed = WeatherParameterValue.createWindSpeedKmph(f);
		return this;
	}

	public CurrentConditionsBuilder withWindSpeedKnots(float f)
	{
		windSpeed = WeatherParameterValue.createWindSpeedKnots(f);
		return this;
	}

	public CurrentConditionsBuilder withWindSpeedMph(float f)
	{
		windSpeed = WeatherParameterValue.createWindSpeedMph(f);
		return this;
	}

	public CurrentConditionsBuilder withWindSpeedMps(float f)
	{
		windSpeed = WeatherParameterValue.createWindSpeedMps(f);
		return this;
	}
}
