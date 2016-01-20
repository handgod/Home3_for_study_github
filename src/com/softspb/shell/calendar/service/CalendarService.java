// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.calendar.service;

import android.app.Service;
import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.os.Process;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.*;
import android.provider.*;
// Referenced classes of package com.softspb.shell.calendar.service:
//			Appointment, LoadAppointmentsParams

public class CalendarService extends Service
{
	class IncomingHandler extends Handler
	{

		final CalendarService this$0;

		public void handleMessage(Message message)
		{
			switch (message.what) {
			case 1:
				CalendarService.logd("handleMessage: REGISTER_CLIENT");
				clients.add(message.replyTo);
				break;
			case 2:
				CalendarService.logd("handleMessage: UNREGISTER_CLIENT");
				clients.remove(message.replyTo);
				break;
			case 3:
				CalendarService.logd("handleMessage: LOAD_APPOINTMENTS");
				Bundle bundle = message.getData();
				bundle.setClassLoader(classLoader);
				LoadAppointmentsParams loadappointmentsparams = (LoadAppointmentsParams)bundle.getParcelable("load-appts-params");
				loadAppointments(loadappointmentsparams.token, loadappointmentsparams.searchStartDate, loadappointmentsparams.searchEndDate);		
				break;
			case 99:
				CalendarService.logd("handleMessage: GET_PID");
				Messenger messenger1 = message.replyTo;
				Message message1 = Message.obtain(null, 8, Process.myPid(), 0);
				try
				{
					messenger1.send(message1);
				}
				catch (RemoteException remoteexception) { }
				break;
			default:
				super.handleMessage(message);
				break;
			}
			return;
		}

		IncomingHandler()
		{
			this$0 = CalendarService.this;
//			super();
		}
	}


	public static final String CALENDARS_PROJECTION[];
	public static final String CANELDAR_PROJECTION[];
	private static final String ENABLED_CALENDARS_QUERY;
	static final String EXTRA_APPOINTMENT = "appointment";
	static final String EXTRA_LOAD_APPOINTMENTS_PARAMS = "load-appts-params";
	public static final int INDEX_ALL_DAY = 5;
	public static final int INDEX_CALENDAR_ID = 12;
	public static final int INDEX_DESC = 2;
	public static final int INDEX_END = 1;
	public static final int INDEX_END_DAY = 9;
	public static final int INDEX_END_MINUTE = 11;
	public static final int INDEX_ID = 7;
	public static final int INDEX_LOCATION = 4;
	public static final int INDEX_RRULE = 6;
	public static final int INDEX_START = 0;
	public static final int INDEX_START_DAY = 8;
	public static final int INDEX_START_MINUTE = 10;
	public static final int INDEX_TITLE = 3;
	static final int MSG_GET_PID = 99;
	static final int MSG_LOAD_APPOINTMENTS = 3;
	static final int MSG_ON_APPOINTMENT_LOADED = 4;
	static final int MSG_ON_CALENDAR_CHANGED = 5;
	static final int MSG_ON_FINISHED_RELOADING_APPOINTMENTS = 7;
	static final int MSG_ON_STARTED_RELOADING_APPOINTMENTS = 6;
	static final int MSG_REGISTER_CLIENT = 1;
	static final int MSG_RESPONSE_PID = 8;
	static final int MSG_UNREGISTER_CLIENT = 2;
	private static final Logger logger = Loggers.getLogger(CalendarService.class);
	private Uri CALENDAR_CALENDARS_URI;
	private Uri CALENDAR_CONTENT_URI;
	private Uri CALENDAR_OBSERVE_URI;
	ClassLoader classLoader;
	ArrayList clients;
	private ContentObserver contentObserver;
	final Messenger messenger = new Messenger(new IncomingHandler());

	public CalendarService()
	{
		clients = new ArrayList();
		Handler localhandler = new Handler();
		contentObserver = new ContentObserver(new Handler()) {
			
			final CalendarService this$0;

			public void onChange(boolean flag)
			{
				CalendarService.logd("onReceive >>>");
				notifyClients(5, "ON_CALENDAR_CHANGED");
				CalendarService.logd("onReceive <<<");
			}
			
			{
//				super(new Handler());
				this$0 = CalendarService.this;
			}
			
			
		};
	}

	public static String convertTime(long l)
	{
		return (new StringBuilder()).append(l).append("(").append(new Date(l)).append(")").toString();
	}

	private List getEnabledCalendarIds()
	{
		Object obj;
		Cursor cursor;
		obj = Collections.emptyList();
		cursor = null;
		cursor = getContentResolver().query(CALENDAR_CALENDARS_URI, CALENDARS_PROJECTION, ENABLED_CALENDARS_QUERY, null, null);
		if (cursor != null)
		{
			
			ArrayList arraylist = new ArrayList(cursor.getCount());
			for (; cursor.moveToNext(); arraylist.add(Long.valueOf(cursor.getLong(0))));
			obj = arraylist;
			
		}
		else 
		{
			if (cursor != null)
				cursor.close();
		}	
		return ((List) (obj));

	}

	private void loadAppointments(int i, long l, long l1)
	{
		List list;
		Uri uri1;
		Cursor cursor;
		Log.i("CalendarService", "loadAppointments");
		logd((new StringBuilder()).append("loadAppointments >>> searchStartDate=").append(convertTime(l)).append(", searchEndDate=").append(convertTime(l1)).toString());
		list = getEnabledCalendarIds();
		Uri uri = CALENDAR_CONTENT_URI;
		Object aobj[] = new Object[2];
		aobj[0] = Long.valueOf(l);
		aobj[1] = Long.valueOf(l1);
		uri1 = Uri.withAppendedPath(uri, String.format("%d/%d", aobj));
		Log.i("CalendarService", (new StringBuilder()).append("loadAppointments[uri=").append(uri1).append("]").toString());
		cursor = null;
		notifyClients(6, i, "ON_STARTED_RELOADING_APPOINTMENTS");
		cursor = getContentResolver().query(uri1, CANELDAR_PROJECTION, null, null, null);
		if (cursor == null)
		{
			Log.i("CalendarService", "loadAppointments[cursor==null]");
			logw("loadAppointments: cursor is null");
		}
		else
		{
			logd((new StringBuilder()).append("loadAppointments: cursor is not null, count=").append(cursor.getCount()).toString());
		}
		
_L1:
		logd((new StringBuilder()).append("loadAppointments: cursor is not null, count=").append(cursor.getCount()).toString());
_L6:    
	    while (!list.contains(Long.valueOf(cursor.getLong(12))))
	    {
			while(!cursor.moveToNext())
			{	
				
			}
	    }
		long l2;
		long l3;
		String s;
		String s1;
		boolean flag;
		boolean flag1;
		l2 = cursor.getLong(0);
		l3 = cursor.getLong(1);
		cursor.getString(2);
		s = cursor.getString(3);
		s1 = cursor.getString(4);
		if (cursor.getInt(5) != 1) 
		{
			flag = false;
		}
		else 
		{
			flag = true;
		}
		
		if (TextUtils.isEmpty(cursor.getString(6))) 
		{
			flag1 = false;
		}
		else 
		{
			flag1 = true;
		}
		long l4 = cursor.getLong(7);
		int j = cursor.getInt(8);
		int k = cursor.getInt(10);
		Time time = new Time();
		time.setJulianDay(j);
		time.second = k * 60;
		int i1 = cursor.getInt(9);
		int j1 = cursor.getInt(11);
		Time time1 = new Time();
		time1.setJulianDay(i1);
		time1.second = j1 * 60;
		if (android.os.Build.VERSION.SDK_INT >= 9)
		{
			l2 = time.toMillis(true);
			l3 = time1.toMillis(true);
		}
		notifyOnAppointmentLoaded(i, s, s1, time.toMillis(true), time1.toMillis(true), l2, l3, flag, flag1, 0, l4);
		while(!cursor.moveToNext())
		{	
			
		}
		if (cursor != null)
			try
			{
				cursor.close();
			}
			catch (Exception exception2) { }
		return;
	}

	private static void logd(String s)
	{
		logger.d((new StringBuilder()).append("ApptList ").append(s).toString());
	}

	private static void loge(String s, Throwable throwable)
	{
		logger.e((new StringBuilder()).append("ApptList ").append(s).toString(), throwable);
	}

	private static void logw(String s)
	{
		logger.w((new StringBuilder()).append("ApptList ").append(s).toString());
	}

	void notifyClients(int i, int j, String s)
	{
		logd((new StringBuilder()).append("notifyClients >>> ").append(s).append(" token=").append(j).toString());
		int k = -1 + clients.size();
		while (k >= 0) 
		{
			try
			{
				((Messenger)clients.get(k)).send(Message.obtain(null, i, j, 0));
			}
			catch (RemoteException remoteexception)
			{
				clients.remove(k);
			}
			k--;
		}
		logd((new StringBuilder()).append("notifyClients <<< ").append(s).toString());
	}

	void notifyClients(int i, String s)
	{
		logd((new StringBuilder()).append("notifyClients >>> ").append(s).toString());
		int j = -1 + clients.size();
		while (j >= 0) 
		{
			try
			{
				((Messenger)clients.get(j)).send(Message.obtain(null, i));
			}
			catch (RemoteException remoteexception)
			{
				clients.remove(j);
			}
			j--;
		}
		logd((new StringBuilder()).append("notifyClients <<< ").append(s).toString());
	}

	void notifyOnAppointmentLoaded(int i, String s, String s1, long l, long l1, 
			long l2, long l3, boolean flag, boolean flag1, int j, 
			long l4)
	{
		logd((new StringBuilder()).append("notifyOnAppointmentLoaded >>> id=").append(l4).append(" subject=\"").append(s).append("\"").toString());
		Appointment appointment = new Appointment(i, s, s1, l, l1, l2, l3, flag, flag1, j, l4);
		int k = -1 + clients.size();
		while (k >= 0) 
		{
			try
			{
				Message message = Message.obtain(null, 4);
				Bundle bundle = new Bundle();
				bundle.putParcelable("appointment", appointment);
				message.setData(bundle);
				((Messenger)clients.get(k)).send(message);
			}
			catch (RemoteException remoteexception)
			{
				clients.remove(k);
			}
			k--;
		}
		logd("notifyOnAppointmentLoaded <<<");
	}

	public IBinder onBind(Intent intent)
	{
		return messenger.getBinder();
	}

	public void onCreate()
	{
		logd("onCreate >>>");
		super.onCreate();
		boolean flag;
		boolean flag1;
		String s;
		if (android.os.Build.VERSION.SDK_INT > 7)
			flag = true;
		else
			flag = false;
		if (android.os.Build.VERSION.SDK_INT >= 14)
			flag1 = true;
		else
			flag1 = false;
		if (flag)
			s = "com.android.calendar";
		else
		if (flag1)
			s = "com.android.calendar";
		else
			s = "calendar";
		logd((new StringBuilder()).append("onCreate: authority=").append(s).toString());
		if (!flag1)
		{
			CALENDAR_CONTENT_URI = Uri.parse((new StringBuilder()).append("content://").append(s).append("/instances/when").toString());
			CALENDAR_OBSERVE_URI = Uri.parse((new StringBuilder()).append("content://").append(s).append("/events").toString());
			CALENDAR_CALENDARS_URI = Uri.parse((new StringBuilder()).append("content://").append(s).append("/calendars").toString());
		} else
		{
//			CALENDAR_CONTENT_URI = android.provider.CalendarContract.Instances.CONTENT_URI;
//			CALENDAR_OBSERVE_URI = android.provider.CalendarContract.Events.CONTENT_URI;
//			CALENDAR_CALENDARS_URI = android.provider.CalendarContract.Calendars.CONTENT_URI;
		}
		try
		{
			classLoader = createPackageContext(getPackageName(), 3).getClassLoader();
		}
		catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception)
		{
			loge((new StringBuilder()).append("Failed to initialize class loader: ").append(namenotfoundexception).toString(), namenotfoundexception);
		}
		getContentResolver().registerContentObserver(CALENDAR_OBSERVE_URI, true, contentObserver);
		logd("onCreate <<<");
	}

	public void onDestroy()
	{
		logd("onDestroy >>>");
		super.onDestroy();
		getContentResolver().unregisterContentObserver(contentObserver);
		logd("onDestroy >>>");
	}

	static 
	{
		String as1[];
		if (android.os.Build.VERSION.SDK_INT >= 14)
		{
			String as2[] = new String[13];
			as2[0] = "begin";
			as2[1] = "end";
			as2[2] = "description";
			as2[3] = "title";
			as2[4] = "eventLocation";
			as2[5] = "allDay";
			as2[6] = "rrule";
			as2[7] = "event_id";
			as2[8] = "startDay";
			as2[9] = "endDay";
			as2[10] = "startMinute";
			as2[11] = "endMinute";
			as2[12] = "calendar_id";
			CANELDAR_PROJECTION = as2;
			ENABLED_CALENDARS_QUERY = "visible=1";
		} else
		{
			String as[] = new String[13];
			as[0] = "begin";
			as[1] = "end";
			as[2] = "description";
			as[3] = "title";
			as[4] = "eventLocation";
			as[5] = "allDay";
			as[6] = "rrule";
			as[7] = "event_id";
			as[8] = "startDay";
			as[9] = "endDay";
			as[10] = "startMinute";
			as[11] = "endMinute";
			as[12] = "calendar_id";
			CANELDAR_PROJECTION = as;
			ENABLED_CALENDARS_QUERY = "selected=1";
		}
		as1 = new String[1];
		as1[0] = "_id";
		CALENDARS_PROJECTION = as1;
	}


}
