package com.softspb.shell.adapters;

public abstract class WirelessAdapter
  implements Adapter2
{
  public static final int State_Discoverable = 3;
  public static final int State_Off = 0;
  public static final int State_On = 1;
  public static final int State_Transition = 2;
  public static final int State_Unknown = 0;
  public static final int Type_Bluetooth = 2;
  public static final int Type_FlightMode = 4;
  public static final int Type_FreeConn = 5;
  public static final int Type_GPS = 3;
  public static final int Type_Phone = 0;
  public static final int Type_WiFi = 1;
  private volatile int nativePeer;

  public WirelessAdapter(int paramInt)
  {
    this.nativePeer = paramInt;
  }

  protected int getPeer()
  {
    return this.nativePeer;
  }

  public abstract int getWirelessState(int paramInt);

  public abstract boolean isBtDiscoverableSupported();

  public abstract boolean isWirelessSupported(int paramInt);

  protected void notifyChange(int paramInt1, int paramInt2)
  {
    int i = getPeer();
    if (i != 0)
      stateChanged(i, paramInt1, paramInt2);
  }

  public void onStart()
  {
  }

  public void onStop()
  {
    this.nativePeer = 0;
  }

  protected native void stateChanged(int paramInt1, int paramInt2, int paramInt3);

  public abstract boolean switchWirelessState(int paramInt1, int paramInt2);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.WirelessAdapter
 * JD-Core Version:    0.6.0
 */