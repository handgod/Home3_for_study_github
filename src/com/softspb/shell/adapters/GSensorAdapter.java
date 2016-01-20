package com.softspb.shell.adapters;

public abstract class GSensorAdapter extends Adapter
{
  public GSensorAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void EnableGSensor(boolean paramBoolean);

  public abstract void askEvents();

  public abstract boolean isPresent();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.GSensorAdapter
 * JD-Core Version:    0.6.0
 */