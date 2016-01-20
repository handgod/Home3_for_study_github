// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.Browser;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.softspb.shell.browser.service:
//			Bookmark, IBrowserServiceCallback, HTCBrowserUtils, BrowserConfiguration, 
//			HTCThumbObserver, BrowserUtils

public class BrowserService extends Service
{
	private class HTCThumbObserver extends com.softspb.shell.browser.service.HTCThumbObserver
	{

		final BrowserService this$0;

		void onThumbChanged(String s)
		{
			logger.d((new StringBuilder()).append("onThumbChanged: url=").append(s).toString());
			Bookmark bookmark = (Bookmark)url2bookmark.get(s);
			if (bookmark != null)
				notifyBookmarkUpdated(bookmark);
		}

		HTCThumbObserver(Looper looper)
		{
			super(looper);
			this$0 = BrowserService.this;
		}
	}


	static final String BOOKMARKS_SELECTION = (new StringBuilder()).append("bookmark").append("=1").toString();
	public static final int BOOKMARK_IMAGE_TYPE_ICON = 1;
	public static final int BOOKMARK_INAGE_TYPE_THUMBNAIL = 2;
	private static final int MAX_BITMAP_LENGTH = 0x182b8;
	private static final Logger logger = Loggers.getLogger(BrowserService.class);
	final SparseArray bookmarks = new SparseArray();
	private BrowserConfiguration browserConfiguration;
	private final IBrowserService.Stub browserServiceBinder = new IBrowserService.Stub() {

		final BrowserService this$0;

		public BrowserConfiguration getBrowserConfiguration()
			throws RemoteException
		{
			return browserConfiguration;
		}

		public void loadBookmarks()
			throws RemoteException
		{
			BrowserService.this.loadBookmarks();
		}

		public Bitmap loadIcon(int i)
			throws RemoteException
		{
			return BrowserUtils.loadIcon(contentResolver, i, BrowserService.logger);
		}

		public Bitmap loadThumbnail(int i)
			throws RemoteException
		{
			return BrowserUtils.loadThumbnail(contentResolver, i, isHtcBrowser, BrowserService.logger);
		}

		public void registerCallback(IBrowserServiceCallback ibrowserservicecallback)
			throws RemoteException
		{
			BrowserService.logger.d((new StringBuilder()).append("registerCallback: ").append(ibrowserservicecallback).toString());
			if (ibrowserservicecallback != null)
				callbackList.register(ibrowserservicecallback);
		}

		public void unregisterCallback(IBrowserServiceCallback ibrowserservicecallback)
			throws RemoteException
		{
			BrowserService.logger.d((new StringBuilder()).append("unregisterCallback: ").append(ibrowserservicecallback).toString());
			if (ibrowserservicecallback != null)
				callbackList.unregister(ibrowserservicecallback);
		}

			
			{
				this$0 = BrowserService.this;
//				super();
			}
	};
	private final RemoteCallbackList callbackList = new RemoteCallbackList();
	final ArrayList clients = new ArrayList();
	private ContentObserver contentObserver;
	ContentResolver contentResolver;
	private HTCThumbObserver htcThumbObserver;
	private boolean isHtcBrowser;
	private HashMap url2bookmark;

	public BrowserService()
	{
		Handler localhandler = new Handler();
		contentObserver = new ContentObserver(localhandler) {

			final BrowserService this$0;

			public void onChange(boolean flag)
			{
				BrowserService.logd("onReceive >>>");
				loadBookmarks();
				BrowserService.logd("onReceive <<<");
			}

			
			{
//				super(localhandler);
				this$0 = BrowserService.this;
			}
		};
	}

	private void deleteBookmark(int i)
	{
		Bookmark bookmark = (Bookmark)bookmarks.get(i);
		bookmarks.delete(i);
		if (isHtcBrowser && bookmark != null)
			url2bookmark.remove(bookmark.url);
		notifyBookmarkDeleted(i);
	}
	private void loadBookmarks()
	  {
		 Cursor localCursor = null;
		 Bookmark localBookmark = null;
	    logger.d("loadBookmarks >>>");
	    SparseArray localSparseArray = this.bookmarks;
	    SparseIntArray localSparseIntArray = new SparseIntArray();
	    int i = localSparseArray.size();
	    int j = 0;
	    while (j < i)
	    {
	      int k = localSparseArray.keyAt(j);
	      localSparseIntArray.put(k, 1);
	      j += 1;
	    }
	    int m = 0;
	    try
	    {
	      ContentResolver localContentResolver = this.contentResolver;
	      Uri localUri = Browser.BOOKMARKS_URI;
	      String[] arrayOfString = Browser.HISTORY_PROJECTION;
	      String str1 = BOOKMARKS_SELECTION;
	      localCursor = localContentResolver.query(localUri, arrayOfString, str1, null, null);
	      if ((localCursor != null) && (localCursor.moveToFirst()))
	        while (true)
	        {
	          if (localCursor.isAfterLast())
	           return;
	          int n = (int)localCursor.getLong(0);
	          localSparseIntArray.delete(n);
	          localBookmark = (Bookmark)localSparseArray.get(n);
	          if (localBookmark != null)
	            break;
	          int i1 = 1;
	          localBookmark = new Bookmark(n, localCursor);
	          localSparseArray.put(n, localBookmark);
	          if ((this.isHtcBrowser) && (localBookmark.url != null))
	          {
	            HashMap localHashMap = this.url2bookmark;
	            String str2 = localBookmark.url;
	            Object localObject1 = localHashMap.put(str2, localBookmark);
	            HTCThumbObserver localHTCThumbObserver = this.htcThumbObserver;
	            String str3 = localBookmark.url;
	            localHTCThumbObserver.addURL(str3);
	          }
	          m += 1;
	          logd("loadBookmarks: " + localBookmark);
	          if (i1 != 0)
	            updateBookmark(localBookmark);
	          boolean bool = localCursor.moveToNext();
	        }
	    }
	    finally
	    {
	      {
	    	boolean i1;
	        if (localCursor != null);
	        try
	        {
	          localCursor.close();
	          label306: logd("loadBookmarks: loaded " + m + " bookmarks");
	          logd("loadBookmarks <<<");
	        
	          boolean i2 = localBookmark.update(localCursor);
	         
	            i = localSparseIntArray.size();
	            j = 0;
	            while (j < i)
	            {
	              int i3 = localSparseIntArray.keyAt(j);
	              deleteBookmark(i3);
	              j += 1;
	            }
	          }
	          catch (Exception localException1)
	          {
	            
	          }
	
	      }
	    }
	  }
	

	private static void logd(String s)
	{
		logger.d(s);
	}

	private static void loge(String s, Throwable throwable)
	{
		logger.e(s, throwable);
	}

	private static void logw(String s)
	{
		logger.w(s);
	}

	private void updateBookmark(Bookmark bookmark)
	{
		notifyBookmarkUpdated(bookmark);
	}

	void notifyBookmarkDeleted(int i)
	{
		int j = callbackList.beginBroadcast();
		int k = 0;
		while (k < j) 
		{
			try
			{
				((IBrowserServiceCallback)callbackList.getBroadcastItem(k)).onBookmarkDeleted(i);
			}
			catch (RemoteException remoteexception) { }
			k++;
		}
		callbackList.finishBroadcast();
	}

	void notifyBookmarkUpdated(Bookmark bookmark)
	{
		int i = callbackList.beginBroadcast();
		int j = 0;
		while (j < i) 
		{
			try
			{
				((IBrowserServiceCallback)callbackList.getBroadcastItem(j)).onBookmarkUpdated(bookmark.id, bookmark.title, bookmark.url);
			}
			catch (RemoteException remoteexception) { }
			j++;
		}
		callbackList.finishBroadcast();
	}

	public IBinder onBind(Intent intent)
	{
		return browserServiceBinder;
	}

	public void onCreate()
	{
		logd("onCreate >>>");
		super.onCreate();
		contentResolver = getContentResolver();
		contentResolver.registerContentObserver(Browser.BOOKMARKS_URI, true, contentObserver);
		isHtcBrowser = HTCBrowserUtils.detectHTCBrowser(this);
		browserConfiguration = new BrowserConfiguration(isHtcBrowser);
		if (isHtcBrowser)
		{
			htcThumbObserver = new HTCThumbObserver(Looper.myLooper());
			htcThumbObserver.start();
			url2bookmark = new HashMap();
		}
		logd("onCreate <<<");
	}

	public void onDestroy()
	{
		logd("onDestroy >>>");
		super.onDestroy();
		contentResolver.unregisterContentObserver(contentObserver);
		if (isHtcBrowser)
			htcThumbObserver.stop();
		logd("onDestroy <<<");
	}








}
