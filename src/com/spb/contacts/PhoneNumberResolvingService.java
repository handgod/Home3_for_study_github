// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Referenced classes of package com.spb.contacts:
//			ContactsServiceConstants, PhoneNumberCallbacksHelper, ContactsService, IPhoneNumberResolvingServiceCallback

class PhoneNumberResolvingService
	implements ContactsServiceConstants
{
	static class PhonesObserver extends ContentObserver
	{

		Handler handler;

		public void onChange(boolean flag)
		{
			handler.sendMessageAtFrontOfQueue(Message.obtain(handler, 2));
		}

		public PhonesObserver(Handler handler1)
		{
			super(handler1);
			handler = handler1;
		}
	}

	class ResolverHandler extends Handler
	{

		final PhoneNumberResolvingService this$0;

		public void handleMessage(Message message)
		{
			switch (message.what) {
			case 1:
				doResolvePhoneNumber((String)message.obj);
				break;
			case 2:
				sendMessageDelayed(Message.obtain(this, 3), 1000L);
				break;
			case 3:
				if (!hasMessages(3))
					doReloadPhones();
				break;
			case 4:
				doResolveUnresolvedNumbers();
				break;
			case 5:
				callbacksHelper.notifyResolvedPhonesChanged(((Integer)message.obj).intValue());
				break;
			default:
				break;
			}
			return;
		}

		public ResolverHandler(Looper looper)
		{
			super(looper);
			this$0 = PhoneNumberResolvingService.this;
		}
	}

	public static class PhoneLookupResult
	{

		long contactId;
		long dataId;

		public PhoneLookupResult()
		{
		}
	}


	private static final String CONTACT_PHONE_LOOKUP_PROJECTION[];
	private static final String DATA_PROJECTION[];
	private static final int INDEX_CONTACT_LOOKUP_KEY = 1;
	private static final int INDEX_DATA_CONTACT_ID = 1;
	private static final int INDEX_DATA_DATA_ID = 0;
	private static final int INDEX_DATA_ID = 0;
	private static final int INDEX_DATA_NUMBER = 3;
	private static final int INDEX_DATA_VERSION = 2;
	static final int MSG_DATA_CHANGED = 2;
	static final int MSG_DO_NOTIFY_LISTENERS = 5;
	static final int MSG_DO_RELOAD_PHONES = 3;
	static final int MSG_DO_RESOLVE_UNRESOLVED = 4;
	static final int MSG_NEW_PHONE_NUMBER = 1;
	private static final String SELECTION_PHONES = (new StringBuilder()).append("mimetype").append("='").append("vnd.android.cursor.item/phone_v2").append('\'').toString();
	static final Logger logger = Loggers.getLogger(PhoneNumberResolvingService.class.getName());
	final PhoneNumberCallbacksHelper callbacksHelper = new PhoneNumberCallbacksHelper();
	final SparseIntArray contactIdByPhoneId = new SparseIntArray();
	final ContactsService contactsService;
	private ContentResolver contentResolver;
	final SparseIntArray dataVersions = new SparseIntArray();
	private PhonesObserver observer;
	final SparseArray phoneNumbersByContactId = new SparseArray();
	final ConcurrentHashMap resolvedNumbers = new ConcurrentHashMap();
	ResolverHandler resolverHandler;
	final Set unresolvedNumbers = Collections.synchronizedSet(new HashSet());

	PhoneNumberResolvingService(ContactsService contactsservice)
	{
		contactsService = contactsservice;
		contentResolver = contactsservice.getContentResolver();
		HandlerThread handlerthread = new HandlerThread("PhoneNumberResolverService");
		handlerthread.start();
		resolverHandler = new ResolverHandler(handlerthread.getLooper());
		observer = new PhonesObserver(resolverHandler);
		contentResolver.registerContentObserver(android.provider.ContactsContract.Data.CONTENT_URI, true, observer);
		doReloadPhones();
	}

	/**
	 * @deprecated Method doReloadPhones is deprecated
	 */

	private synchronized void doReloadPhones()
	{
		long l;
		SparseIntArray sparseintarray;
		Cursor cursor;
		boolean flag;
		ArrayList arraylist;
		l = SystemClock.uptimeMillis();
		logger.d("doReloadPhones >>>");
		sparseintarray = new SparseIntArray();
		int i = contactIdByPhoneId.size();
		for (int j = 0; j < i; j++)
			sparseintarray.put(contactIdByPhoneId.keyAt(j), contactIdByPhoneId.valueAt(j));

		cursor = null;
		flag = false;
		arraylist = new ArrayList();
		cursor = contentResolver.query(android.provider.ContactsContract.Data.CONTENT_URI, DATA_PROJECTION, SELECTION_PHONES, null, null);
		if (cursor == null || !cursor.moveToFirst())
			return;

		int i3;
		int j3;
		int k3;
		int l3;
		int i4;
		while (!cursor.isAfterLast())
		{
		i3 = (int)cursor.getLong(0);
		j3 = (int)cursor.getLong(1);
		k3 = cursor.getInt(2);
		sparseintarray.delete(i3);
		l3 = dataVersions.get(i3, -1);
		i4 = contactIdByPhoneId.get(i3, -1);
		if (i4 != -1)
			break; /* Loop/switch isn't completed */
		String s2 = cursor.getString(3);
		logger.d((new StringBuilder()).append("doReloadPhones: new phone number: dataId=").append(i3).append(" contactId=").append(j3).append(" number=").append(s2).toString());
		if (!TextUtils.isEmpty(s2))
		{
			flag = true;
			long l4 = getResolvedContactId(s2);
			if (l4 != 0L && !arraylist.contains(Long.valueOf(l4)))
				arraylist.add(Integer.valueOf((int)l4));
		}

		contactIdByPhoneId.put(i3, j3);
		dataVersions.put(i3, k3);
		cursor.moveToNext();
		
		if (l3 == k3)
			return;
		logger.d((new StringBuilder()).append("doReloadPhones: phone number has changed: dataId=").append(i3).append(" contactId=").append(j3).append(" dataVersion=").append(k3).append(" previousDataVersion=").append(l3).toString());
		flag = true;
		if (!arraylist.contains(Integer.valueOf(i4)))
			arraylist.add(Integer.valueOf(j3));
		
		if (j3 != i4)
		{
			logger.d((new StringBuilder()).append("doReloadPhones: phone number has changed owner: dataId=").append(i3).append(" contactId=").append(j3).append(" previousCountatcId=").append(i4).toString());
			if (!arraylist.contains(Integer.valueOf(i4)))
				arraylist.add(Integer.valueOf(i4));
		}
	}
		int k = sparseintarray.size();
		for (int i1 = 0; i1 < k; i1++)
		{
			int k2 = sparseintarray.keyAt(i1);
			int l2 = sparseintarray.valueAt(i1);
			logger.d((new StringBuilder()).append("doReloadPhones: phone number data row was deleted: dataId=").append(k2).append(" contactId=").append(l2).toString());
			contactIdByPhoneId.delete(k2);
			dataVersions.delete(k2);
			arraylist.add(Integer.valueOf(l2));
		}

		int j1 = arraylist.size();
		if (j1 > 0)
		{
			for (int k1 = 0; k1 < j1; k1++)
			{
				int l1 = ((Integer)arraylist.get(k1)).intValue();
				ArrayList arraylist1 = (ArrayList)phoneNumbersByContactId.get(l1);
				Logger logger1 = logger;
				StringBuilder stringbuilder = (new StringBuilder()).append("doReloadPhones: invalidated contactId=").append(l1).append(" numbers=");
				String s;
				if (arraylist1 == null)
					s = "null";
				else
					s = Arrays.toString(arraylist1.toArray());
				logger1.d(stringbuilder.append(s).toString());
				postNotifyListeners(l1);
				if (arraylist1 != null)
				{
					int i2 = arraylist1.size();
					for (int j2 = 0; j2 < i2; j2++)
					{
						String s1 = (String)arraylist1.get(j2);
						resolvedNumbers.remove(s1);
						postResolveNumber(s1);
					}

				}
				phoneNumbersByContactId.remove(l1);
			}

		}
		if (flag)
		{
			logger.d("doReloadPhones: requesting re-resolving unresolved numbers");
			resolverHandler.sendMessage(Message.obtain(resolverHandler, 4));
		}

	}

	private void doResolveUnresolvedNumbers()
	{
		long l = SystemClock.uptimeMillis();
		String as[] = (String[])unresolvedNumbers.toArray(new String[unresolvedNumbers.size()]);
		logger.d((new StringBuilder()).append("doResolveUnresolvedNumbers >>> numbers=").append(Arrays.toString(as)).toString());
		int i = as.length;
		for (int j = 0; j < i; j++)
		{
			String s = as[j];
			unresolvedNumbers.remove(s);
			doResolvePhoneNumber(s);
		}

		logger.d((new StringBuilder()).append("doResolveUnresolvedNumbers <<< completed in").append(SystemClock.uptimeMillis() - l).append("ms").toString());
	}

	private void onNotResolved(String s)
	{
		int i;
		logger.d((new StringBuilder()).append("onNotResolved: number=").append(s).toString());
		unresolvedNumbers.add(s);
		PhoneLookupResult phonelookupresult = (PhoneLookupResult)resolvedNumbers.remove(s);
		if (phonelookupresult != null)
		{
		i = (int)phonelookupresult.contactId;
		postNotifyListeners(i);
		SparseArray sparsearray = phoneNumbersByContactId;
		synchronized (sparsearray) {
			ArrayList arraylist = (ArrayList)phoneNumbersByContactId.get(i);
				if (arraylist != null)
				{
					arraylist.remove(s);
					if (arraylist.size() == 0)
						phoneNumbersByContactId.remove(i);
				}
			}
		}
		
	}

	private void onResolved(String s, PhoneLookupResult phonelookupresult)
	{
		int i;
		logger.d((new StringBuilder()).append("onResolved: number=").append(s).append(" contactId=").append(phonelookupresult.contactId).append(" dataId=").append(phonelookupresult.dataId).toString());
		resolvedNumbers.put(s, phonelookupresult);
		unresolvedNumbers.remove(s);
		i = (int)phonelookupresult.contactId;
		postNotifyListeners(i);
		SparseArray sparsearray = phoneNumbersByContactId;
		synchronized (sparsearray) {
			ArrayList arraylist = (ArrayList)phoneNumbersByContactId.get(i);
			if (arraylist == null)
			{
				arraylist = new ArrayList();
				phoneNumbersByContactId.put(i, arraylist);
			}
			if (!arraylist.contains(s))
				arraylist.add(s);
		}
		
		return;
	}

	private void postNotifyListeners(int i)
	{
		Integer integer = Integer.valueOf(i);
		if (!resolverHandler.hasMessages(5, integer))
			resolverHandler.sendMessage(Message.obtain(resolverHandler, 5, integer));
	}

	private void postResolveNumber(String s)
	{
		String s1 = s.intern();
		if (!resolverHandler.hasMessages(1, s1))
		{
			logger.d((new StringBuilder()).append("postResolveNumber: adding phone number to resolver queue: ").append(s).toString());
			resolverHandler.sendMessage(Message.obtain(resolverHandler, 1, s1));
		} else
		{
			logger.d((new StringBuilder()).append("postResolveNumber: phone number already in queue: ").append(s).toString());
		}
	}

	public void addPhoneNumber(String s)
	{
		if (resolvedNumbers.containsKey(s))
			logger.d((new StringBuilder()).append("addPhoneNumber: phoneNumber=").append(s).append(": already resolved").toString());
		else
		if (unresolvedNumbers.contains(s))
			logger.d((new StringBuilder()).append("addPhoneNumber: phoneNumber=").append(s).append(": already unresolved").toString());
		else
			postResolveNumber(s);
	}

	void doResolvePhoneNumber(String s)
	{
		logger.d((new StringBuilder()).append("doResolvePhoneNumber: ").append(s).toString());
		PhoneLookupResult phonelookupresult = lookup(s);
		if (phonelookupresult != null)
			onResolved(s, phonelookupresult);
		else
			onNotResolved(s);
	}

	public long getResolvedContactId(String s)
	{
		long l = 0L;
		if (!TextUtils.isEmpty(s)) 
		{
			PhoneLookupResult phonelookupresult = (PhoneLookupResult)resolvedNumbers.get(s);
			if (phonelookupresult != null)
				l = phonelookupresult.contactId;
		}
		return l;
	}

	List getResolvedPhoneNumbers(int i)
	{
		SparseArray sparsearray = phoneNumbersByContactId;
		List list ;
		synchronized (sparsearray) {
			list = (List)phoneNumbersByContactId.get(i);
		}
		return list;
	}

	PhoneLookupResult lookup(String s)
	{
		PhoneLookupResult phonelookupresult = null;
		Cursor cursor = null;
		if (s != null && !"".equals(s)) 
		{
			Uri uri = Uri.withAppendedPath(android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(s));
			cursor = contentResolver.query(uri, CONTACT_PHONE_LOOKUP_PROJECTION, null, null, null);
			if (cursor == null || !cursor.moveToFirst())
				return phonelookupresult;
			String s1 = cursor.getString(1);
			long l = cursor.getLong(0);
			cursor.close();
			Uri uri1 = android.provider.ContactsContract.Contacts.getLookupUri(0L, s1);
			Uri uri2 = android.provider.ContactsContract.Contacts.lookupContact(contentResolver, uri1);
			if (uri2 == null)
				return phonelookupresult;
			long l1 = ContentUris.parseId(uri2);
			phonelookupresult = new PhoneLookupResult();
			phonelookupresult.contactId = l1;
			phonelookupresult.dataId = l;
			
		}
		else 
		{
			phonelookupresult = null;
		}
		return phonelookupresult;
	}

	public long lookupContactId(String s)
	{
		PhoneLookupResult phonelookupresult = lookup(s);
		long l;
		if (phonelookupresult == null)
			l = 0L;
		else
			l = phonelookupresult.contactId;
		return l;
	}

	void registerCallback(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
		throws RemoteException
	{
		if (iphonenumberresolvingservicecallback != null)
			callbacksHelper.register(iphonenumberresolvingservicecallback);
	}

	public void removePhoneNumber(String s)
	{
		resolverHandler.removeMessages(1, s.intern());
		resolvedNumbers.remove(s);
		unresolvedNumbers.remove(s);
	}

	void stop()
	{
		contentResolver.unregisterContentObserver(observer);
		resolverHandler.removeCallbacksAndMessages(null);
		resolverHandler.getLooper().quit();
	}

	void unregisterCallback(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
		throws RemoteException
	{
		if (iphonenumberresolvingservicecallback != null)
			callbacksHelper.unregister(iphonenumberresolvingservicecallback);
	}

	static 
	{
		String as[] = new String[2];
		as[0] = "_id";
		as[1] = "lookup";
		CONTACT_PHONE_LOOKUP_PROJECTION = as;
		String as1[] = new String[4];
		as1[0] = "_id";
		as1[1] = "contact_id";
		as1[2] = "data_version";
		as1[3] = "data1";
		DATA_PROJECTION = as1;
	}


}
