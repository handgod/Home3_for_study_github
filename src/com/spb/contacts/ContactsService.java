// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.os.Process;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.SparseIntArray;
import com.softspb.util.DelayedHandler;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Referenced classes of package com.spb.contacts:
//			ContactsServiceConstants, ContactsDataProjection, ContactsCallbacksHelper, ObservableContacts, 
//			PhoneNumberResolvingService, StructuredName, IContactsService, IPhoneNumberResolvingService, 
//			IGetPid, ObservableData, DataEntry, ConnectionEntry, 
//			EventEntry, ContactsUtils, IContactsServiceCallback, IPhoneNumberResolvingServiceCallback

public class ContactsService extends Service
	implements ContactsServiceConstants, ContactsDataProjection
{
	private final class UpdateContactsAndBirthdaysRunnable
		implements Runnable
	{

		private final boolean doNotifyAll;
		final ContactsService this$0;

		public void run()
		{
			ContactsService.logd("updateContactsAndBirthdaysCallback.run");
			reloadContactsLock.lock();
			callbacksHelper.notifyStartedReload(2);
			callbacksHelper.notifyStartedReloadingBirthdays();
			doReloadBirthdays(1, doNotifyAll);
			callbacksHelper.notifyFinishedReloadingBirthdays();
			doReloadContacts(2);
			callbacksHelper.notifyFinishedReload(2);
			reloadContactsLock.unlock();
			return;
		}

		UpdateContactsAndBirthdaysRunnable(boolean flag)
		{
			super();
			this$0 = ContactsService.this;
			doNotifyAll = flag;
		}
	}

	class ContactsObserver extends ContentObserver
	{

		DelayedHandler delayedHandler;
		final ContactsService this$0;

		public void onChange(boolean flag)
		{
			boolean flag1 = callbacksHelper.hasRegisteredCallbacks();
			ContactsService.logd((new StringBuilder()).append("ContactsObserver.onChange: hasRegisteredCallbacks=").append(flag1).toString());
			if (flag1)
				delayedHandler.handleDelayed(updateContactsAndBirthdaysRunnable_notifyOnlyChanged);
		}

		ContactsObserver(DelayedHandler delayedhandler)
		{
			super(delayedhandler);
			this$0 = ContactsService.this;
			delayedHandler = delayedhandler;
		}
	}

	class ObservableNames extends ObservableData
	{

		final ContactsService this$0;

		private StructuredName getPrimaryStucturedName(int i)
		{
			ContactsService.logd((new StringBuilder()).append("getPrimaryStucturedName >>> contactId=").append(i).toString());
			ArrayList arraylist = getEntriesForContact(i);
			int j;
			StructuredName structuredname;
			if (arraylist == null)
				j = 0;
			else
				j = arraylist.size();
			if (j == 0)
			{
				structuredname = null;
			} else
			{
				String s = allContacts.getDisplayName(i);
				if (s == null)
					s = favoriteContacts.getDisplayName(i);
				structuredname = null;
				for (int k = 0; k < j; k++)
				{
					StructuredName structuredname1 = (StructuredName)arraylist.get(k);
					if (compareStructuredNames(structuredname1, structuredname, s) > 0)
						structuredname = structuredname1;
					ContactsService.logd((new StringBuilder()).append("getPrimaryStucturedName: bestName=").append(structuredname).toString());
				}

				ContactsService.logd((new StringBuilder()).append("getPrimaryStucturedName <<< contactId=").append(i).append(" bestName=").append(structuredname).toString());
			}
			return structuredname;
		}

//		DataEntry createDataFromRow(Cursor cursor)
//		{
//			return createDataFromRow(cursor);
//		}

		StructuredName createDataFromRow(Cursor cursor)
		{
			int i = (int)cursor.getLong(9);
			int j = cursor.getInt(10);
			String s = cursor.getString(4);
			String s1 = cursor.getString(5);
			return new StructuredName(i, j, cursor.getString(3), s, s1);
		}

		boolean isContactDeleted(int i)
		{
			boolean flag;
			if (!allContacts.hasContact(i))
				flag = true;
			else
				flag = false;
			return flag;
		}

		void notifyStructuredNameChanged(int i)
		{
			StructuredName structuredname = getPrimaryStucturedName(i);
			if (structuredname == null)
				callbacksHelper.notifyStructuredNameChanged(i, null, null);
			else
				callbacksHelper.notifyStructuredNameChanged(i, structuredname.firstName, structuredname.lastName);
		}

		void onDataDeleted(int i, int j)
		{
			notifyStructuredNameChanged(i);
		}

		 void onDataUpdated(int i, DataEntry dataentry)
		{
			onDataUpdated(i, (StructuredName)dataentry);
		}

		void onDataUpdated(int i, StructuredName structuredname)
		{
			notifyStructuredNameChanged(i);
		}

		ObservableNames()
		{
			super("Names", new String[]{"vnd.android.cursor.item/name"});
//			super("Names", as);
			String as[] = new String[1];
			this$0 = ContactsService.this;
			as[0] = "vnd.android.cursor.item/name";
		}
	}

	class ObservableConnections extends ObservableData
	{

		final ContactsService this$0;

		ConnectionEntry createDataFromRow(Cursor cursor)
		{
			int i = (int)cursor.getLong(9);
			int j = cursor.getInt(10);
			String s = cursor.getString(3);
			String s1 = cursor.getString(2);
			int k = cursor.getInt(4);
			String s2 = cursor.getString(5);
			return new ConnectionEntry(i, j, s, s1, k, ContactsService.getConnectionTypeLabel(getResources(), s1, s2, k));
		}

//		DataEntry createDataFromRow(Cursor cursor)
//		{
//			return createDataFromRow(cursor);
//		}

		boolean isContactDeleted(int i)
		{
			boolean flag;
			if (!allContacts.hasContact(i))
				flag = true;
			else
				flag = false;
			return flag;
		}

		void onDataDeleted(int i, int j)
		{
			callbacksHelper.notifyConnectionDeleted(i, j, currentContactsKind);
		}

		void onDataUpdated(int i, ConnectionEntry connectionentry)
		{
			callbacksHelper.notifyConnectionUpdated(i, connectionentry.id, connectionentry.mimetype, connectionentry.locationType, connectionentry.address, connectionentry.label, connectionentry.version);
		}

		 void onDataUpdated(int i, DataEntry dataentry)
		{
			onDataUpdated(i, (ConnectionEntry)dataentry);
		}

		 ObservableConnections(String s, String as[])
		{
			 super(s, as);
			this$0 = ContactsService.this;
		}
	}

	class ObservableBirthdays extends ObservableData
	{

		private Time now;
		final ContactsService this$0;
//
//		 DataEntry createDataFromRow(Cursor cursor)
//		{
//			return createDataFromRow(cursor);
//		}

		EventEntry createDataFromRow(Cursor cursor)
		{
			return new EventEntry((int)cursor.getLong(9), cursor.getInt(10), ContactsUtils.parseBirthdayDate(cursor.getString(3), now.year, localTimeZone, ContactsService.logger), cursor.getInt(4));
		}

		boolean isContactDeleted(int i)
		{
			boolean flag;
			if (!allContacts.hasContact(i))
				flag = true;
			else
				flag = false;
			return flag;
		}

		void onDataDeleted(int i, int j)
		{
			callbacksHelper.notifyBirthdayDeleted(j);
		}

		void onDataUpdated(int i, DataEntry dataentry)
		{
			onDataUpdated(i, (EventEntry)dataentry);
		}

		void onDataUpdated(int i, EventEntry evententry)
		{
			callbacksHelper.notifyBirthdayUpdated(evententry.type, i, evententry.id, evententry.date.year, evententry.date.month, evententry.date.monthDay);
		}

		void startReloading()
		{
			super.startReloading();
			if (now == null)
				now = new Time();
			now.setToNow();
		}

		ObservableBirthdays()
		{
			super("Birthdays", new String[]{"vnd.android.cursor.item/contact_event"});
//			super("Birthdays", as);
			this$0 = ContactsService.this;
			String as[] = new String[1];
			as[0] = "vnd.android.cursor.item/contact_event";
		}
	}

	class ObservableEvents extends ObservableData
	{

		final ContactsService this$0;

//		volatile DataEntry createDataFromRow(Cursor cursor)
//		{
//			return createDataFromRow(cursor);
//		}

		EventEntry createDataFromRow(Cursor cursor)
		{
			int i = (int)cursor.getLong(9);
			int j = cursor.getInt(10);
			int k = (int)cursor.getLong(0);
			String s = cursor.getString(3);
			Time time = ContactsUtils.parseEventDate(s, null);
			StringBuilder stringbuilder = (new StringBuilder()).append("loading event data: contactId=").append(k).append(" dateSrc=").append(s).append(" parsedDate=");
			String s1;
			if (time == null)
				s1 = "null";
			else
				s1 = time.format3339(true);
			ContactsService.logd(stringbuilder.append(s1).toString());
			return new EventEntry(i, j, time, cursor.getInt(4));
		}

		boolean isContactDeleted(int i)
		{
			boolean flag;
			if (!allContacts.hasContact(i))
				flag = true;
			else
				flag = false;
			return flag;
		}

		void onDataDeleted(int i, int j)
		{
			callbacksHelper.notifyEventDeleted(i, j, currentContactsKind);
		}

		 void onDataUpdated(int i, DataEntry dataentry)
		{
			onDataUpdated(i, (EventEntry)dataentry);
		}

		void onDataUpdated(int i, EventEntry evententry)
		{
			callbacksHelper.notifyEventUpdated(i, evententry.id, evententry.type, evententry.date, evententry.version);
		}

		ObservableEvents()
		{
			super("Events", new String[]{"vnd.android.cursor.item/contact_event"});
//			super("Events", as);
			this$0 = ContactsService.this;
			String as[] = new String[1];
			as[0] = "vnd.android.cursor.item/contact_event";
		}
	}

	class ObservableFavoriteContacts extends ObservableContacts
	{

		final ContactsService this$0;

		void onContactDeleted(int i)
		{
			callbacksHelper.notifyContactDeleted(i, 2);
		}

		void onContactUpdated(int i, String s, String s1, boolean flag, int j, int k)
		{
			ContactsService.logd((new StringBuilder()).append("onContactUpdated: contactId=").append(i).append(" displayName=").append(s1).append(" isStarred=").append(flag).append(" photoId=").append(j).toString());
			callbacksHelper.notifyContactUpdated(i, s, s1, flag, j, k);
			names.notifyStructuredNameChanged(i);
		}

		ObservableFavoriteContacts()
		{
			super("FavoriteContacts", photoVersions);
			this$0 = ContactsService.this;
		}
	}

	class ObservableAllContacts extends ObservableContacts
	{

		final ContactsService this$0;

		void onContactDeleted(int i)
		{
			callbacksHelper.notifyContactDeleted(i, 1);
		}

		void onContactUpdated(int i, String s, String s1, boolean flag, int j, int k)
		{
			ContactsService.logd((new StringBuilder()).append("onContactUpdated: contactId=").append(i).append(" displayName=").append(s1).append(" isStarred=").append(flag).append(" photoId=").append(j).toString());
			callbacksHelper.notifyContactUpdated(i, s, s1, flag, j, k);
			names.notifyStructuredNameChanged(i);
		}

		ObservableAllContacts()
		{
			super("AllContacts", photoVersions);
			this$0 = ContactsService.this;
		}
	}


	private static final String ALL_CONTACTS_SELECTION;
	private static final String ALL_CONTACTS_SELECTION_ARGS[];
	private static final String CONTACT_ID_SELECTION;
	private static final String FAVORITES_SELECTION;
	private static final String FAVORITE_SELECTION_ARGS[];
	static final String PROJECTION_DATA_VERSIONS[];
	static final String SELECTION_ARGS_BIRTHDAYS[];
	static final String SELECTION_ARGS_PHOTO_MIMETYPE[];
	private static final String SELECTION_BIRTHDAYS = (new StringBuilder()).append("(").append("mimetype").append("=?").append(") AND (").append("data2").append("=?").append(" OR ").append("data2").append("=?").append(")").toString();
	private static final String SELECTION_BIRTHDAYS_FAVORITES = (new StringBuilder()).append("(").append("mimetype").append("=?").append(") AND (").append("data2").append("=?").append(" OR ").append("data2").append("=?").append(") AND (").append("starred").append("=1)").toString();
	static final String SELECTION_DATA_MIMETYPE = (new StringBuilder()).append("mimetype").append("=?").toString();
	static final String SELECTION_DATA_MIMETYPE_FAVORITE = (new StringBuilder()).append("(").append("mimetype").append("=?").append(") AND (").append("starred").append("=1)").toString();
	private static final String SORT_ORDER_DISPLAY_NAME_ID_ASC = "display_name ASC, _id ASC";
	private static final Logger logger = Loggers.getLogger(ContactsService.class.getName());
	final ObservableContacts allContacts = new ObservableAllContacts();
	final ObservableBirthdays birthdays = new ObservableBirthdays();
	final ContactsCallbacksHelper callbacksHelper = new ContactsCallbacksHelper();
	private final SparseIntArray contactIdByPhotoId = new SparseIntArray();
	DelayedHandler contactsHandler;
	ContactsObserver contactsObserver;
	private final IContactsService.Stub contactsServiceBinder = new IContactsService.Stub() {

		final ContactsService this$0;

		public void crash()
			throws RemoteException
		{
			ContactsService.logger.d("crash");
			throw new Error("ContactsService has crashed");
		}

		public void registerCallback(IContactsServiceCallback icontactsservicecallback)
			throws RemoteException
		{
			ContactsService.logger.d((new StringBuilder()).append("registerCallback: ").append(icontactsservicecallback).toString());
			if (icontactsservicecallback != null)
				callbacksHelper.register(icontactsservicecallback);
		}

		public void reloadBirthdays(int i, boolean flag)
			throws RemoteException
		{
			ContactsService.logd((new StringBuilder()).append("IContactsService.reloadBirthdays >>> kins=").append(i).append(" notifyAll=").append(flag).toString());
			reloadContactsLock.lock();
			callbacksHelper.notifyStartedReloadingBirthdays();
			doReloadBirthdays(i, flag);
			callbacksHelper.notifyFinishedReloadingBirthdays();
			reloadContactsLock.unlock();
			ContactsService.logd((new StringBuilder()).append("IContactsService.reloadBirthdays <<< kins=").append(i).append(" notifyAll=").append(flag).toString());
		}

		public void reloadContact(int i)
			throws RemoteException
		{
			doReloadContact(i);
		}

		public void reloadContacts(int i)
			throws RemoteException
		{
			reloadContactsLock.lock();
			doReloadContacts(i);
			reloadContactsLock.unlock();
			return;
		}

		public void unregisterCallback(IContactsServiceCallback icontactsservicecallback)
			throws RemoteException
		{
			ContactsService.logger.d((new StringBuilder()).append("unregisterCallback: ").append(icontactsservicecallback).toString());
			if (icontactsservicecallback != null)
				callbacksHelper.unregister(icontactsservicecallback);
		}

			
			{
				this$0 = ContactsService.this;
//				super();
			}
	};
	volatile boolean contactsServiceIsBound;
	private int currentContactsKind;
	final ObservableEvents events = new ObservableEvents();
	final ObservableContacts favoriteContacts = new ObservableFavoriteContacts();
	private final IGetPid.Stub getPidBinder = new IGetPid.Stub() {

		final ContactsService this$0;

		public int getPid()
			throws RemoteException
		{
			return Process.myPid();
		}

			
			{
//				super();
				this$0 = ContactsService.this;
			}
	};
	private TimeZone localTimeZone;
	final ObservableNames names = new ObservableNames();
	final ObservableConnections otherConnections;
	PhoneNumberResolvingService phoneNumberResolvingService;
	private final IPhoneNumberResolvingService.Stub phoneNumberResolvingServiceBinder = new IPhoneNumberResolvingService.Stub() {

		final ContactsService this$0;

		public void addPhoneNumber(String s)
			throws RemoteException
		{
			phoneNumberResolvingService.addPhoneNumber(s);
		}

		public long getResolvedContactId(String s)
			throws RemoteException
		{
			return phoneNumberResolvingService.getResolvedContactId(s);
		}

		public List getResolvedPhoneNumbers(int i)
			throws RemoteException
		{
			return phoneNumberResolvingService.getResolvedPhoneNumbers(i);
		}

		public void registerCallback(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
			throws RemoteException
		{
			phoneNumberResolvingService.registerCallback(iphonenumberresolvingservicecallback);
		}

		public void removePhoneNumber(String s)
			throws RemoteException
		{
			phoneNumberResolvingService.removePhoneNumber(s);
		}

		public void unregisterCallback(IPhoneNumberResolvingServiceCallback iphonenumberresolvingservicecallback)
			throws RemoteException
		{
			phoneNumberResolvingService.unregisterCallback(iphonenumberresolvingservicecallback);
		}

			
			{
				this$0 = ContactsService.this;
//				super();
			}
	};
	volatile boolean phoneNumberResolvingServiceIsBound;
	final ObservableConnections phones;
	private final SparseIntArray photoVersions = new SparseIntArray();
	private final Lock reloadContactsLock = new ReentrantLock();
	volatile boolean serviceIsStarted;
	private final UpdateContactsAndBirthdaysRunnable updateContactsAndBirthdaysRunnable_notifyAll = new UpdateContactsAndBirthdaysRunnable(true);
	private final UpdateContactsAndBirthdaysRunnable updateContactsAndBirthdaysRunnable_notifyOnlyChanged = new UpdateContactsAndBirthdaysRunnable(false);

	public ContactsService()
	{
		String as[] = new String[1];
		as[0] = "vnd.android.cursor.item/phone_v2";
		phones = new ObservableConnections("Phones", as);
		String as1[] = new String[2];
		as1[0] = "vnd.android.cursor.item/email_v2";
		as1[1] = "vnd.android.cursor.item/im";
		otherConnections = new ObservableConnections("Connections", as1);
		contactsServiceIsBound = false;
		phoneNumberResolvingServiceIsBound = false;
		serviceIsStarted = false;
	}

	  private void doReloadBirthdays(int paramInt, boolean paramBoolean)
	  {
	    logd("reloadBirthdays >>>");
	    long l1 = SystemClock.uptimeMillis();
	    String str = null;
	    if (paramInt == 2)
	      str = SELECTION_BIRTHDAYS_FAVORITES;
	    while (true)
	    {
	      Uri localUri = ContactsContract.Data.CONTENT_URI;
	      Cursor localCursor;
	      try
	      {
	        TimeZone localTimeZone1 = TimeZone.getDefault();
	        this.localTimeZone = localTimeZone1;
	        this.birthdays.startReloading();
	        ContentResolver localContentResolver = getContentResolver();
	        String[] arrayOfString1 = CONTACTS_DATA_PROJECTION;
	        String[] arrayOfString2 = SELECTION_ARGS_BIRTHDAYS;
	        localCursor = localContentResolver.query(localUri, arrayOfString1, str, arrayOfString2, null);
	        if ((localCursor != null) && (localCursor.moveToFirst()))
	          while (!localCursor.isAfterLast())
	          {
	            int i = (int)localCursor.getLong(0);
	            this.birthdays.loadRow(localCursor, paramBoolean, i);
	            boolean bool = localCursor.moveToNext();
	          }
	      }
	      finally
	      {
	        
	      }
	      try
	      {
	        localCursor.close();
	       long l2 = SystemClock.uptimeMillis() - l1;
	        logd("reloadBirthdays <<< " + l2 + " ms");
	       
	        str = SELECTION_BIRTHDAYS;
	     
	        this.birthdays.finishReloading();
	        if (localCursor != null);
	        try
	        {
	          localCursor.close();
	         long l3 = SystemClock.uptimeMillis() - l1;
	          logd("reloadBirthdays <<< " + l3 + " ms");
	          return;
	        }
	        catch (Exception localException1)
	        {
	         
	        }
	      }
	      catch (Exception localException2)
	      {
	       
	      }
	    }
	  }

	private void doReloadContact(int i)
	{
		long l;
		Cursor cursor;
		l = SystemClock.uptimeMillis();
		logd((new StringBuilder()).append("getContact >>> contactId=").append(i).toString());
		cursor = null;
		reloadContactsLock.lock();
		callbacksHelper.notifyStartedReload(currentContactsKind);
		cursor = getContentResolver().query(android.provider.ContactsContract.Data.CONTENT_URI, CONTACTS_DATA_PROJECTION, CONTACT_ID_SELECTION, getContactIDSelectionArgs(i), null);
		if (cursor != null)
			loadContactDataCursor(cursor);
		if (cursor != null)
			cursor.close();
		long l2 = SystemClock.uptimeMillis() - l;
		logd((new StringBuilder()).append("getContact <<< ").append(l2).append(" ms").toString());
		callbacksHelper.notifyFinishedReload(currentContactsKind);
		reloadContactsLock.unlock();
		return;
	
	}

	private void doReloadContacts(int i)
	{
		long l;
		Cursor cursor;
		currentContactsKind = i;
		l = SystemClock.uptimeMillis();
		logd((new StringBuilder()).append("reloadContacts >>> kind=").append(i).toString());
		cursor = null;
		reloadPhotoVersions(1);
		startReloading(i);
		cursor = queryContacts(i);
		if (cursor != null)
			loadContactDataCursor(cursor);
		finishReloading(i);
		Exception exception;
		Exception exception2;
		if (cursor != null)
			try
			{
				cursor.close();
			}
			catch (Exception exception4) { }
		logd((new StringBuilder()).append("reloadContacts <<< ").append(SystemClock.uptimeMillis() - l).append(" ms").toString());
	}

	private void finishReloading(int i)
	{
		logd((new StringBuilder()).append("finishReloading: kind=").append(i).toString());
		if (i == 1)
			allContacts.finishReloading();
		favoriteContacts.finishReloading();
		events.finishReloading();
		phones.finishReloading();
		otherConnections.finishReloading();
		names.finishReloading();
	}

	static String getConnectionTypeLabel(Resources resources, String s, String s1, int i)
	{
		String s2;
		if ("vnd.android.cursor.item/phone_v2".equals(s))
			s2 = android.provider.ContactsContract.CommonDataKinds.Phone.getTypeLabel(resources, i, s1).toString();
		else
		if ("vnd.android.cursor.item/email_v2".equals(s))
			s2 = android.provider.ContactsContract.CommonDataKinds.Email.getTypeLabel(resources, i, s1).toString();
		else
		if ("vnd.android.cursor.item/im".equals(s))
			s2 = android.provider.ContactsContract.CommonDataKinds.Im.getProtocolLabel(resources, i, s1).toString();
		else
			s2 = android.provider.ContactsContract.CommonDataKinds.Phone.getTypeLabel(resources, 2, null).toString();
		return s2;
	}

	private static String[] getContactIDSelectionArgs(int i)
	{
		String as[] = new String[1 + ALL_CONTACTS_SELECTION_ARGS.length];
		System.arraycopy(ALL_CONTACTS_SELECTION_ARGS, 0, as, 0, ALL_CONTACTS_SELECTION_ARGS.length);
		as[ALL_CONTACTS_SELECTION_ARGS.length] = Integer.toString(i);
		return as;
	}

	private void loadContactDataCursor(Cursor cursor)
	{
		long l;
		l = SystemClock.uptimeMillis();
		logd("loadContactDataCursor >>>");
		int i;
		if (cursor.moveToFirst())
			do
				i = loadOneContact(cursor);
			while (i != 0x80000000);
		long l2 = SystemClock.uptimeMillis() - l;
		logd((new StringBuilder()).append("loadContactDataCursor <<< ").append(l2).append(" ms").toString());
		return;

	}
	 private boolean loadDataRow(Cursor paramCursor, int paramInt)
	  {
		 boolean flag = false;
	    int j = (int)paramCursor.getLong(0);
	    if (paramInt != j)
	      return flag;
	    String str = paramCursor.getString(2);
	    if ("vnd.android.cursor.item/phone_v2".equals(str))
	      this.phones.loadRow(paramCursor, false, paramInt);
	    else
	    {
	    
	      if (("vnd.android.cursor.item/email_v2".equals(str)) || ("vnd.android.cursor.item/im".equals(str)))
	      {
	        this.otherConnections.loadRow(paramCursor, false, paramInt);
	       
	      }
	      if ("vnd.android.cursor.item/contact_event".equals(str))
	      {
	        this.events.loadRow(paramCursor, false, paramInt);
	    
	      }
	      if (!"vnd.android.cursor.item/name".equals(str)) ;
	      this.names.loadRow(paramCursor, false, paramInt);
	    }
	    flag = true;
	    return flag;
	  }

	private int loadOneContact(Cursor cursor)
	{
		int i = 0;
		if (!cursor.isAfterLast())
		{
			i = (int)cursor.getLong(0);
			logd((new StringBuilder()).append("loadOneContact >>> contactId=").append(i).toString());
			callbacksHelper.notifyStartedUpdatingContact(i, currentContactsKind);
			if (currentContactsKind == 1)
				allContacts.loadContact(cursor);
			favoriteContacts.loadContact(cursor);
			while(! (cursor.isAfterLast()))
			{
				callbacksHelper.notifyFinishedUpdatingContact(i, currentContactsKind);
				logd((new StringBuilder()).append("loadOneContact <<< contactId=").append(i).toString());
				cursor.moveToNext();

			}
		}		
		return i;
	}

	private static void logd(String s)
	{
		Thread thread = Thread.currentThread();
		String s1 = (new StringBuilder()).append("[Thread id=").append(thread.getId()).append(" name=").append(thread.getName()).append("] ").append(s).toString();
		logger.d(s1);
	}

	private Cursor queryContacts(int i)
	{
		android.net.Uri uri;
		String s;
		String as[];
		if (i == 2)
		{
			uri = android.provider.ContactsContract.Data.CONTENT_URI;
			s = FAVORITES_SELECTION;
			as = FAVORITE_SELECTION_ARGS;
		} else
		{
			uri = android.provider.ContactsContract.Data.CONTENT_URI;
			s = ALL_CONTACTS_SELECTION;
			as = ALL_CONTACTS_SELECTION_ARGS;
		}
		return getContentResolver().query(uri, CONTACTS_DATA_PROJECTION, s, as, "display_name ASC, _id ASC");
	}

	private void reloadPhotoVersions(int i)
	{
		logd("reloadPhotoVersions >>>");
		long l = SystemClock.uptimeMillis();
		String s;
		long l1;
		if (i == 2)
			s = SELECTION_DATA_MIMETYPE_FAVORITE;
		else
			s = SELECTION_DATA_MIMETYPE;
		reloadPhotoVersions(s, SELECTION_ARGS_PHOTO_MIMETYPE);
		l1 = SystemClock.uptimeMillis() - l;
		logd((new StringBuilder()).append("reloadPhotoVersions <<< ").append(l1).append(" ms").toString());
	}

	 private void reloadPhotoVersions(String paramString, String[] paramArrayOfString)
	  {
	    Cursor localCursor = null;
	    SparseIntArray localSparseIntArray1 = this.photoVersions;
	    SparseIntArray localSparseIntArray2 = this.contactIdByPhotoId;
	    Uri localUri = ContactsContract.Data.CONTENT_URI;
	    this.reloadContactsLock.lock();
	    SparseIntArray localSparseIntArray3;
	    int i;
	    int j;
	    int i1;
	    try
	    {
	      localSparseIntArray3 = new SparseIntArray();
	      i = localSparseIntArray1.size();
	      j = 0;
	      while (j < i)
	      {
	        int k = this.photoVersions.keyAt(j);
	        localSparseIntArray3.put(k, 1);
	        j += 1;
	      }
	      ContentResolver localContentResolver = getContentResolver();
	      String[] arrayOfString1 = PROJECTION_DATA_VERSIONS;
	      String str = paramString;
	      String[] arrayOfString2 = paramArrayOfString;
	      localCursor = localContentResolver.query(localUri, arrayOfString1, str, arrayOfString2, null);
	      if ((localCursor != null) && (localCursor.moveToFirst()))
	        while (!localCursor.isAfterLast())
	        {
	          int m = (int)localCursor.getLong(0);
	          int n = localCursor.getInt(1);
	          i1 = (int)localCursor.getLong(2);
	          int i2 = localSparseIntArray1.get(m, -2147483648);
	          int i3 = localSparseIntArray2.get(m, -2147483648);
	          if (i2 != n)
	          {
	            localSparseIntArray1.put(m, n);
	            this.callbacksHelper.notifyContactPhotoUpdated(i1, m, n);
	          }
	          if (i3 != i1)
	          {
	            localSparseIntArray2.put(m, i1);
	            if (i3 != -2147483648)
	            {
	              ContactsCallbacksHelper localContactsCallbacksHelper = this.callbacksHelper;
	              int i4 = i3;
	              localContactsCallbacksHelper.notifyContactPhotoUpdated(i4, m, n);
	            }
	            if (i2 == n)
	              this.callbacksHelper.notifyContactPhotoUpdated(i1, m, n);
	          }
	          localSparseIntArray3.delete(m);
	          boolean bool = localCursor.moveToNext();
	        }
	    }
	    finally
	    {
	      this.reloadContactsLock.unlock();
	      if (localCursor == null);
	    }
	    try
	    {
	      localCursor.close();

	      i = localSparseIntArray3.size();
	      j = 0;
	      while (j < i)
	      {
	        int i5 = localSparseIntArray3.keyAt(j);
	        i1 = localSparseIntArray2.get(i5, -2147483648);
	        if (i1 != -2147483648)
	          this.callbacksHelper.notifyContactPhotoUpdated(i1, 0, 0);
	        localSparseIntArray1.delete(i5);
	        localSparseIntArray2.delete(i5);
	        j += 1;
	      }
	      this.reloadContactsLock.unlock();
	      if (localCursor != null);
	      try
	      {
	        localCursor.close();

	      }
	      catch (Exception localException1)
	      {

	      }
	    }
	    catch (Exception localException2)
	    {

	    }
	  }

	private void startReloading(int i)
	{
		if (i == 1)
			allContacts.startReloading();
		favoriteContacts.startReloading();
		events.startReloading();
		phones.startReloading();
		otherConnections.startReloading();
		names.startReloading();
	}

	/**
	 * @deprecated Method startService is deprecated
	 */

	private synchronized void startService()
	{

		logd("startService >>>");
		HandlerThread handlerthread = new HandlerThread("ContactsAdapter_Observer");
		handlerthread.start();
		handlerthread.setPriority(1);
		contactsHandler = new DelayedHandler(handlerthread.getLooper(), 1000L);
		contactsObserver = new ContactsObserver(contactsHandler);
		ContentResolver contentresolver = getContentResolver();
		contentresolver.registerContentObserver(android.provider.ContactsContract.Contacts.CONTENT_URI, true, contactsObserver);
		contentresolver.registerContentObserver(android.provider.ContactsContract.Data.CONTENT_URI, true, contactsObserver);
		phoneNumberResolvingService = new PhoneNumberResolvingService(this);
		logd("startService <<<");
		serviceIsStarted = true;
	}

	/**
	 * @deprecated Method stopService is deprecated
	 */

	private synchronized void stopService()
	{

		logd("stopService >>>");
		if (phoneNumberResolvingService != null)
		{
			phoneNumberResolvingService.stop();
			phoneNumberResolvingService = null;
		}
		if (contactsObserver != null)
		{
			getContentResolver().unregisterContentObserver(contactsObserver);
			contactsHandler.getLooper().quit();
			contactsHandler.removeCallbacksAndMessages(null);
			contactsObserver = null;
		}
		serviceIsStarted = false;
		logd("stopService <<<");

	}

	int compareStructuredNames(StructuredName structuredname, StructuredName structuredname1, String s)
	{
		int i = 0;
		int j = 1;
		logd("compareStructuredNames >>>");
		logd((new StringBuilder()).append("compareStructuredNames: name1=").append(structuredname).toString());
		logd((new StringBuilder()).append("compareStructuredNames: name2=").append(structuredname1).toString());
		if (structuredname == null)
		{
			if (structuredname1 != null)
				i = -1;
			logd((new StringBuilder()).append("compareStructuredNames <<< ").append(i).toString());
			j = i;
		} else
		if (structuredname1 == null)
		{
			logd("compareStructuredNames <<< 1");
		} else
		{
			logd((new StringBuilder()).append("compareStructuredNames: contactDisplayName=\"").append(s).append("\"").toString());
			boolean flag = TextUtils.equals(structuredname.displayName, s);
			boolean flag1 = TextUtils.equals(structuredname1.displayName, s);
			logd((new StringBuilder()).append("compareStructuredNames: matchesContactDisplayName1=").append(flag).append(" matchesContactDisplayName2=").append(flag1).toString());
			if (flag && !flag1)
				logd("compareStructuredNames <<< 1");
			else
			if (!flag && flag1)
			{
				logd("compareStructuredNames <<< -1");
				j = -1;
			} else
			{
				boolean flag2;
				boolean flag3;
				if (structuredname.firstName != null && structuredname.lastName != null)
					flag2 = true;
				else
					flag2 = false;
				if (structuredname1.firstName != null && structuredname1.lastName != null)
					flag3 = true;
				else
					flag3 = false;
				logd((new StringBuilder()).append("compareStructuredNames: hasBothComponents1=").append(flag2).append(" hasBothComponents2=").append(flag3).toString());
				if (flag2 && !flag3)
					logd("compareStructuredNames <<< 1");
				else
				if (flag3 && !flag2)
				{
					logd("compareStructuredNames <<< -1");
					j = -1;
				} else
				{
					logd("compareStructuredNames <<< 0");
					j = 0;
				}
			}
		}
		return j;
	}

	/**
	 * @deprecated Method onBind is deprecated
	 */

	public synchronized IBinder onBind(Intent intent)
	{

		String s;
		Object obj;
		s = intent.getAction();
		logd((new StringBuilder()).append("onBind >>> ").append(s).toString());
		obj = null;
		if (!IContactsService.class.getName().equals(s)) 
		{
			if (!IPhoneNumberResolvingService.class.getName().equals(s)) 
			{
				if (!(IGetPid.class.getName().equals(s)) )
				{
					obj = getPidBinder;				
				}
			}
			else 
			{
				obj = phoneNumberResolvingServiceBinder;
				phoneNumberResolvingServiceIsBound = true;

			}
		}
		else 
		{
			contactsServiceIsBound = true;
			obj = contactsServiceBinder;
		}
		if (!serviceIsStarted)
			startService();
		logd((new StringBuilder()).append("onBind <<< ").append(s).append(", return ").append(obj).toString());
		return ((IBinder) (obj));
	}

	/**
	 * @deprecated Method onUnbind is deprecated
	 */

	public synchronized boolean onUnbind(Intent intent)
	{

		String s;
		s = intent.getAction();
		logd((new StringBuilder()).append("onUnbind >>> ").append(s).toString());
		if (!IContactsService.class.getName().equals(s))
		{
			if (IPhoneNumberResolvingService.class.getName().equals(s))
			{
				phoneNumberResolvingServiceIsBound = false;
			}
	
		}
		else 
		{
			contactsServiceIsBound = false;
		}
		
		if (!contactsServiceIsBound && !phoneNumberResolvingServiceIsBound)
			stopService();
		logd((new StringBuilder()).append("onUnbind <<< ").append(s).toString());
		return false;
	}

	static 
	{
		String as[] = new String[3];
		as[0] = "vnd.android.cursor.item/contact_event";
		as[1] = Integer.toString(3);
		as[2] = Integer.toString(1);
		SELECTION_ARGS_BIRTHDAYS = as;
		String as1[] = new String[1];
		as1[0] = "vnd.android.cursor.item/photo";
		SELECTION_ARGS_PHOTO_MIMETYPE = as1;
		String as2[] = new String[3];
		as2[0] = "_id";
		as2[1] = "data_version";
		as2[2] = "contact_id";
		PROJECTION_DATA_VERSIONS = as2;
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("mimetype");
		stringbuilder.append("=? OR ");
		stringbuilder.append("mimetype");
		stringbuilder.append("=? OR ");
		stringbuilder.append("mimetype");
		stringbuilder.append("=? OR ");
		stringbuilder.append("mimetype");
		stringbuilder.append("=? OR ");
		stringbuilder.append("mimetype");
		stringbuilder.append("=? OR ");
		stringbuilder.append("mimetype");
		stringbuilder.append("=?");
		ALL_CONTACTS_SELECTION = stringbuilder.toString();
		String as3[] = new String[6];
		as3[0] = "vnd.android.cursor.item/name";
		as3[1] = "vnd.android.cursor.item/phone_v2";
		as3[2] = "vnd.android.cursor.item/email_v2";
		as3[3] = "vnd.android.cursor.item/im";
		as3[4] = "vnd.android.cursor.item/contact_event";
		as3[5] = "vnd.android.cursor.item/nickname";
		ALL_CONTACTS_SELECTION_ARGS = as3;
		StringBuilder stringbuilder1 = new StringBuilder();
		stringbuilder1.append('(').append(stringbuilder).append(") AND ");
		stringbuilder1.append("starred");
		stringbuilder1.append("=1");
		FAVORITES_SELECTION = stringbuilder1.toString();
		FAVORITE_SELECTION_ARGS = ALL_CONTACTS_SELECTION_ARGS;
		StringBuilder stringbuilder2 = new StringBuilder();
		stringbuilder2.append('(').append(stringbuilder).append(") AND ");
		stringbuilder2.append("contact_id");
		stringbuilder2.append("=?");
		CONTACT_ID_SELECTION = stringbuilder2.toString();
	}










}
