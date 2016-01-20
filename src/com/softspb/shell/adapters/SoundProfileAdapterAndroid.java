// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import android.content.*;
import android.media.AudioManager;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.softspb.shell.adapters:
//			SoundProfileAdapter, AdaptersHolder

public class SoundProfileAdapterAndroid extends SoundProfileAdapter
{

	private static final int INVALID_MODE = -1;
	private static final Logger logger = Loggers.getLogger(SoundProfileAdapterAndroid.class.getName());
	private Context context;
	private BroadcastReceiver modeChangedReceiver;
	private volatile boolean started;

	public SoundProfileAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		started = false;
		modeChangedReceiver = new BroadcastReceiver() {

			final SoundProfileAdapterAndroid this$0;

			public void onReceive(Context context1, Intent intent)
			{
				SoundProfileAdapterAndroid.logger.d(">>>onReceive");
				notifyChange();
				SoundProfileAdapterAndroid.logger.d("<<<onReceive");
			}

			
			{
				this$0 = SoundProfileAdapterAndroid.this;
//				super();
			}
		};
		logger.d("constructor");
	}

	public static native void update();

	public int getRingMode()
	{
		int i;
		int j;
		logger.d(">>>getRingMode");
		i = ((AudioManager)context.getSystemService("audio")).getRingerMode();
	
		 switch (i)
	    {
		   case 0:
		   	j = 1;
			break;
		   case 1:
		   	j = 2;
				break;
		    case 2:
	     	j = 0;
				break;
		 default:
	      	logger.w((new StringBuilder()).append("androidMode=").append(i).append("  is not supported - check native to java constants mapping").toString());
			j = 0;
	    }
		 return j;
	}

	public int getRingVolume()
	{
		logger.d(">>>getRingVolume");
		AudioManager audiomanager = (AudioManager)context.getSystemService("audio");
		int i = (5 * audiomanager.getStreamVolume(2)) / audiomanager.getStreamMaxVolume(2);
		if (i == 0 && audiomanager.getStreamVolume(2) != 0)
		{
			logger.d("reported level increased to 1 since native level is more than zero");
			i = 1;
		}
		if (i == 5 && audiomanager.getStreamVolume(2) != audiomanager.getStreamMaxVolume(2))
		{
			logger.d("reported level decreased to 4 since native level is less than maximum");
			i = 4;
		}
		logger.d((new StringBuilder()).append("<<<getRingVolume: level=").append(i).toString());
		return i;
	}

	public void notifyChange()
	{
		logger.d((new StringBuilder()).append(">>>notifyChange: started=").append(started).toString());
		if (started)
			update();
		logger.d("<<<notifyChange");
	}

	public void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		logger.d((new StringBuilder()).append(">>>onCreate: context=").append(context1).toString());
		context = context1;
		context1.registerReceiver(modeChangedReceiver, new IntentFilter("android.media.RINGER_MODE_CHANGED"));
		started = true;
		logger.d("<<<onCreate");
	}

	public void onDestroy(Context context1)
	{
		logger.d((new StringBuilder()).append(">>>onDestroy: context=").append(context1).toString());
		context1.unregisterReceiver(modeChangedReceiver);
		started = false;
		logger.d("<<<onDestroy");
	}

	public void openSettings()
	{
		Intent intent = new Intent("android.settings.SOUND_SETTINGS");
		context.startActivity(intent);
	}

  public void setRingMode(int paramInt)
	{
		byte byte0;
		logger.d((new StringBuilder()).append(">>>setRingMode: nativeMode=").append(paramInt).toString());
		switch (paramInt)
	    {
	    
	    case 0:
			byte0 = 2;
			break;
	    case 1:
			byte0 = 0;
			break;
	    case 2:
			byte0 = 1;
			break;
		default:
	     	logger.w((new StringBuilder()).append("nativeMode=").append(paramInt).append(" is not supported - check native to java constants mapping").toString());
			byte0 = -1;
	    }
	logger.d((new StringBuilder()).append("nativeMode=").append(paramInt).append(" resolved to androidMode=").append(byte0).toString());
		if (byte0 != -1)
			((AudioManager)context.getSystemService("audio")).setRingerMode(byte0);
		logger.d("<<<setRingMode");
		return;
		
	}

	public void setRingVolume(int i)
	{
		logger.d((new StringBuilder()).append(">>>setRingVolume: volume=").append(i).toString());
		AudioManager audiomanager = (AudioManager)context.getSystemService("audio");
		audiomanager.setStreamVolume(2, (i * audiomanager.getStreamMaxVolume(2)) / 5, 0);
		logger.d("<<<setRingVolume");
	}


}
