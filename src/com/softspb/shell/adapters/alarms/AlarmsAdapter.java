// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.alarms;

import android.content.Context;
import com.softspb.shell.adapters.AbstractContentAdapter;
import com.softspb.util.log.Logger;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class AlarmsAdapter extends AbstractContentAdapter
{

	public static final long ALARM_NOT_SET =0;
	protected int nativePeer;

	public AlarmsAdapter(int i, Context context)
	{
		super(context);
		nativePeer = i;
	}

	public static long findAlarmTime(int i, int j, int k, Calendar calendar)
	{
		int l = 60 * calendar.get(11) + calendar.get(12);
		int i1 = -2 + calendar.get(7);
		if (i1 < 0)
			i1 += 7;
		int j1 = j + i * 60;
		int k1;
		for (k1 = (k + 7) - i1; k1 >= 7; k1 -= 7);
		int l1;
		long l2;
		if (k1 == 0)
		{
			if (j1 > l)
				l1 = 0;
			else
				l1 = 7;
		} else
		{
			l1 = k1;
		}
		if (l1 != 0x7fffffff)
		{
			GregorianCalendar gregoriancalendar = new GregorianCalendar(calendar.get(1), calendar.get(2), calendar.get(5), i, j);
			gregoriancalendar.add(5, l1);
			l2 = gregoriancalendar.getTimeInMillis();
		} else
		{
			l2 = 0x7fffffffffffffffL;
		}
		return l2;
	}
 public static long findNextAlarmTime(int paramInt1, int paramInt2, int paramInt3, Calendar paramCalendar)
  {
    int i = paramCalendar.get(11) * 60;
    int j = paramCalendar.get(12);
    int k = i + j;
    int m = paramCalendar.get(7) + -2;
    if (m < 0)
      m += 7;
    int n = paramInt1 * 60 + paramInt2;
    int i1 = 2147483647;
    int i2 = 0;
    int i3;
    long l = 0;
    if (i2 <= 6)
    {
      if ((paramInt3 & 0x1) != 0)
      {
	      i3 = i2 + 7 - m;
	      while (i3 >= 7)
	    	  i3 += -7;
	      if (i3 == 0)
	      {
	    	  if (n > k)
	    		  i1 = 0;
	      }
      }
    }
    else
    {
      if (i1 != 2147483647)
      {
	      int i4 = paramCalendar.get(1);
	      int i5 = paramCalendar.get(2);
	      int i6 = paramCalendar.get(5);
	      int i7 = paramInt1;
	      int i8 = paramInt2;
	      GregorianCalendar localGregorianCalendar = new GregorianCalendar(i4, i5, i6, i7, i8);
	      localGregorianCalendar.add(5, i1);
	      l = localGregorianCalendar.getTimeInMillis();
      }
    }
     return l;
  }
//	protected  void addNativeContentItem(com.softspb.shell.adapters.AbstractContentAdapter.ContentItem contentitem)
//	{
//		addNativeContentItem((AlarmsAdapterAndroid.Alarm)contentitem);
//	}

	protected void addNativeContentItem(AlarmsAdapterAndroid.Alarm alarm)
	{
	}

	protected native void onDateChanged(int i);

	public void onStop()
	{
		super.onStop();
		nativePeer = 0;
	}

	protected void removeNativeContentItem(ContentItem contentitem)
	{
		removeNativeContentItem((AlarmsAdapterAndroid.Alarm)contentitem);
	}

	protected void removeNativeContentItem(AlarmsAdapterAndroid.Alarm alarm)
	{
	}

	protected void setNativeNextAlarm(long l)
	{
		logger.d((new StringBuilder()).append("Invoking setNextAlarm: nativePeer=0x").append(Integer.toHexString(nativePeer)).append(" nextAlarmTime=").append(l).toString());
		if (nativePeer == 0)
		{
			throw new IllegalStateException("No nativePeer in AlarmsAdapter");
		} else
		{
			setNextAlarm(nativePeer, l);
			return;
		}
	}

	protected native void setNextAlarm(int i, long l);

	protected void updateNativeContentItem(ContentItem contentitem)
	{
		updateNativeContentItem((AlarmsAdapterAndroid.Alarm)contentitem);
	}

	protected void updateNativeContentItem(AlarmsAdapterAndroid.Alarm alarm)
	{
	}
}
