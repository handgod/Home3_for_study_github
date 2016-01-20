// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm;

import java.util.HashMap;

// Referenced classes of package com.softspb.shell.util.orm:
//			DataProvider

public abstract class DataAdapter
{
	private static final class TextAdapter extends DataAdapter
	{

//		public volatile Object get(DataProvider dataprovider, String s)
//		{
//			return get(dataprovider, s);
//		}

		public String get(DataProvider dataprovider, String s)
		{
			return dataprovider.getText(s);
		}

		public String getTypeName()
		{
			return "TEXT";
		}

		private TextAdapter()
		{
		}

	}

	private static final class ShortAdapter extends DataAdapter
	{

//		public volatile Object get(DataProvider dataprovider, String s)
//		{
//			return get(dataprovider, s);
//		}

		public Short get(DataProvider dataprovider, String s)
		{
			return Short.valueOf(dataprovider.getShort(s));
		}

		public String getTypeName()
		{
			return "SHORT";
		}

		private ShortAdapter()
		{
		}

	}

	private static final class LongAdapter extends DataAdapter
	{

		public Long get(DataProvider dataprovider, String s)
		{
			return Long.valueOf(dataprovider.getLong(s));
		}
//
//		public volatile Object get(DataProvider dataprovider, String s)
//		{
//			return get(dataprovider, s);
//		}

		public String getTypeName()
		{
			return "LONG";
		}

		private LongAdapter()
		{
		}

	}

	private static final class FloatAdapter extends DataAdapter
	{

		public Float get(DataProvider dataprovider, String s)
		{
			return Float.valueOf(dataprovider.getFloat(s));
		}

//		public volatile Object get(DataProvider dataprovider, String s)
//		{
//			return get(dataprovider, s);
//		}

		public String getTypeName()
		{
			return "FLOAT";
		}

		private FloatAdapter()
		{
		}

	}

	private static final class DoubleAdapter extends DataAdapter
	{

		public Double get(DataProvider dataprovider, String s)
		{
			return Double.valueOf(dataprovider.getDouble(s));
		}

//		public volatile Object get(DataProvider dataprovider, String s)
//		{
//			return get(dataprovider, s);
//		}

		public String getTypeName()
		{
			return "DOUBLE";
		}

		private DoubleAdapter()
		{
		}

	}

	private static final class BlobAdapter extends DataAdapter
	{

//		public volatile Object get(DataProvider dataprovider, String s)
//		{
//			return get(dataprovider, s);
//		}

		public byte[] get(DataProvider dataprovider, String s)
		{
			return dataprovider.getBlob(s);
		}

		public String getTypeName()
		{
			return "BLOB";
		}

		private BlobAdapter()
		{
		}

	}

	private static final class IntAdapter extends DataAdapter
	{

		public Integer get(DataProvider dataprovider, String s)
		{
			return Integer.valueOf(dataprovider.getInt(s));
		}

//		public volatile Object get(DataProvider dataprovider, String s)
//		{
//			return get(dataprovider, s);
//		}

		public String getTypeName()
		{
			return "INTEGER";
		}

		private IntAdapter()
		{
		}

	}


	private static final HashMap ADAPTER_MAP;
	public static final DataAdapter BLOB;
	public static final DataAdapter DOUBLE;
	public static final DataAdapter FLOAT;
	public static final DataAdapter INT;
	public static final DataAdapter LONG;
	public static final DataAdapter SHORT;
	public static final DataAdapter TEXT;

	private DataAdapter()
	{
	}


	public static DataAdapter forType(String s)
	{
		return (DataAdapter)ADAPTER_MAP.get(s);
	}

	public abstract Object get(DataProvider dataprovider, String s);

	public abstract String getTypeName();

	static 
	{
		INT = new IntAdapter();
		BLOB = new BlobAdapter();
		DOUBLE = new DoubleAdapter();
		FLOAT = new FloatAdapter();
		LONG = new LongAdapter();
		SHORT = new ShortAdapter();
		TEXT = new TextAdapter();
		ADAPTER_MAP = new HashMap();
		HashMap hashmap = ADAPTER_MAP;
		synchronized (hashmap) {
			ADAPTER_MAP.put("INTEGER", INT);
			ADAPTER_MAP.put("BLOB", BLOB);
			ADAPTER_MAP.put("DOUBLE", DOUBLE);
			ADAPTER_MAP.put("FLOAT", FLOAT);
			ADAPTER_MAP.put("LONG", LONG);
			ADAPTER_MAP.put("SHORT", SHORT);
			ADAPTER_MAP.put("TEXT", TEXT);	
		}
	}
}
