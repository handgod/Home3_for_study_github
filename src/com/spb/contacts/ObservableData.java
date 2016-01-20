// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.database.Cursor;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.ArrayList;

// Referenced classes of package com.spb.contacts:
//			ContactsDataProjection, ContactsService, DataEntry

abstract class ObservableData
	implements ContactsDataProjection
{

	private static final int RESULT_CHANGED_EVENT = 2;
	private static final int RESULT_DUPLICATE_EVENT = 1;
	private static final int RESULT_NEW_EVENT = 3;
	private static final int RESULT_NOT_CHANGED_EVENT =  1;
	private static final Logger logger = Loggers.getLogger(ContactsService.class);
	final SparseIntArray data2Contact = new SparseIntArray();
	final SparseArray dataById = new SparseArray();
	final SparseArray dataPerContact = new SparseArray();
	final SparseIntArray dataVersions = new SparseIntArray();
	final SparseIntArray deletedData = new SparseIntArray();
	private final String logPrefix;
	final String mimetypes[];

	 ObservableData(String s, String as[])
	{
		logPrefix = (new StringBuilder()).append(s).append(" ").toString();
		mimetypes = as;
	}

	 private int checkForDuplicatesAndInsert(DataEntry paramT, ArrayList paramArrayList)
	  {
		Object localObject;
	    String str1 = "checkForDuplicatesAndInsert >>> data=" + paramT + " entries=" + paramArrayList;
	    logd(str1);
	    int i = paramArrayList.size();
	    int j = 0;
	    DataEntry localDataEntry = null;
	    int n = 0;
	    if (j < i)
	    {
	      localDataEntry = (DataEntry)paramArrayList.get(j);
	      int k = paramT.id;
	      int m = localDataEntry.id;
	      if (k == m)
	        if (paramT.equals(localDataEntry))
	        {
	          String str2 = "checkForDuplicatesAndInsert <<< NOT CHANGED: " + localDataEntry;
	          logd(str2);
	          n = 0;
	        }
	    }
	    else
	    {
	      
	      String str3 = "checkForDuplicatesAndInsert <<< CHANGED: " + localDataEntry;
	      logd(str3);
	      localObject = paramArrayList.set(j, paramT);
	      n = 2;
	
	      if (paramT.isDuplicate(localDataEntry))
	      {
	        String str4 = "checkForDuplicatesAndInsert <<< DUPLICATE: " + localDataEntry;
	        logd(str4);
	        n = 1;
	    
	      }
	      j += 1;
	   
	      logd("checkForDuplicatesAndInsert <<< NEW");
	      boolean bool = paramArrayList.add(paramT);
	      n = 3;
	    }
	    return n;
	  }
//	 
//	private int checkForDuplicatesAndInsert(DataEntry dataentry, ArrayList arraylist)
//	{
//		int i;
//		int j;
//		logd((new StringBuilder()).append("checkForDuplicatesAndInsert >>> data=").append(dataentry).append(" entries=").append(arraylist).toString());
//		i = arraylist.size();
//		j = 0;
//_L1:
//		int k;
//		if (j >= i)
//			break MISSING_BLOCK_LABEL_191;
//		DataEntry dataentry1 = (DataEntry)arraylist.get(j);
//		if (dataentry.id == dataentry1.id)
//		{
//			if (dataentry.equals(dataentry1))
//			{
//				logd((new StringBuilder()).append("checkForDuplicatesAndInsert <<< NOT CHANGED: ").append(dataentry1).toString());
//				k = 0;
//			} else
//			{
//				logd((new StringBuilder()).append("checkForDuplicatesAndInsert <<< CHANGED: ").append(dataentry1).toString());
//				arraylist.set(j, dataentry);
//				k = 2;
//			}
//		} else
//		{
//label0:
//			{
//				if (!dataentry.isDuplicate(dataentry1))
//					break label0;
//				logd((new StringBuilder()).append("checkForDuplicatesAndInsert <<< DUPLICATE: ").append(dataentry1).toString());
//				k = 1;
//			}
//		}
//_L2:
//		return k;
//		j++;
//		  goto _L1
//		logd("checkForDuplicatesAndInsert <<< NEW");
//		arraylist.add(dataentry);
//		k = 3;
//		  goto _L2
//	}

	private static boolean deleteDataEntry(int i, ArrayList arraylist)
	{
		boolean flag = false;
		if (arraylist != null)
		{
			int j = arraylist.size();
			int k = 0;
			do
			{
				if (k >= j)
					break;
				if (((DataEntry)arraylist.get(k)).id == i)
				{
					arraylist.remove(k);
					flag = true;
					continue; /* Loop/switch isn't completed */
				}
				k++;
			} while (true);
		}
		else 
		{
			 flag = false;
		}
		return flag;
	}

	private void logd(String s)
	{
		logger.d((new StringBuilder()).append(logPrefix).append(s).toString());
	}

	private void loge(String s, Throwable throwable)
	{
		logger.e((new StringBuilder()).append(logPrefix).append(s).toString(), throwable);
	}

	abstract DataEntry createDataFromRow(Cursor cursor);

	void deleteData(int i, int j)
	{
		deleteDataEntry(i, getEntriesForContact(j));
		data2Contact.delete(i);
		dataVersions.delete(i);
		onDataDeleted(j, i);
	}

	void finishReloading()
	{
		int i = deletedData.size();
		for (int j = 0; j < i; j++)
		{
			int k = deletedData.valueAt(j);
			if (!isContactDeleted(k))
				deleteData(deletedData.keyAt(j), k);
		}

	}

	ArrayList getEntriesForContact(int i)
	{
		ArrayList arraylist = (ArrayList)dataPerContact.get(i);
		if (arraylist == null)
		{
			arraylist = new ArrayList();
			dataPerContact.put(i, arraylist);
		}
		return arraylist;
	}

	abstract boolean isContactDeleted(int i);

	void loadRow(Cursor cursor, boolean flag, int i)
	{
		int j = (int)cursor.getLong(9);
		int k = cursor.getInt(10);
		int l = dataVersions.get(j, -1);
		int i1 = data2Contact.get(j, -1);
		logd((new StringBuilder()).append("loadRow: dataId=").append(j).append(" dataVersion=").append(k).append(" storedVersion=").append(l).toString());
		if (k != l || i != i1 || flag)
			try
			{
				DataEntry dataentry = createDataFromRow(cursor);
				if (i != i1)
				{
					data2Contact.delete(j);
					deleteDataEntry(j, getEntriesForContact(i1));
					onDataDeleted(i1, j);
				}
				int j1 = checkForDuplicatesAndInsert(dataentry, getEntriesForContact(i));
				if (j1 != 1 && (j1 == 3 || j1 == 2 || flag))
					updateData(dataentry, i);
			}
			catch (Exception exception)
			{
				loge((new StringBuilder()).append("Failed to load data row: ").append(exception).toString(), exception);
				deleteData(j, i);
			}
		deletedData.delete(j);
	}

	abstract void onDataDeleted(int i, int j);

	abstract void onDataUpdated(int i, DataEntry dataentry);

	void startReloading()
	{
		deletedData.clear();
		int i = data2Contact.size();
		for (int j = 0; j < i; j++)
		{
			int k = data2Contact.valueAt(j);
			if (!isContactDeleted(k))
			{
				int l = data2Contact.keyAt(j);
				deletedData.put(l, k);
			}
		}

	}

	void updateData(DataEntry dataentry, int i)
	{
		int j = dataentry.id;
		int k = dataentry.version;
		dataVersions.put(j, k);
		dataById.put(j, dataentry);
		data2Contact.put(j, i);
		onDataUpdated(i, dataentry);
	}

}
