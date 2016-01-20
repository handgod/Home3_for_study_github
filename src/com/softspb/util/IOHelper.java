// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.util.Log;
import java.io.Closeable;
import java.io.IOException;

public class IOHelper
{

	private static final String LOG_TAG = "IOHelper";

	public IOHelper()
	{
	}

	public static void closeSilent(Closeable closeable)
	{
		if (closeable != null)
			try {
				closeable.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
