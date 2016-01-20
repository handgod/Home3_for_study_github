// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.text.format.Time;
import java.util.*;

public final class DecimalDateTimeEncoding
{

	private static ThreadLocal gregCalendar = new ThreadLocal();
	private static TimeZone timeZoneUT = TimeZone.getTimeZone("GMT+0");

	private DecimalDateTimeEncoding()
	{
	}

	public static int add(int i, int j)
	{
		Date date = decodeDate(i);
		GregorianCalendar gregoriancalendar = useCalendarInstance();
		gregoriancalendar.setTime(date);
		gregoriancalendar.add(5, j);
		return encodeCalendar(gregoriancalendar);
	}

	public static int daysBetween(int i, int j)
	{
		Time time = fromDateUTC(i);
		long l = fromDateUTC(j).toMillis(true) - time.toMillis(true);
		long l1;
		if (l >= 0L)
			l1 = l + 0x2932e00L;
		else
			l1 = l - 0x2932e00L;
		return (int)(l1 / 0x5265c00L);
	}

	public static Date decodeDate(int i)
	{
		GregorianCalendar gregoriancalendar = useCalendarInstance();
		gregoriancalendar.setTimeInMillis(0L);
		gregoriancalendar.set(5, i % 100);
		int j = i / 100;
		gregoriancalendar.set(2, -1 + j % 100);
		gregoriancalendar.set(1, j / 100);
		return gregoriancalendar.getTime();
	}

	public static Date decodeDate(String s)
	{
		Date date;
		try
		{
			date = decodeDate(Integer.parseInt(s));
		}
		catch (NumberFormatException numberformatexception)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Date cannot be decoded from argument: ").append(s).toString());
		}
		return date;
	}

	public static Date decodeDateTime(int i, int j, TimeZone timezone)
	{
		GregorianCalendar gregoriancalendar = useCalendarInstance();
		gregoriancalendar.setTimeInMillis(0L);
		if (timezone != null)
			gregoriancalendar.setTimeZone(timezone);
		gregoriancalendar.set(5, i % 100);
		int k = i / 100;
		gregoriancalendar.set(2, -1 + k % 100);
		gregoriancalendar.set(1, k / 100);
		gregoriancalendar.set(11, j / 100);
		gregoriancalendar.set(12, j % 100);
		return gregoriancalendar.getTime();
	}

	public static Date decodeDateTimeLocal(int i, int j)
	{
		return decodeDateTime(i, j, null);
	}

	public static Date decodeDateTimeUT(int i, int j)
	{
		return decodeDateTime(i, j, timeZoneUT);
	}

	public static Date decodeDateUTC(int i, int j, TimeZone timezone)
	{
		int k = i % 100;
		int l = i / 100;
		int i1 = -1 + l % 100;
		int j1 = l / 100;
		int k1 = j / 100;
		int l1 = j % 100;
		Time time = new Time("UTC");
		time.set(0, l1, k1, k, i1, j1);
		time.normalize(false);
		if (timezone != null)
			time.switchTimezone(timezone.getID());
		GregorianCalendar gregoriancalendar = useCalendarInstance();
		gregoriancalendar.set(1, time.year);
		gregoriancalendar.set(2, time.month);
		gregoriancalendar.set(5, time.monthDay);
		return gregoriancalendar.getTime();
	}

	public static Date decodeTime(int i)
	{
		GregorianCalendar gregoriancalendar = useCalendarInstance();
		gregoriancalendar.setTimeInMillis(0L);
		gregoriancalendar.set(11, i / 100);
		gregoriancalendar.set(12, i % 100);
		return gregoriancalendar.getTime();
	}

	private static int encodeCalendar(Calendar calendar)
	{
		return 10000 * calendar.get(1) + 100 * (1 + calendar.get(2)) + calendar.get(5);
	}

	public static int encodeDate(Time time)
	{
		return 10000 * time.year + 100 * (1 + time.month) + time.monthDay;
	}

	public static int encodeDate(Date date)
	{
		GregorianCalendar gregoriancalendar = useCalendarInstance();
		gregoriancalendar.setTime(date);
		return encodeCalendar(gregoriancalendar);
	}

	public static int encodeHourMin(int i, int j)
	{
		int k = j + i * 60;
		int l;
		if (k >= 0)
			l = 100 * (k / 60) + k % 60;
		else
			l = 100 * (k / 60) - -k % 60;
		return l;
	}

	public static int encodeTime(Time time)
	{
		return 100 * time.hour + time.minute;
	}

	private static int encodeTime(Calendar calendar)
	{
		return 100 * calendar.get(11) + calendar.get(12);
	}

	public static int encodeTime(Date date)
	{
		GregorianCalendar gregoriancalendar = useCalendarInstance();
		gregoriancalendar.setTime(date);
		return 100 * gregoriancalendar.get(11) + gregoriancalendar.get(12);
	}

	public static Time fromDateUTC(int i)
	{
		Time time = new Time("UTC");
		time.monthDay = i % 100;
		int j = i / 100;
		time.month = -1 + j % 100;
		time.year = j / 100;
		time.normalize(true);
		return time;
	}

	public static int getDayOfWeek(int i)
	{
		GregorianCalendar gregoriancalendar = useCalendarInstance();
		int j = i % 100;
		int k = i / 100;
		int l = -1 + k % 100;
		gregoriancalendar.set(k / 100, l, j);
		return gregoriancalendar.get(7);
	}

	public static Time getNowAtUTCOffset(int i)
	{
		Time time = new Time();
		time.setToNow();
		long l = time.toMillis(true);
		long l1 = (long)(i * 60) - time.gmtoff;
		int j;
		if (time.isDst != 0)
			j = 3600;
		else
			j = 0;
		time.set(l + 1000L * (l1 + (long)j));
		return time;
	}

	public static int getTimeNowEncoded()
	{
		return encodeTime(GregorianCalendar.getInstance());
	}

	public static int getTodayDateEncoded()
	{
		return encodeCalendar(GregorianCalendar.getInstance());
	}

	public static int idFromDateMillis(int i)
	{
		return encodeDate(new Date(i));
	}

	private static GregorianCalendar useCalendarInstance()
	{
		GregorianCalendar gregoriancalendar = (GregorianCalendar)gregCalendar.get();
		if (gregoriancalendar == null)
		{
			gregoriancalendar = new GregorianCalendar();
			gregCalendar.set(gregoriancalendar);
		}
		gregoriancalendar.clear();
		return gregoriancalendar;
	}

}
