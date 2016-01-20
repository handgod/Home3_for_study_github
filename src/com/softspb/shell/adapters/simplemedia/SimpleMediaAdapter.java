package com.softspb.shell.adapters.simplemedia;

import com.softspb.shell.adapters.Adapter;
import com.softspb.shell.adapters.AdaptersHolder;

public abstract class SimpleMediaAdapter extends Adapter
{
  public SimpleMediaAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract boolean doNext();

  public abstract boolean doPrev();

  public abstract boolean doTogglePause();

  public abstract boolean isSupportingPlayback();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.simplemedia.SimpleMediaAdapter
 * JD-Core Version:    0.6.0
 */