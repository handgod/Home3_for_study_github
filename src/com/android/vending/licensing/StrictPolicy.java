// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;


// Referenced classes of package com.android.vending.licensing:
//			Policy, PreferenceObfuscator, ResponseData

public class StrictPolicy
	implements Policy
{

	private static final String TAG = "StrictPolicy";
	private Policy.LicenseResponse mLastResponse;
	private PreferenceObfuscator mPreferences;

	public StrictPolicy()
	{
		mLastResponse = Policy.LicenseResponse.RETRY;
	}

	public boolean allowAccess()
	{
		boolean flag;
		if (mLastResponse == Policy.LicenseResponse.LICENSED)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public void processServerResponse(Policy.LicenseResponse licenseresponse, ResponseData responsedata)
	{
		mLastResponse = licenseresponse;
	}
}
