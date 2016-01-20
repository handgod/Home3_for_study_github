// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm;

import android.database.Cursor;

// Referenced classes of package com.softspb.shell.util.orm:
//			DataProvider

public class CursorDataAdapter
	implements DataProvider
{

	private final Cursor cursor;

	public CursorDataAdapter(Cursor cursor1)
	{
		cursor = cursor1;
	}

	public void close()
	{
		cursor.close();
	}

	public byte[] getBlob(String s)
	{
		return cursor.getBlob(cursor.getColumnIndexOrThrow(s));
	}

	public double getDouble(String s)
	{
		int i = cursor.getColumnIndexOrThrow(s);
		return cursor.getDouble(i);
	}

	public float getFloat(String s)
	{
		int i = cursor.getColumnIndexOrThrow(s);
		return cursor.getFloat(i);
	}

	public int getInt(String s)
	{
		int i = cursor.getColumnIndexOrThrow(s);
		return cursor.getInt(i);
	}

	public long getLong(String s)
	{
		int i = cursor.getColumnIndexOrThrow(s);
		return cursor.getLong(i);
	}

	public short getShort(String s)
	{
		int i = cursor.getColumnIndexOrThrow(s);
		return cursor.getShort(i);
	}

	public String getText(String s)
	{
		int i = cursor.getColumnIndexOrThrow(s);
		return cursor.getString(i);
	}
}
