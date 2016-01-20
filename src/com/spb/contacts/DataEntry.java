// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;


class DataEntry
{

	public final int id;
	public int version;

	DataEntry(int i, int j)
	{
		id = i;
		version = j;
	}
	 public boolean equals(Object paramObject)
	  {
		 boolean flag;
			flag = false;
	    if (paramObject == null)
	    {}
	    else
	    {
	      Class localClass1 = paramObject.getClass();
	      Class localClass2 = getClass();
	      if (localClass1 == localClass2)
	      {
		      DataEntry localDataEntry = (DataEntry)paramObject;
		      int j = localDataEntry.id;
		      int k = this.id;
		      if (j == k)
		      {
			      int m = localDataEntry.version;
			      int n = this.version;
			      if (m == n)
			      {
			    	  flag = true;
			      }
		      }
	      }
	    }
	    return flag;
	  }
	boolean isDuplicate(DataEntry dataentry)
	{
		return false;
	}
}
