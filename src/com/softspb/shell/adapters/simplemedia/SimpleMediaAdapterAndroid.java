// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.simplemedia;

import java.util.Iterator;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.softspb.shell.adapters.AdaptersHolder;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.softspb.shell.adapters.simplemedia:
//			SimpleMediaAdapter, SimpleMediaCommander, SimpleMediaFactory, SimpleMediaReceivers

public class SimpleMediaAdapterAndroid extends SimpleMediaAdapter
{

	private static final int MAX_SERVICES = 50;
	public static final int MESSAGE_PLAYING = 0;
	public static final int MESSAGE_STOP = 1;
	public static final int PLAYSTATE_PAUSED = 1;
	public static final int PLAYSTATE_PLAYING = 2;
	public static final int PLAYSTATE_STOPPED = 0;
	private static final String TAG = "SimpleMedia";
	private static Logger logger = Loggers.getLogger(SimpleMediaAdapterAndroid.class);
	private SimpleMediaCommander commander;
	private Context context;
	private String packageName;
	private Handler playstateHandler;
	private DecoratedBroadcastReceiver receiver;
	private int sToken;
	private SimpleMediaFactory.PlaybackService service;
	private int vendor;

	public SimpleMediaAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		playstateHandler = new Handler() {

			final SimpleMediaAdapterAndroid this$0;

			public void handleMessage(Message message)
			{
				switch (message.what) {
				case 0:
					onPlaybackUpdated(service.getTrackDuration(), service.getTrackPosition());
					playstateHandler.removeMessages(0);
					if (service.isPlaying())
						playstateHandler.sendEmptyMessageDelayed(0, 1000L);
					break;
				case 1:
					playstateHandler.removeMessages(0);
					break;
				default:
					break;
				}
				return;

			}
			{
				this$0 = SimpleMediaAdapterAndroid.this;
//				super();
			}
		};
		packageName = "";
		sToken = 0;
	}

	private boolean isServiceAlive()
	{
		boolean flag = false ;
		if (service == null) 
		{
			for (Iterator iterator = ((ActivityManager)context.getSystemService("activity")).getRunningServices(50).iterator(); iterator.hasNext();)
			{
				if (((android.app.ActivityManager.RunningServiceInfo)iterator.next()).service.getPackageName().equals(packageName))			
				{
					flag = true;
					break;
				}
				else
					flag = false;
			}
			
		}
		else 
		{
			 flag = service.isServiceAlive();
		}
		return flag;
	}

	private native void onMediaInfoUpdated(int i, String s, String s1);

	private native void onPlayStateUpdated(int i, int j);

	private native void onPlaybackUpdated(int i, int j, int k);

	public boolean doNext()
	{
		commander.doNext();
		return isServiceAlive();
	}

	public boolean doPrev()
	{
		commander.doPrevious();
		return isServiceAlive();
	}

	public boolean doTogglePause()
	{
		commander.doTogglePause();
		return isServiceAlive();
	}

	public boolean isSupportingPlayback()
	{
		boolean flag;
		if (service != null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		context = context1;
		vendor = SimpleMediaFactory.getCurrentVendor(context1);
		logger.i((new StringBuilder()).append("SimpleMedia vendor ").append(vendor).toString());
		if (vendor != -1)
		{
			String s = SimpleMediaFactory.getIntentPrefix(vendor);
			packageName = SimpleMediaFactory.getPackage(vendor);
			commander = new SimpleMediaCommander(s, context1);
			service = SimpleMediaFactory.getServiceWrapper(context1, vendor);
			if (service != null)
			{
				service.setOnConnectionListener(new SimpleMediaFactory.PlaybackService.OnConnectionListener() {

					final SimpleMediaAdapterAndroid this$0;

					public void onConnect()
					{
						if (service.isPlaying())
							onPlayStateUpdated(2);
						else
							onPlayStateUpdated(1);
						onMediaInfoUpdated(service.getArtistName(), service.getTrackName());
						updatePlayState();
					}

			
			{
				this$0 = SimpleMediaAdapterAndroid.this;
//				super();
			}
				});
				receiver = SimpleMediaReceivers.getMediaReceiverWithService(this, s);
			} else
			{
				receiver = SimpleMediaFactory.getSpecificBroadCastReceiver(vendor, this);
			}
		}
	}

	public void onMediaInfoUpdated(String s, String s1)
	{
		onMediaInfoUpdated(sToken, s, s1);
	}

	public void onPlayStateUpdated(int i)
	{
		onPlayStateUpdated(sToken, i);
	}

	public void onPlaybackCompleted()
	{
		playstateHandler.removeMessages(0);
		onPlaybackUpdated(0, 0);
		onPlayStateUpdated(0);
	}

	public void onPlaybackUpdated(int i, int j)
	{
		onPlaybackUpdated(sToken, i, j);
	}

	protected void onStart(int i)
	{
		sToken = i;
		context.registerReceiver(receiver, receiver.getIntentFilter());
		if (service == null)
			commander.checkPlayStatus();
		else
			service.start();
	}

	public void onStop()
	{
		context.unregisterReceiver(receiver);
		if (service != null)
			service.stop();
	}

	public void updatePlayState()
	{
		byte byte0 = 1;
		service.reconnectIfNeeded();
		boolean flag = service.isPlaying();
		logger.i((new StringBuilder()).append("MediaPlayerAdapterAndroid.updatePlayStateisPlaying = ").append(flag).toString());
		if (flag)
		{
			onPlaybackUpdated(service.getTrackDuration(), service.getTrackPosition());
			playstateHandler.sendEmptyMessage(0);
		} else
		{
			onPlaybackUpdated(service.getTrackDuration(), service.getTrackPosition());
			playstateHandler.sendEmptyMessage(byte0);
		}
		if (flag)
			byte0 = 2;
		onPlayStateUpdated(sToken, byte0);
	}



}
