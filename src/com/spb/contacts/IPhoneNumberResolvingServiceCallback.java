// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.os.*;

public interface IPhoneNumberResolvingServiceCallback
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements IPhoneNumberResolvingServiceCallback
	{
		private static class Proxy
			implements IPhoneNumberResolvingServiceCallback
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public String getInterfaceDescriptor()
			{
				return "com.spb.contacts.IPhoneNumberResolvingServiceCallback";
			}

			public void onResolvedPhonesChanged(int i)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IPhoneNumberResolvingServiceCallback");
				parcel.writeInt(i);
				mRemote.transact(1, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;
				
			}

			Proxy(IBinder ibinder)
			{
				mRemote = ibinder;
			}
		}


		private static final String DESCRIPTOR = "com.spb.contacts.IPhoneNumberResolvingServiceCallback";
		static final int TRANSACTION_onResolvedPhonesChanged = 1;

		public static IPhoneNumberResolvingServiceCallback asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.spb.contacts.IPhoneNumberResolvingServiceCallback");
				if (iinterface != null && (iinterface instanceof IPhoneNumberResolvingServiceCallback))
					obj = (IPhoneNumberResolvingServiceCallback)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((IPhoneNumberResolvingServiceCallback) (obj));
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
				flag = super.onTransact(i, parcel, parcel1, j);
				break;
			case 1598968902:
				parcel.enforceInterface("com.spb.contacts.IPhoneNumberResolvingServiceCallback");
				onResolvedPhonesChanged(parcel.readInt());
				parcel1.writeNoException();
				break;
			default:
				break;
			}
			return flag;
		}

		public Stub()
		{
			attachInterface(this, "com.spb.contacts.IPhoneNumberResolvingServiceCallback");
		}
	}


	public abstract void onResolvedPhonesChanged(int i)
		throws RemoteException;
}
