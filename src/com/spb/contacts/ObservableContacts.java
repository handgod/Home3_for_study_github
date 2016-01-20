// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.database.Cursor;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.spb.contacts:
//			ContactsDataProjection, ContactsService

abstract class ObservableContacts
	implements ContactsDataProjection
{

	private static final Logger logger = Loggers.getLogger(ContactsService.class);
	private final SparseIntArray contactIds = new SparseIntArray();
	private final SparseIntArray deletedIds = new SparseIntArray();
	private final SparseArray displayNames = new SparseArray();
	private final String logPrefix;
	private final SparseIntArray photoVersions;

	ObservableContacts(String s, SparseIntArray sparseintarray)
	{
		logPrefix = (new StringBuilder()).append(s).append(" ").toString();
		photoVersions = sparseintarray;
	}

	private void delete(int i)
	{
		contactIds.delete(i);
		displayNames.delete(i);
		onContactDeleted(i);
	}

	private void logd(String s)
	{
		logger.d((new StringBuilder()).append(logPrefix).append(s).toString());
	}

	private void loge(String s, Throwable throwable)
	{
		logger.e((new StringBuilder()).append(logPrefix).append(s).toString(), throwable);
	}

	private void update(int i, String s, String s1, boolean flag, int j, int k)
	{
		contactIds.put(i, 1);
		displayNames.put(i, s1);
		onContactUpdated(i, s, s1, flag, j, k);
	}

	void finishReloading()
	{
		logd("finishReloading >>>");
		int i = deletedIds.size();
		for (int j = 0; j < i; j++)
		{
			int k = deletedIds.keyAt(j);
			logd((new StringBuilder()).append("Contact has been deleted: id=").append(k).toString());
			delete(k);
		}

		logd("finishReloading <<<");
	}

	String getDisplayName(int i)
	{
		return (String)displayNames.get(i);
	}

	boolean hasContact(int i)
	{
		boolean flag;
		if (contactIds.indexOfKey(i) >= 0)
			flag = true;
		else
			flag = false;
		return flag;
	}

	 int loadContact(Cursor paramCursor)
	  {
	    boolean flag = true;
	    int k = (int)paramCursor.getLong(0);
	    String str1 = paramCursor.getString(1);
	    if (paramCursor.getInt(7) != 1)
	    {
	      String str2 = paramCursor.getString(6);
	      int m = 0;
	      int n = 8;
	      try
	      {
	        String str3 = paramCursor.getString(n);
	        if (str3 != null)
	        {
	          int i1 = Integer.parseInt(str3);
	          m = i1;
	        }
	        if (m == 0)
	        {
	          update(k, str2, str1, true, m, this.photoVersions.get(m, 0));
	          this.deletedIds.delete(k);
	          return k;
	        }
	      }
	      catch (NumberFormatException localNumberFormatException)
	      {
	    	  
	      }
	      
	    }
		return k;
	  }

	abstract void onContactDeleted(int i);

	abstract void onContactUpdated(int i, String s, String s1, boolean flag, int j, int k);

	void startReloading()
	{
		logd("startReloading");
		deletedIds.clear();
		int i = contactIds.size();
		for (int j = 0; j < i; j++)
		{
			int k = contactIds.keyAt(j);
			deletedIds.put(k, 1);
		}

	}

}
