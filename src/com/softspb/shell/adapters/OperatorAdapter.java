package com.softspb.shell.adapters;

public abstract class OperatorAdapter extends Adapter
{
  public static final int NETWORK_TYPE_1xRTT = 0;
  public static final int NETWORK_TYPE_CDMA = 1;
  public static final int NETWORK_TYPE_EDGE = 2;
  public static final int NETWORK_TYPE_EVDO_0 = 3;
  public static final int NETWORK_TYPE_EVDO_A = 4;
  public static final int NETWORK_TYPE_GPRS = 5;
  public static final int NETWORK_TYPE_HSDPA = 6;
  public static final int NETWORK_TYPE_HSPA = 7;
  public static final int NETWORK_TYPE_HSUPA = 8;
  public static final int NETWORK_TYPE_IDEN = 9;
  public static final int NETWORK_TYPE_UMTS = 10;
  public static final int NETWORK_TYPE_UNKNOWN = 11;
  public static final int PHONE_TYPE_CDMA = 0;
  public static final int PHONE_TYPE_GSM = 1;
  public static final int PHONE_TYPE_NONE = 2;
  public static final int SIMState_Absent = 0;
  public static final int SIMState_NetworkLocked = 1;
  public static final int SIMState_PINRequired = 2;
  public static final int SIMState_PUKRequired = 3;
  public static final int SIMState_Ready = 4;
  public static final int SIMState_Unknown = 5;
  public static final int State_Connected = 4;
  public static final int State_EmergencyOnly = 1;
  public static final int State_NotConnected = 3;
  public static final int State_RadioOff = 0;
  public static final int State_Searching = 2;

  public OperatorAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract boolean isOperatorSupported();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.OperatorAdapter
 * JD-Core Version:    0.6.0
 */