package com.softspb.shell.adapters.simplemedia;

import android.content.Context;
import android.content.Intent;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

class SimpleMediaCommander
{
  private static final String SUFFIX_CHECKPLAYSTATUS = ".musicservicecommand.checkplaystatus";
  private static final String SUFFIX_NEXT = ".musicservicecommand.next";
  private static final String SUFFIX_PREV = ".musicservicecommand.previous";
  private static final String SUFFIX_TOGGLEPAUSE = ".musicservicecommand.togglepause";
  private static Logger logger = Loggers.getLogger("simpleMediaCommander");
  private String checkPlayStatus;
  private Context context;
  private String next;
  private String prev;
  private String togglePause;

  public SimpleMediaCommander(String paramString, Context paramContext)
  {
    String str1 = paramString + ".musicservicecommand.togglepause";
    this.togglePause = str1;
    String str2 = paramString + ".musicservicecommand.next";
    this.next = str2;
    String str3 = paramString + ".musicservicecommand.previous";
    this.prev = str3;
    String str4 = paramString + ".musicservicecommand.checkplaystatus";
    this.checkPlayStatus = str4;
    this.context = paramContext;
  }

  private Intent createIntent(String paramString)
  {
    return new Intent(paramString);
  }

  private void sendCommandIntent(String paramString)
  {
    Context localContext = this.context;
    Intent localIntent = createIntent(paramString);
    localContext.sendBroadcast(localIntent);
  }

  public void checkPlayStatus()
  {
    logger.e("checkPlayStatus");
    String str = this.checkPlayStatus;
    sendCommandIntent(str);
  }

  public void doNext()
  {
    logger.e("next");
    String str = this.next;
    sendCommandIntent(str);
  }

  public void doPrevious()
  {
    logger.e("prev");
    String str = this.prev;
    sendCommandIntent(str);
  }

  public void doTogglePause()
  {
    logger.e("togglePause");
    String str = this.togglePause;
    sendCommandIntent(str);
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.simplemedia.SimpleMediaCommander
 * JD-Core Version:    0.6.0
 */