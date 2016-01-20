// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;
import java.io.File;
import java.io.IOException;

// Referenced classes of package com.softspb.util:
//			FileUtils

public abstract class AssetSQLiteOpenHelper extends SQLiteOpenHelper
{

	private static final String TEMP_DB_NAME = "upgrade_temp_db";
	protected final String TAG = getClass().getSimpleName();
	private String assetPath;
	private final Context context;
	private File dbFile;
	private boolean needUpgrade;
	private int newVersion;
	private int oldVersion;

	public AssetSQLiteOpenHelper(Context context1, String s, android.database.sqlite.SQLiteDatabase.CursorFactory cursorfactory, int i)
	{
		super(context1, (new File(s)).getName(), cursorfactory, i);
		context = context1;
		dbFile = context1.getDatabasePath((new File(s)).getName());
		assetPath = s;
	}

	/**
	 * @deprecated Method getWritableDatabase is deprecated
	 */

	public synchronized SQLiteDatabase getWritableDatabase()
	{

		File file;
		boolean flag;
		logd("getWritableDatabase");
		SQLiteDatabase sqlitedatabase1 = null;
		SQLiteDatabase sqlitedatabase = null;
		file = new File(context.getDatabasePath("dummy_this_filename_shouldnt_exist").getParentFile(), assetPath);
		if (file.equals(dbFile))
		{
			flag = file.canRead();
			SQLiteDatabase sqlitedatabase3;
			if (!flag)
				{
				sqlitedatabase3 = null;
				sqlitedatabase3 = SQLiteDatabase.openDatabase(file.getPath(), null, 16);
				logd((new StringBuilder()).append("createDatabase: found legacy DB file: ").append(file.getPath()).toString());
				logd((new StringBuilder()).append("createDatabase: existing legacy DB version: ").append(sqlitedatabase3.getVersion()).toString());
				if (sqlitedatabase3 != null)
				{
					sqlitedatabase3.close();
				
					logd((new StringBuilder()).append("getWritableDatabase: dropping legacy file: ").append(file.getPath()).toString());
					file.delete();
				}
		}
		if (dbFile.canRead())
		{
			logd((new StringBuilder()).append("createDatabase: DB file exists: ").append(dbFile.getPath()).toString());

			sqlitedatabase = SQLiteDatabase.openDatabase(dbFile.getPath(), null, 16);
			logd((new StringBuilder()).append("createDatabase: existing DB version: ").append(sqlitedatabase.getVersion()).toString());
		
				needUpgrade = false;
				sqlitedatabase1 = super.getWritableDatabase();
			
			
				{
				if (sqlitedatabase != null)
					sqlitedatabase.close();
				}
			return sqlitedatabase;
		}
		else 
		{
			logd("createDatabase: copying DB file...");
			SQLiteDatabase sqlitedatabase2 = null;
			try {
				FileUtils.copyAssetToFile(context, assetPath, dbFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logd("Checking copied DB...");
			
			sqlitedatabase2 = SQLiteDatabase.openDatabase(dbFile.getPath(), null, 16);
			logd("Copied DB is OK");
			if (sqlitedatabase2 == null)
			{
				sqlitedatabase2.close();
			}
			 sqlitedatabase =sqlitedatabase2 ;
		}
		}
		return sqlitedatabase;
	}

	protected void logd(String s)
	{
		Log.d(TAG, s);
	}

	protected void logw(String s, Exception exception)
	{
		Log.w(TAG, s, exception);
	}

	public void onCreate(SQLiteDatabase sqlitedatabase)
	{
	}

	public void onOpen(SQLiteDatabase sqlitedatabase)
	{
		File file;
		if (!needUpgrade)
			return;
		file = context.getDatabasePath((new StringBuilder()).append("temp_").append((new File(assetPath)).getName()).toString());
		logd((new StringBuilder()).append("onOpen: copying new DB to temp file: ").append(file.getPath()).toString());
		String as[];
		try
		{
			FileUtils.copyAssetToFile(context, assetPath, file);
		}
		catch (IOException ioexception)
		{
			logw((new StringBuilder()).append("onOpen: failed to copy DB file: ").append(ioexception).toString(), ioexception);
		}
		logd((new StringBuilder()).append("onOpen: attaching temp DB: ").append("ATTACH DATABASE ? AS upgrade_temp_db").toString());
		as = new String[1];
		as[0] = file.getPath();
		sqlitedatabase.execSQL("ATTACH DATABASE ? AS upgrade_temp_db", as);
		sqlitedatabase.setVersion(oldVersion);
		sqlitedatabase.beginTransaction();
		onUpgrade(sqlitedatabase, oldVersion, newVersion, "upgrade_temp_db");
		sqlitedatabase.setTransactionSuccessful();
		sqlitedatabase.setVersion(newVersion);
		sqlitedatabase.endTransaction();
		logd("onOpen: detaching temp DB...");
		sqlitedatabase.execSQL("DETACH DATABASE upgrade_temp_db");
		logd("onOpen: finishing DB upgrade.");
		file.delete();

	}

	public final void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
	{
		logd((new StringBuilder()).append("onUpgrade: oldVersion=").append(i).append(" newVersion=").append(j).toString());
		needUpgrade = true;
		oldVersion = i;
		newVersion = j;
	}

	protected abstract void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j, String s);
}
