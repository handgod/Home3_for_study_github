// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;


// Referenced classes of package com.softspb.util.log:
//			SPBLogPrinter

class MultiLogPrinter extends SPBLogPrinter
{

	SPBLogPrinter printers[];

	 MultiLogPrinter(String s, SPBLogPrinter aspblogprinter[])
	{
		super(s);
		printers = aspblogprinter;
	}

	void println(int i, String s, long l)
	{
		SPBLogPrinter aspblogprinter[] = printers;
		int j = aspblogprinter.length;
		for (int k = 0; k < j; k++)
			aspblogprinter[k].println(i, s, l);

	}

	void setTag(String s)
	{
		super.setTag(s);
		SPBLogPrinter aspblogprinter[] = printers;
		int i = aspblogprinter.length;
		for (int j = 0; j < i; j++)
			aspblogprinter[j].setTag(s);

	}
}
