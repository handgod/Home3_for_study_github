// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Referenced classes of package com.softspb.shell.util.orm:
//			DataAdapter, PrimaryKeyInfo, DataProvider

public class ValueSetter
{

	private final DataAdapter dataAdapter;
	private final Method method;
	private final String name;
	private final PrimaryKeyInfo primaryKeyInfo;

	public ValueSetter(Method method1, String s, DataAdapter dataadapter)
	{
		this(method1, s, dataadapter, null);
	}

	public ValueSetter(Method method1, String s, DataAdapter dataadapter, PrimaryKeyInfo primarykeyinfo)
	{
		method = method1;
		name = s;
		dataAdapter = dataadapter;
		primaryKeyInfo = primarykeyinfo;
	}

	public void apply(Object obj, DataProvider dataprovider)
		throws InvocationTargetException, IllegalAccessException
	{
		Method method1 = method;
		Object aobj[] = new Object[1];
		aobj[0] = dataAdapter.get(dataprovider, name);
		method1.invoke(obj, aobj);
	}

	public String getName()
	{
		return name;
	}

	public PrimaryKeyInfo getPrimaryKeyInfo()
	{
		return primaryKeyInfo;
	}

	public String getTypeName()
	{
		return dataAdapter.getTypeName();
	}
}
