package com.softspb.shell.adapters;

public abstract class YandexSearchAdapter extends Adapter
{
  public static int SOURCE_SEARCH_BTN = 0;
  public static int SOURCE_WIDGET = 1;

  public YandexSearchAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract boolean isVoiceSearchAvailable();

  public abstract boolean isYandexCountry();

  public abstract void startSearch(int paramInt);

  public abstract void startVoiceSearch();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.YandexSearchAdapter
 * JD-Core Version:    0.6.0
 */