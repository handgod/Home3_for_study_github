// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util.log;

import android.content.*;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.softspb.util.log:
//			Loggers, Logger

public class SDCardReceiver extends BroadcastReceiver
{
	public static interface SDCardListener
	{

		public abstract void onReceive(Intent intent);
	}


	private static final ArrayList listeners = new ArrayList();
	Logger logger;

	public SDCardReceiver()
	{
		logger = Loggers.getLogger(SDCardReceiver.class.getName());
	}

	private static void notifyListeners(Intent intent)
	{
		ArrayList arraylist = listeners;
		synchronized (arraylist) {
			for (Iterator iterator = listeners.iterator(); iterator.hasNext(); ((SDCardListener)iterator.next()).onReceive(intent));
		}
	}

	public static void registerListener(SDCardListener sdcardlistener)
	{
		ArrayList arraylist = listeners;
		synchronized (arraylist) {
			if (!listeners.contains(sdcardlistener))
				listeners.add(sdcardlistener);
		}
		return;
	}

	public static void unregisterListener(SDCardListener sdcardlistener)
	{
		ArrayList arraylist = listeners;
		synchronized (arraylist) {
			listeners.remove(sdcardlistener);
		}		
		return;
	}

	public void onReceive(Context context, Intent intent)
	{
		logger.d((new StringBuilder()).append("received SD card intent: action=").append(intent.getAction()).append(" data=").append(intent.getData()).toString());
		notifyListeners(intent);
	}

}
