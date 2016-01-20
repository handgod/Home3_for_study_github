package com.softspb.shell.adapters;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.softspb.shell.opengl.NativeCallbacks;

public class Adapter
{
  private Handler uiHandler;
  private AdaptersHolder watcher;

  class Adapter$1
  implements Runnable
{
  public void run()
  {
    onStartInUIThread();
  }
}
  public Adapter(AdaptersHolder paramAdaptersHolder)
  {
    this.watcher = paramAdaptersHolder;
    Handler localHandler = new Handler();
    this.uiHandler = localHandler;
  }

  private void postStartInUI()
  {
    Handler localHandler = this.uiHandler;
    Adapter$1 local1 = new Adapter$1();
    boolean bool = localHandler.post(local1);
  }

  public static void writeTimeLog(String paramString, long paramLong)
  {
    String str = paramString + " :::: " + paramLong;
    int i = Log.i("profiler", str);
  }

  public final void create(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    onCreate(paramContext, paramNativeCallbacks);
    this.watcher.addCreated(this);
  }

  public final void destroy(Context paramContext)
  {
    onDestroy(paramContext);
    this.watcher.destroyed(this);
  }

  protected void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
  }

  protected void onDestroy(Context paramContext)
  {
  }

  protected void onStart()
  {
  }

  protected void onStart(int paramInt)
  {
  }

  protected void onStartInUIThread()
  {
  }

  protected void onStop()
  {
  }

  public final void start()
  {
    onStart();
    postStartInUI();
    this.watcher.addStarted(this);
  }

  public final void start(int paramInt)
  {
    onStart(paramInt);
    postStartInUI();
    this.watcher.addStarted(this);
  }

  public final void stop()
  {
    onStop();
    this.watcher.stopped(this);
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.Adapter
 * JD-Core Version:    0.6.0
 */