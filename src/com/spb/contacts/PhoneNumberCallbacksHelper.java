// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

// Referenced classes of package com.spb.contacts:
//			IPhoneNumberResolvingServiceCallback

public class PhoneNumberCallbacksHelper
{

	private final RemoteCallbackList callbackList = new RemoteCallbackList();

	public PhoneNumberCallbacksHelper()
	{
	}

	void notifyResolvedPhonesChanged(int i)
	{
		int j = callbackList.beginBroadcast();
		int k = 0;
		while (k < j) 
		{
			try
			{
				((IPhoneNumberResolvingServiceCallback)callbackList.getBroadcastItem(k)).onResolvedPhonesChanged(i);
			}
			catch (RemoteException remoteexception) { }
			k++;
		}
		callbackList.finishBroadcast();
	}

	void register(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
	{
		callbackList.register(iphonenumberresolvingservicecallback);
	}

	void unregister(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
	{
		callbackList.unregister(iphonenumberresolvingservicecallback);
	}
}
