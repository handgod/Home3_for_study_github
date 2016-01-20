// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import android.content.*;
import android.content.pm.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import com.softspb.shell.data.ShortcutInfo;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.shell.util.orm.CursorDataAdapter;
import com.softspb.shell.util.orm.DataBuilder;
import com.softspb.util.CollectionFactory;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Referenced classes of package com.softspb.shell.adapters:
//			ShortcutAdapter, AdaptersHolder

public class ShortcutAdapterAndroid extends ShortcutAdapter
{

	private static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	private int adapterToken;
	private DataBuilder builder;
	private Context context;
	private DecoratedBroadcastReceiver installShortcutReceiver;
	private Lock lock;
	private Set notInited;
	private ContentResolver resolver;
	private Uri shortcutsContentUri;

	public ShortcutAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		builder = DataBuilder.createBuilder(ShortcutInfo.class);
//		lock = new ReentrantLock();
		notInited = CollectionFactory.newHashSet();
		installShortcutReceiver = new DecoratedBroadcastReceiver("com.android.launcher.action.INSTALL_SHORTCUT", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final ShortcutAdapterAndroid this$0;

			public void onAction(Context context1, Intent intent)
			{
				addShortcut(intent, false);
			}

			
			{
				this$0 = ShortcutAdapterAndroid.this;
//				super();
			}
		});
	}

	public static native void addShortcut(int i, boolean flag, int j, String s, String s1, String s2);

	public static native void deleteShortcut(int i, int j);

	private String getPackageName(Intent intent)
	{
		ResolveInfo resolveinfo = context.getPackageManager().resolveActivity(intent, 0);
		String s;
		if (resolveinfo == null || resolveinfo.activityInfo == null || resolveinfo.activityInfo.packageName == null)
		{
			s = "";
		} else
		{
			Log.i("shortcuts resolve", (new StringBuilder()).append("package ").append(resolveinfo.activityInfo.packageName).toString());
			s = resolveinfo.activityInfo.packageName;
		}
		return s;
	}

	private boolean init(ShortcutInfo shortcutinfo)
	{
		String s = getPackageName(shortcutinfo.getIntent());
		boolean flag;
		if (s.length() == 0)
		{
			flag = false;
		} else
		{
			initShortcut(adapterToken, shortcutinfo.getId(), shortcutinfo.getTitle(), shortcutinfo.getImageUri(context).toString(), s);
			flag = true;
		}
		return flag;
	}

	public static native void initShortcut(int i, int j, String s, String s1, String s2);

	public void addShortcut(Intent intent, boolean flag)
	{
		String s = intent.getStringExtra("android.intent.extra.shortcut.NAME");
		Intent intent1 = (Intent)intent.getParcelableExtra("android.intent.extra.shortcut.INTENT");
		ShortcutInfo shortcutinfo = new ShortcutInfo(s, intent1);
		android.os.Parcelable parcelable = intent.getParcelableExtra("android.intent.extra.shortcut.ICON");
		int i;
		if (parcelable instanceof Bitmap)
		{
			shortcutinfo.setIcon((Bitmap)parcelable);
		} else
		{
			android.os.Parcelable parcelable1 = intent.getParcelableExtra("android.intent.extra.shortcut.ICON_RESOURCE");
			if (parcelable1 instanceof android.content.Intent.ShortcutIconResource)
				shortcutinfo.setIconResource((android.content.Intent.ShortcutIconResource)parcelable1);
		}
		i = (new Integer(resolver.insert(shortcutsContentUri, shortcutinfo.toContentValues()).getLastPathSegment())).intValue();
		shortcutinfo.setId(i);
		addShortcut(adapterToken, flag, i, shortcutinfo.getTitle(), shortcutinfo.getImageUri(context).toString(), getPackageName(intent1));

	}

	public void launch(int i, int j, int k, int l, int i1)
	{
		Uri uri = ContentUris.withAppendedId(shortcutsContentUri, i);
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if (cursor != null)
			if (!cursor.moveToFirst())
			{
				cursor.close();
			} else
			{
				try
				{
					Intent intent = ((ShortcutInfo)builder.newInstance(new CursorDataAdapter(cursor))).getIntent();
					intent.setSourceBounds(new Rect(j, k, l, i1));
					context.startActivity(intent);
				}
				catch (Exception exception)
				{
					exception.printStackTrace();
				}
				cursor.close();
			}
	}

	protected void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		super.onCreate(context1, nativecallbacks);
		context = context1;
		resolver = context1.getContentResolver();
		shortcutsContentUri = com.softspb.shell.data.WidgetsContract.ShortcutInfoContract.getContentUri(context1);
		installShortcutReceiver.addActionListener("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final ShortcutAdapterAndroid this$0;

			public void onAction(Context context2, Intent intent)
			{
				lock.lock();
				Iterator iterator = notInited.iterator();
				do
				{
					if (!iterator.hasNext())
						break;
					ShortcutInfo shortcutinfo = (ShortcutInfo)iterator.next();
					if (init(shortcutinfo))
						iterator.remove();
				} while (true);				
				lock.unlock();				
			}

			
			{
				this$0 = ShortcutAdapterAndroid.this;
//				super();
			}
		});
		context1.registerReceiver(installShortcutReceiver, installShortcutReceiver.getIntentFilter());
	}

	protected void onDestroy(Context context1)
	{
		context1.unregisterReceiver(installShortcutReceiver);
	}

	protected void onStart(int i)
	{
		Cursor cursor;
		adapterToken = i;
		cursor = resolver.query(shortcutsContentUri, null, null, null, null);
		if (cursor != null) 
			{
			lock.lock();
			while( cursor.moveToNext())
			{
				ShortcutInfo shortcutinfo = null;
				try {
					shortcutinfo = (ShortcutInfo)builder.newInstance(new CursorDataAdapter(cursor));
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!init(shortcutinfo))
					notInited.add(shortcutinfo);
			}
			cursor.close();
			lock.unlock();
			}
		else 
			{
			return;
			}

	}

	protected void onStop()
	{
		adapterToken = 0;
		super.onStop();
	}

	public void remove(int i)
	{
		Uri uri = ContentUris.withAppendedId(shortcutsContentUri, i);
		resolver.delete(uri, null, null);
		deleteShortcut(adapterToken, i);
	}



}
