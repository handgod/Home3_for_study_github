// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm;

import com.softspb.shell.util.orm.ann.PrimaryKey;

public class PrimaryKeyInfo
{

	private final boolean isAutoIncrement;

	public PrimaryKeyInfo(PrimaryKey primarykey)
	{
		this(primarykey.autoincrement());
	}

	public PrimaryKeyInfo(boolean flag)
	{
		isAutoIncrement = flag;
	}

	public boolean isAutoIncrement()
	{
		return isAutoIncrement;
	}

	public String toSQLString()
	{
		StringBuilder stringbuilder = (new StringBuilder()).append("PRIMARY KEY");
		String s;
		if (isAutoIncrement)
			s = " AUTOINCREMENT";
		else
			s = "";
		return stringbuilder.append(s).toString();
	}
}
