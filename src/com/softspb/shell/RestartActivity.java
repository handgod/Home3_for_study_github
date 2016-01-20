package com.softspb.shell;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

import com.spb.shell3d.R.string;
import com.spb.shell3d.R;

public class RestartActivity extends Activity
{
  public static final String EXTRA_PID = "pid";
  private static final int TIMEOUT = 5000;
  private static final int TIMEOUT_AFTER_KILL = 1000;

  
  class RestartActivity$1  implements Runnable
{
	private int val$shellPid  ;
	
	public RestartActivity$1(int i) {
		this.val$shellPid = i;
		}
	public void run()
	{
		try
		{
			Thread.sleep(5000L);
		}
		catch (InterruptedException interruptedexception)
		{
			interruptedexception.printStackTrace();
		}
		Process.killProcess(val$shellPid);
		try
		{
			Thread.sleep(1000L);
		}
		catch (InterruptedException interruptedexception1)
		{
			interruptedexception1.printStackTrace();
		}
		try
		{
			dismissDialog(0);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		finish();
		startActivity(new Intent(RestartActivity.this,Home.class));
	}
}

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Bundle localBundle = getIntent().getExtras();
    if (!localBundle.containsKey("pid"))
      finish();
    int i = localBundle.getInt("pid");
    RestartActivity$1 local1 = new RestartActivity$1(i);
    Thread localThread = new Thread(local1);
    showDialog(0);
    localThread.start();
  }

  protected Dialog onCreateDialog(int paramInt)
  {
    ProgressDialog localProgressDialog = new ProgressDialog(this);
    int i = R.string.wait;
    String str = getString(i);
    localProgressDialog.setMessage(str);
    return localProgressDialog;
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.RestartActivity
 * JD-Core Version:    0.6.0
 */