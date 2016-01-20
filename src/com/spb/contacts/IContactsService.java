// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.os.*;

// Referenced classes of package com.spb.contacts:
//			IContactsServiceCallback

public interface IContactsService
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements IContactsService
	{
		private static class Proxy
			implements IContactsService
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public void crash()
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsService");
				mRemote.transact(6, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public String getInterfaceDescriptor()
			{
				return "com.spb.contacts.IContactsService";
			}

			public void registerCallback(IContactsServiceCallback icontactsservicecallback)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				IBinder ibinder;
				parcel.writeInterfaceToken("com.spb.contacts.IContactsService");
				if (icontactsservicecallback == null)
					return;
				ibinder = icontactsservicecallback.asBinder();

				parcel.writeStrongBinder(ibinder);
				mRemote.transact(4, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void reloadBirthdays(int i, boolean flag)
				throws RemoteException
			{
				int j;
				Parcel parcel;
				Parcel parcel1;
				j = 1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsService");
				parcel.writeInt(i);
				if (!flag)
					j = 0;
				parcel.writeInt(j);
				mRemote.transact(1, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void reloadContact(int i)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsService");
				parcel.writeInt(i);
				mRemote.transact(3, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void reloadContacts(int i)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsService");
				parcel.writeInt(i);
				mRemote.transact(2, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void unregisterCallback(IContactsServiceCallback icontactsservicecallback)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				IBinder ibinder;
				parcel.writeInterfaceToken("com.spb.contacts.IContactsService");
				if (icontactsservicecallback == null)
					return;
				ibinder = icontactsservicecallback.asBinder();

				parcel.writeStrongBinder(ibinder);
				mRemote.transact(5, parcel, parcel1, 0);
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


		private static final String DESCRIPTOR = "com.spb.contacts.IContactsService";
		static final int TRANSACTION_crash = 6;
		static final int TRANSACTION_registerCallback = 4;
		static final int TRANSACTION_reloadBirthdays = 1;
		static final int TRANSACTION_reloadContact = 3;
		static final int TRANSACTION_reloadContacts = 2;
		static final int TRANSACTION_unregisterCallback = 5;

		public static IContactsService asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.spb.contacts.IContactsService");
				if (iinterface != null && (iinterface instanceof IContactsService))
					obj = (IContactsService)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((IContactsService) (obj));
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
				parcel.enforceInterface("com.spb.contacts.IContactsService");
				int k = parcel.readInt();
				boolean flag1;
				if (parcel.readInt() != 0)
					flag1 = flag;
				else
					flag1 = false;
				reloadBirthdays(k, flag1);
				parcel1.writeNoException();
				break;
			case 2:
				parcel.enforceInterface("com.spb.contacts.IContactsService");
				reloadContacts(parcel.readInt());
				parcel1.writeNoException();
				break;
			case 3:
				parcel.enforceInterface("com.spb.contacts.IContactsService");
				reloadContact(parcel.readInt());
				parcel1.writeNoException();
				break;
			case 4:
				parcel.enforceInterface("com.spb.contacts.IContactsService");
				registerCallback(IContactsServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
				parcel1.writeNoException();
				break;
			case 5:
				parcel.enforceInterface("com.spb.contacts.IContactsService");
				unregisterCallback(IContactsServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
				parcel1.writeNoException();
				break;
			case 6:
				parcel.enforceInterface("com.spb.contacts.IContactsService");
				crash();
				parcel1.writeNoException();
				break;
			case 1598968902:
				parcel1.writeString("com.spb.contacts.IContactsService");
				break;
			default:
				flag = super.onTransact(i, parcel, parcel1, j);
				break;
			}
			return flag;
		}

		public Stub()
		{
			attachInterface(this, "com.spb.contacts.IContactsService");
		}
	}


	public abstract void crash()
		throws RemoteException;

	public abstract void registerCallback(IContactsServiceCallback icontactsservicecallback)
		throws RemoteException;

	public abstract void reloadBirthdays(int i, boolean flag)
		throws RemoteException;

	public abstract void reloadContact(int i)
		throws RemoteException;

	public abstract void reloadContacts(int i)
		throws RemoteException;

	public abstract void unregisterCallback(IContactsServiceCallback icontactsservicecallback)
		throws RemoteException;
}
