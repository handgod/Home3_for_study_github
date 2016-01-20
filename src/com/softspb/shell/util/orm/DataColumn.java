// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Referenced classes of package com.softspb.shell.util.orm:
//			DataAdapter, DataProvider

public class DataColumn
{

	private final DataAdapter dataAdapter;
	private final Method getter;
	private final boolean isPrimaryKey;
	private final String name;
	private final Method setter;

	public DataColumn(Method method, Method method1, String s, DataAdapter dataadapter)
	{
		this(method, method1, s, dataadapter, false);
	}

	public DataColumn(Method method, Method method1, String s, DataAdapter dataadapter, boolean flag)
	{
		setter = method;
		getter = method1;
		name = s;
		dataAdapter = dataadapter;
		isPrimaryKey = flag;
	}

	public String getName()
	{
		return name;
	}

	public String getTypeName()
	{
		return dataAdapter.getTypeName();
	}

	public Object getValue(Object obj, Class class1)
		throws IllegalAccessException, InvocationTargetException
	{
		return class1.cast(getter.invoke(obj, new Object[0]));
	}

	public boolean isPrimaryKey()
	{
		return isPrimaryKey;
	}

	public void setValue(Object obj, DataProvider dataprovider)
		throws InvocationTargetException, IllegalAccessException
	{
		Method method = setter;
		Object aobj[] = new Object[1];
		aobj[0] = dataAdapter.get(dataprovider, name);
		method.invoke(obj, aobj);
	}
}
