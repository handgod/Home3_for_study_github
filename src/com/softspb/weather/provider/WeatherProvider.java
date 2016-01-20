// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.provider;

import android.content.*;
import android.content.pm.ProviderInfo;
import android.database.*;
import android.database.sqlite.*;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.Time;
import com.softspb.util.DecimalDateTimeEncoding;
import com.softspb.util.ProjectionCursor;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.CurrentLocationInfo;
import java.util.Arrays;
import java.util.List;

// Referenced classes of package com.softspb.weather.provider:
//			WeatherMetaData

public abstract class WeatherProvider extends ContentProvider
{
//	public static Logger logger;

	 class DailyForecastCursor extends AbstractCursor
	{

		private static final int TIME_2_PM = 1400;
		private SQLiteCursor cloudPrecipCur;
		private int columnCount;
		private String columnNames[];
		private Cursor cur;
		private int cursorId;
		private Object data[];
		private String query;
		private int requestId;
		private int rowCount;
		final WeatherProvider this$0;

		private void addRow(Object aobj[])
		{
			if (aobj.length != columnCount)
			{
				throw new IllegalArgumentException((new StringBuilder()).append("columnNames.length = ").append(columnCount).append(", columnValues.length = ").append(aobj.length).toString());
			} else
			{
				int i = rowCount;
				rowCount = i + 1;
				int j = i * columnCount;
				ensureCapacity(j + columnCount);
				System.arraycopy(((Object) (aobj)), 0, ((Object) (data)), j, columnCount);
				return;
			}
		}

		private void ensureCapacity(int i)
		{
			if (i > data.length)
			{
				Object aobj[] = data;
				int j = 2 * data.length;
				if (j < i)
					j = i;
				data = new Object[j];
				System.arraycopy(((Object) (aobj)), 0, ((Object) (data)), 0, aobj.length);
			}
		}

		private Object get(int i)
		{
			if (i < 0 || i >= columnCount)
				throw new CursorIndexOutOfBoundsException((new StringBuilder()).append("Requested column: ").append(i).append(", # of columns: ").append(columnCount).toString());
			if (mPos < 0)
				throw new CursorIndexOutOfBoundsException("Before first row.");
			if (mPos >= rowCount)
				throw new CursorIndexOutOfBoundsException("After last row.");
			else
				return data[i + mPos * columnCount];
		}

		private int[] getCloudPrecip(int i, int j)
		{
			System.currentTimeMillis();
			SQLiteCursor sqlitecursor = cloudPrecipCur;
			String as[] = new String[2];
			as[0] = Integer.toString(i);
			as[1] = Integer.toString(j);
			sqlitecursor.setSelectionArguments(as);
			int ai[];
			if (!cloudPrecipCur.requery())
			{
				ai = null;
			} else
			{
				ai = new int[2];
				if (cloudPrecipCur.moveToFirst())
				{
					int k = 0x7fffffff;
					for (; !cloudPrecipCur.isAfterLast(); cloudPrecipCur.moveToNext())
					{
						int l = Math.abs(-1400 + cloudPrecipCur.getInt(2));
						if (l < k)
						{
							ai[0] = cloudPrecipCur.getInt(0);
							ai[1] = cloudPrecipCur.getInt(1);
							k = l;
						}
					}

				}
				cloudPrecipCur.deactivate();
				System.currentTimeMillis();
			}
			return ai;
		}

		private void initCapacity(int i)
		{
			if (i < 1)
				i = 1;
			data = new Object[i * columnCount];
			rowCount = 0;
			mPos = -1;
		}

		private void updateData_Sql()
		{
			logd(requestId, "Updating DailyForecastCursor data...");
			initCapacity(cur.getCount());
			int i = 0;
			cur.moveToFirst();
			do
			{
				if (cur.isAfterLast())
					break;
				int j = cur.getInt(0);
				int k = cur.getInt(1);
				logd(requestId, (new StringBuilder()).append("Generating cursor data for cityId=").append(j).append(", date=").append(k).toString());
				int l = cur.getInt(3);
				int i1 = cur.getInt(2);
				int ai[] = getCloudPrecip(j, k);
				Object aobj[] = new Object[columnCount];
				int j1 = 0;
				while (j1 < columnCount) 
				{
					String s = columnNames[j1];
					if ("_id".equals(s))
						aobj[j1] = Integer.valueOf(i);
					else
					if ("city_id".equals(s))
						aobj[j1] = Integer.valueOf(j);
					else
					if ("cloud".equals(s))
						aobj[j1] = Integer.valueOf(ai[0]);
					else
					if ("date".equals(s))
						aobj[j1] = Integer.valueOf(k);
					else
					if ("precip".equals(s))
						aobj[j1] = Integer.valueOf(ai[1]);
					else
					if ("temp_max".equals(s))
						aobj[j1] = Integer.valueOf(i1);
					else
					if ("temp_min".equals(s))
						aobj[j1] = Integer.valueOf(l);
					j1++;
				}
				addRow(aobj);
				cur.moveToNext();
				i++;
			} while (true);
			cur.deactivate();
		}

		public void close()
		{
			super.close();
			cur.close();
			cloudPrecipCur.close();
		}

		public String[] getColumnNames()
		{
			return columnNames;
		}

		public int getCount()
		{
			return rowCount;
		}

		public double getDouble(int i)
		{
			Object obj = get(i);
			double d;
			if (obj == null)
				d = 0.0D;
			else
			if (obj instanceof Number)
				d = ((Number)obj).doubleValue();
			else
				d = Double.parseDouble(obj.toString());
			return d;
		}

		public float getFloat(int i)
		{
			Object obj = get(i);
			float f;
			if (obj == null)
				f = 0.0F;
			else
			if (obj instanceof Number)
				f = ((Number)obj).floatValue();
			else
				f = Float.parseFloat(obj.toString());
			return f;
		}

		public int getInt(int i)
		{
			Object obj = get(i);
			int j;
			if (obj == null)
				j = 0;
			else
			if (obj instanceof Number)
				j = ((Number)obj).intValue();
			else
				j = Integer.parseInt(obj.toString());
			return j;
		}

		public long getLong(int i)
		{
			Object obj = get(i);
			long l;
			if (obj == null)
				l = 0L;
			else
			if (obj instanceof Number)
				l = ((Number)obj).longValue();
			else
				l = Long.parseLong(obj.toString());
			return l;
		}

		String getQuery()
		{
			return query;
		}

		public short getShort(int i)
		{
			Object obj = get(i);
			short word0;
			if (obj == null)
				word0 = 0;
			else
			if (obj instanceof Number)
				word0 = ((Number)obj).shortValue();
			else
				word0 = Short.parseShort(obj.toString());
			return word0;
		}

		public String getString(int i)
		{
			Object obj = get(i);
			String s;
			if (obj == null)
				s = null;
			else
				s = obj.toString();
			return s;
		}

		public boolean isNull(int i)
		{
			boolean flag;
			if (get(i) == null)
				flag = true;
			else
				flag = false;
			return flag;
		}

		protected void onChange(boolean flag)
		{
			logd(requestId, "DailyForecastCursor.onChange");
			long l = System.currentTimeMillis();
			if (cur.requery())
			{
				updateData_Sql();
				long l1 = System.currentTimeMillis();
				logd(requestId, (new StringBuilder()).append("Cursor updated in ").append(l1 - l).append("ms").toString());
			}
			super.onChange(flag);
		}

		public DailyForecastCursor(int i, Uri uri, int j, int k, String as[], String s, 
				String as1[], String s1)
		{
			this(i, uri, as, WeatherProvider.compoundSelection(s, j, k, new StringBuilder[0]).toString(), mergeSelectionArgs(as1, new Object[]{Integer.valueOf(j),Integer.valueOf(k)}), s1);
//			String s2 = WeatherProvider.compoundSelection(s, j, k, new StringBuilder[0]).toString();
//			Object aobj[] = new Object[2];
//			aobj[0] = Integer.valueOf(j);
//			aobj[1] = Integer.valueOf(k);
		}

		public DailyForecastCursor(int i, Uri uri, int j, String as[], String s, String as1[], 
				String s1)
		{
			this(i, uri, as, WeatherProvider.compoundSelection(s, j, new StringBuilder[0]).toString(), mergeSelectionArgs(as1,  new Object[]{Integer.valueOf(j)}), s1);
		}

		public DailyForecastCursor(int i, Uri uri, String as[], String s, String as1[], String s1)
		{
			this$0 = WeatherProvider.this;
//			super();
			rowCount = 0;
			cursorId = 1;
// JavaClassFileOutputException: get_constant: invalid tag
		}
	}

	private  class WeatherDatabaseHelper extends SQLiteOpenHelper
	{

		private static final String CITIES_INDEX_BEFORE_VERSION_15 = "idx_cities";
		private static final String CITIES_TABLE_BEFORE_VERSION_15 = "cities";
		private static final String DB_NAME = "weather.db";
		private static final int DB_VERSION_15 = 15;
		private static final int DB_VERSION_6 = 6;
		private static final int DB_VERSION_7 = 7;
		private static final int DB_VERSION_8 = 8;
		private static final String EVENTS_LOG_TABLE_NAME_BEFORE_VERSION_7 = "events_log";
		private static final String TIMEZONES_TABLE_BEFORE_VERSION_15 = "timezones";

		private void logd(String s)
		{
			((Logger) WeatherProvider.logger).d(s);
		}

		public void onCreate(SQLiteDatabase sqlitedatabase)
		{
			logd("onCreate");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS forecast (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER NOT NULL, date INTEGER, time INTEGER NOT NULL, temp_min INTEGER, temp_max INTEGER, cloud INTEGER, precip INTEGER, press_min INTEGER, press_max INTEGER, humidity_min INTEGER, humidity_max INTEGER, heat_min INTEGER, heat_max INTEGER, wind_min INTEGER, wind_max INTEGER, wind_dir INTEGER, UNIQUE (city_id, date, time));");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS current (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER, station TEXT, date INTEGER, time INTEGER, temp INTEGER, wind_dir TEXT, wind_speed REAL, pressure REAL, humidity REAL, dew_point INTEGER, sky_icon INTEGER,  UNIQUE (city_id, station));");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS update_status (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER NOT NULL, status INTEGER NOT NULL, timestamp LONG NOT NULL, type INTEGER NOT NULL,  UNIQUE (city_id,status,type));");
		}

		public void onOpen(SQLiteDatabase sqlitedatabase)
		{
			super.onOpen(sqlitedatabase);
			int i = DecimalDateTimeEncoding.getTodayDateEncoded();
			Time time = new Time();
			time.setToNow();
			time.monthDay = -1 + time.monthDay;
			time.normalize(false);
			int j = DecimalDateTimeEncoding.encodeDate(time);
			logd((new StringBuilder()).append("Deleting outdated forecasts: currentDate=").append(i).toString());
			String as[] = new String[1];
			as[0] = Integer.toString(i);
			int k = sqlitedatabase.delete("forecast", "date < ?", as);
			logd((new StringBuilder()).append("Deleted ").append(k).append(" rows from ").append("forecast").append(" table.").toString());
			logd((new StringBuilder()).append("Deleting outdated current data: yesterdayDate=").append(j).toString());
			String as1[] = new String[1];
			as1[0] = Integer.toString(j);
			int l = sqlitedatabase.delete("current", "date < ?", as1);
			logd((new StringBuilder()).append("Deleted ").append(l).append(" rows from ").append("current").append(" table.").toString());
		}

		public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
		{
			logd((new StringBuilder()).append("onUpgrade: oldVersion=").append(i).append(" newVersion=").append(j).toString());
			if (i < 6)
			{
				sqlitedatabase.execSQL("DROP TABLE IF EXISTS forecast");
				sqlitedatabase.execSQL("DROP TABLE IF EXISTS current");
				sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS forecast (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER NOT NULL, date INTEGER, time INTEGER NOT NULL, temp_min INTEGER, temp_max INTEGER, cloud INTEGER, precip INTEGER, press_min INTEGER, press_max INTEGER, humidity_min INTEGER, humidity_max INTEGER, heat_min INTEGER, heat_max INTEGER, wind_min INTEGER, wind_max INTEGER, wind_dir INTEGER, UNIQUE (city_id, date, time));");
				sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS current (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER, station TEXT, date INTEGER, time INTEGER, temp INTEGER, wind_dir TEXT, wind_speed REAL, pressure REAL, humidity REAL, dew_point INTEGER, sky_icon INTEGER,  UNIQUE (city_id, station));");
			}
			if (i >= 7)
				{
				if (i < 8)
					sqlitedatabase.execSQL("DROP TABLE IF EXISTS update_status");
				if (i < 8)
					sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS update_status (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER NOT NULL, status INTEGER NOT NULL, timestamp LONG NOT NULL, type INTEGER NOT NULL,  UNIQUE (city_id,status,type));");
				if (i < 15)
				{
					sqlitedatabase.execSQL("DROP INDEX IF EXISTS idx_cities");
					sqlitedatabase.execSQL("DROP TABLE IF EXISTS cities");
					sqlitedatabase.execSQL("DROP TABLE IF EXISTS timezones");
				}
				return;
				}
			else
				{
				sqlitedatabase.execSQL("DROP TABLE IF EXISTS events_log");
				}
		}

		public WeatherDatabaseHelper(Context context)
		{
			super(context, "weather.db", null, 15);
		}
	}


	private static final int ALL_CURRENTS = 5;
	private static final int ALL_DAILY_FORECASTS = 7;
	private static final int ALL_DAILY_FORECASTS_FOR_CITY = 8;
	private static final int ALL_DETAILED_FORECASTS = 10;
	private static final int ALL_FORECASTS = 3;
	private static final int ALL_UPDATE_STATUS = 12;
	private static String CITY_DATE_GROUPING = "city_id,date";
	private static String CITY_DATE_SELECTION = "(city_id = ?) AND (date = ?)";
	private static final int CITY_ID_NOT_SET = 0x80000000;
	private static final int CLOUD_INDEX = 0;
	private static final String CLOUD_PRECIP_PROJECTION[];
	private static final int DETAILED_FORECAST_CITY = 13;
	private static final int DETAILED_FORECAST_CITY_DATE = 14;
	private static final int FORECAST_FOR_CITY = 18;
	private static final int FORECAST_FOR_CITY_DAY = 19;
	private static final int PRECIP_INDEX = 1;
	private static final int SINGLE_CURRENT = 6;
	private static final int SINGLE_DAILY_FORECAST = 9;
	private static final int SINGLE_UPDATE_STATUS = 11;
	private static final int TIME_INDEX = 2;
	private static int dayForecastCursorCount = 0;
	private static Logger logger = Loggers.getLogger(WeatherProvider.class.getName());
	private static int requestCount = 0;
	String DELETE_UPDATE_STATUS_CURRENT_LOCATION_SQL;
	String UPDATE_UPDATE_STATUS_CURRENT_LOCATION_SQL;
	private int currentLocationCityId;
	private ContentObserver currentLocationObserver;
	private Uri currentLocationUpdateStatusUri;
	private SQLiteStatement deleteUpdateStatusCurrentStatement;
	private ContentResolver mContentResolver;
	private SQLiteStatement updateUpdateStatusCurrentStatement;
	private UriMatcher uriMatcher;
	private SQLiteDatabase weatherDB;
	private WeatherDatabaseHelper weatherDBHelper;

	public void WeatherProvider()
	{
		DELETE_UPDATE_STATUS_CURRENT_LOCATION_SQL = "DELETE FROM update_status WHERE city_id=?";
		UPDATE_UPDATE_STATUS_CURRENT_LOCATION_SQL = "INSERT OR REPLACE INTO update_status (city_id,status,timestamp,type) SELECT -1024,status,timestamp,type FROM update_status WHERE city_id=?";
		final Handler localhandler = new Handler();
		currentLocationObserver = new ContentObserver(localhandler) {

			final WeatherProvider this$0;
			final Handler val$localhandler;
			public void onChange(boolean flag)
			{
				int i = com.spb.cities.provider.CitiesContract.CurrentLocation.queryCurrentLocationInfo(getContext(), mContentResolver).getCityId();
				if (i != currentLocationCityId)
				{
					currentLocationCityId = i;
					updateUpdateStatusForCurrentLocation();
				}
			}

			
			{
				this$0 = WeatherProvider.this;
				val$localhandler = localhandler;
//				ContentObserver(localhandler);
			}
		};
	}

	private static  StringBuilder compoundSelection(String s, int i, int j, StringBuilder astringbuilder[])
	{
		StringBuilder stringbuilder;
		StringBuilder astringbuilder1[];
		StringBuilder stringbuilder1;
		if (astringbuilder == null || astringbuilder.length == 0)
			stringbuilder = new StringBuilder();
		else
			stringbuilder = astringbuilder[0];
		astringbuilder1 = new StringBuilder[1];
		astringbuilder1[0] = stringbuilder;
		stringbuilder1 = compoundSelection(s, i, astringbuilder1);
		stringbuilder1.append(" AND (").append("date").append(" = ?)");
		return stringbuilder1;
	}

	private static StringBuilder compoundSelection(String s, int i, StringBuilder astringbuilder[])
	{
		StringBuilder stringbuilder;
		if (astringbuilder == null || astringbuilder.length == 0)
			stringbuilder = new StringBuilder();
		else
			stringbuilder = astringbuilder[0];
		if (s != null)
			stringbuilder.append("(").append(s).append(") AND (");
		stringbuilder.append("city_id").append(" = ?");
		if (s != null)
			stringbuilder.append(')');
		return stringbuilder;
	}

	private void initAuthority(String s, Context context)
	{
		logd((new StringBuilder()).append("initAuthority: authority=").append(s).toString());
		uriMatcher = new UriMatcher(-1);
		uriMatcher.addURI(s, "forecast", 3);
		uriMatcher.addURI(s, "forecast/#", 18);
		uriMatcher.addURI(s, "forecast/#/#", 19);
		uriMatcher.addURI(s, "current", 5);
		uriMatcher.addURI(s, "current/#", 6);
		uriMatcher.addURI(s, "dailyforecast", 7);
		uriMatcher.addURI(s, "dailyforecast/#", 8);
		uriMatcher.addURI(s, "dailyforecast/#/#", 9);
		uriMatcher.addURI(s, "forecast/timeofday", 10);
		uriMatcher.addURI(s, "forecast/timeofday/#", 13);
		uriMatcher.addURI(s, "forecast/timeofday/#/#", 14);
		uriMatcher.addURI(s, "update_status/*", 11);
		uriMatcher.addURI(s, "update_status", 12);
	}

	private void logd(int i, String s)
	{
		logger.d((new StringBuilder()).append('(').append(i).append(") ").append(s).toString());
	}

	private void logd(String s)
	{
		logger.d(s);
	}

	private void logw(int i, String s)
	{
		logger.w((new StringBuilder()).append('(').append(i).append(") ").append(s).toString());
	}

	private String[] mergeSelectionArgs(String as[], Object aobj[])
	{
		int i;
		int j;
		String as1[];
		if (as == null)
			i = 0;
		else
			i = as.length;
		if (aobj == null)
			j = 0;
		else
			j = aobj.length;
		as1 = new String[i + j];
		if (as != null)
			System.arraycopy(as, 0, as1, 0, i);
		if (aobj != null)
		{
			int k = 0;
			while (k < j) 
			{
				int l = i + k;
				String s;
				if (aobj[k] == null)
					s = "NULL";
				else
					s = aobj[k].toString();
				as1[l] = s;
				k++;
			}
		}
		return as1;
	}

	private void notifyDailyForecastChanged(int i)
	{
		Uri uri = Uri.withAppendedPath(WeatherMetaData.DailyForecastMetaData.getContentUri(null), Integer.toString(i));
		logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		mContentResolver.notifyChange(uri, null);
	}

	private void notifyDailyForecastChanged(int i, int j)
	{
		Uri uri = Uri.withAppendedPath(Uri.withAppendedPath(WeatherMetaData.DailyForecastMetaData.getContentUri(null), Integer.toString(i)), Integer.toString(j));
		logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		mContentResolver.notifyChange(uri, null);
	}

	private void notifyDetailedForecastChanged(int i)
	{
		Uri uri = WeatherMetaData.TimeOfDayForecastMetaData.getUri(null, i);
		logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		mContentResolver.notifyChange(uri, null);
	}

	private void notifyDetailedForecastChanged(int i, int j)
	{
		Uri uri = WeatherMetaData.TimeOfDayForecastMetaData.getUri(null, i, j);
		logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		mContentResolver.notifyChange(uri, null);
	}

	private void notifyForecastChanged(int i)
	{
		Uri uri = WeatherMetaData.ForecastMetaData.getCityUri(null, i);
		logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		mContentResolver.notifyChange(uri, null);
	}

	private void notifyForecastChanged(int i, int j)
	{
		Uri uri = WeatherMetaData.ForecastMetaData.getCityDateUri(null, i, j);
		logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		mContentResolver.notifyChange(uri, null);
	}

	private Cursor queryAllDailyForecasts(int i, Uri uri, String as[], String s, String as1[], String s1)
	{
		logd(i, "Processing All Daily Forecasts query...");
		long l = System.currentTimeMillis();
		DailyForecastCursor dailyforecastcursor = new DailyForecastCursor(i, uri, as, s, as1, s1);
		long l1 = System.currentTimeMillis();
		logd(i, (new StringBuilder()).append("DailyForeacastCursor object created in ").append(l1 - l).append("ms").toString());
		dailyforecastcursor.setNotificationUri(mContentResolver, uri);
		logd(i, "Request Completed.");
		return dailyforecastcursor;
	}

	private Cursor queryAllDailyForecastsForCity(int i, Uri uri, int j, String as[], String s, String as1[], String s1)
	{
		logd(i, (new StringBuilder()).append("Processing All Daily Forecasts query for one city: cityId=").append(j).toString());
		long l = System.currentTimeMillis();
		DailyForecastCursor dailyforecastcursor = new DailyForecastCursor(i, uri, j, as, s, as1, s1);
		long l1 = System.currentTimeMillis();
		logd(i, (new StringBuilder()).append("DailyForeacastCursor object created in ").append(l1 - l).append("ms").toString());
		dailyforecastcursor.setNotificationUri(mContentResolver, uri);
		logd(i, "Request Completed.");
		return dailyforecastcursor;
	}

	private Cursor queryDetailedForecasts(int i, Uri uri, String as[], int j, int k, String s, String as1[], 
			String s1)
	{
		logd(i, "Processing All Time-Of-Day Forecasts query...");
		SQLiteQueryBuilder sqlitequerybuilder = new SQLiteQueryBuilder();
		sqlitequerybuilder.setTables("forecast");
		boolean flag = false;
		if (j >= 0)
		{
			sqlitequerybuilder.appendWhere((new StringBuilder()).append("city_id").append('=').append(j));
			flag = true;
		}
		if (k >= 0)
		{
			if (flag)
				sqlitequerybuilder.appendWhere(") AND (");
			sqlitequerybuilder.appendWhere((new StringBuilder()).append("date").append('=').append(k));
		}
		StringBuilder stringbuilder = (new StringBuilder()).append("city_id").append(',').append("date").append(',').append("time_of_day");
		String s2 = sqlitequerybuilder.buildQuery(WeatherMetaData.TimeOfDayForecastMetaData.TIME_OF_DAY_FORECAST_PROJECTION, s, as1, stringbuilder.toString(), null, s1, null);
		long l = System.currentTimeMillis();
		logd(i, (new StringBuilder()).append("Doing raw query: ").append(s2).toString());
		Cursor cursor = weatherDB.rawQuery(s2, as1);
		long l1 = System.currentTimeMillis();
		logd(i, (new StringBuilder()).append("Query completed in ").append(l1 - l).append("ms").toString());
		ProjectionCursor projectioncursor = new ProjectionCursor(cursor, as);
		projectioncursor.setNotificationUri(getContext().getContentResolver(), uri);
		logd(i, "Request Completed.");
		return projectioncursor;
	}

	private Cursor queryForecast(int i, Uri uri, String as[], String s, String as1[], String s1, String s2, 
			String s3)
	{
		StringBuilder stringbuilder = (new StringBuilder()).append("queryForecast >>> uri=").append(uri).append(" selection=").append(s).append(" selectionArgs=");
		String s4;
		SQLiteQueryBuilder sqlitequerybuilder;
		boolean flag;
		String s5;
		Cursor cursor;
		Uri uri1;
		if (as1 == null)
			s4 = "null";
		else
			s4 = Arrays.toString(as1);
		logd(i, stringbuilder.append(s4).append(" sort=").append(s1).append(" cityId=").append(s2).append(" date=").append(s3).toString());
		sqlitequerybuilder = new SQLiteQueryBuilder();
		flag = false;
		sqlitequerybuilder.setTables("forecast");
		if (s2 != null)
		{
			if (false)
				sqlitequerybuilder.appendWhere(") AND (");
			sqlitequerybuilder.appendWhere("city_id");
			sqlitequerybuilder.appendWhere("=");
			sqlitequerybuilder.appendWhereEscapeString(s2);
			flag = true;
		}
		if (s3 != null)
		{
			if (flag)
				sqlitequerybuilder.appendWhere(") AND (");
			sqlitequerybuilder.appendWhere("date");
			sqlitequerybuilder.appendWhere(">=");
			sqlitequerybuilder.appendWhereEscapeString(s3);
		}
		s5 = sqlitequerybuilder.buildQuery(as, s, as1, null, null, s1, null);
		logd(i, (new StringBuilder()).append("queryForecast: ").append(s5).toString());
		cursor = sqlitequerybuilder.query(weatherDB, as, s, as1, null, null, s1);
		uri1 = uri;
		if (s3 != null)
			if (s2 != null)
				uri1 = Uri.withAppendedPath(WeatherMetaData.ForecastMetaData.getContentUri(null), s2);
			else
				uri1 = WeatherMetaData.ForecastMetaData.getContentUri(null);
		cursor.setNotificationUri(mContentResolver, uri1);
		logd(i, "queryForecast <<<");
		return cursor;
	}

	private Cursor querySingleDailyForecast(int i, Uri uri, int j, int k, String as[], String s, String as1[], 
			String s1)
	{
		logd(i, (new StringBuilder()).append("Processing Single Daily Forecast query: cityId=").append(j).append(", date=").append(k).toString());
		long l = System.currentTimeMillis();
		DailyForecastCursor dailyforecastcursor = new DailyForecastCursor(i, uri, j, k, as, s, as1, s1);
		long l1 = System.currentTimeMillis();
		logd(i, (new StringBuilder()).append("DailyForeacastCursor object created in ").append(l1 - l).append("ms").toString());
		dailyforecastcursor.setNotificationUri(mContentResolver, uri);
		logd(i, "Request Completed.");
		return dailyforecastcursor;
	}

	private void updateUpdateStatusForCurrentLocation()
	{
		if (updateUpdateStatusCurrentStatement == null)
		{
			updateUpdateStatusCurrentStatement = weatherDB.compileStatement(UPDATE_UPDATE_STATUS_CURRENT_LOCATION_SQL);
			deleteUpdateStatusCurrentStatement = weatherDB.compileStatement(DELETE_UPDATE_STATUS_CURRENT_LOCATION_SQL);
			deleteUpdateStatusCurrentStatement.bindLong(1, -1024L);
			currentLocationUpdateStatusUri = Uri.withAppendedPath(WeatherMetaData.UpdateStatusMetaData.getContentUri(getContext()), Integer.toString(-1024));
		}
		deleteUpdateStatusCurrentStatement.execute();
		updateUpdateStatusCurrentStatement.bindLong(1, currentLocationCityId);
		updateUpdateStatusCurrentStatement.execute();
		mContentResolver.notifyChange(currentLocationUpdateStatusUri, null);
	}

	public void attachInfo(Context context, ProviderInfo providerinfo)
	{
		super.attachInfo(context, providerinfo);
		String s = providerinfo.authority;
		String s1 = WeatherMetaData.getAuthority(context);
		if (!s.equals(s1))
			throw new RuntimeException((new StringBuilder()).append("Unexpected authority in manifest: ").append(s).append(", expected: ").append(s1).toString());
		String s2 = getClass().getName();
		String s3 = (new StringBuilder()).append(context.getPackageName()).append(".weather.provider.WeatherProvider").toString();
		if (!s2.equals(s3))
			throw new RuntimeException((new StringBuilder()).append("Unexpected WeatherProvider class name: ").append(s2).append(", must be: ").append(s3).toString());
		else
			return;
	}

	public int bulkInsert(Uri uri, ContentValues acontentvalues[])
	{
		int i;
		int j;
		i = 1 + requestCount;
		requestCount = i;
		j = uriMatcher.match(uri);
		int k = 0;;
		logd(i, (new StringBuilder()).append("bulkInsert: uri=").append(uri).toString());
		switch (j) {
		case 3:
			String s;
			String s1;
			s = "forecast";
			s1 = "date";
			break;
		case 5:
			s = "current";
			s1 = "time";
		
			int l;
			Integer integer;
			boolean flag;
			
			l = 0x80000000;
			integer = null;
			flag = false;
			int i1 = acontentvalues.length;
			int j1 = 0;
			while (j1 < i1) 
			{
				ContentValues contentvalues = acontentvalues[j1];
				if (j == 12 && contentvalues.get("timestamp") == null)
					contentvalues.put("timestamp", Long.valueOf(System.currentTimeMillis()));
				if (weatherDB.replace(s, s1, contentvalues) > 0L)
				{
					logd(i, (new StringBuilder()).append("Inserted: ").append(contentvalues).toString());
					Integer integer1 = contentvalues.getAsInteger("city_id");
					int k1;
					Integer integer2;
					if (integer1 == null)
						k1 = 0x80000000;
					else
						k1 = integer1.intValue();
					k++;
					integer2 = contentvalues.getAsInteger("date");
					if (k1 != 0x80000000)
						l = k1;
					if (j == 3 && integer2 != null)
					{
						if (!integer2.equals(integer))
							flag = true;
						integer = integer2;
					}
				}
				j1++;
			}
		
			if (k <= 0 || j != 3)
			{
				return k;
			}
			else 
			{
				if (l != 0x80000000)
				{
					if (flag)
					{
						notifyDailyForecastChanged(l);
						notifyDetailedForecastChanged(l);
						notifyForecastChanged(l);
					} else
					if (integer != null)
					{
						notifyDailyForecastChanged(l, integer.intValue());
						notifyDetailedForecastChanged(l, integer.intValue());
						notifyForecastChanged(l, integer.intValue());
					}
					logd((new StringBuilder()).append("Notifying change URI=").append(WeatherMetaData.TimeOfDayForecastMetaData.getContentUri(null).toString()).toString());
					mContentResolver.notifyChange(WeatherMetaData.TimeOfDayForecastMetaData.getContentUri(null), null);

				}
				else 
				{
					logd((new StringBuilder()).append("Notifying change URI=").append(WeatherMetaData.DailyForecastMetaData.getContentUri(null).toString()).toString());
					mContentResolver.notifyChange(WeatherMetaData.DailyForecastMetaData.getContentUri(null), null);
				}
			}
			break;
		case 12:
	
			break;
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI: ").append(uri).toString());
		}
	
		return k;
	}

	 public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
	  {
		String str5 = null;
	    String str1 = null;
	    String str3;
	    StringBuilder localStringBuilder2;
	    switch (this.uriMatcher.match(paramUri))
	    {
	    case 4:
	    default:
	      String str2 = "Unsupported URI: " + paramUri;
	      throw new IllegalArgumentException(str2);
	    case 3:
	      str3 = "forecast";
	      if (str1 == null)
	        break;
	      StringBuilder localStringBuilder1 = new StringBuilder().append(str1).append("=");
	      String str4 = (String)paramUri.getPathSegments().get(1);
	      localStringBuilder2 = localStringBuilder1.append(str4);
	      if (TextUtils.isEmpty(paramString));
	    case 5:
		      str3 = "current";
		      break;
	    case 6:
		      str3 = "current";
		      str1 = "city_id";
		      break;
	    }
	      paramString = str5;
	      int i = this.weatherDB.delete(str3, paramString, paramArrayOfString);
	      StringBuilder localStringBuilder3 = new StringBuilder().append("Notifying change URI=");
	      String str6 = paramUri.toString();
	      String str7 = str6;
	      logd(str7);
	      this.mContentResolver.notifyChange(paramUri, null);
	      return i;
	  }


	public String getType(Uri uri)
	{
		String s ;
	
		switch (uriMatcher.match(uri)) {
		case 3:
			 s = "vnd.android.cursor.dir/vnd.softspb.forecast";
			break;
		case 4:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());		

		case 5:
			s = "vnd.android.cursor.dir/vnd.softspb.current";
			break;
		case 6:
			s = "vnd.android.cursor.item/vnd.softspb.current";
			break;
		case 7:
			s = "vnd.android.cursor.dir/vnd.softspb.forecast.day";
			break;
		case 8:
			s = "vnd.android.cursor.item/vnd.softspb.forecast.day";
			break;
		case 9:
			s = "vnd.android.cursor.item/vnd.softspb.forecast.day";
			break;
		case 10:
			s = "vnd.android.cursor.dir/vnd.softspb.forecast.day";
			break;
		case 11:
			s = "vnd.android.cursor.item/vnd.softspb.eventslog";
			break;
		case 12:
			s = "vnd.android.cursor.dir/vnd.softspb.eventslog";
			break;
		case 13:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());

		case 14:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());

		case 15:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());

		case 16:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());

		case 17:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());

		case 18:
			s = "vnd.android.cursor.dir/vnd.softspb.forecast";
			break;
		case 19:
			s = "vnd.android.cursor.dir/vnd.softspb.forecast";
			break;

		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());
		}
		return s;
	}
	public Uri insert(Uri uri, ContentValues contentvalues)
	{
		int i;
		int j;
		String s = null;
		String s1 = null;
		i = 1 + requestCount;
		requestCount = i;
		j = uriMatcher.match(uri);
		logd(i, (new StringBuilder()).append("insert: uri=").append(uri.toString()).toString());
		switch (j) {
		case 3:
		
			s = "forecast";
			s1 = "date";
			break;
		case 5:

			s = "update_status";
			s1 = "timestamp";
			if (contentvalues.get("timestamp") == null)
				contentvalues.put("timestamp", Long.valueOf(System.currentTimeMillis()));
			break;
		case 12:
	
			break;
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());
		}
		
		long l = weatherDB.replace(s, s1, contentvalues);
		Uri uri1;
		if (l > 0L)
		{
			logd(i, (new StringBuilder()).append("Inserted: ").append(contentvalues).toString());
			if (j == 5)
				uri1 = ContentUris.withAppendedId(uri, contentvalues.getAsInteger("city_id").intValue());
			else
			if (j == 12)
			{
				int k = contentvalues.getAsInteger("city_id").intValue();
				uri1 = ContentUris.withAppendedId(uri, k);
				if (k == currentLocationCityId)
				{
					contentvalues.put("city_id", Integer.valueOf(-1024));
					weatherDB.replace(s, s1, contentvalues);
					Uri uri2 = Uri.withAppendedPath(uri, Integer.toString(-1024));
					logd((new StringBuilder()).append("Notifying change URI=").append(uri2.toString()).toString());
					mContentResolver.notifyChange(uri2, null);
				}
			} else
			{
				uri1 = ContentUris.withAppendedId(uri, l);
			}
			logd((new StringBuilder()).append("Notifying change URI=").append(uri1.toString()).toString());
			mContentResolver.notifyChange(uri1, null);
			if (j == 3)
			{
				Integer integer = contentvalues.getAsInteger("date");
				Integer integer1 = contentvalues.getAsInteger("city_id");
				if (integer1 != null)
					if (integer != null)
					{
						notifyForecastChanged(integer1.intValue(), integer.intValue());
						notifyDailyForecastChanged(integer1.intValue(), integer.intValue());
						notifyDetailedForecastChanged(integer1.intValue(), integer.intValue());
					} else
					{
						notifyForecastChanged(integer1.intValue());
						notifyDailyForecastChanged(integer1.intValue());
						notifyDetailedForecastChanged(integer1.intValue());
					}
			}
		} else
		{
			uri1 = null;
		}
		return uri1;
	
	}

	public boolean onCreate()
	{
		boolean flag = true;
		Context context = getContext();
		initAuthority(WeatherMetaData.getAuthority(context), context);
		mContentResolver = context.getContentResolver();
		weatherDBHelper = new WeatherDatabaseHelper(context);
		weatherDB = weatherDBHelper.getWritableDatabase();
		currentLocationCityId = com.spb.cities.provider.CitiesContract.CurrentLocation.queryCurrentLocationInfo(context, mContentResolver).getCityId();
		mContentResolver.registerContentObserver(com.spb.cities.provider.CitiesContract.CurrentLocation.getContentUri(context), flag, currentLocationObserver);
		if (weatherDB == null)
			flag = false;
		return flag;
	}

	public Cursor query(Uri uri, String as[], String s, String as1[], String s1)
	{
		int i;
		String s2;
		List list;
		String s3;
		String s4;
		String s5;
		i = 1 + requestCount;
		requestCount = i;
		s2 = null;
		Cursor cursor = null;
		list = uri.getPathSegments();
		s3 = null;
		s4 = null;
		s5 = null;
		ContentResolver contentresolver;
		logd(i, (new StringBuilder()).append("Received QUERY: ").append(uri.toString()).toString());
		logd(i, (new StringBuilder()).append("    projection=").append(Arrays.toString(as)).toString());
		logd(i, (new StringBuilder()).append("    selection=").append(s).toString());
		logd(i, (new StringBuilder()).append("    selectionArgs=").append(Arrays.toString(as1)).toString());
		logd(i, (new StringBuilder()).append("    sort=").append(s1).toString());
		
		String s6;
		String s7;
		String s8;
		String s9;
		String s10;
		long l;
		long l1;

		switch (uriMatcher.match(uri)) {
		case 3:
			cursor = queryForecast(i, uri, as, s, as1, s1, s4, s5);
			break;
		case 4:
			logw(i, (new StringBuilder()).append("Unssuported URI, throwing exception: uri=").append(uri.toString()).toString());
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());	

		case 5:
			s4 = (String)list.get(1);
			break;
		case 6:
			s2 = "city_id";
			s3 = (String)list.get(1);
			break;
		case 7:
			cursor = queryAllDailyForecasts(i, uri, as, s, as1, s1);
			break;
		case 8:
			cursor = queryAllDailyForecastsForCity(i, uri, Integer.parseInt((String)list.get(1)), as, s, as1, s1);
			break;
		case 9:
			cursor = querySingleDailyForecast(i, uri, Integer.parseInt((String)list.get(1)), Integer.parseInt((String)list.get(2)), as, s, as1, s1);

			break;
		case 10:
			cursor = queryDetailedForecasts(i, uri, as, -1, -1, s, as1, s1);

			break;
		case 11:
			s2 = "city_id";
			s3 = (String)list.get(1);
			break;
		case 12:
			s6 = "update_status";
			s7 = null;
		
			if (TextUtils.isEmpty(s1))
				s8 = s7;
			else
				s8 = s1;
			if (s2 != null)
			{
				StringBuilder stringbuilder = (new StringBuilder()).append(s2).append("=").append(s3);
			
			
				String s11;
				if (!TextUtils.isEmpty(s))
					s11 = (new StringBuilder()).append(" AND (").append(s).append(')').toString();
				else
					s11 = "";
				s = stringbuilder.append(s11).toString();
			}
			s9 = null;
			if (as != null && as.length == 2 && as[1].equalsIgnoreCase("date"))
				s9 = "date";
			s10 = SQLiteQueryBuilder.buildQueryString(false, s6, as, s, s9, null, s8, null);
			l = System.currentTimeMillis();
			logd(i, (new StringBuilder()).append("Doing Raw Query: ").append(s10).toString());
			cursor = weatherDB.rawQuery(s10, as1);
			l1 = System.currentTimeMillis();
			logd(i, (new StringBuilder()).append("Query completed in ").append(l1 - l).append("ms").toString());
			logd(i, (new StringBuilder()).append("Registering cursor [").append(cursor).append("] with notification URI: ").append(uri.toString()).toString());
			contentresolver = mContentResolver;
			cursor.setNotificationUri(contentresolver, uri);
			logd(i, "Request Completed.");
			break;
		case 13:
			cursor = queryDetailedForecasts(i, uri, as, Integer.parseInt((String)list.get(2)), -1, s, as1, s1);
			break;
		case 14:
			cursor = queryDetailedForecasts(i, uri, as, Integer.parseInt((String)list.get(2)), Integer.parseInt((String)list.get(3)), s, as1, s1);
			break;
		case 15:
			logw(i, (new StringBuilder()).append("Unssuported URI, throwing exception: uri=").append(uri.toString()).toString());
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());

		case 16:
			logw(i, (new StringBuilder()).append("Unssuported URI, throwing exception: uri=").append(uri.toString()).toString());
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());

		case 17:
			logw(i, (new StringBuilder()).append("Unssuported URI, throwing exception: uri=").append(uri.toString()).toString());
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());

		case 18:
			s4 = (String)list.get(1);
			break;
		case 19:
			s5 = (String)list.get(2);
			break;

		default:
			logw(i, (new StringBuilder()).append("Unssuported URI, throwing exception: uri=").append(uri.toString()).toString());
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());
		}
		return cursor;
	
	}

	public int update(Uri uri, ContentValues contentvalues, String s, String as[])
	{
		String s1 = null;
		int i;
		String s2 ;
		switch (uriMatcher.match(uri)) {
		case 3:
			s2 = "forecast";
			break;
		case 4:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI: ").append(uri).toString());		
		case 5:
			s2 = "current";
			s1 = "city_id";

			break;
		case 6:
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI: ").append(uri).toString());
		}
		if (s1 != null)
		{
			StringBuilder stringbuilder = (new StringBuilder()).append(s1).append("=").append((String)uri.getPathSegments().get(1));
		
			String s3;
			if (!TextUtils.isEmpty(s))
				s3 = (new StringBuilder()).append(" AND (").append(s).append(')').toString();
			else
				s3 = "";
			s = stringbuilder.append(s3).toString();
		}
		i = weatherDB.update(s2, contentvalues, s, as);
		logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		mContentResolver.notifyChange(uri, null);
		return i;	
	}

	static 
	{
		String as[] = new String[3];
		as[0] = "cloud";
		as[1] = "precip";
		as[2] = "time";
		CLOUD_PRECIP_PROJECTION = as;
	}





/*
	static int access$102(WeatherProvider weatherprovider, int i)
	{
		weatherprovider.currentLocationCityId = i;
		return i;
	}

*/






/*
	static int access$404()
	{
		int i = 1 + dayForecastCursorCount;
		dayForecastCursorCount = i;
		return i;
	}

*/





}

