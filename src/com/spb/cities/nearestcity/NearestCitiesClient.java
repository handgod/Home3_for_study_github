// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.nearestcity;

import android.content.Context;
import android.location.Location;
import com.softspb.util.log.Logger;
import java.io.*;
import java.util.ArrayList;
import org.apache.http.client.HttpClient;

// Referenced classes of package com.spb.cities.nearestcity:
//			DownloadClient, ClientToken

public class NearestCitiesClient extends DownloadClient
{
	public static class ResponseItem
	{

		int cityId;
		String cityName;

		public int getCityId()
		{
			return cityId;
		}

		public String getCityName()
		{
			return cityName;
		}

		public ResponseItem(int i, String s)
		{
			cityId = i;
			cityName = s;
		}
	}

	public static class QueryParams
	{

		String lat;
		String limit;
		String lon;

		public String getLat()
		{
			return lat;
		}

		public String getLimit()
		{
			return limit;
		}

		public String getLon()
		{
			return lon;
		}

		public QueryParams(Location location, int i)
		{
			this(Double.toString(location.getLongitude()), Double.toString(location.getLatitude()), Integer.toString(i));
		}

		public QueryParams(String s, String s1, String s2)
		{
			lon = s;
			lat = s1;
			limit = s2;
		}
	}


	private static final String NEAREST_CITIES_SERVER_URLS[];
	private String clientToken;

	public NearestCitiesClient(Context context, HttpClient httpclient)
	{
		super(NEAREST_CITIES_SERVER_URLS);
		clientToken = ClientToken.getInstance(context).getToken();
	}

	protected String createUrl(String s, Object obj)
	{
		QueryParams queryparams = (QueryParams)obj;
		return (new StringBuilder()).append(s).append("?lat=").append(queryparams.lat).append("&lon=").append(queryparams.lon).append("&count=").append(queryparams.limit).append('&').append("client_token").append('=').append(clientToken).toString();
	}

	protected Object parseResponse(InputStream inputstream, Object obj)
		throws Exception
	{
		logger.d("parseResponse");
		BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
		ArrayList arraylist = new ArrayList();
		do
		{
			String s = bufferedreader.readLine();
			if (s != null)
			{
				logger.d((new StringBuilder()).append("Received line: ").append(s).toString());
				int i = s.indexOf(',');
				int j = Integer.parseInt(s.substring(0, i));
				String s1 = s.substring(i + 1).trim();
				logger.d((new StringBuilder()).append("Adding response item: cityId=").append(j).append(" cityName=").append(s1).toString());
				arraylist.add(new ResponseItem(j, s1));
			} else
			{
				return arraylist;
			}
		} while (true);
	}

	static 
	{
		String as[] = new String[1];
		as[0] = "http://weather.trv.softspb.com/current_conditions/get_nearest_cities.php";
		NEAREST_CITIES_SERVER_URLS = as;
	}
}
