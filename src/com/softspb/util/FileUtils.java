// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.*;

public final class FileUtils
{

	private FileUtils()
	{
	}

	public static void copyAssetToFile(Context context, String s, File file)
		throws IOException
	{
		InputStream inputstream;
		FileOutputStream fileoutputstream;
		boolean flag;
		logd((new StringBuilder()).append("copyAssetToFile: assetName=").append(s).append(" destFile=").append(file).toString());
		inputstream = null;
		fileoutputstream = null;
		flag = false;
		String s1;
		String s2;
		boolean flag1;
		File file1;
		StringBuilder stringbuilder;
		inputstream = context.getAssets().open(s);
		logd((new StringBuilder()).append("copyAssetToFile: opened asset: ").append(s).toString());
		file1 = file.getParentFile();
		stringbuilder = (new StringBuilder()).append("copyAssetToFile: parentDir=");
		if (file1 != null) 
		{
			s1 = file1.getPath();
			s2 = s1;
			
			logd(stringbuilder.append(s2).toString());
			if (file1 == null || !file1.exists()) 
			{
				flag1 = false;
				
			}
			else 
			{
				 flag1 = true;
			}
			FileOutputStream fileoutputstream1;
			logd((new StringBuilder()).append("copyAssetToFile: parentDirExists=").append(flag1).toString());
			if (file1 != null && !flag1)
			{
				logd((new StringBuilder()).append("copyAssetToFile: creating directory: ").append(file1.getPath()).toString());
				if (!file1.mkdirs())
					Log.w("Shell3D", (new StringBuilder()).append("Failed to create directory: ").append(file1.getPath()).append(". Weather DB may be not installed.").toString());
				logd((new StringBuilder()).append("copyAssetToFile: created directory: ").append(file1.getPath()).toString());
			}
			fileoutputstream1 = new FileOutputStream(file);
			byte abyte0[];
			int i;
			int j;
			logd((new StringBuilder()).append("copyAssetToFile: created destination file: ").append(file.getPath()).toString());
			abyte0 = new byte[1024];
			i = 0;
			j = 0;
			int k = inputstream.read(abyte0);
			
			if(k != -1)
			{
				int l;
				fileoutputstream1.write(abyte0, 0, k);
				i += k;
				l = j + 1;
				if (j % 100 != 0)
				{
					j = l;
					k = inputstream.read(abyte0);
			
				}

			}
			if (k == -1)
			{
				logd("copyAssetToFile: copy done.");
				return;
			}
		}
		else
		{
			s2 = "null";
		}
		
	}

	static void logd(String s)
	{
	}

	static void loge(String s, Throwable throwable)
	{
		Log.e("FileUtils", s, throwable);
	}
}
