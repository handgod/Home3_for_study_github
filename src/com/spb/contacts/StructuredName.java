// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.text.TextUtils;

// Referenced classes of package com.spb.contacts:
//			DataEntry

class StructuredName extends DataEntry
{

	final String displayName;
	final String firstName;
	final String lastName;

	StructuredName(int i, int j, String s, String s1, String s2)
	{
		super(i, j);
		displayName = s;
		firstName = s1;
		lastName = s2;
	}

	private boolean sameFirstLastName(StructuredName structuredname)
	{
		boolean flag;
		if (TextUtils.equals(firstName, structuredname.firstName) && TextUtils.equals(lastName, structuredname.lastName))
			flag = true;
		else
			flag = false;
		return flag;
	}

	  public boolean equals(Object paramObject)
	  {
		  boolean flag = false;
	    if (paramObject == null);
	    {
	      Class localClass1 = paramObject.getClass();
	      Class localClass2 = getClass();
	      if (localClass1 == localClass2)
	      {
		      StructuredName localStructuredName = (StructuredName)paramObject;
		      int j = this.id;
		      int k = localStructuredName.id;
		      if (!((j != k) || (!sameFirstLastName(localStructuredName))))
		      {
		      flag = true;
		      }
	      }
	    }
	    return flag;
	  }

	boolean isDuplicate(DataEntry dataentry)
	{
		boolean flag;
		flag = false;
		if (dataentry != null && dataentry.getClass() == getClass())
			flag = sameFirstLastName((StructuredName)dataentry);
		return flag;
	}

	public String toString()
	{
		return (new StringBuilder()).append("StructuredName[firstName=\"").append(firstName).append("\" lastName=\"").append(lastName).append("\" displayName=\"").append(displayName).append("\"]").toString();
	}
}
