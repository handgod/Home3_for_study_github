// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.regex.Pattern;

class ResponseData
{

	public String extra;
	public int nonce;
	public String packageName;
	public int responseCode;
	public long timestamp;
	public String userId;
	public String versionCode;

	ResponseData()
	{
	}

	public static ResponseData parse(String s)
	{
		android.text.TextUtils.SimpleStringSplitter simplestringsplitter = new android.text.TextUtils.SimpleStringSplitter(':');
		simplestringsplitter.setString(s);
		Iterator iterator = simplestringsplitter.iterator();
		if (!iterator.hasNext())
			throw new IllegalArgumentException("Blank response.");
		String s1 = (String)iterator.next();
		String s2 = "";
		if (iterator.hasNext())
			s2 = (String)iterator.next();
		String as[] = TextUtils.split(s1, Pattern.quote("|"));
		if (as.length < 6)
		{
			throw new IllegalArgumentException("Wrong number of fields.");
		} else
		{
			ResponseData responsedata = new ResponseData();
			responsedata.extra = s2;
			responsedata.responseCode = Integer.parseInt(as[0]);
			responsedata.nonce = Integer.parseInt(as[1]);
			responsedata.packageName = as[2];
			responsedata.versionCode = as[3];
			responsedata.userId = as[4];
			responsedata.timestamp = Long.parseLong(as[5]);
			return responsedata;
		}
	}

	public String toString()
	{
		Object aobj[] = new Object[6];
		aobj[0] = Integer.valueOf(responseCode);
		aobj[1] = Integer.valueOf(nonce);
		aobj[2] = packageName;
		aobj[3] = versionCode;
		aobj[4] = userId;
		aobj[5] = Long.valueOf(timestamp);
		return TextUtils.join("|", aobj);
	}
}
