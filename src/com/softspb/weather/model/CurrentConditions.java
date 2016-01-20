// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.softspb.util.DecimalDateTimeEncoding;
import java.util.Random;

// Referenced classes of package com.softspb.weather.model:
//			WeatherParameterValue

public class CurrentConditions
	implements Parcelable
{

	public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

		public CurrentConditions createFromParcel(Parcel parcel)
		{
			return new CurrentConditions(parcel);
		}

//		public volatile Object createFromParcel(Parcel parcel)
//		{
//			return createFromParcel(parcel);
//		}

		public CurrentConditions[] newArray(int i)
		{
			return new CurrentConditions[i];
		}

//		public volatile Object[] newArray(int i)
//		{
//			return newArray(i);
//		}

	};
	private static Random random;
	String airportICAOCode;
	int cityId;
	String dataSource;
	int dateUTC;
	WeatherParameterValue dewPoint;
	WeatherParameterValue heatIndex;
	WeatherParameterValue humidex;
	String latitude;
	String location;
	String longitude;
	String metar;
	WeatherParameterValue press;
	WeatherParameterValue relHumidity;
	int skyIcon;
	WeatherParameterValue temp;
	int timeUTC;
	long timestamp;
	WeatherParameterValue visibility;
	String weather;
	WeatherParameterValue windChill;
	WeatherParameterValue windDir;
	WeatherParameterValue windSpeed;

	CurrentConditions()
	{
	}

	private CurrentConditions(Parcel parcel)
	{
		readFromParcel(parcel);
	}


	public static CurrentConditions createSimple(WeatherParameterValue weatherparametervalue, int i, WeatherParameterValue weatherparametervalue1, WeatherParameterValue weatherparametervalue2, WeatherParameterValue weatherparametervalue3, WeatherParameterValue weatherparametervalue4)
	{
		CurrentConditions currentconditions = new CurrentConditions();
		currentconditions.temp = weatherparametervalue;
		currentconditions.skyIcon = i;
		currentconditions.windSpeed = weatherparametervalue1;
		currentconditions.press = weatherparametervalue2;
		currentconditions.relHumidity = weatherparametervalue3;
		currentconditions.dewPoint = weatherparametervalue4;
		return currentconditions;
	}

	public static CurrentConditions generateRandom(int i)
	{
		if (random == null)
			random = new Random(System.currentTimeMillis());
		CurrentConditions currentconditions = new CurrentConditions();
		currentconditions.cityId = i;
		currentconditions.skyIcon = 1 + random.nextInt(30);
		currentconditions.temp = WeatherParameterValue.createTemperatureDefaultUnits(-50 + random.nextInt(100));
		currentconditions.dewPoint = WeatherParameterValue.createDewPointDefaultUnits(-10 + random.nextInt(20));
		currentconditions.relHumidity = WeatherParameterValue.createRelHumidityDefaultUnits(100F * random.nextFloat());
		currentconditions.windDir = WeatherParameterValue.createWindDirectionDefaultValues("VRB");
		currentconditions.windSpeed = WeatherParameterValue.createWindSpeedDefaultUnits(50F * random.nextFloat());
		currentconditions.press = WeatherParameterValue.createPressureDefaultUnits(650F + 200F * random.nextFloat());
		currentconditions.location = (new StringBuilder()).append("Location-").append(Integer.toHexString(random.nextInt(1000)).toUpperCase()).toString();
		currentconditions.airportICAOCode = (new StringBuilder()).append("Station-").append(Integer.toHexString(random.nextInt(1000)).toUpperCase()).toString();
		currentconditions.latitude = Float.toString(180F * random.nextFloat() - 90F);
		currentconditions.longitude = Float.toString(360F * random.nextFloat() - 180F);
		currentconditions.dateUTC = DecimalDateTimeEncoding.getTodayDateEncoded();
		currentconditions.timeUTC = DecimalDateTimeEncoding.getTimeNowEncoded();
		return currentconditions;
	}

	public int describeContents()
	{
		return 0;
	}

	public String getAirportICAOCode()
	{
		return airportICAOCode;
	}

	public int getCityId()
	{
		return cityId;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public int getDateUTC()
	{
		return dateUTC;
	}

	public WeatherParameterValue getDewPoint()
	{
		return dewPoint;
	}

	public WeatherParameterValue getHeatIndex()
	{
		return heatIndex;
	}

	public WeatherParameterValue getHumidex()
	{
		return humidex;
	}

	public String getLatitude()
	{
		return latitude;
	}

	public String getLocation()
	{
		return location;
	}

	public String getLongitude()
	{
		return longitude;
	}

	public String getMetar()
	{
		return metar;
	}

	public WeatherParameterValue getPressure()
	{
		return press;
	}

	public WeatherParameterValue getRelHumidity()
	{
		return relHumidity;
	}

	public int getSkyIcon()
	{
		return skyIcon;
	}

	public WeatherParameterValue getTemp()
	{
		return temp;
	}

	public int getTimeUTC()
	{
		return timeUTC;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public WeatherParameterValue getVisibility()
	{
		return visibility;
	}

	public String getWeather()
	{
		return weather;
	}

	public WeatherParameterValue getWindChill()
	{
		return windChill;
	}

	public WeatherParameterValue getWindDirection()
	{
		return windDir;
	}

	public WeatherParameterValue getWindSpeed()
	{
		return windSpeed;
	}

	public void readFromParcel(Parcel parcel)
	{
		cityId = parcel.readInt();
		dateUTC = parcel.readInt();
		timeUTC = parcel.readInt();
		temp = WeatherParameterValue.createTemperatureDefaultUnits(parcel.readInt());
		press = WeatherParameterValue.createPressureDefaultUnits(parcel.readFloat());
		relHumidity = WeatherParameterValue.createRelHumidityDefaultUnits(parcel.readFloat());
		windDir = WeatherParameterValue.createWindDirectionDegrees(parcel.readDouble());
		windSpeed = WeatherParameterValue.createWindSpeedDefaultUnits(parcel.readFloat());
		dewPoint = WeatherParameterValue.createDewPointDefaultUnits(parcel.readInt());
		skyIcon = parcel.readInt();
		location = parcel.readString();
		timestamp = parcel.readLong();
	}

	public String toString()
	{
		StringBuilder stringbuilder = (new StringBuilder()).append("CurrentConditions: date=").append(getDateUTC()).append(" time=").append(getTimeUTC()).append(" timestamp=").append(getTimestamp());
		if (temp != null)
			stringbuilder.append(" temp=").append(temp.getValueInDefaultUnits());
		if (press != null)
			stringbuilder.append(" press=").append(press.getValueInDefaultUnits());
		if (windSpeed != null)
			stringbuilder.append(" windSpeed=").append(windSpeed.getValueInDefaultUnits());
		if (relHumidity != null)
			stringbuilder.append(" relHum=").append(relHumidity.getValueInDefaultUnits());
		if (dewPoint != null)
			stringbuilder.append(" dewPoint=").append(dewPoint.getValueInDefaultUnits());
		if (windDir != null)
			stringbuilder.append(" windDir=").append(windDir.getValue(2));
		return stringbuilder.toString();
	}

	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeInt(cityId);
		parcel.writeInt(dateUTC);
		parcel.writeInt(timeUTC);
		parcel.writeInt(((Number)temp.getValueInDefaultUnits()).intValue());
		parcel.writeFloat(((Number)press.getValueInDefaultUnits()).floatValue());
		parcel.writeFloat(((Number)relHumidity.getValueInDefaultUnits()).floatValue());
		parcel.writeDouble(((Number)windDir.getValue(2)).doubleValue());
		parcel.writeFloat(((Number)windSpeed.getValueInDefaultUnits()).floatValue());
		parcel.writeInt(((Number)dewPoint.getValueInDefaultUnits()).intValue());
		parcel.writeInt(skyIcon);
		parcel.writeString(location);
		parcel.writeLong(timestamp);
	}

}
