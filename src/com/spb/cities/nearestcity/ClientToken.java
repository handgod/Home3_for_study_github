// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.nearestcity;

import android.content.Context;
import android.telephony.TelephonyManager;

public class ClientToken
{

	public static final String QUERY_PARAM_CLIENT_TOKEN = "client_token";
	private static ClientToken instance;
	private final String token;

	private ClientToken(Context context)
	{
		String s = context.getPackageName();
		String s1 = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append(getCode(s));
		stringbuilder.append('-').append(getCode(s1));
		token = stringbuilder.toString();
	}

	public static ClientToken getInstance(Context context)
	{

		if (instance == null)
			instance = new ClientToken(context);

		return instance;

	}

	long getCode(String s)
	{
		long l;
		if (s == null)
		{
			l = 0L;
		} else
		{
			int i = s.hashCode();
			if (i >= 0)
				l = i;
			else
				l = 0x100000000L + (long)i;
		}
		return l;
	}

	public String getToken()
	{
		return token;
	}
}
