// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.updateservice.spb;

import android.content.Context;
import com.softspb.updateservice.DownloadClient;
import com.softspb.util.DecimalDateTimeEncoding;
import com.softspb.weather.model.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;

// Referenced classes of package com.softspb.weather.updateservice.spb:
//			ClientToken

public class GismeteoClient extends DownloadClient
{

	public static final int GISMETEO_DATA_VERSION = 1;
	private static final String SERVER_URLS[];
	private String clientToken;

	public GismeteoClient(Context context)
	{
		super(SERVER_URLS);
		clientToken = ClientToken.getInstance(context).getToken();
	}

	static Forecast decodeForecastDataItem(int i, InputStream inputstream, byte byte0)
		throws IOException
	{
		ForecastBuilder forecastbuilder;
		int j;
		GregorianCalendar gregoriancalendar;
		int k;
		forecastbuilder = new ForecastBuilder();
		forecastbuilder.withCityId(i);
		j = 0;
		gregoriancalendar = new GregorianCalendar();
		gregoriancalendar.setTimeInMillis(0L);
		k = 0;
		int i1;
		if (k >= 20)
			return forecastbuilder.build();
		int l = inputstream.read();
		if (l == -1)
			throw new IOException((new StringBuilder()).append("Invalid format of Gismeteo data item: enexpected EOF at offset ").append(k).toString());
		i1 = l ^ byte0 & 0xff;
		
		switch (k) {
		case 0:
			
			break;
		case 1:
			
			break;
		case 2:
			gregoriancalendar.set(2, i1 - 1);
			
			break;
		case 3:
			j = i1;
		
			break;
		case 4:
			j |= i1 << 8;
			gregoriancalendar.set(1, j);
		
			break;
		case 5:
			gregoriancalendar.set(11, i1);
			java.util.Date date = gregoriancalendar.getTime();
			forecastbuilder.withDateLocal(DecimalDateTimeEncoding.encodeDate(date));
			forecastbuilder.withTimeLocal(DecimalDateTimeEncoding.encodeTime(date));
		
			break;
		case 6:
			forecastbuilder.withCloudiness(i1);
			
			break;
		case 7:
			forecastbuilder.withPrecipitation(i1);
			
			break;
		case 8:
			j = i1;
			break;
		case 9:
			j |= i1 << 8;
			forecastbuilder.withMaxPressMm(j);
			break;
		case 10:
			j = i1;
			break;
		case 11:
			j |= i1 << 8;
			forecastbuilder.withMinPressMm(j);
			break;
		case 12:
			forecastbuilder.withMaxTempC((byte)i1);
			break;
		case 13:
			forecastbuilder.withMinTempC((byte)i1);
			break;
		case 14:
			forecastbuilder.withMaxWindSpeedMps(i1);
			break;
		case 15:
			forecastbuilder.withMinWindSpeedMps(i1);
			break;
		case 16:
			forecastbuilder.withMaxHumidityPercents(i1);
			break;
		case 17:
			forecastbuilder.withMinHumidityPercents(i1);
			break;
		case 18:
			forecastbuilder.withMaxHeatIndexC((byte)i1);
			break;
		case 19:
		
		default:
			return forecastbuilder.build();

		}

		return forecastbuilder.build();
	}

	 private static ForecastArray decodeGismeteoPacket(int paramInt, InputStream paramInputStream)
	    throws IOException
	  {
	    int i = 0;
	    byte j = 0;
	    int k = paramInputStream.read();
	    if (k == -1)
	      throw new IOException("Invalid format of Gismeteo data packet: unexpected EOF.");
	    if (k != 1)
	    {
	      String str1 = "Unsupported version of Gismeteo format: " + k;
	      throw new IOException(str1);
	    }
	    int m = 1;
	    if (m < 6)
	    {
	      int n = paramInputStream.read();
	      if (n == -1)
	      {
	        String str2 = "Invalid format of Gismeteo data packet: unexpected EOF at offset " + m;
	        throw new IOException(str2);
	      }
	      switch (m)
	      {
	      case 1:
	    	  j = (byte)n;
	      case 2:
	    	  i = n;
	      default:
	    	  m += 1;
	      }
	    }
	    ForecastArray localForecastArray = new ForecastArray(paramInt, i);
	    int i1 = 0;
	    while (i1 < i)
	    {
	      Forecast localForecast = decodeForecastDataItem(paramInt, paramInputStream, j);
	      localForecastArray.setItem(localForecast, i1);
	      i1 += 1;
	    }
	    return localForecastArray;
	  }

	protected String createUrl(String s, Integer integer)
	{
		return (new StringBuilder()).append(s).append("?id=").append(integer).append('&').append("client_token").append('=').append(clientToken).toString();
	}

	protected  String createUrl(String s, Object obj)
	{
		return createUrl(s, (Integer)obj);
	}

	protected ForecastArray parseResponse(InputStream inputstream, Integer integer)
		throws IOException
	{
		return decodeGismeteoPacket(integer.intValue(), inputstream);
	}

	protected  Object parseResponse(InputStream inputstream, Object obj)
		throws Exception
	{
		return parseResponse(inputstream, (Integer)obj);
	}

	static 
	{
		String as[] = new String[4];
		as[0] = "http://www.spbtraveler.com/weather/spb_index.php";
		as[1] = "http://www.spbtraveler1.com/weather/spb_index.php";
		as[2] = "http://www.spbtraveler2.com/weather/spb_index.php";
		as[3] = "http://www.spbtraveler3.com/weather/spb_index.php";
		SERVER_URLS = as;
	}
}
