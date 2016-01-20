// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.imageviewer;

import android.graphics.Bitmap;
import android.os.*;

public interface IImageViewer
	extends IInterface
{
	public static abstract class Stub extends Binder
		implements IImageViewer
	{
		private static class Proxy
			implements IImageViewer
		{

			private IBinder mRemote;

			public IBinder asBinder()
			{
				return mRemote;
			}

			public Bitmap getBitmap(String s)
				throws RemoteException
			{
				Bitmap bitmap;
				Parcel parcel;
				Parcel parcel1;
				parcel = Parcel.obtain();
				parcel1 = Parcel.obtain();
				parcel.writeInterfaceToken("com.softspb.shell.adapters.imageviewer.IImageViewer");
				parcel.writeString(s);
				mRemote.transact(1, parcel, parcel1, 0);
				parcel1.readException();
				if (parcel1.readInt() != 0) 
				{
					bitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(parcel1);			
				}
				else
				{
					bitmap = null;
				}
				parcel1.recycle();
				parcel.recycle();
				return bitmap;
			}

			public String getInterfaceDescriptor()
			{
				return "com.softspb.shell.adapters.imageviewer.IImageViewer";
			}

			Proxy(IBinder ibinder)
			{
				mRemote = ibinder;
			}
		}


		private static final String DESCRIPTOR = "com.softspb.shell.adapters.imageviewer.IImageViewer";
		static final int TRANSACTION_getBitmap = 1;

		public static IImageViewer asInterface(IBinder ibinder)
		{
			Object obj;
			if (ibinder == null)
			{
				obj = null;
			} else
			{
				IInterface iinterface = ibinder.queryLocalInterface("com.softspb.shell.adapters.imageviewer.IImageViewer");
				if (iinterface != null && (iinterface instanceof IImageViewer))
					obj = (IImageViewer)iinterface;
				else
					obj = new Proxy(ibinder);
			}
			return ((IImageViewer) (obj));
		}

		public IBinder asBinder()
		{
			return this;
		}

	public Bitmap getBitmap(String paramString)
        throws RemoteException
      {
		Bitmap localBitmap = null;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.softspb.shell.adapters.imageviewer.IImageViewer");
          localParcel1.writeString(paramString);
          boolean bool = IImageViewer.Stub.this.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localBitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(localParcel2);
            return localBitmap;
          }
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
          localBitmap = null;
          return localBitmap;
        }
      }

		public Stub()
		{
			attachInterface(this, "com.softspb.shell.adapters.imageviewer.IImageViewer");
		}
	}


	public abstract Bitmap getBitmap(String s)
		throws RemoteException;
}
