// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.os.*;

public class DelayedHandler extends Handler
{

	static final int MSG_DO_UPDATE = 10002;
	static final int MSG_INCOMING_UPDATE = 10001;
	long thresholdMs;

	public DelayedHandler(long l)
	{
		thresholdMs = l;
	}

	public DelayedHandler(Looper looper, long l)
	{
		super(looper);
		thresholdMs = l;
	}

	public void handleDelayed(Runnable runnable)
	{
		sendMessage(Message.obtain(this, 10001, runnable));
	}

	public void handleMessage(Message message)
	{
		switch (message.what) {
		case 10001:
			sendMessageDelayed(Message.obtain(this, 10002, message.obj), thresholdMs);
			break;

		case 10002:
			if (!hasMessages(10002, message.obj))
				((Runnable)message.obj).run();
			break;
		default:
			break;
		}
		return;
	}
}
