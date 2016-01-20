// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.softspb.util.DecimalDateTimeEncoding;
import java.util.Date;
import java.util.Random;

// Referenced classes of package com.softspb.weather.model:
//			WeatherParameterValue

public class Forecast
	implements Parcelable
{

	private static final byte CLOUD_VALUES[];
	public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

		public Forecast createFromParcel(Parcel parcel)
		{
			return new Forecast(parcel);
		}

//		public volatile Object createFromParcel(Parcel parcel)
//		{
//			return createFromParcel(parcel);
//		}

		public Forecast[] newArray(int i)
		{
			return new Forecast[i];
		}

//		public volatile Object[] newArray(int i)
//		{
//			return newArray(i);
//		}

	};
	private static final byte PRECIP_VALUES[];
	private static Random random;
	int cityId;
	int cloud;
	int dateLocal;
	WeatherParameterValue maxHeatIndex;
	WeatherParameterValue maxHumidity;
	WeatherParameterValue maxPress;
	WeatherParameterValue maxTemp;
	WeatherParameterValue maxWindSpeed;
	WeatherParameterValue minHeatIndex;
	WeatherParameterValue minHumidity;
	WeatherParameterValue minPress;
	WeatherParameterValue minTemp;
	WeatherParameterValue minWindSpeed;
	int precip;
	int timeLocal;
	long timestamp;
	WeatherParameterValue windDirection;

	Forecast()
	{
	}

	private Forecast(Parcel parcel)
	{
		readFromParcel(parcel);
	}


	public static Forecast createSimple(WeatherParameterValue weatherparametervalue, WeatherParameterValue weatherparametervalue1, int i, int j, int k, int l)
	{
		Forecast forecast = new Forecast();
		forecast.minTemp = weatherparametervalue;
		forecast.maxTemp = weatherparametervalue1;
		forecast.cloud = i;
		forecast.precip = j;
		forecast.dateLocal = k;
		forecast.timeLocal = l;
		return forecast;
	}

	public static Forecast generateRandom(int i, Date date)
	{
		if (random == null)
			random = new Random(System.currentTimeMillis());
		Forecast forecast = new Forecast();
		forecast.dateLocal = DecimalDateTimeEncoding.encodeDate(date);
		forecast.timeLocal = DecimalDateTimeEncoding.encodeTime(date);
		forecast.cityId = i;
		forecast.cloud = CLOUD_VALUES[random.nextInt(CLOUD_VALUES.length)];
		forecast.precip = PRECIP_VALUES[random.nextInt(PRECIP_VALUES.length)];
		int j = -50 + random.nextInt(100);
		forecast.minHeatIndex = WeatherParameterValue.createTemperatureCelsius(j);
		forecast.maxHeatIndex = WeatherParameterValue.createTemperatureCelsius(j + random.nextInt(50));
		int k = random.nextInt(100);
		forecast.minHumidity = WeatherParameterValue.createRelHumidityPercents(k);
		forecast.maxHumidity = WeatherParameterValue.createRelHumidityPercents(k + random.nextInt(1 + (100 - k)));
		int l = 700 + random.nextInt(100);
		forecast.minPress = WeatherParameterValue.createPressureMm(l);
		forecast.maxPress = WeatherParameterValue.createPressureMm(l + random.nextInt(50));
		forecast.minTemp = WeatherParameterValue.createTemperatureCelsius(-50 + random.nextInt(100));
		forecast.maxTemp = forecast.minTemp;
		int i1 = random.nextInt(50);
		forecast.minWindSpeed = WeatherParameterValue.createWindSpeedMps(i1);
		forecast.maxWindSpeed = WeatherParameterValue.createWindSpeedMps(i1 + random.nextInt(50));
		return forecast;
	}

	public int describeContents()
	{
		return 0;
	}

	public int getCityId()
	{
		return cityId;
	}

	public int getCloud()
	{
		return cloud;
	}

	public int getDateLocal()
	{
		return dateLocal;
	}

	public WeatherParameterValue getMaxHeatIndex()
	{
		return maxHeatIndex;
	}

	public WeatherParameterValue getMaxHumidity()
	{
		return maxHumidity;
	}

	public WeatherParameterValue getMaxPress()
	{
		return maxPress;
	}

	public WeatherParameterValue getMaxTemp()
	{
		return maxTemp;
	}

	public WeatherParameterValue getMaxWindSpeed()
	{
		return maxWindSpeed;
	}

	public WeatherParameterValue getMinHeatIndex()
	{
		return minHeatIndex;
	}

	public WeatherParameterValue getMinHumidity()
	{
		return minHumidity;
	}

	public WeatherParameterValue getMinPress()
	{
		return minPress;
	}

	public WeatherParameterValue getMinTemp()
	{
		return minTemp;
	}

	public WeatherParameterValue getMinWindSpeed()
	{
		return minWindSpeed;
	}

	public int getPrecip()
	{
		return precip;
	}

	public int getTimeLocal()
	{
		return timeLocal;
	}

	public long getTimestamp()
	{
		return timestamp;
	}

	public WeatherParameterValue getWindDirection()
	{
		return windDirection;
	}

	public void readFromParcel(Parcel parcel)
	{
		cityId = parcel.readInt();
		dateLocal = parcel.readInt();
		timeLocal = parcel.readInt();
		cloud = parcel.readInt();
		precip = parcel.readInt();
		maxTemp = WeatherParameterValue.createTemperatureDefaultUnits(parcel.readInt());
		minTemp = WeatherParameterValue.createTemperatureDefaultUnits(parcel.readInt());
		timestamp = parcel.readLong();
	}

	public String toString()
	{
		return (new StringBuilder()).append("[Forecast: cityId=").append(cityId).append(", date=").append(dateLocal).append(", time=").append(timeLocal).append(", minTemp=").append(minTemp.getValueInDefaultUnits()).append(", maxTemp=").append(maxTemp.getValueInDefaultUnits()).append(", cloud=").append(cloud).append(", precip=").append(precip).toString();
	}

	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeInt(cityId);
		parcel.writeInt(dateLocal);
		parcel.writeInt(timeLocal);
		parcel.writeInt(cloud);
		parcel.writeInt(precip);
		parcel.writeInt(((Number)maxTemp.getValueInDefaultUnits()).intValue());
		parcel.writeInt(((Number)minTemp.getValueInDefaultUnits()).intValue());
		parcel.writeLong(timestamp);
	}

	static 
	{
		byte abyte0[] = new byte[4];
		abyte0[0] = 0;
		abyte0[1] = 1;
		abyte0[2] = 2;
		abyte0[3] = 3;
		CLOUD_VALUES = abyte0;
		byte abyte1[] = new byte[7];
		abyte1[0] = 4;
		abyte1[1] = 5;
		abyte1[2] = 6;
		abyte1[3] = 7;
		abyte1[4] = 8;
		abyte1[5] = 9;
		abyte1[6] = 10;
		PRECIP_VALUES = abyte1;
	}
}
