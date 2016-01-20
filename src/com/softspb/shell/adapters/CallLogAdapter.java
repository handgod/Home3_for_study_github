package com.softspb.shell.adapters;

public abstract interface CallLogAdapter extends Adapter2
{
  public static final int NATIVE_CALL_TYPE_FAILED = 2;
  public static final int NATIVE_CALL_TYPE_INCOMING = 1;
  public static final int NATIVE_CALL_TYPE_MISSED = 0;
  public static final int NATIVE_CALL_TYPE_OUTGOING = 3;
  public static final int NATIVE_CALL_TYPE_TOTAL = 4;

  public abstract void openCallLog();

  public abstract void reloadCallLog();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.CallLogAdapter
 * JD-Core Version:    0.6.0
 */