// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import com.softspb.util.Conditions;
import com.softspb.util.IOHelper;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.io.*;

public class BitmapHelper
{

	private static Logger logger = Loggers.getLogger(BitmapHelper.class.getName());

	private BitmapHelper()
	{
	}

	public static Bitmap rotate(Bitmap bitmap, float f)
	{
		Conditions.checkNotNull(bitmap);
		boolean flag;
		Matrix matrix;
		if (!bitmap.isRecycled())
			flag = true;
		else
			flag = false;
		Conditions.checkArgument(flag, "Can't rotate recycled bitmap");
		matrix = new Matrix();
		matrix.reset();
		matrix.postRotate(f);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	public static void writeBitmap(Bitmap bitmap)
	{
		if (Environment.getExternalStorageState().equals("mounted"))
		{
			Object aobj[] = new Object[2];
			aobj[0] = Long.toString(System.currentTimeMillis());
			aobj[1] = "png";
			writeBitmap(bitmap, String.format("%s.%s", aobj), Environment.getExternalStorageDirectory());
		}
	}

	public static void writeBitmap(Bitmap bitmap, String s, File file)
	{
		if (file.exists()) 
		{
			File file1 = new File(file, s);
			Object obj = null;
			FileOutputStream fileoutputstream = null;
			try {
				fileoutputstream = new FileOutputStream(file1);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean flag = bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 90, fileoutputstream);
			logger.i((new StringBuilder()).append("write result=").append(flag).append(" to ").append(s).toString());
			IOException ioexception;
			if (fileoutputstream != null)
			{
				try
				{
					IOHelper.closeSilent(fileoutputstream);
				}
				catch (SecurityException securityexception)
				{
					logger.e("can't access sdcard", securityexception);
				}
			}
		}
		
		return;
	}

}
