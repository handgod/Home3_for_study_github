// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;

import android.R.bool;
import android.content.Context;
import android.content.Intent;
import android.content.pm.*;
import android.text.TextUtils;

// Referenced classes of package com.spb.programlist:
//			IntentPattern, ProgramInfo

public class ProgramsUtil
{

	public ProgramsUtil()
	{
	}

	static ProgramInfo addFromActivity(Context context, PackageManager packagemanager, ActivityInfo activityinfo, String s, boolean flag)
	{
		String s1 = activityinfo.packageName;
		boolean flag1;
		if (!IntentPattern.DUMMY_PACKAGE_NAME.equals(s1))
			flag1 = true;
		else
			flag1 = false;
		return addFromActivity(context, packagemanager, activityinfo, s, flag, flag1);
	}

	static ProgramInfo addFromActivity(Context context, PackageManager packagemanager, ActivityInfo activityinfo, String s, boolean flag, boolean flag1)
	{
		ProgramInfo programinfo;
		if (activityinfo != null)
		{
			int i = activityinfo.icon;
			String s1 = activityinfo.packageName;
			if (i == 0)
				i = activityinfo.applicationInfo.icon;
			String s2 = String.valueOf(activityinfo.loadLabel(packagemanager));
			String s3 = activityinfo.name;
			String s4;
			boolean flag2;
			if (i != 0 && packagemanager.getDrawable(s1, i, activityinfo.applicationInfo) != null)
			{
				Object aobj1[] = new Object[3];
				aobj1[0] = "android.resource";
				aobj1[1] = activityinfo.applicationInfo.packageName;
				aobj1[2] = Integer.valueOf(i);
				s4 = String.format("%s://%s/%d", aobj1);
			} else
			{
				Object aobj[] = new Object[3];
				aobj[0] = "android.resource";
				aobj[1] = context.getPackageName();
				aobj[2] = Integer.valueOf(R.drawable.dummy_icon);
				s4 = String.format("%s://%s/%d", aobj);
			}
			if (!isSystem(activityinfo) && !TextUtils.equals(context.getPackageName(), s1))
				flag2 = true;
			else
				flag2 = false;
			programinfo = new ProgramInfo(s1, s3, s2, s4, s, flag1, flag, flag2);
		} else
		{
			programinfo = null;
		}
		return programinfo;
	}

	public static boolean isSystem(ActivityInfo activityinfo)
	{
		int i = 1;
		int j = 0;
		boolean flag;
		if (activityinfo != null)
		{
			if ((1 & activityinfo.applicationInfo.flags) != i)
				i = 0;
			j = i;
		}
		if(j == 1)
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		return flag;
	}

	public static void startActivitySafely(Context context, Intent intent)
	{
		context.startActivity(intent);

	}
}
