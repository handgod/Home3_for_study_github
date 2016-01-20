package com.softspb.shell.adapters.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.DateUtils;

import com.softspb.shell.adapters.AbstractContentAdapter;
import com.softspb.shell.adapters.AbstractContentAdapter.ContentItem;
import com.softspb.util.log.Logger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class AlarmsAdapterAndroid extends BaseAlarmsAdapter
{
  public static final String ACTION_ALARM_IS_RUN = "com.spb.shell3d.adapters.ACTION_ALARM_IS_RUN";
  static final String ALARMS_URI_PATH = "alarm";
  static final String ALARM_MAGIC_WORD = "alarm";
  private static final String AUTHORITY_ALARM_SAMSUNG = "com.samsung.sec.android.clockpackage";
  private static final String COLUMN_ALARMTIME_SAMSUNG = "alarmtime";
  private static final String COLUMN_ALERTTIME_SAMSUNG = "alerttime";
  private static final String COLUMN_ENABLED_SAMSUNG = "active";
  private static final String COLUMN_REPEATTYPE_SAMSUNG = "repeattype";
  PendingIntent alarmIntent;
  private AlarmIsRunReceiver alarmIsRunReceiver;
  private long nextAlarmTime;
  private Calendar now;
  private long nowMillis;
  boolean samsung = false;

  public AlarmsAdapterAndroid(int paramInt, Context paramContext)
  {
    super(paramInt, paramContext);
    Uri[] arrayOfUri = findAlarmsContentUris(paramContext);
    setContentUris(arrayOfUri);
  }

  	public static Uri checkAlarmProvider(ProviderInfo providerinfo)
	{
		Uri uri;
		if (providerinfo.name.toLowerCase().contains("alarm") || providerinfo.authority.toLowerCase().contains("alarm"))
			uri = Uri.parse((new StringBuilder()).append("content://").append(providerinfo.authority).append("/").append("alarm").toString());
		else
			uri = null;
		return uri;
	}

  static Uri[] findAlarmsContentUris(Context paramContext)
  {
    List localList = paramContext.getPackageManager().queryContentProviders(null, 0, 0);
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      Uri localUri = checkAlarmProvider((ProviderInfo)localIterator.next());
      if (localUri == null)
        continue;
      boolean bool = localArrayList.add(localUri);
    }
    Uri[] arrayOfUri = new Uri[localArrayList.size()];
    return (Uri[])localArrayList.toArray(arrayOfUri);
  }

  private boolean processRowSamsung(Cursor paramCursor, boolean paramBoolean)
  {
    this.logger.d("processRowSamsung");
    logRow("Alarm", paramCursor);
      int i = paramCursor.getColumnIndex("active");
      int j;
      long l1 = 0;
      if (paramCursor.getInt(i) != 0)
      {
        j = 1;
        if (j != 0)
        {
          int k = paramCursor.getColumnIndex("alarmtime");
          int m = paramCursor.getInt(k);
          int n = m / 100;
          int i1 = m % 100;
          int i2 = paramCursor.getColumnIndex("alerttime");
          l1 = paramCursor.getLong(i2);
          int i3 = paramCursor.getColumnIndex("repeattype");
          String str1 = paramCursor.getString(i3);
          Logger localLogger1 = this.logger;
          String str2 = "processRow: hour=" + n + " minutes=" + i1 + " alerttime=" + l1 + " repeattype=" + str1;
          localLogger1.d(str2);
          if (l1 != 0L)
          {
			 logger.d((new StringBuilder()).append("processRow: alarmTime: ").append(DateUtils.formatDateTime(context, l1, 17)).toString());

          }
		  else
		     logger.d("processRow: alarmTime: N/A");
          if (l1 < nextAlarmTime)
          	nextAlarmTime = l1;
        }
      }
      return true;
   
  }

  protected Alarm createContentItem(int paramInt, Cursor paramCursor)
  {
    return new Alarm(paramInt,paramCursor);
  }

  protected int getContentItemId(Cursor paramCursor)
  {
    int i = paramCursor.getColumnIndex("_id");
    return (int)paramCursor.getLong(i);
  }

  void logRow(String paramString, Cursor paramCursor)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(paramString).append("[");
    int i = 0;
    while (true)
    {
      int j = paramCursor.getColumnCount();
      if (i >= j)
        break;
      String str1 = paramCursor.getString(i);
      if (str1 != null)
      {
        StringBuilder localStringBuilder3 = localStringBuilder1.append(" ");
        String str2 = paramCursor.getColumnName(i);
        StringBuilder localStringBuilder4 = localStringBuilder3.append(str2).append("=").append(str1);
      }
      i += 1;
    }
    StringBuilder localStringBuilder5 = localStringBuilder1.append("]");
    Logger localLogger = this.logger;
    String str3 = localStringBuilder1.toString();
    localLogger.d(str3);
  }

  public void onStop()
  {
    super.onStop();
    if (this.alarmIsRunReceiver != null)
    {
      Context localContext = this.context;
      AlarmIsRunReceiver localAlarmIsRunReceiver = this.alarmIsRunReceiver;
      localContext.unregisterReceiver(localAlarmIsRunReceiver);
    }
  }

  protected boolean processRow(Cursor paramCursor, boolean paramBoolean)
  {
    if (this.samsung);
    for (boolean bool = processRowSamsung(paramCursor, paramBoolean); ; bool = processRowDefault(paramCursor, paramBoolean))
      return bool;
  }

	protected boolean processRowDefault(Cursor cursor, boolean flag)
	{
		logger.d("processRowDefault");
		logRow("Alarm", cursor);
		boolean flag1;
			
		if (cursor.getInt(cursor.getColumnIndex("enabled")) != 0)
		{
			flag1 = true;
			int i = cursor.getInt(cursor.getColumnIndex("daysofweek"));
			logger.d((new StringBuilder()).append("processRow: daysOfWeek=").append(i).toString());
			long l;
			if (i == 0)
			{
				l = cursor.getLong(cursor.getColumnIndex("alarmtime"));
			} else
			{
				int j = cursor.getInt(cursor.getColumnIndex("hour"));
				int k = cursor.getInt(cursor.getColumnIndex("minutes"));
				logger.d((new StringBuilder()).append("processRow: hour=").append(j).append(" minutes=").append(k).append(" daysOfWeek=").append(i).toString());
				l = findNextAlarmTime(j, k, i, now);
			}
			if (l != 0x7fffffffffffffffL)
				logger.d((new StringBuilder()).append("processRow: alarmTime: ").append(DateUtils.formatDateTime(context, l, 17)).toString());
			else
				logger.d("processRow: alarmTime: N/A");
			if (l != 0L && l < nextAlarmTime && l > nowMillis)
				nextAlarmTime = l;
		}
		else
		   flag1 = false;
		return flag1;	
  }

 	protected Cursor query()
	{
		ArrayList arraylist = new ArrayList();
		samsung = false;
		Uri auri[] = contentUris;
		int i = auri.length;
		int j = 0;
		int k;
		Object obj;
		while (j < i) 
		{
			Uri uri = auri[j];
			samsung = samsung | "com.samsung.sec.android.clockpackage".equals(uri.getAuthority());
			
			try
			{
				logger.d((new StringBuilder()).append("query: uri=").append(uri).toString());
				Cursor cursor = contentResolver.query(uri, null, null, null, null);
				logger.d((new StringBuilder()).append("query: cursor=").append(cursor).toString());
				if (cursor != null)
					arraylist.add(cursor);
			}
			catch (Exception exception) { }
			j++;
		}
		k = arraylist.size();
		if (k == 0)
			obj = null;
		else
			obj = new MergeCursor((Cursor[])arraylist.toArray(new Cursor[k]));
		return ((Cursor) (obj));
	}

	public synchronized void reload(boolean flag)
	{
		
		Object obj = reloadLock;
		synchronized(obj)
		{
			Cursor cursor;
			logger.d((new StringBuilder()).append("reload >>> doNotify=").append(flag).toString());
			cursor = null;
			nextAlarmTime = 0x7fffffffffffffffL;
			now = new GregorianCalendar();
			nowMillis = now.getTimeInMillis();
			cursor = query();
			if (cursor == null || !cursor.moveToFirst())
				{
					
				}
			 else 
				{
				 	 while (!cursor.isAfterLast() && processRow(cursor, flag)) 
				 		{
				 			cursor.moveToNext();
				 		}
				 	if (nextAlarmTime == 0x7fffffffffffffffL)
						nextAlarmTime = 0L;
					setNativeNextAlarm(nextAlarmTime);
					resetAlarmReceiver();
				}
		}
	}

	void resetAlarmReceiver()
	{
		if (!samsung) 
		{
			if (alarmIsRunReceiver == null)
			{
				alarmIsRunReceiver = new AlarmIsRunReceiver();
				context.registerReceiver(alarmIsRunReceiver, new IntentFilter("com.spb.shell3d.adapters.ACTION_ALARM_IS_RUN"));
			}
			AlarmManager alarmmanager = (AlarmManager)context.getSystemService("alarm");
			if (alarmIntent != null)
			{
				alarmmanager.cancel(alarmIntent);
				alarmIntent = null;
			}
			if (nextAlarmTime != 0L && nextAlarmTime > nowMillis)
			{
				logger.d((new StringBuilder()).append("resetAlarmReceiver: nextAlarmTime=").append(nextAlarmTime).toString());
				alarmIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.spb.shell3d.adapters.ACTION_ALARM_IS_RUN"), 0x40000000);
				alarmmanager.set(1, nextAlarmTime, alarmIntent);
			}
		}
		else 
			return;
	}

  class AlarmIsRunReceiver extends BroadcastReceiver
  {
    AlarmIsRunReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      AlarmsAdapterAndroid.this.logger.d("AlarmIsRunReceiver.onReceive");
      String str = paramIntent.getAction();
      if ("com.spb.shell3d.adapters.ACTION_ALARM_IS_RUN".equals(str))
        AlarmsAdapterAndroid.this.reload(true);
    }
  }

  class Alarm extends AbstractContentAdapter.ContentItem
    implements AlarmsAdapterAndroid.AlarmColumns
  {
    int daysOfWeek;
    boolean enabled;
    int hour;
    int minutes;

//    Alarm(Cursor arg2)
//    {
//      super(localCursor);
//      int i = localCursor.getColumnIndex("hour");
//      int j = localCursor.getInt(i);
//      this.hour = j;
//      int k = localCursor.getColumnIndex("minutes");
//      int m = localCursor.getInt(k);
//      this.minutes = m;
//      int n = localCursor.getColumnIndex("daysofweek");
//      int i1 = localCursor.getInt(n);
//      this.daysOfWeek = i1;
//      int i2 = localCursor.getColumnIndex("enabled");
//      if (localCursor.getInt(i2) != 0);
//      for (int i3 = 1; ; i3 = 0)
//      {
//        this.enabled = i3;
//        return;
//      }
//    }
    Alarm(int i, Cursor cursor)
	{
		super(i, cursor);
		hour = cursor.getInt(cursor.getColumnIndex("hour"));
		minutes = cursor.getInt(cursor.getColumnIndex("minutes"));
		daysOfWeek = cursor.getInt(cursor.getColumnIndex("daysofweek"));
		boolean flag;
		if (cursor.getInt(cursor.getColumnIndex("enabled")) != 0)
			flag = true;
		else
			flag = false;
		enabled = flag;
	}
    protected void formatFields(StringBuilder paramStringBuilder)
    {
      StringBuilder localStringBuilder1 = paramStringBuilder.append(" hour=");
      int i = this.hour;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(i);
      StringBuilder localStringBuilder3 = paramStringBuilder.append(" minutes=");
      int j = this.minutes;
      StringBuilder localStringBuilder4 = localStringBuilder3.append(j);
      StringBuilder localStringBuilder5 = paramStringBuilder.append(" daysOfWeek=");
      int k = this.daysOfWeek;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(k);
      StringBuilder localStringBuilder7 = paramStringBuilder.append(" enabled=");
      boolean bool = this.enabled;
      StringBuilder localStringBuilder8 = localStringBuilder7.append(bool);
    }
    public boolean update(Cursor cursor)
	{
		boolean flag = false;
		int i = cursor.getInt(cursor.getColumnIndex("hour"));
		int j = cursor.getInt(cursor.getColumnIndex("minutes"));
		int k = cursor.getInt(cursor.getColumnIndex("daysofweek"));
		boolean flag1;
		if (cursor.getInt(cursor.getColumnIndex("enabled")) != 0)
			flag1 = true;
		else
			flag1 = false;
		if (i != hour)
		{
			flag = true;
			hour = i;
		}
		if (j != minutes)
		{
			flag = true;
			minutes = j;
		}
		if (k != daysOfWeek)
		{
			flag = true;
			daysOfWeek = k;
		}
		if (flag1 != enabled)
		{
			flag = true;
			enabled = flag1;
		}
		return flag;
	}
  }

	

  abstract interface AlarmColumns extends BaseColumns
  {
    public static final String ALARM_TIME = "alarmtime";
    public static final String ALERT = "alert";
    public static final String DAYS_OF_WEEK = "daysofweek";
    public static final String ENABLED = "enabled";
    public static final String HOUR = "hour";
    public static final String MESSAGE = "message";
    public static final String MINUTES = "minutes";
    public static final String VIBRATE = "vibrate";
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.alarms.AlarmsAdapterAndroid
 * JD-Core Version:    0.6.0
 */