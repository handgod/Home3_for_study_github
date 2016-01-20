// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.updateservice.spb;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.softspb.util.DeviceUtil;

public class ClientToken
{

	public static final String QUERY_PARAM_CLIENT_TOKEN = "client_token";
	private static ClientToken instance;
	private final String token;

	private ClientToken(Context context)
	{
		String s;
		String s1;
		String s2;
		String s3;
		s = context.getPackageName();
		s1 = DeviceUtil.getDeviceId(context);
		s2 = null;
		s3 = null;
		String s4;
		PackageInfo packageinfo = null;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(s, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s2 = packageinfo.versionName;
		s4 = Integer.toString(packageinfo.versionCode);
		s3 = s4;
		token = createToken(s, s2, s3, s1);
		return;

	}

	private String createToken(String s, String s1, String s2, String s3)
	{
		return (new StringBuilder()).append(getCode(s)).append('-').append(getCode(s1)).append('-').append(getCode(s2)).append('-').append(getCode(s3)).toString();
	}

	public static ClientToken getInstance(Context context)
	{
		if (instance != null)
		{
			return instance;
		}
		else 
		{
			if (instance == null)
				instance = new ClientToken(context);
		}
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
