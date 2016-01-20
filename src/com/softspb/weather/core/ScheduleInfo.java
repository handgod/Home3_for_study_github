// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import java.util.List;

public class ScheduleInfo
{

	List scheduledIds;
	long scheduledInterval;
	long scheduledTimestampToken;

	public ScheduleInfo()
	{
	}

	public String toString()
	{
		return (new StringBuilder()).append("ScheduleInfo[timestamp=").append(scheduledTimestampToken).append(" ids=").append(scheduledIds).append((new StringBuilder()).append(" interval=").append(scheduledInterval).toString()).toString();
	}
}
