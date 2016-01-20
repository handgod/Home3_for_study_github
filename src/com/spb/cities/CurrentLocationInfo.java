// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities;


public class CurrentLocationInfo
{

	protected int cityId;
	protected long lastUpdatedMsUtc;
	protected int positioningStatus;

	public CurrentLocationInfo()
	{
		this(0x80000000, 2, 0L);
	}

	public CurrentLocationInfo(int i, int j, long l)
	{
		cityId = i;
		positioningStatus = j;
		lastUpdatedMsUtc = l;
	}

	public int getCityId()
	{
		return cityId;
	}

	public long getLastUpdatedMsUtc()
	{
		return lastUpdatedMsUtc;
	}

	public int getPositioningStatus()
	{
		return positioningStatus;
	}

	public String toString()
	{
		return (new StringBuilder()).append("CurrentLocationInfo [cityId=").append(cityId).append(" positioningStatus=").append(positioningStatus).append(" lastUpdatedMsUtc=").append(lastUpdatedMsUtc).append("]").toString();
	}
}
