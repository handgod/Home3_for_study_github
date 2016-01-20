package com.softspb.shell.adapters;

public abstract class TimeAdapter extends Adapter
{
  public static final int Date_Day = 16;
  public static final int Date_MonthAbbr = 64;
  public static final int Date_MonthLong = 128;
  public static final int Date_MonthOrd = 32;
  public static final int Date_WeekDayAbbr = 256;
  public static final int Date_WeekDayLong = 512;
  public static final int Date_Year = 2048;
  public static final int Date_YearShort = 1024;
  public static final int Time_Hours = 1;
  public static final int Time_Hours24 = 2;
  public static final int Time_Minutes = 4;
  public static final int Time_Seconds = 8;

  public TimeAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract String format(long paramLong, int paramInt);

  public abstract String getAMPMString(long paramLong);

  public abstract int getFirstDayOfWeek();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.TimeAdapter
 * JD-Core Version:    0.6.0
 */