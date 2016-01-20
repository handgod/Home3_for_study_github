// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import android.os.*;

public interface IBrowserServiceCallback
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements IBrowserServiceCallback
	{
		private static class Proxy
			implements IBrowserServiceCallback
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public String getInterfaceDescriptor()
			{
				return "com.softspb.shell.browser.service.IBrowserServiceCallback";
			}

			public void onBookmarkDeleted(int i)
				throws RemoteException
			{
				Parcel parcel = Parcel.obtain();
				parcel.writeInterfaceToken("com.softspb.shell.browser.service.IBrowserServiceCallback");
				parcel.writeInt(i);
				mRemote.transact(2, parcel, null, 1);
				parcel.recycle();
				return;
			
			}

			public void onBookmarkUpdated(int i, String s, String s1)
				throws RemoteException
			{
				Parcel parcel = Parcel.obtain();
				parcel.writeInterfaceToken("com.softspb.shell.browser.service.IBrowserServiceCallback");
				parcel.writeInt(i);
				parcel.writeString(s);
				parcel.writeString(s1);
				mRemote.transact(1, parcel, null, 1);
				parcel.recycle();
				return;
		
			}

			Proxy(IBinder ibinder)
			{
				mRemote = ibinder;
			}
		}


		private static final String DESCRIPTOR = "com.softspb.shell.browser.service.IBrowserServiceCallback";
		static final int TRANSACTION_onBookmarkDeleted = 2;
		static final int TRANSACTION_onBookmarkUpdated = 1;

		public static IBrowserServiceCallback asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.softspb.shell.browser.service.IBrowserServiceCallback");
				if (iinterface != null && (iinterface instanceof IBrowserServiceCallback))
					obj = (IBrowserServiceCallback)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((IBrowserServiceCallback) (obj));
		}

		public IBinder asBinder()
		{
			return this;
		}

		public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
			throws RemoteException
		{
			boolean flag = true;
			switch (i) {
			case 1:
				parcel.enforceInterface("com.softspb.shell.browser.service.IBrowserServiceCallback");
				onBookmarkUpdated(parcel.readInt(), parcel.readString(), parcel.readString());
				break;
			case 2:
				parcel.enforceInterface("com.softspb.shell.browser.service.IBrowserServiceCallback");
				onBookmarkDeleted(parcel.readInt());
				break;
			case 1598968902:
				parcel1.writeString("com.softspb.shell.browser.service.IBrowserServiceCallback");
				break;
			default:
				break;
			}
			flag = super.onTransact(i, parcel, parcel1, j);
			return flag;
		}

		public Stub()
		{
			attachInterface(this, "com.softspb.shell.browser.service.IBrowserServiceCallback");
		}
	}


	public abstract void onBookmarkDeleted(int i)
		throws RemoteException;

	public abstract void onBookmarkUpdated(int i, String s, String s1)
		throws RemoteException;
}
