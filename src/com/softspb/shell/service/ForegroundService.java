package com.softspb.shell.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.softspb.shell.Home;
import com.spb.shell3d.R;

public class ForegroundService extends Service
{
  public static final String ACTION_FOREGROUND = "com.spb.shell3d.FOREGROUND";

 	void handleCommand(Intent intent)
	{
		if (intent != null && "com.spb.shell3d.FOREGROUND".equals(intent.getAction()))
		{
			CharSequence charsequence = getText(R.string.app_name);
			Notification notification = new Notification(0, charsequence, System.currentTimeMillis());
			PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(this, Home.class), 0);
			notification.setLatestEventInfo(this, getText(R.string.app_name), charsequence, pendingintent);
			startForeground(R.string.app_name, notification);
		}
	}

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  public void onDestroy()
  {
    stopForeground(true);
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    handleCommand(paramIntent);
    return 1;
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.service.ForegroundService
 * JD-Core Version:    0.6.0
 */