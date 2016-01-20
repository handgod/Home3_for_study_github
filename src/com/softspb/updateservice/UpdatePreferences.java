// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.updateservice;


public interface UpdatePreferences
{

	public static final String PREFERENCE_ONLY_WIFI = "use-only-wifi";
	public static final String PREFERENCE_UPDATE_INTERVAL = "update-interval";

	public abstract long getUpdateIntervalMs();

	public abstract boolean isUseOnlyWifi();

	public abstract void setUpdateIntervalMs(long l);

	public abstract void setUseOnlyWifi(boolean flag);
}
