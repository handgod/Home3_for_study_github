// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Browser;

import com.softspb.util.log.Logger;

// Referenced classes of package com.softspb.shell.browser.service:
//			HTCBrowserUtils

public class BrowserUtils
{

	static final int BOOKMARK_ICON_INDEX =0;
	static final String BOOKMARK_ICON_PROJECTION[];

	public BrowserUtils()
	{
	}

	public static Bitmap loadIcon(ContentResolver contentresolver, int i, Logger logger)
	{
		Bitmap bitmap;
		Cursor cursor;
		bitmap = null;
		logger.d((new StringBuilder()).append("getIcon: bookmarkId=").append(i).toString());
		cursor = null;
		Bitmap bitmap1;
		cursor = contentresolver.query(ContentUris.withAppendedId(Browser.BOOKMARKS_URI, i), BOOKMARK_ICON_PROJECTION, "bookmark=1", null, null);
		if (cursor == null || !cursor.moveToFirst())
			return bitmap;
		byte abyte0[] = cursor.getBlob(0);
		bitmap = null;
		if (abyte0 == null)
			return bitmap;
		logger.d((new StringBuilder()).append("getIcon: decoding ").append(abyte0.length).append(" bytes of icon bitmap").toString());
		bitmap1 = BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length);
		bitmap = bitmap1;
		
		return bitmap;
	}

	public static Bitmap loadThumbnail(ContentResolver contentresolver, int i, boolean flag, Logger logger)
	{
		Bitmap bitmap;
		Cursor cursor;
		bitmap = null;
		
		logger.d((new StringBuilder()).append("getThumbnail: bookmarkId=").append(i).toString());
		cursor = null;
		byte abyte0[];
		abyte0 = null;
		int j;
		cursor = contentresolver.query(ContentUris.withAppendedId(Browser.BOOKMARKS_URI, i), null, "bookmark=1", null, null);
		if (cursor == null || !cursor.moveToFirst())
			return bitmap;
		
		j = cursor.getColumnIndex("thumbnail");
		abyte0 = cursor.getBlob(j);
		if (j == -1) 
		{
			logger.w("Missing thumbnail column in bookmarks table");
			if (abyte0 != null)
				{
					logger.d((new StringBuilder()).append("getThumbnail: decoding ").append(abyte0.length).append(" bytes of thumbnail bitmap").toString());
					Bitmap bitmap1 = BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length);
					bitmap = bitmap1;
					if (bitmap == null)
						logger.w("Failed to load thumbnail.");
					return bitmap;
				}
			else 
			{
				logger.w("Thumbnail data missing in bookmarks table");
				if (flag)
					bitmap = HTCBrowserUtils.tryLoadHTCThumbnail(i, cursor);
			}
		}
		else
		{
			abyte0 = cursor.getBlob(j);
		}
		
		return bitmap;
	}

	static 
	{
		String as[] = new String[1];
		as[0] = "favicon";
		BOOKMARK_ICON_PROJECTION = as;
	}
}
