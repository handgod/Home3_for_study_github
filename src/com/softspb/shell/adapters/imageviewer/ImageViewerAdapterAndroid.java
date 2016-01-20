// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.imageviewer;

import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.*;
import android.text.TextUtils;
import android.util.Pair;
import com.softspb.shell.adapters.AdaptersHolder;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.shell.util.BitmapHelper;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;
import java.util.*;

// Referenced classes of package com.softspb.shell.adapters.imageviewer:
//			ImageViewerAdapter, IImageViewer, ImageViewerService

public class ImageViewerAdapterAndroid extends ImageViewerAdapter
{

	private static final String BUCKET_ID_SELECTION = "bucket_id=?";
	private static final String ID_PROJECTION[];
	private ContentObserver contentObserver;
	private Context context;
	private IImageViewer imageViewer;
	private boolean isBound;
	private DecoratedBroadcastReceiver mediaReceiver;
	private NativeCallbacks nc;
	private DecoratedBroadcastReceiver sdReceiver;
	private ServiceConnection serviceConnection;
	private Intent serviceIntent;

	public ImageViewerAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		imageViewer = null;
		isBound = false;
		sdReceiver = new DecoratedBroadcastReceiver("android.intent.action.MEDIA_EJECT", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final ImageViewerAdapterAndroid this$0;

			public void onAction(Context context1, Intent intent)
			{
				ImageViewerAdapterAndroid.update();
				tryToUnbind();
			}

			
			{
				this$0 = ImageViewerAdapterAndroid.this;
//				super();
			}
		});
		mediaReceiver = new DecoratedBroadcastReceiver("android.intent.action.MEDIA_SCANNER_FINISHED", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final ImageViewerAdapterAndroid this$0;

			public void onAction(Context context1, Intent intent)
			{
				ImageViewerAdapterAndroid.update();
			}

			
			{
//				super();
				this$0 = ImageViewerAdapterAndroid.this;
			}
		});
		serviceConnection = new ServiceConnection() {

			final ImageViewerAdapterAndroid this$0;

			public void onServiceConnected(ComponentName componentname, IBinder ibinder)
			{
				imageViewer = IImageViewer.Stub.asInterface(ibinder);
			}

			public void onServiceDisconnected(ComponentName componentname)
			{
				imageViewer = null;
			}

			
			{
				this$0 = ImageViewerAdapterAndroid.this;
//				super();
			}
		};
	}

	public static native void folderAdd(int i, String s, String s1);

	private List getImagesByFolder(String s)
	{
		ArrayList arraylist;
		Cursor cursor;
		arraylist = new ArrayList();
		Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		int i;
		int j;
		Exception exception;
		if (!TextUtils.isEmpty(s))
		{
			ContentResolver contentresolver = context.getContentResolver();
			String as[] = ID_PROJECTION;
			String as1[] = new String[1];
			as1[0] = s;
			cursor = contentresolver.query(uri, as, "bucket_id=?", as1, "date_added DESC");
		} else
		{
			cursor = context.getContentResolver().query(uri, ID_PROJECTION, null, null, "date_added DESC LIMIT 50");
		}
		if (cursor!= null)
		{
			i = cursor.getColumnIndex("_id");
			j = cursor.getColumnIndex("orientation");
			for (; cursor.moveToNext(); arraylist.add(new Pair(cursor.getString(i), Integer.valueOf(cursor.getInt(j)))));
		}
		cursor.close();
		return arraylist;
	}

	private int getRotation(String s)
	{
		Cursor cursor;
		int j = 0;
		ContentResolver contentresolver = context.getContentResolver();
		Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String as[] = ID_PROJECTION;
		String as1[] = new String[1];
		as1[0] = s;
		cursor = contentresolver.query(uri, as, "_id=?", as1, null);
		if (cursor != null)
		{
			int k;
			int i = cursor.getColumnIndex("orientation");
			if (!(i == -1 || !cursor.moveToFirst()))
			{	
				k = cursor.getInt(i);
				j = k;
			}
			
		}
		else 
		{
			
			j = 0;
		}
		if (cursor != null)
			cursor.close();
		return j;

	}

	public static native void imageAdd(int i, String s, Integer integer);

	private void tryToBind()
	{
		if (!isBound)
		{
			context.bindService(serviceIntent, serviceConnection, 1);
			isBound = true;
		}
	}

	private void tryToUnbind()
	{
		if (isBound)
		{
			context.unbindService(serviceConnection);
			isBound = false;
		}
	}

	public static native void update();

	public void getFolderIds(int i)
	{
		Cursor cursor;
		Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendQueryParameter("distinct", "true").build();
		ContentResolver contentresolver = context.getContentResolver();
		String as[] = new String[2];
		as[0] = "bucket_display_name";
		as[1] = "bucket_id";
		cursor = android.provider.MediaStore.Images.Media.query(contentresolver, uri, as, null, null, null);
		while (cursor.moveToNext()) 
			folderAdd(i, cursor.getString(1), cursor.getString(0));
		cursor.close();
		return;
	}

	public Bitmap getImage(String s)
	{
		Bitmap bitmap;
		if (imageViewer != null)
		{
			Bitmap bitmap1;
			try
			{
				bitmap1 = imageViewer.getBitmap(s);
				if (bitmap1 == null)
				{
					bitmap = null;
					return bitmap;
				}
				int i = getRotation(s);
				if (i != 0)
				{
					bitmap = BitmapHelper.rotate(bitmap1, i);
					bitmap1.recycle();
					return bitmap;
				}
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
				bitmap = null;
				return bitmap;
			}
			bitmap = bitmap1;
		} else
		{
			bitmap = null;
		}
		return bitmap;
	}

	public void getImageList(int i, String s)
	{
		if ("mounted".equals(Environment.getExternalStorageState()))
			try
			{
				if (s.equals("gallery"))
					s = "";
				Iterator iterator = getImagesByFolder(s).iterator();
				while (iterator.hasNext()) 
				{
					Pair pair = (Pair)iterator.next();
					imageAdd(i, (String)pair.first, (Integer)pair.second);
				}
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
	}

	public void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		context = context1;
		nc = nativecallbacks;
		serviceIntent = new Intent(context1, ImageViewerService.class);
		sdReceiver.addActionListener("android.intent.action.MEDIA_UNMOUNTED", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final ImageViewerAdapterAndroid this$0;

			public void onAction(Context context2, Intent intent)
			{
				ImageViewerAdapterAndroid.update();
				tryToBind();
			}

			
			{
				this$0 = ImageViewerAdapterAndroid.this;
//				super();
			}
		});
		sdReceiver.addActionListener("android.intent.action.MEDIA_MOUNTED", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final ImageViewerAdapterAndroid this$0;

			public void onAction(Context context2, Intent intent)
			{
				ImageViewerAdapterAndroid.update();
			}

			
			{
				this$0 = ImageViewerAdapterAndroid.this;
//				super();
			}
		});
		contentObserver = new ContentObserver(new Handler()) {

			final ImageViewerAdapterAndroid this$0;

			public void onChange(boolean flag)
			{
				ImageViewerAdapterAndroid.update();
			}
			{
//				super(handler);
				this$0 = ImageViewerAdapterAndroid.this;
			}
		};
		IntentFilter intentfilter = sdReceiver.getIntentFilter();
		intentfilter.addDataScheme("file");
		context1.registerReceiver(sdReceiver, intentfilter);
		IntentFilter intentfilter1 = mediaReceiver.getIntentFilter();
		intentfilter1.addDataScheme("file");
		context1.registerReceiver(mediaReceiver, intentfilter1);
		tryToBind();
		context1.getContentResolver().registerContentObserver(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver);
	}

	public void onDestroy(Context context1)
	{
		context1.getContentResolver().unregisterContentObserver(contentObserver);
		tryToUnbind();
		context1.unregisterReceiver(sdReceiver);
		context1.unregisterReceiver(mediaReceiver);
	}

	public void openImage(String s)
	{
		if (TextUtils.isEmpty(s))
		{
			nc.launch("gallery");
		} else
		{
			Intent intent = new Intent("android.intent.action.VIEW", ContentUris.withAppendedId(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.valueOf(s).longValue()));
			context.startActivity(intent);
		}
	}

	static 
	{
		String as[] = new String[2];
		as[0] = "_id";
		as[1] = "orientation";
		ID_PROJECTION = as;
	}



/*
	static IImageViewer access$102(ImageViewerAdapterAndroid imagevieweradapterandroid, IImageViewer iimageviewer)
	{
		imagevieweradapterandroid.imageViewer = iimageviewer;
		return iimageviewer;
	}

*/

}
