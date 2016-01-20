package com.softspb.shell.adapters;

public abstract class CitiesAdapter extends Adapter
{
  public static final String CTA_REQUEST_ID = "cta_request_id";
  public static final int NATIVE_CURRENT_LOCATION_CITY_ID=0;

  public CitiesAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void loadCityInfo(int paramInt);

  public abstract long openCitySelect();

  public abstract void startListeningCurrentLocation();

  public abstract void stopListeningCurrentLocation();

  public abstract void updateCurrentLocation();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.CitiesAdapter
 * JD-Core Version:    0.6.0
 */