package com.softspb.shell.adapters.backlight;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;

import com.softspb.shell.adapters.AdaptersHolder;

public class BacklightAdapterAndroidApi8 extends BacklightAdapterAndroidBase
  implements Runnable
{
  public static int MODE_AUTOMATIC = 0;
  public static int MODE_MANUAL = 0;
  private static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
  private static final Uri modeObserverUri = Settings.System.getUriFor("screen_brightness_mode");

  static
  {
    MODE_MANUAL = 0;
    MODE_AUTOMATIC = 1;
  }

  public BacklightAdapterAndroidApi8(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  private int getMode()
  {
    ContentResolver localContentResolver = this.context.getContentResolver();
    int i = MODE_MANUAL;
    return Settings.System.getInt(localContentResolver, "screen_brightness_mode", i);
  }

  private void setMode(int paramInt)
  {
    boolean bool = Settings.System.putInt(this.context.getContentResolver(), "screen_brightness_mode", paramInt);
  }

  public int getLevel()
  {
    int i = getMode();
    int j = MODE_AUTOMATIC;
    int k;
    if (i == j)
      k = -1;
    else
    {
     
      ContentResolver localContentResolver = this.context.getContentResolver();
      int m = DEFAULT_BACKLIGHT;
      k = Settings.System.getInt(localContentResolver, "screen_brightness", m);
      int n = MINIMUM_BACKLIGHT;
      if (k <= n)
    	  k = 0;
    }
    return k;
  }

  public boolean isAutolevelSupported()
  {
	  boolean flag = false;
    try
    {
       Settings.System.getInt(this.context.getContentResolver(), "screen_brightness_mode");
      flag = true;
      return flag;
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException)
    {
    	flag = false;
    	return flag;
    }    
  }

  protected void onDestroy(Context paramContext)
  {
    super.onDestroy(paramContext);
    ContentResolver localContentResolver = paramContext.getContentResolver();
    ContentObserver localContentObserver = this.observer;
    localContentResolver.unregisterContentObserver(localContentObserver);
  }

	protected void onStart()
	{
//		onStart();
		context.getContentResolver().registerContentObserver(modeObserverUri, false, observer);
	}

  public void setAutoLevel()
  {
    int i = MODE_AUTOMATIC;
    setMode(i);
    super.setAutoLevel();
  }

  public void setManualLevel(int paramInt)
  {
    int i = MODE_MANUAL;
    setMode(i);
    super.setManualLevel(paramInt);
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.backlight.BacklightAdapterAndroidApi8
 * JD-Core Version:    0.6.0
 */