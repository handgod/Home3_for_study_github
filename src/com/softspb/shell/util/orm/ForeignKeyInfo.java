// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm;

import com.softspb.shell.util.orm.ann.ForeignKey;

public class ForeignKeyInfo
{

	private final String fieldName;
	private final String foreignFieldName;
	private final String foreignTableName;

	public ForeignKeyInfo(String s, String s1, String s2)
	{
		fieldName = s;
		foreignTableName = s1;
		foreignFieldName = s2;
	}

	public static ForeignKeyInfo create(ForeignKey foreignkey, String s)
	{
		return new ForeignKeyInfo(s, foreignkey.tableName(), foreignkey.fieldName());
	}

	public String getFieldName()
	{
		return fieldName;
	}

	public String getForeignFieldName()
	{
		return foreignFieldName;
	}

	public String getForeignTableName()
	{
		return foreignTableName;
	}

	public String toSQLString()
	{
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("FOREIGN KEY (").append(fieldName).append(")").append(") REFERENCES ").append(foreignTableName);
		return stringbuilder.append("(").append(foreignFieldName).append(")").toString();
	}
}
