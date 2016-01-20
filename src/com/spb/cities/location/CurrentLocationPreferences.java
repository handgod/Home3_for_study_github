// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.location;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.os.Parcel;
import com.softspb.updateservice.UpdatePreferences;
import com.softspb.util.Base64;
import com.softspb.util.CrossProcessusPreferences;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class CurrentLocationPreferences extends CrossProcessusPreferences
	implements UpdatePreferences
{

	private static final long DEFAULT_UPDATE_PERIOD = 0x6ddd00L;
	private static final String PREFS_NAME = "current-location";
	private static final String PREF_LAST_KNOWN_LOCATION = "last-known-location";
	private static final String PREF_LAST_KNOWN_NEAREST_CITY_ID = "last-known-nearest-city-id";
	private static final Logger logger = Loggers.getLogger(CurrentLocationPreferences.class);
	private Context context;

	public CurrentLocationPreferences(Context context1)
	{
		super(context1, context1.getPackageName(), "current-location");
		context = context1;
	}

	private static String encodeByteArray(byte abyte0[])
	{
		String s;
		if (abyte0 == null)
		{
			s = "<null>";
		} else
		{
			StringBuilder stringbuilder = new StringBuilder();
			int i = abyte0.length;
			for (int j = 0; j < i; j++)
			{
				int k = abyte0[j];
				if (k < 0)
					k += 256;
				String s1 = Integer.toHexString(k);
				if (s1.length() < 2)
					stringbuilder.append('0');
				stringbuilder.append(s1);
			}

			s = stringbuilder.toString();
		}
		return s;
	}

	private static final void logd(String s)
	{
		logger.d(s);
	}

	public long getDefaultUpdateInterval(Context context1)
	{
		int i = context1.getResources().getInteger(com.spb.cities.R.integer.current_location_default_update_period);
		long l = i;

		return l;
	}

	public Location getLastKnownLocation()
	{
		String s = getString("last-known-location", null);
		Location location;
		if (s != null) 
		{
			Parcel parcel = null;
			byte abyte0[] =null;// Base64.decode(s, 0);
			logger.d((new StringBuilder()).append("Decoded loc bytes: ").append(encodeByteArray(abyte0)).toString());
			parcel = Parcel.obtain();
			parcel.unmarshall(abyte0, 0, abyte0.length);
			parcel.setDataPosition(0);
			location = (Location)Location.CREATOR.createFromParcel(parcel);
			logger.d((new StringBuilder()).append("Restored from preferences: ").append(location).toString());
			if (parcel != null)
				parcel.recycle();
		}
		else 
		{
			location = null;
		}
		return location;
	}

	public int getLastKnownNearestCityId()
	{
		return getInt("last-known-nearest-city-id", 0x80000000);
	}

	public long getUpdateIntervalMs()
	{
		long l = getLong("update-interval", -1L);
		if (l == -1L)
			l = getDefaultUpdateInterval(context);
		logd((new StringBuilder()).append("getUpdateIntervalMs <<< ").append(l).toString());
		return l;
	}

	public boolean isUseOnlyWifi()
	{
		logd("isUseOnlyWifi >>>");
		boolean flag = getBoolean("use-only-wifi", false);
		logd((new StringBuilder()).append("isUseOnlyWifi <<< ").append(flag).toString());
		return flag;
	}

	public void setLastKnownLocation(Location location, int i)
	{
		Parcel parcel = Parcel.obtain();
		location.writeToParcel(parcel, 0);
		byte abyte0[] = parcel.marshall();
		parcel.recycle();
		String s = "";//Base64.encodeToString(abyte0, 0);
		android.content.SharedPreferences.Editor editor = edit();
		editor.putString("last-known-location", s);
		editor.putInt("last-known-nearest-city-id", i);
		editor.commit();
		logger.d((new StringBuilder()).append("Saved to preferences: location=").append(location.toString()).append(" cityId=").append(i).toString());
	}

	public void setUpdateIntervalMs(long l)
	{
		logd((new StringBuilder()).append("setUpdateIntervalMs: ").append(l).toString());
		edit().putLong("update-interval", l).commit();
	}

	public void setUseOnlyWifi(boolean flag)
	{
		logd((new StringBuilder()).append("setUseOnlyWifi: ").append(flag).toString());
		edit().putBoolean("use-only-wifi", flag).commit();
	}

}
