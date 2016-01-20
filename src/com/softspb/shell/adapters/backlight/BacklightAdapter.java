package com.softspb.shell.adapters.backlight;

import com.softspb.shell.adapters.Adapter;
import com.softspb.shell.adapters.AdaptersHolder;

public abstract class BacklightAdapter extends Adapter
{
  public static final int AUTO_LEVEL = 255;

  public BacklightAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract int getLevel();

  public abstract int getMaxLevelsCount();

  public abstract boolean isAutolevelSupported();

  public abstract boolean isSupported();

  public abstract void setAutoLevel();

  public abstract void setLevel(int paramInt);

  public abstract void setManualLevel(int paramInt);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.backlight.BacklightAdapter
 * JD-Core Version:    0.6.0
 */