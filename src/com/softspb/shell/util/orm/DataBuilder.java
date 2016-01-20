// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm;

import android.content.ContentValues;
import com.softspb.shell.util.orm.ann.DataSetter;
import com.softspb.shell.util.orm.ann.ForeignKey;
import com.softspb.shell.util.orm.ann.PrimaryKey;
import com.softspb.util.CollectionFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Referenced classes of package com.softspb.shell.util.orm:
//			ValueSetter, DataAdapter, ForeignKeyInfo, PrimaryKeyInfo, 
//			DataProvider

public class DataBuilder
{

	private static final ConcurrentHashMap BUILDERS_CACHE = new ConcurrentHashMap();
	private final Class clazz;
	private final LinkedList fkInfo = new LinkedList();
	private final LinkedList valueSetters = new LinkedList();

	private DataBuilder(Class class1)
	{
		clazz = class1;
	}

	private static void checkClass(Class class1)
	{
		if (class1.isInterface())
			throw new IllegalArgumentException((new StringBuilder()).append(class1.getName()).append(" is an interface.").toString());
		if (class1.isAnnotation())
			throw new IllegalArgumentException((new StringBuilder()).append(class1.getName()).append(" is an annotation").toString());
		else
			return;
	}

	public static DataBuilder createBuilder(Class class1)
	{
		checkClass(class1);
		DataBuilder databuilder1;
		if (BUILDERS_CACHE.containsKey(class1))
		{
			databuilder1 = (DataBuilder)BUILDERS_CACHE.get(class1);
		} else
		{
			DataBuilder databuilder = new DataBuilder(class1);
			Method amethod[] = class1.getMethods();
			int i = amethod.length;
			int j = 0;
			while (j < i) 
			{
				Method method = amethod[j];
				if (method.isAnnotationPresent(DataSetter.class))
				{
					DataSetter datasetter = (DataSetter)method.getAnnotation(DataSetter.class);
					if (!method.isAnnotationPresent(PrimaryKey.class))
						databuilder.valueSetters.add(new ValueSetter(method, datasetter.columnName(), DataAdapter.forType(datasetter.type())));
					else
						databuilder.valueSetters.add(new ValueSetter(method, datasetter.columnName(), DataAdapter.forType(datasetter.type()), new PrimaryKeyInfo((PrimaryKey)method.getAnnotation(PrimaryKey.class))));
					if (method.isAnnotationPresent(ForeignKey.class))
					{
						ForeignKey foreignkey = (ForeignKey)method.getAnnotation(ForeignKey.class);
						databuilder.fkInfo.add(ForeignKeyInfo.create(foreignkey, datasetter.columnName()));
					}
				}
				j++;
			}
			BUILDERS_CACHE.put(class1, databuilder);
			databuilder1 = databuilder;
		}
		return databuilder1;
	}

	public ArrayList checkForMissedValues(ContentValues contentvalues)
	{
		ArrayList arraylist = CollectionFactory.newArrayList();
		for (int i = 0; i < valueSetters.size(); i++)
		{
			ValueSetter valuesetter = (ValueSetter)valueSetters.get(i);
			if (valuesetter.getPrimaryKeyInfo() == null && !contentvalues.containsKey(valuesetter.getName()))
				arraylist.add(valuesetter.getName());
		}

		return arraylist;
	}

	public HashMap createProjectionMap()
	{
		HashMap hashmap = CollectionFactory.newHashMap();
		ValueSetter valuesetter;
		for (Iterator iterator = valueSetters.iterator(); iterator.hasNext(); hashmap.put(valuesetter.getName(), valuesetter.getName()))
			valuesetter = (ValueSetter)iterator.next();

		return hashmap;
	}

	public String createTableSQL(String s)
	{
		StringBuilder stringbuilder = (new StringBuilder("CREATE TABLE ")).append(s).append(" (");
		ListIterator listiterator = valueSetters.listIterator();
		do
		{
			if (!listiterator.hasNext())
				break;
			if (listiterator.hasPrevious())
				stringbuilder.append(",");
			ValueSetter valuesetter = (ValueSetter)listiterator.next();
			stringbuilder.append(valuesetter.getName()).append(" ").append(valuesetter.getTypeName());
			if (valuesetter.getPrimaryKeyInfo() != null)
				stringbuilder.append(" ").append(valuesetter.getPrimaryKeyInfo().toSQLString());
		} while (true);
		ForeignKeyInfo foreignkeyinfo;
		for (ListIterator listiterator1 = fkInfo.listIterator(); listiterator1.hasNext(); stringbuilder.append(",").append(foreignkeyinfo.toSQLString()))
			foreignkeyinfo = (ForeignKeyInfo)listiterator1.next();

		stringbuilder.append(");");
		return stringbuilder.toString();
	}

	public Object newInstance(DataProvider dataprovider)
		throws InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Object obj = clazz.newInstance();
		for (Iterator iterator = valueSetters.iterator(); iterator.hasNext(); ((ValueSetter)iterator.next()).apply(obj, dataprovider));
		return obj;
	}

}
