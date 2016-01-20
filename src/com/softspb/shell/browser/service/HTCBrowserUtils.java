// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public abstract class HTCBrowserUtils
{

	private static final String HTC_SENSE_ALBUM_WIDGET_PROVIDER = "com.htc.album.PhotoAppWidgetProvider";
	private static final String HTC_SENSE_MUSIC_WIDGET_PROVIDER = "com.htc.music.MediaAppWidgetProvider";
	public static final String MANUFACTURER_HTC = "HTC";
	public static final String THUMB_FOLDER_HTC = "/sdcard/.bookmark_thumb1";
	private static Logger logger = Loggers.getLogger(HTCBrowserUtils.class.getName());

	private HTCBrowserUtils()
	{
	}

	public static boolean detectHTCBrowser(Context context)
	{
		boolean flag = false;
		if ("HTC".equals(Build.MANUFACTURER)) 
		{
			AppWidgetManager appwidgetmanager = AppWidgetManager.getInstance(context);
			for (Iterator iterator = appwidgetmanager.getInstalledProviders().iterator(); iterator.hasNext();)
			{
				AppWidgetProviderInfo appwidgetproviderinfo1 = (AppWidgetProviderInfo)iterator.next();
				if (appwidgetproviderinfo1.provider.getClassName().equals("com.htc.album.PhotoAppWidgetProvider"))
				{
					logger.d((new StringBuilder()).append("Detected HTC Sense widget provider: ").append(appwidgetproviderinfo1.provider).toString());
					logger.d("Detected HTC Browser");
					flag = true;
					continue; /* Loop/switch isn't completed */
				}
			}

			for (Iterator iterator1 = appwidgetmanager.getInstalledProviders().iterator(); iterator1.hasNext();)
			{
				AppWidgetProviderInfo appwidgetproviderinfo = (AppWidgetProviderInfo)iterator1.next();
				if (appwidgetproviderinfo.provider.getClassName().equals("com.htc.music.MediaAppWidgetProvider"))
				{
					logger.d((new StringBuilder()).append("Detected HTC Sense widget provider: ").append(appwidgetproviderinfo.provider).toString());
					logger.d("Detected HTC Browser");
					flag = true;
					continue; /* Loop/switch isn't completed */
				}
			}

			logger.d("NOT an HTC Browser");
		}
		else 
		{
		logger.d("Not an HTC device");
		}
		return flag;
	}

	public static String getHTCThumbnailFilename(String s)
	{
		return (new StringBuilder()).append("m").append(Integer.toHexString(s.hashCode())).append(".jpg").toString();
	}

	public static Bitmap tryLoadHTCThumbnail(int i, Cursor cursor)
	{
		Bitmap bitmap;
		File file;
		String s;
		bitmap = null;
		logger.d("Trying to load thumbnail from HTC...");
		file = new File("/sdcard/.bookmark_thumb1");
		s = cursor.getString(cursor.getColumnIndex("url"));
		if (s != null)
		{
			String s1;
			Bitmap bitmap1;
			
			if (!file.isDirectory())
			{
				logger.w("HTC thumbnails directory not found.");
				return bitmap;
			}
			else
			{
				s1 = (new File(file, getHTCThumbnailFilename(s))).getPath();
				logger.d((new StringBuilder()).append("Loading HTC thumbnail from file: ").append(s1).toString());
				bitmap1 = BitmapFactory.decodeFile(s1);
				bitmap = bitmap1;
			}
		}
		else
		{
			logger.w((new StringBuilder()).append("Bookmark has no URL: id=").append(i).toString());
		}
		return bitmap;
		
	}

}
