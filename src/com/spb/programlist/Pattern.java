// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

abstract class Pattern
{

	Pattern()
	{
	}

	abstract ActivityInfo getActivityInfo(PackageManager packagemanager);

	abstract boolean isAntiPattern();

	abstract boolean isDefault(PackageManager packagemanager, ActivityInfo activityinfo);

	abstract boolean match(PackageManager packagemanager, ActivityInfo activityinfo);
}
