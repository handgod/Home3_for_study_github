// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.os.*;
import java.util.List;

// Referenced classes of package com.spb.contacts:
//			IPhoneNumberResolvingServiceCallback

public interface IPhoneNumberResolvingService
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements IPhoneNumberResolvingService
	{
		private static class Proxy
			implements IPhoneNumberResolvingService
		{

			private IBinder mRemote;

			public void addPhoneNumber(String s)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IPhoneNumberResolvingService");
				parcel.writeString(s);
				mRemote.transact(2, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public IBinder asBinder()
			{
				return mRemote;
			}

			public String getInterfaceDescriptor()
			{
				return "com.spb.contacts.IPhoneNumberResolvingService";
			}

			public long getResolvedContactId(String s)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				long l;
				parcel.writeInterfaceToken("com.spb.contacts.IPhoneNumberResolvingService");
				parcel.writeString(s);
				mRemote.transact(1, parcel, parcel1, 0);
				parcel1.readException();
				l = parcel1.readLong();
				parcel1.recycle();
				parcel.recycle();
				return l;

			}

			public List getResolvedPhoneNumbers(int i)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				java.util.ArrayList arraylist;
				parcel.writeInterfaceToken("com.spb.contacts.IPhoneNumberResolvingService");
				parcel.writeInt(i);
				mRemote.transact(6, parcel, parcel1, 0);
				parcel1.readException();
				arraylist = parcel1.createStringArrayList();
				parcel1.recycle();
				parcel.recycle();
				return arraylist;

			}

			public void registerCallback(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				IBinder ibinder;
				parcel.writeInterfaceToken("com.spb.contacts.IPhoneNumberResolvingService");
				if (iphonenumberresolvingservicecallback == null)
					return;
				ibinder = iphonenumberresolvingservicecallback.asBinder();

				parcel.writeStrongBinder(ibinder);
				mRemote.transact(4, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();

			}

			public void removePhoneNumber(String s)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.spb.contacts.IPhoneNumberResolvingService");
				parcel.writeString(s);
				mRemote.transact(3, parcel, parcel1, 0);
				parcel1.readException();
				parcel1.recycle();
				parcel.recycle();
				return;

			}

			public void unregisterCallback(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				IBinder ibinder;
				parcel.writeInterfaceToken("com.spb.contacts.IPhoneNumberResolvingService");
				if (iphonenumberresolvingservicecallback == null)
					return;
				ibinder = iphonenumberresolvingservicecallback.asBinder();

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


		private static final String DESCRIPTOR = "com.spb.contacts.IPhoneNumberResolvingService";
		static final int TRANSACTION_addPhoneNumber = 2;
		static final int TRANSACTION_getResolvedContactId = 1;
		static final int TRANSACTION_getResolvedPhoneNumbers = 6;
		static final int TRANSACTION_registerCallback = 4;
		static final int TRANSACTION_removePhoneNumber = 3;
		static final int TRANSACTION_unregisterCallback = 5;

		public static IPhoneNumberResolvingService asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.spb.contacts.IPhoneNumberResolvingService");
				if (iinterface != null && (iinterface instanceof IPhoneNumberResolvingService))
					obj = (IPhoneNumberResolvingService)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((IPhoneNumberResolvingService) (obj));
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
				parcel.enforceInterface("com.spb.contacts.IPhoneNumberResolvingService");
				long l = getResolvedContactId(parcel.readString());
				parcel1.writeNoException();
				parcel1.writeLong(l);
				break;
			case 2:
				parcel.enforceInterface("com.spb.contacts.IPhoneNumberResolvingService");
				addPhoneNumber(parcel.readString());
				parcel1.writeNoException();
				break;

			case 3:
				parcel.enforceInterface("com.spb.contacts.IPhoneNumberResolvingService");
				removePhoneNumber(parcel.readString());
				parcel1.writeNoException();
				break;
			case 4:
				parcel.enforceInterface("com.spb.contacts.IPhoneNumberResolvingService");
				registerCallback(IPhoneNumberResolvingServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
				parcel1.writeNoException();
				break;
			case 5:
				parcel.enforceInterface("com.spb.contacts.IPhoneNumberResolvingService");
				unregisterCallback(IPhoneNumberResolvingServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
				parcel1.writeNoException();
				break;
			case 6:
				parcel.enforceInterface("com.spb.contacts.IPhoneNumberResolvingService");
				List list = getResolvedPhoneNumbers(parcel.readInt());
				parcel1.writeNoException();
				parcel1.writeStringList(list);
				break;
			case 1598968902:
				parcel1.writeString("com.spb.contacts.IPhoneNumberResolvingService");
				break;

			default:
				flag = super.onTransact(i, parcel, parcel1, j);
				break;
			}
			return flag;
		}

		public Stub()
		{
			attachInterface(this, "com.spb.contacts.IPhoneNumberResolvingService");
		}
	}


	public abstract void addPhoneNumber(String s)
		throws RemoteException;

	public abstract long getResolvedContactId(String s)
		throws RemoteException;

	public abstract List getResolvedPhoneNumbers(int i)
		throws RemoteException;

	public abstract void registerCallback(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
		throws RemoteException;

	public abstract void removePhoneNumber(String s)
		throws RemoteException;

	public abstract void unregisterCallback(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
		throws RemoteException;
}
