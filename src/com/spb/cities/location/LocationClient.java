// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public final class LocationClient
	implements LocationListener
{
	static final class PowerConsumptionComparator
		implements Comparator
	{

		private static final List CRITERIAS;

		 public int compare(LocationProvider paramLocationProvider1, LocationProvider paramLocationProvider2)
		 {
		      Iterator localIterator = CRITERIAS.iterator();
		      boolean bool1= false;
		      boolean bool2 =false;
		      int i = 0;
		      if (localIterator.hasNext())
		      {
		        Criteria localCriteria = (Criteria)localIterator.next();
		        bool1 = paramLocationProvider1.meetsCriteria(localCriteria);
		        bool2 = paramLocationProvider2.meetsCriteria(localCriteria);
		        if ((bool1) && (!bool2))
		          i = -1;
		      }
		      else
		      {
		        
		        if (!((bool1) || (!bool2)))
		        {
		        i = 1;
		        }
		        else
		        {
		        	String str1 = paramLocationProvider1.getName();
			        String str2 = paramLocationProvider2.getName();
			        i = str1.compareTo(str2);
		        }
		      }
		      return i;
		 }
		public int compare(Object obj, Object obj1)
		{
			return compare((LocationProvider)obj, (LocationProvider)obj1);
		}

		static 
		{
			CRITERIAS = new ArrayList(4);
			Criteria criteria1 = new Criteria();
			criteria1.setPowerRequirement(1);
			CRITERIAS.add(criteria1);
			Criteria criteria2 = new Criteria();
			criteria2.setPowerRequirement(2);
			CRITERIAS.add(criteria2);
			Criteria criteria3 = new Criteria();
			criteria3.setCostAllowed(false);
			CRITERIAS.add(criteria3);
			Criteria criteria4 = new Criteria();
			criteria4.setAccuracy(1);
			CRITERIAS.add(criteria4);
		}

		PowerConsumptionComparator()
		{
		}
	}

	class LocationHandler extends Handler
	{

		private static final int MSG_REQUEST_UPDATES = 1;
		private static final int MSG_TIMEOUT = 2;
		final LocationClient this$0;

		public void handleMessage(Message message)
		{
			switch (message.what) {
			case 1:
				String s = (String)message.obj;
				logd((new StringBuilder()).append("request updates: ").append(s).toString());
				locationManager.requestLocationUpdates(s, 0L, 0.0F, LocationClient.this, getLooper());
				break;
			case 2:
				logd("timeout");
				Object obj = locationMonitor;
				locationMonitor.notifyAll();
				break;
			default:
				break;
			}
			return;
		}

		void postRequestUpdates(String s)
		{
			sendMessage(Message.obtain(this, 1, s));
		}

		void postTimeout(long l)
		{
			sendEmptyMessageDelayed(2, l);
		}

		LocationHandler(Looper looper)
		{
			super(looper);
			this$0 = LocationClient.this;
		}
	}


	private static final long DEFAULT_EXPIRATION_MS = 0x36ee80L;
	private static final long DEFAULT_TIMEOUT_MS = 20000L;
	private static final long DEFAULT_TIMEOUT_MS_GPS = 0x493e0L;
	private static final long DEFAULT_TIMEOUT_MS_NETWORK = 20000L;
	private static final String LOCATION_PROVIDER_PASSIVE = "passive";
	private static final PowerConsumptionComparator POWER_CONSUMPTION_COMPARATOR = new PowerConsumptionComparator();
	private static final String TAG = "LocationClient";
	private static volatile int instanceCount = 0;
	private static final Logger logger = Loggers.getLogger(LocationClient.class);
	private Criteria criteria;
	private Location currentLocation;
	private volatile boolean currentOperationAborted;
	private long expirationMs;
	private HandlerThread handlerThread;
	private String instanceId;
	private int instanceNo;
	private LocationHandler locationHandler;
	private LocationManager locationManager;
	private final Object locationMonitor;
	private long timeoutMsGps;
	private long timeoutMsNetwork;
	private long timeoutMsOthers;

	public LocationClient(Context context)
	{
		this(context, null);
	}

	public LocationClient(Context context, Criteria criteria1)
	{

		int i = 1 + instanceCount;
		instanceCount = i;
		instanceNo = i;

		instanceId = (new StringBuilder()).append(Process.myPid()).append(":").append(instanceNo).toString();
		currentOperationAborted = false;
		timeoutMsNetwork = 20000L;
		timeoutMsGps = 0x493e0L;
		timeoutMsOthers = 20000L;
		expirationMs = 0x36ee80L;
		locationMonitor = new Object();
		logd((new StringBuilder()).append("Ctor >>> context=").append(context.getPackageName()).toString());
		logTrace();
		locationManager = (LocationManager)context.getSystemService("location");
		criteria = criteria1;
		handlerThread = new HandlerThread("LocationClient");
		handlerThread.start();
		locationHandler = new LocationHandler(handlerThread.getLooper());
		Resources resources = context.getResources();
		setGpsTimeout(1000L * (long)resources.getInteger(com.spb.cities.R.integer.location_timeout_sec_gps));
		setNetworkTimeout(1000L * (long)resources.getInteger(com.spb.cities.R.integer.location_timeout_sec_network));
		setOthersTimeout(1000L * (long)resources.getInteger(com.spb.cities.R.integer.location_timeout_sec_others));
		return;
	
	}

	private static final List getEnabledProviders(LocationManager locationmanager, Criteria criteria1, boolean flag)
	{
		Object obj = null;
		List list;
		if (criteria1 == null)
			list = locationmanager.getProviders(true);
		else
			list = locationmanager.getProviders(criteria1, true);
		if (!flag)
		{
			int i;
			int j;
			if (list == null)
				i = 0;
			else
				i = list.size();
			for (j = 0; j < i; j++)
			{
				String s = (String)list.get(j);
				if ("passive".equals(s))
				{
					if (obj != null)
						continue;
					obj = new ArrayList(i - 1);
					for (int k = 0; k < j; k++)
						((List) (obj)).add(list.get(k));

					continue;
				}
				if (obj != null)
					((List) (obj)).add(s);
			}

		}
		if (obj == null)
			obj = list;
		if (obj == null)
			obj = Collections.emptyList();
		return ((List) (obj));
	}

	private Location getLastKnownLocation(List list, long l)
	{
		logd((new StringBuilder()).append("getLastKnownLocation >>> providers: ").append(list).append(" expirationToleranceMs=").append(l).toString());
		Location location = null;
		long l1 = 0x7fffffffffffffffL;
		Iterator iterator;
		if (l != 0L)
			System.currentTimeMillis();
		iterator = list.iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			String s = ((LocationProvider)iterator.next()).getName();
			logd((new StringBuilder()).append("getLastKnownLocation: trying provider: ").append(s).append("...").toString());
			Location location1 = locationManager.getLastKnownLocation(s);
			logd((new StringBuilder()).append("getLastKnownLocation: location=").append(location1).toString());
			if (location1 != null)
			{
				long l2 = System.currentTimeMillis() - location1.getTime();
				if (l2 < l1 && (l == 0L || l2 < l))
				{
					location = location1;
					l1 = l2;
				}
			}
		} while (true);
		logd((new StringBuilder()).append("getLastKnownLocation <<< return ").append(location).toString());
		return location;
	}

	private static List getOrderedProvider(LocationManager locationmanager, Criteria criteria1)
	{
		List list = getEnabledProviders(locationmanager, criteria1, false);
		ArrayList arraylist = new ArrayList(list.size());
		for (Iterator iterator = list.iterator(); iterator.hasNext(); arraylist.add(locationmanager.getProvider((String)iterator.next())));
		Collections.sort(arraylist, POWER_CONSUMPTION_COMPARATOR);
		return arraylist;
	}

	private long getTimeout(LocationProvider locationprovider)
	{
		String s = locationprovider.getName();
		long l;
		if ("gps".equals(s))
			l = timeoutMsGps;
		else
		if ("network".equals(s))
			l = timeoutMsNetwork;
		else
			l = timeoutMsOthers;
		return l;
	}

	private void logTrace()
	{
		StackTraceElement astacktraceelement[] = Thread.currentThread().getStackTrace();
		for (int i = 3; i < astacktraceelement.length; i++)
			logd((new StringBuilder()).append("|   ").append(astacktraceelement[i]).toString());

	}

	private void logd(String s)
	{
		logger.d((new StringBuilder()).append("[").append(instanceId).append("]").append(s).toString());
	}

	private Location queryLocation(LocationProvider locationprovider)
	{
		long l;
		long l1;
		String s = locationprovider.getName();
		logd((new StringBuilder()).append("queryLocation >>> ").append(s).toString());
		l = System.currentTimeMillis();
		l1 = getTimeout(locationprovider);
		currentLocation = null;
		locationHandler.postRequestUpdates(s);
		locationHandler.postTimeout(l1);
_L2:
		while(! (currentOperationAborted || currentLocation != null || System.currentTimeMillis() - l >= l1))
		{
			Object obj = locationMonitor;
			synchronized (obj) {
				try {
					locationMonitor.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				locationManager.removeUpdates(this);
				locationHandler.removeCallbacksAndMessages(null);
			}
		}
		locationManager.removeUpdates(this);
		locationHandler.removeCallbacksAndMessages(null);
		logd((new StringBuilder()).append("queryLocation: return ").append(currentLocation).toString());
		logd((new StringBuilder()).append("queryLocation <<< finished in ").append(System.currentTimeMillis() - l).append(" ms").toString());
		return currentLocation;
	}

	public void abort()
	{
		logd("abort");
		currentOperationAborted = true;
		Object obj = locationMonitor;
		synchronized (obj) {
			locationMonitor.notifyAll();
		}
		
		return;
	}

	public void dispose()
	{
		logd("dispose");
		if (locationManager != null)
			locationManager.removeUpdates(this);
		handlerThread.getLooper().quit();
		locationHandler.removeCallbacksAndMessages(null);
	}

	public boolean isLocationPossible()
	{
		boolean flag = false;
		if (locationManager != null)
		{
			List list = getEnabledProviders(locationManager, criteria, false);
			if (list != null && list.size() > 0)
				flag = true;
			else
				flag = false;
		}
		return flag;
	}

	 public Location obtainLocation()
	  {
		 Location localLocation = null;
	    StringBuilder localStringBuilder1 = new StringBuilder().append("obtainLocation >>> expirationMs=");
	    long l1 = this.expirationMs;
	   
	    logTrace();
	    long l2 = System.currentTimeMillis();
	    this.currentOperationAborted = false;
	    if (this.locationManager == null)
	    {
	      logd("obtainLocation: LocationManager is null, returning null");
	      StringBuilder localStringBuilder2 = new StringBuilder().append("obtainLocation <<< finished in ");
	      long l3 = (System.currentTimeMillis() - l2) / 1000L;
	      String str2 = l3 + " secs";
	      logd(str2);
	      localLocation = null;
	      return localLocation;
	    }
	    LocationManager localLocationManager = this.locationManager;
	    Criteria localCriteria = this.criteria;
	    List localList = getOrderedProvider(localLocationManager, localCriteria);
	    String str3 = "obtainLocation: available providers: " + localList;
	    logd(str3);
	    long l4 = this.expirationMs;
	    localLocation = getLastKnownLocation(localList, l4);
	    int i = 0;
	    int j = 0;
	    if (localLocation == null)
	    {
	      i = localList.size();
	      j = 0;
	    }
	    while (true)
	    {
	      if ((j < i) && (!this.currentOperationAborted))
	      {
	        LocationProvider localLocationProvider = (LocationProvider)localList.get(j);
	        localLocation = queryLocation(localLocationProvider);
	        if (localLocation == null);
	      }
	      else
	      {
	        if (localLocation == null)
	          localLocation = getLastKnownLocation(localList, 0L);
	        String str4 = "obtainLocation: returning location: " + localLocation;
	        logd(str4);
	        StringBuilder localStringBuilder3 = new StringBuilder().append("obtainLocation <<< finished in ");
	        long l5 = (System.currentTimeMillis() - l2) / 1000L;
	        String str5 = l5 + " secs";
	        logd(str5);
	        break;
	      }
	      j += 1;
	    }
	    return localLocation;
	  }
	
	public void onLocationChanged(Location location)
	{
		logd((new StringBuilder()).append("onLocationChanged: ").append(location).toString());
		Object obj = locationMonitor;
		synchronized (obj) {
			currentLocation = location;
			locationMonitor.notify();
		}
	
		return;
	}

	public void onProviderDisabled(String s)
	{
		logd((new StringBuilder()).append("onProviderDisabled: ").append(s).toString());
	}

	public void onProviderEnabled(String s)
	{
		logd((new StringBuilder()).append("onProviderEnabled: ").append(s).toString());
	}

	public void onStatusChanged(String s, int i, Bundle bundle)
	{
		logd((new StringBuilder()).append("onStatusCahnged: provider=").append(s).append(" status=").append(i).toString());
	}

	public void setExpirationMs(long l)
	{
		expirationMs = l;
	}

	public void setGpsTimeout(long l)
	{
		timeoutMsGps = l;
	}

	public void setNetworkTimeout(long l)
	{
		timeoutMsNetwork = l;
	}

	public void setOthersTimeout(long l)
	{
		timeoutMsOthers = l;
	}




}
