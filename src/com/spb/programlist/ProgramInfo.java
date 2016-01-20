// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;


public final class ProgramInfo
{

	public final String activityClassName;
	public final String activityIconResource;
	public final String activityLabel;
	public final boolean addToPrograms;
	public final boolean isDefault;
	public final boolean isUninstallable;
	public final String packageName;
	public final String widgetTag;

	ProgramInfo(String s, String s1, String s2, String s3, String s4, boolean flag, boolean flag1, 
			boolean flag2)
	{
		packageName = s;
		activityClassName = s1;
		activityLabel = s2;
		activityIconResource = s3;
		widgetTag = s4;
		addToPrograms = flag;
		isDefault = flag1;
		isUninstallable = flag2;
	}
}
