// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;


abstract class SPBLogPrinter
{

	String tag;

	SPBLogPrinter(String s)
	{
		tag = s;
	}

	void close()
	{
	}

	void flush()
	{
	}

	void println(int i, String s)
	{
		println(i, s, System.currentTimeMillis());
	}

	abstract void println(int i, String s, long l);

	void setTag(String s)
	{
		tag = s;
	}
}
