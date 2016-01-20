package com.softspb.shell.adapters.program.adapter;

import android.content.Context;
import com.softspb.shell.adapters.AdaptersHolder;
import com.softspb.shell.adapters.ProgramListAdapter;
import com.softspb.shell.adapters.ProgramListAdapterAndroid;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.programlist.ProgramInfo;
import com.spb.programlist.ProgramList;
import com.spb.programlist.ProgramListListener;

public class ProgramListAdapterAndroid2 extends ProgramListAdapter
  implements ProgramListListener
{
  private static Logger logger = Loggers.getLogger(ProgramListAdapterAndroid2.class.getName());
  ProgramList programList;

  public ProgramListAdapterAndroid2(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  /** @deprecated */
  public synchronized void findByTag(String paramString)
  {
 
    try
    {
      Logger localLogger = logger;
      String str = "findByTag: tag=" + paramString;
      localLogger.d(str);
      ProgramInfo localProgramInfo = this.programList.findByTag(paramString);
  
      return;
    }
    finally
    {
 
    }

  }

  /** @deprecated */
  public synchronized  boolean launch(String paramString)
  {
    ;
    try
    {
      Logger localLogger = logger;
      String str = "launch: tag=" + paramString;
      localLogger.d(str);
      boolean bool1 = this.programList.launch(paramString);
      boolean bool2 = bool1;
  
      return bool2;
    }
    finally
    {
   
    }
  
  }

  /** @deprecated */
  public synchronized boolean launch(String paramString1, String paramString2)
  {
 
    try
    {
      Logger localLogger = logger;
      String str = "launch: packageName=" + paramString1 + " activityClassName=" + paramString2;
      localLogger.d(str);
      boolean bool1 = this.programList.launch(paramString1, paramString2);
      boolean bool2 = bool1;
      return bool2;
    }
    finally
    {
  
    }

  }

	public void onAdded(ProgramInfo programinfo)
	{
		logger.d((new StringBuilder()).append("onAdded >>> package=").append(programinfo.packageName).append(" activity=").append(programinfo.activityClassName).append(" tag=").append(programinfo.widgetTag).toString());
		ProgramListAdapterAndroid.launcherPut(programinfo.packageName, programinfo.activityClassName, programinfo.activityLabel, programinfo.activityIconResource, programinfo.widgetTag, programinfo.addToPrograms, programinfo.isDefault, programinfo.isUninstallable);
		logger.d("onAdded <<<");
	}

  protected void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    super.onCreate(paramContext, paramNativeCallbacks);
    ProgramList localProgramList = ProgramList.getInstance(paramContext);
    this.programList = localProgramList;
  }

  public void onDeleted(String paramString)
  {
    Logger localLogger = logger;
    String str = "onDeleted >>> packageName=" + paramString;
    localLogger.d(str);
    ProgramListAdapterAndroid.launcherDelete(paramString);
    logger.d("onDeleted <<<");
  }

  protected void onStart()
  {
    logger.d("onStart >>>");
    this.programList.registerListener(this);
    this.programList.start();
    logger.d("onStart <<<");
  }

  protected void onStop()
  {
    logger.d("onStop >>>");
    this.programList.stop();
    this.programList.unregisterListener(this);
    logger.d("onStop <<<");
  }

public void uninstall(String s)
	{
		logger.d((new StringBuilder()).append("uninstall: packageName=").append(s).toString());
		programList.uninstall(s);
	}

}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.program.adapter.ProgramListAdapterAndroid2
 * JD-Core Version:    0.6.0
 */