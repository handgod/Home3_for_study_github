// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.*;
import android.text.format.Time;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.Iterator;
import java.util.LinkedList;

public final class DateChangedObserver extends BroadcastReceiver
{
	public static interface DateChangedListener
	{

		public abstract void onDateChanged();
	}


	private static final String ACTION_MIDNIGHT = "com.spb.shell3d.ACTION_MIDNIGHT";
	private static volatile DateChangedObserver instance;
	private static Logger logger = Loggers.getLogger(DateChangedObserver.class.getName());
	private int eventCounter;
	private Time latestCurrentDate;
	private final LinkedList listeners = new LinkedList();
	private PendingIntent midnightAlarm;
	private boolean notifyEnabled;

	private DateChangedObserver()
	{
		notifyEnabled = false;
	}

	private Time getCurrentDate()
	{
		Time time = new Time();
		time.setToNow();
		time.hour = 0;
		time.minute = 0;
		time.second = 0;
		time.normalize(false);
		return time;
	}

	public static DateChangedObserver getInstance()
	{
		if (instance != null) 
		{
			return instance;
		}
		else
		{
			if (instance == null)
				instance = new DateChangedObserver();
		}
		
		return (DateChangedObserver)instance;
	}

	private Time getNextMidnight()
	{
		Time time = getCurrentDate();
		time.monthDay = 1 + time.monthDay;
		time.normalize(false);
		return time;
	}

	private void notifyListeners()
	{
		DateChangedListener datechangedlistener;
		LinkedList linkedlist = listeners;
		synchronized (linkedlist) {
			for (Iterator iterator = listeners.iterator(); iterator.hasNext(); datechangedlistener.onDateChanged())
			{
				datechangedlistener = (DateChangedListener)iterator.next();
				logger.d((new StringBuilder()).append("Notifying listener: ").append(datechangedlistener).toString());
			}
		}
	}

	private void registerForEvents(Context context)
	{
		logger.d("registerForEvents");
		IntentFilter intentfilter = new IntentFilter();
		midnightAlarm = PendingIntent.getBroadcast(context, 0, new Intent("com.spb.shell3d.ACTION_MIDNIGHT"), 0x50000000);
		Time time = getNextMidnight();
		((AlarmManager)context.getSystemService("alarm")).set(1, time.toMillis(false), midnightAlarm);
		intentfilter.addAction("com.spb.shell3d.ACTION_MIDNIGHT");
		intentfilter.addAction("android.intent.action.DATE_CHANGED");
		intentfilter.addAction("android.intent.action.TIME_SET");
		intentfilter.addAction("android.intent.action.TIMEZONE_CHANGED");
		context.registerReceiver(this, intentfilter);
	}

	private void unregisterFromEvents(Context context)
	{
		logger.d("unregisterFromEvents");
		AlarmManager alarmmanager = (AlarmManager)context.getSystemService("alarm");
		if (midnightAlarm != null)
		{
			alarmmanager.cancel(midnightAlarm);
			midnightAlarm = null;
		}
		context.unregisterReceiver(this);
	}

	public void onReceive(Context context, Intent intent)
	{
		logger.d((new StringBuilder()).append("event: ").append(intent.getAction()).toString());
		eventCounter = 1 + eventCounter;
		if (notifyEnabled)
			notifyListeners();
	}

	public void pause(Context context)
	{
		logger.d("pause");
		notifyEnabled = false;
		eventCounter = 0;
	}

	public void registerListener(DateChangedListener datechangedlistener)
	{
		LinkedList linkedlist = listeners;
		synchronized (linkedlist) {
			if (!listeners.contains(datechangedlistener))
			{
				listeners.add(datechangedlistener);
				logger.d((new StringBuilder()).append("Added listener: ").append(datechangedlistener).toString());
				latestCurrentDate = null;
			}
		}		
		return;
	}

	public void resume(Context context)
	{
		logger.d("resume");
		Time time = getCurrentDate();
		if (latestCurrentDate == null || Time.compare(time, latestCurrentDate) != 0 || eventCounter > 0)
		{
			notifyListeners();
			latestCurrentDate = time;
		}
		notifyEnabled = true;
	}

	public void start(Context context)
	{
		logger.d("start");
		registerForEvents(context);
	}

	public void stop(Context context)
	{
		logger.d("stop");
		unregisterFromEvents(context);
	}

	public void unregisterListener(DateChangedListener datechangedlistener)
	{
		LinkedList linkedlist = listeners;
		synchronized (linkedlist) {
			listeners.remove(datechangedlistener);
			logger.d((new StringBuilder()).append("Removed listener: ").append(datechangedlistener).toString());
		}
		return;
	}

}
