package com.softspb.shell.adapters.alarms;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.text.format.DateUtils;
import com.softspb.shell.Home;
import com.softspb.shell.Home.PauseResumeListener;
import com.softspb.util.log.Logger;
import java.util.GregorianCalendar;

public class AlarmsAdapterAndroid2 extends BaseAlarmsAdapter
  implements Home.PauseResumeListener
{
  private String KEY_ALARM_REC = "key_alarm_rec";
  private String KEY_ALARM_TIME = "key_alarm_time";
  long nextAlarm = 0L;

  public AlarmsAdapterAndroid2(int paramInt, Context paramContext)
  {
    super(paramInt, paramContext);
  }

	private static long parseDate(String s)
	{
		String as[];
		boolean flag;
		boolean flag1;
		String s3;
		as = TextUtils.split(s, "[ ]+");
		flag = false;
		flag1 = false;
		String s1 = DateUtils.getAMPMString(0);
		String s2 = DateUtils.getAMPMString(1);
		s3 = null;
		long l;
		int k = 0;
		int i1 = 0;
		int j1 = 0;
		int i = as.length;
		for (int j = 0; j < i; j++)
		{
			String s5 = as[j];
			if (s5.contains(":"))
				s3 = s5;
			if (s1.equalsIgnoreCase(s5) | s2.equalsIgnoreCase(s5))
			{
				flag = true;
				flag1 = s1.equalsIgnoreCase(s5);
			}
		}

		if (s3 == null) 
		{
			l = 0L;	
		}
		else
		{
			
			String as1[] = TextUtils.split(s3, ":");
			if (as1.length != 2)
			{
				l = 0L;
	
			}
			try
			{
				k = (new Integer(as1[0])).intValue();
				i1 = (new Integer(as1[1])).intValue();
			}
			catch (Exception exception)
			{
				l = 0L;

			}
			if (i1 > 59 || k > 23 || flag && k > 13)
			{
				l = 0L;

			}
			String as2[] = new String[7];
			as2[0] = DateUtils.getDayOfWeekString(2, 20);
			as2[1] = DateUtils.getDayOfWeekString(3, 20);
			as2[2] = DateUtils.getDayOfWeekString(4, 20);
			as2[3] = DateUtils.getDayOfWeekString(5, 20);
			as2[4] = DateUtils.getDayOfWeekString(6, 20);
			as2[5] = DateUtils.getDayOfWeekString(7, 20);
			as2[6] = DateUtils.getDayOfWeekString(1, 20);
			j1 = -1;
			int k1 = as.length;
			int l1 = 0;
			while(l1 < k1)
			{
				String s4 = as[l1];
				int j2 = 0;
			
				while(!s4.equals(as2[j2]))
				{
					{
						if (j2 < as2.length)
						{
							j1 = j2;
						}
						l1++;
					}
					if (true)
						break;
					j2++;
				}
			}
		} 
			
			if (j1 == -1)
			{
				l = 0L;
			} else
			{
				if (flag)
				{
					int i2;
					if (flag1)
						i2 = 0;
					else
						i2 = 12;
					if (k == 12)
						i2 += 12;
					k = (k + i2) % 24;
				}
				l = findAlarmTime(k, i1, j1, new GregorianCalendar());
			}
		

		return l;
	}


	private void saveAlarmsInPreferences(String s, long l)
	{
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_ALARM_REC, s).putLong(KEY_ALARM_TIME, l).commit();
	}

  	private long tryToGetSavedValue(String s)
	{
		long l;
		SharedPreferences sharedpreferences;
		l = 0L;
		sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (sharedpreferences.contains(KEY_ALARM_REC) && s.equals(sharedpreferences.getString(KEY_ALARM_REC, null)))
			l = sharedpreferences.getLong(KEY_ALARM_TIME, l);
		return l;
	}

  public void onPause()
  {
  }

  public void onResume()
  {
		reload(false);
  }

  public void onStart()
  {
    super.onStart();
    ((Home)this.context).setOnPauseResumeListener(this);
		reload(false);
  }

  public void onStop()
  {
    super.onStop();
    ((Home)this.context).removeOnPauseResumeListener(this);
  }

 	public void reload(boolean flag)
	{
		String s = android.provider.Settings.System.getString(contentResolver, "next_alarm_formatted");
		long l = 0L;
		if (!TextUtils.isEmpty(s))
		{
			l = parseDate(s);
			if (l == 0x7fffffffffffffffL)
				l = 0L;
			if (l == 0L)
				l = tryToGetSavedValue(s);
		}
		if (l != nextAlarm)
		{
			nextAlarm = l;
			saveAlarmsInPreferences(s, nextAlarm);
			logger.d((new StringBuilder()).append("set new alarm at ").append(nextAlarm).toString());
			setNativeNextAlarm(nextAlarm);
		}
	}
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.alarms.AlarmsAdapterAndroid2
 * JD-Core Version:    0.6.0
 */