// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.softspb.shell.Home;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.DateChangedObserver;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.contacts.IContactsService;
import com.spb.contacts.IContactsServiceCallback;
import com.spb.contacts.IPhoneNumberResolvingService;
import com.spb.contacts.IPhoneNumberResolvingServiceCallback;
import com.spb.contacts.R;

// Referenced classes of package com.softspb.shell.adapters:
//			ContactsAdapter, AdaptersHolder

public class ContactsAdapterAndroid extends ContactsAdapter
	implements com.softspb.util.DateChangedObserver.DateChangedListener
{

	private static final String CONTACT_PHONE_LOOKUP_PROJECTION[];
	private static final String CONTACT_PHOTO_ID_PROJECTION[];
	private static final int INDEX_CONTACT_LOOKUP_KEY = 0;
	private static final int INDEX_CONTACT_PHOTO_ID = 0;
	private static final int INDEX_PHOTO_DATA = 0;
	private static final String ORDER_SMS_DATE_DESC_LIMIT_1 = "date DESC LIMIT 1";
	private static final String PHOTO_DATA_PROJECTION[];
	private static final String PROJECTION_PHONE_NUMBER[];
	private static final String PROJECTION_SMS_ID[];
	private static final String PROJECTION_SMS_ID_DATE[];
	private static final String SELECTION_PHONES_BY_CONTACT_ID = (new StringBuilder()).append("contact_id").append("=? AND ").append("mimetype").append("='").append("vnd.android.cursor.item/phone_v2").append('\'').toString();
	private static Logger logger = Loggers.getLogger(ContactsAdapterAndroid.class.getName());
	final SparseIntArray contactIds = new SparseIntArray();
	int contactPickingToken;
	int contactsAdapterToken;
	IContactsService contactsService;
	private IContactsServiceCallback contactsServiceCallback;
	final ServiceConnection contactsServiceConnection = new ServiceConnection() {

		final ContactsAdapterAndroid this$0;

		public void onServiceConnected(ComponentName componentname, IBinder ibinder)
		{
			logd((new StringBuilder()).append("onServiceConnected: ContactsService name=").append(componentname).toString());
			logd((new StringBuilder()).append("onServiceConnected: ContactsService Thread ").append(Thread.currentThread().getId()).append(" ").append(Thread.currentThread().getName()).toString());
			contactsService = com.spb.contacts.IContactsService.Stub.asInterface(ibinder);
			try
			{
				contactsService.registerCallback(contactsServiceCallback);
			}
			catch (RemoteException remoteexception) { }
			serviceConnectionCountDown.countDown();
		}

		public void onServiceDisconnected(ComponentName componentname)
		{
			logd("onServiceDisconnected: ContactsService");
			contactsService = null;
		}

			
			{
				this$0 = ContactsAdapterAndroid.this;
//				super();
			}
	};
	ContentResolver contentResolver;
	Context context;
	private Handler myUiHandler;
	Handler nativeThreadHandler;
	IPhoneNumberResolvingServiceCallback phoneNumberCallback;
	IPhoneNumberResolvingService phoneNumberService;
	final ServiceConnection phoneNumberServiceConnection = new ServiceConnection() {

		final ContactsAdapterAndroid this$0;

		public void onServiceConnected(ComponentName componentname, IBinder ibinder)
		{
			logd((new StringBuilder()).append("onServiceConnected: PhoneNumberService name=").append(componentname).toString());
			logd((new StringBuilder()).append("onServiceConnected: PhoneNumberService Thread ").append(Thread.currentThread().getId()).append(" ").append(Thread.currentThread().getName()).toString());
			phoneNumberService = com.spb.contacts.IPhoneNumberResolvingService.Stub.asInterface(ibinder);
			try
			{
				phoneNumberService.registerCallback(phoneNumberCallback);
			}
			catch (RemoteException remoteexception) { }
			serviceConnectionCountDown.countDown();
		}

		public void onServiceDisconnected(ComponentName componentname)
		{
			logd("onServiceDisconnected: PhoneNumberService");
			phoneNumberService = null;
		}

			
			{
				this$0 = ContactsAdapterAndroid.this;
//				super();
			}
	};
	CountDownLatch serviceConnectionCountDown;

	public ContactsAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		contactsServiceCallback = new com.spb.contacts.IContactsServiceCallback.Stub() {

			final ContactsAdapterAndroid this$0;

			public void onBirthdayDeleted(int i)
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onBirthdayDeleted: dataId=").append(i).toString());
				checkAdapterInitialized();
				ContactsAdapterAndroid.onBirthdayDeleted(contactsAdapterToken, i);
			}

			public void onBirthdayUpdated(int i, int j, int k, int l, int i1, int j1)
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onBirthdayUpdated: type=").append(i).append(" contactId=").append(j).append(" dataId=").append(k).append(" year=").append(l).append(" month=").append(i1).append(" dayOfMonth=").append(j1).toString());
				int k1 = ContactsAdapterAndroid.eventType2NativeEventType(i);
				checkAdapterInitialized();
				ContactsAdapterAndroid.onBirthdayUpdated(contactsAdapterToken, k1, j, k, l, i1, j1);
			}

			public void onConnectionDeleted(int i, int j, int k)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onConnectionDeleted: contactId=").append(i).append(" connectionId=").append(j).append(" kind=").append(k).toString());
				checkAdapterInitialized();
				int l;
				if (k == 2)
					l = 1;
				else
					l = 0;
				ContactsAdapterAndroid.onConnectionDeleted(contactsAdapterToken, i, j, l);
			}

			public void onConnectionUpdated(int i, int j, String s, int k, String s1, String s2, int l)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onConnectionUpdated: contactId=").append(i).append(" dataId=").append(j).append(" mimetype=").append(s).append(" locationType=").append(k).append(" numberAddress=").append(s1).append(" label=").append(s2).append(" dataVersion=").append(l).toString());
				checkAdapterInitialized();
				int i1 = ContactsAdapterAndroid.locationType2NativeLocationType(k);
				int j1 = ContactsAdapterAndroid.dataMimetype2NativeAddressType(s);
				ContactsAdapterAndroid.onConnectionUpdated(contactsAdapterToken, i, j, j1, i1, s1, s2, l);
			}

			public void onContactDeleted(int i, int j)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onContactDeleted: contactId=").append(i).append(" kind=").append(j).toString());
				contactIds.delete(i);
				checkAdapterInitialized();
				int k;
				if (j == 2)
					k = 1;
				else
					k = 0;
				ContactsAdapterAndroid.onContactDeleted(contactsAdapterToken, i, k);
			}

			public void onContactPhotoUpdated(int i, int j, int k)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onContactPhotoUpdated: contactId=").append(i).append(" datatId=").append(j).append(" dataVersion=").append(k).toString());
				checkAdapterInitialized();
				ContactsAdapterAndroid.onContactPhotoUpdated(contactsAdapterToken, i, j, k);
			}

			public void onContactUpdated(int i, String s, String s1, boolean flag, int j, int k)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onContactUpdated: contactId=").append(i).append(" lookupKey=").append(s).append(" displayName=").append(s1).append(" isStarred=").append(flag).append(" photoId=").append(j).append(" photoVersion=").append(k).toString());
				contactIds.put(i, 1);
				checkAdapterInitialized();
				ContactsAdapterAndroid.onContactUpdated(contactsAdapterToken, i, s, s1, flag, j, k);
			}

			public void onEventDeleted(int i, int j, int k)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onEventDeleted: contactId=").append(i).append(" eventId=").append(j).append(" kind=").append(k).toString());
				checkAdapterInitialized();
				int l;
				if (k == 2)
					l = 1;
				else
					l = 0;
				ContactsAdapterAndroid.onEventDeleted(contactsAdapterToken, i, j, l);
			}

			public void onEventUpdated(int i, int j, int k, long l, int i1)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onEventUpdated: contactId=").append(i).append(" dataId=").append(j).append(" eventType=").append(k).append(" eventDate=").append(l).append(" dataVersion=").append(i1).toString());
				checkAdapterInitialized();
				int j1 = ContactsAdapterAndroid.eventType2NativeEventType(k);
				ContactsAdapterAndroid.onEventUpdated(contactsAdapterToken, i, j, j1, l, i1);
			}

			public void onFinishedReload(int i)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onFinishedReload: kind=").append(i).toString());
				checkAdapterInitialized();
				int j;
				if (i == 2)
					j = 1;
				else
					j = 0;
				ContactsAdapterAndroid.onFinishedReload(contactsAdapterToken, j);
			}

			public void onFinishedReloadingBirthdays()
				throws RemoteException
			{
				logd("IContactsServiceCallback.onFinishedReloadingBirthdays");
				checkAdapterInitialized();
				ContactsAdapterAndroid.onFinishedReloadingBirthdays(contactsAdapterToken);
			}

			public void onFinishedUpdatingContact(int i, int j)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onFinishedUpdatingContact: contactId=").append(i).append(" kind=").append(j).toString());
				checkAdapterInitialized();
				int k;
				if (j == 2)
					k = 1;
				else
					k = 0;
				ContactsAdapterAndroid.onFinishedUpdatingContact(contactsAdapterToken, i, k);
			}

			public void onStartedReload(int i)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onStartedReload: kind=").append(i).toString());
				checkAdapterInitialized();
				int j;
				if (i == 2)
					j = 1;
				else
					j = 0;
				ContactsAdapterAndroid.onStartedReload(contactsAdapterToken, j);
			}

			public void onStartedReloadingBirthdays()
				throws RemoteException
			{
				logd("IContactsServiceCallback.onStartedReloadingBirthdays");
				checkAdapterInitialized();
				ContactsAdapterAndroid.onStartedReloadingBirthdays(contactsAdapterToken);
			}

			public void onStartedUpdatingContact(int i, int j)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onStartedUpdatingContact: contactId=").append(i).append(" kind=").append(j).toString());
				checkAdapterInitialized();
				int k;
				if (j == 2)
					k = 1;
				else
					k = 0;
				ContactsAdapterAndroid.onStartedUpdatingContact(contactsAdapterToken, i, k);
			}

			public void onStructuredNameUpdated(int i, String s, String s1)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IContactsServiceCallback.onStructuredNameUpdated: contactId=").append(i).append(" firstName=").append(s).append(" lastName=").append(s1).toString());
				checkAdapterInitialized();
				ContactsAdapterAndroid.onStructuredNameChanged(contactsAdapterToken, i, s, s1);
			}

			
			{
				this$0 = ContactsAdapterAndroid.this;
//				super();
			}
		};
		phoneNumberCallback = new com.spb.contacts.IPhoneNumberResolvingServiceCallback.Stub() {

			final ContactsAdapterAndroid this$0;

			public void onResolvedPhonesChanged(int i)
				throws RemoteException
			{
				logd((new StringBuilder()).append("IPhoneNumberResolvingService.onResolvedPhonesChanged: contactId=").append(i).toString());
				ContactsAdapterAndroid.notifyContactChanged(contactsAdapterToken, i);
			}

			
			{
				this$0 = ContactsAdapterAndroid.this;
//				super();
			}
		};
	}

	private String buildSelectionSMSByPhones_numbers(ArrayList arraylist)
	{
		int i;
		String s;
		if (arraylist == null)
			i = 0;
		else
			i = arraylist.size();
		if (i == 0)
		{
			s = null;
		} else
		{
			StringBuilder stringbuilder = new StringBuilder();
			for (int j = 0; j < i; j++)
			{
				if (j > 0)
					stringbuilder.append(" OR ");
				stringbuilder.append("PHONE_NUMBERS_EQUAL(address,'").append((String)arraylist.get(j)).append("')");
			}

			s = stringbuilder.toString();
		}
		return s;
	}

	private boolean changeContactIsFavorite(final int contactId, final boolean isFavorite, boolean flag)
	{
		boolean flag1 = true;
		logd((new StringBuilder()).append("changeContactIsFavorite: contactId=").append(contactId).append(" isFavorite=").append(isFavorite).append(" isNativeThread=").append(flag).toString());
		ContentValues contentvalues = new ContentValues();
		int i;
		Uri uri;
		if (isFavorite)
			i = ((flag1) ? 1 : 0);
		else
			i = 0;
		contentvalues.put("starred", Integer.valueOf(i));
		uri = ContentUris.withAppendedId(android.provider.ContactsContract.Contacts.CONTENT_URI, contactId);
		if (contentResolver.update(uri, contentvalues, null, null) == 1)
		{
			StringBuilder stringbuilder = (new StringBuilder()).append("Contact id=").append(contactId);
			String s;
			Runnable runnable;
			if (isFavorite)
				s = " was added to favorites.";
			else
				s = " was removed from favorites.";
			logd(stringbuilder.append(s).toString());
			runnable = new Runnable() {

				final ContactsAdapterAndroid this$0;
				final int val$contactId;
				final boolean val$isFavorite;

				public void run()
				{
					ContactsAdapterAndroid.onContactIsFavoriteChanged(contactsAdapterToken, contactId, isFavorite);
				}

			
			{
//				super();
				this$0 = ContactsAdapterAndroid.this;
				val$contactId = contactId;
				val$isFavorite = isFavorite;
			
			}
			};
			if (flag)
				runnable.run();
			else
				nativeThreadHandler.post(runnable);
		} else
		{
			logger.w((new StringBuilder()).append("Contact id=").append(contactId).append(" was NOT added to favorites").toString());
			flag1 = false;
		}
		return flag1;
	}

	private void checkAdapterInitialized()
	{
		if (contactsAdapterToken == 0)
		{
			IllegalStateException illegalstateexception = new IllegalStateException("Contacts adapter is not initialized");
			illegalstateexception.fillInStackTrace();
			logger.e("Contacts adapter is not initialized", illegalstateexception);
			throw illegalstateexception;
		} else
		{
			return;
		}
	}

	private boolean checkCanLoadContact(Uri uri)
	{
		boolean flag;
		logd((new StringBuilder()).append("checkCanLoadContact >>> ").append(uri).toString());
		flag = false;
		if (uri == null)
		{
			logd((new StringBuilder()).append("checkCanLoadContact <<< ").append(flag).toString());
			return flag;
		}
		else 
		{
			Cursor cursor = null;
			cursor = contentResolver.query(uri, null, null, null, null);
			if (cursor != null)
			{
				boolean flag1 = cursor.moveToFirst();
				flag = flag1;
			}
			return flag;
		}
	}

	static int dataMimetype2NativeAddressType(String s)
	{
		int i;
		if ("vnd.android.cursor.item/phone_v2".equals(s))
			i = 0;
		else
		if ("vnd.android.cursor.item/email_v2".equals(s))
			i = 1;
		else
		if ("vnd.android.cursor.item/im".equals(s))
			i = 2;
		else
			i = 3;
		return i;
	}

	static int eventType2NativeEventType(int i)
	{
		byte byte0;
		switch (i) {
		case 1:
			byte0 = 1;
			break;
		case 2:
			byte0 = 2;
			break;
		case 3:
			byte0 = 0;	
			break;
		default:
			byte0 = 2;
		}
		return byte0;
	}

	static int locationType2NativeLocationType(int i)
	{
		byte byte0;
		switch (i) {
		case 1:
			byte0 = 1;
			break;
		case 2:
			byte0 = 0;
			break;
		case 3:
			byte0 = 2;	
			break;
		case 17:
			byte0 = 2;	
			break;
		default:
			byte0 = 3;
		}
		return byte0;
	}

	private void logd(String s)
	{
		Thread thread = Thread.currentThread();
		String s1 = (new StringBuilder()).append("[Thread id=").append(thread.getId()).append(" name=").append(thread.getName()).append("; this=").append(this).append("] ").append(s).toString();
		logger.d(s1);
	}

	private void loge(String s, Throwable throwable)
	{
		Thread thread = Thread.currentThread();
		String s1 = (new StringBuilder()).append("[Thread id=").append(thread.getId()).append(" name=").append(thread.getName()).append("; this=").append(this).append("] ").append(s).toString();
		logger.e(s1, throwable);
	}

	 private long lookupContactId(String paramString)
	  {
	    long l1 = 0L;
	    Cursor localCursor =null;
	    try
	    {
	      Uri localUri1 = ContactsContract.PhoneLookup.CONTENT_FILTER_URI;
	      String str1 = Uri.encode(paramString);
	      Uri localUri2 = Uri.withAppendedPath(localUri1, str1);
	      ContentResolver localContentResolver = this.contentResolver;
	      String[] arrayOfString = CONTACT_PHONE_LOOKUP_PROJECTION;
	      localCursor = localContentResolver.query(localUri2, arrayOfString, null, null, null);
	      if ((localCursor != null) && (localCursor.moveToFirst()))
	      {
	        String str2 = localCursor.getString(0);
	        localCursor.close();
	        Uri localUri3 = ContactsContract.Contacts.getLookupUri(0L, str2);
	        Uri localUri4 = ContactsContract.Contacts.lookupContact(this.contentResolver, localUri3);
	        if (localUri4 != null)
	        {
	          long l2 = ContentUris.parseId(localUri4);
	          l1 = l2;
	        }
	      }
	    }
	    finally
	    {
	      if ((localCursor != null) && (!localCursor.isClosed()))
	        localCursor.close();
	      return l1;
	    }
	  }

	private static native void notifyContactChanged(int i, int j);

	private static native void notifyContactPicked(int i, boolean flag, int j);

	private static native void notifyNearestBirthdaysChanged(int i);

	private static native void onBirthdayDeleted(int i, int j);

	private static native void onBirthdayUpdated(int i, int j, int k, int l, int i1, int j1, int k1);

	private static native void onConnectionDeleted(int i, int j, int k, int l);

	private static native void onConnectionUpdated(int i, int j, int k, int l, int i1, String s, String s1, int j1);

	private static native void onContactDeleted(int i, int j, int k);

	private static native void onContactIsFavoriteChanged(int i, int j, boolean flag);

	private static native void onContactPhotoUpdated(int i, int j, int k, int l);

	private static native void onContactUpdated(int i, int j, String s, String s1, boolean flag, int k, int l);

	private static native void onEventDeleted(int i, int j, int k, int l);

	private static native void onEventUpdated(int i, int j, int k, int l, long l1, int i1);

	private static native void onFinishedReload(int i, int j);

	private static native void onFinishedReloadingBirthdays(int i);

	private static native void onFinishedUpdatingContact(int i, int j, int k);

	private static native void onStartedReload(int i, int j);

	private static native void onStartedReloadingBirthdays(int i);

	private static native void onStartedUpdatingContact(int i, int j, int k);

	private static native void onStructuredNameChanged(int i, int j, String s, String s1);

	private byte[] queryContactPhoto(Uri paramUri)
	  {
		byte[] arrayOfByte2 = null;
	    long l = queryContactPhotoId(paramUri);
	    Cursor localCursor =null;
	    Uri localUri = null;
	    if (l != 0L)
	      localUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, l);
	    try
	    {
	      ContentResolver localContentResolver = this.contentResolver;
	      String[] arrayOfString = PHOTO_DATA_PROJECTION;
	      localCursor = localContentResolver.query(localUri, arrayOfString, null, null, null);
	      if ((localCursor != null) && (localCursor.moveToFirst()))
	      {
	        byte[] arrayOfByte1 = localCursor.getBlob(0);
	        arrayOfByte2 = arrayOfByte1;
	        return arrayOfByte2;
	      }
	      
	    }
	    finally
	    {
	      
	      if (localCursor != null)
	        localCursor.close();
	      return arrayOfByte2;
	    }
	  }
	
	 private long queryContactPhotoId(Uri paramUri)
	  { 
		 long l2 = 0L;
		 Cursor localCursor =null;
	    try
	    {
	      ContentResolver localContentResolver = this.contentResolver;
	      String[] arrayOfString = CONTACT_PHOTO_ID_PROJECTION;
	      Uri localUri = paramUri;
	      localCursor = localContentResolver.query(localUri, arrayOfString, null, null, null);
	      if ((localCursor != null) && (localCursor.moveToFirst()))
	      {
	        long l1 = localCursor.getLong(0);
	        l2 = l1;
	      }
	    }
	    finally
	    {
	      if (localCursor != null)
	        localCursor.close();
	      	return l2;
	    }
	  }	
	 private ArrayList<String> queryPhoneNumbers(int paramInt)
			    throws Exception
			  {
			    long l1 = SystemClock.uptimeMillis();
			    StringBuilder localStringBuilder1 = new StringBuilder().append("queryPhoneNumbers: contactId=");
			    int i = paramInt;
			    String str1 = i + " >>>";
			    logd(str1);
			    ArrayList localArrayList = new ArrayList();
			    int j = 0;
			    Cursor localCursor = null;
			    while (true)
			    {
			     
			      int m;
			      try
			      {
			        ContentResolver localContentResolver = this.contentResolver;
			        Uri localUri = ContactsContract.Data.CONTENT_URI;
			        String[] arrayOfString1 = PROJECTION_PHONE_NUMBER;
			        String str2 = SELECTION_PHONES_BY_CONTACT_ID;
			        String[] arrayOfString2 = new String[1];
			        String str3 = Integer.toString(paramInt);
			        arrayOfString2[0] = str3;
			        localCursor = localContentResolver.query(localUri, arrayOfString1, str2, arrayOfString2, null);
			        if ((localCursor != null) && (localCursor.moveToFirst()))
			          if (!localCursor.isAfterLast())
			          {
			            String str4 = localCursor.getString(0);
			            int k = 1;
			            m = 0;
			            if (m >= j)
			              continue;
			            String str5 = (String)localArrayList.get(m);
			            if (PhoneNumberUtils.compare(str4, str5))
			            {
			              k = 0;
			              if (k == 0)
			                continue;
			              boolean bool1 = localArrayList.add(str4);
			              j += 1;
			              boolean bool2 = localCursor.moveToNext();
			              continue;
			            }
			          }
			      }
			      catch (Exception localException1)
			      {
			        StringBuilder localStringBuilder2 = new StringBuilder().append("queryPhoneNumbers: error: ").append(localException1).append(" <<< ");
			        long l2 = SystemClock.uptimeMillis() - l1;
			        String str6 = l2 + "ms";
			        logd(str6);
			        throw localException1;
			      }
			      finally
			      {
			        if (localCursor != null)
			        	 localCursor.close();
			  		  logd((new StringBuilder()).append("queryPhoneNumbers: got ").append(localArrayList.size()).append(" numbers <<< ").append(SystemClock.uptimeMillis() - l1).append("ms").toString());
			          return localArrayList;
			      }
			    }
			  }
	public boolean addToFavorites(int i)
	{
		logd((new StringBuilder()).append("addToFavorites: contactId=").append(i).toString());
		return changeContactIsFavorite(i, true, true);
	}

	public void call(String s, boolean flag)
	{
		Uri uri = Uri.fromParts("tel", s, null);
		Intent intent = new Intent();
		intent.setData(uri);
		if (flag)
			intent.setAction("android.intent.action.CALL");
		else
			intent.setAction("android.intent.action.DIAL");
		context.startActivity(intent);
	}

	public void disposeContactsAdapter()
	{
		logd("disposeContactsAdapter");
		if (contactsAdapterToken != 0)
		{
			contactsAdapterToken = 0;
			DateChangedObserver.getInstance().unregisterListener(this);
		}
		if (contactsService != null)
		{
			Exception exception;
			try
			{
				contactsService.unregisterCallback(contactsServiceCallback);
			}
			catch (Exception exception1) { }
			context.unbindService(contactsServiceConnection);
			contactsService = null;
		}
		if (phoneNumberService != null)
		{
			try
			{
				phoneNumberService.unregisterCallback(phoneNumberCallback);
			}
			// Misplaced declaration of an exception variable
			catch (Exception exception) { }
			context.unbindService(phoneNumberServiceConnection);
			phoneNumberService = null;
		}
	}

	 public int getContactByPhone(String paramString)
	  {
	    String str1 = "getContactByPhone: phoneNumber=" + paramString;
	    logd(str1);
	    checkAdapterInitialized();
	    int i = 0;
	    int j;
	    if (this.phoneNumberService == null)
	    {
	      logd("getContactByPhone <<< NOT bound to service, do nothing");
	      j = 0;
	    }
	    else
	    {
	      try
	      {
	        long l = this.phoneNumberService.getResolvedContactId(paramString);
	        i = (int)l;
	        if (i == 0L)
	        {
	          logd("getContactByPhone: failed to obtain resolved contact ID from reslover service, trying to lookup...");
	          i = (int)lookupContactId(paramString);
	        }
	        String str2 = "getContactByPhone: resolved contactId=" + i;
	        logd(str2);
	        if (i != 0)
	          reloadContact(i);
	        j = i;
	      }
	      catch (Exception localException)
	      {
	        while (true)
	        {
	          Logger localLogger = logger;
	          String str3 = "Error invoking contacts service: " + localException;
	          localLogger.e(str3, localException);
	        }
	      }
	    }
	    return j;
	  }
	 
	

	public Bitmap getContactPic(int i, String s)
	{
		logd((new StringBuilder()).append("getContactPic >>> contactLookupKey=").append(s).toString());
		checkAdapterInitialized();
		Uri uri = android.provider.ContactsContract.Contacts.getLookupUri(i, s);
		Uri uri1 = android.provider.ContactsContract.Contacts.lookupContact(contentResolver, uri);
		Bitmap bitmap = null;
		if (uri1 != null)
		{
			byte abyte0[] = queryContactPhoto(uri1);
			if (abyte0 != null)
				bitmap = BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, null);
			else
				logger.w((new StringBuilder()).append("Failed to load contact photo: ").append(uri).toString());
		} else
		{
			logger.w((new StringBuilder()).append("Failed to lookup Contact with lookup URI: ").append(uri).toString());
		}
		logd("getContactPic <<<");
		return bitmap;
	}

	public int getLastMessage(int i)
	{
		throw new UnsupportedOperationException("Unexpected use of last message feature.");
	}
	 public void initContactsAdapter(int paramInt)
	  {
	    StringBuilder localStringBuilder1 = new StringBuilder().append("initContactsAdapter: contactsAdapterToken=0x");
	    String str1 = Integer.toHexString(paramInt);
	    String str2 = str1;
	    logd(str2);
	    StringBuilder localStringBuilder2 = new StringBuilder().append("initContactsAdapter: Thread ");
	    long l = Thread.currentThread().getId();
	    StringBuilder localStringBuilder3 = localStringBuilder2.append(l).append(" ");
	    String str3 = Thread.currentThread().getName();
	    String str4 = str3;
	    logd(str4);
	    this.contactsAdapterToken = paramInt;
	    DateChangedObserver.getInstance().registerListener(this);
	    Looper localLooper = Looper.myLooper();
	    if (localLooper == null)
	    {
	      Looper.prepare();
	      localLooper = Looper.myLooper();
	    }
	    Handler localHandler = new Handler(localLooper);
	    this.nativeThreadHandler = localHandler;
	    CountDownLatch localCountDownLatch = new CountDownLatch(2);
	    this.serviceConnectionCountDown = localCountDownLatch;
	    logd("initContactsAdapter: connecting to ContactsService...");
	    String str5 = IContactsService.class.getName();
	    Intent localIntent1 = new Intent(str5);
	    String str6 = this.context.getPackageName();
	    Intent localIntent2 = localIntent1.setPackage(str6);
	    Context localContext1 = this.context;
	    ServiceConnection localServiceConnection1 = this.contactsServiceConnection;
	    boolean bool1 = localContext1.bindService(localIntent1, localServiceConnection1, 1);
	    logd("initContactsAdapter: connecting to PhoneNumberService...");
	    String str7 = IPhoneNumberResolvingService.class.getName();
	    Intent localIntent3 = new Intent(str7);
	    Intent localIntent4 = localIntent3.setPackage(str6);
	    Context localContext2 = this.context;
	    ServiceConnection localServiceConnection2 = this.phoneNumberServiceConnection;
	    boolean bool2 = localContext2.bindService(localIntent3, localServiceConnection2, 1);
	    try
	    {
	      this.serviceConnectionCountDown.await();
	      logd("initContactsAdapter: connected to ContactsService.");
	      return;
	    }
	    catch (InterruptedException localInterruptedException)
	    {
	      while (true)
	      {
	        Logger localLogger = logger;
	        String str8 = "initContactsAdapter: connection to server interrupted: " + localInterruptedException;
	        localLogger.e(str8, localInterruptedException);
	      }
	    }
	  }

	
	public void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		context = context1;
		contentResolver = context1.getContentResolver();
	}

	public void onDateChanged()
	{
		logd("onDateChanged");
		notifyNearestBirthdaysChanged(contactsAdapterToken);
	}

	 public void onPickContactResult(boolean paramBoolean, int paramInt, Intent paramIntent)
	  {
	    Uri localUri = null;
	    if (paramInt == -1)
	    {
	      localUri = paramIntent.getData();
	      if (!checkCanLoadContact(localUri))
	      {
	        Context localContext = this.context;
	        int i = R.string.contacts_failed_pick;
	        Toast.makeText(localContext, i, 1).show();
	        if (!paramBoolean)
	          notifyContactPicked(this.contactPickingToken, false, 0);
	      }
	    }
	    else
	    {	    
	      if (!paramBoolean)
	      {
	        if (this.contactPickingToken == 0)
	        return;
	        int j = (int)ContentUris.parseId(localUri);
	        notifyContactPicked(this.contactPickingToken, true, j);
	        return;
	      }
	      int k = (int)ContentUris.parseId(localUri);
	      String str = "onPickContactResult: contactId=" + k;
	      logd(str);
	      if (this.contactIds.get(k, 0) == 0)
	        reloadContact(k);
	      boolean bool = changeContactIsFavorite(k, true, false);
//	      continue;
	      if ((paramBoolean) || (this.contactPickingToken == 0))
	      {
	    	  return;
	      }
	      notifyContactPicked(this.contactPickingToken,false, 0);
	    }
	    return;
	  }	
	protected void onStartInUIThread()
	{
		super.onStartInUIThread();
		myUiHandler = new Handler();
	}

	public void openContactCard(int i, String s)
	{
		Uri uri = android.provider.ContactsContract.Contacts.getLookupUri(i, s);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setData(uri);
		context.startActivity(intent);
	}

	public void reloadBirthdays(int i)
	{
		logd("reloadBirthdays >>>");
		long l = SystemClock.uptimeMillis();
		if (contactsService == null)
		{
			logd((new StringBuilder()).append("reloadBirthdays <<< NOT bound to service, do nothing: ").append(l).append(" ms").toString());
		} else
		{
			byte byte0;
			if (i == 1)
				byte0 = 2;
			else
				byte0 = 1;
			try
			{
				contactsService.reloadBirthdays(byte0, true);
			}
			catch (Exception exception)
			{
				logger.w((new StringBuilder()).append("Error invoking ContactsService: ").append(exception).toString(), exception);
			}
			logd((new StringBuilder()).append("reloadBirthdays <<< ").append(l).append(" ms").toString());
		}
	}

	public void reloadContact(int i)
	{
		logd((new StringBuilder()).append("reloadContact >>> contactId=").append(i).toString());
		long l = System.currentTimeMillis();
		if (contactsService == null)
		{
			logd((new StringBuilder()).append("reloadContact <<< NOT bound to service, do nothing: ").append(l).append(" ms").toString());
		} else
		{
			try
			{
				contactsService.reloadContact(i);
			}
			catch (Exception exception)
			{
				loge((new StringBuilder()).append("Error invoking ContactsService: ").append(exception).toString(), exception);
			}
			logd((new StringBuilder()).append("reloadContact <<< ").append(System.currentTimeMillis() - l).append(" ms").toString());
		}
	}

	public void reloadContacts(int i)
	{
		logd("reloadContacts >>>");
		long l = SystemClock.uptimeMillis();
		if (contactsService == null)
		{
			logd((new StringBuilder()).append("reloadContacts <<< NOT bound to service, do nothing: ").append(l).append(" ms").toString());
		} else
		{
			byte byte0;
			if (i == 1)
				byte0 = 2;
			else
				byte0 = 1;
			try
			{
				contactsService.reloadContacts(byte0);
			}
			catch (Exception exception)
			{
				loge((new StringBuilder()).append("Error invoking ContactsService: ").append(exception).toString(), exception);
			}
			logd((new StringBuilder()).append("reloadContacts <<< ").append(l).append(" ms").toString());
		}
	}

	public boolean removeFromFavorites(int i)
	{
		logd((new StringBuilder()).append("removeFromFavorites: contactId=").append(i).toString());
		return changeContactIsFavorite(i, false, true);
	}

	public void showAddToFavoritesDialog()
	{
		((Home)context).startPickContact(true);
	}

	public void showPickContactDialog(final int token)
	{
		myUiHandler.post(new Runnable() {

			final ContactsAdapterAndroid this$0;
			final int val$token;

			public void run()
			{
				contactPickingToken = token;
				((Home)context).startPickContact(false);
			}

			
			{
				this$0 = ContactsAdapterAndroid.this;
				val$token = token;
//				super();
			}
		});
	}

	static 
	{
		String as[] = new String[1];
		as[0] = "data1";
		PROJECTION_PHONE_NUMBER = as;
		String as1[] = new String[1];
		as1[0] = "_id";
		PROJECTION_SMS_ID = as1;
		String as2[] = new String[2];
		as2[0] = "_id";
		as2[1] = "date";
		PROJECTION_SMS_ID_DATE = as2;
		String as3[] = new String[1];
		as3[0] = "lookup";
		CONTACT_PHONE_LOOKUP_PROJECTION = as3;
		String as4[] = new String[1];
		as4[0] = "data15";
		PHOTO_DATA_PROJECTION = as4;
		String as5[] = new String[1];
		as5[0] = "photo_id";
		CONTACT_PHOTO_ID_PROJECTION = as5;
	}





















}
