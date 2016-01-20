// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;

import android.util.Log;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Referenced classes of package com.softspb.util.log:
//			SPBLogPrinter, Loggers

public class Logger
{

	private volatile boolean isEnabled;
	private volatile boolean isThreadLoggingEnabled;
	private final List logPrinters;
	private final String name;

	Logger(String s, int j)
	{
		logPrinters = new CopyOnWriteArrayList();
		isThreadLoggingEnabled = false;
		isEnabled = false;
		name = s;
	}

	Logger(String s, int j, SPBLogPrinter spblogprinter)
	{
		this(s, j);
		logPrinters.add(spblogprinter);
	}

	private final String prepareMessage(String s)
	{
		Thread thread = Thread.currentThread();
		if (isThreadLoggingEnabled)
			s = (new StringBuilder()).append('[').append(thread.getName()).append(", tid=").append(thread.getId()).append("] - ").append(s).toString();
		return s;
	}

	private final String prepareMessage(String s, Throwable throwable)
	{
		return (new StringBuilder()).append(prepareMessage(s)).append('\n').append(Log.getStackTraceString(throwable)).toString();
	}

	private void println(int j, String s)
	{
		for (Iterator iterator = logPrinters.iterator(); iterator.hasNext(); ((SPBLogPrinter)iterator.next()).println(j, s));
	}

	void addLogPrinter(SPBLogPrinter spblogprinter)
	{
		logPrinters.add(spblogprinter);
		Loggers.logger.d((new StringBuilder()).append("Logger \"").append(name).append("\" is starting to log here with tag=").append(spblogprinter.tag).toString());
	}

	public void d(String s)
	{
		if (isEnabled)
			println(3, prepareMessage(s));
	}

	public void d(String s, Throwable throwable)
	{
		if (isEnabled)
			println(3, prepareMessage(s, throwable));
	}

	public  void d(String s, Object aobj[])
	{
		if (isEnabled)
			println(3, prepareMessage(String.format(s, aobj)));
	}

	public void disable()
	{
		isEnabled = false;
	}

	public void disableThreadLog()
	{
		isThreadLoggingEnabled = false;
	}

	public void e(String s)
	{
		if (isEnabled)
			println(6, prepareMessage(s));
	}

	public void e(String s, Throwable throwable)
	{
		if (isEnabled)
			println(6, prepareMessage(s, throwable));
	}

	public void enable()
	{
		isEnabled = true;
	}

	public void enableThreadLog()
	{
		isThreadLoggingEnabled = true;
	}

	public String getName()
	{
		return name;
	}

	public void i(String s)
	{
		if (isEnabled)
			println(4, prepareMessage(s));
	}

	public void i(String s, Throwable throwable)
	{
		if (isEnabled)
			println(4, prepareMessage(s, throwable));
	}

	public  void i(String s, Object aobj[])
	{
		if (isEnabled)
			println(4, prepareMessage(String.format(s, aobj)));
	}

	public void v(String s)
	{
		if (isEnabled)
			println(2, prepareMessage(s));
	}

	public void v(String s, Throwable throwable)
	{
		if (isEnabled)
			println(2, prepareMessage(s, throwable));
	}

	public void w(String s)
	{
		if (isEnabled)
			println(5, prepareMessage(s));
	}

	public void w(String s, Throwable throwable)
	{
		if (isEnabled)
			println(5, prepareMessage(s, throwable));
	}

	public  void w(String s, Object aobj[])
	{
		if (isEnabled)
			println(5, prepareMessage(String.format(s, aobj)));
	}
}
