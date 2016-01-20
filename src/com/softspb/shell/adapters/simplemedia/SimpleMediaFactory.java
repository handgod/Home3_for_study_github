// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.simplemedia;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.music.IMediaPlaybackService;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;

// Referenced classes of package com.softspb.shell.adapters.simplemedia:
//			SimpleSecReceiverAction, SimpleMediaReceivers, SimpleMediaAdapterAndroid

class SimpleMediaFactory
{
	public static class PlaybackService
		implements ServiceConnection, SimplePlaybackServiceWrapper
	{
		public static interface OnConnectionListener
		{

			public abstract void onConnect();
		}


		private String activityName;
		private Context context;
		private boolean isConnected;
		private OnConnectionListener listener;
		private String packageName;
		private InitableServiceWrapper serviceInstance;

		private void connectToService()
		{
			Intent intent = makeServiceIntent();
			context.startService(intent);
			context.bindService(intent, this, 0);
		}

		private Intent makeServiceIntent()
		{
			Intent intent = new Intent();
			intent.setClassName(packageName, activityName);
			intent.putExtra("command", "appwidgetupdate");
			return intent;
		}

		public String getArtistName()
		{
			String s1 = null;
			try {
				s1 = serviceInstance.getArtistName();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String s = s1;

			return s;
	
		}

		public int getTrackDuration()
		{
			int i = 0;
			if (isConnected)
			{
				int j = 0;
				try {
					j = serviceInstance.getTrackDuration();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i = j;				
			}
			return i;

		}

		public String getTrackName()
		{
			String s1 = null;
			try {
				s1 = serviceInstance.getTrackName();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String s = s1;			
			return s;
	
		}

		public int getTrackPosition()
		{
			int i = 0;
			if (isConnected)
			{
				int j = 0;
				try {
					j = serviceInstance.getTrackPosition();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i = j;
			}
		
			return i;

		}

		public boolean isPlaying()
		{
			boolean flag = false;
			if (isConnected) 
			{
				boolean flag1 = false;
				try {
					flag1 = serviceInstance.isPlaying();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flag = flag1;
			}
			return flag;

		}

		public boolean isServiceAlive()
		{
			boolean flag1 = false;
			try {
				flag1 = serviceInstance.isServiceAlive();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag1;
		}

		public boolean isServiceConneted()
		{
			return false;
		}

		public void onServiceConnected(ComponentName componentname, IBinder ibinder)
		{
			isConnected = true;
			serviceInstance.init(ibinder);
			if (listener != null)
				listener.onConnect();
		}

		public void onServiceDisconnected(ComponentName componentname)
		{
			Log.e("Disconnected", "service");
			isConnected = false;
		}

		public void reconnectIfNeeded()
		{
			if (!isConnected)
				connectToService();
		}

		public void setOnConnectionListener(OnConnectionListener onconnectionlistener)
		{
			listener = onconnectionlistener;
		}

		public void start()
		{
			connectToService();
		}

		public void stop()
		{
			context.unbindService(this);
		}

		public PlaybackService(InitableServiceWrapper initableservicewrapper, String s, Context context1)
		{
			this(initableservicewrapper, s, (new StringBuilder()).append(s).append(".MediaPlaybackService").toString(), context1);
		}

		public PlaybackService(InitableServiceWrapper initableservicewrapper, String s, String s1, Context context1)
		{
			isConnected = false;
			packageName = s;
			activityName = s1;
			context = context1;
			serviceInstance = initableservicewrapper;
		}
	}

	private static class AndroidWrapper
		implements InitableServiceWrapper
	{

		IMediaPlaybackService service;

		public String getArtistName()
			throws RemoteException
		{
			return service.getArtistName();
		}

		public int getTrackDuration()
			throws RemoteException
		{
			return (int)service.duration();
		}

		public String getTrackName()
			throws RemoteException
		{
			return service.getTrackName();
		}

		public int getTrackPosition()
			throws RemoteException
		{
			return (int)service.position();
		}

		public void init(IBinder ibinder)
		{
			service = com.android.music.IMediaPlaybackService.Stub.asInterface(ibinder);
		}

		public boolean isPlaying()
			throws RemoteException
		{
			return service.isPlaying();
		}

		public boolean isServiceAlive()
			throws RemoteException
		{
			return service.asBinder().pingBinder();
		}

		private AndroidWrapper()
		{
		}

	}

	private static class HtcWrapper
		implements InitableServiceWrapper
	{

		com.htc.music.IMediaPlaybackService service;

		public String getArtistName()
			throws RemoteException
		{
			return service.getArtistName();
		}

		public int getTrackDuration()
			throws RemoteException
		{
			return (int)service.duration();
		}

		public String getTrackName()
			throws RemoteException
		{
			return service.getTrackName();
		}

		public int getTrackPosition()
			throws RemoteException
		{
			return (int)service.position();
		}

		public void init(IBinder ibinder)
		{
			service = com.htc.music.IMediaPlaybackService.Stub.asInterface(ibinder);
		}

		public boolean isPlaying()
			throws RemoteException
		{
			return service.isPlaying();
		}

		public boolean isServiceAlive()
			throws RemoteException
		{
			return service.asBinder().pingBinder();
		}

		private HtcWrapper()
		{
		}

	}

	private static interface InitableServiceWrapper
		extends SimplePlaybackServiceWrapper, Initable
	{
	}

	private static interface Initable
	{

		public abstract void init(IBinder ibinder);
	}

	public static interface SimplePlaybackServiceWrapper
	{

		public abstract String getArtistName()
			throws RemoteException;

		public abstract int getTrackDuration()
			throws RemoteException;

		public abstract String getTrackName()
			throws RemoteException;

		public abstract int getTrackPosition()
			throws RemoteException;

		public abstract boolean isPlaying()
			throws RemoteException;

		public abstract boolean isServiceAlive()
			throws RemoteException;
	}


	private static final String ANDROID_PACKAGE = "com.android.music";
	private static final String ANDROID_PACKAGE_UP2_2_1 = "com.google.android.music";
	private static final String CMDAPPWIDGETUPDATE = "appwidgetupdate";
	private static final String HTC_PACKAGE = "com.htc.music";
	private static final String SAMSUNG_BRAND = "samsung";
	private static final String SAMSUNG_GENERAL_PACKAGE = "com.sec";
	private static final String SAMSUNG_MUSIC_PACKAGE = "com.sec.android.app.music";
	private static final int VENDOR_GOOGLE = 1;
	private static final int VENDOR_GOOGLE_2_3 = 6;
	private static final int VENDOR_GOOGLE_UP2_2_1 = 5;
	private static final int VENDOR_HTC = 2;
	public static final int VENDOR_NONE = -1;
	private static final int VENDOR_SAMSUNG_ECLAIR_MR1 = 3;
	private static final int VENDOR_SAMSUNG_FROYO = 4;

	private SimpleMediaFactory()
	{
	}

	public static PlaybackService getAndroidServiceWrapper(Context context)
	{
		return new PlaybackService(new AndroidWrapper(), "com.android.music", context);
	}

	public static PlaybackService getAndroidServiceWrapper2_2_1(Context context)
	{
		return new PlaybackService(new AndroidWrapper(), "com.google.android.music", "com.android.music.MediaPlaybackService", context);
	}

	public static int getCurrentVendor(Context context)
	{
		byte byte0;
		if (hasPackage(context, "com.sec.android.app.music"))
			byte0 = 3;
		else
		if (hasPackage(context, "com.htc.music"))
			byte0 = 2;
		else
		if (hasPackage(context, "com.android.music"))
		{
			if (Build.BRAND.equals("samsung"))
				byte0 = 4;
			else
				byte0 = 1;
		} else
		if (hasPackage(context, "com.google.android.music"))
		{
			if (android.os.Build.VERSION.SDK_INT == 9)
				byte0 = 6;
			else
				byte0 = 5;
		} else
		{
			byte0 = -1;
		}
		return byte0;
	}

	public static PlaybackService getHtcServiceWrapper(Context context)
	{
		return new PlaybackService(new HtcWrapper(), "com.htc.music", context);
	}

	public static String getIntentPrefix(int i)
	{
		String s;
		if (i == 6)
			s = "com.android.music";
		else
			s = getPackage(i);
		return s;
	}

	public static String getPackage(int i)
	{
		String s = "";
		switch (i) {
		case 1:
			s = "com.android.music";
			break;
		case 2:
			s = "com.htc.music";
			break;
		case 3:
			s = "com.sec.android.app.music";
			break;
		case 4:
			s = "com.android.music";
			break;
		case 5:
			s = "com.android.music";
			break;
		case 6:
			s = "com.google.android.music";
			break;
		default:
			s = "";
			break;
		}
		
		return s;
	}

	public static PlaybackService getServiceWrapper(Context context, int i)
	{
		PlaybackService playbackservice = null;
		switch (i) {
		case 1:
			playbackservice = getAndroidServiceWrapper(context);
			break;
		case 2:
			playbackservice = getHtcServiceWrapper(context);
			break;
		case 3:
			playbackservice = null;
			break;
		case 4:
			playbackservice = null;
			break;
		case 5:
			playbackservice = getAndroidServiceWrapper2_2_1(context);
			break;
		default:
			playbackservice = null;
			break;
		}
		return playbackservice;
	
	}

	public static DecoratedBroadcastReceiver getSpecificBroadCastReceiver(int i, SimpleMediaAdapterAndroid simplemediaadapterandroid)
	{
		DecoratedBroadcastReceiver decoratedbroadcastreceiver = new DecoratedBroadcastReceiver();
		if (i == 3)
			decoratedbroadcastreceiver.addActionListener("com.sec.android.app.music.musicservicecommand.mediainfo", new SimpleSecReceiverAction(simplemediaadapterandroid));
		if (i == 4)
			decoratedbroadcastreceiver.addActionListener("com.android.music.musicservicecommand.mediainfo", new SimpleSecReceiverAction(simplemediaadapterandroid));
		if (i == 6)
			decoratedbroadcastreceiver = SimpleMediaReceivers.getGeneralMediaReceiver(simplemediaadapterandroid, getIntentPrefix(i));
		return decoratedbroadcastreceiver;
	}

	private static boolean hasPackage(Context context, String s)
	{
		boolean flag;
		PackageManager packagemanager;
		flag = false;
		packagemanager = context.getPackageManager();
		try {
			packagemanager.getApplicationInfo(s, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flag = true;

		return flag;

	}
}
