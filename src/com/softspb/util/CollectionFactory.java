// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.util.SparseArray;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CollectionFactory
{

	private CollectionFactory()
	{
	}

	public static ArrayList newArrayList()
	{
		return new ArrayList();
	}

	public static ArrayList newArrayList(int i)
	{
		return new ArrayList(i);
	}

	public static ConcurrentHashMap newConcurentHashMap()
	{
		return new ConcurrentHashMap();
	}

	public static HashMap newHashMap()
	{
		return new HashMap();
	}

	public static HashSet newHashSet()
	{
		return new HashSet();
	}

	public static LinkedList newLinkedList()
	{
		return new LinkedList();
	}

	public static LinkedList newLinkedList(Collection collection)
	{
		return new LinkedList(collection);
	}

	public static SparseArray newSparseArray()
	{
		return new SparseArray();
	}

	public static TreeSet newTreeSet()
	{
		return new TreeSet();
	}
}
