// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;

import java.util.concurrent.locks.ReentrantLock;

// Referenced classes of package com.softspb.util.log:
//			Loggers, Logger

class DebuggingReentrantLock extends ReentrantLock
{

	static Logger logger = Loggers.getLogger(DebuggingReentrantLock.class.getName());
	private String id;

	DebuggingReentrantLock(String s)
	{
		id = s;
	}

	private void logTrace()
	{
		Exception exception = new Exception();
		exception.fillInStackTrace();
		StackTraceElement astacktraceelement[] = exception.getStackTrace();
		for (int i = 2; i < astacktraceelement.length; i++)
			logger.d(astacktraceelement[i].toString());

	}

	public void lock()
	{
		logTrace();
		Thread thread = getOwner();
		Logger logger1 = logger;
		StringBuilder stringbuilder = (new StringBuilder()).append("Lock(").append(id).append(") owned by ");
		String s;
		Logger logger2;
		StringBuilder stringbuilder1;
		String s1;
		if (thread == null)
			s = "null";
		else
			s = (new StringBuilder()).append("thread #").append(thread.getId()).append("(").append(thread.getName()).append(")").toString();
		logger1.d(stringbuilder.append(s).append(" : thread #").append(Thread.currentThread().getId()).append("(").append(Thread.currentThread().getName()).append(")").append(" is entering the lock, count=").append(getHoldCount()).toString());
		super.lock();
		logger2 = logger;
		stringbuilder1 = (new StringBuilder()).append("Lock(").append(id).append(") owned by ");
		if (thread == null)
			s1 = "null";
		else
			s1 = (new StringBuilder()).append("thread #").append(thread.getId()).append("(").append(thread.getName()).append(")").toString();
		logger2.d(stringbuilder1.append(s1).append(" : thread #").append(Thread.currentThread().getId()).append(" has entered the lock, count=").append(getHoldCount()).toString());
	}

	public void unlock()
	{
		Thread thread = getOwner();
		Logger logger1 = logger;
		StringBuilder stringbuilder = (new StringBuilder()).append("Lock(").append(id).append(") owned by ");
		String s;
		Logger logger2;
		StringBuilder stringbuilder1;
		String s1;
		if (thread == null)
			s = "null";
		else
			s = (new StringBuilder()).append("thread #").append(thread.getId()).append("(").append(thread.getName()).append(")").toString();
		logger1.d(stringbuilder.append(s).append(" : thread #").append(Thread.currentThread().getId()).append("(").append(Thread.currentThread().getName()).append(")").append(" is about to release the lock, count=").append(getHoldCount()).toString());
		super.unlock();
		logger2 = logger;
		stringbuilder1 = (new StringBuilder()).append("Lock(").append(id).append(") owned by ");
		if (thread == null)
			s1 = "null";
		else
			s1 = (new StringBuilder()).append("thread #").append(thread.getId()).append("(").append(thread.getName()).append(")").toString();
		logger2.d(stringbuilder1.append(s1).append(" : thread #").append(Thread.currentThread().getId()).append("(").append(Thread.currentThread().getName()).append(")").append(" has released the lock, count=").append(getHoldCount()).toString());
	}

}
