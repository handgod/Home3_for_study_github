// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;


public class Conditions
{

	private Conditions()
	{
	}

	public static void checkArgument(boolean flag)
	{
		if (!flag)
			throw new IllegalArgumentException();
		else
			return;
	}

	public static void checkArgument(boolean flag, Object obj)
	{
		if (!flag)
			throw new IllegalArgumentException(String.valueOf(obj));
		else
			return;
	}

	public static  void checkArgument(boolean flag, String s, Object aobj[])
	{
		if (!flag)
			throw new IllegalArgumentException(format(s, aobj));
		else
			return;
	}

	public static Object checkNotNull(Object obj)
	{
		if (obj == null)
			throw new NullPointerException();
		else
			return obj;
	}

	public static Object checkNotNull(Object obj, Object obj1)
	{
		if (obj == null)
			throw new NullPointerException(String.valueOf(obj1));
		else
			return obj;
	}

	public static  Object checkNotNull(Object obj, String s, Object aobj[])
	{
		if (obj == null)
			throw new NullPointerException(format(s, aobj));
		else
			return obj;
	}

	static  String format(String s, Object aobj[])
	{
		StringBuilder stringbuilder;
label0:
		{
			String s1 = String.valueOf(s);
			stringbuilder = new StringBuilder(s1.length() + 16 * aobj.length);
			int i = 0;
			int j = 0;
label1:
			do
			{
				int j1;
label2:
				{
					if (j < aobj.length)
					{
						j1 = s1.indexOf("%s", i);
						if (j1 != -1)
							break label2;
					}
					stringbuilder.append(s1.substring(i));
					if (j >= aobj.length)
						break label0;
					stringbuilder.append(" [");
					int k = j + 1;
					stringbuilder.append(aobj[j]);
					int i1;
					for (int l = k; l < aobj.length; l = i1)
					{
						stringbuilder.append(", ");
						i1 = l + 1;
						stringbuilder.append(aobj[l]);
					}

					break label1;
				}
				stringbuilder.append(s1.substring(i, j1));
				int k1 = j + 1;
				stringbuilder.append(aobj[j]);
				i = j1 + 2;
				j = k1;
			} while (true);
			stringbuilder.append(']');
		}
		return stringbuilder.toString();
	}
}
