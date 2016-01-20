// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

import android.os.*;

public interface ILicenseResultListener
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements ILicenseResultListener
	{
		private static class Proxy
			implements ILicenseResultListener
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public String getInterfaceDescriptor()
			{
				return "com.android.vending.licensing.ILicenseResultListener";
			}
			public void verifyLicense(int paramInt, String paramString1, String paramString2)
			   throws RemoteException
			{
		        Parcel localParcel = Parcel.obtain();
		        try
		        {
		          localParcel.writeInterfaceToken("com.android.vending.licensing.ILicenseResultListener");
		          localParcel.writeInt(paramInt);
		          localParcel.writeString(paramString1);
		          localParcel.writeString(paramString2);
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
			
			
			Proxy(IBinder ibinder)
			{
				mRemote = ibinder;
			}
		}


		private static final String DESCRIPTOR = "com.android.vending.licensing.ILicenseResultListener";
		static final int TRANSACTION_verifyLicense = 1;

		public static ILicenseResultListener asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.android.vending.licensing.ILicenseResultListener");
				if (iinterface != null && (iinterface instanceof ILicenseResultListener))
					obj = (ILicenseResultListener)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((ILicenseResultListener) (obj));
		}

		public IBinder asBinder()
		{
			return this;
		}
		
		public boolean onTransact(int i, Parcel parcel, Parcel parcel1, int j)
				throws RemoteException
			{
				boolean flag = true;
				try{
				switch (i) {
				case 1:
					parcel.enforceInterface("com.android.vending.licensing.ILicenseResultListener");
					verifyLicense(parcel.readInt(), parcel.readString(), parcel.readString());
					return flag;
				case 1598968902:
					parcel1.writeString("com.android.vending.licensing.ILicenseResultListener");
					break;
				}
				flag = super.onTransact(i, parcel, parcel1, j);
				return flag;
				}
				catch (RemoteException e) {
					throw new RemoteException();
				}
									
			}
		public Stub()
		{
			attachInterface(this, "com.android.vending.licensing.ILicenseResultListener");
		}
	}


	public abstract void verifyLicense(int i, String s, String s1)
		throws RemoteException;
}
