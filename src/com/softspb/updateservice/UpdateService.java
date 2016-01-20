// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.updateservice;

import android.app.Service;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.*;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.Arrays;

// Referenced classes of package com.softspb.updateservice:
//			DownloadClient

public abstract class UpdateService extends Service
{

	public static final String ACTION_SET_PREFERENCES = "com.softspb.weather.updateservice.action.SetPreferences";
	public static final String ACTION_UPDATE_PREFIX = "com.softspb.weather.updateservice.action.Update";
	public static final String PARAM_FORCE_UPDATE = "force_update";
	public static final String PREFERENCE_USE_ONLY_WIFI = "use-only-wifi";
	public static final String UPDATE_IDS_INT_ARRAY = "update_ids";
	protected final String TAG = getClass().getSimpleName();
	private DownloadClient downloadClient;
	protected final Logger logger = Loggers.getLogger(getClass().getName());
	private android.os.PowerManager.WakeLock wakeLock;

	public UpdateService()
	{
	}

	 public static boolean checkNetworkAvailabilityAndSettings(Context paramContext, boolean paramBoolean1, boolean paramBoolean2, Logger paramLogger)
	  {
	    boolean i = false;
	    ConnectivityManager localConnectivityManager = null;
	    if (!paramBoolean1)
	    {
	      localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
	      if (!localConnectivityManager.getBackgroundDataSetting())
	        paramLogger.d("Background data setting is off, not updating");
	    }
	    else
	    {
	      
	      NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
	      if ((localNetworkInfo == null) || (!localNetworkInfo.isAvailable()))
	      {
	        paramLogger.d("Network is not available, not updating");
	      
	      }
	      if (paramBoolean2)
	      {
	        if (localNetworkInfo == null);
	        for (int j = -2147483648; ; j = localNetworkInfo.getType())
	        {
	          String str = "onStartCommand: netType=" + j;
	          paramLogger.d(str);
	          if (j == 1)
	          break;
	        }
	      }
	      i = true;
	    }
	    return i;
	  }

	private void handleSetPreferences(Intent intent)
	{
		logger.d("handleSetPreference >>>");
		SharedPreferences sharedpreferences = getSharedPreferences();
		if (intent.hasExtra("use-only-wifi"))
		{
			boolean flag = intent.getBooleanExtra("use-only-wifi", false);
			logger.d((new StringBuilder()).append("handleSetPreference: use-only-wifi=").append(flag).toString());
			sharedpreferences.edit().putBoolean("use-only-wifi", flag).commit();
		} else
		{
			logger.d("handleSetPreference: use-only-wifi not set");
		}
		logger.d("handleSetPreference <<<");
	}

	public static void setUseOnlyWifiPreference(Context context, boolean flag)
	{
		Intent intent = new Intent("com.softspb.weather.updateservice.action.SetPreferences");
		intent.putExtra("use-only-wifi", flag);
		intent.setPackage(context.getPackageName());
		context.startService(intent);
	}

	protected abstract DownloadClient createDownloadClient(Context context);

 void doUpdate(int[] paramArrayOfInt)
  {
	 int k = 0;
	 boolean m = false;
	 int j = 0;
    try
    {
      onStartedUpdate();
      int[] arrayOfInt = paramArrayOfInt;
      int i = arrayOfInt.length;

      if (j < i)
      {
        k = arrayOfInt[j];
        m = false;
        onStartedUpdateId(k);
      }
    }
    finally
    {
      try
      {
       
        Logger localLogger1 = this.logger;
        String str1 = "Attempting to update data for city_id=" + k;
        localLogger1.i(str1);
        DownloadClient localDownloadClient = this.downloadClient;
        Integer localInteger = Integer.valueOf(k);
        Object localObject1 = localDownloadClient.download(localInteger);
        Logger localLogger2 = this.logger;
        String str2 = "Received data: " + localObject1;
        localLogger2.d(str2);
        if (localObject1 != null)
        {
          boolean n = onDataReceived(localObject1);
          m = n;
        }
        onFinishedUpdateId(k, m);
        j += 1;
      }
      finally
      {
 
        onFinishedUpdateId(k, m);
      }
      this.logger.d("Finished update.");
      onFinishedUpdate();
    }
    this.logger.d("Finished update.");
    onFinishedUpdate();
  }


	protected abstract SharedPreferences getSharedPreferences();

	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public void onCreate()
	{
		logger.d("onCreate");
		super.onCreate();
		downloadClient = createDownloadClient(this);
		wakeLock = ((PowerManager)getSystemService("power")).newWakeLock(1, TAG);
	}

	protected abstract boolean onDataReceived(Object obj);

	public void onDestroy()
	{
		logger.d("onDestroy");
		logger.d("Aborting download client...");
		if (downloadClient != null)
		{
			downloadClient.abort();
			logger.d("Download client aborted");
		}
	}

	protected void onFinishedUpdate()
	{
	}

	protected void onFinishedUpdateId(int i, boolean flag)
	{
	}

	public int onStartCommand(Intent intent, int i, int j)
	{
		logger.d((new StringBuilder()).append("onStartCommand: intent=").append(intent.getAction()).toString());
		super.onStart(intent, j);
		String s = intent.getAction();
		if (s == null)
			stopSelf();
		else
		if ("com.softspb.weather.updateservice.action.SetPreferences".equals(s))
		{
			handleSetPreferences(intent);
			stopSelf();
		} else
		{
			final int updateIds[] = intent.getExtras().getIntArray("update_ids");
			boolean flag = intent.getBooleanExtra("force_update", false);
			boolean flag1 = getSharedPreferences().getBoolean("use-only-wifi", false);
			logger.d((new StringBuilder()).append("onStartCommand: useOnlyWifi=").append(flag1).append(" forceUpdate=").append(flag).toString());
			Logger logger1 = logger;
			StringBuilder stringbuilder = (new StringBuilder()).append("onStartCommand: action=").append(s).append(", updateIds=");
			String s1;
			if (updateIds == null)
				s1 = "null";
			else
				s1 = Arrays.toString(updateIds);
			logger1.d(stringbuilder.append(s1).toString());
			if (!checkNetworkAvailabilityAndSettings(this, flag, flag1, logger))
			{
				logger.d("Update is not allowed, stop.");
				stopSelf();
			} else
			if (updateIds == null || updateIds.length == 0)
			{
				logger.w("update IDs not specified, doing nothing.");
				stopSelf();
			} else
			{
				Thread thread = new Thread() {

					final UpdateService this$0;
					final int val$updateIds[];

					public void run()
					{
						doUpdate(updateIds);
						releaseWakeLock();
						stopSelf();
						
					}

			
			{
				this$0 = UpdateService.this;
				val$updateIds = updateIds ;
//				super();
			}
				};
				wakeLock.acquire();
				thread.start();
			}
		}
		return 2;
	}

	protected void onStartedUpdate()
	{
	}

	protected void onStartedUpdateId(int i)
	{
	}

	void releaseWakeLock()
	{
		if (wakeLock != null && wakeLock.isHeld())
			wakeLock.release();
	}
}
