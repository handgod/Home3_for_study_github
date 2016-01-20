package com.softspb.shell.adapters;

public abstract class ApptListAdapter extends Adapter
{
  public ApptListAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void getApptList(int paramInt, long paramLong1, long paramLong2);

  public abstract void openAppt(long paramLong1, long paramLong2, long paramLong3);

  public abstract void openCalendar(long paramLong);

  public abstract boolean openCreateApptDialog(long paramLong);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.ApptListAdapter
 * JD-Core Version:    0.6.0
 */