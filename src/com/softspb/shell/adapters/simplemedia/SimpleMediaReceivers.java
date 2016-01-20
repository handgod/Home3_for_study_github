// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.simplemedia;

import android.content.Context;
import android.content.Intent;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;

// Referenced classes of package com.softspb.shell.adapters.simplemedia:
//			MediaReceiver, SimpleMediaAdapterAndroid

class SimpleMediaReceivers
{
	private static class PlayStateAction
		implements com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener
	{

		private final SimpleMediaAdapterAndroid simpleAdapter;

		public void onAction(Context context, Intent intent)
		{
			if (intent.getBooleanExtra("playing", false))
				simpleAdapter.onPlayStateUpdated(2);
			else
				simpleAdapter.onPlayStateUpdated(1);
		}

		public PlayStateAction(SimpleMediaAdapterAndroid simplemediaadapterandroid)
		{
			simpleAdapter = simplemediaadapterandroid;
		}
	}

	private static class MetaChangedListener extends MetaChangedAction
	{

		public void onAction(Context context, Intent intent)
		{
			super.onAction(context, intent);
		}

		public MetaChangedListener(SimpleMediaAdapterAndroid simplemediaadapterandroid)
		{
			super(simplemediaadapterandroid);
		}
	}

	private static class MetaChangedAction
		implements com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener
	{

		protected final SimpleMediaAdapterAndroid simpleAdapter;

		private String getSafeStringExtra(Intent intent, String s)
		{
			String s1 = "";
			if (intent.hasExtra(s)) 
			{
				s1 = intent.getStringExtra(s);
				if (s1 == null)
					s1 = "";
			}
			else
				s1 = "";
			return s1;
		}

		public void onAction(Context context, Intent intent)
		{
			String s = getSafeStringExtra(intent, "artist");
			getSafeStringExtra(intent, "album");
			String s1 = getSafeStringExtra(intent, "track");
			simpleAdapter.onMediaInfoUpdated(s, s1);
		}

		public MetaChangedAction(SimpleMediaAdapterAndroid simplemediaadapterandroid)
		{
			simpleAdapter = simplemediaadapterandroid;
		}
	}

	private static class PlayBackAction
		implements com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener
	{

		private final SimpleMediaAdapterAndroid simpleAdapter;

		public void onAction(Context context, Intent intent)
		{
			simpleAdapter.onPlaybackCompleted();
		}

		public PlayBackAction(SimpleMediaAdapterAndroid simplemediaadapterandroid)
		{
			simpleAdapter = simplemediaadapterandroid;
		}
	}

	private static class ServicedPlayStateAction
		implements com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener
	{

		private final SimpleMediaAdapterAndroid simpleAdapter;

		public void onAction(Context context, Intent intent)
		{
			simpleAdapter.updatePlayState();
		}

		public ServicedPlayStateAction(SimpleMediaAdapterAndroid simplemediaadapterandroid)
		{
			simpleAdapter = simplemediaadapterandroid;
		}
	}


	SimpleMediaReceivers()
	{
	}

	public static DecoratedBroadcastReceiver getGeneralMediaReceiver(SimpleMediaAdapterAndroid simplemediaadapterandroid, String s)
	{
		return new MediaReceiver(s, new PlayBackAction(simplemediaadapterandroid), new MetaChangedListener(simplemediaadapterandroid), new PlayStateAction(simplemediaadapterandroid));
	}

	public static DecoratedBroadcastReceiver getMediaReceiverWithService(SimpleMediaAdapterAndroid simplemediaadapterandroid, String s)
	{
		return new MediaReceiver(s, new PlayBackAction(simplemediaadapterandroid), new MetaChangedAction(simplemediaadapterandroid), new ServicedPlayStateAction(simplemediaadapterandroid));
	}
}
