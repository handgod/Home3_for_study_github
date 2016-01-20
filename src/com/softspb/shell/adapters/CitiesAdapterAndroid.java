package com.softspb.shell.adapters;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import com.softspb.shell.Home;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.provider.CitiesContract;
import com.spb.cities.service.CurrentLocationClient;
import com.spb.cities.service.CurrentLocationConnectivityReceiver;
import com.spb.cities.service.CurrentLocationService;

public class CitiesAdapterAndroid extends CitiesAdapter
  implements CurrentLocationClient.CurrentLocationListener
{
  private static final String[] CITY_PROJECTION ;
  private static final int INDEX_CITY_NAME = 0;
  private static final int INDEX_LATITUDE = 1;
  private static final int INDEX_LONGITUDE = 2;
  private static final int INDEX_UTC_OFFSET_MINUTES = 3;
  private static final IntentFilter connectivityFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
  private static final Logger logger;
  private int adapterToken;
  private Context context;
  private long ctaRequestCounter;
  private CurrentLocationClient currentLocationClient;
  private CurrentLocationConnectivityReceiver currentLocationConnectivityReceiver;
  int currentLocationCounter = 0;

  static
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "city_name";
    arrayOfString[1] = "latitude";
    arrayOfString[2] = "longitude";
    arrayOfString[3] = "utc_offset_min";
    CITY_PROJECTION = arrayOfString;
    logger = Loggers.getLogger(CitiesAdapterAndroid.class.getName());
  }

  public CitiesAdapterAndroid(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
    Logger localLogger = logger;
    String str = "Ctor: watcher=" + paramAdaptersHolder;
    localLogger.d(str);
    long l = System.currentTimeMillis();
    this.ctaRequestCounter = l;
  }

  private native void onCityInfoLoaded(int paramInt1, int paramInt2, String paramString, int paramInt3, double paramDouble1, double paramDouble2);

  private native void onCitySelected(int paramInt1, long paramLong, int paramInt2);

  private native void onCurrentLocationChanged(int paramInt1, int paramInt2);

  public void homeOnDestroy()
  {
    Context localContext = this.context;
    CurrentLocationConnectivityReceiver localCurrentLocationConnectivityReceiver = this.currentLocationConnectivityReceiver;
    localContext.unregisterReceiver(localCurrentLocationConnectivityReceiver);
    this.currentLocationConnectivityReceiver = null;
  }

  public void homeOnStart()
  {
    if (this.currentLocationConnectivityReceiver == null)
    {
      CurrentLocationConnectivityReceiver localCurrentLocationConnectivityReceiver1 = new CurrentLocationConnectivityReceiver();
      this.currentLocationConnectivityReceiver = localCurrentLocationConnectivityReceiver1;
      Context localContext = this.context;
      CurrentLocationConnectivityReceiver localCurrentLocationConnectivityReceiver2 = this.currentLocationConnectivityReceiver;
      IntentFilter localIntentFilter = connectivityFilter;
      Intent localIntent = localContext.registerReceiver(localCurrentLocationConnectivityReceiver2, localIntentFilter);
    }
  }
  public void loadCityInfo(int paramInt)
  {
    Logger localLogger1 = logger;
    String str1 = "loadCityInfo: cityId=" + paramInt;
    localLogger1.d(str1);
    int i = paramInt;
    if (paramInt == 0)
      paramInt = this.currentLocationClient.getCurrentLocationCityId();
    Cursor localCursor = null;
    if (paramInt == -2147483648)
    {
    	return;
    }
    else
    {
      Uri localUri1 = CitiesContract.Cities.getContentUri(this.context);
      long l = paramInt;
      Uri localUri2 = ContentUris.withAppendedId(localUri1, l);
      try
      {
        ContentResolver localContentResolver = this.context.getContentResolver();
        String[] arrayOfString = CITY_PROJECTION;
        localCursor = localContentResolver.query(localUri2, arrayOfString, null, null, null);
        if ((localCursor != null) && (localCursor.moveToFirst()))
        {
          String str2 = localCursor.getString(0);
          double d1 = localCursor.getDouble(1);
          double d2 = localCursor.getDouble(2);
          int j = localCursor.getInt(3);
          Logger localLogger2 = logger;
          String str3 = "loadCityInfo: requestedCityId=" + i + " cityName=" + str2 + " zoneOffsetMin=" + j + " lat=" + d1 + " lon=" + d2;
          localLogger2.d(str3);
          int k = this.adapterToken;
          CitiesAdapterAndroid localCitiesAdapterAndroid = this;
          int m = i;
          localCitiesAdapterAndroid.onCityInfoLoaded(k, m, str2, j, d1, d2);
        }
        else if (localCursor != null)
        {
          logger.w((new StringBuilder()).append("No data available from provider for cityId=").append(i).toString());
          try
          {
            localCursor.close();
            return;
          }
          catch (Exception localException1)
          {
          }
       
        }
      }
      catch (Exception localException2)
      {
        Logger localLogger4 = logger;
        String str5 = "Failed city query; " + localException2;
        localLogger4.e(str5, localException2);
        if (localCursor == null)
        {
        	return;
        }
        else
        {
	        try
	        {
	          localCursor.close();
	        }
	        catch (Exception localException3)
	        {
	        }
        }
      }
      finally
      {
        if (localCursor == null);
      }
    }
    try
    {
      localCursor.close();
    }
    catch (Exception localException4)
    {
    }
  }

  public void onCitySelectResult(int paramInt, Intent paramIntent)
  {
    if (paramInt == -1)
    {
      int i = paramIntent.getIntExtra("city_id", -2147483648);
      long l = paramIntent.getLongExtra("cta_request_id", 65535L);
      Logger localLogger = logger;
      String str = "onCitySelectResult: cityId=" + i + " ctaRequestId=" + l;
      localLogger.d(str);
      if ((i != -2147483648) && (l != 65535L) && (this.context != null))
      {
        if (i == 64512)
          i = 0;
        int j = this.adapterToken;
        onCitySelected(j, l, i);
      }
    }
  }

  protected void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    Logger localLogger = logger;
    StringBuilder localStringBuilder = new StringBuilder().append("onCreate: context=");
    String str1 = paramContext.getPackageName();
    String str2 = str1 + " nc=" + paramNativeCallbacks;
    localLogger.d(str2);
    super.onCreate(paramContext, paramNativeCallbacks);
    this.context = paramContext;
    CurrentLocationClient localCurrentLocationClient = CurrentLocationClient.getInstance(paramContext);
    this.currentLocationClient = localCurrentLocationClient;
  }

  public void onCurrenLocationCityIdUpdated(int paramInt)
  {
    Logger localLogger = logger;
    String str = "onCurrentLocationCityIdUpdated: cityId=" + paramInt;
    localLogger.d(str);
    int i = this.adapterToken;
    onCurrentLocationChanged(i, paramInt);
  }

  protected void onDestroy(Context paramContext)
  {
    logger.d("onDestroy");
  }

  protected void onStart(int paramInt)
  {
    Logger localLogger = logger;
    String str = "onStart: adapterToken=" + paramInt;
    localLogger.d(str);
    super.onStart(paramInt);
    this.adapterToken = paramInt;
  }

  protected void onStop()
  {
    logger.d("onStop");
    super.onStop();
    this.adapterToken = 0;
  }

  public long openCitySelect()
  {
    long l = this.ctaRequestCounter + 1L;
    this.ctaRequestCounter = l;
    Logger localLogger = logger;
    String str = "openCitySelect: ctaRequestId=" + l;
    localLogger.d(str);
    ((Home)this.context).startSelectCityForCitiesAdapter(l);
    return l;
  }

  public void startListeningCurrentLocation()
  {
    logger.d("GROUPER startListeningCurrentLocation >>>");
    this.currentLocationClient.registerCurrentLocationListener(this);
    logger.d("GROUPER startListeningCurrentLocation <<<");
  }

  public void stopListeningCurrentLocation()
  {
    logger.d("GROUPER stopListeningCurrentLocation >>>");
    this.currentLocationClient.unregisterCurrentLocationListener(this);
    logger.d("GROUPER stopListeningCurrentLocation <<<");
  }

  public void updateCurrentLocation()
  {
    logger.d("updateCurrentLocation");
    CurrentLocationService.updateNow(this.context, true);
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.CitiesAdapterAndroid
 * JD-Core Version:    0.6.0
 */