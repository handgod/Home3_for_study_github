// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.app.Service;
import android.content.*;
import android.os.*;
import android.os.Process;

import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.spb.contacts:
//			IGetPid, ContactsService

public class KillContactsService extends Service
{

	ServiceConnection connection;
	Logger logger;

	public KillContactsService()
	{
		connection = new ServiceConnection() {

			final KillContactsService this$0;

			public void onServiceConnected(ComponentName componentname, IBinder ibinder)
			{
				logger.d((new StringBuilder()).append("onServiceConnected: ").append(componentname).toString());
				int i = 0;
				try
				{
					i = IGetPid.Stub.asInterface(ibinder).getPid();
					logger.d((new StringBuilder()).append("Got pid from contacts service: ").append(i).toString());
				}
				catch (RemoteException remoteexception)
				{
					logger.e((new StringBuilder()).append("Failed to get pid from contacts service: ").append(remoteexception).toString(), remoteexception);
				}
				unbindService(this);
				if (i == 0)
				{
					logger.w("Contacts service pid unknown");
				} else
				{
					logger.d((new StringBuilder()).append("Killing contacts service process: ").append(i).toString());
					Process.killProcess(i);
				}
				stopSelf();
			}

			public void onServiceDisconnected(ComponentName componentname)
			{
				logger.d((new StringBuilder()).append("onServiceDisconnected: ").append(componentname).toString());
			}

			
			{
				this$0 = KillContactsService.this;
//				super();
			}
		};
		logger = Loggers.getLogger(KillContactsService.class);
	}

	private void killContactsService()
	{
		logger.d("killContactsService >>>");
		Intent intent = new Intent(IGetPid.class.getName());
		intent.setClassName(this, ContactsService.class.getName());
		bindService(intent, connection, 1);
		logger.d("killContactsService <<<");
	}

	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public int onStartCommand(Intent intent, int i, int j)
	{
		killContactsService();
		return 2;
	}
}
