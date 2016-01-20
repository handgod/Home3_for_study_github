package com.softspb.shell.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import com.softspb.shell.browser.service.BrowserClient;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class BookmarksAdapterAndroid extends BookmarksAdapter
{
  private static final long BROWSER_SERVICE_TIMEOUT_MS = 5000L;
  static final String SCHEMA_BOOKMARK_IMAGE_INT = "bookmark-image" + "://";
  static final String ICON_URI_PREFIX ;
  static final String IMAGE_PATH_ICON = "icon";
  static final String IMAGE_PATH_THUMBNAIL = "thumbnail";

  static final String THUMBNAIL_URI_PREFIX;
  private static final Logger logger;
  BrowserClientImpl browserClient;
  private Context context;
  private int nativePeer;

  static
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = SCHEMA_BOOKMARK_IMAGE_INT;
    ICON_URI_PREFIX = str1 + "icon" + 47;
    StringBuilder localStringBuilder2 = new StringBuilder();
    String str2 = SCHEMA_BOOKMARK_IMAGE_INT;
    THUMBNAIL_URI_PREFIX = str2 + "thumbnail" + 47;
    logger = Loggers.getLogger(BookmarksAdapterAndroid.class);
  }

  public BookmarksAdapterAndroid(int paramInt, Context paramContext)
  {
    this.context = paramContext;
    this.nativePeer = paramInt;
  }

  private Bitmap getIcon(int paramInt)
  {
    Logger localLogger = logger;
    String str = "getIcon: bookmarkId=" + paramInt;
    localLogger.d(str);
    Bitmap localBitmap = null;
    if (this.browserClient != null)
      localBitmap = this.browserClient.loadIcon(paramInt);
    return localBitmap;
  }

  private Bitmap getThumbnail(int paramInt)
  {
    Logger localLogger = logger;
    String str = "getThumbnail: bookmarkId=" + paramInt;
    localLogger.d(str);
    Bitmap localBitmap = null;
    if (this.browserClient != null)
      localBitmap = this.browserClient.loadThumbnail(paramInt);
    return localBitmap;
  }

  protected void deleteNativeBookmark(int paramInt)
  {
    if (this.nativePeer == 0)
      throw new IllegalStateException("BookmarksAdapter is dead");
    int i = this.nativePeer;
    removeBookmark(i, paramInt);
  }

  public Bitmap getImage(String s)
	{
		logger.d((new StringBuilder()).append("getImage: imageUrl=").append(s).toString());
		Bitmap bitmap;
		Logger logger1;
		StringBuilder stringbuilder;
		String s1;
		if (s.startsWith(ICON_URI_PREFIX))
			bitmap = getIcon(Integer.parseInt(s.substring(ICON_URI_PREFIX.length())));
		else
		if (s.startsWith(THUMBNAIL_URI_PREFIX))
			bitmap = getThumbnail(Integer.parseInt(s.substring(THUMBNAIL_URI_PREFIX.length())));
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Invalid bookmark image URI: ").append(s).toString());
		logger1 = logger;
		stringbuilder = (new StringBuilder()).append("returning image=").append(bitmap);
		if (bitmap == null)
			s1 = "";
		else
			s1 = (new StringBuilder()).append(" w=").append(bitmap.getWidth()).append(" h=").append(bitmap.getHeight()).toString();
		logger1.d(stringbuilder.append(s1).toString());
		return bitmap;
	}

  public void onStart()
  {
    logger.d("onStart >>>");
    Context localContext = this.context;
    BrowserClientImpl localBrowserClientImpl = new BrowserClientImpl(localContext);
    this.browserClient = localBrowserClientImpl;
    this.browserClient.connect();
    logger.d("onStart <<<");
  }

  public void onStop()
  {
    logger.d("onStop >>>");
    this.browserClient.disconnect();
    this.browserClient = null;
    this.nativePeer = 0;
    logger.d("onStop <<<");
  }

  public boolean openBookmark(String paramString)
  {
    Logger localLogger1 = logger;
    String str1 = "openBookmark: url=" + paramString;
    localLogger1.d(str1);
    Intent localIntent1 = new Intent("android.intent.action.VIEW");
    boolean flag;
    try
    {
      Uri localUri1 = Uri.parse(paramString);
      Uri localUri2 = localUri1;
      Intent localIntent2 = localIntent1.setData(localUri2);
      this.context.startActivity(localIntent1);
      flag = true;
      return flag;
    }
    catch (Exception localException)
    {
		logger.e((new StringBuilder()).append("Failed to parse url: ").append(paramString).toString(), localException);
		flag = false;
		return flag;
    }
  }

  protected void updateNativeBookmark(int paramInt, String paramString1, String paramString2)
  {
    if (this.nativePeer == 0)
      throw new IllegalStateException("BookmarksAdapter is dead");
    int i = this.nativePeer;
    addBookmark(i, paramInt, paramString1, paramString2);
  }

  class BrowserClientImpl extends BrowserClient
  {
    BrowserClientImpl(Context arg2)
    {
      super(arg2);
    }

    protected void onBookmarkDeleted(int paramInt)
    {
      Logger localLogger = BookmarksAdapterAndroid.logger;
      String str = "BrowserClient.onBookmarkDeleted: bookmarkId=" + paramInt;
      localLogger.d(str);
      BookmarksAdapterAndroid.this.deleteNativeBookmark(paramInt);
    }

    protected void onBookmarkUpdated(int paramInt, String paramString1, String paramString2)
    {
      Logger localLogger = BookmarksAdapterAndroid.logger;
      String str = "BrowserClient.onBookmarkUpdated: bookmarkId=" + paramInt + " title=" + paramString1 + " url=" + paramString2;
      localLogger.d(str);
      BookmarksAdapterAndroid.this.updateNativeBookmark(paramInt, paramString1, paramString2);
    }

    protected void onConnected()
    {
      BookmarksAdapterAndroid.logger.d("BrowserClient.onConnected");
      postLoadBookmarks();
    }

    protected void onDisconnected()
    {
      BookmarksAdapterAndroid.logger.d("BrowserClient.onDisconnected");
    }
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.BookmarksAdapterAndroid
 * JD-Core Version:    0.6.0
 */