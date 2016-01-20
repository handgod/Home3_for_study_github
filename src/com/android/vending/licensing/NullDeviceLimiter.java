// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;


// Referenced classes of package com.android.vending.licensing:
//			DeviceLimiter

public class NullDeviceLimiter
	implements DeviceLimiter
{

	public NullDeviceLimiter()
	{
	}

	public Policy.LicenseResponse isDeviceAllowed(String s)
	{
		return Policy.LicenseResponse.LICENSED;
	}
}
