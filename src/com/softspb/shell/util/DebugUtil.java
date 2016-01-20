// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util;

import android.app.ActivityManager;

public class DebugUtil
{

	public DebugUtil()
	{
	}

	public static boolean isMonkey()
	{
		boolean flag = false;
	//	boolean flag1 = ActivityManager.isUserAMonkey();
		//flag = flag1;
		return flag;
	}
}
