// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.service;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.*;
import android.text.format.Time;
import com.softspb.updateservice.UpdateService;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.location.CurrentLocationPreferences;
import com.spb.cities.location.LocationClient;

public class CurrentLocationService extends Service
{
	class UpdateThread extends Thread
	{

		private final boolean forceUpdate;
		final CurrentLocationService this$0;
		private final long updateIntervalMs;

		public void run()
		{
			byte byte0;
			int i;
			CurrentLocationPreferences currentlocationpreferences;
			byte0 = 2;
			long l;
			i = 0x80000000;
			currentlocationpreferences = null;
			LocationClient locationclient = new LocationClient(CurrentLocationService.this);
			if (forceUpdate || updateIntervalMs <= 0L)
				return;
			else
			{
				l = updateIntervalMs / 3L;		
				CurrentLocationPreferences currentlocationpreferences1;
				locationclient.setExpirationMs(l);
				currentlocationpreferences1 = new CurrentLocationPreferences(CurrentLocationService.this);
				Location location;
				mLastLocation = currentlocationpreferences1.getLastKnownLocation();
				mLastNearestCityId = currentlocationpreferences1.getLastKnownNearestCityId();
				updatePositioningStatus(3);
				CurrentLocationService.logd("Updating current location...");
				CurrentLocationService.logd((new StringBuilder()).append("Last known location: ").append(mLastLocation).toString());
				CurrentLocationService.logd((new StringBuilder()).append("Last known nearest city ID: ").append(mLastNearestCityId).toString());
				location = locationclient.obtainLocation();
				CurrentLocationService.logd((new StringBuilder()).append("Obtained new location: ").append(location).toString());
				if (location == null)
				{
					CurrentLocationService.logd("Failed to obtain location.");
					byte0 = 4;
				}
				CurrentLocationService.logd("Update done.");
				
				if (byte0 != 0)
					updatePositioningStatus(byte0);
				else
					updateCurrentLocationInfo(byte0, i);
				if (currentlocationpreferences1 != null)
					currentlocationpreferences1.dispose();
				releaseLock();
				stopSelf();
				return;
			}
		}

		UpdateThread(boolean flag, long l)
		{
			super();
			this$0 = CurrentLocationService.this;
			forceUpdate = flag;
			updateIntervalMs = l;
		}
	}


	public static final String ACTION_UPDATE = "com.softspb.weather.service.CurrentLocationService.UPDATE";
	private static final long DEFAULT_UPDATE_INTERVAL_MS = 0x6ddd00L;
	private static final float LOCATION_THRESHOLD_METERS = 1000F;
	public static final String PARAM_FORCE_UPDATE = "location-force-update";
	public static final String PARAM_IS_SCHEDULED = "location-is-scheduled";
	private static final long REGULAR_UPDATES_DELAY_MS = 0x493e0L;
	public static final String TAG_POSITIONING = "Positioning";
	private static Logger logger = Loggers.getLogger(CurrentLocationService.class.getName());
	private Location mLastLocation;
	private int mLastNearestCityId;
	private android.os.PowerManager.WakeLock mWakeLock;
	private CurrentLocationPreferences prefs;

	public CurrentLocationService()
	{
		mLastNearestCityId = 0x80000000;
	}

	public static void cancelUpdates(Context context)
	{
		logd("GROUPER cancelUpdates");
		Intent intent = new Intent("com.softspb.weather.service.CurrentLocationService.UPDATE");
		intent.setComponent(new ComponentName(context,CurrentLocationService.class));
		PendingIntent pendingintent = PendingIntent.getService(context, 0, intent, 0x10000000);
		((AlarmManager)context.getSystemService("alarm")).cancel(pendingintent);
	}

	private static void logTrace()
	{
		StackTraceElement astacktraceelement[] = Thread.currentThread().getStackTrace();
		for (int i = 3; i < astacktraceelement.length; i++)
			logger.d((new StringBuilder()).append("|   ").append(astacktraceelement[i]).toString());

	}

	private static void logd(String s)
	{
		logger.d(s);
	}

	private static void loge(String s, Throwable throwable)
	{
		logger.e(s, throwable);
	}

	public static void rescheduleUpdates(Context context)
	{
		CurrentLocationPreferences currentlocationpreferences = new CurrentLocationPreferences(context);
		long l = currentlocationpreferences.getUpdateIntervalMs();
		logd((new StringBuilder()).append("GROUPER rescheduleUpdates: using intervalMs=").append(l).toString());
		scheduleUpdates_Int(context, l);
		currentlocationpreferences.dispose();
		return;
	}

	public static void scheduleUpdates(Context context, long l)
	{
		logd((new StringBuilder()).append("GROUPER scheduleUpdates: intervalMs=").append(l).toString());
		scheduleUpdates_Int(context, l);
	}

	private static void scheduleUpdates_Int(Context context, long l)
	{
		Intent intent = new Intent("com.softspb.weather.service.CurrentLocationService.UPDATE");
		intent.putExtra("location-is-scheduled", true);
		intent.setComponent(new ComponentName(context, CurrentLocationService.class));
		PendingIntent pendingintent = PendingIntent.getService(context, 0, intent, 0x10000000);
		((AlarmManager)context.getSystemService("alarm")).setInexactRepeating(2, 0x493e0L + SystemClock.elapsedRealtime(), l, pendingintent);
	}

	private void updateCurrentLocationInfo(int i, int j)
	{
		logd((new StringBuilder()).append("updateCurrentLocationInfo: positioningStatus=").append(i).append(" cityId=").append(j).toString());
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("positioning_status", Integer.valueOf(i));
		contentvalues.put("city_id", Integer.valueOf(j));
		Time time = new Time("UTC");
		time.setToNow();
		contentvalues.put("last_updated_utc", Long.valueOf(time.toMillis(false)));
		getContentResolver().insert(com.spb.cities.provider.CitiesContract.CurrentLocation.getContentUri(getBaseContext()), contentvalues);
	}

	public static void updateNow(Context context, boolean flag)
	{
		logger.d((new StringBuilder()).append("updateNow: forceUpdate=").append(flag).toString());
		logTrace();
		Intent intent = new Intent("com.softspb.weather.service.CurrentLocationService.UPDATE");
		intent.putExtra("location-force-update", flag);
		intent.setComponent(new ComponentName(context, CurrentLocationService.class));
		context.startService(intent);
	}

	private void updatePositioningStatus(int i)
	{
		logd((new StringBuilder()).append("updatePositioningStatus: positioningStatus=").append(i).toString());
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("positioning_status", Integer.valueOf(i));
		getContentResolver().update(com.spb.cities.provider.CitiesContract.CurrentLocation.getContentUri(getBaseContext()), contentvalues, null, null);
	}

	void doUpdate(boolean flag, long l)
	{
		UpdateThread updatethread = new UpdateThread(flag, l);
		mWakeLock.acquire();
		updatethread.start();
	}

	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public void onCreate()
	{
		super.onCreate();
		mWakeLock = ((PowerManager)getSystemService("power")).newWakeLock(1, getClass().getSimpleName());
		prefs = new CurrentLocationPreferences(this);
	}

	public void onDestroy()
	{
		super.onDestroy();
		prefs.dispose();
	}

	public int onStartCommand(Intent intent, int i, int j)
	{
		boolean flag = false;
		logger.d((new StringBuilder()).append("onStartCommand: intetnt=").append(intent).append(" flags=").append(Integer.toHexString(i)).append(" startId=").append(j).toString());
		String s;
		boolean flag1;
		boolean flag2;
		if (intent == null)
			s = null;
		else
			s = intent.getAction();
		if (intent == null)
			flag1 = false;
		else
			flag1 = intent.getBooleanExtra("location-force-update", false);
		if (intent != null)
			flag = intent.getBooleanExtra("location-is-scheduled", false);
		flag2 = prefs.isUseOnlyWifi();
		logger.d((new StringBuilder()).append("    action=").append(s).append(" forceUpdate=").append(" isScheduled=").append(flag).append(" useOnlyWifi=").append(flag2).toString());
		if ("com.softspb.weather.service.CurrentLocationService.UPDATE".equals(s))
			if (!UpdateService.checkNetworkAvailabilityAndSettings(this, flag1, flag2, logger))
			{
				logger.d("Update is not allowed, stop.");
				stopSelf();
			} else
			{
				doUpdate(flag1, prefs.getUpdateIntervalMs());
			}
		return 2;
	}

	int queryNearestCityId(Location location)
	{
		int i = 0;
		com.spb.cities.nearestcity.NearestCitiesClient.QueryParams queryparams = new com.spb.cities.nearestcity.NearestCitiesClient.QueryParams(location, 1);
		if (queryparams != null)
		{
			Uri uri;
			Cursor cursor;
			uri = com.spb.cities.provider.CitiesContract.Cities.buildNearestQueryUri(getBaseContext(), queryparams.getLat(), queryparams.getLon(), queryparams.getLimit());
			cursor = null;
			int j;
			cursor = getContentResolver().query(uri, com.spb.cities.provider.CitiesContract.Cities.NEAREST_PROJECTION, null, null, null);
			if (!(cursor == null || !cursor.moveToFirst()))
			{
				j = cursor.getInt(1);
				i = j;
			}
			if (cursor != null)
				cursor.close();
		}
		return i;
		
	}

	void releaseLock()
	{
		if (mWakeLock != null && mWakeLock.isHeld())
			mWakeLock.release();
	}

	boolean updateCurrentLocation(int i)
	{
		ContentValues contentvalues;
		contentvalues = new ContentValues();
		contentvalues.put("city_id", Integer.valueOf(i));
		boolean flag1;
		Uri uri = com.spb.cities.provider.CitiesContract.Cities.getContentUri(getBaseContext());
		flag1 = uri.equals(getContentResolver().insert(uri, contentvalues));
		boolean flag = flag1;
		return flag;
	}




/*
	static Location access$002(CurrentLocationService currentlocationservice, Location location)
	{
		currentlocationservice.mLastLocation = location;
		return location;
	}

*/



/*
	static int access$102(CurrentLocationService currentlocationservice, int i)
	{
		currentlocationservice.mLastNearestCityId = i;
		return i;
	}

*/




}
