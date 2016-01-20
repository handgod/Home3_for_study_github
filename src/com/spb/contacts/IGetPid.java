// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.os.*;

public interface IGetPid
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements IGetPid
	{
		private static class Proxy
			implements IGetPid
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public String getInterfaceDescriptor()
			{
				return "com.spb.contacts.IGetPid";
			}

			public int getPid()
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				int i;
				parcel.writeInterfaceToken("com.spb.contacts.IGetPid");
				mRemote.transact(1, parcel, parcel1, 0);
				parcel1.readException();
				i = parcel1.readInt();
				parcel1.recycle();
				parcel.recycle();
				return i;

			}

			Proxy(IBinder ibinder)
			{
				mRemote = ibinder;
			}
		}


		private static final String DESCRIPTOR = "com.spb.contacts.IGetPid";
		static final int TRANSACTION_getPid = 1;

		public static IGetPid asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.spb.contacts.IGetPid");
				if (iinterface != null && (iinterface instanceof IGetPid))
					obj = (IGetPid)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((IGetPid) (obj));
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
				parcel.enforceInterface("com.spb.contacts.IGetPid");
				int k = getPid();
				parcel1.writeNoException();
				parcel1.writeInt(k);
				break;
			case 1598968902:
				parcel1.writeString("com.spb.contacts.IGetPid");
				break;
			default:
				flag = super.onTransact(i, parcel, parcel1, j);
				break;
			}
			return flag;
		}

		public Stub()
		{
			attachInterface(this, "com.spb.contacts.IGetPid");
		}
	}


	public abstract int getPid()
		throws RemoteException;
}
