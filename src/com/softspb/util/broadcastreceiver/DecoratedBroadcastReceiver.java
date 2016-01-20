// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.broadcastreceiver;

import android.content.*;
import com.softspb.util.CollectionFactory;
import java.util.*;

public class DecoratedBroadcastReceiver extends BroadcastReceiver
{
	public static interface IActionListener
	{

		public abstract void onAction(Context context, Intent intent);
	}


	private Map listeners;

	public DecoratedBroadcastReceiver()
	{
		listeners = CollectionFactory.newHashMap();
	}

	public DecoratedBroadcastReceiver(String s, IActionListener iactionlistener)
	{
		listeners = CollectionFactory.newHashMap();
		addActionListener(s, iactionlistener);
	}

	public void addActionListener(String s, IActionListener iactionlistener)
	{
		if (s == null)
			throw new IllegalArgumentException("action couldn't be null");
		if (iactionlistener == null)
		{
			throw new IllegalArgumentException("listener couldn't be null");
		} else
		{
			listeners.put(s, iactionlistener);
			return;
		}
	}

	public IntentFilter getIntentFilter()
	{
		IntentFilter intentfilter = new IntentFilter();
		for (Iterator iterator = listeners.keySet().iterator(); iterator.hasNext(); intentfilter.addAction((String)iterator.next()));
		return intentfilter;
	}

	public void onReceive(Context context, Intent intent)
	{
		if (listeners.containsKey(intent.getAction()))
			((IActionListener)listeners.get(intent.getAction())).onAction(context, intent);
	}
}
