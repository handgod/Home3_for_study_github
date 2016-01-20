// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.format.Time;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.softspb.shell.Home;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.updateservice.UpdateService;
import com.softspb.util.DecimalDateTimeEncoding;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.softspb.weather.core.WeatherApplication;
import com.softspb.weather.core.WeatherApplicationPreferences;
import com.softspb.weather.core.WeatherConnectivityReceiver;
import com.softspb.weather.core.WeatherDataCache;
import com.softspb.weather.model.CurrentConditions;
import com.softspb.weather.model.Forecast;
import com.softspb.weather.model.UpdateStatus;
import com.softspb.weather.model.WeatherConstants;
import com.softspb.weather.model.WeatherParameterValue;
import com.spb.cities.location.CurrentLocationPreferences;
import com.spb.cities.service.CurrentLocationService;

// Referenced classes of package com.softspb.shell.adapters:
//			WeatherAdapter, AdaptersHolder

public class WeatherAdapterAndroid extends WeatherAdapter
	implements com.softspb.weather.core.WeatherDataCache.WeatherListener
{

	private static final int NATIVE_WIND_DIRECTION_INTERVALS = 8;
	private static final int NATIVE_WIND_DIRECTION_VARIABLE = 8;
	private static final IntentFilter connectivityFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
	private static Logger logger;
	private final SparseArray cityId2providers = new SparseArray();
	private Context context;
	private CurrentLocationPreferences currentLocationPrefs;
	private final SparseIntArray provider2cityId = new SparseIntArray();
	private final Object providersLock = new Object();
	private int weatherAdapterToken;
	private WeatherConnectivityReceiver weatherConnectivityReceiver;
	private WeatherDataCache weatherDataCache;
	private WeatherApplicationPreferences weatherPrefs;

	public WeatherAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
	}

	private  boolean bindProviderAndCity(int i, int j)
	{
		Object obj = providersLock;
		synchronized(obj){
			provider2cityId.put(i, j);
			Object obj1 = (List)cityId2providers.get(j);
			if (obj1 == null)
			{
				obj1 = new CopyOnWriteArrayList();
				cityId2providers.put(j, obj1);
			}
			boolean flag = ((List) (obj1)).isEmpty();
			if (!((List) (obj1)).contains(Integer.valueOf(i)))
				((List) (obj1)).add(Integer.valueOf(i));
			return flag;
		}
	}

	private static int[] encodeForecast(Forecast aforecast[], int i)
		throws IllegalArgumentException
	{
		logger.d((new StringBuilder()).append("encodeForecast >>> startDate=").append(i).append(" forecast=").append(toDatesString(aforecast)).toString());
		int j = aforecast.length;
		int k = -1;
		int l = -1;
		int i1 = 0;
		int j1 = 0;
		if (j > 0)
		{
			j1 = DecimalDateTimeEncoding.daysBetween(i, aforecast[0].getDateLocal());
			if (j1 < 0)
				j1 = 0;
		}
		int k1 = 0;
		while (k1 < j) 
		{
			int k4 = aforecast[k1].getDateLocal();
			if (k4 == l)
			{
				i1++;
			} else
			{
				if (i1 > 4)
					throw new IllegalArgumentException((new StringBuilder()).append("Too many forecast data item per day ").append(l).append("  (max 4): ").append(i1).toString());
				if (k != -1)
				{
					if (i1 != 4)
						throw new IllegalArgumentException((new StringBuilder()).append("Unexpected number of forecast items per day ").append(l).append(" (must be 4): ").append(i1).toString());
				} else
				if (l != -1)
					k = 4 - i1;
				i1 = 1;
			}
			l = k4;
			k1++;
		}
		if (k == -1)
			k = 0;
		if (i1 > 4)
			throw new IllegalStateException((new StringBuilder()).append("Too many forecast data item per day ").append(l).append(" (max 4): ").append(i1).toString());
		int l1 = 4 - i1;
		int i2 = Math.min(l1 + (j + (k + j1 * 4)), 20);
		logger.d((new StringBuilder()).append("encodeForecast: headPadding=").append(k).append(" tailPadding=").append(l1).append(" startingInvalidDays=").append(j1).append(" count=").append(j).append(" outCount=").append(i2).toString());
		int ai[] = new int[i2 * 6];
		int j2 = 0;
		Time time = DecimalDateTimeEncoding.fromDateUTC(i);
		for (int k2 = 0; k2 < j1 && j2 < ai.length; k2++)
		{
			int i4 = DecimalDateTimeEncoding.encodeDate(time);
			for (int j4 = 0; j4 < 4; j4++)
			{
				ai[j2 + 0] = 0;
				ai[j2 + 1] = i4;
				j2 += 6;
			}

			time.monthDay = 1 + time.monthDay;
			time.normalize(true);
		}

		int l2 = DecimalDateTimeEncoding.encodeDate(time);
		for (int i3 = 0; i3 < k && j2 < ai.length; i3++)
		{
			ai[j2 + 0] = 0;
			ai[j2 + 1] = l2;
			j2 += 6;
		}

		for (int j3 = 0; j3 < j && j2 < ai.length; j3++)
		{
			Forecast forecast = aforecast[j3];
			ai[j2 + 0] = 1;
			ai[j2 + 1] = forecast.getDateLocal();
			ai[j2 + 2] = forecast.getTimeLocal();
			int l3 = WeatherConstants.gismeteoCodesToDayIcon(forecast.getCloud(), forecast.getPrecip());
			ai[j2 + 3] = l3;
			ai[j2 + 4] = ((Number)forecast.getMaxTemp().getValue(0)).intValue();
			ai[j2 + 5] = ((Number)forecast.getMaxTemp().getValue(1)).intValue();
			j2 += 6;
		}

		for (int k3 = 0; k3 < l1 && j2 < ai.length; k3++)
		{
			ai[j2 + 0] = 0;
			j2 += 6;
		}

		return ai;
	}

	private int getCityIdForProvider(int i)
	{
		logger.d((new StringBuilder()).append("getCityIdForProvider >>> provider=0x").append(Integer.toHexString(i)).toString());
		int j = provider2cityId.get(i, 0x80000000);
		if (j == 0x80000000)
		{
			selectCity(i, -1024);
			loadCityName(i);
			loadConditions(i);
			loadForecast(i);
			j = provider2cityId.get(i, 0x80000000);
		}
		logger.d((new StringBuilder()).append("getCityIdForProvider <<< return cityId=").append(j).toString());
		return j;
	}

	public static int getNativeWindDirection(WeatherParameterValue weatherparametervalue)
	{
		int i;
		if (weatherparametervalue == null)
		{
			i = 0x80000000;
		} else
		{
			double d = ((Number)weatherparametervalue.getValue(2)).doubleValue();
			if (d == (0.0D / 0.0D))
			{
				i = 8;
			} else
			{
				double d1;
				for (d1 = d + 45D * 0.5D; d1 < 0.0D; d1 += 360D);
				for (; d1 >= 360D; d1 -= 360D);
				if (d1 < 0.0D)
					i = 0;
				else
					i = (int)Math.floor(d1 / 45D);
			}
		}
		return i;
	}

	private static boolean isSuitableForMobileShell(Forecast aforecast[])
	{
		boolean flag;
		flag = false;
		logger.d((new StringBuilder()).append("isSuitableForMobileShell >>> ").append(toDatesString(aforecast)).toString());
		if (aforecast != null && aforecast.length != 0) 
		{
			int i = aforecast.length;
			int j;
			int k;
			int l;
			int i1;
			if (i > 0)
				j = aforecast[0].getDateLocal();
			else
				j = 0;
			k = j;
			if (j != 0)
				l = 1;
			else
				l = 0;
			for (i1 = 1; i1 < i; i1++)
			{
				int j1 = aforecast[i1].getDateLocal();
				if (j != j1)
				{
					if (k == j && l > 4 || j != k && l != 4)
					{
						logger.d((new StringBuilder()).append("isSuitableForMobileShell <<< false - currentDate=").append(j).append(" currentDateCount=").append(l).toString());
						continue; /* Loop/switch isn't completed */
					}
					j = j1;
					l = 0;
				}
				l++;
			}

			if (l > 4)
			{
				logger.d((new StringBuilder()).append("isSuitableForMobileShell <<< false - currentDate=").append(j).append(" currentDateCount=").append(l).toString());
			} else
			{
				logger.d("isSuitableForMobileShell <<< true");
				flag = true;
			}
		}
		else 
		{
			logger.d("isSuitableForMobileShell <<< true - null");
			flag = true;
		}
		return flag;

	}

	private static native void notifyWeatherUpdated(int i, int j, int k);

	private static native void onCitySelected(int i, int j, int k);

	private static native void setCityName(int i, int j, String s);

	private void setConditions(int i, CurrentConditions currentconditions)
	{
		if (weatherAdapterToken == 0)
		{
			logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
		} else
		{
			int j = provider2cityId.get(i);
			if (currentconditions != null)
			{
				int k = currentconditions.getDateUTC();
				int l = currentconditions.getTimeUTC();
				Date date = DecimalDateTimeEncoding.decodeDateTimeUT(k, l);
				int i1 = currentconditions.getSkyIcon();
				long l1 = date.getTime();
				WeatherParameterValue weatherparametervalue = currentconditions.getTemp();
				int j1;
				int k1;
				int i2;
				WeatherParameterValue weatherparametervalue1;
				int j2;
				WeatherParameterValue weatherparametervalue2;
				int k2;
				int l2;
				int i3;
				WeatherParameterValue weatherparametervalue3;
				int j3;
				int k3;
				int l3;
				int i4;
				WeatherParameterValue weatherparametervalue4;
				int j4;
				WeatherParameterValue weatherparametervalue5;
				int k4;
				int l4;
				String s;
				if (weatherparametervalue == null)
					j1 = 0x80000000;
				else
					j1 = ((Number)weatherparametervalue.getValue(0)).intValue();
				if (weatherparametervalue == null)
					k1 = 0x80000000;
				else
					k1 = ((Number)weatherparametervalue.getValue(1)).intValue();
				i2 = getNativeWindDirection(currentconditions.getWindDirection());
				weatherparametervalue1 = currentconditions.getWindSpeed();
				if (weatherparametervalue1 == null)
					j2 = 0x80000000;
				else
					j2 = weatherparametervalue1.getInt1000(2);
				weatherparametervalue2 = currentconditions.getWindSpeed();
				if (weatherparametervalue2 == null)
					k2 = 0x80000000;
				else
					k2 = weatherparametervalue2.getInt1000(0);
				if (weatherparametervalue2 == null)
					l2 = 0x80000000;
				else
					l2 = weatherparametervalue2.getInt1000(1);
				if (weatherparametervalue2 == null)
					i3 = 0x80000000;
				else
					i3 = weatherparametervalue2.getInt1000(3);
				weatherparametervalue3 = currentconditions.getPressure();
				if (weatherparametervalue3 == null)
					j3 = 0x80000000;
				else
					j3 = weatherparametervalue3.getInt1000(0);
				if (weatherparametervalue3 == null)
					k3 = 0x80000000;
				else
					k3 = weatherparametervalue3.getInt1000(1);
				if (weatherparametervalue3 == null)
					l3 = 0x80000000;
				else
					l3 = weatherparametervalue3.getInt1000(3);
				if (weatherparametervalue3 == null)
					i4 = 0x80000000;
				else
					i4 = weatherparametervalue3.getInt1000(2);
				weatherparametervalue4 = currentconditions.getRelHumidity();
				if (weatherparametervalue4 == null)
					j4 = 0x80000000;
				else
					j4 = ((Number)weatherparametervalue4.getValue(0)).intValue();
				weatherparametervalue5 = currentconditions.getDewPoint();
				if (weatherparametervalue5 == null)
					k4 = 0x80000000;
				else
					k4 = ((Number)weatherparametervalue5.getValue(0)).intValue();
				if (weatherparametervalue5 == null)
					l4 = 0x80000000;
				else
					l4 = ((Number)weatherparametervalue5.getValue(1)).intValue();
				logger.d((new StringBuilder()).append("Invoking NativeCalls.setWeatherConditions: cityId=").append(j).append(" timeMs=").append(l1).append(" date=").append(k).append(" time=").append(l).append(" skyIcon=").append(i1).append(" tempC=").append(j1).append(" tempF=").append(k1).append(" winDir=").append(i2).append(" windSpeed_Mps=").append(j2).append(" windSpeed_Mph=").append(k2).append(" windSpeed_Knots=").append(l2).append(" windSpeed_Kph=").append(i3).append(" pressure_Mm=").append(j3).append(" pressure_Inch=").append(k3).append(" pressure_Hpa=").append(l3).append(" pressure_Atm=").append(i4).append(" humidity=").append(j4).append(" dewPointC=").append(k4).append(" dewPointF=").append(l4).toString());
				setWeatherConditions(weatherAdapterToken, i, j, l1, i1, j1, k1, i2, j2, k2, l2, i3, j3, k3, l3, i4, j4, k4, l4);
				s = currentconditions.getLocation();
				logger.d((new StringBuilder()).append("Invoking NativeCalls.setStationName: stationName=").append(s).toString());
				setStationName(weatherAdapterToken, i, s);
			} else
			{
				logger.d("Inoking NativeCalls.setWeatherConditionsNA");
				setWeatherConditionsNA(weatherAdapterToken, i);
			}
		}
	}

	private static native void setForecast(int i, int j, int ai[]);

	private void setForecast(int i, int ai[])
	{
		if (weatherAdapterToken == 0)
			logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
		else
		if (ai != null)
		{
			try
			{
				logger.d((new StringBuilder()).append("Invoking NativeCalls.setForecast()... length=").append(ai.length).toString());
				setForecast(weatherAdapterToken, i, ai);
			}
			catch (RuntimeException runtimeexception)
			{
				logger.e((new StringBuilder()).append("Error encoding forecast: ").append(runtimeexception).toString(), runtimeexception);
			}
		} else
		{
			logger.d("Invoking NativeCalls.setForecastNA()");
			setForecastNA(weatherAdapterToken, i);
		}
	}

	private static native void setForecastNA(int i, int j);

	private static native void setStationName(int i, int j, String s);

	private static native void setUpdateStatus(int i, int j, long l, boolean flag);

	private static native void setWeatherConditions(int i, int j, int k, long l, int i1, int j1, int k1, 
			int l1, int i2, int j2, int k2, int l2, int i3, int j3, 
			int k3, int l3, int i4, int j4, int k4);

	private static native void setWeatherConditionsNA(int i, int j);

	private void startListeningCityId(int i)
	{
		logger.d((new StringBuilder()).append("GROUPER startListeningCityId >>> cityId=").append(i).toString());
		weatherPrefs.addCityId(i);
		weatherDataCache.registerWeatherListener(this, i, true);
		logger.d("GROUPER startListeningCityId <<<");
	}

	private void stopListeningCityId(int i)
	{
		logger.d((new StringBuilder()).append("GROUPER stopListeningCityId >>>  cityId=").append(i).toString());
		weatherPrefs.removeCityId(i);
		weatherDataCache.unregisterWeatherListener(this, i);
		logger.d("GROUPER stopListeningCityId <<<");
	}

	private static String toDatesString(Forecast aforecast[])
	{
		String s;
		if (aforecast == null)
		{
			s = "NULL";
		} else
		{
			StringBuilder stringbuilder = new StringBuilder();
			int i = aforecast.length;
			for (int j = 0; j < i; j++)
			{
				Forecast forecast = aforecast[j];
				if (stringbuilder.length() > 0)
					stringbuilder.append(' ');
				stringbuilder.append(forecast.getDateLocal()).append(':').append(forecast.getTimeLocal());
			}

			s = stringbuilder.toString();
		}
		return s;
	}

	private boolean unbindProviderAndCity(int i, int j)
	{
		Object obj = providersLock;
		boolean flag;
		synchronized (providersLock) {

			provider2cityId.delete(i);
			List list = (List)cityId2providers.get(j);
			if (list != null)
			{
				list.remove(Integer.valueOf(i));
				if (list.isEmpty())
				{
					cityId2providers.remove(j);
					flag = true;
					return flag;
				}
			}
			flag = false;	
		}
		return flag;
	}

	public void forceUpdate(int i)
	{
		logger.d("forceUpdate >>>");
		if (weatherAdapterToken == 0)
		{
			logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
			logger.d("forceUpdate <<<");
		} else
		{
			logger.d("forceUpdate: obtaining weather_lock 3...");
			int j;
			synchronized (providersLock)
			{
				logger.d("forceUpdate: obtained weather_lock 3");
				loadCityName(i);
				j = getCityIdForProvider(i);
			}
			logger.d("forceUpdate: released weather_lock 3");
			if (j == -1024)
			{
				j = weatherDataCache.getCurrentLocationCityId();
				CurrentLocationService.updateNow(context, true);
			}
			if (j != 0x80000000)
			{
				logger.d((new StringBuilder()).append("forceUpdate: cityId=").append(j).toString());
				WeatherApplication.updateWeather(Collections.singletonList(Integer.valueOf(j)), context, true);
			}
			logger.d("forceUpdate <<<");
		}
	}

	public int getDetailedForecastDate()
	{
		return 0;
	}

	public void homeOnDestroy()
	{
		context.unregisterReceiver(weatherConnectivityReceiver);
		weatherConnectivityReceiver = null;
	}

	public void homeOnStart()
	{
		if (weatherConnectivityReceiver == null)
		{
			weatherConnectivityReceiver = new WeatherConnectivityReceiver();
			context.registerReceiver(weatherConnectivityReceiver, connectivityFilter);
		}
	}

	public void loadCityName(int i)
	{
		logger.d((new StringBuilder()).append("loadCityName >>> provider=0x").append(Integer.toHexString(i)).toString());
		if (weatherAdapterToken == 0)
		{
			logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
			logger.d("loadCityName <<<");
		} else
		{
			logger.d("loadCityName: obtaining weather_lock 3...");
			synchronized (providersLock)
			{
				logger.d("loadCityName: obtained weather_lock 3");
				int j = getCityIdForProvider(i);
				if (j != 0x80000000)
				{
					String s = weatherDataCache.getCityName(j);
					if (s != null)
					{
						logger.d((new StringBuilder()).append("Invoking NativeCalls.setCityName: cityName=").append(s).toString());
						setCityName(weatherAdapterToken, i, s);
					}
				}
			}
			logger.d("loadCityName: released weather_lock 3");
			logger.d("loadCityName <<<");
		}
		return;

	}

	public void loadConditions(int i)
	{
		logger.d((new StringBuilder()).append("loadConditions >>> provider=0x").append(Integer.toHexString(i)).toString());
		logger.d("loadCityName: obtaining weather_lock 3...");
		synchronized (providersLock)
		{
			logger.d("loadConditions: obtained weather_lock 3");
			int j = getCityIdForProvider(i);
			logger.d((new StringBuilder()).append("loadConditions: got mapping: 0x").append(Integer.toHexString(i)).append(" -> ").append(j).toString());
			if (j != 0x80000000)
				setConditions(i, weatherDataCache.getCurrent(j));
		}
		logger.d("loadConditions: released weather_lock 3");
		logger.d("loadConditions <<<");
		return;

	}

	public void loadForecast(int i)
	{
		logger.d((new StringBuilder()).append("loadForecast >>> provider=0x").append(Integer.toHexString(i)).toString());
		if (weatherAdapterToken != 0) 
		{
			logger.d("loadForecast: obtaining weather_lock 3...");
			Object obj = providersLock;
			int j;
			Forecast aforecast[];
			logger.d("loadForecast: obtained weather_lock 3");
			j = getCityIdForProvider(i);
			if (j == 0x80000000)
				return;
			aforecast = weatherDataCache.getRawForecast(j);
			if (!isSuitableForMobileShell(aforecast))
				return;
			int ai[];
			if (aforecast != null && aforecast.length != 0)
				ai = encodeForecast(aforecast, DecimalDateTimeEncoding.getTodayDateEncoded());
			else
				ai = null;
			setForecast(i, ai);
			logger.d((new StringBuilder()).append("Notifying weather provider: 0x").append(Integer.toHexString(i)).toString());
			notifyWeatherUpdated(weatherAdapterToken, i, j);
			logger.d("loadForecast: released weather_lock 3");
			logger.d("loadForecast >>>");
			
		}
		else
		{
			logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
			logger.d("loadForecast <<<");
		}
		return;
	
	}

	public void loadUpdateStatus(int i)
	{
		logger.d((new StringBuilder()).append("loadUpdateStatus >>> provider=0x").append(Integer.toHexString(i)).toString());
		if (weatherAdapterToken != 0) 
		{
			Object obj = providersLock;
			boolean flag;
			int j = getCityIdForProvider(i);
			if (j == 0x80000000)
			{
				logger.d("loadUpdateStatus <<<");
				flag = false;
			}
			else 
			{
				UpdateStatus updatestatus = weatherDataCache.getUpdateStatus(j);
				if (updatestatus == null)
				{
					logger.d("loadUpdateStatus <<<");
					flag = false;
				}
				else 
				{
					
					if (updatestatus.getOverallStatus() != 2)
						return;
					flag = true;
				}
			}
		}
		else 
		{
			logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
			logger.d("loadUpdateStatus <<<");
		}
		return;

	}

	public void onCityNameUpdated(int i, String s)
	{
		logger.d((new StringBuilder()).append("onCityNameUpdated >>>  cityId=").append(i).append(" name=").append(s).toString());
		if (weatherAdapterToken != 0)
		{
			logger.d("onCityNameUpdated: obtaining weather_lock 3...");
			Object obj = providersLock;
		
			List list;
			logger.d("onCityNameUpdated: obtained weather_lock 3");
			list = (List)cityId2providers.get(i);
			if (list == null)
			{
				logger.w((new StringBuilder()).append("City not registered in adapter: cityId=").append(i).toString());
				logger.d("onCityNameUpdated <<<");
		
			}
			int j;
			for (Iterator iterator = list.iterator(); iterator.hasNext(); notifyWeatherUpdated(weatherAdapterToken, j, i))
			{
				j = ((Integer)iterator.next()).intValue();
				logger.d((new StringBuilder()).append("Invoking NativeCalls.setCityName: cityName=").append(s).toString());
				setCityName(weatherAdapterToken, j, s);
				logger.d((new StringBuilder()).append("Notifying weather provider: 0x").append(Integer.toHexString(j)).toString());
			}

			logger.d("onCityNameUpdated: released weather_lock 3");
			logger.d("onCityNameUpdated <<<");
		}
		else 
		{
			logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
			logger.d("onCityNameUpdated <<<");
		}
		return;
	}

	public void onCitySelectResult(int i, Intent intent)
	{
		int j = intent.getIntExtra("city_id", 0x80000000);
		if (j == 0x80000000)
			j = -1;
		logger.d((new StringBuilder()).append("onCitySelectResult: cityId=").append(j).toString());
		int k = intent.getIntExtra("weather-provider-token", 0);
		if (k != 0)
			onCitySelected(weatherAdapterToken, k, j);
	}

	protected void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		context = context1;
	}

	public void onCurrentUpdated(int i, CurrentConditions currentconditions)
	{
		logger.d((new StringBuilder()).append("onCurrentUpdated >>> cityId=").append(i).append(" curr=").append(currentconditions).toString());
		if (weatherAdapterToken != 0) 
		{
			logger.d("onCurrentUpdated: obtaining weather_lock 3...");
			Object obj = providersLock;
			List list;
			logger.d("onCurrentUpdated: obtained weather_lock 3");
			list = (List)cityId2providers.get(i);
			if (list == null)
			{
				logger.w((new StringBuilder()).append("City not registered in adapter: cityId=").append(i).toString());
				logger.d("onCurrentUpdated <<<");
			}
		}
		else 
		{
			logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
			logger.d("onCurrentUpdated <<<");
		}
		return;
	}

	public void onDetailedForecastUpdated(int i, Forecast aforecast[])
	{
	}

	public void onForecastUpdated(int i, Forecast aforecast[])
	{
	}

	public void onRawForecastUpdated(int i, Forecast aforecast[])
	{
		Logger logger1 = logger;
		int ai[];
		StringBuilder stringbuilder = (new StringBuilder()).append("onRawForecastUpdated >>> cityId=").append(i).append(" count=");
		int j;
		if (aforecast == null)
			j = 0;
		else
			j = aforecast.length;
		logger1.d(stringbuilder.append(j).append(" forecasts: ").append(toDatesString(aforecast)).toString());
		if (weatherAdapterToken != 0) 
			{
				Object obj = providersLock;

				List list;
				list = (List)cityId2providers.get(i);
				if (list != null)
					return;
				logger.w((new StringBuilder()).append("City not registered in adapter: cityId=").append(i).toString());
				logger.d("onRawForecastUpdated <<<");

				if (isSuitableForMobileShell(aforecast))
				{
					if (aforecast == null || aforecast.length == 0)
						return;
					ai = encodeForecast(aforecast, DecimalDateTimeEncoding.getTodayDateEncoded());
				
					int k;
					for (Iterator iterator = list.iterator(); iterator.hasNext(); notifyWeatherUpdated(weatherAdapterToken, k, i))
					{
						k = ((Integer)iterator.next()).intValue();
						setForecast(k, ai);
						logger.d((new StringBuilder()).append("Notifying weather provider: 0x").append(Integer.toHexString(k)).toString());
					}
					logger.d("onRawForecastUpdated <<<");
				}
				else 
				{
					logger.d("onRawForecastUpdated <<<");
				}
			}
		else 
			{
				logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
				logger.d("onRawForecastUpdated <<<");
			}
		return;
	}

	protected void onStart(int i)
	{
		weatherAdapterToken = i;
		weatherDataCache = WeatherDataCache.getInstance(context);
		weatherPrefs = new WeatherApplicationPreferences(context);
		currentLocationPrefs = new CurrentLocationPreferences(context);
	}

	protected void onStop()
	{
		logger.d("onStop: obtaining weather_lock 3...");
		synchronized (providersLock)
		{
			logger.d("onStop: obtained weather_lock 3");
			int i = provider2cityId.size();
			for (int j = 0; j < i; j++)
			{
				int k = provider2cityId.valueAt(j);
				weatherDataCache.unregisterWeatherListener(this, k);
			}

		}
		logger.d("onStop: released weather_lock 3");
		weatherAdapterToken = 0;
		weatherPrefs.dispose();
		currentLocationPrefs.dispose();
		return;
	}

	public void onUpdateStatusChanged(int i, UpdateStatus updatestatus)
	{
		logger.d((new StringBuilder()).append("onUpdateStatusChanged >>> cityId=").append(i).toString());
		if (updatestatus == null)
		{
			logger.d("onUpdateStatusChanged <<<");
		} else
		{
label0:
			{
				if (weatherAdapterToken != 0)
					break label0;
				logger.e("WeatherAdapter not initialized: weatherAdapterToken=0");
				logger.d("onUpdateStatusChanged <<<");
			}
		}
		Object obj = providersLock;

		List list;
		logger.d("onUpdateStatusChanged: obtained weather_lock 3");
		list = (List)cityId2providers.get(i);
		if (list == null)
		{
		logger.w((new StringBuilder()).append("City not registered in adapter: cityId=").append(i).toString());
		logger.d("onUpdateStatusChanged <<<");
		}
		
		long l;
		boolean flag;
		l = updatestatus.getLatestSuccessfulUpdateTimestamp();
		if (updatestatus.getOverallStatus() != 2)
			return;
		flag = true;

		int j;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); notifyWeatherUpdated(weatherAdapterToken, j, i))
		{
			j = ((Integer)iterator.next()).intValue();
			setUpdateStatus(weatherAdapterToken, j, l, flag);
			logger.d((new StringBuilder()).append("Notifying weather provider: 0x").append(Integer.toHexString(j)).toString());
		}
	}

	public void onWeatherProviderDeleted(int i)
	{
		logger.d((new StringBuilder()).append("GROUPER onWeatherProviderDeleted >>> weatherProviderToken=0x").append(Integer.toHexString(i)).append(i).toString());
		boolean flag = false;
		logger.d("onWeatherProviderDeleted : obtaining weather_lock 3...");
		int j;
		synchronized (providersLock)
		{
			logger.d("onWeatherProviderDeleted : obtained weather_lock 3");
			j = provider2cityId.get(i, 0x80000000);
			if (j != 0x80000000)
				flag = unbindProviderAndCity(i, j);
		}
		if (flag)
			stopListeningCityId(j);
		logger.d("onWeatherProviderDeleted : released weather_lock 3");
		logger.d("onWeatherProviderDeleted <<<");
		return;
	}

	public void openCitySelect(int i)
	{
		logger.d("openCitySelect");
		((Home)context).startSelectCityForWeather(weatherAdapterToken, i);
	}

	public void selectCity(int i, int j)
	{
		boolean flag;
		boolean flag1;
		logger.d((new StringBuilder()).append("GROUPER selectCity >>> weatherProviderToken=0x").append(Integer.toHexString(i)).append(" cityId=").append(j).toString());
		flag = false;
		flag1 = false;
		logger.d("selectCity: obtaining weather_lock 3...");
		int k;
		synchronized (providersLock)
		{
			logger.d("selectCity: obtained weather_lock 3");
			k = provider2cityId.get(i, 0x80000000);
			if (k != 0x80000000)
				flag = unbindProviderAndCity(i, k);
			if (j != 0x80000000)
				flag1 = bindProviderAndCity(i, j);
		}
		logger.d("selectCity: released weather_lock 3");
		if (flag)
			stopListeningCityId(k);
		if (flag1)
			startListeningCityId(j);
		logger.d("selectCity <<<");
		return;

	}

	public void setUpdateRate(int i)
	{
		logger.d((new StringBuilder()).append("setUpdateRate: updateRateNative=").append(i).toString());
		long l = nativeUpdateRateToAlarmManagerInterval(i);
		weatherPrefs.setUpdateIntervalMs(l);
		currentLocationPrefs.setUpdateIntervalMs(l);
	}

	public void setUseOnlyWifi(boolean flag)
	{
		logger.d((new StringBuilder()).append("setUseOnlyWifi: useOnlyWifi=").append(flag).toString());
		weatherPrefs.setUseOnlyWifi(flag);
		currentLocationPrefs.setUseOnlyWifi(flag);
		UpdateService.setUseOnlyWifiPreference(context, flag);
	}

	static 
	{
		logger = Loggers.getLogger(WeatherAdapterAndroid.class.getName());
		logger.enableThreadLog();
	}
}
