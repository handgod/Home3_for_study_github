// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.os.*;

public interface IContactsServiceCallback
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements IContactsServiceCallback
	{
		private static class Proxy
			implements IContactsServiceCallback
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public String getInterfaceDescriptor()
			{
				return "com.spb.contacts.IContactsServiceCallback";
			}

			public void onBirthdayDeleted(int i)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				mRemote.transact(3, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onBirthdayUpdated(int i, int j, int k, int l, int i1, int j1)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				parcel.writeInt(k);
				parcel.writeInt(l);
				parcel.writeInt(i1);
				parcel.writeInt(j1);
				mRemote.transact(2, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onConnectionDeleted(int i, int j, int k)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				parcel.writeInt(k);
				mRemote.transact(8, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onConnectionUpdated(int i, int j, String s, int k, String s1, String s2, int l)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				parcel.writeString(s);
				parcel.writeInt(k);
				parcel.writeString(s1);
				parcel.writeString(s2);
				parcel.writeInt(l);
				mRemote.transact(13, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();

			}

			public void onContactDeleted(int i, int j)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				mRemote.transact(6, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onContactPhotoUpdated(int i, int j, int k)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				parcel.writeInt(k);
				mRemote.transact(5, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onContactUpdated(int i, String s, String s1, boolean flag, int j, int k)
				throws RemoteException
			{
				int l;
				Parcel parcel;
				Parcel parcel1;
				l = 0;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeString(s);
				parcel.writeString(s1);
				if (flag)
					l = 1;
				parcel.writeInt(l);
				parcel.writeInt(j);
				parcel.writeInt(k);
				mRemote.transact(10, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onEventDeleted(int i, int j, int k)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				parcel.writeInt(k);
				mRemote.transact(7, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onEventUpdated(int i, int j, int k, long l, int i1)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				parcel.writeInt(k);
				parcel.writeLong(l);
				parcel.writeInt(i1);
				mRemote.transact(14, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onFinishedReload(int i)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				mRemote.transact(16, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onFinishedReloadingBirthdays()
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				mRemote.transact(4, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onFinishedUpdatingContact(int i, int j)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				mRemote.transact(12, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onStartedReload(int i)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				mRemote.transact(15, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onStartedReloadingBirthdays()
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				mRemote.transact(1, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onStartedUpdatingContact(int i, int j)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeInt(j);
				mRemote.transact(9, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void onStructuredNameUpdated(int i, String s, String s1)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IContactsServiceCallback");
				parcel.writeInt(i);
				parcel.writeString(s);
				parcel.writeString(s1);
				mRemote.transact(11, parcel, parcel1, 0);
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


		private static final String DESCRIPTOR = "com.spb.contacts.IContactsServiceCallback";
		static final int TRANSACTION_onBirthdayDeleted = 3;
		static final int TRANSACTION_onBirthdayUpdated = 2;
		static final int TRANSACTION_onConnectionDeleted = 8;
		static final int TRANSACTION_onConnectionUpdated = 13;
		static final int TRANSACTION_onContactDeleted = 6;
		static final int TRANSACTION_onContactPhotoUpdated = 5;
		static final int TRANSACTION_onContactUpdated = 10;
		static final int TRANSACTION_onEventDeleted = 7;
		static final int TRANSACTION_onEventUpdated = 14;
		static final int TRANSACTION_onFinishedReload = 16;
		static final int TRANSACTION_onFinishedReloadingBirthdays = 4;
		static final int TRANSACTION_onFinishedUpdatingContact = 12;
		static final int TRANSACTION_onStartedReload = 15;
		static final int TRANSACTION_onStartedReloadingBirthdays = 1;
		static final int TRANSACTION_onStartedUpdatingContact = 9;
		static final int TRANSACTION_onStructuredNameUpdated = 11;

		public static IContactsServiceCallback asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.spb.contacts.IContactsServiceCallback");
				if (iinterface != null && (iinterface instanceof IContactsServiceCallback))
					obj = (IContactsServiceCallback)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((IContactsServiceCallback) (obj));
		}

		public IBinder asBinder()
		{
			return this;
		}

		public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
			throws RemoteException
		{
			boolean flag;
			switch (i) {
			case 1:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onStartedReloadingBirthdays();
				parcel1.writeNoException();
				flag = true;
				break;
			case 2:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onBirthdayUpdated(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;			
				break;
			case 3:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onBirthdayDeleted(parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 4:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onFinishedReloadingBirthdays();
				parcel1.writeNoException();
				flag = true;
				break;
			case 5:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onContactPhotoUpdated(parcel.readInt(), parcel.readInt(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 6:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onContactDeleted(parcel.readInt(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 7:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onEventDeleted(parcel.readInt(), parcel.readInt(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 8:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onConnectionDeleted(parcel.readInt(), parcel.readInt(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 9:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onStartedUpdatingContact(parcel.readInt(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 10:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				int k = parcel.readInt();
				String s = parcel.readString();
				String s1 = parcel.readString();
				boolean flag1;
				if (parcel.readInt() != 0)
					flag1 = true;
				else
					flag1 = false;
				onContactUpdated(k, s, s1, flag1, parcel.readInt(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 11:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onStructuredNameUpdated(parcel.readInt(), parcel.readString(), parcel.readString());
				parcel1.writeNoException();
				flag = true;
				break;
			case 12:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onFinishedUpdatingContact(parcel.readInt(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 13:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onConnectionUpdated(parcel.readInt(), parcel.readInt(), parcel.readString(), parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 14:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onEventUpdated(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readLong(), parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 15:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onStartedReload(parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 16:
				parcel.enforceInterface("com.spb.contacts.IContactsServiceCallback");
				onFinishedReload(parcel.readInt());
				parcel1.writeNoException();
				flag = true;
				break;
			case 1598968902:
				parcel1.writeString("com.spb.contacts.IContactsServiceCallback");
				flag = true;
				break;

			default:
				flag = super.onTransact(i, parcel, parcel1, j);
				break;
			}
			return flag;
		}

		public Stub()
		{
			attachInterface(this, "com.spb.contacts.IContactsServiceCallback");
		}
	}


	public abstract void onBirthdayDeleted(int i)
		throws RemoteException;

	public abstract void onBirthdayUpdated(int i, int j, int k, int l, int i1, int j1)
		throws RemoteException;

	public abstract void onConnectionDeleted(int i, int j, int k)
		throws RemoteException;

	public abstract void onConnectionUpdated(int i, int j, String s, int k, String s1, String s2, int l)
		throws RemoteException;

	public abstract void onContactDeleted(int i, int j)
		throws RemoteException;

	public abstract void onContactPhotoUpdated(int i, int j, int k)
		throws RemoteException;

	public abstract void onContactUpdated(int i, String s, String s1, boolean flag, int j, int k)
		throws RemoteException;

	public abstract void onEventDeleted(int i, int j, int k)
		throws RemoteException;

	public abstract void onEventUpdated(int i, int j, int k, long l, int i1)
		throws RemoteException;

	public abstract void onFinishedReload(int i)
		throws RemoteException;

	public abstract void onFinishedReloadingBirthdays()
		throws RemoteException;

	public abstract void onFinishedUpdatingContact(int i, int j)
		throws RemoteException;

	public abstract void onStartedReload(int i)
		throws RemoteException;

	public abstract void onStartedReloadingBirthdays()
		throws RemoteException;

	public abstract void onStartedUpdatingContact(int i, int j)
		throws RemoteException;

	public abstract void onStructuredNameUpdated(int i, String s, String s1)
		throws RemoteException;
}
