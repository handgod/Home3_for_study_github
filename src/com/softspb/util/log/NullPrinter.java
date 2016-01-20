// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;


// Referenced classes of package com.softspb.util.log:
//			SPBLogPrinter

public class NullPrinter extends SPBLogPrinter
{

	NullPrinter()
	{
		super(null);
	}

	void println(int i, String s)
	{
	}

	void println(int i, String s, long l)
	{
	}

	void setTag(String s)
	{
	}
}
