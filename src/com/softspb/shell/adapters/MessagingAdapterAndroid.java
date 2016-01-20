package com.softspb.shell.adapters;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;
import android.util.SparseArray;
import com.softspb.shell.Home;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.contacts.IPhoneNumberResolvingService;
import com.spb.contacts.IPhoneNumberResolvingServiceCallback;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class MessagingAdapterAndroid extends MessagingAdapter
{
  private static final String[] ALL_MESSAGES_PROJECTION ;
  static Uri CONVERSATIONS_URI  ;
  private static final long[] EMPTY_LONG_ARRAY ;
  private static final String[] ID_PROJECTION  ;
  private static final int INDEX_ALL_MESSAGES_ADDRESS = 1;
  private static final int INDEX_ALL_MESSAGES_DATE = 2;
  private static final int INDEX_ALL_MESSAGES_ID = 0;
  static Uri MESSAGES_BY_PHONE_URI ;
  static Uri MMS_INBOX_URI ;
  static Uri MMS_SMS_URI ;
  static final int MSG_LAST_MESSAGE_BY_PHONE_NUMBER_CHANGED = 2;
  static final int MSG_MESSAGES_UPDATED = 1;
  static final int MSG_NOTIFY_UPDATED = 2;
  static final int MSG_ON_CONTACTS_CHANGED = 1;
  static final String[] NAME_LOOKUP_PROJECTION ;
  private static final String PDU_MESSAGE_TYPE = "m_type";
  private static String SELECTION_NOT_MMS_DELIVERY_OR_READ_REPORT ;
  static Uri SMS_ALL_URI  ;
  static Uri SMS_INBOX_URI ;
  private static final String UNREAD_SELECTION = "read=0";
  private static final int com_google_android_mms_pdu_PduHeaders_MESSAGE_TYPE_DELIVERY_IND = 134;
  private static final int com_google_android_mms_pdu_PduHeaders_MESSAGE_TYPE_READ_ORIG_IND = 136;
  private static Logger logger;
  AllMessagesHandler allMessagesHandler;
  final HashMap<String, Integer> allPhoneNumbers;
  ContactNameByPhoneCache contactNameByPhoneCache;
  ContactObserver contactObserver;
  private ContentResolver contentResolver;
  private Context context;
  final HashMap<String, Integer> deletedPhoneNumbers;
  private final HashMap<String, Integer> inboxFoldersByAccount;
  private boolean isLiteVersion;
  final SparseArray<SMSMessage> lastMessageByContactId;
  final HashMap<String, SMSMessage> lastMessageByPhoneNumber;
  private long lastMessageId;
  private List<Integer> listeners;
  MessagesObserver messageObserver;
  private int nativeAdapterToken;
  private NativeCallbacks nativeCallbacks;
  final IPhoneNumberResolvingServiceCallback phoneNumberCallback;
  private IPhoneNumberResolvingService phoneNumberResolvingService;
  final ServiceConnection phoneNumberServiceConnection;
  private CountDownLatch serviceConnectionCountDown;
  HandlerThread workerThread;

  static
  {
    MMS_SMS_URI = Uri.parse("content://mms-sms/");
    SMS_ALL_URI = Uri.parse("content://sms");
    MESSAGES_BY_PHONE_URI = Uri.parse("content://mms-sms/messages/byphone");
    SMS_INBOX_URI = Uri.parse("content://sms/inbox");
    MMS_INBOX_URI = Uri.parse("content://mms/inbox");
    CONVERSATIONS_URI = Uri.parse("content://mms-sms/conversations");
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "_id";
    ID_PROJECTION = arrayOfString1;
    EMPTY_LONG_ARRAY = null;
    String[] arrayOfString2 = new String[3];
    arrayOfString2[0] = "_id";
    arrayOfString2[1] = "address";
    arrayOfString2[2] = "date";
    ALL_MESSAGES_PROJECTION = arrayOfString2;
    logger = Loggers.getLogger(MessagingAdapterAndroid.class.getName());
    String[] arrayOfString3 = new String[1];
    arrayOfString3[0] = "display_name";
    NAME_LOOKUP_PROJECTION = arrayOfString3;
  }
  class MessagingAdapterAndroid$1 extends IPhoneNumberResolvingServiceCallback.Stub
  {
    public void onResolvedPhonesChanged(int paramInt)
      throws RemoteException
    {
  	updateLastMessageByContactId(paramInt);
    }
  }
  class MessagingAdapterAndroid$2  implements ServiceConnection
  {
	public void onServiceConnected(ComponentName componentname, IBinder ibinder)
	{
		MessagingAdapterAndroid.logger.d((new StringBuilder()).append("onServiceConnected: PhoneNumberService name=").append(componentname).toString());
		phoneNumberResolvingService = com.spb.contacts.IPhoneNumberResolvingService.Stub.asInterface(ibinder);
		try
		{
			phoneNumberResolvingService.registerCallback(phoneNumberCallback);
		}
		catch (RemoteException remoteexception) { }
		serviceConnectionCountDown.countDown();
	}
	public void onServiceDisconnected(ComponentName componentname)
	{
		MessagingAdapterAndroid.logger.d("onServiceDisconnected: PhoneNumberService");
		phoneNumberResolvingService = null;
	}
  }
  public MessagingAdapterAndroid(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
    HashMap localHashMap1 = new HashMap();
    this.inboxFoldersByAccount = localHashMap1;
    List localList = Collections.synchronizedList(new LinkedList());
    this.listeners = localList;
    this.lastMessageId = 65535L;
    MessagingAdapterAndroid$1 local1 = new MessagingAdapterAndroid$1();
    this.phoneNumberCallback = local1;
    MessagingAdapterAndroid$2 local2 = new MessagingAdapterAndroid$2();
    this.phoneNumberServiceConnection = local2;
    HashMap localHashMap2 = new HashMap();
    this.allPhoneNumbers = localHashMap2;
    HashMap localHashMap3 = new HashMap();
    this.deletedPhoneNumbers = localHashMap3;
    HashMap localHashMap4 = new HashMap();
    this.lastMessageByPhoneNumber = localHashMap4;
    SparseArray localSparseArray = new SparseArray();
    this.lastMessageByContactId = localSparseArray;
  }

  private void connectToPhoneNumberService()
  {
    logger.d("connectToPhoneNumberService >>>");
    CountDownLatch localCountDownLatch = new CountDownLatch(1);
    this.serviceConnectionCountDown = localCountDownLatch;
    Context localContext = this.context;
    String str1 = IPhoneNumberResolvingService.class.getName();
    Intent localIntent = new Intent(str1);
    ServiceConnection localServiceConnection = this.phoneNumberServiceConnection;
    boolean bool = localContext.bindService(localIntent, localServiceConnection, 1);
    Logger localLogger1 = logger;
    String str2 = "connectToPhoneNumberService: bindService returned " + bool;
    localLogger1.d(str2);
    try
    {
      this.serviceConnectionCountDown.await();
      logger.d("connected to PhoneNumberResolvingService.");
      logger.d("connectToPhoneNumberService <<<");
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        Logger localLogger2 = logger;
        String str3 = "connection to server interrupted: " + localInterruptedException;
        localLogger2.e(str3, localInterruptedException);
      }
    }
  }
	private void disconnectFromPhoneNumberService()
	{
		if (phoneNumberResolvingService != null)
		{
			try
			{
				phoneNumberResolvingService.unregisterCallback(phoneNumberCallback);
			}
			catch (RemoteException remoteexception) { }
			context.unbindService(phoneNumberServiceConnection);
			phoneNumberResolvingService = null;
		}
	}
	private void doUpdateLastMessageChanged(String s)
	{
		logger.d((new StringBuilder()).append("doUpdateLastMessageChanged: phoneNumber=").append(s).toString());
		if (phoneNumberResolvingService != null) 
			{
				try
				{
					long l = phoneNumberResolvingService.getResolvedContactId(s);
					if (l != 0L)
						updateLastMessageByContactId((int)l);
				}
				catch (Exception exception)
				{
					logger.e((new StringBuilder()).append("Error invoking PhoneNumber service: ").append(exception).toString(), exception);
				}			
			}
		else 
		{
			logger.d("doUpdateLastMessageChanged <<< NOT bound to service, do nothing");
		}
		return;
	}
  private static Uri getContentUri()
  {
    return SMS_INBOX_URI;
  }
	private int getUnreadCount(Uri uri, String s)
	{
		Cursor cursor;
		cursor = null;
		int i = 0;
		String s1;
		int j;
		if (s == null)
			s1 = "read=0";
		else
			s1 = (new StringBuilder()).append('(').append("read=0").append(") AND (").append(s).append(')').toString();
		cursor = contentResolver.query(uri, ID_PROJECTION, s1, null, null);
		
		if (cursor != null)
		{
			j = cursor.getCount();
			i = j;
			cursor.close();
		}
		return i;
	}
  private void markAsRead(long paramLong)
  {
    ContentValues localContentValues = new ContentValues();
    Integer localInteger = Integer.valueOf(1);
    localContentValues.put("read", localInteger);
    ContentResolver localContentResolver = this.contentResolver;
    Uri localUri = ContentUris.withAppendedId(MMS_SMS_URI, paramLong);
    int i = localContentResolver.update(localUri, localContentValues, null, null);
  }

  private void notifyListeners()
  {
    synchronized (this.listeners)
    {
      Iterator localIterator = this.listeners.iterator();
      if (localIterator.hasNext())
        notifyMessagesChanged(((Integer)localIterator.next()).intValue());
    }
  }

  private static native void notifyMessagesChanged(int paramInt);

  private void postLastMessageByPhoneNumberChanged(String paramString)
  {
    if (!this.isLiteVersion)
    {
      paramString = paramString.intern();
      if (!this.allMessagesHandler.hasMessages(2, paramString))
      {
        AllMessagesHandler localAllMessagesHandler = this.allMessagesHandler;
        Message localMessage = Message.obtain(this.allMessagesHandler, 2, paramString);
        boolean bool = localAllMessagesHandler.sendMessage(localMessage);
      }
    }
  }

  private static native void setLastMessageByContact(int paramInt1, int paramInt2, long paramLong);

  private void startServices()
  {
    logger.d("startServices >>>");
    Logger localLogger = logger;
    StringBuilder localStringBuilder1 = new StringBuilder().append("startServices: Thread ");
    long l = Thread.currentThread().getId();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(l).append(" ");
    String str1 = Thread.currentThread().getName();
    String str2 = str1;
    localLogger.d(str2);
    connectToPhoneNumberService();
    HandlerThread localHandlerThread = new HandlerThread("AllMessagesHandler");
    this.workerThread = localHandlerThread;
    this.workerThread.start();
    Looper localLooper1 = this.workerThread.getLooper();
    ContactNameByPhoneCache localContactNameByPhoneCache1 = new ContactNameByPhoneCache(localLooper1);
    this.contactNameByPhoneCache = localContactNameByPhoneCache1;
    ContactNameByPhoneCache localContactNameByPhoneCache2 = this.contactNameByPhoneCache;
    ContactObserver localContactObserver1 = new ContactObserver(contactNameByPhoneCache,this);
    this.contactObserver = localContactObserver1;
    ContentResolver localContentResolver1 = this.contentResolver;
    Uri localUri1 = ContactsContract.Contacts.CONTENT_URI;
    ContactObserver localContactObserver2 = this.contactObserver;
    localContentResolver1.registerContentObserver(localUri1, true, localContactObserver2);
    if (!this.isLiteVersion)
    {
      Looper localLooper2 = this.workerThread.getLooper();
      AllMessagesHandler localAllMessagesHandler = new AllMessagesHandler(localLooper2);
      this.allMessagesHandler = localAllMessagesHandler;
    }
    Looper localLooper3 = this.workerThread.getLooper();
    Handler localHandler = new Handler(localLooper3);
    MessagesObserver localMessagesObserver1 = new MessagesObserver(new Handler(workerThread.getLooper()), this);
    this.messageObserver = localMessagesObserver1;
    ContentResolver localContentResolver2 = this.contentResolver;
    Uri localUri2 = MMS_SMS_URI;
    MessagesObserver localMessagesObserver2 = this.messageObserver;
    localContentResolver2.registerContentObserver(localUri2, true, localMessagesObserver2);
    if (this.isLiteVersion)
    {
      ContentResolver localContentResolver3 = this.contentResolver;
      Uri localUri3 = ContactsContract.Data.CONTENT_URI;
      MessagesObserver localMessagesObserver3 = this.messageObserver;
      localContentResolver3.registerContentObserver(localUri3, true, localMessagesObserver3);
    }
    logger.d("startServices <<<");
  }

  private void stopServices()
  {
    logger.d("stopServices >>>");
    ContentResolver localContentResolver1 = this.contentResolver;
    MessagesObserver localMessagesObserver = this.messageObserver;
    localContentResolver1.unregisterContentObserver(localMessagesObserver);
    this.contactNameByPhoneCache.removeCallbacksAndMessages(null);
    ContentResolver localContentResolver2 = this.contentResolver;
    ContactObserver localContactObserver = this.contactObserver;
    localContentResolver2.unregisterContentObserver(localContactObserver);
    if (!this.isLiteVersion)
      this.allMessagesHandler.removeCallbacksAndMessages(null);
    this.workerThread.getLooper().quit();
    disconnectFromPhoneNumberService();
    logger.d("stopServices <<<");
  }

  private void updateLastMessageByContactId(int paramInt)
  {
    Logger localLogger1 = logger;
    String str1 = "updateLastMessageByContactId: contactId=" + paramInt;
    localLogger1.d(str1);
    Object localObject1 = null;
    if (this.phoneNumberResolvingService == null)
      logger.w("updateLastMessageByContactId: NOT bound to service, do nothing");
    else
    {
      try
      {
        List localList = this.phoneNumberResolvingService.getResolvedPhoneNumbers(paramInt);
        SMSMessage smsmessage;
		int j;
		if (localList == null)
			j = 0;
		else
			j = localList.size();
		smsmessage = null;
		for (int k = 0; k < j; k++)
		{
			String s = (String)localList.get(k);
			SMSMessage smsmessage2 = (SMSMessage)lastMessageByPhoneNumber.get(s);
			if (smsmessage2 != null && (smsmessage == null || smsmessage2.date > smsmessage.date))
				smsmessage = smsmessage2;
		}
      }
      catch (Exception localException)
      {
    	  
      }
    }
  }

  private static native void updateMessage(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, long paramLong1, long paramLong2, long paramLong3);

  public void addListener(int paramInt)
  {
    synchronized (this.listeners)
    {
      List localList2 = this.listeners;
      Integer localInteger = Integer.valueOf(paramInt);
      boolean bool = localList2.add(localInteger);
      return;
    }
  }
 public int getCount()
  {
    int i = 0;
    Cursor localCursor = null;
    try
    {
      ContentResolver localContentResolver = this.contentResolver;
      Uri localUri = SMS_INBOX_URI;
      String[] arrayOfString = ID_PROJECTION;
      localCursor = localContentResolver.query(localUri, arrayOfString, null, null, null);
      if (localCursor != null)
      {
        int j = localCursor.getCount();
        i = j;
      }
      return i;
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
  }

  public int getInboxFolder(String paramString)
  {
    Integer localInteger = (Integer)this.inboxFoldersByAccount.get(paramString);
    if (localInteger == null);
    for (int i = 0; ; i = localInteger.intValue())
      return i;
  }

  public int getLastMessage(int paramInt)
  {
    long l1 = SystemClock.uptimeMillis();
    Logger localLogger1 = logger;
    String str1 = "getLastMessage >>> contactId=" + paramInt;
    localLogger1.d(str1);
    Logger localLogger2 = logger;
    StringBuilder localStringBuilder = new StringBuilder().append("getLastMessage <<< completed in ");
    long l2 = SystemClock.uptimeMillis() - l1;
    String str2 = l2 + "ms";
    localLogger2.d(str2);
    SMSMessage localSMSMessage = (SMSMessage)this.lastMessageByContactId.get(paramInt);
    if (localSMSMessage == null);
    for (int i = -1; ; i = (int)localSMSMessage.id)
      return i;
  }

  public void getMessageById(int paramInt, long paramLong)
  {
	Cursor localCursor1 = null; 
	logger.d((new StringBuilder()).append("getMessageById: messageToken=0x").append(Integer.toHexString(paramInt)).append(" messageId=").append(paramLong).toString());
	HashSet localHashSet;
	if (paramInt != 0)  	
    {
      try
      {
        ContentResolver localContentResolver = this.contentResolver;
        Uri localUri1 = SMS_INBOX_URI;
        long l2 = paramLong;
        Uri localUri2 = ContentUris.withAppendedId(localUri1, l2);
        localCursor1 = localContentResolver.query(localUri2, null, null, null, "date DESC");
        if ((localCursor1 != null) && (localCursor1.moveToFirst()))
        {
          int i = localCursor1.getColumnIndex("address");
          int j = localCursor1.getColumnIndex("subject");
          int k = localCursor1.getColumnIndex("body");
          int m = localCursor1.getColumnIndex("date");
          int n = localCursor1.getColumnIndex("read");
          int i1 = localCursor1.getColumnIndex("_id");
          int i2 = localCursor1.getColumnIndex("thread_id");
          localHashSet = this.contactNameByPhoneCache.getAllPhoneNumbers();
          while (true)
          {
            if (localCursor1.isAfterLast())
              break ;
            String str3 = localCursor1.getString(k);
            Cursor localCursor2 = localCursor1;
            int i3 = i;
            String str4 = localCursor2.getString(i3);
            boolean bool1 = localHashSet.remove(str4);
            this.contactNameByPhoneCache.addPhoneNumber(str4);
            ContactNameByPhoneCache localContactNameByPhoneCache1 = this.contactNameByPhoneCache;
            int i4 = 1;
            String str5 = localContactNameByPhoneCache1.getNameByPhone(str4, true);
            Cursor localCursor3 = localCursor1;
            int i5 = j;
            String str6 = localCursor3.getString(i5);
            Cursor localCursor4 = localCursor1;
            int i6 = n;
            int i7 = localCursor4.getInt(i6);
            int i8 = 1;
            if (i7 != i8)
              break;
            int i9 = 1;
            long l3 = localCursor1.getLong(m);
            Cursor localCursor5 = localCursor1;
            int i11 = i1;
            long l4 = localCursor5.getLong(i11);
            Cursor localCursor6 = localCursor1;
            int i12 = i2;
            long l5 = localCursor6.getLong(i12);
            Logger localLogger2 = logger;
            StringBuilder localStringBuilder3 = new StringBuilder().append("getMessageById: messageId=");
            long l6 = paramLong;
            String str7 = l6 + " senderAddress=" + str4 + " sender=" + str5;
            localLogger2.d(str7);
            updateMessage(paramInt, str3, str4, str5, str6, true, l3, l4, l5);
            boolean bool2 = localCursor1.moveToNext();
          }
        }
      }
      catch (Exception localException1)
      {}
      finally
      {
        if (localCursor1 != null)
          localCursor1.close();
      }    
    }
  }


	public long[] getMessageList(int i)
	{
		long l;
		Cursor cursor;
		long al[];
		logger.d((new StringBuilder()).append("getMessagesList: >>> limit=").append(i).toString());
		l = SystemClock.uptimeMillis();
		cursor = null;
		al = EMPTY_LONG_ARRAY;
		cursor = contentResolver.query(SMS_INBOX_URI, ID_PROJECTION, null, null, (new StringBuilder()).append("date DESC LIMIT ").append(i).toString());
		if (cursor != null)
		{
			int j = cursor.getColumnIndex("_id");
			al = new long[cursor.getCount()];
			if (cursor.moveToFirst())
			{
				for (int k = 0; !cursor.isAfterLast(); k++)
				{
					al[k] = cursor.getLong(j);
					cursor.moveToNext();
				}

				lastMessageId = al[0];
			}
		}
		if (cursor != null)
			cursor.close();
		logger.d((new StringBuilder()).append("getMessagesList: <<< completed in ").append(SystemClock.uptimeMillis() - l).append("ms").toString());
		return al;
	}

  public int getUnreadCount()
  {
    logger.d("getUnreadCount: >>>");
    Uri localUri1 = SMS_INBOX_URI;
    int i = getUnreadCount(localUri1, null);
    Uri localUri2 = MMS_INBOX_URI;
    String str1 = SELECTION_NOT_MMS_DELIVERY_OR_READ_REPORT;
    int j = getUnreadCount(localUri2, str1);
    int k = i + j;
    Logger localLogger = logger;
    String str2 = "getUnreadCount: <<< count=" + k;
    localLogger.d(str2);
    return k;
  }

  public void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    this.context = paramContext;
    ContentResolver localContentResolver = paramContext.getContentResolver();
    this.contentResolver = localContentResolver;
    this.nativeCallbacks = paramNativeCallbacks;
    boolean bool = Home.isLiteVersion();
    this.isLiteVersion = bool;
  }

  protected void onDestroy(Context paramContext)
  {
  }

  public void onInboxFolderCreated(String paramString, int paramInt)
  {
    Logger localLogger = logger;
    String str = "onInboxFolderCreated: accountName=" + paramString + " folderToken=" + paramInt;
    localLogger.d(str);
    if (!"Messaging".equals(paramString))
      throw new IllegalArgumentException("Account not supported: Messaging");
    HashMap localHashMap = this.inboxFoldersByAccount;
    Integer localInteger = Integer.valueOf(paramInt);
    Object localObject = localHashMap.put(paramString, localInteger);
  }

  void onMessagesContentChanged()
  {
    logger.d("onMessagesContentChanged");
    notifyListeners();
  }
  protected void onStart(int i)
	{
		nativeAdapterToken = i;
		startServices();
		if (!isLiteVersion)
			allMessagesHandler.sendMessage(Message.obtain(allMessagesHandler, 1));
		else
			contactNameByPhoneCache.sendMessage(Message.obtain(contactNameByPhoneCache, 1));
	}
  protected void onStop()
  {
    this.nativeAdapterToken = 0;
    stopServices();
  }

  public void openMessageThread(long paramLong)
  {
    Logger localLogger = logger;
    String str = "openMessageThread: threadId" + paramLong;
    localLogger.d(str);
    Uri localUri = ContentUris.withAppendedId(CONVERSATIONS_URI, paramLong);
    Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
    this.context.startActivity(localIntent);
  }

  public void openSmsMmsAccount()
  {
    Intent localIntent1 = new Intent("android.intent.action.MAIN");
    Intent localIntent2 = localIntent1.setType("vnd.android-dir/mms-sms");
    this.context.startActivity(localIntent1);
  }

  public void openSmsMmsActivity()
  {
    boolean bool = this.nativeCallbacks.getProgramListAdapter().launch("sms");
  }

  void reloadAllMessages()
  {
    logger.d("reloadAllMessages >>> ");
    long l1 = SystemClock.uptimeMillis();
    this.deletedPhoneNumbers.clear();
    HashMap localHashMap1 = this.deletedPhoneNumbers;
    HashMap localHashMap2 = this.allPhoneNumbers;
    localHashMap1.putAll(localHashMap2);
    Cursor localCursor = null;
    while (true)
    {
      String str1;
      try
      {
        ContentResolver localContentResolver = this.contentResolver;
        Uri localUri = getContentUri();
        String[] arrayOfString = ALL_MESSAGES_PROJECTION;
        localCursor = localContentResolver.query(localUri, arrayOfString, null, null, null);
        if ((localCursor == null) || (!localCursor.moveToFirst()))
          break;
        if (localCursor.isAfterLast())
          break;
        long l2 = localCursor.getLong(0);
        str1 = localCursor.getString(1);
        long l3 = localCursor.getLong(2);
        Object localObject1 = this.deletedPhoneNumbers.remove(str1);
        if (this.allPhoneNumbers.containsKey(str1))
          continue;
        HashMap localHashMap3 = this.allPhoneNumbers;
        Integer localInteger = Integer.valueOf(1);
        Object localObject2 = localHashMap3.put(str1, localInteger);
        if (this.phoneNumberResolvingService == null)
        {
          Logger localLogger1 = logger;
          String str2 = "reloadAllMessages <<< NOT bound to service, not adding phone number: " + str1;
          localLogger1.d(str2);
          SMSMessage localSMSMessage1 = (SMSMessage)this.lastMessageByPhoneNumber.get(str1);
          if (localSMSMessage1 == null)
            continue;
          long l4 = localSMSMessage1.date;
          if (l3 <= l4)
            continue;
          SMSMessage localSMSMessage2 = new SMSMessage(l2,l3, str1);
          Object localObject3 = this.lastMessageByPhoneNumber.put(str1, localSMSMessage2);
          postLastMessageByPhoneNumberChanged(str1);
          boolean bool = localCursor.moveToNext();
          continue;
        }
      }
      finally
      {
        if (localCursor == null)
          continue;
        localCursor.close();
        Logger localLogger2 = logger;
        StringBuilder localStringBuilder1 = new StringBuilder().append("reloadAllMessages <<< completed in ");
        long l5 = SystemClock.uptimeMillis() - l1;
        String str3 = l5 + "ms";
        localLogger2.d(str3);
      }
      try
      {
        this.phoneNumberResolvingService.addPhoneNumber(str1);
      }
      catch (Exception localException1)
      {
        Logger localLogger3 = logger;
        String str4 = "Error invoking PhoneNumber service: " + localException1;
        localLogger3.e(str4, localException1);
      }
    }
    Iterator localIterator = this.deletedPhoneNumbers.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str5 = (String)localIterator.next();
      Object localObject5 = this.allPhoneNumbers.remove(str5);
      Object localObject6 = this.lastMessageByPhoneNumber.remove(str5);
      postLastMessageByPhoneNumberChanged(str5);
      if (this.phoneNumberResolvingService == null)
      {
        Logger localLogger4 = logger;
        String str6 = "reloadAllMessages <<< NOT bound to service, not removing phone number: " + str5;
        localLogger4.d(str6);
        continue;
      }
      try
      {
        this.phoneNumberResolvingService.removePhoneNumber(str5);
      }
      catch (Exception localException2)
      {
        Logger localLogger5 = logger;
        String str7 = "Error invoking PhoneNumber service: " + localException2;
        localLogger5.e(str7, localException2);
      }
    }
    if (localCursor != null)
      localCursor.close();
    Logger localLogger6 = logger;
    StringBuilder localStringBuilder2 = new StringBuilder().append("reloadAllMessages <<< completed in ");
    long l6 = SystemClock.uptimeMillis() - l1;
    String str8 = l6 + "ms";
    localLogger6.d(str8);
  }

  public void removeListener(int paramInt)
  {
    synchronized (this.listeners)
    {
      List localList2 = this.listeners;
      Integer localInteger = Integer.valueOf(paramInt);
      boolean bool = localList2.remove(localInteger);
      return;
    }
  }

  class ContactNameByPhoneCache extends Handler
  {
    private final HashMap<String, String> nameByPhone;

    ContactNameByPhoneCache(Looper arg2)
    {
      super();
      HashMap localHashMap = new HashMap();
      this.nameByPhone = localHashMap;
    }	
    private String lookup(String paramString)
    {
    	Cursor localCursor = null;
      try
      {
        Uri localUri1 = ContactsContract.PhoneLookup.CONTENT_FILTER_URI;
        String str1 = Uri.encode(paramString);
        Uri localUri2 = Uri.withAppendedPath(localUri1, str1);
        ContentResolver localContentResolver = MessagingAdapterAndroid.this.contentResolver;
        String[] arrayOfString = MessagingAdapterAndroid.NAME_LOOKUP_PROJECTION;
        localCursor = localContentResolver.query(localUri2, arrayOfString, null, null, null);
        if ((localCursor != null) && (localCursor.moveToFirst()))
        {
          String str2 = localCursor.getString(0);
          String str3 = str2;
     
        }
        if ((localCursor != null) && (!localCursor.isClosed()))
          localCursor.close();
        String str3 = null;
        return str3;
      }
      finally
      {
        if ((localCursor != null) && (!localCursor.isClosed()))
          localCursor.close();
      }
    }

    void addPhoneNumber(String paramString)
    {
      synchronized (this.nameByPhone)
      {
        if (!this.nameByPhone.containsKey(paramString))
        	this.nameByPhone.put(paramString, null);
        return;
      }
    }

    HashSet<String> getAllPhoneNumbers()
    {
      synchronized (this.nameByPhone)
      {
        Set localSet = this.nameByPhone.keySet();
        HashSet localHashSet = new HashSet(localSet);
        return localHashSet;
      }
    }
    String getNameByPhone(String paramString, boolean paramBoolean)
    {
      String str;
      HashMap hashmap1 ;
      synchronized (this.nameByPhone)
      {
        str = (String)this.nameByPhone.get(paramString);
        if ((str == null) && (paramBoolean))
        {
          str = lookup(paramString);
          hashmap1 = this.nameByPhone;
        }
      }
      try
      {
        if (this.nameByPhone.containsKey(str))
         this.nameByPhone.put(str, paramString);
        return str;
   
      }
      finally
      {

      }

    }
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      
      case 1:
    	  reload();
    	  break;
      case 2:
    	  notifyListeners();
    	  break;
      default:
      }
      return;
    }

    void reload()
    {
      ArrayList localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      synchronized (this.nameByPhone)
      {
        Iterator localIterator = this.nameByPhone.entrySet().iterator();
        if (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          Object localObject1 = localEntry.getKey();
          boolean bool1 = localArrayList1.add(localObject1);
          Object localObject2 = localEntry.getValue();
          boolean bool2 = localArrayList2.add(localObject2);
        }
      }

      int i = localArrayList1.size();
      int j = 0;
      int k = 0;
      while (true)
      {
        String str1 = null;
        String str3 = null;
        if (k < i)
        {
          str1 = (String)localArrayList1.get(k);
          String str2 = (String)localArrayList2.get(k);
          str3 = lookup(str1);
          if (!TextUtils.equals(str3, str2))
            j = 1;
        }
        synchronized (this.nameByPhone)
        {
           this.nameByPhone.put(str1, str3);
          k += 1;
        }
        if (!hasMessages(2))
        {
          Message localMessage = Message.obtain(this, 2);
          boolean bool3 = sendMessage(localMessage);      
        }
        else
        	return;
      }
     
    }

    void removePhoneNumber(String paramString)
    {
      synchronized (this.nameByPhone)
      {
        Object localObject1 = this.nameByPhone.remove(paramString);
        return;
      }
    }
  }

  class ContactObserver extends ContentObserver
  {
    WeakReference<MessagingAdapterAndroid> adapterRef;

	public ContactObserver(Handler handler, MessagingAdapterAndroid messagingadapterandroid)
	{
		super(handler);
		adapterRef = new WeakReference(messagingadapterandroid);
	}
	public void onChange(boolean flag)
	{
		MessagingAdapterAndroid.logger.d("ContactObserver: onChange");
		MessagingAdapterAndroid messagingadapterandroid = (MessagingAdapterAndroid)adapterRef.get();
		if (messagingadapterandroid != null)
			messagingadapterandroid.contactNameByPhoneCache.sendMessage(Message.obtain(messagingadapterandroid.contactNameByPhoneCache, 1));
	}
	
  }

  class MessagesObserver extends ContentObserver
  {
    WeakReference<MessagingAdapterAndroid> adapterRef;
 
	public MessagesObserver(Handler handler, MessagingAdapterAndroid messagingadapterandroid)
	{
		super(handler);
		adapterRef = new WeakReference(messagingadapterandroid);
	}
    public void onChange(boolean paramBoolean)
    {
      MessagingAdapterAndroid.logger.d("MessagesObserver: onChange");
      MessagingAdapterAndroid localMessagingAdapterAndroid = (MessagingAdapterAndroid)this.adapterRef.get();
      if (localMessagingAdapterAndroid != null)
      {
    	  localMessagingAdapterAndroid.onMessagesContentChanged();
  		if (!localMessagingAdapterAndroid.isLiteVersion)
  			localMessagingAdapterAndroid.allMessagesHandler.sendMessage(Message.obtain(localMessagingAdapterAndroid.allMessagesHandler, 1));
      }
    }
  }

  class AllMessagesHandler extends Handler
  {
    AllMessagesHandler(Looper arg2)
    {
      super();
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
     
      case 1:
    	  MessagingAdapterAndroid.this.reloadAllMessages();
    	  break;
      case 2:
    	  MessagingAdapterAndroid localMessagingAdapterAndroid = MessagingAdapterAndroid.this;
          String str = (String)paramMessage.obj;
          localMessagingAdapterAndroid.doUpdateLastMessageChanged(str);
          break;
      default:
      }
      return;
    }
  }

  static class SMSMessage
	{

		String address;
		long date;
		long id;

		SMSMessage(long l, long l1, String s)
		{
			id = l;
			date = l1;
			address = s;
		}
	}
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.MessagingAdapterAndroid
 * JD-Core Version:    0.6.0
 */