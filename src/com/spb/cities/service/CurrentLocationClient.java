// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.service;

import android.app.Application;
import android.content.*;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.*;
import android.os.Process;
import android.text.format.Time;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.CurrentLocationInfo;
import com.spb.cities.location.CurrentLocationPreferences;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.spb.cities.service:
//			CurrentLocationService

public class CurrentLocationClient
	implements android.content.SharedPreferences.OnSharedPreferenceChangeListener
{
	class CurrentLocationObserver extends ContentObserver
	{

		final CurrentLocationClient this$0;

		public void onChange(boolean flag)
		{
			int i;
			logd((new StringBuilder()).append("CurrentLocationObserver.onChange: uri=").append(com.spb.cities.provider.CitiesContract.CurrentLocation.getContentUri(application)).toString());
			i = com.spb.cities.provider.CitiesContract.CurrentLocation.queryCurrentLocationInfo(application, contentResolver).getCityId();
			logd((new StringBuilder()).append("CurrentLocationObserver.onChange: new currrent location cityId=").append(i).toString());
			if (i != 0x80000000)
			{
				if (i > 0 && i != currentLocationCityId)
				{
					currentLocationCityId = i;
					notifyCurrentLocationCityIdUpdated(i);
				}
			}
			else 
			{
				logd((new StringBuilder()).append("CurrentLocationObserver.onChange: new current location uknown, keep using last known value: cityId=").append(currentLocationCityId).toString());
			}
			return;
		}

		public CurrentLocationObserver(Handler handler)
		{
			super(handler);
			this$0 = CurrentLocationClient.this;
			android.net.Uri uri = com.spb.cities.provider.CitiesContract.CurrentLocation.getContentUri(application);
			logd((new StringBuilder()).append("Registering content observer for URI: ").append(uri).toString());
			contentResolver.registerContentObserver(uri, true, this);
		}
	}

	private class DataHandler extends Handler
	{

		private static final int MSG_SCHEDULE_UPDATES = 2;
		private static final int MSG_UPDATE_CURRENT_LOCATION = 1;
		final CurrentLocationClient this$0;

		private void scheduleUpdates(Long long1)
		{
			CurrentLocationService.scheduleUpdates(application, long1.longValue());
		}

		private void updateCurrentLocation()
		{
			if (currentLocationObserver != null)
				currentLocationObserver.onChange(false);
		}

		public void handleMessage(Message message)
		{
			switch (message.what) {
			case 1:
				updateCurrentLocation();
				break;
			case 2:
				scheduleUpdates((Long)message.obj);
				break;
			default:
				break;
			}
			return;
			
		}

		void postScheduleUpdates(Long long1)
		{
			sendMessage(Message.obtain(this, 2, long1));
		}

		void postUpdateCurrentLocation()
		{
			sendMessage(Message.obtain(this, 1));
		}

		public DataHandler(Looper looper)
		{
			super(looper);
			this$0 = CurrentLocationClient.this;
		}
	}

	public static interface CurrentLocationListener
	{

		public abstract void onCurrenLocationCityIdUpdated(int i);
	}


	static int instanceCount = 0;
	private final Application application;
	private final ContentResolver contentResolver;
	private int currentLocationCityId;
	private final ArrayList currentLocationListeners;
	private CurrentLocationObserver currentLocationObserver;
	private CurrentLocationPreferences currentLocationPreferences;
	private DataHandler dataHandler;
	int instanceNo;
	Logger logger;

	public CurrentLocationClient(Application application1)
	{
		currentLocationListeners = new ArrayList();
		currentLocationCityId = 0x80000000;
	
		int i = 1 + instanceCount;
		instanceCount = i;
		instanceNo = i;

		logger = Loggers.getLogger(CurrentLocationClient.class);
		logd("Ctor >>>");
		application = application1;
		contentResolver = application1.getContentResolver();
		currentLocationPreferences = new CurrentLocationPreferences(application1);
		currentLocationPreferences.registerOnSharedPreferenceChangeListener(this);
		HandlerThread handlerthread = new HandlerThread("CurrentLocationClient.Data");
		handlerthread.start();
		dataHandler = new DataHandler(handlerthread.getLooper());
		logd("Ctor <<<");
		return;
	}

	public static CurrentLocationClient getInstance(Context context)
	{
		Context context1 = context.getApplicationContext();
		if (!(context1 instanceof Application))
			throw new IllegalArgumentException((new StringBuilder()).append("Application context is not an Application instance: ").append(context1).toString());
		Application application1 = (Application)context1;
		Class class1 = application1.getClass();
		CurrentLocationClient currentlocationclient;
		try
		{
			currentlocationclient = (CurrentLocationClient)class1.getMethod("getCurrentLocationClient", new Class[0]).invoke(application1, new Object[0]);
		}
		catch (NoSuchMethodException nosuchmethodexception)
		{
			throw new IllegalArgumentException("Application class must define getCurrentLocationClient() method", nosuchmethodexception);
		}
		catch (Exception exception)
		{
			throw new IllegalArgumentException("Failed to invoke getCurrentLocationClient() method", exception);
		}
		return currentlocationclient;
	}

	private static boolean isNetworkLocationProviderEnabled(Context context)
	{
		LocationManager locationmanager = (LocationManager)context.getSystemService("location");
		boolean flag;
		if (locationmanager != null && locationmanager.isProviderEnabled("network"))
			flag = true;
		else
			flag = false;
		return flag;
	}

	private void logd(String s)
	{
		logger.d(prepareMessage(s));
	}

	private void loge(String s, Throwable throwable)
	{
		logger.e(prepareMessage(s), throwable);
	}

	private void onUpdateRateChanged()
	{
		if (hasListeners())
		{
			long l = currentLocationPreferences.getUpdateIntervalMs();
			logd((new StringBuilder()).append("onUpdateRateChanged: new interval: ").append(l / 1000L / 60L).append(" min").toString());
			reschedultUpdates(l);
		} else
		{
			logd("onUpdateRateChanged: no registered listeners");
		}
	}

	private String prepareMessage(String s)
	{
		return (new StringBuilder()).append('[').append(Process.myPid()).append(':').append(Thread.currentThread().getName()).append(',').append(Thread.currentThread().getId()).append(':').append(instanceNo).append("] ").append(s).toString();
	}

	private boolean updateIntervalElapsed(CurrentLocationInfo currentlocationinfo)
	{
		Time time = new Time("UTC");
		time.setToNow();
		long l = time.toMillis(false);
		long l1 = currentLocationPreferences.getUpdateIntervalMs();
		long l2 = currentlocationinfo.getLastUpdatedMsUtc();
		logd((new StringBuilder()).append("considerUpdateCurrentLocation: latestSuccessfullTimestamp=").append(l2).toString());
		long l3 = l - l2;
		logd((new StringBuilder()).append("considerUpdateCurrentLocation: currentMillis=").append(l).toString());
		logd((new StringBuilder()).append("considerUpdateCurrentLocation: lastUpdated=").append(l2).toString());
		logd((new StringBuilder()).append("considerUpdateCurrentLocation: updateInterval=").append(l1).toString());
		logd((new StringBuilder()).append("considerUpdateCurrentLocation: updateDelay=").append(l).toString());
		logd((new StringBuilder()).append("considerUpdateCurrentLocation: update ").append(l3 / 60000L).append(" min ago, update interval ").append(l1 / 60000L).append(" min").toString());
		boolean flag;
		if (l3 >= l1)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public void considerUpdateCurrentLocation(boolean flag)
	{
		logd((new StringBuilder()).append("considerUpdateCurrentLocation >>> networkConnectedEvent=").append(flag).toString());
		if (hasListeners()) 
		{
			CurrentLocationInfo currentlocationinfo = com.spb.cities.provider.CitiesContract.CurrentLocation.queryCurrentLocationInfo(application, contentResolver);
			boolean flag1 = false;
			int i = currentlocationinfo.getPositioningStatus();
			if (currentlocationinfo == null)
			{
				logd("considerUpdateCurrentLocation: no update status for current location");
				flag1 = true;
			} else
			if (!updateIntervalElapsed(currentlocationinfo))
				logd("considerUpdateCurrentLocation: update interval has not yet elapsed.");
			else
			if (i == 0)
			{
				logd("considerUpdateCurrentLocation: update interval has elapsed.");
				logd("considerUpdateCurrentLocation: previous update was successful.");
				flag1 = true;
			} else
			if (!flag)
			{
				logd("considerUpdateCurrentLocation: update interval has elapsed.");
				logd("considerUpdateCurrentLocation: previous update was unsuccessful.");
				logd("considerUpdateCurrentLocation: it's not a network event");
				flag1 = true;
			} else
			if (i == 5 || i == 4 && isNetworkLocationProviderEnabled(application))
			{
				logd("considerUpdateCurrentLocation: update interval has elapsed.");
				logd("considerUpdateCurrentLocation: previous update was unsuccessful.");
				logd("considerUpdateCurrentLocation: it's a network event");
				logd("considerUpdateCurrentLocation: previous failure was due to network");
				flag1 = true;
			} else
			{
				logd("considerUpdateCurrentLocation: update interval has elapsed.");
				logd("considerUpdateCurrentLocation: previous update was unsuccessful.");
				logd("considerUpdateCurrentLocation: it's a network event");
				logd("considerUpdateCurrentLocation: previous failure was NOT due to network");
			}
			logd((new StringBuilder()).append("considerUpdateCurrentLocation <<< need update: ").append(flag1).toString());
			if (flag1)
				CurrentLocationService.updateNow(application, false);
			
		}
		else 
		{
			logger.d("considerUpdateCurrentLocation <<< no registered listeners, not updating");
		}
		return;
	}

	public void dispose()
	{
		logd("dispose >>>");
		currentLocationPreferences.dispose();
		if (dataHandler != null)
		{
			dataHandler.getLooper().quit();
			dataHandler = null;
		}
		logd("dispose <<<");
	}

	public int getCurrentLocationCityId()
	{
		return currentLocationCityId;
	}

	boolean hasListeners()
	{
		boolean flag;
		ArrayList arraylist = currentLocationListeners;
		synchronized (arraylist) {
			if (!currentLocationListeners.isEmpty())
				flag = true;
			else
				flag = false;
		}
		return flag;
	}

	void notifyCurrentLocationCityIdUpdated(int i)
	{
		logd((new StringBuilder()).append("notifyCurrentLocationCityIdUpdated >>> cityId=").append(i).toString());
		ArrayList arraylist = currentLocationListeners;
		synchronized (arraylist) {
			for (Iterator iterator = currentLocationListeners.iterator(); iterator.hasNext(); ((CurrentLocationListener)iterator.next()).onCurrenLocationCityIdUpdated(i));
		}
		logd((new StringBuilder()).append("notifyCurrentLocationCityIdUpdated >>> cityId=").append(i).toString());
		return;
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String s)
	{
		if ("update-interval".equals(s))
			onUpdateRateChanged();
	}

	public void postUpdateCurrentLocation()
	{
		if (dataHandler != null)
			dataHandler.postUpdateCurrentLocation();
	}

	public void registerCurrentLocationListener(CurrentLocationListener currentlocationlistener)
	{
		registerCurrentLocationListener(currentlocationlistener, true);
	}

	public void registerCurrentLocationListener(CurrentLocationListener currentlocationlistener, boolean flag)
	{
		int i;
		i = 1;
		int j = 0;
		
		logd((new StringBuilder()).append("GROUPER registerCurrentLocationListener >>> l=").append(currentlocationlistener).toString());
		ArrayList arraylist = currentLocationListeners;
		synchronized (arraylist) {
			if (currentLocationListeners.size() == 0)
			{
				j = i;
			}	
		}
		if (!currentLocationListeners.contains(currentlocationlistener))
			currentLocationListeners.add(currentlocationlistener);
		int k;
		if (currentLocationListeners.size() != i)
			i = 0;
		if ((j & i) != 0)
			startObservingCurrentLocation();
		k = currentLocationListeners.size();
		Exception exception;
		if (flag)
			if (currentLocationCityId != 0x80000000)
				notifyCurrentLocationCityIdUpdated(currentLocationCityId);
			else
				dataHandler.postUpdateCurrentLocation();
		logd((new StringBuilder()).append("GROUPER registerCurrentLocationListener <<< count=").append(k).append(" l=").append(currentlocationlistener).toString());
		return;
	
	}

	public void reschedultUpdates(long l)
	{
		if (dataHandler != null)
			dataHandler.postScheduleUpdates(Long.valueOf(l));
	}

	void startObservingCurrentLocation()
	{
		logger.d("GROUPER startObservingCurrentLocation:");
		if (currentLocationObserver == null)
		{
			currentLocationObserver = new CurrentLocationObserver(dataHandler);
			CurrentLocationService.updateNow(application, false);
			CurrentLocationService.rescheduleUpdates(application);
			dataHandler.postUpdateCurrentLocation();
		}
	}

	void stopObservingCurrentLocation()
	{
		logger.d("GROUPER stopObservingCurrentLocation:");
		if (currentLocationObserver != null)
		{
			contentResolver.unregisterContentObserver(currentLocationObserver);
			currentLocationObserver = null;
			CurrentLocationService.cancelUpdates(application);
		}
	}

	public void unregisterCurrentLocationListener(CurrentLocationListener currentlocationlistener)
	{
		int i;
		i = 1;
		int j = 0;
		logd((new StringBuilder()).append("GROUPER unregisterCurrentLocationListener >>> l=").append(currentlocationlistener).toString());
		ArrayList arraylist = currentLocationListeners;	
		synchronized (arraylist) {
			if (currentLocationListeners.size() == i)
				j = i;
		}
		currentLocationListeners.remove(currentlocationlistener);
		int k;
		if (currentLocationListeners.size() != 0)
			i = 0;
		if ((j & i) != 0)
			stopObservingCurrentLocation();
		k = currentLocationListeners.size();
		logd((new StringBuilder()).append("GROUPER unregisterCurrentLocationListener <<< count=").append(k).append(" l=").append(currentlocationlistener).toString());
		return;

	}








/*
	static int access$402(CurrentLocationClient currentlocationclient, int i)
	{
		currentlocationclient.currentLocationCityId = i;
		return i;
	}

*/
}
