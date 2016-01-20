// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.simplemedia;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

// Referenced classes of package com.softspb.shell.adapters.simplemedia:
//			SimpleMediaAdapterAndroid

class SimpleSecReceiverAction
	implements com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener
{

	private static final String EXTRA_IS_STOP = "isStop";
	private static final String EXTRA_MEDIAURI = "mediauri";
	private static final String EXTRA_PLAYING = "playing";
	private SimpleMediaAdapterAndroid adapter;

	public SimpleSecReceiverAction(SimpleMediaAdapterAndroid simplemediaadapterandroid)
	{
		adapter = simplemediaadapterandroid;
	}

	public void onAction(Context context, Intent intent)
	{
		boolean flag;
		boolean flag1;
		Object obj;
		flag = intent.getBooleanExtra("playing", false);
		flag1 = intent.getBooleanExtra("isStop", true);
		obj = intent.getExtras().get("mediauri");
		if (obj instanceof Uri)
		{
		Uri uri = (Uri)obj;
		if (flag1)
		{
			adapter.onPlayStateUpdated(0);
			return;
		}
		Cursor cursor;
		if (flag)
			adapter.onPlayStateUpdated(2);
		else
			adapter.onPlayStateUpdated(1);
		cursor = context.getContentResolver().query(uri, null, null, null, null);
		if (cursor.moveToFirst())
		{
			int i = cursor.getColumnIndex("artist");
			int j = cursor.getColumnIndex("title");
			if (i == -1 || j == -1)
			{
				cursor.close();
				return;
			}
			String s = cursor.getString(i);
			String s1 = cursor.getString(j);
			if (s == null)
				s = "";
			if (s1 == null)
				s1 = "";
			adapter.onMediaInfoUpdated(s, s1);
		}
		cursor.close();
		}
		else {
			adapter.onPlaybackCompleted();
		}
		return;
	}
}
