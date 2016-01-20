// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.softspb.weather.model.*;

public final class WeatherMetaData
{
	public static final class UpdateStatusMetaData
		implements UpdateStatusColumns
	{

		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.softspb.eventslog";
		public static final String CONTENT_PATH = "update_status";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.eventslog";
		public static final int DEFAULT_CITY_ID_INDEX = 0;
		public static final String DEFAULT_PROJECTION[];
		public static final int DEFAULT_STATUS_INDEX = 1;
		public static final int DEFAULT_TIMESTAMP_INDEX = 2;
		public static final int DEFAULT_TYPE_INDEX = 3;
		public static final String TABLE_NAME = "update_status";
		public static final int UPDATE_TYPE_CURRENT_CONDITIONS = 1;
		public static final int UPDATE_TYPE_FORECAST = 2;
		private static Uri contentUri;

		static void checkInitUris(Context context)
		{
			if (contentUri == null)
			{
				if (contentUri == null)
					initUris(WeatherMetaData.getAuthority(context));
			}
		}

		public static UpdateStatus fromDefaultCursor(int paramInt, Cursor paramCursor)
	    {
	      UpdateStatus localUpdateStatus = new UpdateStatus(paramInt);
	      if (paramCursor.moveToFirst())
	        if (!paramCursor.isAfterLast())
	        {
	          int i = 0;
	          long l1 = 0;
	          if (paramCursor.getInt(0) == paramInt)
	          {
	            i = paramCursor.getInt(1);
	            l1 = paramCursor.getLong(2);
	            switch (paramCursor.getInt(3))
	            {
	            default:
	            case 2:
	            case 1:
	            }
	          }
	          while ( paramCursor.moveToNext())
	          {
	            
	            long l2 = localUpdateStatus.latestForecastTimestamp;
	            if (l1 > l2)
	            {
	              localUpdateStatus.forecastStatus = i;
	              localUpdateStatus.latestForecastTimestamp = l1;
	            }
	            if (i != 1)
	              continue;
	            long l3 = localUpdateStatus.latestSuccessfulForecastTimestamp;
	            if (l1 <= l3)
	              continue;

	            long l4 = localUpdateStatus.latestCurrentConditionsTimestamp;
	            if (l1 > l4)
	            {
	              localUpdateStatus.currentConditionsStatus = i;
	              localUpdateStatus.latestCurrentConditionsTimestamp = l1;
	            }
	            if (i != 1)
	              continue;
	            long l5 = localUpdateStatus.latestSuccessfulCurrentConditionsTimestamp;
	            if (l1 <= l5)
	              continue;
	            localUpdateStatus.latestSuccessfulCurrentConditionsTimestamp = l1;
	          }
	        }
	      return localUpdateStatus;
	    }

		public static Uri getContentUri(Context context)
		{
			checkInitUris(context);
			return contentUri;
		}

		private static void initUris(String s)
		{
			if (contentUri == null)
				contentUri = Uri.parse((new StringBuilder()).append("content://").append(s).append("/").append("update_status").toString());
		}

		static 
		{
			String as[] = new String[4];
			as[0] = "city_id";
			as[1] = "status";
			as[2] = "timestamp";
			as[3] = "type";
			DEFAULT_PROJECTION = as;
		}

		public UpdateStatusMetaData()
		{
		}
	}

	public static interface UpdateStatusColumns
		extends BaseColumns
	{

		public static final String CITY_ID = "city_id";
		public static final String STATUS = "status";
		public static final String TIMESTAMP = "timestamp";
		public static final String TYPE = "type";
	}

	public static final class CurrentMetaData
		implements CurrentColumns
	{

		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.softspb.current";
		public static final String CONTENT_PATH = "current";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.current";
		public static final int DEFAULT_CITY_ID_INDEX = 1;
		public static final int DEFAULT_DATE_INDEX = 3;
		public static final int DEFAULT_DEW_POINT_INDEX = 11;
		public static final int DEFAULT_HUMIDITY_INDEX = 8;
		public static final int DEFAULT_ID_INDEX = 0;
		public static final int DEFAULT_PRESSURE_INDEX = 7;
		public static final String DEFAULT_PROJECTION[];
		public static final int DEFAULT_SKY_ICON_INDEX = 6;
		public static final int DEFAULT_STATION_INDEX = 2;
		public static final int DEFAULT_TEMP_INDEX = 5;
		public static final int DEFAULT_TIME_INDEX = 4;
		public static final int DEFAULT_WIND_DIRECTION_INDEX = 9;
		public static final int DEFAULT_WIND_SPEED_INDEX = 10;
		public static final String TABLE_NAME = "current";
		private static Uri contentUri;

		static void checkInitUris(Context context)
		{
			if (contentUri == null)
				initUris(WeatherMetaData.getAuthority(context));
		}

		public static CurrentConditions fromDefaultCursor(Cursor cursor)
		{
			CurrentConditionsBuilder currentconditionsbuilder = new CurrentConditionsBuilder();
			currentconditionsbuilder.withCityId(cursor.getInt(1));
			if (!cursor.isNull(2))
				currentconditionsbuilder.withLocation(cursor.getString(2));
			if (!cursor.isNull(3))
				currentconditionsbuilder.withDateUTC(cursor.getInt(3));
			if (!cursor.isNull(4))
				currentconditionsbuilder.withTimeUTC(cursor.getInt(4));
			if (!cursor.isNull(6))
				currentconditionsbuilder.withSkyIcon(cursor.getInt(6));
			if (!cursor.isNull(5))
				currentconditionsbuilder.withTempDefaultUnits(cursor.getInt(5));
			if (!cursor.isNull(7))
				currentconditionsbuilder.withPressureDefaultUnits(cursor.getFloat(7));
			if (!cursor.isNull(10))
				currentconditionsbuilder.withWindSpeedDefaultUnits(cursor.getFloat(10));
			if (!cursor.isNull(8))
				currentconditionsbuilder.withRelativeHumidityDefaultUnits(cursor.getFloat(8));
			if (!cursor.isNull(9))
				currentconditionsbuilder.withWindDirDefaultUnits(cursor.getString(9));
			if (!cursor.isNull(11))
				currentconditionsbuilder.withDewPointDefaultUnits(cursor.getInt(11));
			currentconditionsbuilder.withTimestamp(System.currentTimeMillis());
			return currentconditionsbuilder.build();
		}

		public static Uri getContentUri(Context context)
		{
			checkInitUris(context);
			return contentUri;
		}

		private static void initUris(String s)
		{

			if (contentUri == null)
				contentUri = Uri.parse((new StringBuilder()).append("content://").append(s).append("/").append("current").toString());
		}

		static 
		{
			String as[] = new String[12];
			as[0] = "_id";
			as[1] = "city_id";
			as[2] = "station";
			as[3] = "date";
			as[4] = "time";
			as[5] = "temp";
			as[6] = "sky_icon";
			as[7] = "pressure";
			as[8] = "humidity";
			as[9] = "wind_dir";
			as[10] = "wind_speed";
			as[11] = "dew_point";
			DEFAULT_PROJECTION = as;
		}

		private CurrentMetaData()
		{
		}
	}

	public static interface CurrentColumns
		extends BaseColumns
	{

		public static final String CITY_ID = "city_id";
		public static final String DATE = "date";
		public static final String DEW_POINT = "dew_point";
		public static final String HUMIDITY = "humidity";
		public static final String PRESSURE = "pressure";
		public static final String SKY_ICON = "sky_icon";
		public static final String STATION = "station";
		public static final String TEMP = "temp";
		public static final String TIME = "time";
		public static final String WIND_DIRECTION = "wind_dir";
		public static final String WIND_SPEED = "wind_speed";
	}

	public static final class TimeOfDayForecastMetaData
		implements TimeOfDayForecastColumns
	{

		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.softspb.forecast.timeofday";
		public static final String CONTENT_PATH = "forecast/timeofday";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.forecast.timeofday";
		public static final int DEFAULT_CITY_ID_INDEX = 1;
		public static final int DEFAULT_CLOUDINESS_INDEX = 6;
		public static final int DEFAULT_DATE_INDEX = 2;
		public static final int DEFAULT_ID_INDEX = 0;
		public static final int DEFAULT_PRECIPITATION_INDEX = 7;
		public static final String DEFAULT_PROJECTION[];
		public static final int DEFAULT_TEMP_MAX_INDEX = 5;
		public static final int DEFAULT_TEMP_MIN_INDEX = 4;
		public static final int DEFAULT_TIME_OF_DAY_INDEX = 3;
		public static final String TIME_OF_DAY_FORECAST_PROJECTION[];
		private static Uri contentUri;

		static void checkInitUris(Context context)
		{

			if (contentUri == null)
				initUris(WeatherMetaData.getAuthority(context));
		}

		public static Forecast fromDefaultCursor(Cursor cursor)
		{
			ForecastBuilder forecastbuilder = new ForecastBuilder();
			String s = cursor.getString(1);
			if (s != null)
				forecastbuilder.withCityId(Integer.parseInt(s));
			String s1 = cursor.getString(6);
			if (s1 != null)
				forecastbuilder.withCloudiness(Integer.parseInt(s1));
			String s2 = cursor.getString(2);
			if (s2 != null)
				forecastbuilder.withDateLocal(Integer.parseInt(s2));
			String s3 = cursor.getString(3);
			if (s3 != null)
				forecastbuilder.withTimeLocal(Integer.parseInt(s3));
			String s4 = cursor.getString(7);
			if (s4 != null)
				forecastbuilder.withPrecipitation(Integer.parseInt(s4));
			String s5 = cursor.getString(5);
			if (s5 != null)
				forecastbuilder.withMaxTempDefaultUnits(Integer.parseInt(s5));
			String s6 = cursor.getString(4);
			if (s6 != null)
				forecastbuilder.withMinTempDefaultUnits(Integer.parseInt(s6));
			forecastbuilder.withTimestamp(System.currentTimeMillis());
			return forecastbuilder.build();
		}

		public static Uri getContentUri(Context context)
		{
			checkInitUris(context);
			return contentUri;
		}

		public static Uri getUri(Context context, int i)
		{
			return Uri.withAppendedPath(getContentUri(context), Integer.toString(i));
		}

		public static Uri getUri(Context context, int i, int j)
		{
			return Uri.withAppendedPath(Uri.withAppendedPath(getContentUri(context), Integer.toString(i)), Integer.toString(j));
		}

		private static void initUris(String s)
		{

			if (contentUri == null)
				contentUri = Uri.parse((new StringBuilder()).append("content://").append(s).append("/").append("forecast/timeofday").toString());
		}

		static 
		{
			String as[] = new String[8];
			as[0] = "_id";
			as[1] = "city_id AS city_id";
			as[2] = "date AS date";
			as[3] = "CASE WHEN time BETWEEN 0 AND 559 THEN 1 WHEN time BETWEEN 600 AND 1159 THEN 2 WHEN time BETWEEN 1200 AND 1759 THEN 3 WHEN time BETWEEN 1800 AND 2359 THEN 4 END AS time_of_day";
			as[4] = "MIN(temp_min) AS temp_min";
			as[5] = "MAX(temp_max) AS temp_max";
			as[6] = "cloud AS cloud";
			as[7] = "precip AS precip";
			TIME_OF_DAY_FORECAST_PROJECTION = as;
			String as1[] = new String[8];
			as1[0] = "_id";
			as1[1] = "city_id";
			as1[2] = "date";
			as1[3] = "time_of_day";
			as1[4] = "temp_min";
			as1[5] = "temp_max";
			as1[6] = "cloud";
			as1[7] = "precip";
			DEFAULT_PROJECTION = as1;
		}

		private TimeOfDayForecastMetaData()
		{
		}
	}

	public static interface TimeOfDayForecastColumns
		extends BaseColumns
	{

		public static final String CITY_ID = "city_id";
		public static final String CLOUDINESS = "cloud";
		public static final String DATE = "date";
		public static final String PRECIPITATION = "precip";
		public static final String TEMP_MAX = "temp_max";
		public static final String TEMP_MIN = "temp_min";
		public static final String TIME_OF_DAY = "time_of_day";
	}

	public static class DailyForecastMetaData
		implements DailyForecastColumns
	{

		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.softspb.forecast.day";
		public static final String CONTENT_PATH = "dailyforecast";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.forecast.day";
		public static final int DEFAULT_CITY_ID_INDEX = 1;
		public static final int DEFAULT_CLOUDINESS_INDEX = 5;
		public static final int DEFAULT_DATE_INDEX = 2;
		public static final int DEFAULT_ID_INDEX = 0;
		public static final int DEFAULT_PRECIPITATION_INDEX = 6;
		public static final String DEFAULT_PROJECTION[];
		public static final int DEFAULT_TEMP_MAX_INDEX = 4;
		public static final int DEFAULT_TEMP_MIN_INDEX = 3;
		private static Uri contentUri;

		static void checkInitUris(Context context)
		{

			if (contentUri == null)
				initUris(WeatherMetaData.getAuthority(context));
		}

		public static Forecast fromDefaultCursor(Cursor cursor)
		{
			ForecastBuilder forecastbuilder = new ForecastBuilder();
			String s = cursor.getString(1);
			if (s != null)
				forecastbuilder.withCityId(Integer.parseInt(s));
			String s1 = cursor.getString(5);
			if (s1 != null)
				forecastbuilder.withCloudiness(Integer.parseInt(s1));
			String s2 = cursor.getString(2);
			if (s2 != null)
				forecastbuilder.withDateLocal(Integer.parseInt(s2));
			String s3 = cursor.getString(6);
			if (s3 != null)
				forecastbuilder.withPrecipitation(Integer.parseInt(s3));
			String s4 = cursor.getString(4);
			if (s4 != null)
				forecastbuilder.withMaxTempDefaultUnits(Integer.parseInt(s4));
			String s5 = cursor.getString(3);
			if (s5 != null)
				forecastbuilder.withMinTempDefaultUnits(Integer.parseInt(s5));
			forecastbuilder.withTimestamp(System.currentTimeMillis());
			return forecastbuilder.build();
		}

		public static Uri getContentUri(Context context)
		{
			checkInitUris(context);
			return contentUri;
		}

		private static void initUris(String s)
		{

			if (contentUri == null)
				contentUri = Uri.parse((new StringBuilder()).append("content://").append(s).append("/").append("dailyforecast").toString());
		}

		static 
		{
			String as[] = new String[7];
			as[0] = "_id";
			as[1] = "city_id";
			as[2] = "date";
			as[3] = "temp_min";
			as[4] = "temp_max";
			as[5] = "cloud";
			as[6] = "precip";
			DEFAULT_PROJECTION = as;
		}

		public DailyForecastMetaData()
		{
		}
	}

	public static interface DailyForecastColumns
		extends BaseColumns
	{

		public static final String CITY_ID = "city_id";
		public static final String CLOUDINESS = "cloud";
		public static final String DATE = "date";
		public static final String PRECIPITATION = "precip";
		public static final String TEMP_MAX = "temp_max";
		public static final String TEMP_MIN = "temp_min";
	}

	public static final class ForecastMetaData
		implements ForecastColumns
	{

		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.softspb.forecast";
		public static final String CONTENT_PATH = "forecast";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.forecast";
		public static final int DAT_FORECAST_TEMP_MIN_INDEX = 3;
		public static final int DAY_FORECAST_CITY_ID_INDEX = 0;
		public static final int DAY_FORECAST_DATE_INDEX = 1;
		public static final String DAY_FORECAST_PROJECTION[];
		public static final int DAY_FORECAST_TEMP_MAX_INDEX = 2;
		public static final int DEFAULT_CITY_ID_INDEX = 1;
		public static final int DEFAULT_CLOUDINESS_INDEX = 6;
		public static final int DEFAULT_DATE_INDEX = 2;
		public static final int DEFAULT_ID_INDEX = 0;
		public static final int DEFAULT_PRECIPITATION_INDEX = 7;
		public static final String DEFAULT_PROJECTION[];
		public static final int DEFAULT_TEMP_MAX_INDEX = 5;
		public static final int DEFAULT_TEMP_MIN_INDEX = 4;
		public static final int DEFAULT_TIME_INDEX = 3;
		public static final String TABLE_NAME = "forecast";
		public static final String TIME_OF_DAY_ALIAS = "time_of_day";
		public static final int TIME_OF_DAY_DAY = 2;
		public static final int TIME_OF_DAY_EVENING = 3;
		public static final int TIME_OF_DAY_MORNING = 1;
		public static final int TIME_OF_DAY_NIGHT =1;
		private static Uri contentUri;

		static void checkInitUris(Context context)
		{

			if (contentUri == null)
				initUris(WeatherMetaData.getAuthority(context));
		}

		public static Forecast fromDefaultCursor(Cursor cursor)
		{
			ForecastBuilder forecastbuilder = new ForecastBuilder();
			String s = cursor.getString(1);
			if (s != null)
				forecastbuilder.withCityId(Integer.parseInt(s));
			String s1 = cursor.getString(6);
			if (s1 != null)
				forecastbuilder.withCloudiness(Integer.parseInt(s1));
			String s2 = cursor.getString(2);
			if (s2 != null)
				forecastbuilder.withDateLocal(Integer.parseInt(s2));
			String s3 = cursor.getString(3);
			if (s3 != null)
				forecastbuilder.withTimeLocal(Integer.parseInt(s3));
			String s4 = cursor.getString(7);
			if (s4 != null)
				forecastbuilder.withPrecipitation(Integer.parseInt(s4));
			String s5 = cursor.getString(5);
			if (s5 != null)
				forecastbuilder.withMaxTempDefaultUnits(Integer.parseInt(s5));
			String s6 = cursor.getString(4);
			if (s6 != null)
				forecastbuilder.withMinTempDefaultUnits(Integer.parseInt(s6));
			forecastbuilder.withTimestamp(System.currentTimeMillis());
			return forecastbuilder.build();
		}

		public static Uri getCityDateUri(Context context, int i, int j)
		{
			return Uri.withAppendedPath(Uri.withAppendedPath(getContentUri(context), Integer.toString(i)), Integer.toString(j));
		}

		public static Uri getCityUri(Context context, int i)
		{
			return Uri.withAppendedPath(getContentUri(context), Integer.toString(i));
		}

		public static Uri getContentUri(Context context)
		{
			checkInitUris(context);
			return contentUri;
		}

		private static void initUris(String s)
		{

			if (contentUri == null)
				contentUri = Uri.parse((new StringBuilder()).append("content://").append(s).append("/").append("forecast").toString());
		}

		static 
		{
			String as[] = new String[8];
			as[0] = "_id";
			as[1] = "city_id";
			as[2] = "date";
			as[3] = "time";
			as[4] = "temp_min";
			as[5] = "temp_max";
			as[6] = "cloud";
			as[7] = "precip";
			DEFAULT_PROJECTION = as;
			String as1[] = new String[4];
			as1[0] = "city_id AS city_id";
			as1[1] = "date AS date";
			as1[2] = "MAX(temp_max) AS temp_max";
			as1[3] = "MIN(temp_min) AS temp_min";
			DAY_FORECAST_PROJECTION = as1;
		}

		private ForecastMetaData()
		{
		}
	}

	public static interface ForecastColumns
		extends BaseColumns
	{

		public static final String CITY_ID = "city_id";
		public static final String CLOUDINESS = "cloud";
		public static final String DATE = "date";
		public static final String HEAT_INDEX_MAX = "heat_max";
		public static final String HEAT_INDEX_MIN = "heat_min";
		public static final String HUMIDITY_MAX = "humidity_max";
		public static final String HUMIDITY_MIN = "humidity_min";
		public static final String PRECIPITATION = "precip";
		public static final String PRESSURE_MAX = "press_max";
		public static final String PRESSURE_MIN = "press_min";
		public static final String TEMP_MAX = "temp_max";
		public static final String TEMP_MIN = "temp_min";
		public static final String TIME = "time";
		public static final String WIND_DIRECTION = "wind_dir";
		public static final String WIND_SPEED_MAX = "wind_max";
		public static final String WIND_SPEED_MIN = "wind_min";
	}


	static final String CREATE_TABLE_CURRENT = "CREATE TABLE IF NOT EXISTS current (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER, station TEXT, date INTEGER, time INTEGER, temp INTEGER, wind_dir TEXT, wind_speed REAL, pressure REAL, humidity REAL, dew_point INTEGER, sky_icon INTEGER,  UNIQUE (city_id, station));";
	static final String CREATE_TABLE_FORECAST = "CREATE TABLE IF NOT EXISTS forecast (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER NOT NULL, date INTEGER, time INTEGER NOT NULL, temp_min INTEGER, temp_max INTEGER, cloud INTEGER, precip INTEGER, press_min INTEGER, press_max INTEGER, humidity_min INTEGER, humidity_max INTEGER, heat_min INTEGER, heat_max INTEGER, wind_min INTEGER, wind_max INTEGER, wind_dir INTEGER, UNIQUE (city_id, date, time));";
	static final String CREATE_TABLE_UPDATE_STATUS = "CREATE TABLE IF NOT EXISTS update_status (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER NOT NULL, status INTEGER NOT NULL, timestamp LONG NOT NULL, type INTEGER NOT NULL,  UNIQUE (city_id,status,type));";
	private static final String WEATHER_AUTHORITY_SUFFIX = ".weather";
	private static String weatherProviderAuthority;

	private WeatherMetaData()
	{
	}

	static String getAuthority(Context context)
	{
		if (weatherProviderAuthority != null)
			return weatherProviderAuthority;
		else
		{
			if (weatherProviderAuthority == null)
				weatherProviderAuthority = (new StringBuilder()).append(context.getPackageName()).append(".weather").toString();
		}
		return weatherProviderAuthority;
	}
}
