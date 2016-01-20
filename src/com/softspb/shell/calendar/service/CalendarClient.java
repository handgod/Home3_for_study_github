// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.calendar.service;

import android.content.*;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.*;

import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.softspb.shell.calendar.service:
//			CalendarService, LoadAppointmentsParams, Appointment

public abstract class CalendarClient
{
	class IncomingHandler extends Handler
	{

		final CalendarClient this$0;

		public void handleMessage(Message message)
		{
			switch (message.what) {
			case 4:
				CalendarClient.logd("handleMessage: ON_APPOINTMENT_LOADED");
				Bundle bundle = message.getData();
				bundle.setClassLoader(classLoader);
				Appointment appointment = (Appointment)bundle.getParcelable("appointment");
				onAppointmentLoaded(appointment);
				break;
			case 5:
				CalendarClient.logd("handleMessage: ON_CALENDAR_CHANGED");
				onCalendarChanged();
				break;
			case 6:
				CalendarClient.logd("handleMessage: ON_STARTED_RELOADING_APPOINTMENTS");
				onStartedReloadingAppointments(message.arg1);
				break;
			case 7:
				CalendarClient.logd("handleMessage: ON_FINISHED_RELOADING_APPOINTMENTS");
				onFinishedReloadingAppointments(message.arg1);
				break;
			case 8:
				CalendarClient.logd("handleMessage: RESPONSE_PID");
				onPidResponse(message.arg1);
				break;
			default:
				super.handleMessage(message);
				break;
			}
			
			return;
		}

		IncomingHandler()
		{
			this$0 = CalendarClient.this;
//			super();
		}
	}


	static final Logger logger = Loggers.getLogger(CalendarClient.class);
	ClassLoader classLoader;
	private ServiceConnection connection;
	final Context context;
	boolean isBound;
	final Messenger messenger;
	Messenger service;

	protected CalendarClient(Context context1)
	{
		service = null;
		messenger = new Messenger(new IncomingHandler());
		connection = new ServiceConnection() {

			final CalendarClient this$0;

			public void onServiceConnected(ComponentName componentname, IBinder ibinder)
			{
				CalendarClient.logd((new StringBuilder()).append("onServiceConnected: ").append(componentname).toString());
				service = new Messenger(ibinder);
				Message message = Message.obtain(null, 1);
				message.replyTo = messenger;
				try {
					service.send(message);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CalendarClient.logd("onServiceConnected <<< sent REGISTER");
				onConnected();	
				return;
				
			}

			public void onServiceDisconnected(ComponentName componentname)
			{
				CalendarClient.logd((new StringBuilder()).append("onServiceDisconnected: ").append(componentname).toString());
				service = null;
				onDisconnected();
			}

			
			{
				this$0 = CalendarClient.this;
//				super();
			}
		};
		logd("Ctor");
		context = context1;
		try {
			classLoader = context1.createPackageContext(context1.getPackageName(), 3).getClassLoader();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;

	}

	protected static void logd(String s)
	{
		logger.d((new StringBuilder()).append("ApptList ").append(s).toString());
	}

	public void connect()
	{
		logd("connect >>>");
		context.bindService(new Intent(context, CalendarService.class), connection, 1);
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
					Message message = Message.obtain(null, 2);
					message.replyTo = messenger;
					service.send(message);
					logd("disconnect: sent UNREGISTER");
				}
				catch (RemoteException remoteexception) { }
			else
				logd("disconnect: service is null");
			context.unbindService(connection);
			logd("disconnect <<< service connection detached");
			isBound = false;
		} else
		{
			logd("disconnect <<< not bound");
		}
	}

	public boolean isConnected()
	{
		return isBound;
	}

	public void loadAppointments(int i, long l, long l1)
	{
		logd((new StringBuilder()).append("loadAppointments >>> searchStartDate=").append(l).append(" searchEndDate=").append(l1).toString());
		if (service == null)
		{
			Message message = Message.obtain(null, 3);
			LoadAppointmentsParams loadappointmentsparams = new LoadAppointmentsParams(i, l, l1);
			Bundle bundle = new Bundle();
			bundle.putParcelable("load-appts-params", loadappointmentsparams);
			message.setData(bundle);
			try {
				service.send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logd("loadAppointments <<< sent request");
		}
		return;
	}

	protected abstract void onAppointmentLoaded(Appointment appointment);

	protected abstract void onCalendarChanged();

	protected abstract void onConnected();

	protected abstract void onDisconnected();

	protected abstract void onFinishedReloadingAppointments(int i);

	protected abstract void onPidResponse(int i);

	protected abstract void onStartedReloadingAppointments(int i);

	public void requestServicePid()
	{
		Message message;
		logd("requestServicePid >>>");
		if (service == null)
		{
			message = Message.obtain(null, 99);
			message.replyTo = messenger;
			try {
				service.send(message);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logd("requestServicePid <<< request sent");
		}
		return;

	}

}
