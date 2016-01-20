package com.softspb.shell.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.softspb.shell.calendar.service.Appointment;
import com.softspb.shell.calendar.service.CalendarClient;
import com.softspb.shell.calendar.service.CalendarService;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.Date;

public class ApptListAdapterAndroid extends ApptListAdapter
{
  public static final String EVENT_BEGIN_TIME = "beginTime";
  public static final String EVENT_END_TIME = "endTime";
  private static final Logger logger = Loggers.getLogger(ApptListAdapterAndroid.class.getName());
  private Uri CALENDAR_EVENTS_OPEN_URI;
  private CalendarClient calendarClient;
  private Context context;
  private final SparseBooleanArray isFinished;
  private final SparseArray<Object> monitors;

  public ApptListAdapterAndroid(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
    SparseArray localSparseArray = new SparseArray();
    this.monitors = localSparseArray;
    SparseBooleanArray localSparseBooleanArray = new SparseBooleanArray();
    this.isFinished = localSparseBooleanArray;
    logger.d("constructor");
  }

  public static native void addAppt(int paramInt1, String paramString1, String paramString2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, long paramLong5);

  private void notifyOnAppointmentLoaded(Appointment paramAppointment)
  {
    int i = paramAppointment.token;
    String str1 = paramAppointment.subject;
    String str2 = paramAppointment.location;
    long l1 = paramAppointment.start;
    long l2 = paramAppointment.end;
    long l3 = paramAppointment.originalStart;
    long l4 = paramAppointment.originalEnd;
    boolean bool1 = paramAppointment.isAllDay;
    boolean bool2 = paramAppointment.isRecurring;
    int j = paramAppointment.status;
    long l5 = paramAppointment.id;
    addAppt(i, str1, str2, l1, l2, l3, l4, bool1, bool2, j, l5);
  }

  private void notifyOnCalendarChanged()
  {
    logger.d(">>>notifyChange");
    update();
    logger.d("<<<notifyChange");
  }

  public static native void update();

  void abortAllRequests()
  {
    SparseArray localSparseArray = this.monitors;
    Object localObject1;
    synchronized (this.monitors)
    {
    int i = 0;
    try
    {
      int j = this.monitors.size();
      if (i < j)
      {
        int k = this.monitors.keyAt(i);
        localObject1 = this.monitors.valueAt(i);
        this.isFinished.put(k, true);
      }
    }
    finally
    {
    }
    
    this.monitors.clear();
    this.isFinished.clear();
    }
  }

  // ERROR //
  public void getApptList(int paramInt, long paramLong1, long paramLong2)
  {
    // Byte code:
    //   0: getstatic 44	com/softspb/shell/adapters/ApptListAdapterAndroid:logger	Lcom/softspb/util/log/Logger;
    //   3: astore 6
    //   5: new 168	java/lang/StringBuilder
    //   8: dup
    //   9: invokespecial 169	java/lang/StringBuilder:<init>	()V
    //   12: ldc 171
    //   14: invokevirtual 175	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   17: iload_1
    //   18: invokevirtual 178	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   21: ldc 180
    //   23: invokevirtual 175	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: astore 7
    //   28: lload_2
    //   29: invokestatic 186	com/softspb/shell/calendar/service/CalendarService:convertTime	(J)Ljava/lang/String;
    //   32: astore 8
    //   34: aload 7
    //   36: aload 8
    //   38: invokevirtual 175	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: ldc 188
    //   43: invokevirtual 175	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: astore 9
    //   48: lload 4
    //   50: invokestatic 186	com/softspb/shell/calendar/service/CalendarService:convertTime	(J)Ljava/lang/String;
    //   53: astore 10
    //   55: aload 9
    //   57: aload 10
    //   59: invokevirtual 175	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: invokevirtual 191	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   65: astore 11
    //   67: aload 6
    //   69: aload 11
    //   71: invokevirtual 67	com/softspb/util/log/Logger:d	(Ljava/lang/String;)V
    //   74: aload_0
    //   75: getfield 54	com/softspb/shell/adapters/ApptListAdapterAndroid:monitors	Landroid/util/SparseArray;
    //   78: astore 12
    //   80: aload 12
    //   82: monitorenter
    //   83: new 153	java/lang/Object
    //   86: dup
    //   87: invokespecial 192	java/lang/Object:<init>	()V
    //   90: astore 13
    //   92: aload_0
    //   93: getfield 54	com/softspb/shell/adapters/ApptListAdapterAndroid:monitors	Landroid/util/SparseArray;
    //   96: iload_1
    //   97: aload 13
    //   99: invokevirtual 195	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   102: aload_0
    //   103: getfield 59	com/softspb/shell/adapters/ApptListAdapterAndroid:isFinished	Landroid/util/SparseBooleanArray;
    //   106: iload_1
    //   107: ldc 196
    //   109: invokevirtual 151	android/util/SparseBooleanArray:put	(IZ)V
    //   112: aload 12
    //   114: monitorexit
    //   115: aload_0
    //   116: getfield 198	com/softspb/shell/adapters/ApptListAdapterAndroid:calendarClient	Lcom/softspb/shell/adapters/ApptListAdapterAndroid$CalendarClient;
    //   119: astore 14
    //   121: iload_1
    //   122: istore 15
    //   124: lload_2
    //   125: lstore 16
    //   127: lload 4
    //   129: lstore 18
    //   131: aload 14
    //   133: iload 15
    //   135: lload 16
    //   137: lload 18
    //   139: invokevirtual 201	com/softspb/shell/adapters/ApptListAdapterAndroid$CalendarClient:loadAppointments	(IJJ)V
    //   142: aload_0
    //   143: iload_1
    //   144: invokevirtual 205	com/softspb/shell/adapters/ApptListAdapterAndroid:isRequestFinished	(I)Z
    //   147: ifne +113 -> 260
    //   150: aload 13
    //   152: monitorenter
    //   153: aload 13
    //   155: invokevirtual 208	java/lang/Object:wait	()V
    //   158: aload 13
    //   160: monitorexit
    //   161: goto -19 -> 142
    //   164: astore 20
    //   166: aload 13
    //   168: monitorexit
    //   169: aload 20
    //   171: athrow
    //   172: astore 21
    //   174: getstatic 44	com/softspb/shell/adapters/ApptListAdapterAndroid:logger	Lcom/softspb/util/log/Logger;
    //   177: astore 22
    //   179: new 168	java/lang/StringBuilder
    //   182: dup
    //   183: invokespecial 169	java/lang/StringBuilder:<init>	()V
    //   186: ldc 210
    //   188: invokevirtual 175	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   191: aload 21
    //   193: invokevirtual 213	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   196: invokevirtual 191	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   199: astore 23
    //   201: aload 22
    //   203: aload 23
    //   205: aload 21
    //   207: invokevirtual 217	com/softspb/util/log/Logger:e	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   210: aload_0
    //   211: getfield 54	com/softspb/shell/adapters/ApptListAdapterAndroid:monitors	Landroid/util/SparseArray;
    //   214: astore 12
    //   216: aload 12
    //   218: monitorenter
    //   219: aload_0
    //   220: getfield 54	com/softspb/shell/adapters/ApptListAdapterAndroid:monitors	Landroid/util/SparseArray;
    //   223: iload_1
    //   224: invokevirtual 221	android/util/SparseArray:delete	(I)V
    //   227: aload_0
    //   228: getfield 59	com/softspb/shell/adapters/ApptListAdapterAndroid:isFinished	Landroid/util/SparseBooleanArray;
    //   231: iload_1
    //   232: invokevirtual 222	android/util/SparseBooleanArray:delete	(I)V
    //   235: aload 12
    //   237: monitorexit
    //   238: getstatic 44	com/softspb/shell/adapters/ApptListAdapterAndroid:logger	Lcom/softspb/util/log/Logger;
    //   241: ldc 224
    //   243: invokevirtual 67	com/softspb/util/log/Logger:d	(Ljava/lang/String;)V
    //   246: return
    //   247: astore 20
    //   249: aload 12
    //   251: monitorexit
    //   252: aload 20
    //   254: athrow
    //   255: astore 24
    //   257: aload 13
    //   259: monitorexit
    //   260: aload_0
    //   261: getfield 54	com/softspb/shell/adapters/ApptListAdapterAndroid:monitors	Landroid/util/SparseArray;
    //   264: astore 12
    //   266: aload 12
    //   268: monitorenter
    //   269: aload_0
    //   270: getfield 54	com/softspb/shell/adapters/ApptListAdapterAndroid:monitors	Landroid/util/SparseArray;
    //   273: iload_1
    //   274: invokevirtual 221	android/util/SparseArray:delete	(I)V
    //   277: aload_0
    //   278: getfield 59	com/softspb/shell/adapters/ApptListAdapterAndroid:isFinished	Landroid/util/SparseBooleanArray;
    //   281: iload_1
    //   282: invokevirtual 222	android/util/SparseBooleanArray:delete	(I)V
    //   285: aload 12
    //   287: monitorexit
    //   288: goto -50 -> 238
    //   291: astore 25
    //   293: aload 12
    //   295: monitorexit
    //   296: aload 25
    //   298: athrow
    //   299: astore 20
    //   301: aload 12
    //   303: monitorexit
    //   304: aload 20
    //   306: athrow
    //   307: astore 20
    //   309: aload_0
    //   310: getfield 54	com/softspb/shell/adapters/ApptListAdapterAndroid:monitors	Landroid/util/SparseArray;
    //   313: astore 12
    //   315: aload 12
    //   317: monitorenter
    //   318: aload_0
    //   319: getfield 54	com/softspb/shell/adapters/ApptListAdapterAndroid:monitors	Landroid/util/SparseArray;
    //   322: iload_1
    //   323: invokevirtual 221	android/util/SparseArray:delete	(I)V
    //   326: aload_0
    //   327: getfield 59	com/softspb/shell/adapters/ApptListAdapterAndroid:isFinished	Landroid/util/SparseBooleanArray;
    //   330: iload_1
    //   331: invokevirtual 222	android/util/SparseBooleanArray:delete	(I)V
    //   334: aload 12
    //   336: monitorexit
    //   337: aload 20
    //   339: athrow
    //   340: astore 20
    //   342: aload 12
    //   344: monitorexit
    //   345: aload 20
    //   347: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   153	158	164	finally
    //   158	164	164	finally
    //   257	260	164	finally
    //   115	153	172	java/lang/Exception
    //   169	172	172	java/lang/Exception
    //   83	115	247	finally
    //   249	252	247	finally
    //   153	158	255	java/lang/InterruptedException
    //   269	291	291	finally
    //   219	238	299	finally
    //   301	304	299	finally
    //   115	153	307	finally
    //   169	172	307	finally
    //   174	210	307	finally
    //   318	337	340	finally
    //   342	345	340	finally
  }

  boolean isRequestFinished(int paramInt)
  {
    synchronized (this.monitors)
    {
      boolean bool = this.isFinished.get(paramInt);
      return bool;
    }
  }

	public void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		logger.d((new StringBuilder()).append("onCreate >>> context=").append(context1.getPackageName()).toString());
		context = context1;
		calendarClient = new CalendarClient(context1);
		boolean flag;
		String s;
		if (android.os.Build.VERSION.SDK_INT > 7)
			flag = true;
		else
			flag = false;
		if (flag)
			s = "com.android.calendar";
		else
			s = "calendar";
		logger.d((new StringBuilder()).append("onCreate: authority=").append(s).toString());
		CALENDAR_EVENTS_OPEN_URI = Uri.parse((new StringBuilder()).append("content://").append(s).append("/events/").toString());
		calendarClient.connect();
		logger.d("onCreate <<<");
	}

  public void onDestroy(Context paramContext)
  {
    logger.d("onDestroy");
    this.calendarClient.disconnect();
  }

  public void openAppt(long paramLong1, long paramLong2, long paramLong3)
  {
    Logger localLogger = logger;
    StringBuilder localStringBuilder1 = new StringBuilder().append(">>>openAppt: id=").append(paramLong1).append(", begin=");
    String str1 = CalendarService.convertTime(paramLong2);
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(", end=");
    String str2 = CalendarService.convertTime(paramLong3);
    String str3 = str2;
    localLogger.d(str3);
    Uri localUri = ContentUris.withAppendedId(this.CALENDAR_EVENTS_OPEN_URI, paramLong1);
    Intent localIntent1 = new Intent("android.intent.action.VIEW", localUri);
    Intent localIntent2 = localIntent1.putExtra("beginTime", paramLong2);
    Intent localIntent3 = localIntent1.putExtra("endTime", paramLong3);
    try
    {
      this.context.startActivity(localIntent1);
      logger.d("<<<openAppt");
      return;
    }
    catch (Throwable localThrowable)
    {
      while (true)
        logger.e("openAppt failed:", localThrowable);
    }
  }

  public void openCalendar(long paramLong)
  {
    if (paramLong == 0L)
      paramLong = new Date().getTime();
    String str = "content://com.android.calendar/time/" + paramLong;
    Intent localIntent1 = new Intent("android.intent.action.VIEW");
    Intent localIntent2 = localIntent1.putExtra("VIEW", "DAY");
    Uri localUri = Uri.parse(str);
    Intent localIntent3 = localIntent1.setDataAndType(localUri, "time/epoch");
    try
    {
      this.context.startActivity(localIntent1);
      return;
    }
    catch (Throwable localThrowable)
    {
      while (true)
        localThrowable.printStackTrace();
    }
  }

	public boolean openCreateApptDialog(long l)
	{
		boolean flag = false;
		if (l == 0L)
			l = (new Date()).getTime();
		Intent intent = new Intent();
		intent.putExtra("beginTime", l);
		intent.putExtra("endTime", l);
		intent.setType("vnd.android.cursor.item/event");
		intent.setAction("android.intent.action.EDIT");
		if (context.getPackageManager().resolveActivity(intent, 0) != null)
		{
			context.startActivity(intent);
			flag = true;
		}
		return flag;
	}
  class CalendarClient extends com.softspb.shell.calendar.service.CalendarClient
//  class CalendarClient extends CalendarClient
  {
    boolean wasDisconnected = false;

    CalendarClient(Context arg2)
    {
      super(arg2);
    }

    protected void onAppointmentLoaded(Appointment paramAppointment)
    {
      Logger localLogger = ApptListAdapterAndroid.logger;
      String str = "CalendarClient.onAppointmentLoaded: " + paramAppointment;
      localLogger.d(str);
      ApptListAdapterAndroid.this.notifyOnAppointmentLoaded(paramAppointment);
    }

    protected void onCalendarChanged()
    {
      ApptListAdapterAndroid.logger.d("CalendarClient.onCalendarChanged");
      ApptListAdapterAndroid.this.notifyOnCalendarChanged();
    }

    protected void onConnected()
    {
      ApptListAdapterAndroid.logger.d("CalendarClient.onConnected");
      if (this.wasDisconnected)
        ApptListAdapterAndroid.this.notifyOnCalendarChanged();
    }

    protected void onDisconnected()
    {
      ApptListAdapterAndroid.logger.d("CalendarClient.onDisconnected");
      ApptListAdapterAndroid.this.abortAllRequests();
      this.wasDisconnected = true;
    }

    protected void onFinishedReloadingAppointments(int paramInt)
    {
      Logger localLogger1 = ApptListAdapterAndroid.logger;
      String str1 = "onFinishedReloadingAppointments: token=" + paramInt;
      localLogger1.d(str1);
      while (true)
      {
        synchronized (ApptListAdapterAndroid.this.monitors)
        {
          Object localObject1 = ApptListAdapterAndroid.this.monitors.get(paramInt);
          if (localObject1 != null)
          {
            Logger localLogger2 = ApptListAdapterAndroid.logger;
            String str2 = "onFinishedReloadingAppointments: finishing request for token=" + paramInt;
            localLogger2.d(str2);
            ApptListAdapterAndroid.this.isFinished.put(paramInt, true);
            synchronized(localObject1)
            {
	            try
	            {
	              localObject1.notifyAll();
	              return;
	            }
	            finally
	            {
	            }
            }
          }
        }
        Logger localLogger3 = ApptListAdapterAndroid.logger;
        String str3 = "onFinishedReloadingAppointments: request not found for token=" + paramInt;
        localLogger3.d(str3);
      }
    }

    protected void onPidResponse(int paramInt)
    {
      Logger localLogger = ApptListAdapterAndroid.logger;
      String str = "onPidResponse: pid=" + paramInt;
      localLogger.d(str);
    }

    protected void onStartedReloadingAppointments(int paramInt)
    {
      Logger localLogger = ApptListAdapterAndroid.logger;
      String str = "onStartedReloadingAppointments: token=" + paramInt;
      localLogger.d(str);
    }
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.ApptListAdapterAndroid
 * JD-Core Version:    0.6.0
 */