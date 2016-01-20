package com.softspb.shell.adapters;

public abstract class SoundProfileAdapter extends Adapter
{
  static final int VOLUME_MAX = 5;
  static final int eRING_MODE_SILENT = 1;
  static final int eRING_MODE_SOUND = 0;
  static final int eRING_MODE_VIBRO = 2;

  public SoundProfileAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract int getRingMode();

  public abstract int getRingVolume();

  public abstract void notifyChange();

  public abstract void openSettings();

  public abstract void setRingMode(int paramInt);

  public abstract void setRingVolume(int paramInt);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.SoundProfileAdapter
 * JD-Core Version:    0.6.0
 */