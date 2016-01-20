// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm;


public interface DataProvider
{

	public abstract byte[] getBlob(String s);

	public abstract double getDouble(String s);

	public abstract float getFloat(String s);

	public abstract int getInt(String s);

	public abstract long getLong(String s);

	public abstract short getShort(String s);

	public abstract String getText(String s);
}
