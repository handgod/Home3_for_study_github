package com.softspb.shell.adapters;

public abstract class AppAdapter extends Adapter
{
  public AppAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract int GetAndroidVersion();

  public abstract boolean canChangeInterfaceMode();

  public abstract void enableHTCStereo3D(boolean paramBoolean);

  public abstract String getDeviceId();

  public abstract String getDeviceModel();

  public abstract String getKey();

  public abstract void getVersionInfo(int paramInt);

  public abstract boolean hasKeyboard();

  public abstract boolean hasTelephony();

  public abstract boolean isHTCStereo3DEnabled();

  public abstract boolean isMipmapEnabled();

  public abstract void setOrientation(int paramInt);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.AppAdapter
 * JD-Core Version:    0.6.0
 */