// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.database.*;

public class ProjectionCursor extends CursorWrapper
	implements CrossProcessCursor
{

	int columnCount;
	CrossProcessCursor cursor;
	int indexMap[];
	String projection[];

	public ProjectionCursor(Cursor cursor1, String as[])
	{
		super(cursor1);
		cursor = (CrossProcessCursor)cursor1;
		projection = as;
		columnCount = as.length;
		indexMap = new int[columnCount];
		for (int i = 0; i < columnCount; i++)
		{
			cursor1.getColumnIndexOrThrow(as[i]);
			indexMap[i] = cursor1.getColumnIndex(as[i]);
		}

	}

	public void copyStringToBuffer(int i, CharArrayBuffer chararraybuffer)
	{
		super.copyStringToBuffer(indexMap[i], chararraybuffer);
	}

	public void fillWindow(int i, CursorWindow cursorwindow)
	{
		cursor.fillWindow(i, cursorwindow);
	}

	public byte[] getBlob(int i)
	{
		return super.getBlob(indexMap[i]);
	}

	public int getColumnCount()
	{
		return columnCount;
	}

	public int getColumnIndex(String s)
	{
		int i = 0;
_L3:
		while (i < columnCount)
		{
		if (!projection[i].equals(s)) 
			i++;

		}
		return i;

	}

	public int getColumnIndexOrThrow(String s)
	{
		int i = getColumnIndex(s);
		if (i == -1)
			throw new IllegalArgumentException((new StringBuilder()).append("Column ").append(s).append(" doesn't exist").toString());
		else
			return i;
	}

	public String getColumnName(int i)
	{
		return projection[i];
	}

	public String[] getColumnNames()
	{
		return projection;
	}

	public double getDouble(int i)
	{
		return super.getDouble(indexMap[i]);
	}

	public float getFloat(int i)
	{
		return super.getFloat(indexMap[i]);
	}

	public int getInt(int i)
	{
		return super.getInt(indexMap[i]);
	}

	public long getLong(int i)
	{
		return super.getLong(indexMap[i]);
	}

	public short getShort(int i)
	{
		return super.getShort(indexMap[i]);
	}

	public String getString(int i)
	{
		return super.getString(indexMap[i]);
	}

	public CursorWindow getWindow()
	{
		return cursor.getWindow();
	}

	public boolean onMove(int i, int j)
	{
		return cursor.onMove(i, j);
	}
}
