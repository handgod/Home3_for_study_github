// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.calendar.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

// Referenced classes of package com.softspb.shell.calendar.service:
//			CalendarClient, Appointment

public class KillCalendarService extends Service
{

	private static final String TAG = "KillCalendarService";

	public KillCalendarService()
	{
	}

	private static void logd(String s)
	{
		Log.d("KillCalendarService", s);
	}

	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public int onStartCommand(Intent intent, int i, int j)
	{
		logd("start killing Calendar Service");
		(new CalendarClient(this) {

			private int pid;
			final KillCalendarService this$0;

			protected void onAppointmentLoaded(Appointment appointment)
			{
			}

			protected void onCalendarChanged()
			{
			}

			protected void onConnected()
			{
				logd("onConnected");
				requestServicePid();
			}

			protected void onDisconnected()
			{
				logd("onDisconnected");
			}

			protected void onFinishedReloadingAppointments(int k)
			{
			}

			protected void onPidResponse(int k)
			{
				logd((new StringBuilder()).append("onPidResponse: pid=").append(k).toString());
				pid = k;
				disconnect();
				if (k != 0)
				{
					logd((new StringBuilder()).append("onPidResponse: killing pid=").append(k).toString());
					Process.killProcess(k);
				}
				stopSelf();
			}

			protected void onStartedReloadingAppointments(int k)
			{
			}
			{
//				super(context);
				this$0 = KillCalendarService.this;
			}
		}).connect();
		return 2;
	}
}
