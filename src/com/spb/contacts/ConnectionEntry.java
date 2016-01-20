// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

// Referenced classes of package com.spb.contacts:
//			DataEntry

class ConnectionEntry extends DataEntry
{

	final String address;
	final boolean isPhone;
	final String label;
	final int locationType;
	final String mimetype;

	ConnectionEntry(int i, int j, String s, String s1, int k, String s2)
	{
		super(i, j);
		address = s;
		mimetype = s1;
		isPhone = "vnd.android.cursor.item/phone_v2".equals(s1);
		locationType = k;
		label = s2;
	}

	private boolean hasSameAddress(ConnectionEntry connectionentry)
	{
		boolean flag;
		if (isPhone != connectionentry.isPhone)
			flag = false;
		else
		if (isPhone)
			flag = PhoneNumberUtils.compare(address, connectionentry.address);
		else
			flag = TextUtils.equals(address, connectionentry.address);
		return flag;
	}

	public boolean equals(Object paramObject)
	  {
		boolean i = false;
	    if (paramObject != null)
	    {
	     
	      Class localClass1 = paramObject.getClass();
	      Class localClass2 = getClass();
	      if (localClass1 == localClass2)
	      {
		      ConnectionEntry localConnectionEntry = (ConnectionEntry)paramObject;
		      int j = this.id;
		      int k = localConnectionEntry.id;
		      if (!((j != k) || (!hasSameAddress(localConnectionEntry))))
		      {
			      int m = this.locationType;
			      int n = localConnectionEntry.locationType;
			      if (m == n)
			      {
				      String str1 = this.label;
				      String str2 = localConnectionEntry.label;
				      if (TextUtils.equals(str1, str2))
					      {
					      	i = true;
					      }
			      }
		      }
	      }
	    }
	    return i;
	  }
	boolean isDuplicate(DataEntry dataentry)
	{
		boolean flag = false;
		if (dataentry != null && dataentry.getClass() == getClass())
			flag = hasSameAddress((ConnectionEntry)dataentry);
		return flag;
	}
}
