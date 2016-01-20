package com.softspb.shell.adapters;

public abstract class WeatherAdapter extends Adapter
{
  static final int NATIVE_CITY_ID_NOT_SELECTED = 255;
  static final int NATIVE_FORECAST_DATE_OFFSET = 1;
  static final int NATIVE_FORECAST_DAY_PARTS = 4;
  static final int NATIVE_FORECAST_MAX_DAYS = 5;
  static final int NATIVE_FORECAST_PACKET_SIZE = 6;
  static final int NATIVE_FORECAST_SKY_ICON_OFFSET = 3;
  static final int NATIVE_FORECAST_TEMP_C_OFFSET = 4;
  static final int NATIVE_FORECAST_TEMP_F_OFFSET = 5;
  static final int NATIVE_FORECAST_TIME_OFFSET = 2;
  static final int NATIVE_FORECAST_VALID_OFFSET = 0;
  static final int NATIVE_HUMIDITY_UNITS = 0;
  static final int NATIVE_UPDATE_RATE_1_H = 60;
  static final int NATIVE_UPDATE_RATE_24_H = 1440;
  static final int NATIVE_UPDATE_RATE_30_M = 30;
  static final int NATIVE_UPDATE_RATE_4_H = 240;
  static final int NATIVE_UPDATE_RATE_FREE_CONNECTION = 0;
  static final int NATIVE_VALUE_NA = -2147483648;

  public WeatherAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  protected static long nativeUpdateRateToAlarmManagerInterval(int paramInt)
  {
    return paramInt * 60000L;
  }

  public abstract void forceUpdate(int paramInt);

  public abstract void loadCityName(int paramInt);

  public abstract void loadConditions(int paramInt);

  public abstract void loadForecast(int paramInt);

  public abstract void loadUpdateStatus(int paramInt);

  public abstract void onWeatherProviderDeleted(int paramInt);

  public abstract void openCitySelect(int paramInt);

  public abstract void selectCity(int paramInt1, int paramInt2);

  public abstract void setUpdateRate(int paramInt);

  public abstract void setUseOnlyWifi(boolean paramBoolean);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.WeatherAdapter
 * JD-Core Version:    0.6.0
 */