// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.provider;

import android.content.*;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import com.softspb.kana.KanaUtils;
import com.softspb.util.AssetSQLiteOpenHelper;
import com.softspb.util.ProjectionCursor;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.nearestcity.NearestCitiesClient;
import java.util.*;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;

// Referenced classes of package com.spb.cities.provider:
//			CitiesContract

public abstract class CitiesProvider extends ContentProvider
{
	private static class CitiesDatabaseHelper extends AssetSQLiteOpenHelper
	{

		private static final String COUNTRY_JP_SELECTION = "country_short_name_jp=?";
		private static final int DATA_VERSION_MILTIPLIER = 1000;
		private static final int DB_SCHEMA_VERSION_1 = 1;

		private static int createDbVersionNumber(int i, int j)
		{
			return i + j * 1000;
		}

		private static int getDataVersion(int i)
		{
			return i / 1000;
		}

		private static int getSchemaVersion(int i)
		{
			return i % 1000;
		}

		private void japaneseCountryNamesToHalfwidthForm(SQLiteDatabase paramSQLiteDatabase)
	    {
	      long l1 = SystemClock.uptimeMillis();
	      logd("japaneseCountryNamesToHalfwidthForm");
	      HashMap localHashMap1 = new HashMap();
	      Cursor localCursor;
	      int i = 0;
	      try
	      {
	        String[] arrayOfString1 = new String[1];
	        arrayOfString1[0] = "country_short_name_jp";
	        localCursor = paramSQLiteDatabase.query("cities", arrayOfString1, null, null, null, null, null, null);
	        if ((localCursor != null) && (localCursor.moveToNext()))
	        {
	          logd("Converting to halfwidth form...");
	          i = 0;
	          while (!localCursor.isAfterLast())
	          {
	            String str1 = localCursor.getString(0);
	            if (str1 != null)
	            {
	              String str2 = KanaUtils.getHalfwidthForm(str1);
	              HashMap localHashMap2 = localHashMap1;
	              String str3 = str1;
	              String str4 = str2;
	              Object localObject1 = localHashMap2.put(str3, str4);
	              i += 1;
	            }
	            boolean bool = localCursor.moveToNext();
	          }
	        }
	      }
	      finally
	      {
	       
	      }
	      try
	      {
	        localCursor.close();
	      
	        String str5 = "Converted to halfwidth form: " + i;
	        logd(str5);
	        while (true)
	        {
	          if (localCursor != null);
	          try
	          {
	            localCursor.close();
	            label203: logd("Updating database");
	            ContentValues localContentValues1 = new ContentValues();
	            String[] arrayOfString2 = new String[1];
	            arrayOfString2[0] = "";
	            Iterator localIterator = localHashMap1.entrySet().iterator();

	            while (localIterator.hasNext())
	              {
	                Map.Entry localEntry = (Map.Entry)localIterator.next();
	                String str6 = (String)localEntry.getKey();
	                String str7 = (String)localEntry.getValue();
	                ContentValues localContentValues2 = localContentValues1;
	                String str8 = str7;
	                localContentValues2.put("country_short_name_jp", str8);
	                arrayOfString2[0] = str6;
	                SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
	                ContentValues localContentValues3 = localContentValues1;
	                int j = localSQLiteDatabase.update("cities", localContentValues3, "country_short_name_jp=?", arrayOfString2);
	              }
	            StringBuilder localStringBuilder = new StringBuilder().append("japaneseCountryNamesToHalfwidthForm: completed in ");
	            long l2 = SystemClock.uptimeMillis() - l1;
	            String str9 = l2 + "ms";
	            logd(str9);
	            return;
	          }
	          catch (Exception localException1)
	          {
	        
	          }
	        }
	      }
	      catch (Exception localException2)
	      {
	   
	      }
	    }
		private void upgradeAll(SQLiteDatabase sqlitedatabase, int i, int j, String s)
		{
			logd((new StringBuilder()).append("upgradeAll: oldSchemaVersion=").append(i).append(" newSchemaVersion=").append(j).toString());
			upgradeData(sqlitedatabase, s);
		}

		private void upgradeData(SQLiteDatabase sqlitedatabase, String s)
		{
			logd("upgradeData");
			sqlitedatabase.execSQL("DROP INDEX IF EXISTS idx_cities");
			sqlitedatabase.execSQL("DROP TABLE IF EXISTS cities");
			sqlitedatabase.execSQL("DROP TABLE IF EXISTS timezones");
			sqlitedatabase.execSQL("DROP TABLE IF EXISTS current_location");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS cities (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER, city_name TEXT, city_name_jp TEXT, filter_name TEXT, country_short_name TEXT, country_short_name_jp TEXT, state_short_name  TEXT, latitude REAL, longitude  REAL, timezone_id INTEGER)");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS timezones (_id INTEGER PRIMARY KEY AUTOINCREMENT, timezone_id INTEGER UNIQUE, timezone_name TEXT, utc_offset_min INTEGER)");
			sqlitedatabase.execSQL((new StringBuilder()).append("INSERT INTO cities (city_id, city_name, city_name_jp, filter_name , country_short_name, country_short_name_jp, state_short_name, latitude, longitude, timezone_id) SELECT city_id, city_name, city_name_jp, filter_name , country_short_name, country_short_name_jp, state_short_name, latitude, longitude, timezone_id FROM ").append(s).append(".").append("cities").toString());
			sqlitedatabase.execSQL((new StringBuilder()).append("INSERT INTO timezones (_id, timezone_id, timezone_name, utc_offset_min) SELECT _id, timezone_id, timezone_name, utc_offset_min FROM ").append(s).append(".").append("timezones").toString());
			sqlitedatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_cities ON cities (filter_name ASC);");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS current_location (current_location_id INTEGER UNIQUE NOT NULL, city_id INTEGER, positioning_status INTEGER, last_updated_utc INTEGER)");
		}

		public void onCreate(SQLiteDatabase sqlitedatabase)
		{
			logd("onCreate");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS cities (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER, city_name TEXT, city_name_jp TEXT, filter_name TEXT, country_short_name TEXT, country_short_name_jp TEXT, state_short_name  TEXT, latitude REAL, longitude  REAL, timezone_id INTEGER)");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS timezones (_id INTEGER PRIMARY KEY AUTOINCREMENT, timezone_id INTEGER UNIQUE, timezone_name TEXT, utc_offset_min INTEGER)");
			sqlitedatabase.execSQL("CREATE INDEX IF NOT EXISTS idx_cities ON cities (filter_name ASC);");
			sqlitedatabase.execSQL("CREATE TABLE IF NOT EXISTS current_location (current_location_id INTEGER UNIQUE NOT NULL, city_id INTEGER, positioning_status INTEGER, last_updated_utc INTEGER)");
		}

		public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j, String s)
		{
			int k;
			int l;
			int i1;
			int j1;
			logd((new StringBuilder()).append("onUpgrade: oldVersion=").append(i).append(" newVersion=").append(j).append(" newDbName=").append(s).toString());
			k = getSchemaVersion(i);
			l = getSchemaVersion(j);
			i1 = getDataVersion(i);
			j1 = getDataVersion(j);
			logd((new StringBuilder()).append("onUpgrade: oldSchemaVersion=").append(k).append(" newSchemaVersion=").append(l).append(" oldDataVersion=").append(i1).append(" newDataVersion=").append(j1).toString());
			if (k != l || i1 == j1) 
			{
				if (k != l)
					upgradeAll(sqlitedatabase, k, l, s);
			}
			else
				upgradeData(sqlitedatabase, s);
			return;
		}

		public CitiesDatabaseHelper(Context context)
		{
			super(context, context.getString(com.spb.cities.R.string.cities_provider_asset_db_filename), null, createDbVersionNumber(1, context.getResources().getInteger(com.spb.cities.R.integer.cities_db_source_version)));
		}
	}

	private class LocaleChangedReceiver extends BroadcastReceiver
	{

		final CitiesProvider this$0;

		public void onReceive(Context context, Intent intent)
		{
			if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction()) && contentResolver != null)
			{
				logd("WeatherWidgetUpdateService: received Locale Changed Broadcast, notifying content observers...");
				contentResolver.notifyChange(CitiesContract.Cities.getContentUri(context), null);
			}
		}

		private LocaleChangedReceiver()
		{
			this$0 = CitiesProvider.this;
//			super();
		}

	}

	private static interface Tables
	{

		public static final String CITIES = "cities";
		public static final String CITIES_INDEX = "idx_cities";
		public static final String CURRENT_LOCATION = "current_location";
		public static final String TIMEZONES = "timezones";
	}


	static final String CITIES_JOIN_TIMEZONES = (new StringBuilder()).append("cities").append(" JOIN ").append("timezones").append(" ON ").append("cities").append(".").append("timezone_id").append("=").append("timezones").append(".").append("timezone_id").toString();
	static final String CITY_COLUMNS_LIST = "city_id, city_name, city_name_jp, filter_name , country_short_name, country_short_name_jp, state_short_name, latitude, longitude, timezone_id";
	static final String CITY_NAME_RES_COLUMN_JP = "coalesce( city_name_jp,city_name) AS city_name";
	static final String COPY_CITY_TABLE_append_src_table_name = "INSERT INTO cities (city_id, city_name, city_name_jp, filter_name , country_short_name, country_short_name_jp, state_short_name, latitude, longitude, timezone_id) SELECT city_id, city_name, city_name_jp, filter_name , country_short_name, country_short_name_jp, state_short_name, latitude, longitude, timezone_id FROM ";
	static final String COPY_TIMEZONES_TABLE_append_src_table_name = "INSERT INTO timezones (_id, timezone_id, timezone_name, utc_offset_min) SELECT _id, timezone_id, timezone_name, utc_offset_min FROM ";
	static final String COUNTRY_SHORT_NAME_RES_COLUMN_JP = "coalesce(country_short_name_jp,country_short_name) AS country_short_name";
	static final String CREATE_CITY_INDEX = "CREATE INDEX IF NOT EXISTS idx_cities ON cities (filter_name ASC);";
	static final String CREATE_TABLE_CITIES = "CREATE TABLE IF NOT EXISTS cities (_id INTEGER PRIMARY KEY AUTOINCREMENT, city_id INTEGER, city_name TEXT, city_name_jp TEXT, filter_name TEXT, country_short_name TEXT, country_short_name_jp TEXT, state_short_name  TEXT, latitude REAL, longitude  REAL, timezone_id INTEGER)";
	static final String CREATE_TABLE_CURRENT_LOCATION = "CREATE TABLE IF NOT EXISTS current_location (current_location_id INTEGER UNIQUE NOT NULL, city_id INTEGER, positioning_status INTEGER, last_updated_utc INTEGER)";
	static final String CREATE_TABLE_TIMEZONES = "CREATE TABLE IF NOT EXISTS timezones (_id INTEGER PRIMARY KEY AUTOINCREMENT, timezone_id INTEGER UNIQUE, timezone_name TEXT, utc_offset_min INTEGER)";
	static final String CURRENT_LOCATION_ID = "current_location_id";
	static final String CURRENT_LOCATION_ID_SELECTION = "current_location_id=1";
	static final int CURRENT_LOCATION_ID_VALUE = 1;
	public static final String DEFAULT_NEAREST_CITIES_LIMIT = "5";
	private static final int MATCH_ALL_CITIES = 1;
	private static final int MATCH_CITY_FILTER = 20;
	private static final int MATCH_CURRENT_LOCATION_CITY = 16;
	private static final int MATCH_NEAREST_CITIES = 15;
	private static final int MATCH_SINGLE_CITY = 2;
	static final String TIMEZONES_COLUMNS_LIST = "_id, timezone_id, timezone_name, utc_offset_min";
	private static final IntentFilter localeChangedFilter = new IntentFilter("android.intent.action.LOCALE_CHANGED");
	private static Logger logger = Loggers.getLogger(CitiesProvider.class.getName());
	private static int requestCount = 0;
	private SQLiteDatabase citiesDB;
	private ContentResolver contentResolver;
	private NearestCitiesClient nearestClient;
	private UriMatcher uriMatcher;
	private CitiesDatabaseHelper weatherDBHelper;

	public CitiesProvider()
	{
	}

	private static HttpClient createThreadSafeHttpClient()
	{
		BasicHttpParams basichttpparams = new BasicHttpParams();
		HttpProtocolParams.setVersion(basichttpparams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(basichttpparams, "ISO-8859-1");
		HttpProtocolParams.setUseExpectContinue(basichttpparams, true);
		SchemeRegistry schemeregistry = new SchemeRegistry();
		schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeregistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		return new DefaultHttpClient(new ThreadSafeClientConnManager(basichttpparams, schemeregistry), basichttpparams);
	}

	private synchronized NearestCitiesClient getNearestClient()
	{
		
		if (nearestClient == null)
			nearestClient = new NearestCitiesClient(getContext(), createThreadSafeHttpClient());
		return nearestClient;

	}

	private void initAuthority(String s, Context context)
	{
		logd((new StringBuilder()).append("initAuthority: authority=").append(s).toString());
		logd((new StringBuilder()).append("initAuthority: city content uri: ").append(CitiesContract.Cities.getContentUri(context)).toString());
		uriMatcher = new UriMatcher(-1);
		uriMatcher.addURI(s, "city", 1);
		uriMatcher.addURI(s, "city/#", 2);
		uriMatcher.addURI(s, "nearest", 15);
		uriMatcher.addURI(s, "city_filter/*", 20);
		uriMatcher.addURI(s, "currentlocation", 16);
	}

	private static String logUnicode(String s)
	{
		String s1;
		if (s == null)
		{
			s1 = "null";
		} else
		{
			int i = s.length();
			int j = 0;
			StringBuilder stringbuilder = new StringBuilder();
			for (; j < i; j = s.offsetByCodePoints(j, 1))
				stringbuilder.append("\\u").append(Integer.toHexString(s.codePointAt(j)));

			s1 = stringbuilder.toString();
		}
		return s1;
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

	private Cursor queryCities(Uri uri, String as[], String s, String as1[], String s1, String s2, String s3)
	{
		StringBuilder stringbuilder = new StringBuilder();
		boolean flag = Locale.JAPANESE.getLanguage().equals(Locale.getDefault().getLanguage());
		boolean flag1 = false;
		int i;
		if (as == null)
			i = 0;
		else
			i = as.length;
		if (as == null)
			flag1 = true;
		for (int j = 0; j < i; j++)
			if ("timezone_name".equals(as[j]) || "utc_offset_min".equals(as[j]))
				flag1 = true;

		String s4;
		int k;
		int l;
		int i1;
		if (flag1)
			s4 = CITIES_JOIN_TIMEZONES;
		else
			s4 = "cities";
		if (s != null)
			stringbuilder.append('(').append(s).append(')');
		if (s3 != null)
		{
			String s8 = s3.replaceAll("[ .-]", "").toUpperCase();
			if (stringbuilder.length() > 0)
				stringbuilder.append(" AND ");
			stringbuilder.append('(');
			stringbuilder.append("(UPPER(").append("filter_name").append(") GLOB '").append(s8).append("*')");
			stringbuilder.append(" OR ");
			stringbuilder.append("(").append("city_name_jp").append(" GLOB '").append(s8).append("*')");
			stringbuilder.append(')');
		}
		if (s2 != null)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append(" AND ");
			stringbuilder.append('(').append("city_id").append("=").append(s2).append(")");
		}
		k = -1;
		l = -1;
		i1 = 0;
		while (i1 < i) 
		{
			if ("city_name".equals(as[i1]))
				k = i1;
			else
			if ("country_short_name".equals(as[i1]))
				l = i1;
			i1++;
		}
		if ((k != -1 || l != -1) && flag)
		{
			String as2[] = new String[i];
			int j1 = 0;
			while (j1 < i) 
			{
				String s7;
				if (j1 == k)
					s7 = "coalesce( city_name_jp,city_name) AS city_name";
				else
				if (j1 == l)
					s7 = "coalesce(country_short_name_jp,country_short_name) AS country_short_name";
				else
					s7 = as[j1];
				as2[j1] = s7;
				j1++;
			}
			as = as2;
		}
		if (s1 == null)
			s1 = "city_name_jp,city_name";
		String s5 = stringbuilder.toString();
		String s6 = SQLiteQueryBuilder.buildQueryString(false, s4, as, s5, null, null, s1, null);
		long l1 = System.currentTimeMillis();
		logd((new StringBuilder()).append("Doing Raw Query: ").append(s6).toString());
		Cursor cursor = citiesDB.rawQuery(s6, as1);
		long l2 = System.currentTimeMillis();
		logd((new StringBuilder()).append("Query completed in ").append(l2 - l1).append("ms").toString());
		logd((new StringBuilder()).append("Registering cursor [").append(cursor).append("] with notification URI: ").append(uri.toString()).toString());
		cursor.setNotificationUri(contentResolver, uri);
		return cursor;
	}

	private Cursor queryCurrentLocation(String as[], String s, String as1[], String s1)
	{
		Cursor cursor = citiesDB.query("current_location", as, s, as1, null, null, s1);
		cursor.setNotificationUri(contentResolver, CitiesContract.CurrentLocation.getContentUri(getContext()));
		return cursor;
	}

	private Cursor queryNearestCities(Uri uri, String as[], String s, String as1[], String s1)
	{
		Object obj;
		String s6;
		String as3[];
		int l;
		String as4[];
		Iterator iterator;
		Cursor cursor;
		String s2 = uri.getQueryParameter("lon");
		String s3 = uri.getQueryParameter("lat");
		String s4 = uri.getQueryParameter("limit");
		if (s4 == null)
			s4 = "5";
		NearestCitiesClient nearestcitiesclient = getNearestClient();
		ArrayList arraylist = null;
		if (s2 != null && s3 != null)
			arraylist = (ArrayList)nearestcitiesclient.download(new com.spb.cities.nearestcity.NearestCitiesClient.QueryParams(s2, s3, s4));
		int i;
		boolean flag;
		String as2[];
		boolean flag1;
		int j;
		if (arraylist == null)
			i = 0;
		else
			i = arraylist.size();
		obj = new MatrixCursor(as, i);
		if (arraylist == null)
			return ((Cursor) (obj));
		flag = Locale.JAPANESE.getLanguage().equals(Locale.getDefault().getLanguage());
		as2 = as;
		flag1 = false;
		j = as.length;
		for (int k = 0; k < j; k++)
			if ("timezone_name".equals(as[k]) || "utc_offset_min".equals(as[k]))
				flag1 = true;

		String s5;
		if (flag1)
			s5 = CITIES_JOIN_TIMEZONES;
		else
			s5 = "cities";
		if (flag)
		{
			int j1 = -1;
			int k1 = -1;
			int l1 = 0;
			do
			{
				int i2 = as.length;
				if (l1 >= i2)
					break;
				if ("city_name".equals(as[l1]))
					j1 = l1;
				else
				if ("country_short_name".equals(as[l1]))
					k1 = l1;
				l1++;
			} while (true);
			if ((j1 != -1 || k1 == -1) && flag)
			{
				as2 = new String[as.length];
				int j2 = 0;
				do
				{
					int k2 = as.length;
					if (j2 >= k2)
						break;
					String s7;
					if (j2 == j1)
						s7 = "coalesce( city_name_jp,city_name) AS city_name";
					else
					if (j2 == k1)
						s7 = "coalesce(country_short_name_jp,country_short_name) AS country_short_name";
					else
						s7 = as[j2];
					as2[j2] = s7;
					j2++;
				} while (true);
			}
		}
		s6 = SQLiteQueryBuilder.buildQueryString(false, s5, as2, "city_id=?", null, null, null, null);
		as3 = new String[1];
		l = as.length;
		as4 = new String[l];
		iterator = arraylist.iterator();

		com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem responseitem = null;
		
		if (iterator.hasNext())
		
		responseitem = (com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem)iterator.next();
		as3[0] = Integer.toString(responseitem.getCityId());
		cursor = citiesDB.rawQuery(s6, as3);
		if (cursor != null && cursor.moveToFirst())
		{
			for (int i1 = 0; i1 < l; i1++)
				as4[i1] = cursor.getString(i1);

			logd((new StringBuilder()).append("Adding row to cursor: rowLength=").append(as4.length).append(" cursorLength=").append(((MatrixCursor) (obj)).getColumnCount()).toString());
			((MatrixCursor) (obj)).addRow(as4);
		}
		cursor.close();
		
		if (!Arrays.equals(as, CitiesContract.Cities.NEAREST_PROJECTION))
			obj = new ProjectionCursor(((Cursor) (obj)), as);
		return ((Cursor) (obj));
	}

	public void attachInfo(Context context, ProviderInfo providerinfo)
	{
		super.attachInfo(context, providerinfo);
		String s = providerinfo.authority;
		String s1 = CitiesContract.getAuthority(context);
		if (!s.equals(s1))
			throw new RuntimeException((new StringBuilder()).append("Unexpected authority in manifest: ").append(s).append(", expected: ").append(s1).toString());
		String s2 = getClass().getName();
		String s3 = (new StringBuilder()).append(context.getPackageName()).append(".cities.provider.CitiesProvider").toString();
		if (!s2.equals(s3))
		{
			throw new RuntimeException((new StringBuilder()).append("Unexpected CitiesProvider class name: ").append(s2).append(", must be: ").append(s3).toString());
		} else
		{
			initAuthority(s1, context);
			return;
		}
	}

	public int bulkInsert(Uri uri, ContentValues acontentvalues[])
	{
		int i = 1 + requestCount;
		requestCount = i;
		int j = uriMatcher.match(uri);
		logd(i, (new StringBuilder()).append("bulkInsert: uri=").append(uri).toString());
		int k;
		switch (j)
		{
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI: ").append(uri).toString());

		case 1: // '\001'
			k = 0;
			break;
		}
		int l = acontentvalues.length;
		for (int i1 = 0; i1 < l; i1++)
		{
			ContentValues contentvalues = acontentvalues[i1];
			if (citiesDB.replace("cities", "city_name", contentvalues) > 0L)
				k++;
		}

		if (k > 0)
			contentResolver.notifyChange(uri, null);
		return k;
	}

	public int delete(Uri uri, String s, String as[])
	{
		String s1 = null;
		int i;
		String s2 = null ;
		switch (uriMatcher.match(uri)) {
		case 1:
			s2 = "cities";
			break;
		case 2:
			s2 = "cities";
			s1 = "_id";
			break;
		case 16:
	
			break;
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
		i = citiesDB.delete(s2, s, as);
		logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		contentResolver.notifyChange(uri, null);
		return i;
	}

	public String getType(Uri uri)
	{
		String s;
		switch (uriMatcher.match(uri)) {
		case 1:
			s = "vnd.android.cursor.dir/vnd.softspb.city";
			break;
		case 2:
			s = "vnd.android.cursor.item/vnd.softspb.city";
			break;
		case 15:
			s = "vnd.android.cursor.dir/vnd.softspb.city";
			break;
		case 16:
			s = "vnd.android.cursor.dir/vnd.softspb.currentlocation";
			break;
		case 20:
			s = "vnd.android.cursor.item/vnd.softspb.city";
			break;
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI:").append(uri).toString());
		}
		return s;
	}

	  public Uri insert(Uri paramUri, ContentValues paramContentValues)
	  {
	    int i = requestCount + 1;
	    requestCount = i;
	    int j = this.uriMatcher.match(paramUri);
	    StringBuilder localStringBuilder1 = new StringBuilder().append("insert: uri=");
	    String str1 = paramUri.toString();
	    String str2 = str1;
	    logd(i, str2);
	    String str4;
	    String str5;
	    Uri localUri = null;
	    switch (j)
	    {
		    default:
		      String str3 = "Unsupported URI:" + paramUri;
		      throw new IllegalArgumentException(str3);
		    case 1:
		      str4 = "cities";
		      str5 = "city_name";
		      long l = this.citiesDB.replace(str4, str5, paramContentValues);
		      if (l <= 0L)
		        break;
		      String str6 = "Inserted: " + paramContentValues;
		      logd(i, str6);
		      localUri = ContentUris.withAppendedId(paramUri, l);
		      StringBuilder localStringBuilder2 = new StringBuilder().append("Notifying change URI=");
		      String str7 = localUri.toString();
		      String str8 = str7;
		      logd(str8);
		      this.contentResolver.notifyChange(localUri, null);
		    case 16:
	    }
	    {
		      str4 = "current_location";
		      str5 = "positioning_status";
		      ContentValues localContentValues = new ContentValues();
		      localContentValues.putAll(paramContentValues);
		      Integer localInteger = Integer.valueOf(1);
		      localContentValues.put("current_location_id", localInteger);
		      paramContentValues = localContentValues;
	    }
	    return localUri;
	  }
	public boolean onCreate()
	{
		Context context = getContext();
		contentResolver = context.getContentResolver();
		weatherDBHelper = new CitiesDatabaseHelper(context);
		citiesDB = weatherDBHelper.getWritableDatabase();
		context.registerReceiver(new LocaleChangedReceiver(), localeChangedFilter);
		boolean flag;
		if (citiesDB != null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public Cursor query(Uri uri, String as[], String s, String as1[], String s1)
	{
		int i;
		List list;
		String s2;
		i = 1 + requestCount;
		requestCount = i;
		list = uri.getPathSegments();
		s2 = null;
		Cursor cursor = null ;
		logd(i, (new StringBuilder()).append("Received QUERY: ").append(uri.toString()).toString());
		logd(i, (new StringBuilder()).append("    projection=").append(Arrays.toString(as)).toString());
		logd(i, (new StringBuilder()).append("    selection=").append(s).toString());
		logd(i, (new StringBuilder()).append("    selectionArgs=").append(Arrays.toString(as1)).toString());
		logd(i, (new StringBuilder()).append("    sort=").append(s1).toString());
		switch (uriMatcher.match(uri)) {
		case 1:
			cursor = queryCities(uri, as, s, as1, s1, s2, null);
			break;
		case 2:
			s2 = (String)list.get(1);		
			break;
		case 15:
			cursor = queryNearestCities(uri, as, s, as1, s1);
			break;
		case 16:
			cursor = queryCurrentLocation(as, s, as1, s1);
			break;
		case 20:
			cursor = queryCities(uri, as, s, as1, s1, null, (String)list.get(1));
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
		String s2 = null;
		switch (uriMatcher.match(uri)) {
		case 1:
			s2 = "cities";
			break;

		case 2:
			s2 = "cities";
			s1 = "_id";
			break;
		case 16:
	
			break;
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Unsupported URI: ").append(uri).toString());
		}
		int i;
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
		logd((new StringBuilder()).append("update: table=").append(s2).append(" values=").append(contentvalues).append(" where=").append(s).append(" args=").append(Arrays.toString(as)).toString());
		i = citiesDB.update(s2, contentvalues, s, as);
		if (i > 0)
			logd((new StringBuilder()).append("Notifying change URI=").append(uri.toString()).toString());
		else
			logd((new StringBuilder()).append("Affected rows: ").append(i).toString());
		contentResolver.notifyChange(uri, null);
		return i;
		
		
	}



}
