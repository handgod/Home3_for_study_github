package com.softspb.shell.adapters.simplemedia;

import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener;

class MediaReceiver extends DecoratedBroadcastReceiver
{
  private static final String SUFFIX_META_CHANGED = ".metachanged";
  private static final String SUFFIX_PLAYBACK_COMPLETE = ".playbackcomplete";
  private static final String SUFFIX_PLAYSTATE_CHANGED = ".playstatechanged";
  private SimpleMediaAdapterAndroid simpleAdapter;

  public MediaReceiver(String paramString, DecoratedBroadcastReceiver.IActionListener paramIActionListener1, DecoratedBroadcastReceiver.IActionListener paramIActionListener2, DecoratedBroadcastReceiver.IActionListener paramIActionListener3)
  {
    String str1 = paramString + ".playbackcomplete";
    String str2 = paramString + ".metachanged";
    String str3 = paramString + ".playstatechanged";
    addActionListener(str1, paramIActionListener1);
    addActionListener(str2, paramIActionListener2);
    addActionListener(str3, paramIActionListener3);
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.simplemedia.MediaReceiver
 * JD-Core Version:    0.6.0
 */