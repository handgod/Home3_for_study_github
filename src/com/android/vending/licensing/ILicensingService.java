// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

import android.os.*;

// Referenced classes of package com.android.vending.licensing:
//			ILicenseResultListener

public interface ILicensingService
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements ILicensingService
	{
		private static class Proxy
			implements ILicensingService
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public void checkLicense(long paramLong, String paramString, ILicenseResultListener paramILicenseResultListener)
			        throws RemoteException
		      {
		        IBinder localIBinder = null;
		        Parcel localParcel = Parcel.obtain();
		        try
		        {
		          localParcel.writeInterfaceToken("com.android.vending.licensing.ILicensingService");
		          localParcel.writeLong(paramLong);
		          localParcel.writeString(paramString);
		          if (paramILicenseResultListener != null)
		            localIBinder = paramILicenseResultListener.asBinder();
		          localParcel.writeStrongBinder(localIBinder);
		          mRemote.transact(1, localParcel, null, 1);
		          return;
		        }
		        catch (RemoteException e) {
					throw new RemoteException();
				}
		        finally
		        {
		          localParcel.recycle();
		        }
		      }
			public String getInterfaceDescriptor()
			{
				return "com.android.vending.licensing.ILicensingService";
			}

			Proxy(IBinder ibinder)
			{
				mRemote = ibinder;
			}
		}


		private static final String DESCRIPTOR = "com.android.vending.licensing.ILicensingService";
		static final int TRANSACTION_checkLicense = 1;

		public static ILicensingService asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.android.vending.licensing.ILicensingService");
				if (iinterface != null && (iinterface instanceof ILicensingService))
					obj = (ILicensingService)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((ILicensingService) (obj));
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
				parcel.enforceInterface("com.android.vending.licensing.ILicensingService");
				checkLicense(parcel.readLong(), parcel.readString(), ILicenseResultListener.Stub.asInterface(parcel.readStrongBinder()));
				return flag;
			case 1598968902:
				parcel1.writeString("com.android.vending.licensing.ILicensingService");
				break;
			}
			flag = super.onTransact(i, parcel, parcel1, j);
			return flag;		
		}

		public Stub()
		{
			attachInterface(this, "com.android.vending.licensing.ILicensingService");
		}
	}


	public abstract void checkLicense(long l, String s, ILicenseResultListener ilicenseresultlistener)
		throws RemoteException;
}
