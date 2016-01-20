// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;

import com.softspb.util.DecimalDateTimeEncoding;
import java.util.Date;
import java.util.Random;

// Referenced classes of package com.softspb.weather.model:
//			Forecast

public class ForecastArray
{

	private static Random random;
	final int cityId;
	final Forecast items[];

	public ForecastArray(int i, int j)
	{
		cityId = i;
		items = new Forecast[j];
	}

	public static ForecastArray generateRandom(int i, int j, int k)
	{
		return generateRandom(i, j, k, false);
	}

	public static ForecastArray generateRandom(int i, int j, int k, boolean flag)
	{
		if (random == null)
			random = new Random(System.currentTimeMillis());
		ForecastArray forecastarray = new ForecastArray(j, i);
		int l = 1;
		for (int i1 = 0; i1 < forecastarray.size(); i1++)
		{
			Date date = DecimalDateTimeEncoding.decodeDate(k);
			date.setHours(3 + 6 * (i1 % 4));
			Forecast forecast = Forecast.generateRandom(j, date);
			if (flag)
			{
				forecast.timeLocal = l;
				if (++l > 4)
					l = 1;
			}
			forecastarray.setItem(forecast, i1);
			if (i1 % 4 == 3)
				k = DecimalDateTimeEncoding.add(k, 1);
		}

		return forecastarray;
	}

	public int getCityID()
	{
		return cityId;
	}

	public Forecast getItem(int i)
	{
		if (items == null)
			throw new IndexOutOfBoundsException("Data not available.");
		else
			return items[i];
	}

	public Forecast[] getItems()
	{
		return items;
	}

	public void setItem(Forecast forecast, int i)
	{
		int j = forecast.getCityId();
		if (cityId != j)
		{
			throw new IllegalArgumentException((new StringBuilder()).append("Unexpected cityId: ").append(j).append(", expected ").append(cityId).toString());
		} else
		{
			items[i] = forecast;
			return;
		}
	}

	public int size()
	{
		int i;
		if (items == null)
			i = 0;
		else
			i = items.length;
		return i;
	}
}
