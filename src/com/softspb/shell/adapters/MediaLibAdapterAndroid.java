package com.softspb.shell.adapters;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.text.TextUtils;
import android.util.SparseArray;
import com.softspb.shell.SDCardReceiver;
import com.softspb.shell.SDCardReceiver.SDCardListener;
import com.softspb.util.DelayedHandler;
import com.softspb.util.log.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class MediaLibAdapterAndroid extends MediaLibAdapter
  implements SDCardReceiver.SDCardListener
{
  static final String[] ALBUMS_PROJECTION  ;
  static final String ALBUMS_SELECTION_HAVING_ART_NOT_NULL ;
  static final String ALBUMS_SELECTION_HAVING_ART_NULL  ;
  static final String ALBUM_THUMB_FOLDER = "albumthumbs";
  static final String ALBUM_THUMB_PATH  ;
  static final int INDEX_ALBUM = 1;
  static final int INDEX_ALBUM_ART = 2;
  static final int INDEX_ALBUM_ID = 0;
  static final int INDEX_ALBUM_KEY = 3;
  static final int INDEX_ARTIST = 4;
  static final int INDEX_NUMBER_OF_SONGS = 5;
  static final int MSG_REQUEST_THUMB = 1;
  static final String externalStoragePath;
  private static final Uri sArtworkUri;
  private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
  private final HashMap<String, Integer> albumThumbs2Ids;
  private ThumbFileObserver albumThumbsObserver;
  private int nativePeer;
  private Handler thumbGeneratorHandler;

  static
  {
    String[] arrayOfString = new String[6];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "album";
    arrayOfString[2] = "album_art";
    arrayOfString[3] = "album_key";
    arrayOfString[4] = "artist";
    arrayOfString[5] = "numsongs";
    ALBUMS_PROJECTION = arrayOfString;
    ALBUMS_SELECTION_HAVING_ART_NOT_NULL = "album_art" + " NOT NULL";
    ALBUMS_SELECTION_HAVING_ART_NULL = "album_art" + " IS NULL";
    externalStoragePath = Environment.getExternalStorageDirectory().getPath();
    StringBuilder localStringBuilder = new StringBuilder();
    String str = externalStoragePath;
    ALBUM_THUMB_PATH = str + "/" + "albumthumbs";
    sArtworkUri = Uri.parse("content://media/external/audio/albumart");
  }

  public MediaLibAdapterAndroid(int paramInt, Context paramContext)
  {
    super(paramContext);
    HashMap localHashMap = new HashMap();
    this.albumThumbs2Ids = localHashMap;
    this.nativePeer = paramInt;
  }

  private void checkThumb(Album paramAlbum)
  {
    String str = Album.getAlbumImagePathName(paramAlbum.imageUrl);
    if (str != null)
    {
      HashMap localHashMap = this.albumThumbs2Ids;
      Integer localInteger = Integer.valueOf(paramAlbum.id);
      Object localObject = localHashMap.put(str, localInteger);
    }
    if ((paramAlbum.imageUrl != null) && (!paramAlbum.imageFileExists))
    {
      int i = paramAlbum.id;
      postRequestGenerateThumb(i);
    }
  }

	private static ComponentName getComponentNameOnSamsung()
	{
		ComponentName componentname;
		if (android.os.Build.VERSION.SDK_INT < 8)
			componentname = new ComponentName("com.sec.android.music", (new StringBuilder()).append("com.sec.android.app.music").append(".list.activity.MpListActivity").toString());
		else
			componentname = new ComponentName("com.android.music", (new StringBuilder()).append("com.android.music").append(".list.activity.MpListActivity").toString());
		return componentname;
	}
	private void requestGenerateThumbnail_Blocking(int i)
	{
		Uri uri;
		uri = ContentUris.withAppendedId(sArtworkUri, i);
		if (uri != null)
		{	
			ParcelFileDescriptor parcelfiledescriptor = null;
			logger.d((new StringBuilder()).append("Opening file descriptor: ").append(uri).toString());
			try {
				parcelfiledescriptor = contentResolver.openFileDescriptor(uri, "r");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (parcelfiledescriptor != null)
				{
					try {
						parcelfiledescriptor.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
		}
	}

  protected void addContentItem(Album paramAlbum)
  {
    checkThumb(paramAlbum);
    super.addContentItem(paramAlbum);
  }

  protected void addNativeContentItem(Album paramAlbum)
  {
    if (this.nativePeer == 0)
      throw new IllegalStateException("MediaLibAdapter is dead");
    int i = this.nativePeer;
    int j = paramAlbum.id;
    String str1 = paramAlbum.title;
    String str2 = paramAlbum.imageUrl;
    addAlbum(i, j, str1, str2);
  }

  protected Album createContentItem(int paramInt, Cursor paramCursor)
  {
    return new Album(paramInt,paramCursor);
  }

  protected void deleteContentItem(int paramInt)
  {
    Album localAlbum = (Album)this.contentItems.get(paramInt);
    if (localAlbum != null)
    {
      String str = Album.getAlbumImagePathName(localAlbum.imageUrl);
      if (str != null)
          this.albumThumbs2Ids.remove(str);
    }
    super.deleteContentItem(paramInt);
  }

  protected int getContentItemId(Cursor paramCursor)
  {
    return paramCursor.getInt(0);
  }

  void logRow(String paramString, Cursor paramCursor)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(paramString).append("[");
    int i = 0;
    while (true)
    {
      int j = paramCursor.getColumnCount();
      if (i >= j)
        break;
      String str1 = paramCursor.getString(i);
      if (str1 != null)
      {
        StringBuilder localStringBuilder3 = localStringBuilder1.append(" ");
        String str2 = paramCursor.getColumnName(i);
        StringBuilder localStringBuilder4 = localStringBuilder3.append(str2).append("=").append(str1);
      }
      i += 1;
    }
    StringBuilder localStringBuilder5 = localStringBuilder1.append("]");
    Logger localLogger = this.logger;
    String str3 = localStringBuilder1.toString();
    localLogger.d(str3);
  }

  void onAlbumThumbChanged(String paramString)
  {
    Logger localLogger = this.logger;
    String str = "onAlbumThumbChanged: path=" + paramString;
    localLogger.d(str);
    if (this.albumThumbs2Ids.containsKey(paramString))
    {
      int i = ((Integer)this.albumThumbs2Ids.get(paramString)).intValue();
      Album localAlbum = (Album)this.contentItems.get(i);
      if (localAlbum != null)
        updateNativeContentItem(localAlbum);
    }
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
					albumThumbsObserver.stopWatching();	
			}
			else 
			{
				albumThumbsObserver.startWatching();
			}
		}
		return;
	}
  public void onStart()
  {
    super.onStart();
    Uri[] arrayOfUri = new Uri[1];
    Uri localUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    arrayOfUri[0] = localUri;
    setContentUris(arrayOfUri);
    ThumbFileObserver localThumbFileObserver = new ThumbFileObserver();
    this.albumThumbsObserver = localThumbFileObserver;
    this.albumThumbsObserver.startWatching();
    SDCardReceiver.registerListener(this);
    Looper localLooper = this.observerHandler.getLooper();
    ThumbGeneratorHandler localThumbGeneratorHandler = new ThumbGeneratorHandler(localLooper);
    this.thumbGeneratorHandler = localThumbGeneratorHandler;
  }

  public void onStop()
  {
    super.onStop();
    this.thumbGeneratorHandler.removeCallbacksAndMessages(null);
    this.albumThumbsObserver.stopWatching();
    SDCardReceiver.unregisterListener(this);
    this.nativePeer = 0;
  }

public boolean playAlbum(int i)
{
	boolean flag = true;
	Intent intent = new Intent("android.intent.action.PICK");
	intent.setDataAndType(Uri.EMPTY, "vnd.android.cursor.dir/track");
	intent.putExtra("album", Integer.toString(i));
	try
	{
		context.startActivity(intent);
	}
	catch (Exception exception)
	{
		logger.w((new StringBuilder()).append("Failed to start playing album: ").append(exception).toString(), exception);
		Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, i);
		Intent intent1 = new Intent("android.intent.action.VIEW");
		intent1.setDataAndType(uri, "vnd.android.cursor.item/album");
		intent1.setComponent(getComponentNameOnSamsung());
		try
		{
			context.startActivity(intent1);
		}
		catch (Exception exception1)
		{
			logger.w((new StringBuilder()).append("Failed to start playing album: ").append(exception1).toString(), exception1);
			Intent intent2 = new Intent("android.intent.action.VIEW");
			intent2.setDataAndType(uri, "vnd.android.cursor.item/album");
			try
			{
				context.startActivity(intent2);
			}
			catch (Exception exception2)
			{
				logger.w((new StringBuilder()).append("Failed to start playing album: ").append(exception2).toString(), exception2);
				flag = false;
			}
		}
	}
	return flag;
}

  void postRequestGenerateThumb(int paramInt)
  {
    Integer localInteger = Integer.valueOf(paramInt);
    if (!this.thumbGeneratorHandler.hasMessages(1, localInteger))
    {
      Handler localHandler = this.thumbGeneratorHandler;
      Message localMessage = Message.obtain(this.thumbGeneratorHandler, 1, localInteger);
      boolean bool = localHandler.sendMessage(localMessage);
    }
  }

  protected Cursor query()
  {
    int i = 0;
    this.logger.d("query");
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3;
    Cursor localCursor ;
    try
    {
      ContentResolver localContentResolver1 = this.contentResolver;
      Uri localUri1 = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
      String[] arrayOfString1 = ALBUMS_PROJECTION;
      String str1 = ALBUMS_SELECTION_HAVING_ART_NOT_NULL;
       localCursor = localContentResolver1.query(localUri1, arrayOfString1, str1, null, "album");
      localObject1 = localCursor;
    }
    catch (Exception localException1)
    {
    }
    return (Cursor)localObject1;
  }

  public void reload(boolean paramBoolean)
  {
    super.reload(paramBoolean);
  }

  protected void removeNativeContentItem(Album paramAlbum)
  {
    if (this.nativePeer == 0)
      throw new IllegalStateException("MediaLibAdapter is dead");
    int i = this.nativePeer;
    int j = paramAlbum.id;
    removeAlbum(i, j);
  }

  protected void updateContentItem(Album paramAlbum)
  {
    checkThumb(paramAlbum);
    super.updateContentItem(paramAlbum);
  }

  protected void updateNativeContentItem(Album paramAlbum)
  {
    if (this.nativePeer == 0)
      throw new IllegalStateException("MediaLibAdapter is dead");
    int i = this.nativePeer;
    int j = paramAlbum.id;
    String str1 = paramAlbum.title;
    if (paramAlbum.imageFileExists);
    for (String str2 = paramAlbum.imageUrl; ; str2 = null)
    {
      updateAlbum(i, j, str1, str2);
      return;
    }
  }

  class ThumbGeneratorHandler extends Handler
  {
    public ThumbGeneratorHandler(Looper arg2)
    {
      super();
    }

    public void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 1)
      {
        int i = ((Integer)paramMessage.obj).intValue();
        Logger localLogger = MediaLibAdapterAndroid.this.logger;
        String str = "ThumbGeneratorHandler: request generate thumb albumId=" + i;
        localLogger.d(str);
        MediaLibAdapterAndroid.this.requestGenerateThumbnail_Blocking(i);
      }
    }
  }

  class ThumbFileObserver extends FileObserver
  {
	  class MediaLibAdapterAndroid$ThumbFileObserver$1  implements Runnable
	  {
		  private String val$fullPath;
		  
		  public MediaLibAdapterAndroid$ThumbFileObserver$1(String str2) {
			this.val$fullPath = str2;
		  }
		  public void run()
		  {
			 onAlbumThumbChanged(val$fullPath);
		  }
	 }
	  
    ThumbFileObserver()
    {
    	super(MediaLibAdapterAndroid.ALBUM_THUMB_PATH, 520);
//      super(520);
    }

    public void onEvent(int paramInt, String paramString)
    {
      if ((paramInt & 0x208) != 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        String str1 = MediaLibAdapterAndroid.ALBUM_THUMB_PATH;
        String str2 = str1 + "/" + paramString;
        DelayedHandler localDelayedHandler = MediaLibAdapterAndroid.this.observerHandler;
        MediaLibAdapterAndroid$ThumbFileObserver$1 local1 = new MediaLibAdapterAndroid$ThumbFileObserver$1(str2);
        boolean bool = localDelayedHandler.post(local1);
      }
    }
  }

static class Album extends AbstractContentAdapter.ContentItem
  {

	String albumKey;
    String artist;
    boolean imageFileExists;
    String imageUrl;
    int numberOfSongs;
    String title;

    Album(int i, Cursor cursor)
	{
		//super(i, cursor);
    	
    	super(i, cursor);
		title = cursor.getString(1);
		albumKey = cursor.getString(3);
		artist = cursor.getString(4);
		numberOfSongs = cursor.getInt(5);
		imageUrl = cursor.getString(2);
		if (imageUrl != null)
		{
			String s = getAlbumImagePathName(imageUrl);
			if (s != null)
				imageFileExists = (new File(s)).exists();
		}
	}
     static String getAlbumImagePathName(String paramString)
    {
      if ((paramString != null) && (paramString.startsWith("file://")))
        paramString = paramString.substring(7);
      return paramString;
    }

    protected void formatFields(StringBuilder paramStringBuilder)
    {
      StringBuilder localStringBuilder1 = paramStringBuilder.append(" title=");
      String str1 = this.title;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
      StringBuilder localStringBuilder3 = paramStringBuilder.append(" artist=");
      String str2 = this.artist;
      StringBuilder localStringBuilder4 = localStringBuilder3.append(str2);
      StringBuilder localStringBuilder5 = paramStringBuilder.append(" imageUrl=");
      String str3 = this.imageUrl;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(str3);
      StringBuilder localStringBuilder7 = paramStringBuilder.append(" albumKey=");
      String str4 = this.albumKey;
      StringBuilder localStringBuilder8 = localStringBuilder7.append(str4);
      StringBuilder localStringBuilder9 = paramStringBuilder.append(" numberOfSongs=");
      int i = this.numberOfSongs;
      StringBuilder localStringBuilder10 = localStringBuilder9.append(i);
      StringBuilder localStringBuilder11 = paramStringBuilder.append(" imageFileExists=");
      boolean bool = this.imageFileExists;
      StringBuilder localStringBuilder12 = localStringBuilder11.append(bool);
    }
	public boolean update(Cursor cursor)
	{
		boolean flag = false;
		String s = cursor.getString(1);
		String s1 = cursor.getString(3);
		String s2 = cursor.getString(4);
		int i = cursor.getInt(5);
		String s3 = cursor.getString(2);
		boolean flag1 = false;
		if (!TextUtils.equals(title, s))
		{
			flag = true;
			title = s;
		}
		if (!TextUtils.equals(albumKey, s1))
		{
			flag = true;
			albumKey = s1;
		}
		if (!TextUtils.equals(artist, s2))
		{
			flag = true;
			artist = s2;
		}
		if (numberOfSongs != i)
		{
			flag = true;
			numberOfSongs = i;
		}
		if (!TextUtils.equals(imageUrl, s3))
		{
			flag = true;
			imageUrl = s3;
		}
		if (s3 != null)
		{
			String s4 = getAlbumImagePathName(s3);
			if (s4 != null)
				flag1 = (new File(s4)).exists();
		}
		if (flag1 != imageFileExists)
		{
			flag = true;
			imageFileExists = flag1;
		}
		return flag;
	}
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.MediaLibAdapterAndroid
 * JD-Core Version:    0.6.0
 */