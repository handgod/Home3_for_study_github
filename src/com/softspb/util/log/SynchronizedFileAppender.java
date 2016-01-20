// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;

import android.util.Log;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

// Referenced classes of package com.softspb.util.log:
//			Loggers, Logger

class SynchronizedFileAppender
	implements Runnable
{

	private static final long MAX_FLUSH_INTERVAL_MS = 10000L;
	private final File file;
	private boolean isClosed;
	private boolean isEnabled;
	private int lastAddedLineNumber;
	private int lineCount;
	private final ConcurrentLinkedQueue logQueue = new ConcurrentLinkedQueue();
	private final Object monitor = new Object();
	private final String name;
	int usersCount;
	private Thread workerThread;

	SynchronizedFileAppender(File file1, String s)
	{
		isClosed = false;
		isEnabled = true;
		lineCount = 0;
		lastAddedLineNumber = -1;
		usersCount = 0;
		name = s;
		file = file1;
		workerThread = new Thread(this, s);
		workerThread.start();
	}

	void close()
	{
		Loggers.logger.d((new StringBuilder()).append("Closing file appender: ").append(file.getPath()).toString());
		isClosed = true;
		
		synchronized (monitor) {
			monitor.notifyAll();
		}	
		return;
	}

	void disable()
	{
		Loggers.logger.d((new StringBuilder()).append("Disabling file appender: ").append(file.getPath()).toString());
		isEnabled = false;
	
		synchronized (monitor) {
			monitor.notify();
		}
	
		return;
	}

	void enable()
	{
		Loggers.logger.d((new StringBuilder()).append("Enabling file appender: ").append(file.getPath()).toString());
		isEnabled = true;
		println((new StringBuilder()).append("File appender was re-enabled: ").append(-1 + (lineCount - lastAddedLineNumber)).append(" lines were skipped").toString());
		Object obj = monitor;
		synchronized (monitor) {
			monitor.notify();
		}
		return;
	}

	void flush()
	{
		String s;
		PrintWriter printwriter;
		s = (String)logQueue.poll();
		if (s == null)
			return;
		printwriter = null;
		PrintWriter printwriter1 = null;
		try {
			printwriter1 = new PrintWriter(new FileWriter(file, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		printwriter1.println(s);
		s = (String)logQueue.poll();
		if (printwriter1 != null)
			printwriter1.close();
		printwriter = printwriter1;
		
		isEnabled = false;
		if (printwriter != null)
			printwriter.close();
	}

	File getFile()
	{
		return file;
	}

	String getName()
	{
		return name;
	}

	void println(String s)
	{
		lineCount = 1 + lineCount;
		if (isClosed || !isEnabled)
			return;
		logQueue.add(s);
		lastAddedLineNumber = -1 + lineCount;
		Object obj = monitor;
		synchronized (monitor) {
			monitor.notify();
		}
	}

	public void run()
	{
		if (!isClosed)
		{
			if (isEnabled)
				flush();
			Object obj = monitor;
			synchronized (monitor) {
				try {
					monitor.wait(10000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
