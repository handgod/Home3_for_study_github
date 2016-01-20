// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import java.util.Arrays;
import java.util.Calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.softspb.shell.adapters:
//			TimeAdapter, AdaptersHolder

public class TimeAdapterAndroid extends TimeAdapter
{
	class DateTimeFormatChangeObserver extends ContentObserver
	{

		final TimeAdapterAndroid this$0;

		public void onChange(boolean flag)
		{
			TimeAdapterAndroid.logger.d("DateTimeFormatChangeObserver.onChange");
			TimeAdapterAndroid.onDateTimeFormatChanged(timeAdapterToken);
		}

		public DateTimeFormatChangeObserver(Handler handler)
		{
			super(handler);
			this$0 = TimeAdapterAndroid.this;
		}
	}


	private static final int FORMAT_TIME_COMMON = 2569;
	private static final Logger logger = Loggers.getLogger(TimeAdapterAndroid.class.getName());
	private ContentResolver contentResolver;
	private Context context;
	private DateTimeFormatChangeObserver dateTimeFormatChangeObserver;
	private int timeAdapterToken;

	public TimeAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		logger.d("constructor");
	}

	private static native void onDateTimeFormatChanged(int i);

	private void startListeningDateTimeFormatChanges()
	{
		logger.d("startListeningDateTimeFormatChanges");
		android.net.Uri uri = android.provider.Settings.System.getUriFor("date_format");
		android.net.Uri uri1 = android.provider.Settings.System.getUriFor("time_12_24");
		dateTimeFormatChangeObserver = new DateTimeFormatChangeObserver(new Handler());
		contentResolver.registerContentObserver(uri, true, dateTimeFormatChangeObserver);
		contentResolver.registerContentObserver(uri1, true, dateTimeFormatChangeObserver);
	}

	private void stopListeningDateTimeFormatChanges()
	{
		logger.d("stopListeningDateTimeFormatChanges");
		if (dateTimeFormatChangeObserver != null)
			contentResolver.unregisterContentObserver(dateTimeFormatChangeObserver);
	}

	private void throwException(int i)
	{
		IllegalArgumentException illegalargumentexception = new IllegalArgumentException((new StringBuilder()).append("format ").append(i).append(" is not yet supported!").toString());
		illegalargumentexception.printStackTrace();
		logger.d((new StringBuilder()).append("format ").append(i).append(" is not yet supported!").toString(), illegalargumentexception);
	}

	public String format(long l, int i)
	{
		String s;
		int j;
		int k;
		logger.d((new StringBuilder()).append(">>>format: dateLong=").append(l).append(", nativeFlags=").append(i).toString());
		s = "XXXX";
		j = i & 0xfffffff0;
		k = i & 0xfffff00f;
		
		switch (j) {
			case 0:
				if (k == 5)
					s = DateUtils.formatDateTime(context, l, 2569);
				else
				if (k == 13)
				{
					char c;
					char ac[];
					String s1;
					if (DateFormat.is24HourFormat(context))
						c = 'k';
					else
						c = 'h';
					ac = new char[8];
					ac[0] = c;
					ac[1] = c;
					ac[2] = ':';
					ac[3] = 'm';
					ac[4] = 'm';
					ac[5] = ':';
					ac[6] = 's';
					ac[7] = 's';
					s1 = new String(ac);
					s = DateFormat.format(s1, l).toString();
					if (!DateFormat.is24HourFormat(context))
						s = (new StringBuilder()).append(s).append(" ").append(getAMPMString(l)).toString();
				} else
				{
					throwException(i);
				}		
				break;
			case 64:
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(l);
				s = DateUtils.getMonthString(calendar.get(2), 20);
				break;
			
			case 80:
				int i3 = 0x10008;
				if (k == 5)
					i3 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, i3);		
				break;
			case 128:
				if (k != 0)
					throwException(i);
				char ac4[] = new char[4];
				Arrays.fill(ac4, 'M');
				s = DateFormat.format(new String(ac4), l).toString();
				break;
			case 144:
				int l2 = 8;
				if (k == 5)
					l2 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, l2);
				break;
			case 256:
				if (k != 0)
					throwException(i);
				char ac1[] = new char[2];
				Arrays.fill(ac1, 'E');
				String s2 = new String(ac1);
				s = DateFormat.format(s2, l).toString();
				break;
			case 336:
				int i1 = 0x8001a;
				if (k == 5)
					i1 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, i1);
				break;
			case 512:
				if (k != 0)
					throwException(i);
				char ac2[] = new char[4];
				Arrays.fill(ac2, 'E');
				s = DateFormat.format(new String(ac2), l).toString();
				break;
			case 560:
				int k2 = 0x2001a;
				if (k == 5)
					k2 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, k2);
				break;
			case 592:
				int j2 = 0x1001a;
				if (k == 5)
					j2 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, j2);
				break;
			case 1072:
				int k1 = 0x20004;
				if (k == 5)
					k1 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, k1);
				break;
			case 2048:
				if (k != 0)
					throwException(i);
				char ac3[] = new char[4];
				Arrays.fill(ac3, 'y');
				String s3 = new String(ac3);
				s = DateFormat.format(s3, l).toString();
				break;
			case 2096:
				int k22 = 0x20004;
				if (k == 5)
					k22 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, k22);
				break;

			case 2128:
				int l1 = 0x10004;
				if (k == 5)
					l1 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, l1);
				break;
			case 2192:
				int i2 = 4;
				if (k == 5)
					i2 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, i2);
				break;
			case 2704:
				int j1 = 22;
				if (k == 5)
					j1 |= 0xa09;
				s = DateUtils.formatDateTime(context, l, j1);
				break;

		default:
			throwException(i);
		}
		return s;
	}

	public String getAMPMString(long l)
	{
		logger.d((new StringBuilder()).append(">>>getAMPMString: time=").append(l).toString());
		String s;
		if (DateFormat.is24HourFormat(context))
		{
			s = "";
		} else
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(l);
			s = DateUtils.getAMPMString(calendar.get(9));
		}
		logger.d((new StringBuilder()).append("<<<getAMPMString: result=").append(s).toString());
		return s;
	}

	public int getFirstDayOfWeek()
	{
		logger.d(">>>getFirstDayOfWeek");
		byte byte0;
		if (Calendar.getInstance().getFirstDayOfWeek() == 2)
			byte0 = 2;
		else
			byte0 = 1;
		logger.d((new StringBuilder()).append("<<<getFirstDayOfWeek: result=").append(byte0).toString());
		return byte0;
	}

	protected void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		logger.d((new StringBuilder()).append(">>>onCreate: context=").append(context1).toString());
		context = context1;
		contentResolver = context1.getContentResolver();
		startListeningDateTimeFormatChanges();
		super.onCreate(context1, nativecallbacks);
		logger.d("<<<onCreate");
	}

	protected void onDestroy(Context context1)
	{
		logger.d((new StringBuilder()).append(">>>onDestroy: context=").append(context1).toString());
		stopListeningDateTimeFormatChanges();
		logger.d("<<<onDestroy");
	}

	protected void onStart(int i)
	{
		logger.d((new StringBuilder()).append("onStart: adapterToken=").append(Integer.toHexString(i)).toString());
		timeAdapterToken = i;
	}




}
