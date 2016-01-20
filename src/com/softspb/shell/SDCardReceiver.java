package com.softspb.shell;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.ArrayList;
import java.util.Iterator;

public class SDCardReceiver extends BroadcastReceiver
{
  private static final ArrayList<SDCardListener> listeners = new ArrayList();
  Logger logger;

  public SDCardReceiver()
  {
    Logger localLogger = Loggers.getLogger(SDCardReceiver.class.getName());
    this.logger = localLogger;
  }

  private static void notifyListeners(Intent paramIntent)
  {
    synchronized (listeners)
    {
      Iterator localIterator = listeners.iterator();
      if (localIterator.hasNext())
        ((SDCardListener)localIterator.next()).onReceive(paramIntent);
    }
//    monitorexit;
  }

  public static void registerListener(SDCardListener paramSDCardListener)
  {
    synchronized (listeners)
    {
      if (!listeners.contains(paramSDCardListener))
        listeners.add(paramSDCardListener);
      return;
    }
  }

  public static void unregisterListener(SDCardListener paramSDCardListener)
  {
    synchronized (listeners)
    {
      boolean bool = listeners.remove(paramSDCardListener);
      return;
    }
  }

  public void onReceive(Context paramContext, Intent paramIntent)
  {
	logger.d((new StringBuilder()).append("received SD card intent: action=").append(paramIntent.getAction()).append(" data=").append(paramIntent.getData()).toString());
    notifyListeners(paramIntent);
  }

  public abstract interface SDCardListener
  {
    public abstract void onReceive(Intent paramIntent);
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.SDCardReceiver
 * JD-Core Version:    0.6.0
 */