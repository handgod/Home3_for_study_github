// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Referenced classes of package com.softspb.util.log:
//			Logger, SystemLogPrinter, SDCardReceiver, SynchronizedFileAppender, 
//			LogFilePrinter

public final class Loggers
{
	static class LogSDCardListener
		implements SDCardReceiver.SDCardListener
	{

		public void onReceive(Intent intent)
		{
			String s;
			Uri uri;
			s = intent.getAction();
			uri = intent.getData();
			if (uri == null || !"file".equals(uri.getScheme()) || !Loggers.externalStoragePath.equals(uri.getPath()))
				{
				return;
				}
			else 
				{
				if (!"android.intent.action.MEDIA_MOUNTED".equals(s))
					{
					if ("android.intent.action.MEDIA_UNMOUNTED".equals(s))
						Loggers.onSDCardUnmounted();
					
					}
				else 
					{
					Loggers.onSDCardMounted();
					}
				}
			return;
		}

		LogSDCardListener()
		{
		}
	}


	private static Context context;
	static final String externalStoragePath = Environment.getExternalStorageDirectory().getPath();
	private static ConcurrentHashMap fileAppendersMap = new ConcurrentHashMap();
	static boolean globalEnabled = true;
	static final Logger logger;
	private static ConcurrentHashMap loggersMap = new ConcurrentHashMap();
	static final LogSDCardListener sdCardListener;
	private static File spbLogsDir;

	private Loggers()
	{
	}

	private static void addLogFilePrinter(Logger logger1)
	{
		String s = logger1.getName();
		String s1 = s.substring(1 + s.lastIndexOf('.'));
		SynchronizedFileAppender synchronizedfileappender = getFileAppender(s1);
		if (globalEnabled)
			synchronizedfileappender.enable();
		else
			synchronizedfileappender.disable();
		logger1.addLogPrinter(new LogFilePrinter(s1, synchronizedfileappender));
	}

	private static Logger createLogger(String s)
	{
		logger.d((new StringBuilder()).append("createLogger >>> name=").append(s).toString());
		Logger logger1 = new Logger(s, 7, new SystemLogPrinter(s.substring(1 + s.lastIndexOf('.'))));
		if (spbLogsDir != null)
			addLogFilePrinter(logger1);
		if (globalEnabled)
		{
			logger1.d((new StringBuilder()).append("createLogger: enabling logger: ").append(s).toString());
			logger1.enable();
		} else
		{
			logger1.d((new StringBuilder()).append("createLogger: disabling logger: ").append(s).toString());
			logger1.disable();
		}
		logger.d((new StringBuilder()).append("createLogger <<< name=").append(s).toString());
		return logger1;
	}

	private static void disableFileAppenders()
	{
		logger.d("disableFileAppenders >>>");
		ConcurrentHashMap concurrenthashmap = fileAppendersMap;
		synchronized (concurrenthashmap) {
			SynchronizedFileAppender synchronizedfileappender;
			for (Iterator iterator = fileAppendersMap.values().iterator(); iterator.hasNext(); synchronizedfileappender.disable())
			{
				synchronizedfileappender = (SynchronizedFileAppender)iterator.next();
				logger.d((new StringBuilder()).append("disableFileAppenders: disabling file appender: ").append(synchronizedfileappender.getFile().getPath()).toString());
				synchronizedfileappender.println("Stopping printing because SD card was removed");
			}
		}
		logger.d("disableFileAppenders <<<");
		return;
	}

	public static void disableGlobal()
	{
		if (globalEnabled)
			globalEnabled = false;
		disableFileAppenders();
		disableLoggers();
	}

	private static void disableLoggers()
	{
		logger.d("disableLoggers >>>");
		Logger logger1;
		ConcurrentHashMap concurrenthashmap = loggersMap;
		synchronized (concurrenthashmap) {
			for (Iterator iterator = loggersMap.values().iterator(); iterator.hasNext(); logger1.disable())
			{
				logger1 = (Logger)iterator.next();
				logger.d((new StringBuilder()).append("disableLoggers: disabling logger: ").append(logger1.getName()).toString());
			}
		}
		
		logger.d("disableLoggers <<<");
		return;
	}

	private static void enableFileAppenders()
	{
		logger.d("enableFileAppenders >>>");
		ConcurrentHashMap concurrenthashmap = fileAppendersMap;
		synchronized (concurrenthashmap) {
			SynchronizedFileAppender synchronizedfileappender;
			for (Iterator iterator = fileAppendersMap.values().iterator(); iterator.hasNext(); synchronizedfileappender.println("Re-Starting printing after SD card was mounted"))
			{
				synchronizedfileappender = (SynchronizedFileAppender)iterator.next();
				logger.d((new StringBuilder()).append("enableFileAppenders: enabling file appender: ").append(synchronizedfileappender.getFile().getPath()).toString());
				synchronizedfileappender.enable();
			}
		}
		logger.d("enableFileAppenders <<<");
		return;
	}

	public static void enableGlobal()
	{
		logger.d("enableGlobal >>>");
		if (!globalEnabled)
			globalEnabled = true;
		enableFileAppenders();
		enableLoggers();
		logger.d("enableGlobal <<<");
	}

	private static void enableLoggers()
	{
		logger.d("enableLoggers >>>");
		Logger logger1;
		ConcurrentHashMap concurrenthashmap = loggersMap;
		synchronized (concurrenthashmap) {
			for (Iterator iterator = loggersMap.values().iterator(); iterator.hasNext(); logger1.enable())
			{
				logger1 = (Logger)iterator.next();
				logger.d((new StringBuilder()).append("enableLoggers: enabling logger: ").append(logger1.getName()).toString());
			}
		}
		logger.d("enableLoggers <<<");
		return;
	}

	private static File getExternalFilesDir(Context context1)
	{
		Class class1 = context1.getClass();
		File file = null;
		try
		{
			Class aclass[] = new Class[1];
			aclass[0] = String.class;
			Method method = class1.getMethod("getExternalFilesDir", aclass);
			String as[] = new String[1];
			as[0] = null;
			file = (File)method.invoke(context1, as);
		}
		catch (Exception exception) { }
		if (file == null)
		{
			file = Environment.getExternalStorageDirectory();
			if (file != null)
				file = new File(file, (new StringBuilder()).append("Android/data/").append(context1.getPackageName()).append("/files").toString());
		}
		return file;
	}

	private static SynchronizedFileAppender getFileAppender(String s)
	{
		SynchronizedFileAppender synchronizedfileappender;
		logger.d((new StringBuilder()).append("getFileAppender >>> tag=").append(s).toString());
		synchronizedfileappender = (SynchronizedFileAppender)fileAppendersMap.get(s);
		if (synchronizedfileappender != null)
		{
			logger.d((new StringBuilder()).append("getFileAppender <<< tag=").append(s).toString());
			return synchronizedfileappender;
		}
		else 
		{
	
			SynchronizedFileAppender synchronizedfileappender1;
			File file = new File(spbLogsDir, (new StringBuilder()).append(s).append(".log").toString());
			logger.d((new StringBuilder()).append("Creating file appender for file: ").append(file.getPath()).toString());
			synchronizedfileappender1 = new SynchronizedFileAppender(file, s);
			fileAppendersMap.put(s, synchronizedfileappender1);
			if (spbLogsDir.exists() || spbLogsDir.mkdirs()) 
			{
				logger.d((new StringBuilder()).append("Directory ok: ").append(spbLogsDir.getPath()).toString());
				synchronizedfileappender = synchronizedfileappender1;
			}
			else 
			{
				logger.e((new StringBuilder()).append("Failed to create directory: ").append(spbLogsDir.getPath()).toString());
				synchronizedfileappender = synchronizedfileappender1;
			}

		}
		return synchronizedfileappender;
	}

	public static Logger getLogger(Class class1)
	{
		if (class1 == null)
			throw new IllegalArgumentException();
		else
			return getLogger(class1.getName());
	}

	public static Logger getLogger(String s)
	{
		Logger logger1;
		logger.d((new StringBuilder()).append("getLogger >>> name=").append(s).toString());
		logger1 = (Logger)loggersMap.get(s);
		if (logger1 != null) 
		{
			logger.d((new StringBuilder()).append("getLogger <<< name=").append(s).toString());
			return logger1;
		}
		else 
		{
			if (logger1 == null)
			{
				logger1 = createLogger(s);
				loggersMap.put(s, logger1);	
			}
		}
		return logger1;
	}

	public static String[] getRegisteredLoggerNames()
	{
		Set set = loggersMap.keySet();
		return (String[])set.toArray(new String[set.size()]);
	}

	private static void initLogsDirectory(Context context1)
	{
		File file = getExternalFilesDir(context1);
		String s = context1.getString(R.string.spb_logs_directory_pathname);
		if (file != null)
			spbLogsDir = new File(file, s);
		else
			spbLogsDir = new File(externalStoragePath, s);
	}

	static void onSDCardMounted()
	{
		logger.d("onSDCardMounted >>>");
		if (globalEnabled)
			enableFileAppenders();
		logger.d("onSDCardMounted <<<");
	}

	static void onSDCardUnmounted()
	{
		logger.d("onSDCardUnmounted >>>");
		disableFileAppenders();
		logger.d("onSDCardUnmounted <<<");
	}

	public static void setContext(Context context1)
	{
		logger.d((new StringBuilder()).append("setContext >>> context=").append(context1).append(" globalEnabled=").append(globalEnabled).toString());
		if (context1.getResources().getBoolean(R.bool.logging_enabled))
			enableGlobal();
		else
			disableGlobal();
		context = context1;
		initLogsDirectory(context1);
		startLoggingToFiles();
		if (globalEnabled)
			enableFileAppenders();
		logger.d("setContext <<<");
	}

	private static void startLoggingToFiles()
	{
		logger.d("startLoggingToFiles >>>");
		for (Iterator iterator = loggersMap.entrySet().iterator(); iterator.hasNext(); addLogFilePrinter((Logger)((java.util.Map.Entry)iterator.next()).getValue()));
		logger.d("startLoggingToFiles <<<");
	}

	public static void stop()
	{
		logger.d("Loggers.stop >>>");
		if (context == null)
		{
			SDCardReceiver.unregisterListener(sdCardListener);
			logger.d("Loggers.stop <<<");
		}
		else
		{
			SynchronizedFileAppender synchronizedfileappender;
			ConcurrentHashMap concurrenthashmap = fileAppendersMap;
			synchronized (concurrenthashmap) {

				for (Iterator iterator = fileAppendersMap.values().iterator(); iterator.hasNext(); synchronizedfileappender.close())
				{
					synchronizedfileappender = (SynchronizedFileAppender)iterator.next();
					logger.d((new StringBuilder()).append("Loggers.stop: closing file appender: ").append(synchronizedfileappender.getFile().getPath()).toString());
					synchronizedfileappender.flush();
				}
			}
		}

	}

	static 
	{
		logger = new Logger("Loggers", 7, new SystemLogPrinter("Loggers"));
		logger.disable();
		sdCardListener = new LogSDCardListener();
		SDCardReceiver.registerListener(sdCardListener);
	}
}
