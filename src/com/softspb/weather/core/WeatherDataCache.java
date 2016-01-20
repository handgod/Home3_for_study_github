// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import android.app.Application;
import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.softspb.util.DecimalDateTimeEncoding;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.softspb.weather.model.*;
import com.spb.cities.service.CurrentLocationClient;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package com.softspb.weather.core:
//			WeatherApplication, WeatherApplicationPreferences, ScheduleInfo

public class WeatherDataCache
	implements com.spb.cities.service.CurrentLocationClient.CurrentLocationListener, android.content.SharedPreferences.OnSharedPreferenceChangeListener
{
	class PositioningStatusObserver extends ContentObserver
	{

		final WeatherDataCache this$0;
		private Uri uri;

		public void onChange(boolean flag)
		{
			log((new StringBuilder()).append("onChange: uri=").append(uri).toString());
			UpdateStatus updatestatus = queryUpdateStatus(-1024, uri);
			if (updatestatus != null)
				updateStatusCache.put(-1024, updatestatus);
			if (updatestatus == null)
				updatestatus = (UpdateStatus)updateStatusCache.get(-1024);
			notifyUpdateStatus(-1024, updatestatus);
		}

		PositioningStatusObserver(Handler handler)
		{
			super(handler);
			this$0 = WeatherDataCache.this;
			uri = ContentUris.withAppendedId(com.softspb.weather.provider.WeatherMetaData.UpdateStatusMetaData.getContentUri(application), -1024L);
			log((new StringBuilder()).append("Registering content observer for URI: ").append(uri).toString());
			contentResolver.registerContentObserver(uri, true, this);
		}
	}

	class UpdatedStatusObserver extends ContentObserver
	{

		int cityId;
		final WeatherDataCache this$0;
		Uri uri;

		public void onChange(boolean flag)
		{
			log((new StringBuilder()).append("onChange: uri=").append(uri).toString());
			UpdateStatus updatestatus = queryUpdateStatus(cityId, uri);
			if (updatestatus != null)
				updateStatusCache.put(cityId, updatestatus);
			if (updatestatus == null)
				updatestatus = (UpdateStatus)updateStatusCache.get(cityId);
			notifyUpdateStatus(cityId, updatestatus);
		}

		UpdatedStatusObserver(Handler handler, int i)
		{
			super(handler);
			this$0 = WeatherDataCache.this;
			cityId = i;
			uri = ContentUris.withAppendedId(com.softspb.weather.provider.WeatherMetaData.UpdateStatusMetaData.getContentUri(application), i);
			log((new StringBuilder()).append("Registering content observer for URI: ").append(uri).toString());
			contentResolver.registerContentObserver(uri, true, this);
		}
	}

	class CityNameObserver extends ContentObserver
	{

		int mCityId;
		Uri mUri;
		final WeatherDataCache this$0;

		public void onChange(boolean flag)
		{
			log((new StringBuilder()).append("onChange: uri=").append(mUri).toString());
			CityInfo cityinfo = queryCity(mUri);
			if (cityinfo != null)
				cityNameCache.put(mCityId, cityinfo);
			if (cityinfo == null)
				cityinfo = (CityInfo)cityNameCache.get(mCityId);
			if (cityinfo != null)
				notifyCityNameUpdated(mCityId, cityinfo.cityName);
		}

		CityNameObserver(Handler handler, int i)
		{
			super(handler);
			this$0 = WeatherDataCache.this;
			mCityId = i;
			mUri = ContentUris.withAppendedId(com.spb.cities.provider.CitiesContract.Cities.getContentUri(application), i);
			log((new StringBuilder()).append("Registering content observer for URI: ").append(mUri).toString());
			contentResolver.registerContentObserver(mUri, true, this);
		}
	}

	class CurrentObserver extends ContentObserver
	{

		int mCityId;
		Uri mUri;
		final WeatherDataCache this$0;

		public void onChange(boolean flag)
		{
			log((new StringBuilder()).append("onChange: uri=").append(mUri).toString());
			CurrentConditions currentconditions = queryCurrent(mUri);
			if (currentconditions != null)
				currentCache.put(mCityId, currentconditions);
			if (currentconditions == null)
				currentconditions = (CurrentConditions)currentCache.get(mCityId);
			notifyCurrentUpdated(mCityId, currentconditions);
		}

		CurrentObserver(Handler handler, int i)
		{
			super(handler);
			this$0 = WeatherDataCache.this;
			mCityId = i;
			mUri = ContentUris.withAppendedId(com.softspb.weather.provider.WeatherMetaData.CurrentMetaData.getContentUri(application), i);
			log((new StringBuilder()).append("Registering content observer for URI: ").append(mUri).toString());
			contentResolver.registerContentObserver(mUri, true, this);
		}
	}

	class DetailedForecastObserver extends ContentObserver
	{

		int mCityId;
		Uri mUri;
		final WeatherDataCache this$0;

		public void onChange(boolean flag)
		{
			log((new StringBuilder()).append("onChange: uri=").append(mUri).toString());
			Forecast aforecast[] = queryDetailedForecast(mUri);
			if (aforecast != null)
				detailedForecastCache.put(mCityId, aforecast);
			if (aforecast == null)
				aforecast = (Forecast[])detailedForecastCache.get(mCityId);
			notifyDetailedForecastUpdated(mCityId, aforecast);
		}

		DetailedForecastObserver(Handler handler, int i)
		{
			super(handler);
			this$0 = WeatherDataCache.this;
			mCityId = i;
			mUri = com.softspb.weather.provider.WeatherMetaData.TimeOfDayForecastMetaData.getUri(application, i);
			log((new StringBuilder()).append("Registering content observer for URI: ").append(mUri).toString());
			contentResolver.registerContentObserver(mUri, true, this);
		}
	}

	class RawForecastObserver extends ContentObserver
	{

		int cityId;
		int date;
		Uri observeUri;
		Uri queryUri;
		final WeatherDataCache this$0;

		public void onChange(boolean flag)
		{
			log((new StringBuilder()).append("onChange: uri=").append(observeUri).toString());
			int i = DecimalDateTimeEncoding.getTodayDateEncoded();
			if (date != i)
			{
				date = i;
				queryUri = com.softspb.weather.provider.WeatherMetaData.ForecastMetaData.getCityDateUri(application, cityId, i);
			}
			Forecast aforecast[] = queryRawForecast(queryUri);
			if (aforecast != null)
				rawForecastCache.put(cityId, aforecast);
			if (aforecast == null)
				aforecast = (Forecast[])rawForecastCache.get(cityId);
			notifyRawForecastUpdated(cityId, aforecast);
		}

		RawForecastObserver(Handler handler, int i)
		{
			super(handler);
			this$0 = WeatherDataCache.this;
			cityId = i;
			observeUri = com.softspb.weather.provider.WeatherMetaData.ForecastMetaData.getCityUri(application, i);
			log((new StringBuilder()).append("Registering content observer for URI: ").append(observeUri).toString());
			contentResolver.registerContentObserver(observeUri, true, this);
		}
	}

	class ForecastObserver extends ContentObserver
	{

		int mCityId;
		Uri mUri;
		final WeatherDataCache this$0;

		public void onChange(boolean flag)
		{
			log((new StringBuilder()).append("onChange: uri=").append(mUri).toString());
			Forecast aforecast[] = queryForecast(mUri);
			if (aforecast != null)
				forecastCache.put(mCityId, aforecast);
			if (aforecast == null)
				aforecast = (Forecast[])forecastCache.get(mCityId);
			notifyForecastUpdated(mCityId, aforecast);
		}

		ForecastObserver(Handler handler, int i)
		{
			super(handler);
			this$0 = WeatherDataCache.this;
			mCityId = i;
			mUri = ContentUris.withAppendedId(com.softspb.weather.provider.WeatherMetaData.DailyForecastMetaData.getContentUri(application), i);
			log((new StringBuilder()).append("Registering content observer for URI: ").append(mUri).toString());
			contentResolver.registerContentObserver(mUri, true, this);
		}
	}

	static class CityInfo
	{

		String cityName;
		int utcOffset;

		CityInfo()
		{
		}
	}

	public static interface WeatherListener
	{

		public abstract int getDetailedForecastDate();

		public abstract void onCityNameUpdated(int i, String s);

		public abstract void onCurrentUpdated(int i, CurrentConditions currentconditions);

		public abstract void onDetailedForecastUpdated(int i, Forecast aforecast[]);

		public abstract void onForecastUpdated(int i, Forecast aforecast[]);

		public abstract void onRawForecastUpdated(int i, Forecast aforecast[]);

		public abstract void onUpdateStatusChanged(int i, UpdateStatus updatestatus);
	}


	static final String ORDER_CURRENT_LATEST_FIRST = "date DESC,time DESC";
	private static final String ORDER_DATE_TIME = "date,time";
	private static int instanceCount = 0;
	private static Logger logger = Loggers.getLogger(WeatherDataCache.class.getName());
	private Application application;
	private final SparseIntArray cityIdsCount = new SparseIntArray();
	private final SparseArray cityNameCache = new SparseArray();
	protected final Runnable considerUpdateRunnable = new Runnable() {

		final WeatherDataCache this$0;

		public void run()
		{
			WeatherDataCache.logd("considerUpdateRunnable.run >>>");
			int j;
			for (Iterator iterator = resolveCurrentLocationCityIds(weatherPrefs.getAllCityIds()).iterator(); iterator.hasNext(); considerWeatherUpdate(j))
				j = ((Integer)iterator.next()).intValue();

			WeatherDataCache.logd("considerUpdateRunnable.run <<<");
		}

			
			{
				this$0 = WeatherDataCache.this;
//				super();
			}
	};
	private ContentResolver contentResolver;
	private final SparseArray currentCache = new SparseArray();
	private CurrentLocationClient currentLocationClient;
	private final ArrayList currentLocationListeners = new ArrayList();
	private final SparseArray detailedForecastCache = new SparseArray();
	private final SparseArray forecastCache = new SparseArray();
	private boolean isUsingCurrentLocation;
	private int mCurrentLocationCityId;
	protected Handler mHandler;
	private HandlerThread mHandlerThread;
	protected final SparseArray mObservers = new SparseArray();
	private PositioningStatusObserver positioningStatusObserver;
	private final SparseArray rawForecastCache = new SparseArray();
	protected final Runnable rescheduleUpdatesRunnable = new Runnable() {

		final WeatherDataCache this$0;

		public void run()
		{
			WeatherDataCache.logd("rescheduleUpdatesRunnable.run >>>");
			rescheduleWeatherUpdates();
			WeatherDataCache.logd("rescheduleUpdatesRunnable.run <<<");
		}

			
			{
				this$0 = WeatherDataCache.this;
//				super();
			}
	};
	private ScheduleInfo scheduleInfo;
	private String token;
	private final SparseArray updateStatusCache = new SparseArray();
	private final SparseArray weatherListeners = new SparseArray();
	private WeatherApplicationPreferences weatherPrefs;

	public WeatherDataCache(Application application1, CurrentLocationClient currentlocationclient)
	{
		StringBuilder stringbuilder = (new StringBuilder()).append(getClass().getClassLoader().hashCode()).append(":");
		int i = 1 + instanceCount;
		instanceCount = i;
		token = stringbuilder.append(i).toString();
		isUsingCurrentLocation = false;
		mCurrentLocationCityId = 0x80000000;
		logger.enableThreadLog();
		log("Ctor:");
		log((new StringBuilder()).append("    context=").append(application1.getPackageName()).toString());
		log((new StringBuilder()).append("    classLoader=").append(getClass().getClassLoader()).toString());
		log((new StringBuilder()).append("    thread=").append(Thread.currentThread().getName()).append(":").append(Thread.currentThread().getId()).toString());
		application = application1;
		contentResolver = application1.getContentResolver();
		currentLocationClient = currentlocationclient;
		start();
	}

	private void addCityId(int i)
	{
		logd((new StringBuilder()).append("addCityId: ").append(i).toString());
		SparseIntArray sparseintarray = cityIdsCount;
		synchronized (sparseintarray) {
			int j = cityIdsCount.get(i, 0x80000000);
			if (j == 0x80000000)
				j = 0;
			int k = j + 1;
			cityIdsCount.put(i, k);
			logd((new StringBuilder()).append("GROUPER addCityId ").append(i).append(", count=").append(k).toString());
			if (k == 1)
				if (i != -1024)
				{
					startObserving(i);
					considerWeatherUpdate(i);
					rescheduleWeatherUpdates();
				} else
				{
					startObservingCurrentLocation();
				}
		}		
		return;
	}

	private CityInfo getCityInfo(int i)
	{
		CityInfo cityinfo;
		if (i == -1024)
		{
			if (mCurrentLocationCityId > 0)
				cityinfo = getCityInfo(mCurrentLocationCityId);
			else
				cityinfo = null;
		} else
		{
			cityinfo = (CityInfo)cityNameCache.get(i);
			if (cityinfo != null)
			{
				log("    returning data from cache...");
			} else
			{
				log("    no cached data available, querying...");
				Uri uri = ContentUris.withAppendedId(com.spb.cities.provider.CitiesContract.Cities.getContentUri(application), i);
				log((new StringBuilder()).append("    uri=").append(uri).toString());
				cityinfo = queryCity(uri);
				if (cityinfo != null)
				{
					log("    data obtained, updating cache...");
					cityNameCache.put(i, cityinfo);
				} else
				{
					log("    data not avaible, returning null");
				}
			}
		}
		return cityinfo;
	}

	private UpdateStatus getDirectUpdateStatus(int i)
	{
		UpdateStatus updatestatus = (UpdateStatus)updateStatusCache.get(i);
		if (updatestatus == null)
		{
			updatestatus = queryUpdateStatus(i, ContentUris.withAppendedId(com.softspb.weather.provider.WeatherMetaData.UpdateStatusMetaData.getContentUri(application), i));
			if (updatestatus != null)
				updateStatusCache.put(i, updatestatus);
		}
		return updatestatus;
	}

	public static WeatherDataCache getInstance(Context context)
	{
		Context context1 = context.getApplicationContext();
		if (!(context1 instanceof Application))
			throw new IllegalArgumentException((new StringBuilder()).append("Application context is not an Application instance: ").append(context1).toString());
		Application application1 = (Application)context1;
		Class class1 = application1.getClass();
		WeatherDataCache weatherdatacache;
		try
		{
			weatherdatacache = (WeatherDataCache)class1.getMethod("getWeatherDataCache", new Class[0]).invoke(application1, new Object[0]);
		}
		catch (NoSuchMethodException nosuchmethodexception)
		{
			throw new IllegalArgumentException("Application class must define getWeatherDataCache() method", nosuchmethodexception);
		}
		catch (Exception exception)
		{
			throw new IllegalArgumentException("Failed to invoke getWeatherDataCache() method", exception);
		}
		return weatherdatacache;
	}

	private void log(String s)
	{
		logd((new StringBuilder()).append('[').append(token).append("] ").append(s).toString());
	}

	private static void logd(String s)
	{
		logger.d(s);
	}

	private void onUpdateRateChanged()
	{
		logd("onUpdateRateChanged");
		if (mHandler != null)
		{
			mHandler.removeCallbacks(considerUpdateRunnable);
			mHandler.post(considerUpdateRunnable);
			mHandler.removeCallbacks(rescheduleUpdatesRunnable);
			mHandler.post(rescheduleUpdatesRunnable);
		}
	}

	private void reload(int i)
	{
		logd((new StringBuilder()).append("reload: cityId=").append(i).toString());
		final List list = (List)mObservers.get(i);
		if (list != null)
			mHandler.post(new Runnable() {

				final WeatherDataCache this$0;
				final List val$list;

				public void run()
				{
					for (Iterator iterator = list.iterator(); iterator.hasNext(); ((ContentObserver)iterator.next()).onChange(false));
				}

			
			{
				this$0 = WeatherDataCache.this;
//				super();
				val$list = list;
			}
			});
	}

	private void removeCityId(int i)
	{
		log((new StringBuilder()).append("removeCityId: cityId=").append(i).toString());
		SparseIntArray sparseintarray = cityIdsCount;
		int k = 0;
		synchronized (sparseintarray) {
			
			int j = cityIdsCount.get(i, 0x80000000);
			if (j != 0x80000000)
			{
				k = j - 1;
				logd((new StringBuilder()).append("GROUPER removeCityId ").append(i).append(", count=").append(k).toString());
				if (k > 0)
					return;
				if (i != -1024)
				{
					stopObserving(i);
					rescheduleWeatherUpdates();
				} else
				{
					stopObservingCurrentLocation();
				}
				cityIdsCount.delete(i);
			}
		}
		
		cityIdsCount.put(i, k);

	}

	private Forecast[] selectForecast(Forecast aforecast[], int i)
	{
		int j = 0;
		int k;
		int l;
		if (aforecast == null)
			k = 0;
		else
			k = aforecast.length;
		for (l = 0; l < k; l++)
			if (aforecast[l].getDateLocal() == i)
				j++;

		Forecast aforecast1[];
		if (j == 0)
		{
			aforecast1 = null;
		} else
		{
			aforecast1 = new Forecast[j];
			int i1 = 0;
			int j1 = 0;
			while (i1 < k) 
			{
				int k1;
				if (aforecast[i1].getDateLocal() == i)
				{
					k1 = j1 + 1;
					aforecast1[j1] = aforecast[i1];
				} else
				{
					k1 = j1;
				}
				i1++;
				j1 = k1;
			}
		}
		return aforecast1;
	}
	
	  protected void considerWeatherUpdate(int paramInt)
	  {
		int i = 0;
	    logd("considerWeatherUpdate: cityId=" + paramInt);
	    if ((paramInt == -2147483648) || (paramInt == 64512))
	    {
	      Logger localLogger = logger;
	      String str = "considerWeatherUpdated: invalid cityId=" + paramInt;
	      localLogger.w(str);
	      return;
	    }
	 
	    UpdateStatus localUpdateStatus = getUpdateStatus(paramInt);
	    if (localUpdateStatus == null)
	    {
	      logd("considerWeatherUpdate: no update status for cityId=" + paramInt);
	      i = 1;
	    }
	    while (true)
	    {
	      logd("considerWeatherUpdate: need update: " + i);

	      List localList = Collections.singletonList(Integer.valueOf(paramInt));
	      Application localApplication = this.application;
	      WeatherApplication.updateWeather(localList, localApplication);

	      long l1 = System.currentTimeMillis();
	      long l2 = this.weatherPrefs.getUpdateIntervalMs();
	      StringBuilder localStringBuilder1 = new StringBuilder().append("considerWeatherUpdate: latestSuccessfulCurrentConditionsTimestamp=");
	      long l3 = localUpdateStatus.latestSuccessfulCurrentConditionsTimestamp;
//	      logd(l3);
	      StringBuilder localStringBuilder2 = new StringBuilder().append("considerWeatherUpdate: latestSuccessfulForecastTimestamp=");
	      long l4 = localUpdateStatus.latestSuccessfulForecastTimestamp;
//	      logd(l4);
	      long l5 = localUpdateStatus.latestSuccessfulCurrentConditionsTimestamp;
	      long l6 = localUpdateStatus.latestSuccessfulForecastTimestamp;
	      long l7 = Math.min(l5, l6);
	      long l8 = l1 - l7;
	      logd("considerWeatherUpdate: currentMillis=" + l1);
	      logd("considerWeatherUpdate: lastUpdated=" + l7);
	      logd("considerWeatherUpdate: updateInterval=" + l2);
	      logd("considerWeatherUpdate: updateDelay=" + l1);
	      StringBuilder localStringBuilder3 = new StringBuilder().append("considerWeatherUpdate: update ");
	      long l9 = l8 / 60000L;
	      StringBuilder localStringBuilder4 = localStringBuilder3.append(l9).append(" min ago, update interval ");
	      long l10 = l2 / 60000L;
	      logd(l10 + " min");
	      if (l8 < l2)
	        continue;
	      int j = 1;
	    }
	  }	

	protected String createCurrentLocationCityName(String s)
	{
		String s1;
		Application application1 = application;
		int i = R.string.weather_format_current_location;
		Object aobj[] = new Object[1];
		aobj[0] = s;
		s1 = application1.getString(i, aobj);
		logd((new StringBuilder()).append("Loaded resource id=0x").append(Integer.toHexString(R.string.weather_format_current_location)).toString());
		logd((new StringBuilder()).append("    mContext=").append(application.getPackageName()).toString());
		logd((new StringBuilder()).append("    classLoader=").append(getClass().getClassLoader()).toString());

		return s1;	
	}

	public String getCityName(int i)
	{
		log((new StringBuilder()).append("getCityName: cityId=").append(i).toString());
		CityInfo cityinfo = getCityInfo(i);
		String s;
		if (cityinfo == null)
			s = null;
		else
		if (i == -1024)
			s = createCurrentLocationCityName(cityinfo.cityName);
		else
			s = cityinfo.cityName;
		return s;
	}

	public CurrentConditions getCurrent(int i)
	{
		log((new StringBuilder()).append("getCurrent: cityId=").append(i).toString());
		CurrentConditions currentconditions;
		if (i == -1024)
		{
			if (mCurrentLocationCityId > 0)
				currentconditions = getCurrent(mCurrentLocationCityId);
			else
				currentconditions = null;
		} else
		{
			currentconditions = (CurrentConditions)currentCache.get(i);
			if (currentconditions != null)
			{
				log("    returning data from cache");
			} else
			{
				log("    no cached data available, querrying...");
				Uri uri = ContentUris.withAppendedId(com.softspb.weather.provider.WeatherMetaData.CurrentMetaData.getContentUri(application), i);
				log((new StringBuilder()).append("    uri=").append(uri).toString());
				currentconditions = queryCurrent(uri);
				if (currentconditions != null)
				{
					log("    obtained data, updating cache...");
					currentCache.put(i, currentconditions);
				} else
				{
					log("    no data available, returning null");
				}
			}
		}
		return currentconditions;
	}

	public int getCurrentLocationCityId()
	{
		return mCurrentLocationCityId;
	}

	public Forecast[] getDetailedForecast(int i, int j)
	{
		Forecast aforecast[] = null;
		log((new StringBuilder()).append("getDetailedForecast: cityId=").append(i).toString());
		if (i == -1024)
		{
			if (mCurrentLocationCityId > 0)
				aforecast = getDetailedForecast(mCurrentLocationCityId, j);
		} else
		{
			Forecast aforecast1[] = (Forecast[])detailedForecastCache.get(i);
			if (aforecast1 == null)
			{
				log("    no cached data available, querrying...");
				Uri uri = ContentUris.withAppendedId(com.softspb.weather.provider.WeatherMetaData.TimeOfDayForecastMetaData.getContentUri(application), i);
				log((new StringBuilder()).append("    uri=").append(uri).toString());
				aforecast1 = queryDetailedForecast(uri);
				if (aforecast1 != null)
				{
					log("    data obtained, updating cache...");
					detailedForecastCache.put(i, aforecast1);
				}
			} else
			{
				log("    using data from cache");
			}
			if (aforecast1 != null)
				aforecast = selectForecast(aforecast1, j);
			else
				log("    no data available, returning null");
		}
		return aforecast;
	}

	public Forecast[] getForecast(int i)
	{
		log((new StringBuilder()).append("getForecast: cityId=").append(i).toString());
		Forecast aforecast[];
		if (i == -1024)
		{
			if (mCurrentLocationCityId > 0)
				aforecast = getForecast(mCurrentLocationCityId);
			else
				aforecast = null;
		} else
		{
			aforecast = (Forecast[])forecastCache.get(i);
			if (aforecast != null)
			{
				log("getForecast:    returning data from cache...");
			} else
			{
				log("getForecast:    no cached data available, querrying...");
				Uri uri = ContentUris.withAppendedId(com.softspb.weather.provider.WeatherMetaData.DailyForecastMetaData.getContentUri(application), i);
				log((new StringBuilder()).append("getForecast:    uri=").append(uri).toString());
				aforecast = queryForecast(uri);
				if (aforecast != null)
				{
					log("getForecast:    data obtained, updating cache... ");
					forecastCache.put(i, aforecast);
				} else
				{
					log("getForecast:    data not avaible, returning null");
				}
			}
		}
		return aforecast;
	}

	public Forecast[] getRawForecast(int i)
	{
		log((new StringBuilder()).append("getRawForecast: cityId=").append(i).toString());
		Forecast aforecast[];
		if (i == -1024)
		{
			if (mCurrentLocationCityId > 0)
				aforecast = getRawForecast(mCurrentLocationCityId);
			else
				aforecast = null;
		} else
		{
			aforecast = (Forecast[])rawForecastCache.get(i);
			if (aforecast != null)
			{
				log("    returning data from cache...");
			} else
			{
				log("    no cached data available, querying...");
				int j = DecimalDateTimeEncoding.getTodayDateEncoded();
				Uri uri = com.softspb.weather.provider.WeatherMetaData.ForecastMetaData.getCityDateUri(application, i, j);
				log((new StringBuilder()).append("    uri=").append(uri).toString());
				aforecast = queryRawForecast(uri);
				if (aforecast != null)
				{
					log("    data obtained, updating cache... ");
					rawForecastCache.put(i, aforecast);
				} else
				{
					log("    data not avaible, returning null");
				}
			}
		}
		return aforecast;
	}

	public UpdateStatus getUpdateStatus(int paramInt)
	 {
	    String str1 = "getUpdateStatus >>> cityId=" + paramInt;
	    log(str1);
	    UpdateStatus localUpdateStatus2 = null;
	    if (paramInt == 64512)
	    {
	      if (this.mCurrentLocationCityId > 0)
	      {
	        int i = this.mCurrentLocationCityId;
	        UpdateStatus localUpdateStatus1 = getUpdateStatus(i);
	        if (localUpdateStatus1 != null)
	          localUpdateStatus2 = new UpdateStatus(localUpdateStatus1, paramInt);
	      }
	      else
	      {
	     
	      localUpdateStatus2 = null;
	      localUpdateStatus2 = getDirectUpdateStatus(paramInt);
	      String str2 = "getUpdateStatus <<< cityId=" + paramInt + " updateStatus=" + localUpdateStatus2;
	      log(str2);
	      }
	   }
	    return localUpdateStatus2;
	 }

	public int getUtcOffsetMin(int i)
	{
		log((new StringBuilder()).append("getUtcOffsetMin: cityId=").append(i).toString());
		CityInfo cityinfo = getCityInfo(i);
		int j;
		if (cityinfo == null)
			j = 0;
		else
			j = cityinfo.utcOffset;
		return j;
	}

	public boolean isRunning()
	{
		boolean flag;
		if (mHandlerThread != null && mHandlerThread.isAlive())
			flag = true;
		else
			flag = false;
		return flag;
	}

	void  notifyCityNameUpdated(int i, String s)
	{
		log((new StringBuilder()).append("notifyCityNameUpdated >>> cityId=").append(i).append(" cityName=").append(s).toString());
		logd("notifyCityNameUpdated: obtaining weather_lock...");
		SparseArray sparsearray = weatherListeners;
		synchronized (sparsearray) {
			logd("notifyCityNameUpdated: obtained weather_lock...");
			List list = (List)weatherListeners.get(i);
			if (list != null)
			{
				WeatherListener weatherlistener;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); weatherlistener.onCityNameUpdated(i, s))
				{
					weatherlistener = (WeatherListener)iterator.next();
					log((new StringBuilder()).append("notifyCityNameUpdated: notifying Weather listener: ").append(weatherlistener).toString());
				}

			}
		}
		
		if (i == mCurrentLocationCityId)
		{
			log("notifyCityNameUpdated: also notifying current location listeners");
			notifyCityNameUpdated(-1024, createCurrentLocationCityName(s));
		}
		log((new StringBuilder()).append("notifyCityNameUpdated <<< cityId=").append(i).append(" cityName=").append(s).toString());
		return;
	}

	void notifyCurrentLocationCityIdUpdated(int i)
	{
		log((new StringBuilder()).append("notifyCurrentLocationCityIdUpdated >>> cityId=").append(i).toString());
		ArrayList arraylist = currentLocationListeners;
		synchronized (arraylist) {
			for (Iterator iterator = currentLocationListeners.iterator(); iterator.hasNext(); ((com.spb.cities.service.CurrentLocationClient.CurrentLocationListener)iterator.next()).onCurrenLocationCityIdUpdated(i));
		}
		log((new StringBuilder()).append("notifyCurrentLocationCityIdUpdated >>> cityId=").append(i).toString());
		return;
	}

	void notifyCurrentUpdated(int i, CurrentConditions currentconditions)
	{
		log((new StringBuilder()).append("notifyCurrentUpdated >>> cityId=").append(i).toString());
		logd("notifyCurrentUpdated: obtaining weather_lock...");
		SparseArray sparsearray = weatherListeners;
		synchronized (sparsearray) {
			logd("notifyCurrentUpdated: obtained weather_lock");
			List list = (List)weatherListeners.get(i);
			if (list != null)
			{
				WeatherListener weatherlistener;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); weatherlistener.onCurrentUpdated(i, currentconditions))
				{
					weatherlistener = (WeatherListener)iterator.next();
					log((new StringBuilder()).append("notifyCurrentUpdated: notifying Weather listener: ").append(weatherlistener).toString());
				}

			}
		}		
		logd("notifyCurrentUpdated: released weather_lock");
		if (i == mCurrentLocationCityId)
		{
			log("notifyCurrentUpdated: also notifying current location listeners");
			notifyCurrentUpdated(-1024, currentconditions);
		}
		log((new StringBuilder()).append("notifyCurrentUpdated <<< cityId=").append(i).toString());
		return;
	}

	void notifyDetailedForecastUpdated(int i, Forecast aforecast[])
	{
		log((new StringBuilder()).append("notifyDetailedForecastUpdated >>> cityId=").append(i).toString());
		logd("notifyDetailedForecastUpdated: obtaining weather_lock...");
		SparseArray sparsearray = weatherListeners;
		synchronized (sparsearray) {
			logd("notifyDetailedForecastUpdated: obtained weather_lock");
			List list = (List)weatherListeners.get(i);
			if (list != null)
			{
				WeatherListener weatherlistener;
				Forecast aforecast1[];
				for (Iterator iterator = list.iterator(); iterator.hasNext(); weatherlistener.onDetailedForecastUpdated(i, aforecast1))
				{
					weatherlistener = (WeatherListener)iterator.next();
					aforecast1 = selectForecast(aforecast, weatherlistener.getDetailedForecastDate());
					log((new StringBuilder()).append("notifyDetailedForecastUpdated: notifying Weather listener: ").append(weatherlistener).toString());
				}

			}
		}
		
		logd("notifyDetailedForecastUpdated: released weather_lock");
		if (i == mCurrentLocationCityId)
		{
			log("notifyDetailedForecastUpdated: also notifying current location listeners");
			notifyDetailedForecastUpdated(-1024, aforecast);
		}
		log((new StringBuilder()).append("notifyDetailedForecastUpdated <<< cityId=").append(i).toString());
		return;
	}

	void notifyForecastUpdated(int i, Forecast aforecast[])
	{
		log((new StringBuilder()).append("notifyForecastUpdated >>> cityId=").append(i).toString());
		logd("notifyForecastUpdated: obtaining weather_lock...");
		SparseArray sparsearray = weatherListeners;
		synchronized (sparsearray) {
			logd("notifyForecastUpdated: obtained weather_lock...");
			List list = (List)weatherListeners.get(i);
			if (list != null)
			{
				WeatherListener weatherlistener;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); weatherlistener.onForecastUpdated(i, aforecast))
				{
					weatherlistener = (WeatherListener)iterator.next();
					log((new StringBuilder()).append("notifyForecastUpdated: notifying Weather listener: ").append(weatherlistener).toString());
				}

			}
		}
		
		logd("notifyForecastUpdated: released weather_lock...");
		if (i == mCurrentLocationCityId)
		{
			log("notifyForecastUpdated: also notifying current location listeners");
			notifyForecastUpdated(-1024, aforecast);
		}
		log((new StringBuilder()).append("notifyForecastUpdated <<< cityId=").append(i).toString());
		return;
	}

	void notifyNA(int i)
	{
		notifyCurrentUpdated(i, null);
		notifyForecastUpdated(i, null);
		notifyDetailedForecastUpdated(i, null);
		notifyUpdateStatus(i, null);
		notifyRawForecastUpdated(i, null);
		notifyCityNameUpdated(i, null);
	}

	void notifyRawForecastUpdated(int i, Forecast aforecast[])
	{
		log((new StringBuilder()).append("notifyRawForecastUpdated >>> cityId=").append(i).toString());
		logd("notifyRawForecastUpdated: obtaining weather_lock...");
		SparseArray sparsearray = weatherListeners;
		synchronized (sparsearray) {
			logd("notifyRawForecastUpdated: obtained weather_lock");
			List list = (List)weatherListeners.get(i);
			if (list != null)
			{
				WeatherListener weatherlistener;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); weatherlistener.onRawForecastUpdated(i, aforecast))
				{
					weatherlistener = (WeatherListener)iterator.next();
					log((new StringBuilder()).append("notifyRawForecastUpdated: notifying Weather listener: ").append(weatherlistener).toString());
				}

			}
		}
		logd("notifyRawForecastUpdated: released weather_lock");
		if (i == mCurrentLocationCityId)
		{
			log("notifyRawForecastUpdated: also notifying current location listeners");
			notifyRawForecastUpdated(-1024, aforecast);
		}
		log((new StringBuilder()).append("notifyRawForecastUpdated <<< cityId=").append(i).toString());
		return;
	}

	void notifyUpdateStatus(int i, UpdateStatus updatestatus)
	{
		log((new StringBuilder()).append("notifyUpdateStatus >>> cityId=").append(i).append(" updateStatus=").append(updatestatus).toString());
		logd("notifyUpdateStatus: obtaining weather_lock...");
		SparseArray sparsearray = weatherListeners;
		synchronized (sparsearray) {
			logd("notifyUpdateStatus: obtained weather_lock.");
			List list = (List)weatherListeners.get(i);
			if (list != null)
			{
				WeatherListener weatherlistener;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); weatherlistener.onUpdateStatusChanged(i, updatestatus))
				{
					weatherlistener = (WeatherListener)iterator.next();
					log((new StringBuilder()).append("notifyUpdateStatus: notifying Weather listener: ").append(weatherlistener).toString());
				}

			}
		}
		
		logd("notifyUpdateStatus: released weather_lock.");
		if (i == mCurrentLocationCityId)
		{
			log("notifyUpdateStatus: also notifying current location listeners");
			if (updatestatus != null)
				updatestatus = new UpdateStatus(updatestatus, -1024);
			notifyUpdateStatus(-1024, updatestatus);
		}
		log((new StringBuilder()).append("notifyUpdateStatus <<< cityId=").append(i).append(" updateStatus=").append(updatestatus).toString());
		return;
	}

	public void onCurrenLocationCityIdUpdated(int i)
	{
		if (i != 0x80000000)
		{
			if (i > 0 && i != mCurrentLocationCityId)
			{
				removeCityId(mCurrentLocationCityId);
				mCurrentLocationCityId = i;
				addCityId(mCurrentLocationCityId);
				reload(mCurrentLocationCityId);
				notifyCurrentLocationCityIdUpdated(i);
			}
		}
		else
		{
			logd((new StringBuilder()).append("CurrentLocationObserver.onChange: new current location uknown, keep using last known value: cityId=").append(mCurrentLocationCityId).toString());
		}
		return;
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String s)
	{
		if (s.equals("update-interval"))
			onUpdateRateChanged();
	}

	CityInfo queryCity(Uri uri)
	{
		CityInfo cityinfo;
		Cursor cursor;
		int i;
		cityinfo = null;
		log((new StringBuilder()).append("queryCityName: uri=").append(uri).toString());
		cursor = null;
		i = 0;
		ContentResolver contentresolver = contentResolver;
		String as[] = new String[2];
		as[0] = "city_name";
		as[1] = "utc_offset_min";
		cursor = contentresolver.query(uri, as, null, null, null);
		if (cursor == null || !cursor.moveToFirst())
		{
			if (cursor != null)
				cursor.close();
			log((new StringBuilder()).append("queryCityName: read ").append(0).append(" rows").toString());
		}
		else 
		{
			i = 0 + 1;
			cityinfo = new CityInfo();
			cityinfo.cityName = cursor.getString(0);
			cityinfo.utcOffset = cursor.getInt(1);
			if (cursor != null)
				cursor.close();
			log((new StringBuilder()).append("queryCityName: read ").append(i).append(" rows").toString());
		}
		return cityinfo;
	}

	CurrentConditions queryCurrent(Uri uri)
	{
		CurrentConditions currentconditions;
		Cursor cursor;
		currentconditions = null;
		log((new StringBuilder()).append("queryCurrent: uri=").append(uri).toString());
		cursor = null;
		cursor = contentResolver.query(uri, com.softspb.weather.provider.WeatherMetaData.CurrentMetaData.DEFAULT_PROJECTION, null, null, "date DESC,time DESC");
		if (cursor == null || !cursor.moveToFirst())
		{
			if (cursor != null)
				cursor.close();
			log((new StringBuilder()).append("queryCurrent: read ").append(0).append(" rows").toString());
		}
		else
		{
			CurrentConditions currentconditions1 = com.softspb.weather.provider.WeatherMetaData.CurrentMetaData.fromDefaultCursor(cursor);
			currentconditions = currentconditions1;
			int i = 0 + 1;
			if (cursor != null)
				cursor.close();
			log((new StringBuilder()).append("queryCurrent: read ").append(i).append(" rows").toString());
		}
		return currentconditions;
	}

	Forecast[] queryDetailedForecast(Uri uri)
	{
		int i;
		int j;
		Forecast aforecast[];
		Cursor cursor;
		log((new StringBuilder()).append("queryDetailedForecast: uri=").append(uri).toString());
		i = DecimalDateTimeEncoding.getTodayDateEncoded();
		j = 0;
		cursor = null;
		ContentResolver contentresolver = contentResolver;
		String as[] = com.softspb.weather.provider.WeatherMetaData.TimeOfDayForecastMetaData.DEFAULT_PROJECTION;
		String as1[] = new String[1];
		as1[0] = Integer.toString(i);
		cursor = contentresolver.query(uri, as, "date >= ?", as1, "time_of_day");
		if (cursor == null)
		{
			if (cursor != null)
				cursor.close();
			log((new StringBuilder()).append("queryDetailedForecast: read ").append(0).append(" rows").toString());
			aforecast = null;
		}
		else 
		{
			int k = cursor.getCount();
			aforecast = new Forecast[k];
			if (cursor.moveToFirst())
			{
				for (int l = 0; l < k && !cursor.isAfterLast(); cursor.moveToNext())
				{
					aforecast[l] = com.softspb.weather.provider.WeatherMetaData.TimeOfDayForecastMetaData.fromDefaultCursor(cursor);
					j++;
					l++;
				}

			}
			}
		return aforecast;
	}

	Forecast[] queryForecast(Uri uri)
	{
		int i;
		int j;
		Cursor cursor;
		Forecast aforecast[];
		log((new StringBuilder()).append("queryForecast: uri=").append(uri).toString());
		i = DecimalDateTimeEncoding.getTodayDateEncoded();
		j = 0;
		cursor = null;
		ContentResolver contentresolver = contentResolver;
		String as[] = com.softspb.weather.provider.WeatherMetaData.DailyForecastMetaData.DEFAULT_PROJECTION;
		String as1[] = new String[1];
		as1[0] = Integer.toString(i);
		cursor = contentresolver.query(uri, as, "date >= ?", as1, "date");
		if (cursor == null) 
		{
			if (cursor != null)
				cursor.close();
			log((new StringBuilder()).append("queryForecast: read ").append(0).append(" rows.").toString());
			aforecast = null;
		}
		else 
		{	
			int k = cursor.getCount();
			aforecast = new Forecast[k];
			if (cursor.moveToFirst())
			{
				for (int l = 0; l < k && !cursor.isAfterLast(); cursor.moveToNext())
				{
					aforecast[l] = com.softspb.weather.provider.WeatherMetaData.DailyForecastMetaData.fromDefaultCursor(cursor);
					j++;
					l++;
				}

			}
		}
		return aforecast;
	}

	Forecast[] queryRawForecast(Uri uri)
	{
		Forecast aforecast[];
		int i;
		Cursor cursor;
		aforecast = null;
		log((new StringBuilder()).append("queryRawForecast: uri=").append(uri).toString());
		i = 0;
		cursor = null;
		cursor = contentResolver.query(uri, com.softspb.weather.provider.WeatherMetaData.ForecastMetaData.DEFAULT_PROJECTION, null, null, "date,time");
		if (cursor == null)
		{
			if (cursor != null)
				cursor.close();
			log((new StringBuilder()).append("queryRawForecast: read ").append(0).append(" rows.").toString());
		}
		else 
		{
			int j = cursor.getCount();
			aforecast = new Forecast[j];
			if (cursor.moveToFirst())
			{
				for (int k = 0; k < j && !cursor.isAfterLast(); cursor.moveToNext())
				{
					aforecast[k] = com.softspb.weather.provider.WeatherMetaData.ForecastMetaData.fromDefaultCursor(cursor);
					i++;
					k++;
				}

			}
			if (cursor != null)
				cursor.close();
			log((new StringBuilder()).append("queryRawForecast: read ").append(i).append(" rows.").toString());	
		}
		return aforecast;
	}

	UpdateStatus queryUpdateStatus(int i, Uri uri)
	{
		Cursor cursor;
		UpdateStatus updatestatus;
		log((new StringBuilder()).append("queryUpdateStatus: uri=").append(uri).toString());
		cursor = null;
		updatestatus = null;
		UpdateStatus updatestatus1;
		cursor = contentResolver.query(uri, com.softspb.weather.provider.WeatherMetaData.UpdateStatusMetaData.DEFAULT_PROJECTION, null, null, null);
		if (!(cursor == null || !cursor.moveToFirst()))
		{
			updatestatus1 = com.softspb.weather.provider.WeatherMetaData.UpdateStatusMetaData.fromDefaultCursor(i, cursor);
			updatestatus = updatestatus1;
			if (cursor != null)
				cursor.close();
			log("queryUpdateStatus: query completed");
			log((new StringBuilder()).append("queryUpdateStatus: updateStatus=").append(updatestatus).toString());
		}
		return updatestatus;
	}

	public void registerWeatherListener(WeatherListener weatherlistener, int i, boolean flag)
	{
		log((new StringBuilder()).append("GROUPER registerWeatherListener >>> cityId=").append(i).append(" l=").append(weatherlistener).toString());
		log("registerWeatherListener: obtaining weather_lock...");
		synchronized (weatherListeners)
		{
			log("registerWeatherListener: obtained weather_lock");
			Object obj = (List)weatherListeners.get(i);
			if (obj == null)
			{
				obj = new LinkedList();
				weatherListeners.put(i, obj);
			}
			if (!((List) (obj)).contains(weatherlistener))
			{
				((List) (obj)).add(weatherlistener);
				addCityId(i);
			}
		}
		log("registerWeatherListener: released weather_lock");
		if (flag)
			if (i == -1024)
			{
				if (mCurrentLocationCityId != 0x80000000)
				{
					addCityId(mCurrentLocationCityId);
					reload(mCurrentLocationCityId);
				} else
				{
					notifyNA(-1024);
					currentLocationClient.postUpdateCurrentLocation();
				}
			} else
			{
				reload(i);
			}
		log((new StringBuilder()).append("GROUPER registerWeatherListener <<< cityId=").append(i).append(" l=").append(weatherlistener).toString());
		return;
	
	}
	
	public void rescheduleWeatherUpdates()
	{
		Iterator iterator = null;
		boolean flag;
		List list;
		ScheduleInfo scheduleinfo = null;
		logd("rescheduleWeatherUpdates");
		flag = false;
		list = resolveCurrentLocationCityIds(weatherPrefs.getAllCityIds());
		long l = weatherPrefs.getUpdateIntervalMs();
		if (scheduleInfo == null)
		{
			logd("rescheduleWeatherUpdates: no schedule info available");
			flag = true;
		} else
		{
			scheduleinfo = weatherPrefs.getScheduleInfo();
			if (scheduleinfo == null)
			{
				logd("rescheduleWeatherUpdates: stored schedule info not found");
				flag = true;
			} else
			if (scheduleinfo.scheduledTimestampToken != scheduleInfo.scheduledTimestampToken)
			{
				logd("rescheduleWeatherUpdates: schedule info outdated");
				flag = true;
			} else
			{
				logd((new StringBuilder()).append("rescheduleWeatherUpdates: found information about current schedule: ").append(scheduleinfo).toString());
				if (l != scheduleinfo.scheduledInterval)
				{
					logd((new StringBuilder()).append("rescheduleWeatherUpdates: new update interval: ").append(l / 60000L).append(" min").toString());
					flag = true;
				} else
				{
label0:
					{
						logd((new StringBuilder()).append("rescheduleWeatherUpdates: cityIds=").append(list).toString());
						if (list.size() == scheduleInfo.scheduledIds.size())
							break label0;
						logd("rescheduleWeatherUpdates: city IDs changed");
						flag = true;
					}
				}
			}
		}
		while(scheduleinfo.scheduledIds.contains(Integer.valueOf(((Integer)iterator.next()).intValue()))) 
			{
				while (!iterator.hasNext()) 
					{
					
						if (flag)
						{
							logd("rescheduleWeatherUpdates: will re-schedule weather updates");
							WeatherApplication.scheduleWeatherUpdates(l, list, application);
							scheduleInfo = new ScheduleInfo();
							scheduleInfo.scheduledIds = list;
							scheduleInfo.scheduledInterval = l;
							scheduleInfo.scheduledTimestampToken = System.currentTimeMillis();
							weatherPrefs.setScheduleInfo(scheduleInfo);
						} else
						{
							logd("rescheduleWeatherUpdates: will NOT re-schedule weather updates");
						}
				
						iterator = list.iterator();
					}
				
			}
	}

	public List resolveCurrentLocationCityIds(List list)
	{
		int i;
		if (list == null)
			i = 0;
		else
			i = list.size();
		if (i != 0)
		{
			ArrayList arraylist = null;
			for (int j = 0; j < i; j++)
			{
				int k = ((Integer)list.get(j)).intValue();
				if (k == -1024)
				{
					if (arraylist == null)
					{
						arraylist = new ArrayList();
						arraylist.addAll(list.subList(0, j));
					}
					k = getCurrentLocationCityId();
				}
				if (k != 0x80000000 && arraylist != null)
					arraylist.add(Integer.valueOf(k));
			}

			if (arraylist != null)
				list = arraylist;
		}
		return list;
	}

	/**
	 * @deprecated Method start is deprecated
	 */

	public synchronized void start()
	{
		
		log("start");
		mHandlerThread = new HandlerThread("WeatherDataCache");
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper());
		weatherPrefs = new WeatherApplicationPreferences(application);
		weatherPrefs.registerOnSharedPreferenceChangeListener(this);
		synchronized (cityIdsCount)
		{
			int i = cityIdsCount.size();
			for (int j = 0; j < i; j++)
				startObserving(cityIdsCount.keyAt(j));
		}
	}

	void startObserving(int i)
	{
		LinkedList linkedlist = new LinkedList();
		linkedlist.add(new ForecastObserver(mHandler, i));
		linkedlist.add(new RawForecastObserver(mHandler, i));
		linkedlist.add(new DetailedForecastObserver(mHandler, i));
		linkedlist.add(new CurrentObserver(mHandler, i));
		linkedlist.add(new CityNameObserver(mHandler, i));
		linkedlist.add(new UpdatedStatusObserver(mHandler, i));
		mObservers.put(i, linkedlist);
	}

	void startObservingCurrentLocation()
	{
		logd("GROUPER startObservingCurrentLocation");
		if (!isUsingCurrentLocation)
		{
			positioningStatusObserver = new PositioningStatusObserver(mHandler);
			currentLocationClient.registerCurrentLocationListener(this);
			isUsingCurrentLocation = true;
		}
	}

	/**
	 * @deprecated Method stop is deprecated
	 */

	public synchronized void stop()
	{
		
		log("stop");
		mHandler.removeCallbacksAndMessages(null);
		mHandlerThread.getLooper().quit();
		Exception exception;
		try
		{
			mHandlerThread.join(1000L);
		}
		catch (InterruptedException interruptedexception) { }
		mHandlerThread = null;
		weatherPrefs.dispose();
		weatherPrefs = null;
		synchronized (cityIdsCount)
		{
			int i = cityIdsCount.size();
			for (int j = 0; j < i; j++)
				stopObserving(cityIdsCount.keyAt(j));

		}
		return;
	}

	void stopObserving(int i)
	{
		List list = (List)mObservers.get(i);
		if (list != null)
		{
			ContentObserver contentobserver;
			for (Iterator iterator = list.iterator(); iterator.hasNext(); contentResolver.unregisterContentObserver(contentobserver))
			{
				contentobserver = (ContentObserver)iterator.next();
				log("Unregistering Forecast observer...");
			}

			mObservers.remove(i);
		}
	}

	void stopObservingCurrentLocation()
	{
		logd("GROUPER stopObservingCurrentLocation");
		if (isUsingCurrentLocation)
		{
			if (mCurrentLocationCityId != 0x80000000)
				removeCityId(mCurrentLocationCityId);
			contentResolver.unregisterContentObserver(positioningStatusObserver);
			currentLocationClient.unregisterCurrentLocationListener(this);
			positioningStatusObserver = null;
			isUsingCurrentLocation = false;
		}
	}

	public void unregisterWeatherListener(WeatherListener weatherlistener, int i)
	{
		log((new StringBuilder()).append("GROUPER unregisterWeatherListener >>> cityId=").append(i).append(" l=").append(weatherlistener).toString());
		logd("unregisterWeatherListener: obtaining weather_lock...");
		synchronized (weatherListeners)
		{
			logd("unregisterWeatherListener: obtained weather_lock.");
			List list = (List)weatherListeners.get(i);
			if (list != null && list.remove(weatherlistener))
			{
				removeCityId(i);
				if (list.size() == 0)
					weatherListeners.remove(i);
			}
		}
		logd("unregisterWeatherListener: released weather_lock...");
		log((new StringBuilder()).append("GROUPER unregisterWeatherListener <<< cityId=").append(i).append(" l=").append(weatherlistener).toString());
		return;

	}












}
