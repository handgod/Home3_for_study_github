// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.*;

// Referenced classes of package com.softspb.util:
//			IOHelper

public class DeviceUtil
{

	private static final String DEVICE_ID = "device_id";
	private static final String EMPTY = "empty";

	private DeviceUtil()
	{
	}

	public static String getDeviceId(Context context)
	{
		String s;
		Process process;
		Object obj;
		String s2 = null;
		String s1;
		BufferedReader bufferedreader;
		s = PreferenceManager.getDefaultSharedPreferences(context).getString("device_id", "empty");
		if (s.equals("empty"))
		{
			String s4 = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
			if (!TextUtils.isEmpty(s4))
				s = s4;
		}
		if (s.equals("empty"))
		{
			String s3 = android.provider.Settings.Secure.getString(context.getContentResolver(), "android_id");
			if (!TextUtils.isEmpty(s3))
				s = s3;
		}
		if (s.equals("empty"))
		{
			WifiInfo wifiinfo = ((WifiManager)context.getSystemService("wifi")).getConnectionInfo();
			android.content.SharedPreferences.Editor editor;
		
			if (wifiinfo == null)
				s = "empty";
			else
				s = wifiinfo.getMacAddress();
			if (TextUtils.isEmpty(s))
				s = "empty";
		}
		if (!s.equals("empty"))
			return s;
		process = null;
		obj = null;
		try {
			process = Runtime.getRuntime().exec("getprop ro.serialno");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		try {
			s2 = bufferedreader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (s2 != null)
			s = s2;
		IOHelper.closeSilent(bufferedreader);
		if (process != null)
			process.destroy();
		
		if (s.equals("empty"))
			s1 = "emulator";
		else
			s1 = String.valueOf(Math.abs(s.hashCode()));
		return s1;
	}
}
