package com.softspb.shell;


import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

//import ru.yandex.common.clid.ClidManager;
//import ru.yandex.startup.StartupUUIDClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.softspb.shell.view.*;
import com.softspb.shell.adapters.*;
import com.softspb.shell.adapters.dialog.NewDialogAdapter;
import com.softspb.shell.adapters.wallpaper.WallpaperAdapter;
import com.softspb.shell.data.BaseWidgetInfo;
import com.softspb.shell.data.WidgetsContract;
import com.softspb.shell.opengl.MyGlSurfaceView;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.shell.opengl.NativeCalls;
import com.softspb.shell.service.ForegroundService;
import com.softspb.shell.util.DebugUtil;
import com.softspb.shell.util.MenuController;
import com.softspb.shell.util.orm.CursorDataAdapter;
import com.softspb.shell.util.orm.DataBuilder;
import com.softspb.shell.view.FilterPickDialogBuilder;
import com.softspb.shell.view.WidgetController2;
import com.softspb.shell.widget.ShellAppWidgetHost;
import com.softspb.util.CollectionFactory;
import com.softspb.util.Conditions;
import com.softspb.util.DateChangedObserver;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.pick.CitySelectionActivity;
import com.spb.cities.provider.CitiesContract;
import com.spb.programlist.ProgramList;
import com.spb.shell3d.R;

public class Home extends Activity
{
  private static final String CLID_PACKAGE_NAME = "com.spb.shell3d";
  public static final int DIALOG_CHOOSE_HOME = 13;
  private static final int DIALOG_LOAD_FAILED = 12;
  private static final int DIALOG_SD_INSTALL = 14;
  public static final int DIALOG_USER_ID_START = 100;
  private static final int HOST_ID = 2010;
  private static final String LOG_TAG = "Spb Shell 3D";
  public static String PACKAGE_NAME;
  private static final String PREFERENCE_LOAD_SUCCESS = "load_success";
  public static final String SPB_PERMISSION = ".spb.permission";
  private static final String STARTUP_SERVICE_URL = "http://mobile.shell.heroism.yandex.ru/shell/startup";
  private static final int UNSUPPORTED_DEVICE_DIALOG = 11;
  private static ShellApplication application;
  private static final Logger logger = Loggers.getLogger(Home.class.getName());
  public static boolean useLiveWallpaper;
  private AppWidgetManager appWidgetManager;
  private Locale currentLocale;
  private DecoratedBroadcastReceiver externalAppReceiver;
  private Map<Integer, Uri> id2IconUri;
  private Map<Integer, String> id2Label;
  private boolean isResumed;
  private DecoratedBroadcastReceiver killReceiver;
  private BroadcastReceiver licenseCheckFailedReceiver;
  private View menuAnchorView;
  private MenuController menuController;
  private MovementController movementController;
  private NativeCallbacks nc;
  private int onCreateCounter;
  private int onDestroyCounter;
  private int onPauseCounter;
  private int onRestartCounter;
  private int onResumeCounter;
  private int onStartCounter;
  private int onStopCounter;
  private final Set<PauseResumeListener> pauseResumeListeners;
  private Set<BaseWidgetInfo> preInited;
  private ProgressDialog progressDialog;
  private Set<BaseWidgetInfo> restoredWidgets;
  private boolean skip;
  private BroadcastReceiver uninstallReceiver;
  private boolean useTestMenu;
  private MyGlSurfaceView view;
  private WallpaperManager wallpaperManager;
  private boolean wasPaused;
  private WidgetController2 widgetController;
  private ShellAppWidgetHost widgetHost;
  private int widgetToken;

  class Home$2 implements IActionListener
  {
    public void onAction(Context paramContext, Intent paramIntent)
    {
      restartShell();
    }
  }
  class Home$3 extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      String str = paramIntent.getData().getSchemeSpecificPart();
//      if (paramIntent.getBooleanExtra("android.intent.extra.REPLACING", false));
//      while (true)
//      {
//        return;
//        widgetsDelete(str);
//      }
      if (!paramIntent.getBooleanExtra("android.intent.extra.REPLACING", false))
			widgetsDelete(str);
    }
  }
  class Home$4
  implements View.OnKeyListener
{
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
//    Home.access$100().e("will be dispacthed");
    return false;
  }
}
  class Home$5
  implements DecoratedBroadcastReceiver.IActionListener
{
  public void onAction(Context paramContext, Intent paramIntent)
  {
    String[] arrayOfString = paramIntent.getStringArrayExtra("android.intent.extra.changed_package_list");
    int i = arrayOfString.length;
    int j = 0;
    while (j < i)
    {
      String str = arrayOfString[j];
      widgetsDelete(str);
      j += 1;
    }
  }
}
  class Home$20 extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {      
    	System.out.println("Home.onReceive");
    	((ShellApplication)getApplication()).onLicenseCheckFailed(Home.this, paramIntent);
    }
  }
  class Home$9 extends Thread
  {
    public void run()
    {
      try
      {
        ContentResolver localContentResolver = getContentResolver();
        Uri localUri = CitiesContract.Cities.getContentUri(getApplicationContext());
        Cursor localCursor1 = localContentResolver.query(localUri, null, "city_id=0", null, null);
        Cursor localCursor2 = localCursor1;
        if (localCursor2 != null)
          localCursor2.close();
        return;
      }
      finally
      {
      }
 
    }
  }
  
//  class Home$6 extends StartupUUIDClient
//  {	
//    public Home$6(Context paramContext, String paramString1,
//			String paramString2, String paramString3) {
//		super(paramContext, paramString1, paramString2, paramString3);
//		// TODO Auto-generated constructor stub
//	}
//
//	protected void onStartupCompleted()
//    {
////      stop();
//    }
//  }
  class Home$7 implements Runnable
{
	  private PauseResumeListener val$listener;
	  public Home$7(PauseResumeListener paramPauseResumeListener)
	  {
		this.val$listener = paramPauseResumeListener;
	  }

	  public void run()
	  {
		pauseResumeListeners.add(this.val$listener);
	  }
}
  
  class Home$8  implements Runnable
  {
	  private PauseResumeListener val$listener;
	  public Home$8(PauseResumeListener paramPauseResumeListener) {
		this.val$listener = paramPauseResumeListener;
	  }

	public void run()
	  {
		pauseResumeListeners.remove(val$listener);
	  }
  }
  class Home$10  implements Runnable
  {
  		private Intent val$request;
  		private int val$requestCode;
    public Home$10(Intent request, int requestCode)	
    {
  	  this.val$request = request;
  	  this.val$requestCode =requestCode;
    }
    public void run()
    {
      try
      {
//        Home localHome = this.this$0;
        Intent localIntent1 = this.val$request;
        int i = this.val$requestCode;
//        localHome.startActivityForResult(localIntent1, i);
        startActivityForResult(localIntent1, i);
        return;
      }
      catch (Exception exception)
      {
  		Home.logger.e((new StringBuilder()).append("Starting request (").append(val$request).append(") failed: ").append(exception).toString(), exception);
      }
    }
  }
    
  class Home$11  implements Runnable
  {
	  private AppWidgetHostView val$v;
	  private BaseWidgetInfo val$baseInfo;
	  
	  public Home$11(AppWidgetHostView localAppWidgetHostView,
			BaseWidgetInfo localBaseWidgetInfo1) {
	  this.val$v = localAppWidgetHostView;
	  this.val$baseInfo = localBaseWidgetInfo1;
	  }
	  public void run()
	  {
		widgetController.restoreWidget(val$v,val$baseInfo);
	  }
	}
  
  class Home$12 implements FilterPickDialogBuilder.DialogResult
  {
	public void onResult(Intent paramIntent)
	  {
	    int i = Home.RequestCode.REQUEST_INSTALL_SHORTCUT;
	    startActivityForResult(paramIntent, i);
	  }
  }
  class Home$13  implements Runnable
  {
	  private int val$itemType;
	  
	  public Home$13(int paramInt) {
		  this.val$itemType = paramInt;
	}
	  public void run()
	  {
	    switch (this.val$itemType)
	    {
	    case 1:
	    	showAddAppWidgetActivity();
	    	return;
	    case 2:
	    	showAddShortcutDialog();
	    	return;
	    }
	  }
  }
  class Home$14  implements DialogInterface.OnClickListener
  {
	  public void onClick(DialogInterface paramDialogInterface, int paramInt)
	  {
	    showDialog(13);
	  }
  }
  class Home$15  implements DialogInterface.OnClickListener
  {
	  public void onClick(DialogInterface paramDialogInterface, int paramInt)
	  {
	    restartShell();
	  }
  }
  class Home$16  implements DialogInterface.OnClickListener
  {
	  public void onClick(DialogInterface paramDialogInterface, int paramInt)
	  {    
	    startActivity(new Intent("android.settings.MANAGE_APPLICATIONS_SETTINGS"));
		finish();
	  }
  }
  class Home$17  implements FilterPickDialogBuilder.IFilter
  {
	  public boolean filter(ResolveInfo paramResolveInfo)
	  {
		boolean flag;
		if (!paramResolveInfo.activityInfo.packageName.equalsIgnoreCase(getPackageName()))
			flag = true;
		else
			flag = false;
		return flag;
	  }
  }
  class Home$18  implements FilterPickDialogBuilder.DialogResult
  {
	  public void onResult(Intent paramIntent)
	  {
	    finish();
	   startActivity(paramIntent);
	  }
  }
  class Home$19
  implements DialogInterface.OnKeyListener
  {
	  public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
	  {
		boolean flag;
		if (paramInt == 84)
			flag = true;
		else
			flag = false;
		return flag;
	  }
	}
  
  class Home$1  implements Runnable
{
	  private boolean val$Enable;
	  public Home$1( boolean Enable)
	  {
		  this.val$Enable = Enable;
	  }
  public void run()
  {
    view.enableS3D(val$Enable);
  }
}
  
  public Home()
  {
	  Log.e("hxj", "test11");
	Home$2 local2 = new Home$2();
	  
    DecoratedBroadcastReceiver localDecoratedBroadcastReceiver1 = new DecoratedBroadcastReceiver("com.spb.shell3d.action.pifpaf", local2);
    this.killReceiver = localDecoratedBroadcastReceiver1;
    DecoratedBroadcastReceiver localDecoratedBroadcastReceiver2 = new DecoratedBroadcastReceiver();
    this.externalAppReceiver = localDecoratedBroadcastReceiver2;
    Home$3 local3 = new Home$3();
    this.uninstallReceiver = local3;
    this.skip = false;
    HashSet localHashSet1 = new HashSet();
    this.pauseResumeListeners = localHashSet1;
    this.isResumed = false;
    this.wasPaused = true;
    HashMap localHashMap1 = CollectionFactory.newHashMap();
    this.id2IconUri = localHashMap1;
    HashMap localHashMap2 = CollectionFactory.newHashMap();
    this.id2Label = localHashMap2;
    HashSet localHashSet2 = CollectionFactory.newHashSet();
    this.restoredWidgets = localHashSet2;
    this.onCreateCounter = 0;
    this.onDestroyCounter = 0;
    this.onResumeCounter = 0;
    this.onPauseCounter = 0;
    this.onStartCounter = 0;
    this.onRestartCounter = 0;
    this.onStopCounter = 0;
    HashSet localHashSet3 = CollectionFactory.newHashSet();
    this.preInited = localHashSet3;
    Home$20 local20 = new Home$20();
    this.licenseCheckFailedReceiver = local20;
  }

  private void addIconUri(int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "android.resource";
    String str1 = paramAppWidgetProviderInfo.provider.getPackageName();
    arrayOfObject[1] = str1;
    Integer localInteger1 = Integer.valueOf(paramAppWidgetProviderInfo.icon);
    arrayOfObject[2] = localInteger1;
    String str2 = String.format("%s://%s/%d", arrayOfObject);
    Map localMap = this.id2IconUri;
    Integer localInteger2 = Integer.valueOf(paramInt);
    Uri localUri = Uri.parse(str2);
    Object localObject = localMap.put(localInteger2, localUri);
  }

  public static void closeCursorsSilently(Cursor[] paramArrayOfCursor)
  {
    Cursor[] arrayOfCursor = paramArrayOfCursor;
    int i = arrayOfCursor.length;
    int j = 0;
 
	  while (j < i)
	  {
	    Cursor localCursor = arrayOfCursor[j];
	  
	      localCursor.close();
	      j += 1;
	  
	  }
  }
	private int getResizeMode(AppWidgetProviderInfo appwidgetproviderinfo)
	{
		int i;
		if (android.os.Build.VERSION.SDK_INT >= 12)
//			i = appwidgetproviderinfo.resizeMode;
			i = 1;
		else
			i = 0;
		return i;
	}
   private void initWeatherProvider()
  {
    new Home$9().start();
  }

  private void initiateYandexStartupProtocol()
  {
    logger.d("initiateYandexStartupProtocol >>>");
//    try
//    {
//      PackageManager localPackageManager = getPackageManager();
//      String str1 = getPackageName();
//      String str2 = Integer.toString((int)(Float.parseFloat(localPackageManager.getPackageInfo(str1, 0).versionName) * 100.0F));
//      Logger localLogger1 = logger;
//      String str3 = "initiateYandexStartupProtocol: using version=" + str2;
//      localLogger1.d(str3);
////      ClidManager localClidManager = ClidManager.getInstance(this);
////      int i = com.spb.shell3d.R.string.startup_clid;
//      String str4 = getString(com.spb.shell3d.R.string.startup_clid);
//      localClidManager.setClid("com.spb.shell3d", str4);
//      String str5 = localClidManager.getClidBlocking("com.spb.shell3d", str4);
//      Logger localLogger2 = logger;
//      String str6 = "initiateYandexStartupProtocol: using clid=" + str5 + " (clid from resources: " + str4;
//      localLogger2.d(str6);
//      Context localContext = getApplicationContext();
//      
////      Home localHome = this;
////      new Home$6(localHome, localContext, "http://mobile.shell.heroism.yandex.ru/shell/startup", str2, str5).initiateStartupProtocol();
//      Home$6 local6 =(Home$6) new StartupUUIDClient(localContext, "http://mobile.shell.heroism.yandex.ru/shell/startup", str2, str5);
//      local6.initiateStartupProtocol();
//      logger.d("initiateYandexStartupProtocol <<<");
//      return;
//    }
//    catch (Exception localException)
//    {
//      while (true)
//      {
//        String str2 = "000";
//        Logger localLogger3 = logger;
//        String str7 = "initiateYandexStartupProtocol: failed to read version from package info, using version=" + str2;
//        localLogger3.w(str7);
//      }
//    }
  }

  public static native boolean isLiteVersion();

	private void logLifecycle()
	{
		String s;
		Exception exception = new Exception();
		exception.fillInStackTrace();
		s = exception.getStackTrace()[1].getMethodName();
		if (!"onCreate".equals(s))
		{
			if ("onDestroy".equals(s))
				onDestroyCounter = 1 + onDestroyCounter;
			else
			if ("onResume".equals(s))
				onResumeCounter = 1 + onResumeCounter;
			else
			if ("onPause".equals(s))
				onPauseCounter = 1 + onPauseCounter;
			else
			if ("onStart".equals(s))
				onStartCounter = 1 + onStartCounter;
			else
			if ("onStop".equals(s))
				onStopCounter = 1 + onStopCounter;
			else
			if ("onRestart".equals(s))
				onRestartCounter = 1 + onRestartCounter;
		}
		else
		{
			onCreateCounter = 1 + onCreateCounter;
		}	
		
		String s1 = (new StringBuilder()).append(">>>Home.").append(s).append(": onCreate=").append(onCreateCounter).append(" onStart=").append(onStartCounter).append(" onRestart=").append(onRestartCounter).append(" onResume=").append(onResumeCounter).append(" onPause=").append(onPauseCounter).append(" onStop=").append(onStopCounter).append(" onDestroy=").append(onDestroyCounter).append(" this=").append(this).toString();
		logger.d(s1);
		return;		
	}
  public static String makeLicenseCheckFailedAction(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str = paramContext.getPackageName();
    return str + ".ACTION_LICENSE_CHECK_FAILED";
  }

  public static native void mouseEvent(int paramInt1, int paramInt2, int paramInt3);

  private void onInstallWidget(Intent paramIntent, boolean paramBoolean)
  {
    int i = paramIntent.getExtras().getInt("appWidgetId", -1);
    AppWidgetProviderInfo localAppWidgetProviderInfo = AppWidgetManager.getInstance(this).getAppWidgetInfo(i);
    ShellAppWidgetHost localShellAppWidgetHost = this.widgetHost;
    Home localHome1 = this;
    AppWidgetHostView localAppWidgetHostView1 = localShellAppWidgetHost.createView(localHome1, i, localAppWidgetProviderInfo);
    WidgetController2 localWidgetController21 = this.widgetController;
    AppWidgetHostView localAppWidgetHostView2 = localAppWidgetHostView1;
    WidgetController2.LayoutParams localLayoutParams = localWidgetController21.makeLayoutParams(localAppWidgetHostView2, localAppWidgetProviderInfo, i);
    if (localLayoutParams == null)
    {
      int j = com.spb.shell3d.R.string.home_widget_error;
      Toast.makeText(this, j, 1).show();
    }
  
      try
      {
        BaseWidgetInfo localBaseWidgetInfo = BaseWidgetInfo.createInstance(i, localAppWidgetProviderInfo);
        ContentResolver localContentResolver = getContentResolver();
        Home localHome2 = this;
        Uri localUri = localBaseWidgetInfo.getUri(localHome2);
        ContentValues localContentValues = localBaseWidgetInfo.toContentValues();
        String str1 = localContentResolver.insert(localUri, localContentValues).getLastPathSegment();
        int k = new Integer(str1).intValue();
        localLayoutParams.setId(k);
        addIconUri(k, localAppWidgetProviderInfo);
        Map localMap = this.id2Label;
        Integer localInteger = Integer.valueOf(k);
        String str2 = localAppWidgetProviderInfo.label;
        Object localObject = localMap.put(localInteger, str2);
        WidgetController2 localWidgetController22 = this.widgetController;
        AppWidgetHostView localAppWidgetHostView3 = localAppWidgetHostView1;
        localWidgetController22.addWidget(localAppWidgetHostView3, localLayoutParams);
        this.widgetController.requestLayout();
        int m = getResizeMode(localAppWidgetProviderInfo);
        ComponentName localComponentName = localAppWidgetProviderInfo.provider;
        int n = this.widgetToken;
        int i1 = Math.max(localAppWidgetProviderInfo.minWidth, 1);
        int i2 = Math.max(localAppWidgetProviderInfo.minHeight, 1);
        String str3 = localComponentName.getPackageName();
        String str4 = localComponentName.getClassName();
        boolean bool = paramBoolean;
        NativeCalls.AddWidget(n, bool, k, i1, i2, str3, str4, m);
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
  }

  static native void onKeyDown(int paramInt);

  static native void onKeyUp(int paramInt);

  private void onPickActivity(Intent intent)
	{
		int i = intent.getExtras().getInt("appWidgetId", -1);
		AppWidgetProviderInfo appwidgetproviderinfo = appWidgetManager.getAppWidgetInfo(i);
		if (appwidgetproviderinfo != null)
			if (appwidgetproviderinfo.configure != null)
			{
				Intent intent1 = new Intent("android.appwidget.action.APPWIDGET_CONFIGURE");
				intent1.setComponent(appwidgetproviderinfo.configure);
				intent1.putExtra("appWidgetId", i);
				startActivityForResult(intent1, RequestCode.REQUEST_INSTALL_APPWIDGET);
			} else
			{
				onActivityResult(RequestCode.REQUEST_INSTALL_APPWIDGET, -1, intent);
			}
	}
  private void processShortcut(Intent paramIntent)
  {
    int i = RequestCode.REQUEST_INSTALL_SHORTCUT;
    startActivityForResult(paramIntent, i);
  }

  private void reflectSetPersistent(boolean paramBoolean)
  {
    try
    {
      Method[] arrayOfMethod = getClass().getMethods();
      int i = arrayOfMethod.length;
      int j = 0;
      while (true)
      {
        if (j < i)
        {
          Method localMethod = arrayOfMethod[j];
          if (localMethod.getName().equals("setPersistent"))
          {
            Object[] arrayOfObject = new Object[1];
            Boolean localBoolean = Boolean.valueOf(paramBoolean);
            arrayOfObject[0] = localBoolean;
            Object localObject = localMethod.invoke(this, arrayOfObject);
          }
        }
        else
        {
          return;
        }
        j += 1;
      }
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  private void registerLicenseReceiver()
  {
    System.out.println("Home.registerLicenseReceiver");
    String str1 = makeLicenseCheckFailedAction(this);
    IntentFilter localIntentFilter = new IntentFilter(str1);
    BroadcastReceiver localBroadcastReceiver = this.licenseCheckFailedReceiver;
    StringBuilder localStringBuilder = new StringBuilder();
    String str2 = getPackageName();
    String str3 = str2 + ".spb.permission";
    Handler localHandler = new Handler();
    Intent localIntent = registerReceiver(localBroadcastReceiver, localIntentFilter, str3, localHandler);
  }

  private void removeWidget(int paramInt1, int paramInt2)
  {
    Uri localUri1 = WidgetsContract.BaseWidgetInfoContract.getContentUri(this);
    long l = paramInt1;
    Uri localUri2 = ContentUris.withAppendedId(localUri1, l);
    int i = getContentResolver().delete(localUri2, null, null);
    this.widgetHost.deleteAppWidgetId(paramInt2);
    Map localMap1 = this.id2Label;
    Integer localInteger1 = Integer.valueOf(paramInt1);
    Object localObject1 = localMap1.remove(localInteger1);
    Map localMap2 = this.id2IconUri;
    Integer localInteger2 = Integer.valueOf(paramInt1);
    Object localObject2 = localMap2.remove(localInteger2);
    NativeCalls.DeleteWidget(this.widgetToken, paramInt1);
  }

  private void resume()
  {
    Iterator localIterator = this.pauseResumeListeners.iterator();
    while (localIterator.hasNext())
      ((PauseResumeListener)localIterator.next()).onResume();
    this.view.onResume();
    this.widgetHost.startListening();
    DateChangedObserver.getInstance().resume(this);
    this.nc.SoundProfileNotifyChange();
    this.nc.checkWallpaperChange();
    this.wasPaused = false;
    this.isResumed = false;
    logger.d("<<<Home.onResume");
  }

  public static void sendMouseEvent(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((!application.blockTouches()) && (paramInt1 < 3))
      mouseEvent(paramInt1, paramInt2, paramInt3);
  }

  public static native void setNativeFlags(int paramInt);

  private void showAddAppWidgetActivity()
  {
    int i = this.widgetHost.allocateAppWidgetId();
    Intent localIntent1 = new Intent("android.appwidget.action.APPWIDGET_PICK");
    Intent localIntent2 = localIntent1.putExtra("appWidgetId", i);
    int j = R.string.dialog_choose_widget;
    CharSequence localCharSequence = getText(j);
    Intent localIntent3 = localIntent1.putExtra("android.intent.extra.TITLE", localCharSequence);
    ArrayList localArrayList1 = new ArrayList();
    Intent localIntent4 = localIntent1.putParcelableArrayListExtra("customInfo", localArrayList1);
    ArrayList localArrayList2 = new ArrayList();
    Intent localIntent5 = localIntent1.putParcelableArrayListExtra("customExtras", localArrayList2);
    int k = RequestCode.REQUEST_PICK_APPWIDGET;
    startActivityForResult(localIntent1, k);
  }

  private void showAddShortcutDialog()
  {
    Intent localIntent = new Intent("android.intent.action.CREATE_SHORTCUT");
    FilterPickDialogBuilder localFilterPickDialogBuilder1 = new FilterPickDialogBuilder(this, localIntent);
    int i = R.string.dialog_select_shortcut;
    FilterPickDialogBuilder localFilterPickDialogBuilder2 = localFilterPickDialogBuilder1.setTitle(i);
    Home$12 local12 = new Home$12();
    FilterPickDialogBuilder localFilterPickDialogBuilder3 = localFilterPickDialogBuilder1.setDialogResult(local12);
    localFilterPickDialogBuilder1.create().show();
  }

  private void startRequestForResult(Intent paramIntent, int paramInt)
  {
    Home$10 local10 = new Home$10(paramIntent, paramInt);
    runOnUiThread(local10);
  }

  private void unregisterLicenseReceiver()
  {
    BroadcastReceiver localBroadcastReceiver = this.licenseCheckFailedReceiver;
    unregisterReceiver(localBroadcastReceiver);
  }

  public void closeLoadingDialog()
  {
    boolean bool = getPreferences(0).edit().putBoolean("load_success", true).commit();
    if (this.progressDialog != null)
      this.progressDialog.dismiss();
  }

  public void enableHTCStereo3D(boolean paramBoolean)
  {
    Home$1 local1 = new Home$1(paramBoolean);
    runOnUiThread(local1);
  }

  public Uri getIconUri(int paramInt)
  {
    Map localMap = this.id2IconUri;
    Integer localInteger = Integer.valueOf(paramInt);
    return (Uri)localMap.get(localInteger);
  }

  public View getMenuAnchorView()
  {
    return this.menuAnchorView;
  }

  public MenuController getMenuController()
  {
    return this.menuController;
  }

  public NativeCallbacks getNativeCallbacks()
  {
    return this.nc;
  }

  public MyGlSurfaceView getSurfaceView()
  {
    return this.view;
  }

  public String getWidgetName(int paramInt)
  {
    Map localMap = this.id2Label;
    Integer localInteger = Integer.valueOf(paramInt);
    return (String)localMap.get(localInteger);
  }

  public void hideAllWidgets()
  {
    this.widgetController.setVisibility(8);
  }

  public boolean isHTCStereo3DEnabled()
  {
    return this.view.isEnableS3D();
  }

  public int launcherWidgetInit(String paramString1, String paramString2)
  {
    ComponentName localComponentName = new ComponentName(paramString1, paramString2);
    int i = this.widgetHost.allocateAppWidgetId();
    this.appWidgetManager.bindAppWidgetId(i, localComponentName);
    AppWidgetProviderInfo localAppWidgetProviderInfo = this.appWidgetManager.getAppWidgetInfo(i);
    BaseWidgetInfo localBaseWidgetInfo = BaseWidgetInfo.createInstance(i, localAppWidgetProviderInfo);
    ContentResolver localContentResolver = getContentResolver();
    Uri localUri = localBaseWidgetInfo.getUri(this);
    ContentValues localContentValues = localBaseWidgetInfo.toContentValues();
    String str = localContentResolver.insert(localUri, localContentValues).getLastPathSegment();
    int j = new Integer(str).intValue();
    localBaseWidgetInfo.setId(j);
    boolean bool = this.preInited.add(localBaseWidgetInfo);
    return j;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Logger localLogger = logger;
    StringBuilder localStringBuilder = new StringBuilder().append("onActivityResult: requestCode=").append(paramInt1).append(" result=");
    if (paramInt2 == -1);
    for (String str1 = "OK"; ; str1 = "NOT_OK(" + paramInt2 + ")")
    {
      String str2 = str1;
      localLogger.d(str2);
      if (paramInt2 == -1)
        break;
      return;
    }
    int i = RequestCode.REQUEST_PICK_APPWIDGET;
    if (paramInt1 == i)
      onPickActivity(paramIntent);
    while (true)
    {
      int j = RequestCode.REQUEST_INSTALL_SHORTCUT;
      if (paramInt1 == j)
        this.nc.getShortcutAdapter().addShortcut(paramIntent, true);      
      int k = RequestCode.REQUEST_PICK_SHORTCUT;
      if (paramInt1 == k)
      {	  
    	  processShortcut(paramIntent);
    	  return;
      }
      int m = RequestCode.REQUEST_INSTALL_APPWIDGET;
      if (paramInt1 == m)
      {
        onInstallWidget(paramIntent, true);
        continue;
      }
      int n = RequestCode.REQUEST_SELECT_CITY_FOR_WEATHER;
      if (paramInt1 == n)
      {
        ((WeatherAdapterAndroid)this.nc.getWeatherAdapter()).onCitySelectResult(-1, paramIntent);
        continue;
      }
      int i1 = RequestCode.REQUEST_SELECT_CITY_FOR_CITIES_ADAPTER;
      if (paramInt1 == i1)
      {
        ((CitiesAdapterAndroid)this.nc.getCitiesAdapter()).onCitySelectResult(paramInt2, paramIntent);
        continue;
      }
      int i2 = RequestCode.REQUEST_PICK_CONTACT;
      if (paramInt1 == i2)
      {
        ((ContactsAdapterAndroid)this.nc.getContactsAdapter()).onPickContactResult(false, -1, paramIntent);
        continue;
      }
      int i3 = RequestCode.REQUEST_PICK_FAVCONTACT;
      if (paramInt1 != i3)
        continue;
      ((ContactsAdapterAndroid)this.nc.getContactsAdapter()).onPickContactResult(true, -1, paramIntent);
    }
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    logger.d(">>>Home.onConfigurationChanged");
    super.onConfigurationChanged(paramConfiguration);
    if (this.menuController != null)
      this.menuController.onRelayout();
    if (this.currentLocale != null)
    {
      Locale localLocale1 = this.currentLocale;
      Locale localLocale2 = paramConfiguration.locale;
      if (!localLocale1.equals(localLocale2))
        restartShell();
    }
    if (this.widgetController != null)
      this.widgetController.recreateWidgets();
    logger.d("<<<Home.onConfigurationChanged");
  }

	public void onCreate(Bundle bundle)
	{	Log.e("hxj", "test");
		super.onCreate(bundle);
		
		boolean flag;
		application = (ShellApplication)getApplication();
		flag = getPreferences(0).getBoolean("load_success", true);
		getPreferences(0).edit().putBoolean("load_success", false).commit();
		
		if (flag)
		{
			Log.i("Spb Shell 3D", "The previous launch of SPB Shell 3D was successful");	
		}
		else
		{
			PackageManager packagemanager;
			Intent intent1;
			Log.e("Spb Shell 3D", "The previous launch of SPB Shell 3D was unsuccessful");
			packagemanager = getPackageManager();
			intent1 = new Intent("android.intent.action.MAIN");
			intent1.addCategory("android.intent.category.HOME");
			if (packagemanager.queryIntentActivities(intent1, 0).size() >1)
			{
				super.onCreate(bundle);
				skip = true;
				showDialog(12);
			}
			else
			{
				String s = getApplicationInfo().sourceDir;
				boolean flag1;
				if (s.startsWith("/sd-ext/") || s.startsWith("/mnt/"))
					flag1 = true;
				else
					flag1 = false;
				if (flag1)
				{
					Log.e("Spb Shell 3D", (new StringBuilder()).append("SPB Shell 3D installed to storage card: ").append(s).toString());
					super.onCreate(bundle);
					skip = true;
					showDialog(14);
					return;
				}
				PACKAGE_NAME = getPackageName();
				logLifecycle();
				Intent intent = new Intent("com.spb.shell3d.FOREGROUND");
				intent.setClass(this, ForegroundService.class);
				reflectSetPersistent(true);
				if (android.os.Build.VERSION.SDK_INT > 8)
					startService(intent);
				initiateYandexStartupProtocol();
				SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
				System.loadLibrary("shell3d");
				useTestMenu = sharedpreferences.getBoolean("use_test_menu", false);
				logger.d((new StringBuilder()).append("useLiveWallpaper = ").append(useLiveWallpaper).toString());
				int i;
				if (WallpaperAdapter.useLiveWallpapers(this) && WallpaperAdapter.isLiveWallpaper(this))
//					if (android.os.Build.VERSION.SDK_INT >= 11)
//						setTheme(com.spb.shell3d.R.style.Theme_Wallpaper_NoTitleBar_NoBackground);
//					else
						setTheme(0x103005f);
				super.onCreate(bundle);
				wallpaperManager = WallpaperManager.getInstance(this);
				if (useTestMenu)
					i = 1;
				else
					i = 0;
				setNativeFlags(i);
				movementController = new MovementController();
				currentLocale = getResources().getConfiguration().locale;
				widgetHost = new ShellAppWidgetHost(this, 2010);
				widgetHost.setMovementController(movementController);
				widgetHost.startListening();
				appWidgetManager = AppWidgetManager.getInstance(this);
				menuController = new MenuController(this);
				logger.d("Sleep Start threadProc");
				setContentView(com.spb.shell3d.R.layout.main);
				widgetController = (WidgetController2)findViewById(com.spb.shell3d.R.id.widg);
				if (widgetController == null)
					throw new Error();
				widgetController.setWidgetHost(widgetHost, this);
				widgetController.setOnKeyListener(new android.view.View.OnKeyListener() {

//					final Home this$0;

					public boolean onKey(View view2, int k, KeyEvent keyevent)
					{
						
						Home.logger.e("will be dispacthed");
						return false;
					}

					
//					{
////						super();
////						this$0 = Home.this;
//					}
				});
				view = (MyGlSurfaceView)findViewById(com.spb.shell3d.R.id.gl_surface_view);
				if (view == null)
				{
					int j = 0;
		label0:
					do
					{
		label1:
						{
							if (j < widgetController.getChildCount())
							{
								View view1 = widgetController.getChildAt(j);
								if (!(view1 instanceof MyGlSurfaceView))
									break label1;
								view = (MyGlSurfaceView)view1;
							}
							if (view == null)
								throw new Error("Don't have gl view");
							break label0;
						}
						j++;
					} while (true);
				}
				menuAnchorView = new View(this);
				widgetController.addView(menuAnchorView);
				if (!Build.DEVICE.equals("p920"));
				if (WallpaperAdapter.useLiveWallpapers(this) && WallpaperAdapter.isLiveWallpaper(this) || Build.DEVICE.equals("GT-I9100"))
					view.getHolder().setFormat(1);
				nc = new NativeCallbacks(this, widgetController, view);
				if (nc.getScreenMode())
				{
					logger.d("screen format was set to 8888");
					view.getHolder().setFormat(1);
				}
				TouchListener touchlistener = new TouchListener();
				widgetController.setMainView(view, movementController);
				view.setOnTouchListener(touchlistener);
				DateChangedObserver.getInstance().start(this);
				externalAppReceiver.addActionListener("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

//					final Home this$0;

					public void onAction(Context context, Intent intent2)
					{
						String as[] = intent2.getStringArrayExtra("android.intent.extra.changed_package_list");
						int k = as.length;
						for (int l = 0; l < k; l++)
						{
							String s1 = as[l];
							widgetsDelete(s1);
						}

					}

					
//					{
//						this$0 = Home.this;
//						super();
//					}
				});
				registerReceiver(killReceiver, killReceiver.getIntentFilter());
				registerReceiver(externalAppReceiver, externalAppReceiver.getIntentFilter());
				registerLicenseReceiver();
//				restoreWidgets();
//				restoreWidgetsInController();
				nc.Start();
//				initWeatherProvider();
				menuController.onCreate();
				NativeCallbacks.printMemory();
				nc.onHomeLoadStarted();
				logger.d("<<<Home.onCreate");
			}
		}
		return;
	}
  protected Dialog onCreateDialog(int paramInt)
  {
    PrintStream localPrintStream = System.out;
    String str = "Home.onCreateDialog: " + paramInt;
    localPrintStream.println(str);
    Object localObject;
    switch (paramInt)
    {   
    case 12:
    	boolean bool = getPreferences(0).edit().putBoolean("load_success", true).commit();
        AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this).setIcon(17301543);
        int i = R.string.safe_mode_title;
        AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(i);
        int j = R.string.safe_mode_text;
        AlertDialog.Builder localBuilder3 = localBuilder2.setMessage(j).setCancelable(true);
        int k = R.string.safe_mode_retry;
        Home$15 local15 = new Home$15();
        AlertDialog.Builder localBuilder4 = localBuilder3.setPositiveButton(k, local15);
        int m = R.string.safe_mode_home;
        Home$14 local14 = new Home$14();
        localObject = localBuilder4.setNegativeButton(m, local14).create();
        break;
    case 13:
    	 AlertDialog.Builder localBuilder5 = new AlertDialog.Builder(this).setIcon(17301543);
         int n = R.string.sd_install_title;
         AlertDialog.Builder localBuilder6 = localBuilder5.setTitle(n);
         int i1 = R.string.sd_install_text;
         AlertDialog.Builder localBuilder7 = localBuilder6.setMessage(i1);
         Home$16 local16 = new Home$16();
         localObject = localBuilder7.setPositiveButton(17039370, local16).create();
         break;
    case 14:
    	Intent localIntent1 = new Intent("android.intent.action.MAIN");
        Intent localIntent2 = localIntent1.addCategory("android.intent.category.HOME");
        FilterPickDialogBuilder localFilterPickDialogBuilder1 = new FilterPickDialogBuilder(this, localIntent1);
        Home$17 local17 = new Home$17();
        FilterPickDialogBuilder localFilterPickDialogBuilder2 = localFilterPickDialogBuilder1.setFilter(local17);
        Home$18 local18 = new Home$18();
        FilterPickDialogBuilder localFilterPickDialogBuilder3 = localFilterPickDialogBuilder1.setDialogResult(local18);
        int i2 = R.string.safe_mode_home;
        FilterPickDialogBuilder localFilterPickDialogBuilder4 = localFilterPickDialogBuilder1.setTitle(i2);
        localObject = localFilterPickDialogBuilder1.create();
        ((Dialog)localObject).setCancelable(false);
        Home$19 local19 = new Home$19();
        ((Dialog)localObject).setOnKeyListener(local19);
        break;
    default:
        localObject = super.onCreateDialog(paramInt);
    }   
    return (Dialog)localObject;
    
  }
  protected void onDestroy()
	{
		if (menuController != null)
			menuController.onDestroy();
		logger.d(">>>Home.onDestroy");
		logLifecycle();
		super.onDestroy();
		if (!skip)
		{
//			((WeatherAdapterAndroid)nc.getWeatherAdapter()).homeOnDestroy();
//			((CitiesAdapterAndroid)nc.getCitiesAdapter()).homeOnDestroy();
			view.surfaceDestroyed(null);
			DateChangedObserver.getInstance().stop(this);
			unregisterReceiver(killReceiver);
			unregisterReceiver(uninstallReceiver);
			unregisterReceiver(externalAppReceiver);
			unregisterLicenseReceiver();
			stopService(new Intent(this, ForegroundService.class));
			nc.Stop();
			widgetHost.stopListening();
			logger.d("<<<Home.onDestroy");
		}
	}
	public boolean onKeyDown(int i, KeyEvent keyevent)
	{
		boolean flag;
		if (skip)
			flag = false;
		else
		if (i == 23 || i == 4)
		{
			onKeyDown(i);
			flag = true;
		} else
		{
			flag = super.onKeyDown(i, keyevent);
		}
		return flag;
	}
	public boolean onKeyUp(int i, KeyEvent keyevent)
	{
		boolean flag = false;
		if (!skip)
			if (i == 84 && keyevent.isTracking() && !keyevent.isCanceled())
			{
				onSearchRequested();
				flag = true;
			} else
			if (i == 24 || i == 25)
				nc.SoundProfileNotifyChange();
			else
			if (i == 23 || i == 4)
			{
				onKeyUp(i);
				flag = true;
			} else
			{
				flag = super.onKeyUp(i, keyevent);
			}
		return flag;
	}
  public void onLowMemory()
  {
    System.out.println("Home.onLowMemory");
  }

  public boolean onMenuOpened(int paramInt, Menu paramMenu)
  {
    return this.menuController.onMenuOpened(paramInt, paramMenu);
  }

  protected void onNewIntent(Intent intent)
	{
	  if (!skip)
	  {
		  super.onNewIntent(intent);
			if ("android.intent.action.MAIN".equals(intent.getAction()))
			{
				if (nc != null)
				{
					DialogBoxAdapter dialogboxadapter = nc.getDialogBoxAdapter();
					NewDialogAdapter newdialogadapter = nc.getNewDialogAdapter();
					if (dialogboxadapter != null)
						dialogboxadapter.closeAllDialogs();
					if (newdialogadapter != null)
						newdialogadapter.closeAllDialogs();
				}
				int i = intent.getFlags();
				logger.d((new StringBuilder()).append("Home.onNewIntent: flags=0x").append(Integer.toHexString(i)).toString());
				for (int j = 1; j != 0; j <<= 1)
					if ((i & j) != 0)
						logger.d((new StringBuilder()).append("Home.onNewIntent: flag is set: 0x").append(Integer.toHexString(j)).toString());

				boolean flag;
				if (hasWindowFocus() && (0x400000 & intent.getFlags()) != 0x400000)
					flag = true;
				else
					flag = false;
				if (flag && !DebugUtil.isMonkey())
				{
					logger.d("Home.onNewIntent: onKeyDown >>>");
					onKeyDown(3);
					logger.d("Home.onNewIntent: onKeyDown <<<");
					logger.d("Home.onNewIntent: onKeyUp >>>");
					onKeyUp(3);
					logger.d("Home.onNewIntent: onKeyUp <<<");
				}
			} 
	  }
	
		return;
	}

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    return this.menuController.onOptionsItemSelected(paramMenuItem);
  }

  public void onOptionsMenuClosed(Menu paramMenu)
  {
    super.onOptionsMenuClosed(paramMenu);
    this.menuController.onOptionsMenuClosed(paramMenu);
  }

  protected void onPause()
	{
		logLifecycle();
		super.onPause();
		if (!skip)
		{
			view.onPause();
			for (Iterator iterator = pauseResumeListeners.iterator(); iterator.hasNext(); ((PauseResumeListener)iterator.next()).onPause());
			movementController.freeAll();
			widgetHost.stopListening();
			DateChangedObserver.getInstance().pause(this);
			wasPaused = true;
			logger.d("<<<Home.onPause");
		}
	}
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		boolean flag = super.onPrepareOptionsMenu(menu);
		if (menuController != null)
			flag = menuController.prepareOptionsMenu(menu);
		return flag;
	}

  protected void onRestart()
  {
    logger.d("Home.onRestart");
    logLifecycle();
    super.onRestart();
  }

	protected void onResume()
	{
		logLifecycle();
		super.onResume();
		if (!skip)
		{
			isResumed = true;
			if (hasWindowFocus())
				resume();
			((ShellApplication)getApplication()).enableLicenseComponents();
		}
	}
  public boolean onSearchRequested()
  {
  	boolean flag;
    try
    {
    	NativeCalls.handleSearchRequest();
    	flag = true;
    }
    catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
    {
    	Log.e("Spb Shell 3D", "onSearchRequested failed: ", localUnsatisfiedLinkError);
    	flag = false;
    }
    return flag;
  }
  protected void onStart()
	{
		logLifecycle();
		super.onStart();
		if (!skip)
		{
//			((WeatherAdapterAndroid)nc.getWeatherAdapter()).homeOnStart();
//			((CitiesAdapterAndroid)nc.getCitiesAdapter()).homeOnStart();
			widgetHost.startListening();
			widgetController.start();
			logger.d("<<<Home.onStart");
		}
	}
	protected void onStop()
	{
		logLifecycle();
		super.onStop();
		if (!skip)
		{
			widgetController.stop();
			logger.d("<<<Home.onStop");
		}
	}
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    if ((paramBoolean) && (this.wasPaused) && (this.isResumed))
      resume();
  }

  public void openClassicOptionsMenu()
  {
    super.openOptionsMenu();
  }

  public void openOptionsMenu()
  {
    this.menuController.openCompatibleMenu(-1, -1);
  }

  public void removeOnPauseResumeListener(PauseResumeListener paramPauseResumeListener)
  {
    Object localObject = Conditions.checkNotNull(paramPauseResumeListener);
    Home$8 local8 = new Home$8(paramPauseResumeListener);
    runOnUiThread(local8);
  }

  public void restartShell()
  {
    Intent localIntent1 = new Intent(this, RestartActivity.class);
    int i = Process.myPid();
    Intent localIntent2 = localIntent1.putExtra("pid", i);
    startActivity(localIntent1);
    finish();
  }

  public void restoreWidgets()
  {
    logger.i("restoreWidgets >>>");
    this.restoredWidgets.clear();
    while (true)
    {
      Cursor localCursor = null;
      try
      {
        DataBuilder localDataBuilder = DataBuilder.createBuilder(BaseWidgetInfo.class);
        ContentResolver localContentResolver1 = getContentResolver();
        Uri localUri1 = WidgetsContract.BaseWidgetInfoContract.getContentUri(this);
        localCursor = localContentResolver1.query(localUri1, null, null, null, "_id ASC");
        if (localCursor == null)
        { 
        	logger.e("cursor base null");
        	Cursor[] arrayOfCursor1;
        	return;
        }
        int i = -1;
        boolean bool1 = localCursor.moveToPosition(i);
        AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(this);
        if (localCursor.moveToNext())
        {
          CursorDataAdapter localCursorDataAdapter = new CursorDataAdapter(localCursor);
          BaseWidgetInfo localBaseWidgetInfo = (BaseWidgetInfo)localDataBuilder.newInstance(localCursorDataAdapter);
          int j = localBaseWidgetInfo.getAppWidgetId();
          AppWidgetProviderInfo localAppWidgetProviderInfo = localAppWidgetManager.getAppWidgetInfo(j);
          if (localAppWidgetProviderInfo != null)
            continue;
          ContentResolver localContentResolver2 = getContentResolver();
          Uri localUri2 = localBaseWidgetInfo.getUri(this);
          int k = localContentResolver2.delete(localUri2, null, null);
          continue;
        }
      }
      catch (Exception localException)
      {
        BaseWidgetInfo localBaseWidgetInfo;
        AppWidgetProviderInfo localAppWidgetProviderInfo;
        localException.printStackTrace();
        Logger localLogger = logger;
        String str = "restoreAllWidget failed with error: " + localException;
        localLogger.e(str, localException);
//        Cursor[] arrayOfCursor2 = new Cursor[1];
//        arrayOfCursor2[0] = localCursor;
//        closeCursorsSilently(arrayOfCursor2);
//        continue;
//        int m = Math.max(localAppWidgetProviderInfo.minWidth, 1);
//        localBaseWidgetInfo.setMinWidth(m);
//        int n = Math.max(localAppWidgetProviderInfo.minHeight, 1);
//        localBaseWidgetInfo.setMinHeight(n);
//        boolean bool2 = this.restoredWidgets.add(localBaseWidgetInfo);
//        continue;
      }
      finally
      {
        Cursor[] arrayOfCursor3 = new Cursor[1];
        arrayOfCursor3[0] = localCursor;
        closeCursorsSilently(arrayOfCursor3);
      }
      Cursor[] arrayOfCursor4 = new Cursor[1];
      arrayOfCursor4[0] = localCursor;
      closeCursorsSilently(arrayOfCursor4);
    }
  }

  public void restoreWidgetsInController()
  {
    Iterator localIterator = this.restoredWidgets.iterator();
    while (localIterator.hasNext())
    {
      BaseWidgetInfo localBaseWidgetInfo = (BaseWidgetInfo)localIterator.next();
      AppWidgetManager localAppWidgetManager = this.appWidgetManager;
      int i = localBaseWidgetInfo.getAppWidgetId();
      AppWidgetProviderInfo localAppWidgetProviderInfo = localAppWidgetManager.getAppWidgetInfo(i);
      ShellAppWidgetHost localShellAppWidgetHost = this.widgetHost;
      int j = localBaseWidgetInfo.getAppWidgetId();
      AppWidgetHostView localAppWidgetHostView = localShellAppWidgetHost.createView(this, j, localAppWidgetProviderInfo);
      Map localMap = this.id2Label;
      Integer localInteger = Integer.valueOf(localBaseWidgetInfo.getId());
      String str = localAppWidgetProviderInfo.label;
      Object localObject = localMap.put(localInteger, str);
      int k = localBaseWidgetInfo.getId();
      addIconUri(k, localAppWidgetProviderInfo);
      this.widgetController.restoreWidget(localAppWidgetHostView, localBaseWidgetInfo);
    }
    IntentFilter localIntentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
    localIntentFilter.addDataScheme("package");
    BroadcastReceiver localBroadcastReceiver = this.uninstallReceiver;
    Intent localIntent = registerReceiver(localBroadcastReceiver, localIntentFilter);
  }

  public void restoreWidgetsInNative()
  {
    logger.d("restoreWidgetsInNative");
    AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(this);
    Iterator localIterator = this.preInited.iterator();
    while (localIterator.hasNext())
    {
      BaseWidgetInfo localBaseWidgetInfo1 = (BaseWidgetInfo)localIterator.next();
      int i = localBaseWidgetInfo1.getAppWidgetId();
      AppWidgetProviderInfo localAppWidgetProviderInfo1 = this.appWidgetManager.getAppWidgetInfo(i);
      AppWidgetHostView localAppWidgetHostView = this.widgetHost.createView(this, i, localAppWidgetProviderInfo1);
      ContentResolver localContentResolver = getContentResolver();
      Uri localUri = localBaseWidgetInfo1.getUri(this);
      ContentValues localContentValues = localBaseWidgetInfo1.toContentValues();
      int j = localContentResolver.update(localUri, localContentValues, null, null);
      Map localMap = this.id2Label;
      Integer localInteger = Integer.valueOf(localBaseWidgetInfo1.getId());
      String str1 = localAppWidgetProviderInfo1.label;
      Object localObject = localMap.put(localInteger, str1);
      int k = localBaseWidgetInfo1.getId();
      addIconUri(k, localAppWidgetProviderInfo1);
      boolean bool = this.restoredWidgets.add(localBaseWidgetInfo1);
      Home$11 local11 = new Home$11(localAppWidgetHostView, localBaseWidgetInfo1);
      runOnUiThread(local11);
    }
    this.preInited.clear();
    Logger localLogger1 = logger;
//    StringBuilder localStringBuilder1 = new StringBuilder().append("restoredWidgets: ");
//    int m = this.restoredWidgets.size();
//    String str2 = m;
//    localLogger1.d(str2);
    localIterator = this.restoredWidgets.iterator();
    while (localIterator.hasNext())
    {
      BaseWidgetInfo localBaseWidgetInfo2 = (BaseWidgetInfo)localIterator.next();
      int n = localBaseWidgetInfo2.getAppWidgetId();
      AppWidgetProviderInfo localAppWidgetProviderInfo2 = localAppWidgetManager.getAppWidgetInfo(n);
      if (localAppWidgetProviderInfo2 == null)
        continue;
      ComponentName localComponentName = localAppWidgetProviderInfo2.provider;
      Logger localLogger2 = logger;
//      StringBuilder localStringBuilder2 = new StringBuilder().append("InitWidget ");
//      int i1 = localBaseWidgetInfo2.getId();
//      String str3 = i1;
//      localLogger2.d(str3);
  	  logger.d((new StringBuilder()).append("InitWidget ").append(localBaseWidgetInfo2.getId()).toString());
  	  
      int i2 = this.widgetToken;
      int i3 = localBaseWidgetInfo2.getId();
      int i4 = Math.max(localAppWidgetProviderInfo2.minWidth, 1);
      int i5 = Math.max(localAppWidgetProviderInfo2.minHeight, 1);
      String str4 = localComponentName.getPackageName();
      String str5 = localComponentName.getClassName();
      int i6 = getResizeMode(localAppWidgetProviderInfo2);
      NativeCalls.InitWidget(i2, i3, i4, i5, str4, str5, i6);
    }
  }

  public void sendEventToLiveWallpaper(int paramInt1, int paramInt2)
  {
    WallpaperManager localWallpaperManager = this.wallpaperManager;
    IBinder localIBinder = this.widgetController.getWindowToken();
    int i = paramInt1;
    int j = paramInt2;
    localWallpaperManager.sendWallpaperCommand(localIBinder, "android.home.drop", i, j, 0, null);
  }

  public void setOnPauseResumeListener(PauseResumeListener paramPauseResumeListener)
  {
    Object localObject = Conditions.checkNotNull(paramPauseResumeListener);
    Home$7 local7 = new Home$7(paramPauseResumeListener);
    runOnUiThread(local7);
  }

  public void setWidgetToken(int paramInt)
  {
    this.widgetToken = paramInt;
  }

  public void showAddDialog(int paramInt)
  {
    Home$13 local13 = new Home$13(paramInt);
    runOnUiThread(local13);
  }
  
  public void showLoadingDialog()
	{
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getString(com.spb.shell3d.R.string.dialog_shell_loading));
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(0);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

  public void showWidgets()
  {
    this.widgetController.setVisibility(0);
  }
  public void startPickContact(boolean flag)
	{
		Intent intent = ProgramList.getInstance(this).getIntent("contacts_pick");
		if (intent != null)
		{
			int i;
			if (flag)
				i = RequestCode.REQUEST_PICK_FAVCONTACT;
			else
				i = RequestCode.REQUEST_PICK_CONTACT;
			startRequestForResult(intent, i);
		} else
		{
			logger.w("PICK Contact intent wasn't resolved");
		}
	}

  public void startSearch(String paramString, boolean paramBoolean1, Bundle paramBundle, boolean paramBoolean2)
  {
    SearchManager localSearchManager = (SearchManager)getSystemService("search");
    ComponentName localComponentName = getComponentName();
    String str = paramString;
    boolean bool1 = paramBoolean1;
    Bundle localBundle = paramBundle;
    boolean bool2 = paramBoolean2;
    localSearchManager.startSearch(str, bool1, localComponentName, localBundle, bool2);
  }

  public void startSelectCityForCitiesAdapter(long paramLong)
  {
    Intent localIntent1 = new Intent(this, CitySelectionActivity.class);
    Intent localIntent2 = localIntent1.putExtra("cta_request_id", paramLong);
    int i = RequestCode.REQUEST_SELECT_CITY_FOR_CITIES_ADAPTER;
    startRequestForResult(localIntent1, i);
  }

  public void startSelectCityForWeather(int paramInt1, int paramInt2)
  {
    Intent localIntent1 = new Intent(this, CitySelectionActivity.class);
    Intent localIntent2 = localIntent1.putExtra("weather-provider-token", paramInt2);
    Intent localIntent3 = localIntent1.putExtra("weather-adapter-token", paramInt1);
    int i = RequestCode.REQUEST_SELECT_CITY_FOR_WEATHER;
    startRequestForResult(localIntent1, i);
  }

  public void widgetDelete(int paramInt)
  {
    try
    {
      WidgetController2.LayoutParams localLayoutParams = this.widgetController.removeWidget(paramInt);
      int i = localLayoutParams.getId();
      int j = localLayoutParams.getAppWidgetId();
      removeWidget(i, j);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public void widgetsDelete(String paramString)
  {
    Object localObject = Conditions.checkNotNull(paramString);
    Iterator localIterator = this.widgetController.removeWidgets(paramString).iterator();
    while (localIterator.hasNext())
    {
      WidgetController2.LayoutParams localLayoutParams = (WidgetController2.LayoutParams)localIterator.next();
      int i = localLayoutParams.getId();
      int j = localLayoutParams.getAppWidgetId();
      removeWidget(i, j);
    }
  }
  public static class RequestCode
	{

		public static int REQUEST_INSTALL_APPWIDGET = 2;
		public static int REQUEST_INSTALL_SHORTCUT = 5;
		public static int REQUEST_PICK_APPWIDGET = 1;
		public static int REQUEST_PICK_CONTACT = 4;
		public static int REQUEST_PICK_FAVCONTACT = 8;
		public static int REQUEST_PICK_SHORTCUT = 6;
		public static int REQUEST_SELECT_CITY_FOR_CITIES_ADAPTER = 7;
		public static int REQUEST_SELECT_CITY_FOR_WEATHER = 3;


		private RequestCode()
		{
		}
	}
  final class TouchListener
    implements View.OnTouchListener
  {
    Object lock = "glView";

    private TouchListener()
    {
    }

	public boolean onTouch(View view1, MotionEvent motionevent)
	{
		return movementController.processTouchIgnoreLock(lock, motionevent, view1);
	}
  }

  public final class MovementController
  {
    private boolean hasDelayed = false;
    private Object owner = null;

    public MovementController()
    {
    }

    private void freeAll()
    {
      this.owner = null;
    }

    private boolean processTouchIgnoreLock(Object paramObject, MotionEvent paramMotionEvent, View paramView)
    {
      this.owner = paramObject;
      return processTouch(paramObject, paramMotionEvent, paramView);
    }

    public void free(Object paramObject)
    {
      this.hasDelayed = false;
      Object localObject = this.owner;
      if (paramObject.equals(localObject))
        this.owner = null;
    }

    public boolean getDelayed()
    {
      return this.hasDelayed;
    }

	public boolean isProcessedByOther(Object obj)
	{
		boolean flag;
		if (owner != null && !owner.equals(obj))
			flag = true;
		else
			flag = false;
		return flag;
	}
	
	public void process(Object obj)
	{
		if (owner == null)
			owner = obj;
	}

	public boolean processTouch(Object obj, MotionEvent motionevent, View view1)
	{
		boolean flag = false;
		if (!isProcessedByOther(obj))
		{
			int i = motionevent.getAction();
			int j = (int)motionevent.getX();
			int k = (int)motionevent.getY();
			int l = Math.max(j, 0);
			int i1 = Math.max(k, 0);
			int j1 = Math.min(l, -1 + view1.getWidth());
			int k1 = Math.min(i1, -1 + view1.getHeight());
			if (i == 0)
				movementController.process(obj);
			if (i == 1 || i == 3)
				movementController.free(obj);
			if (i < 3)
				Home.sendMouseEvent(i, j1, k1);
			flag = true;
		}
		return flag;
	}

    public void setDelayed()
    {
      this.hasDelayed = true;
    }
  }

  public abstract interface PauseResumeListener
  {
    public abstract void onPause();

    public abstract void onResume();
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.Home
 * JD-Core Version:    0.6.0
 */