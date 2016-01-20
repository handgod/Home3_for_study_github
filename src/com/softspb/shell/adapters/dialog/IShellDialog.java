package com.softspb.shell.adapters.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

abstract class IShellDialog
{
  private static int instCount = 0;
  protected final int adapterAddress;
  protected Context context;
  protected AlertDialog dialog;
  protected final int dialogToken;
  protected final int instNo;
  protected final Logger logger;
  protected final UIHandler uiHandler;

  public  IShellDialog(Context paramContext, Looper paramLooper, int paramInt1, int paramInt2)
  {
	  synchronized(this)
	  {
	    try
	    {
	      int i = instCount + 1;
	      instCount = i;
	      this.instNo = i;
	
	      Logger localLogger = Loggers.getLogger(getClass());
	      this.logger = localLogger;
	      this.context = paramContext;
	      UIHandler localUIHandler = createUIHandler(paramLooper);
	      this.uiHandler = localUIHandler;
	      this.adapterAddress = paramInt1;
	      this.dialogToken = paramInt2;
	      return;
	    }
	    finally
	    {
	    }
	  }
  }

  protected abstract UIHandler createUIHandler(Looper paramLooper);

  public void dismiss()
  {
    logd("dismiss >>>");
    if (this.dialog != null)
      this.dialog.dismiss();
    logd("dismiss <<<");
  }

  protected void logd(String paramString)
  {
    Logger localLogger = this.logger;
    Object[] arrayOfObject = new Object[1];
    StringBuilder localStringBuilder1 = new StringBuilder().append("(thread=");
    String str1 = Thread.currentThread().getName();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(",");
    long l = Thread.currentThread().getId();
    StringBuilder localStringBuilder3 = localStringBuilder2.append(l).append(") ").append("[");
    int i = this.instNo;
    String str2 = i + "] " + paramString;
    arrayOfObject[0] = str2;
    localLogger.d("ShellDialog", arrayOfObject);
  }

  public void show()
  {
    logd("show >>>");
    this.uiHandler.postShow();
    logd("show <<<");
  }

  abstract class UIHandler extends Handler
  {
    private static final int MSG_SHOW = 1;

    UIHandler(Looper arg2)
    {
      super();
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      
      case 1:
    	  show();
      default:
    	  return;
      }
    }

    void postShow()
    {
      Message localMessage = Message.obtain(this, 1);
      boolean bool = sendMessage(localMessage);
    }

    protected abstract void show();
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.dialog.IShellDialog
 * JD-Core Version:    0.6.0
 */