// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.softspb.util.log:
//			SPBLogPrinter, SynchronizedFileAppender

class LogFilePrinter extends SPBLogPrinter
{

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
	private boolean isClosed;
	private final SynchronizedFileAppender out;

	LogFilePrinter(String s, SynchronizedFileAppender synchronizedfileappender)
	{
		super(s);
		isClosed = false;
		out = synchronizedfileappender;
	}

	void close()
	{
		isClosed = true;
	}

	void flush()
	{
		if (!isClosed)
			out.flush();
	}

	SynchronizedFileAppender getOut()
	{
		return out;
	}

	public void println(int i, String s, long l)
	{
		if (!isClosed) 
		{
			StringBuilder stringbuilder;
			stringbuilder = new StringBuilder();
			stringbuilder.append(dateFormat.format(new Date(l)));
			switch (i) {
			case 2:
				stringbuilder.append(": VERBOSE/");
				break;
			case 3:
				stringbuilder.append(": DEBUG/");
				break;
			case 4:
				stringbuilder.append(": INFO/");
				break;
			case 5:
				
				break;
			case 6:
				
				break;
		

			default:
				stringbuilder.append(": ANY/");
				break;
			}
			stringbuilder.append(tag).append(": ").append(s);
			out.println(stringbuilder.toString());
		}
		else 
		{
			return;
		}
	}

}
