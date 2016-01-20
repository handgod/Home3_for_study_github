// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import android.content.Intent;
import android.net.Uri;
import android.os.*;
import com.softspb.shell.SDCardReceiver;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.io.File;
import java.util.HashMap;

// Referenced classes of package com.softspb.shell.browser.service:
//			HTCBrowserUtils

abstract class HTCThumbObserver
	implements com.softspb.shell.SDCardReceiver.SDCardListener
{
	class ThumbFileObserver extends FileObserver
	{

		final HTCThumbObserver this$0;

		public void onEvent(int i, String s)
		{
			HTCThumbObserver.logger.d((new StringBuilder()).append("File system event: event=").append(HTCThumbObserver.getEventDescription(i)).append(" path=").append(s).toString());
			if ((i & 0x208) != 0)
			{
				final String url = (String)filename2Url.get(s);
				if (url != null)
					handler.post(new Runnable() {

						final ThumbFileObserver this$1;
						final String val$url;

						public void run()
						{
							onThumbChanged(url);
						}

				
				{
					this$1 = ThumbFileObserver.this;
					val$url = url;
//					super();
				}
					});
			}
		}

		ThumbFileObserver()
		{
			super(HTCThumbObserver.THUMB_FOLDER_HTC, 520);
			this$0 = HTCThumbObserver.this;
		}
	}

	class ThumbHandler extends Handler
	{

		final HTCThumbObserver this$0;

		ThumbHandler(Looper looper)
		{
			super(looper);
			this$0 = HTCThumbObserver.this;
		}
	}

	static final String externalStoragePath = Environment.getExternalStorageDirectory().getPath();
	static final String THUMB_FOLDER_HTC = (new StringBuilder()).append(externalStoragePath).append("/").append(".bookmark_thumb1").toString();
	
	static final Logger logger = Loggers.getLogger(HTCThumbObserver.class.getName());
	private final HashMap filename2Url = new HashMap();
	private final ThumbHandler handler;
	private FileObserver thumbFolderObserver;

	HTCThumbObserver(Looper looper)
	{
		handler = new ThumbHandler(looper);
		thumbFolderObserver = new ThumbFileObserver();
	}

	static String getEventDescription(int i)
	{
		StringBuilder stringbuilder = new StringBuilder();
		if ((i & 1) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("ACCESS");
		}
		if ((i & 4) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("ATTRIB");
		}
		if ((i & 0x10) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("CLOSE_NOWRITE");
		}
		if ((i & 8) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("CLOSE_WRITE");
		}
		if ((i & 0x100) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("CREATE");
		}
		if ((i & 0x200) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("DELETE");
		}
		if ((i & 0x400) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("DELETE_SELF");
		}
		if ((i & 2) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("MODIFY");
		}
		if ((i & 0x800) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("MOVE_SELF");
		}
		if ((i & 0x40) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("MOVED_FROM");
		}
		if ((i & 0x80) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("MOVED_TO");
		}
		if ((i & 0x20) != 0)
		{
			if (stringbuilder.length() > 0)
				stringbuilder.append('|');
			stringbuilder.append("OPEN");
		}
		return stringbuilder.toString();
	}

	void addURL(String s)
	{
		String s1 = HTCBrowserUtils.getHTCThumbnailFilename(s);
		filename2Url.put(s1, s);
	}

	public void onReceive(Intent intent)
	{
		String s;
		Uri uri;
		s = intent.getAction();
		uri = intent.getData();
		if (uri == null || !"file".equals(uri.getScheme()) || !externalStoragePath.equals(uri.getPath())) 
		{
			return;
		}
		else
		{
			if (!"android.intent.action.MEDIA_MOUNTED".equals(s))
				{
				if ("android.intent.action.MEDIA_UNMOUNTED".equals(s))
					thumbFolderObserver.stopWatching();
				}
			else 
				{
				thumbFolderObserver.startWatching();
				}
		}
		return;
	}

	abstract void onThumbChanged(String s);

	void removeUrl(String s)
	{
		String s1 = HTCBrowserUtils.getHTCThumbnailFilename(s);
		filename2Url.remove(s1);
	}

	void start()
	{
		thumbFolderObserver.startWatching();
		SDCardReceiver.registerListener(this);
	}

	void stop()
	{
		thumbFolderObserver.stopWatching();
		handler.removeCallbacksAndMessages(null);
		SDCardReceiver.unregisterListener(this);
	}



}
