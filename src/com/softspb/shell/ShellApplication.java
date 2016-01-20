package com.softspb.shell;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.service.CurrentLocationClient;
import com.spb.programlist.ProgramList;
import java.util.Iterator;
import java.util.List;

public abstract class ShellApplication extends Application
{
  public static final int DIALOG_CHECK_FAILED = 101;
  private static final long FIRST_DELAY = 900000L;
  private static final long REPEAT_DELAY = 3600000L;
  private static final Logger logger = Loggers.getLogger(ShellApplication.class);
  private volatile CurrentLocationClient currentLocationClient;
  private volatile ProgramList programList;
  private volatile com.softspb.weather.core.WeatherDataCache weatherDataCache;

  public boolean blockTouches()
  {
    return false;
  }

  public void disableHome()
  {
    PackageManager localPackageManager = getPackageManager();
    ComponentName localComponentName = new ComponentName(this, Home.class);
    localPackageManager.setComponentEnabledSetting(localComponentName, 2, 1);
  }

	public void enableLicenseComponents()
	{
		List list = getLicenseComponents();
		if (list == null)
		{
			logger.e("List of license components is null");
		} else
		{
			PackageManager packagemanager = getPackageManager();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) 
				packagemanager.setComponentEnabledSetting(new ComponentName(this, (Class)iterator.next()), 1, 1);
		}
	}
  
  public synchronized CurrentLocationClient getCurrentLocationClient()
  {
	      if (this.currentLocationClient == null)
	      {
	        CurrentLocationClient localCurrentLocationClient = new CurrentLocationClient(this);
	        this.currentLocationClient = localCurrentLocationClient;
	      }

    return this.currentLocationClient;
  }

  public abstract List<Class<?>> getLicenseComponents();

  public ProgramList getProgramList(Context paramContext)
  {

	      if (this.programList == null)
	      {
	        ProgramList localProgramList = new ProgramList(paramContext);
	        this.programList = localProgramList;
	      }
	    
    return this.programList;
  }

  public com.softspb.weather.core.WeatherDataCache getWeatherDataCache()
  {

      if (this.weatherDataCache == null)
      {
        CurrentLocationClient localCurrentLocationClient = getCurrentLocationClient();
        com.softspb.shell.weather.service.WeatherDataCache localWeatherDataCache = new com.softspb.shell.weather.service.WeatherDataCache(this, localCurrentLocationClient);
        this.weatherDataCache = localWeatherDataCache;
      }
    return this.weatherDataCache;
  }

  public void onCreate()
  {
    Log.d("Loggers", "ShellApplication.onCreate: invoking Loggers.setContext");
    Loggers.setContext(this);
    logger.d("onCreate");
    super.onCreate();
    logger.d("onCreate <<<");
  }

  public abstract void onLicenseCheckFailed(Activity paramActivity, Intent paramIntent);

  public void onLowMemory()
  {
    logger.d("onLowMemory >>>");
    super.onLowMemory();
    logger.d("onLowMemory <<<");
  }

  public void onTerminate()
  {
    logger.d("onTerminate >>>");
    Loggers.stop();
    if (this.currentLocationClient != null)
      this.currentLocationClient.dispose();
    if (this.weatherDataCache != null)
      this.weatherDataCache.stop();
    if (this.programList != null)
      this.programList.stop();
    super.onTerminate();
    logger.d("onTerminate <<<");
  }

  public void startLicenseService(Class<?> paramClass)
  {
    Intent localIntent1 = new Intent(this, paramClass);
    ComponentName localComponentName = startService(localIntent1);
    AlarmManager localAlarmManager = (AlarmManager)getSystemService("alarm");
    Intent localIntent2 = new Intent(this, paramClass);
    PendingIntent localPendingIntent = PendingIntent.getService(this, 0, localIntent2, 134217728);
    localAlarmManager.cancel(localPendingIntent);
    long l = SystemClock.elapsedRealtime() + 900000L;
    localAlarmManager.setInexactRepeating(3, l, 3600000L, localPendingIntent);
  }

  public abstract void startLicensingProcess(Activity paramActivity);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.ShellApplication
 * JD-Core Version:    0.6.0
 */