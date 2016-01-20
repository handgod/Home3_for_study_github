// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.format.Time;
import android.util.Log;

// Referenced classes of package com.spb.contacts:
//			IContactsServiceCallback

class ContactsCallbacksHelper
{

	private final RemoteCallbackList callbackList = new RemoteCallbackList();
	private int callbacksCount;

	ContactsCallbacksHelper()
	{
		callbacksCount = 0;
	}

	/**
	 * @deprecated Method hasRegisteredCallbacks is deprecated
	 */

	synchronized boolean hasRegisteredCallbacks()
	{

		int i = callbacksCount;
		boolean flag;
		if (i > 0)
			flag = true;
		else
			flag = false;

		return flag;
	}

	/**
	 * @deprecated Method notifyBirthdayDeleted is deprecated
	 */

	synchronized void notifyBirthdayDeleted(int i)
	{

		int j = callbackList.beginBroadcast();
		int k = 0;
		while (k < j) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(k)).onBirthdayDeleted(i);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
			}
			k++;
		}
		callbackList.finishBroadcast();

		return;

	}

	/**
	 * @deprecated Method notifyBirthdayUpdated is deprecated
	 */

	synchronized void notifyBirthdayUpdated(int i, int j, int k, int l, int i1, int j1)
	{

		int k1 = callbackList.beginBroadcast();
		int l1 = 0;
		while (l1 < k1) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(l1)).onBirthdayUpdated(i, j, k, l, i1, j1);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
			
			}
			l1++;
		}
		callbackList.finishBroadcast();
		return;

	}

	/**
	 * @deprecated Method notifyConnectionDeleted is deprecated
	 */

	synchronized void notifyConnectionDeleted(int i, int j, int k)
	{

		int l = callbackList.beginBroadcast();
		int i1 = 0;
		while (i1 < l) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(i1)).onConnectionDeleted(i, j, k);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
				
			}
			i1++;
		}
		callbackList.finishBroadcast();

		return;

	}

	/**
	 * @deprecated Method notifyConnectionUpdated is deprecated
	 */

	synchronized void notifyConnectionUpdated(int i, int j, String s, int k, String s1, String s2, int l)
	{

		int i1 = callbackList.beginBroadcast();
		int j1 = 0;
		while (j1 < i1) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(j1)).onConnectionUpdated(i, j, s, k, s1, s2, l);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
	
			}
			j1++;
		}
		callbackList.finishBroadcast();

		return;

	}

	/**
	 * @deprecated Method notifyContactDeleted is deprecated
	 */

	synchronized void notifyContactDeleted(int i, int j)
	{

		int k = callbackList.beginBroadcast();
		int l = 0;
		while (l < k) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(l)).onContactDeleted(i, j);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
			
			}
			l++;
		}
		callbackList.finishBroadcast();

	}

	/**
	 * @deprecated Method notifyContactPhotoUpdated is deprecated
	 */

	synchronized void notifyContactPhotoUpdated(int i, int j, int k)
	{

		int l = callbackList.beginBroadcast();
		int i1 = 0;
		while (i1 < l) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(i1)).onContactPhotoUpdated(i, j, k);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
				
			}
			i1++;
		}
		callbackList.finishBroadcast();

	}
	/** @deprecated */
	  synchronized void notifyContactUpdated(int paramInt1, String paramString1, String paramString2, boolean paramBoolean, int paramInt2, int paramInt3)
	  {
	
	    try
	    {
	      String str1 = "notifyContactUpdated: contactId=" + paramInt1 + " >>>";
	      int i = Log.d("CallbacksHelper", str1);
	      int j = this.callbackList.beginBroadcast();
	      int k = j;
	      int m = 0;
	      while (true)
	        if (m < k)
	          try
	          {
	            IContactsServiceCallback localIContactsServiceCallback = (IContactsServiceCallback)this.callbackList.getBroadcastItem(m);
	            int n = paramInt1;
	            String str2 = paramString1;
	            String str3 = paramString2;
	            boolean bool = paramBoolean;
	            int i1 = paramInt2;
	            int i2 = paramInt3;
	            localIContactsServiceCallback.onContactUpdated(n, str2, str3, bool, i1, i2);
	            String str4 = "notifyContactUpdated: contactId=" + paramInt1 + " <<< done";
	            int i3 = Log.d("CallbacksHelper", str4);
	            m += 1;
	          }
	          catch (RemoteException localRemoteException)
	          {
	            while (true)
	            {
	              String str5 = "notifyContactUpdated: contactId=" + paramInt1 + " <<< error: " + localRemoteException;
	              int i4 = Log.e("CallbacksHelper", str5, localRemoteException);
	            }
	          }
	    }
	    finally
	    {
		    this.callbackList.finishBroadcast();
	    }
	  }	

	/**
	 * @deprecated Method notifyEventDeleted is deprecated
	 */

	synchronized void notifyEventDeleted(int i, int j, int k)
	{

		int l = callbackList.beginBroadcast();
		int i1 = 0;
		while (i1 < l) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(i1)).onEventDeleted(i, j, k);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
				
			}
			i1++;
		}
		callbackList.finishBroadcast();

	}

	/**
	 * @deprecated Method notifyEventUpdated is deprecated
	 */

	synchronized void notifyEventUpdated(int i, int j, int k, Time time, int l)
	{

		int i1 = callbackList.beginBroadcast();
		int j1 = 0;
		while (j1 < i1) 
		{
			Exception exception;
			try
			{
				long l1 = time.toMillis(false);
				((IContactsServiceCallback)callbackList.getBroadcastItem(j1)).onEventUpdated(i, j, k, l1, l);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
				
			}
			j1++;
		}
		callbackList.finishBroadcast();

	}

	/**
	 * @deprecated Method notifyFinishedReload is deprecated
	 */

	synchronized void notifyFinishedReload(int i)
	{

		int j = callbackList.beginBroadcast();
		int k = 0;
		while (k < j) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(k)).onFinishedReload(i);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
				
			}
			k++;
		}
		callbackList.finishBroadcast();

	}

	/**
	 * @deprecated Method notifyFinishedReloadingBirthdays is deprecated
	 */

	synchronized void notifyFinishedReloadingBirthdays()
	{

		int i = callbackList.beginBroadcast();
		int j = 0;
		while (j < i) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(j)).onFinishedReloadingBirthdays();
			}
			catch (RemoteException remoteexception) { }
			finally
			{
				
			}
			j++;
		}
		callbackList.finishBroadcast();

	}

	/**
	 * @deprecated Method notifyFinishedUpdatingContact is deprecated
	 */

	synchronized void notifyFinishedUpdatingContact(int i, int j)
	{

		int k = callbackList.beginBroadcast();
		int l = 0;
		while (l < k) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(l)).onFinishedUpdatingContact(i, j);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
				
			}
			l++;
		}

	}

	/**
	 * @deprecated Method notifyStartedReload is deprecated
	 */

	synchronized void notifyStartedReload(int i)
	{
		int j = callbackList.beginBroadcast();
		int k = 0;
		while (k < j) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(k)).onStartedReload(i);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
				
			}
			k++;
		}
		callbackList.finishBroadcast();

	}

	/**
	 * @deprecated Method notifyStartedReloadingBirthdays is deprecated
	 */

	synchronized void notifyStartedReloadingBirthdays()
	{

		int i = callbackList.beginBroadcast();
		int j = 0;
		while (j < i) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(j)).onStartedReloadingBirthdays();
			}
			catch (RemoteException remoteexception) { }
			finally
			{
			
			}
			j++;
		}
		callbackList.finishBroadcast();

	}

	/**
	 * @deprecated Method notifyStartedUpdatingContact is deprecated
	 */

	synchronized void notifyStartedUpdatingContact(int i, int j)
	{

		int k = callbackList.beginBroadcast();
		int l = 0;
		while (l < k) 
		{
			Exception exception;
			try
			{
				((IContactsServiceCallback)callbackList.getBroadcastItem(l)).onStartedUpdatingContact(i, j);
			}
			catch (RemoteException remoteexception) { }
			finally
			{
			
			}
			l++;
		}
		callbackList.finishBroadcast();

	}
	 /** @deprecated */
	synchronized void notifyStructuredNameChanged(int paramInt, String paramString1, String paramString2)
	  {
	    try
	    {
	      String str1 = "notifyStructuredNameUpdated: contactId=" + paramInt + " firstName=" + paramString1 + " lastName=" + paramString2;
	      int i = Log.d("CallbacksHelper", str1);
	      int j = this.callbackList.beginBroadcast();
	      int k = j;
	      int m = 0;
	      while (true)
	        if (m < k)
	          try
	          {
	            ((IContactsServiceCallback)this.callbackList.getBroadcastItem(m)).onStructuredNameUpdated(paramInt, paramString1, paramString2);
	            String str2 = "notifyStructuredNameUpdated: contactId=" + paramInt + " <<< done";
	            int n = Log.d("CallbacksHelper", str2);
	            m += 1;
	          }
	          catch (RemoteException localRemoteException)
	          {
	            while (true)
	            {
	              String str3 = "notifyStructuredNameUpdated: contactId=" + paramInt + " <<< error: " + localRemoteException;
	              int i1 = Log.e("CallbacksHelper", str3, localRemoteException);
	            }
	          }
	    }
	    finally
	    {
	    	 this.callbackList.finishBroadcast();
	    }
	   

	  }

	/**
	 * @deprecated Method register is deprecated
	 */

	synchronized void register(IContactsServiceCallback icontactsservicecallback)
	{
		
		if (callbackList.register(icontactsservicecallback))
			callbacksCount = 1 + callbacksCount;
	}

	/**
	 * @deprecated Method unregister is deprecated
	 */

	synchronized void unregister(IContactsServiceCallback icontactsservicecallback)
	{

		if (callbackList.unregister(icontactsservicecallback))
			callbacksCount = -1 + callbacksCount;
	}
}
