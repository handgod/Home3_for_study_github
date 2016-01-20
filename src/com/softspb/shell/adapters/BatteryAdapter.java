package com.softspb.shell.adapters;

public abstract class BatteryAdapter extends Adapter
{
  public static final int State_Charging = 2;
  public static final int State_Discharging = 3;
  public static final int State_Full = 5;
  public static final int State_NotCharging = 4;
  public static final int State_Unknown = 1;

  public BatteryAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.BatteryAdapter
 * JD-Core Version:    0.6.0
 */