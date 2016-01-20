package com.softspb.shell.adapters.backlight;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

import com.softspb.shell.Home;
import com.softspb.shell.adapters.AdaptersHolder;
import com.softspb.shell.opengl.NativeCallbacks;

public abstract class BacklightAdapterAndroidBase extends BacklightAdapter
  implements Runnable, Home.PauseResumeListener
{
  protected static final int DEFAULT_BACKLIGHT;
  protected static final int MAXIMUM_BACKLIGHT;
  protected static final int MINIMUM_BACKLIGHT = getStaticIntField("android.os.Power", "BRIGHTNESS_DIM", 0) + 10;
  protected static final Uri levelUri;
  private Activity activity;
  protected Context context;
  private Handler handler;
  protected ContentObserver observer;

  static
  {
    MAXIMUM_BACKLIGHT = getStaticIntField("android.os.Power", "BRIGHTNESS_ON", 255);
    DEFAULT_BACKLIGHT = (int)(MAXIMUM_BACKLIGHT * 0.4F);
    levelUri = Settings.System.getUriFor("screen_brightness");
  }
	public BacklightAdapterAndroidBase(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		observer = new ContentObserver(new Handler(Looper.getMainLooper())) {

			final BacklightAdapterAndroidBase this$0;

			public void onChange(boolean flag)
			{
				BacklightAdapterAndroidBase.setLevelNative(getLevel());
				handler.postDelayed(BacklightAdapterAndroidBase.this, 100L);
			}

			
			{
				this$0 = BacklightAdapterAndroidBase.this;
//				ContentObserver(handler1);
			}
		};
	}
  private static int getStaticIntField(String paramString1, String paramString2, int paramInt)
  {
    try
    {
      Class localClass = Class.forName(paramString1);
      int i = localClass.getField(paramString2).getInt(localClass);
      paramInt = i;
      return paramInt;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public static native void setLevelNative(int paramInt);

  private void updateWindowBrightness()
  {
    Window localWindow = this.activity.getWindow();
    WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
    int i = getLevel();
    int j = MINIMUM_BACKLIGHT;
    if ((i < j) && (i != -1))
      i = MINIMUM_BACKLIGHT;
    float f1 = i;
    float f2 = 1.0F * f1;
    float f3 = MAXIMUM_BACKLIGHT;
    float f4 = f2 / f3;
    localLayoutParams.screenBrightness = f4;
    localWindow.setAttributes(localLayoutParams);
  }

  public int getMaxLevelsCount()
  {
    return MAXIMUM_BACKLIGHT;
  }

  public boolean isSupported()
  {
    return true;
  }

  public void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    this.context = paramContext;
    if ((paramContext instanceof Activity))
    {
      Activity localActivity = (Activity)paramContext;
      this.activity = localActivity;
      ContentResolver localContentResolver = paramContext.getContentResolver();
      Uri localUri = levelUri;
      ContentObserver localContentObserver = this.observer;
      localContentResolver.registerContentObserver(localUri, false, localContentObserver);
      return;
    }
    throw new IllegalArgumentException("activity must be provided!");
  }

  protected void onDestroy(Context paramContext)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    ContentObserver localContentObserver = this.observer;
    localContentResolver.unregisterContentObserver(localContentObserver);
  }

  public void onPause()
  {
  }

  public void onResume()
  {
    setLevelNative(getLevel());
  }

  protected void onStart()
  {
    super.onStart();
    ((Home)this.context).setOnPauseResumeListener(this);
  }

  public void onStartInUIThread()
  {
    Handler localHandler = new Handler();
    this.handler = localHandler;
  }

  public void run()
  {
    updateWindowBrightness();
  }

  public void setAutoLevel()
  {
    boolean bool = this.handler.postDelayed(this, 100L);
  }

	public void setLevel(int i)
	{
		if (i == -1)
			setAutoLevel();
		else
			setManualLevel(i);
	}


  	public void setManualLevel(int i)
	{
		if (i < MINIMUM_BACKLIGHT)
			i = MINIMUM_BACKLIGHT;
		android.provider.Settings.System.putInt(context.getContentResolver(), "screen_brightness", i);
		handler.postDelayed(this, 100L);
	}
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.backlight.BacklightAdapterAndroidBase
 * JD-Core Version:    0.6.0
 */