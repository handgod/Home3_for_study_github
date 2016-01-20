// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import android.graphics.Bitmap;
import android.os.*;

// Referenced classes of package com.softspb.shell.browser.service:
//			BrowserConfiguration, IBrowserServiceCallback

public interface IBrowserService
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements IBrowserService
	{
		private static class Proxy
			implements IBrowserService
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public BrowserConfiguration getBrowserConfiguration()
				throws RemoteException
			{
				BrowserConfiguration browserconfiguration ;
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.softspb.shell.browser.service.IBrowserService");
				mRemote.transact(4, parcel, parcel1, 0);
				parcel1.readException();
				if (parcel1.readInt() == 0) 
				{
					browserconfiguration = null;
				}
				else 
				{
					browserconfiguration = (BrowserConfiguration)BrowserConfiguration.CREATOR.createFromParcel(parcel1);
				}
				parcel1.recycle();
				parcel.recycle();
				
				return browserconfiguration;
			}

			public String getInterfaceDescriptor()
			{
				return "com.softspb.shell.browser.service.IBrowserService";
			}

			public void loadBookmarks()
				throws RemoteException
			{
				Parcel parcel = Parcel.obtain();
				parcel.writeInterfaceToken("com.softspb.shell.browser.service.IBrowserService");
				mRemote.transact(1, parcel, null, 1);
				parcel.recycle();
				return;
			
			}

			public Bitmap loadIcon(int i)
				throws RemoteException
			{
				Bitmap bitmap = null;
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.softspb.shell.browser.service.IBrowserService");
				parcel.writeInt(i);
				mRemote.transact(2, parcel, parcel1, 0);
				parcel1.readException();
				if (parcel1.readInt() == 0)
				{
				bitmap = null;
				}
				else 
				{
				 bitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(parcel1);
				}
				parcel1.recycle();
				parcel.recycle();
				return bitmap;
			}

			public Bitmap loadThumbnail(int i)
				throws RemoteException
			{
				Bitmap bitmap =null;
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.softspb.shell.browser.service.IBrowserService");
				parcel.writeInt(i);
				mRemote.transact(3, parcel, parcel1, 0);
				parcel1.readException();
				if (parcel1.readInt() == 0) 
				{
					bitmap = null;
				}
				else 
				{
					bitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(parcel1);
				}
				parcel1.recycle();
				parcel.recycle();
				return bitmap;
			}

			public void registerCallback(IBrowserServiceCallback ibrowserservicecallback)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				IBinder ibinder;
				parcel.writeInterfaceToken("com.softspb.shell.browser.service.IBrowserService");
				if (ibrowserservicecallback != null)
				{
					ibinder = ibrowserservicecallback.asBinder();
					parcel.writeStrongBinder(ibinder);
					mRemote.transact(5, parcel, parcel1, 0);
					parcel1.readException();
					parcel1.recycle();
					parcel.recycle();
				}
				return;
			}

			public void unregisterCallback(IBrowserServiceCallback ibrowserservicecallback)
				throws RemoteException
			{
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				IBinder ibinder;
				parcel.writeInterfaceToken("com.softspb.shell.browser.service.IBrowserService");
				if (ibrowserservicecallback == null)
				{
					ibinder = ibrowserservicecallback.asBinder();
					parcel.writeStrongBinder(ibinder);
					mRemote.transact(6, parcel, parcel1, 0);
					parcel1.readException();
					parcel1.recycle();
					parcel.recycle();
				}
				return;
				
			}

			Proxy(IBinder ibinder)
			{
				mRemote = ibinder;
			}
		}


		private static final String DESCRIPTOR = "com.softspb.shell.browser.service.IBrowserService";
		static final int TRANSACTION_getBrowserConfiguration = 4;
		static final int TRANSACTION_loadBookmarks = 1;
		static final int TRANSACTION_loadIcon = 2;
		static final int TRANSACTION_loadThumbnail = 3;
		static final int TRANSACTION_registerCallback = 5;
		static final int TRANSACTION_unregisterCallback = 6;

		public static IBrowserService asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.softspb.shell.browser.service.IBrowserService");
				if (iinterface != null && (iinterface instanceof IBrowserService))
					obj = (IBrowserService)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((IBrowserService) (obj));
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
				parcel.enforceInterface("com.softspb.shell.browser.service.IBrowserService");
				loadBookmarks();
				break;
			case 2:
				parcel.enforceInterface("com.softspb.shell.browser.service.IBrowserService");
				Bitmap bitmap1 = loadIcon(parcel.readInt());
				parcel1.writeNoException();
				if (bitmap1 != null)
				{
					parcel1.writeInt(1);
					bitmap1.writeToParcel(parcel1, 1);
				} else
				{
					parcel1.writeInt(0);
				}
				break;
			case 3:
				parcel.enforceInterface("com.softspb.shell.browser.service.IBrowserService");
				Bitmap bitmap = loadThumbnail(parcel.readInt());
				parcel1.writeNoException();
				if (bitmap != null)
				{
					parcel1.writeInt(1);
					bitmap.writeToParcel(parcel1, 1);
				} else
				{
					parcel1.writeInt(0);
				}
				break;
			case 4:
				parcel.enforceInterface("com.softspb.shell.browser.service.IBrowserService");
				BrowserConfiguration browserconfiguration = getBrowserConfiguration();
				parcel1.writeNoException();
				if (browserconfiguration != null)
				{
					parcel1.writeInt(1);
					browserconfiguration.writeToParcel(parcel1, 1);
				} else
				{
					parcel1.writeInt(0);
				}
				break;
			case 5:
				parcel.enforceInterface("com.softspb.shell.browser.service.IBrowserService");
				registerCallback(IBrowserServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
				parcel1.writeNoException();
				break;
			case 6:
				parcel.enforceInterface("com.softspb.shell.browser.service.IBrowserService");
				unregisterCallback(IBrowserServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
				parcel1.writeNoException();
				break;
			case 1598968902:
				parcel1.writeString("com.softspb.shell.browser.service.IBrowserService");
				break;
			default:
			
				break;
			}
			flag = super.onTransact(i, parcel, parcel1, j);
			return flag;
		
		}

		public Stub()
		{
			attachInterface(this, "com.softspb.shell.browser.service.IBrowserService");
		}
	}


	public abstract BrowserConfiguration getBrowserConfiguration()
		throws RemoteException;

	public abstract void loadBookmarks()
		throws RemoteException;

	public abstract Bitmap loadIcon(int i)
		throws RemoteException;

	public abstract Bitmap loadThumbnail(int i)
		throws RemoteException;

	public abstract void registerCallback(IBrowserServiceCallback ibrowserservicecallback)
		throws RemoteException;

	public abstract void unregisterCallback(IBrowserServiceCallback ibrowserservicecallback)
		throws RemoteException;
}
