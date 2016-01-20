package com.softspb.shell.adapters;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.contacts.IPhoneNumberResolvingService;
import com.spb.contacts.IPhoneNumberResolvingServiceCallback;
import com.spb.contacts.IPhoneNumberResolvingService;

public class CallLogAdapterAndroid
  implements CallLogAdapter
{
  private static final String[] CALLS_PROJECTION ;
  static final int CALL_LOG_ACTION_ADD = 1;
  static final int CALL_LOG_ACTION_NOTIFY = 3;
  static final int CALL_LOG_ACTION_REMOVE = 2;
  private static final String CALL_LOG_ORDER_TIME_DESC;
  private static final int INDEX_DATE = 1;
  private static final int INDEX_DURATION = 4;
  private static final int INDEX_ID = 0;
  private static final int INDEX_NEW = 5;
  private static final int INDEX_NUMBER = 3;
  private static final int INDEX_TYPE = 2;
  static final int MSG_CALL_LOG_ACTION = 42;
  static final int MSG_RELOAD_CALL_LOG = 43;
  static final Logger logger;
  final ConcurrentLinkedQueue<CallLogAction> callLogActionsQueue;
  final SparseArray<CallLogEntry> callLogEntries;
  CallLogObserver callLogObserver;
  HandlerThread contentObserverThread;
  Handler contentReloadHandler;
  ContentResolver contentResolver;
  Context context;
  int nativeAdapterToken;
  private NativeCallbacks nativeCallbacks;
  Handler notifierHandler;
  HandlerThread notifierThread;
  final IPhoneNumberResolvingServiceCallback phoneNumberCallback;
  private IPhoneNumberResolvingService phoneNumberResolvingService;
  final ServiceConnection phoneNumberServiceConnection;
  private CountDownLatch serviceConnectionCountDown;

  static
  {
    String[] arrayOfString = new String[6];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "date";
    arrayOfString[2] = "type";
    arrayOfString[3] = "number";
    arrayOfString[4] = "duration";
    arrayOfString[5] = "new";
    CALLS_PROJECTION = arrayOfString;
    CALL_LOG_ORDER_TIME_DESC = "date" + " DESC";
    logger = Loggers.getLogger(CallLogAdapterAndroid.class.getName());
  }
  class CallLogAdapterAndroid$1 extends IPhoneNumberResolvingServiceCallback.Stub
  {
    public void onResolvedPhonesChanged(int paramInt)
      throws RemoteException
    {   
		CallLogAdapterAndroid.logger.d((new StringBuilder()).append("onResolvedPhonesChanged: contactId=").append(paramInt).toString());
		postCallLogAction(CallLogAction.notifyCallLogChanged());

    }
  }
  
  class CallLogAdapterAndroid$2  implements ServiceConnection
  {
	public void onServiceConnected(ComponentName componentname, IBinder ibinder)
	{
		CallLogAdapterAndroid.logger.d((new StringBuilder()).append("onServiceConnected: PhoneNumberService name=").append(componentname).toString());
		phoneNumberResolvingService = com.spb.contacts.IPhoneNumberResolvingService.Stub.asInterface(ibinder);
		try
		{
			phoneNumberResolvingService.registerCallback(phoneNumberCallback);
		}
		catch (RemoteException remoteexception) { }
		serviceConnectionCountDown.countDown();
	}
  public void onServiceDisconnected(ComponentName paramComponentName)
  {
	phoneNumberResolvingService = null;
  }
  
}
  
  public CallLogAdapterAndroid(int paramInt, Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    SparseArray localSparseArray = new SparseArray();
    this.callLogEntries = localSparseArray;
    ConcurrentLinkedQueue localConcurrentLinkedQueue = new ConcurrentLinkedQueue();
    this.callLogActionsQueue = localConcurrentLinkedQueue;
    CallLogAdapterAndroid$1 local1 = new CallLogAdapterAndroid$1();
    this.phoneNumberCallback = local1;
    CallLogAdapterAndroid$2 local2 = new CallLogAdapterAndroid$2();
    this.phoneNumberServiceConnection = local2;
    Logger localLogger = logger;
    StringBuilder localStringBuilder = new StringBuilder().append("Ctor: nativeAdapterToken=0x");
    String str1 = Integer.toHexString(paramInt);
    String str2 = str1 + " context=" + paramContext;
    localLogger.d(str2);
    this.nativeAdapterToken = paramInt;
    this.context = paramContext;
    this.nativeCallbacks = paramNativeCallbacks;
    ContentResolver localContentResolver = paramContext.getContentResolver();
    this.contentResolver = localContentResolver;
  }

  private static native void addToCallLog(int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4, String paramString, boolean paramBoolean);

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
        Logger localLogger = logger;
        String str2 = "connection to server interrupted: " + localInterruptedException;
        localLogger.e(str2, localInterruptedException);
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
			catch (Exception exception) { }
			context.unbindService(phoneNumberServiceConnection);
			phoneNumberResolvingService = null;
		}
	}
  
  private void doReloadCallLog()
  {
    Logger localLogger1 = logger;
    StringBuilder localStringBuilder1 = new StringBuilder().append("doReloadCallLog >>> thread=");
    String str1 = Thread.currentThread().toString();
    String str2 = str1;
    localLogger1.d(str2);
    SparseIntArray localSparseIntArray;
    Cursor localCursor = null;
    while (true)
    {
      synchronized (this.callLogEntries)
      {
        localSparseIntArray = new SparseIntArray();
        int i = this.callLogEntries.size();
        int j = 0;
        if (j >= i)
          continue;
        else
        {
	        int k = this.callLogEntries.keyAt(j);
	        localSparseIntArray.put(k, 1);
	        j += 1;
        }
        try
        {
          localCursor = queryCallLog();
          Logger localLogger2 = logger;
          StringBuilder localStringBuilder2 = new StringBuilder().append("queried call log: count=");
          if (localCursor == null)
          {
            String str3 = "null";
            String str4 = str3;
            localLogger2.d(str4);
            if ((localCursor == null) || (!localCursor.moveToFirst()))
              break;
            if (localCursor.isAfterLast())
              break;
            int m = processCallLogRow(localCursor);
            localSparseIntArray.delete(m);
            boolean bool = localCursor.moveToNext();
            continue;
          }
        }
        finally
        {
          if (localCursor != null)
          {
        	  localCursor.close();
          }
        }
      }
      Integer localInteger = Integer.valueOf(localCursor.getCount());
    }
    int i = localSparseIntArray.size();
    int j = 0;
    while (j < i)
    {
      int n = localSparseIntArray.keyAt(j);
      Logger localLogger3 = logger;
      String str5 = "removed from call log: callTime=" + n;
      localLogger3.d(str5);
      CallLogAction localCallLogAction = CallLogAction.remove(n);
      postCallLogAction(localCallLogAction);
      CallLogEntry localCallLogEntry = (CallLogEntry)this.callLogEntries.get(n);
      this.callLogEntries.delete(n);
      if (localCallLogEntry != null)
      {
        if (this.phoneNumberResolvingService != null)
        {
        	try
        	{
        	phoneNumberResolvingService.removePhoneNumber(localCallLogEntry.phoneNumber);
        	}
        	catch (Exception localException)
            {
              Logger localLogger5 = logger;
              String str9 = "Error invoking PhoneNumber service: " + localException;
              localLogger5.e(str9, localException);
            }
        }
        Logger localLogger4 = logger;
        StringBuilder localStringBuilder3 = new StringBuilder().append("doReloadCallLog: NOT bound to service, not removing phone number: ");
        String str6 = localCallLogEntry.phoneNumber;
        String str7 = str6;
        localLogger4.w(str7);
      }
      else
      {
    	  j++;
      }  
     
    }
    if (localCursor != null)
      localCursor.close();
    logger.d("reloadCallLog: <<<");
  }

	private static int getNativeCallType(int i)
	{
		byte byte0;
		switch (i) {
		case 1:
			byte0 = 1;
			break;
		case 2:
			byte0 = 3;
			break;
		case 3:
			byte0 = 0;
			break;
		default:
			byte0 = 2;
		}
		return byte0;
	}
  private static native void notifyCallDetailsChanged(int paramInt);

  private void postCallLogAction(CallLogAction paramCallLogAction)
  {
    if (this.notifierHandler != null)
    {
      boolean bool1 = this.callLogActionsQueue.add(paramCallLogAction);
      Handler localHandler = this.notifierHandler;
      Message localMessage = Message.obtain(this.notifierHandler, 42);
      boolean bool2 = localHandler.sendMessage(localMessage);
    }
  }

  private void postReloadCallLog()
  {
    if (this.contentReloadHandler != null)
    {
      Handler localHandler = this.contentReloadHandler;
      Message localMessage = Message.obtain(this.contentReloadHandler, 43);
      boolean bool = localHandler.sendMessage(localMessage);
    }
  }

  private int processCallLogRow(Cursor cursor)
	{
		int i = (int)cursor.getLong(0);
		logger.d((new StringBuilder()).append("Received new call log entry: callId=").append(i).toString());
		CallLogEntry calllogentry = (CallLogEntry)callLogEntries.get(i);
		long l = cursor.getLong(1);
		int j = cursor.getInt(2);
		String s = cursor.getString(3);
		long l1 = cursor.getLong(4);
		int k = getNativeCallType(j);
		boolean flag;
		boolean flag1;
		if (cursor.getInt(5) != 0)
			flag = true;
		else
			flag = false;
		if (calllogentry == null || calllogentry.isNew != flag)
			flag1 = true;
		else
			flag1 = false;
		if (calllogentry == null)
		{
			calllogentry = new CallLogEntry(s);
			callLogEntries.put(i, calllogentry);
			if (phoneNumberResolvingService == null)
				logger.w((new StringBuilder()).append("processCallLogRow: NOT bound to service, not adding phone number: ").append(s).toString());
			else
				try
				{
					phoneNumberResolvingService.addPhoneNumber(s);
				}
				catch (Exception exception)
				{
					logger.e((new StringBuilder()).append("Error invoking PhoneNumber service: ").append(exception).toString(), exception);
				}
		}
		calllogentry.isNew = flag;
		if (flag1)
			postCallLogAction(CallLogAction.add(i, l, k, (int)l1, s, flag));
		return i;
	}

//  private int processCallLogRow(Cursor paramCursor)
//  {
//    int i = (int)paramCursor.getLong(0);
//    Logger localLogger1 = logger;
//    String str1 = "Received new call log entry: callId=" + i;
//    localLogger1.d(str1);
//    CallLogEntry localCallLogEntry = (CallLogEntry)this.callLogEntries.get(i);
//    long l1 = paramCursor.getLong(1);
//    int j = paramCursor.getInt(2);
//    String str2 = paramCursor.getString(3);
//    long l2 = paramCursor.getLong(4);
//    int k = getNativeCallType(j);
//    int m;
//    int i1;
//    if (paramCursor.getInt(5) != 0)
//    {
//      m = 1;
//      if ((localCallLogEntry != null) && (localCallLogEntry.isNew == m))
//        break label245;
//      i1 = 1;
//      label129: if (localCallLogEntry == null)
//      {
//        localCallLogEntry = new CallLogEntry();
//        this.callLogEntries.put(i, localCallLogEntry);
//        if (this.phoneNumberResolvingService != null)
//          break label251;
//        Logger localLogger2 = logger;
//        String str3 = "processCallLogRow: NOT bound to service, not adding phone number: " + str2;
//        localLogger2.w(str3);
//      }
//    }
//    while (true)
//    {
//      localCallLogEntry.isNew = m;
//      if (i1 != 0)
//      {
//        int i2 = (int)l2;
//        CallLogAction localCallLogAction = CallLogAction.add(i, l1, k, i2, str2, m);
//        postCallLogAction(localCallLogAction);
//      }
//      return i;
//      int n = 0;
//      break;
//      label245: i1 = 0;
//      break label129;
//      try
//      {
//        label251: this.phoneNumberResolvingService.addPhoneNumber(str2);
//      }
//      catch (Exception localException)
//      {
//        Logger localLogger3 = logger;
//        String str4 = "Error invoking PhoneNumber service: " + localException;
//        localLogger3.e(str4, localException);
//      }
//    }
//  }
  private Cursor queryCallLog()
	{
		return contentResolver.query(android.provider.CallLog.Calls.CONTENT_URI, CALLS_PROJECTION, null, null, CALL_LOG_ORDER_TIME_DESC);
	}

  private static native void removeFromCallLog(int paramInt1, int paramInt2);

	public void onStart()
	{
		logger.d("onStart");
		connectToPhoneNumberService();
		contentObserverThread = new HandlerThread("CallLogAdapter_ContentObserver");
		contentObserverThread.start();
		notifierThread = new HandlerThread("CallLogAdapter_Notifier");
		notifierThread.start();
		notifierHandler = new CallLogHandler(notifierThread.getLooper());
		contentReloadHandler = new CallLogHandler(contentObserverThread.getLooper());
		callLogObserver = new CallLogObserver(contentReloadHandler, this);
		contentResolver.registerContentObserver(android.provider.CallLog.Calls.CONTENT_URI, true, callLogObserver);
	}
  public void onStop()
  {
    logger.d("onStop");
    if (this.contentObserverThread != null)
    {
      this.contentObserverThread.getLooper().quit();
      this.contentReloadHandler.removeCallbacksAndMessages(null);
      this.contentObserverThread = null;
      this.notifierThread.getLooper().quit();
      this.notifierHandler.removeCallbacksAndMessages(null);
      this.notifierThread = null;
      ContentResolver localContentResolver = this.contentResolver;
      CallLogObserver localCallLogObserver = this.callLogObserver;
      localContentResolver.unregisterContentObserver(localCallLogObserver);
    }
    disconnectFromPhoneNumberService();
    this.callLogEntries.clear();
    this.callLogActionsQueue.clear();
    this.nativeAdapterToken = 0;
  }

  public void openCallLog()
  {
    boolean bool = this.nativeCallbacks.getProgramListAdapter().launch("calllog");
  }

  public void reloadCallLog()
  {
    logger.d("reloadCallLog");
    doReloadCallLog();
    if (this.notifierHandler.hasMessages(42))
      try
      {
        synchronized (this.notifierHandler)
        {
          this.notifierHandler.wait(100L);
        }
      }
      catch (InterruptedException localInterruptedException)
      {
      
      }
  }

  class CallLogHandler extends Handler
  {
    public CallLogHandler(Looper arg2)
    {
      super();
    }

    public synchronized void handleMessage(Message message)
	{
		if (message.what != 42)
		{
			do
			{
				CallLogAction calllogaction = (CallLogAction)callLogActionsQueue.poll();
				if (calllogaction == null)
					break;
				calllogaction.perform(nativeAdapterToken);
			} while (true);
		}
		
		{
			notifyAll();		
			if (message.what == 43)
			{
				removeMessages(43);
				doReloadCallLog();
			}
		}
	}
  }

  class CallLogObserver extends ContentObserver
  {
    WeakReference<CallLogAdapterAndroid> adapterRef;

	CallLogObserver(Handler handler, CallLogAdapterAndroid calllogadapterandroid)
	{
		super(handler);
		adapterRef = new WeakReference(calllogadapterandroid);
	}
    public void onChange(boolean paramBoolean)
    {
      CallLogAdapterAndroid.logger.d("CallLogObserver.onChange");
      CallLogAdapterAndroid localCallLogAdapterAndroid = (CallLogAdapterAndroid)this.adapterRef.get();
      if (localCallLogAdapterAndroid != null)
        localCallLogAdapterAndroid.postReloadCallLog();
    }
  }

 static class CallLogActionAdd extends CallLogAdapterAndroid.CallLogAction
  {
    private final int duration;
    private final boolean isNew;
    private final String phone;
    private final long time;
    private final int type;

	public CallLogActionAdd(int i, long l, int j, int k, String s, boolean flag)
	{
		super(1,i);
		time = l;
		type = j;
		duration = k;
		phone = s;
		isNew = flag;
	}
	void perform(int i)
	{
		if (action == 1)
		{
			CallLogAdapterAndroid.logger.d((new StringBuilder()).append("Invoking NativeCalls.addToCallLog callId=").append(callId).append(" time=").append(time).append(" type=").append(type).append(" duration=").append(duration).append(" phone=").append(phone).append(" isNew=").append(isNew).toString());
			CallLogAdapterAndroid.addToCallLog(i, callId, time, type, duration, phone, isNew);
		} else
		{
			super.perform(i);
		}
	}
  }

  static class CallLogAction
  {
    static int instanceCount = 0;
    final int action;
    final int callId;
    int id;

	private CallLogAction(int i, int j)
	{
		int k = 1 + instanceCount;
		instanceCount = k;
		id = k;
		action = i;
		callId = j;
	}
	static CallLogAction add(int i, long l, int j, int k, String s, boolean flag)
	{
		
		return new CallLogAdapterAndroid.CallLogActionAdd(i, l, j, k, s, flag);
	}
	
    static CallLogAction notifyCallLogChanged()
    {
      return new CallLogAction(3, 0);
    }

    static CallLogAction remove(int paramInt)
    {
      return new CallLogAction(2, paramInt);
    }

	void perform(int i)
	{
		if (action != 2)
		{
			if (action == 3)
			{
				CallLogAdapterAndroid.logger.d("Invoking NativeCalls.notifyCallDetailsChanged");
				CallLogAdapterAndroid.notifyCallDetailsChanged(i);
			}
		}
		else
		{
			CallLogAdapterAndroid.logger.d((new StringBuilder()).append("Invoking NativeCalls.removeFromCallLog callId=").append(callId).toString());
			CallLogAdapterAndroid.removeFromCallLog(i, callId);

		}
		return;
		
	}

	public String toString()
	{
		return (new StringBuilder()).append("CallLogAction-").append(id).toString();
	}
  }

	static class CallLogEntry
	{

		boolean isNew;
		String phoneNumber;

		CallLogEntry(String s)
		{
			phoneNumber = s;
		}
	}
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.CallLogAdapterAndroid
 * JD-Core Version:    0.6.0
 */