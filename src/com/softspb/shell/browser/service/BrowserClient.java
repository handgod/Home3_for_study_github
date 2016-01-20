// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import android.content.*;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.softspb.shell.browser.service:
//			BrowserService, IBrowserService, BrowserConfiguration, IBrowserServiceCallback

public abstract class BrowserClient
{

	static final Logger logger = Loggers.getLogger(BrowserClient.class);
	BrowserConfiguration browserConfiguration;
	private ServiceConnection connection;
	final Context context;
	boolean isBound;
	boolean isHtcBrowser;
	boolean isReady;
	IBrowserService service;
	IBrowserServiceCallback serviceCallback;

	protected BrowserClient(Context context1)
	{
		isHtcBrowser = false;
		serviceCallback = new IBrowserServiceCallback.Stub() {

			final BrowserClient this$0;

			public void onBookmarkDeleted(int i)
				throws RemoteException
			{
				BrowserClient.this.onBookmarkDeleted(i);
			}

			public void onBookmarkUpdated(int i, String s, String s1)
				throws RemoteException
			{
				BrowserClient.this.onBookmarkUpdated(i, s, s1);
			}

			
			{
				this$0 = BrowserClient.this;
//				super();
			}
		};
		connection = new ServiceConnection() {

			final BrowserClient this$0;

			public void onServiceConnected(ComponentName componentname, IBinder ibinder)
			{
				BrowserClient.logd((new StringBuilder()).append("onServiceConnected: ").append(componentname).toString());
				service = IBrowserService.Stub.asInterface(ibinder);
				try {
					service.registerCallback(serviceCallback);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					browserConfiguration = service.getBrowserConfiguration();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isHtcBrowser = browserConfiguration.isHtcBrowser;
				isReady = true;
				BrowserClient.logd((new StringBuilder()).append("onServiceConnected: connected to service, isHtcBrowser=").append(isHtcBrowser).toString());
				onConnected();
				return;

			}

			public void onServiceDisconnected(ComponentName componentname)
			{
				BrowserClient.logd((new StringBuilder()).append("onServiceDisconnected: ").append(componentname).toString());
				service = null;
				onDisconnected();
			}

			
			{
				this$0 = BrowserClient.this;
//				super();
			}
		};
		logd("Ctor");
		context = context1;
	}

	protected static void logd(String s)
	{
		logger.d(s);
	}

	public void connect()
	{
		logd("connect >>>");
		context.bindService(new Intent(context, BrowserService.class), connection, 1);
		isBound = true;
		logd("connect <<< attached service connection");
	}

	public void disconnect()
	{
		logd("disconnect >>>");
		if (isBound)
		{
			if (service != null)
				try
				{
					service.unregisterCallback(serviceCallback);
				}
				catch (RemoteException remoteexception) { }
			else
				logd("disconnect: service is null");
			context.unbindService(connection);
			logd("disconnect <<< service connection detached");
			isBound = false;
			isReady = false;
		} else
		{
			logd("disconnect <<< not bound");
		}
	}

	public boolean isConnected()
	{
		return isBound;
	}

	public boolean isReady()
	{
		return isReady;
	}

	public Bitmap loadIcon(int i)
	{
		Bitmap bitmap;
		bitmap = null;
		if (isReady)
		{
			Bitmap bitmap1 = null;
			try {
				bitmap1 = service.loadIcon(i);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bitmap = bitmap1;
		}
		return bitmap;

	}

	public Bitmap loadThumbnail(int i)
	{
		Bitmap bitmap;
		bitmap = null;
		if (isReady)
		{
			Bitmap bitmap1 = null;
			try {
				bitmap1 = service.loadThumbnail(i);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bitmap = bitmap1;
		}
		return bitmap;
	}

	protected abstract void onBookmarkDeleted(int i);

	protected abstract void onBookmarkUpdated(int i, String s, String s1);

	protected abstract void onConnected();

	protected abstract void onDisconnected();

	public void postLoadBookmarks()
	{
		logd((new StringBuilder()).append("postReloadBookmarks >>> isReady=").append(isReady).toString());
		if (isReady)
			try
			{
				service.loadBookmarks();
			}
			catch (RemoteException remoteexception) { }
		logd("postReloadBookmarks <<<");
	}

}
