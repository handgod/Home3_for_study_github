// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import java.util.Collection;
import java.util.Iterator;

// Referenced classes of package com.spb.programlist:
//			TagInfo

class AlarmTagInfo extends TagInfo
{

	static final String NAME = "alarm";
	static final String SPB_TIME_PACKAGE = "com.softspb.time";

	AlarmTagInfo(Collection collection)
	{
		super("alarm", collection);
	}

	ActivityInfo find(Context context)
	{
		ActivityInfo activityinfo;
		activityinfo = null;
		Iterator iterator = activities.iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			ActivityInfo activityinfo1 = (ActivityInfo)iterator.next();
			if ("com.softspb.time".equals(activityinfo1.packageName))
				activityinfo = activityinfo1;
		} while (true);
		if (activityinfo != null)
		{
			boolean flag;
			SharedPreferences sharedpreferences = null;
			try {
				sharedpreferences = context.createPackageContext("com.softspb.time", 0).getSharedPreferences("SPBTime", 1);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String s = android.provider.Settings.System.getString(context.getContentResolver(), "next_alarm_formatted");
			if (sharedpreferences.getString("SPBAlarm", "").equals(s))
			{
				flag = TextUtils.isEmpty(s);
			}
		}
		else 
		{
			activityinfo = super.find(context);
		}
		return activityinfo;

	}
}
