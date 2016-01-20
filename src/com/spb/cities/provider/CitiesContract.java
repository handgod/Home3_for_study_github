// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import com.spb.cities.CurrentLocationInfo;

public final class CitiesContract
{
	public static final class CurrentLocation
		implements CurrentLocationColumns
	{

		public static final String CONTENT_PATH = "currentlocation";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.currentlocation";
		public static final int DEFAULT_INDEX_CITY_ID = 0;
		public static final int DEFAULT_INDEX_LAST_UPDATED_TIMESTAMP_UTC = 2;
		public static final int DEFAULT_INDEX_POSITIONING_STATUS = 1;
		public static final String DEFAULT_PROJECTION[];
		public static final int POSITIONING_STATUS_FAILED = 1;
		public static final int POSITIONING_STATUS_FAILED_NEAREST_CITY_QUERY = 5;
		public static final int POSITIONING_STATUS_FAILED_POSITIONING = 4;
		public static final int POSITIONING_STATUS_OK = 0;
		public static final int POSITIONING_STATUS_UNKNOWN = 2;
		public static final int POSITIONING_STATUS_UPDATING = 3;
		private static Uri contentUri;

		public static Uri getContentUri(Context context)
		{
			if (contentUri != null) 
				{
				
				}
			else 
				{
				if (contentUri == null)
					contentUri = (new android.net.Uri.Builder()).scheme("content").authority(CitiesContract.getAuthority(context)).appendPath("currentlocation").build();

				}
			return contentUri;
		}

		public static boolean isStatusFailed(int i)
		{
			boolean flag = true;
			if (i != 1 && i != 4 && i != 5)
				flag = false;
			return flag;
		}

		public static CurrentLocationInfo queryCurrentLocationInfo(Context context, ContentResolver contentresolver)
		{
			Cursor cursor = null;
			CurrentLocationInfo currentlocationinfo = null;
			cursor = contentresolver.query(getContentUri(context), DEFAULT_PROJECTION, null, null, null);
			if (!(cursor == null || !cursor.moveToFirst()))
			{
			currentlocationinfo = new CurrentLocationInfo(cursor.getInt(0), cursor.getInt(1), cursor.getLong(2));
			}
			return currentlocationinfo;
		}

		static 
		{
			String as[] = new String[3];
			as[0] = "city_id";
			as[1] = "positioning_status";
			as[2] = "last_updated_utc";
			DEFAULT_PROJECTION = as;
		}

		private CurrentLocation()
		{
		}
	}

	public static interface CurrentLocationColumns
	{

		public static final String CITY_ID = "city_id";
		public static final String LAST_UPDATED_TIMESTAMP_UTC = "last_updated_utc";
		public static final String POSITIONING_STATUS = "positioning_status";
	}

	public static final class Cities
		implements BaseColumns, CitiesColumns
	{

		public static final int CITY_ID_UNKNOWN = 0x80000000;
		public static final String CONTENT_FILTER_PATH = "city_filter";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.softspb.city";
		public static final String CONTENT_PATH = "city";
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.city";
		public static final int CURRENT_LOCATION_CITY_ID = -1024;
		public static final int DEFAULT_CITY_ID_INDEX = 1;
		public static final int DEFAULT_CITY_NAME_INDEX = 2;
		public static final int DEFAULT_COUNTRY_SHORT_NAME_INDEX = 3;
		public static final int DEFAULT_ID_INDEX = 0;
		public static final String DEFAULT_PROJECTION[];
		public static final int DEFAULT_STATE_SHORT_NAME_INDEX = 4;
		public static final int NEAREST_CITY_ID_INDEX = 1;
		public static final int NEAREST_CITY_NAME_INDEX = 2;
		public static final String NEAREST_CONTENT_PATH = "nearest";
		public static final int NEAREST_ID_INDEX = 0;
		public static final String NEAREST_PROJECTION[];
		public static final String NEAREST_QUERY_PARAM_LATITUDE = "lat";
		public static final String NEAREST_QUERY_PARAM_LIMIT = "limit";
		public static final String NEAREST_QUERY_PARAM_LONGITUDE = "lon";
		private static Uri contentFilterUri;
		private static Uri contentUri;
		private static Uri nearestContentUri;

		public static Uri buildNearestQueryUri(Context context, String s, String s1, String s2)
		{
			return (new android.net.Uri.Builder()).scheme("content").authority(CitiesContract.getAuthority(context)).appendPath("nearest").appendQueryParameter("lat", s).appendQueryParameter("lon", s1).appendQueryParameter("limit", s2).build();
		}

		static void checkInitUris(Context context)
		{
			if (contentUri == null)
				initUris(CitiesContract.getAuthority(context));
		}

		public static Uri getContentFilterUri(Context context)
		{
			checkInitUris(context);
			return contentFilterUri;
		}

		public static Uri getContentUri(Context context)
		{
			checkInitUris(context);
			return contentUri;
		}

		public static Uri getNearestContentUri(Context context)
		{
			checkInitUris(context);
			return nearestContentUri;
		}

		static void initUris(String s)
		{
			if (contentUri == null)
			{
				contentUri = (new android.net.Uri.Builder()).scheme("content").authority(s).appendPath("city").build();
				contentFilterUri = (new android.net.Uri.Builder()).scheme("content").authority(s).appendPath("city_filter").build();
				nearestContentUri = (new android.net.Uri.Builder()).scheme("content").authority(s).appendPath("nearest").build();
			}
		}

		static 
		{
			String as[] = new String[5];
			as[0] = "_id";
			as[1] = "city_id";
			as[2] = "city_name";
			as[3] = "country_short_name";
			as[4] = "state_short_name";
			DEFAULT_PROJECTION = as;
			String as1[] = new String[3];
			as1[0] = "_id";
			as1[1] = "city_id";
			as1[2] = "city_name";
			NEAREST_PROJECTION = as1;
		}

		public Cities()
		{
		}
	}

	public static interface CitiesColumns
	{

		public static final String CITY_ID = "city_id";
		public static final String CITY_NAME = "city_name";
		public static final String CITY_NAME_JP = "city_name_jp";
		public static final String COUNTRY_SHORT_NAME = "country_short_name";
		public static final String COUNTRY_SHORT_NAME_JP = "country_short_name_jp";
		public static final String FILTER_NAME = "filter_name";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String STATE_SHORT_NAME = "state_short_name";
		public static final String TIMEZONE_ID = "timezone_id";
		public static final String TIMEZONE_NAME = "timezone_name";
		public static final String UTC_OFFSET_MINUTES = "utc_offset_min";
	}


	private static final String AUTHORITY_SUFFIX = ".cities";
	private static String authority;

	private CitiesContract()
	{
	}

	public static String getAuthority(Context context)
	{
		if (authority == null)
			authority = (new StringBuilder()).append(context.getPackageName()).append(".cities").toString();
		return authority;
	}
}
