// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.imageviewer;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import com.softspb.util.Conditions;

public class ImageViewerService extends Service
{

	private IImageViewer.Stub binder;

	public ImageViewerService()
	{
		binder = new IImageViewer.Stub() {

			final ImageViewerService this$0;

			public Bitmap getBitmap(String s)
				throws RemoteException
			{
				return ImageViewerService.this.getBitmap(s);
			}

			
			{
				this$0 = ImageViewerService.this;
//				super();
			}
		};
	}

	private Bitmap getBitmap(String s)
	{
		Conditions.checkNotNull(s);
		android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
		return android.provider.MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), Long.valueOf(s).longValue(), 1, options);
	}

	public IBinder onBind(Intent intent)
	{
		return binder;
	}

}
