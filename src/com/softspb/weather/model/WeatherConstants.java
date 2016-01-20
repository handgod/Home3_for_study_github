// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;


public abstract class WeatherConstants
{

	public static final int CLOUDINESS_CLEAR = 1;
	public static final int CLOUDINESS_CLOUDY = 3;
	public static final int CLOUDINESS_HEAVY = 4;
	public static final int CLOUDINESS_PARTLY = 2;
	public static final int ICON_CLEAR = 2;
	public static final int ICON_CLOUDY = 4;
	public static final int ICON_HAIL = 12;
	public static final int ICON_HEAVY_CLOUDY = 5;
	public static final int ICON_HEAVY_RAIN = 8;
	public static final int ICON_HEAVY_SNOW = 11;
	public static final int ICON_HOT = 1;
	public static final int ICON_LIGHT_RAIN = 7;
	public static final int ICON_LIGHT_SNOW = 10;
	public static final int ICON_NA = 0;
	public static final int ICON_NIGHT_CLEAR = 17;
	public static final int ICON_NIGHT_CLOUDY = 19;
	public static final int ICON_NIGHT_HAIL = 26;
	public static final int ICON_NIGHT_HARDLY_CLOUDY = 20;
	public static final int ICON_NIGHT_HEAVY_RAIN = 22;
	public static final int ICON_NIGHT_HEAVY_SNOW = 25;
	public static final int ICON_NIGHT_LIGHT_RAIN = 21;
	public static final int ICON_NIGHT_LIGHT_SNOW = 24;
	public static final int ICON_NIGHT_PARTLY_CLOUDY = 18;
	public static final int ICON_NIGHT_RAIN_WITH_SNOW = 23;
	public static final int ICON_NIGHT_SUNNY_WITH_RAIN = 28;
	public static final int ICON_NIGHT_SUNNY_WITH_SNOW = 30;
	public static final int ICON_NIGHT_SUNNY_WITH_THUNDERSTORM = 29;
	public static final int ICON_NIGHT_THUNDERSTORM = 27;
	public static final int ICON_PARTLY_CLOUDY = 3;
	public static final int ICON_RAIN_WITH_SNOW = 9;
	public static final int ICON_SIZE_LARGE = 3;
	public static final int ICON_SIZE_MEDIUM = 2;
	public static final int ICON_SIZE_SMALL = 1;
	public static final int ICON_SIZE_XLARGE = 4;
	public static final int ICON_SMOKE = 6;
	public static final int ICON_SUNNY_WITH_RAIN = 14;
	public static final int ICON_SUNNY_WITH_SNOW = 16;
	public static final int ICON_SUNNY_WITH_THUNDERSTORM = 15;
	public static final int ICON_THUNDERSTORM = 13;
	public static final int PRECIPITATION_HEAVY_RAIN = 4;
	public static final int PRECIPITATION_HEAVY_SNOW = 6;
	public static final int PRECIPITATION_LIGHT_RAIN = 5;
	public static final int PRECIPITATION_LIGHT_SNOW = 7;
	public static final int PRECIPITATION_NONE_1 = 9;
	public static final int PRECIPITATION_NONE_2 = 10;
	public static final int PRECIPITATION_THUNDERSTORM = 8;
	public static final int TIME_OF_DAY_DAY = 3;
	public static final int TIME_OF_DAY_DAY_END = 1759;
	public static final int TIME_OF_DAY_DAY_START = 1200;
	public static final int TIME_OF_DAY_EVENING = 4;
	public static final int TIME_OF_DAY_EVENING_END = 2359;
	public static final int TIME_OF_DAY_EVENING_START = 1800;
	public static final int TIME_OF_DAY_MORNING = 2;
	public static final int TIME_OF_DAY_MORNING_END = 1159;
	public static final int TIME_OF_DAY_MORNING_START = 600;
	public static final int TIME_OF_DAY_NIGHT = 1;
	public static final int TIME_OF_DAY_NIGHT_END = 559;
	public static final int TIME_OF_DAY_NIGHT_START = 0;
	public static final int WEATHER_CLEAR = 2;
	public static final int WEATHER_CLOUDY = 4;
	public static final int WEATHER_HAIL = 12;
	public static final int WEATHER_HEAVY_CLOUDY = 5;
	public static final int WEATHER_HEAVY_RAIN = 8;
	public static final int WEATHER_HEAVY_SNOW = 11;
	public static final int WEATHER_HOT = 1;
	public static final int WEATHER_LIGHT_RAIN = 7;
	public static final int WEATHER_LIGHT_SNOW = 10;
	private static final int WEATHER_MAX_VALUE = 16;
	private static final int WEATHER_MIN_VALUE = 0;
	public static final int WEATHER_NA = 0;
	public static final int WEATHER_PARTLY_CLOUDY = 3;
	public static final int WEATHER_RAIN_WITH_SNOW = 9;
	public static final int WEATHER_SMOKE = 6;
	public static final int WEATHER_SUNNY_WITH_RAIN = 14;
	public static final int WEATHER_SUNNY_WITH_SNOW = 16;
	public static final int WEATHER_SUNNY_WITH_THUNDERSTORM = 15;
	public static final int WEATHER_THUNDERSTORM = 13;

	public WeatherConstants()
	{
	}

	public static int dayIconToNightIcon(int i)
	{
		if (i != 1)
			{
			if (i >= 2 && i <= 5)
				i += 15;
			else
			if (i >= 7 && i <= 16)
				i += 14;
			}
		else 
			{
				i = 17;		
			}
		return i;
	}

	public static int getIconIndex(int i, int j)
	{
		if (i < 0 || i > 16)
			throw new IllegalArgumentException((new StringBuilder()).append("Illegal weather value: ").append(i).toString());
		if (j != 3 && j != 2)
			if (j == 4 || j == 1)
				i = dayIconToNightIcon(i);
			else
				throw new IllegalArgumentException((new StringBuilder()).append("Illegal timeOfDay value: ").append(j).toString());
		return i;
	}

	public static int getTimeOfDay(int i)
	{
		byte byte0;
		if (i >= 1200 && i <= 1759)
			byte0 = 3;
		else
		if (i >= 1800 && i <= 2359)
			byte0 = 4;
		else
		if (i >= 600 && i <= 1159)
			byte0 = 2;
		else
		if (i >= 0 && i <= 559)
			byte0 = 1;
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Illegal time value: ").append(i).toString());
		return byte0;
	}

	public static int gismeteoCodesToDayIcon(int i, int j)
	{
		int k = 0;
		switch (j) {
		case 4:
			switch (i)
			{
			case 0: // '\0'
				k = 14;
				break;

			case 1: // '\001'
			case 2: // '\002'
			case 3: // '\003'
				if (j == 4)
					k = 8;
				else
					k = 7;
				break;
			}
			break;
		case 5:
			switch (i)
			{
			case 0: // '\0'
				k = 14;
				break;

			case 1: // '\001'
			case 2: // '\002'
			case 3: // '\003'
				if (j == 4)
					k = 8;
				else
					k = 7;
				break;
			}
			break;
		case 6:
			switch (i)
			{
			case 0: // '\0'
				k = 16;
				break;

			case 1: // '\001'
			case 2: // '\002'
				k = 10;
				break;

			case 3: // '\003'
				k = 11;
				break;
			}
			break;
		case 7:
			switch (i)
			{
			case 0: // '\0'
				k = 16;
				break;

			case 1: // '\001'
			case 2: // '\002'
				k = 10;
				break;

			case 3: // '\003'
				k = 11;
				break;
			}
			break;
		case 8:
			switch (i)
			{
			case 0: // '\0'
				k = 15;
				break;

			case 1: // '\001'
			case 2: // '\002'
			case 3: // '\003'
				k = 13;
				break;
			}
			break;
		case 9:
			switch (i)
			{
			case 0: // '\0'
				k = 2;
				break;

			case 1: // '\001'
				k = 3;
				break;

			case 2: // '\002'
				k = 4;
				break;

			case 3: // '\003'
				k = 5;
				break;
			}
			break;
		case 10:
			switch (i)
			{
			case 0: // '\0'
				k = 2;
				break;

			case 1: // '\001'
				k = 3;
				break;

			case 2: // '\002'
				k = 4;
				break;

			case 3: // '\003'
				k = 5;
				break;
			}
			break;
		default:
			break;
		}
		return k;
	
	}
}
