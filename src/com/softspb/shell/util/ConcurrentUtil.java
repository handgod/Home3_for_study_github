// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util;

import android.os.Handler;
import android.os.Looper;
import com.softspb.util.Conditions;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.concurrent.*;

public class ConcurrentUtil
{
	private static class ResultHolder
	{

		Object result;

		Object get()
		{
			return result;
		}

		void set(Object obj)
		{
			result = obj;
		}

		private ResultHolder()
		{
			result = null;
		}

	}


	private static Logger logger = Loggers.getLogger(ConcurrentUtil.class.getName());

	private ConcurrentUtil()
	{
	}

	public static void completeAction(final Runnable action, long l)
	{
		final CountDownLatch screenLatch;
		Conditions.checkNotNull(action);
		screenLatch = new CountDownLatch(1);
		getHandler().post(new Runnable() {

			final Runnable val$action;
			final CountDownLatch val$screenLatch;

			public void run()
			{
				action.run();
				ConcurrentUtil.latchDownInUIThread(screenLatch);		
			}

			
			{
				val$action =action;
				val$screenLatch = screenLatch ;
//				super();
			}
		});
		try {
			screenLatch.await(l, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Handler getHandler()
	{
		return new Handler(Looper.getMainLooper());
	}

	private static void latchDownInUIThread(final CountDownLatch latch)
	{
		getHandler().post(new Runnable() {

			final CountDownLatch val$latch;

			public void run()
			{
				ConcurrentUtil.logger.i("->latch.countDown");
				latch.countDown();
				ConcurrentUtil.logger.i("<-latch.countDown");
			}

			
			{
				val$latch = latch;
//				super();
			}
		});
	}

	public static Object runSynchronouslyInUiThread(final Callable task, long l)
	{
		Conditions.checkNotNull(task);
		final ResultHolder holder = new ResultHolder();
		final CountDownLatch latch = new CountDownLatch(1);
		getHandler().post(new Runnable() {

			final ResultHolder val$holder;
			final CountDownLatch val$latch;
			final Callable val$task;

			public void run()
			{
				try {
					holder.set(task.call());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				latch.countDown();
			}

			
			{
				val$holder = holder;
				val$task = task ;
				val$latch =latch;
//				super();
			}
		});
		try
		{
			latch.await(l, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException interruptedexception)
		{
			interruptedexception.printStackTrace();
		}
		return holder.get();
	}

	public static void runSynchronouslyInUiThread(final Runnable runnable, long l)
	{
		final CountDownLatch latch;
		Conditions.checkNotNull(runnable);
		latch = new CountDownLatch(1);
		getHandler().post(new Runnable() {

			final CountDownLatch val$latch;
			final Runnable val$runnable;

			public void run()
			{
				runnable.run();
				latch.countDown();
			}

			
			{
				val$runnable = runnable;
				val$latch = latch ;
//				super();
			}
		});
		try {
			latch.await(l, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
