// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.updateservice.spb;

import android.content.Context;
import com.softspb.updateservice.DownloadClient;
import com.softspb.weather.model.CurrentConditions;
import com.softspb.weather.model.CurrentConditionsBuilder;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.*;

// Referenced classes of package com.softspb.weather.updateservice.spb:
//			ClientToken

public class CurrentClient extends DownloadClient
{

	private static final String ATTR_VERSION = "version";
	private static final String SERVER_URLS[];
	private static final String TAG_CURRENT_OBSERVATION = "current_observation";
	private static final String TAG_DATA_SOURCE = "data_source";
	private static final String TAG_DEWPOINT_C = "dewpoint_c";
	private static final String TAG_DEWPOINT_F = "dewpoint_f";
	private static final String TAG_HEAT_INDEX_C = "heat_index_c";
	private static final String TAG_HEAT_INDEX_F = "heat_index_f";
	private static final String TAG_HUMIDEX_C = "humidex_c";
	private static final String TAG_HUMIDEX_F = "humidex_f";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_METAR = "metar";
	private static final String TAG_OBSERVATION_TIME = "observation_time";
	private static final String TAG_OBSERVATION_TIME_RFC822 = "observation_time_rfc822";
	private static final String TAG_PRESSURE_ATM = "pressure_atm";
	private static final String TAG_PRESSURE_HPA = "pressure_hpa";
	private static final String TAG_PRESSURE_IN = "pressure_in";
	private static final String TAG_PRESSURE_MM = "pressure_mm";
	private static final String TAG_RELATIVE_HUMIDITY = "relative_humidity";
	private static final String TAG_SKY_ICON = "sky_icon";
	private static final String TAG_STATION_ID = "station_id";
	private static final String TAG_TEMP_C = "temp_c";
	private static final String TAG_TEMP_F = "temp_f";
	private static final String TAG_VISIBILITY_FT = "visibility_ft";
	private static final String TAG_VISIBILITY_KM = "visibility_km";
	private static final String TAG_VISIBILITY_METER = "visibility_meter";
	private static final String TAG_VISIBILITY_MI = "visibility_mi";
	private static final String TAG_WEATHER = "weather";
	private static final String TAG_WINDCHILL_C = "windchill_c";
	private static final String TAG_WINDCHILL_F = "windchill_f";
	private static final String TAG_WIND_DEGREES = "wind_degrees";
	private static final String TAG_WIND_KNOTS = "wind_knots";
	private static final String TAG_WIND_KPH = "wind_kph";
	private static final String TAG_WIND_MPH = "wind_mph";
	private static final String TAG_WIND_MPS = "wind_mps";
	private static final String VERSION_1_0 = "1.0";
	private final String clientToken;
	private XmlPullParserFactory xmlPullParserFactory;

	public CurrentClient(Context context)
	{
		super(SERVER_URLS);
		try
		{
			xmlPullParserFactory = XmlPullParserFactory.newInstance();
			xmlPullParserFactory.setNamespaceAware(false);
		}
		catch (XmlPullParserException xmlpullparserexception)
		{
			throw new RuntimeException((new StringBuilder()).append("Failed to initialize CurrentClient: ").append(xmlpullparserexception).toString(), xmlpullparserexception);
		}
		clientToken = ClientToken.getInstance(context).getToken();
	}

	protected String createUrl(String s, Integer integer)
	{
		return (new StringBuilder()).append(s).append("cur").append(integer).append(".xml").append('?').append("client_token").append('=').append(clientToken).toString();
	}

	protected String createUrl(String s, Object obj)
	{
		return createUrl(s, (Integer)obj);
	}


	  protected CurrentConditions parseResponse(InputStream paramInputStream, Integer paramInteger)
	    throws XmlPullParserException, IOException
	  {
	    Object localObject = null;
	    XmlPullParser localXmlPullParser = this.xmlPullParserFactory.newPullParser();
	    localXmlPullParser.setInput(paramInputStream, (String)localObject);
	    CurrentConditionsBuilder localCurrentConditionsBuilder1 = null;
	    int i = localXmlPullParser.getEventType();
	    String str5 = null;
	    if (i != 1)
	    {
	      switch (i)
	      {
		      default:
		      case 2:
		      case 4:
		        while (localXmlPullParser.next() !=0)
		        {
	
		          String str1 = localXmlPullParser.getName();
		          if ("current_observation".equals(str1))
		          {
		            String str2 = localXmlPullParser.getAttributeValue(null, "version");
		            if (!"1.0".equals(str2))
		            {
		              String str3 = "Unsupported current data version: " + str2;
		              throw new RuntimeException(str3);
		            }
		            CurrentConditionsBuilder localCurrentConditionsBuilder2 = new CurrentConditionsBuilder();
		            int j = paramInteger.intValue();
		            localCurrentConditionsBuilder1 = localCurrentConditionsBuilder2.withCityId(j);
		          }
	
		        }
		      case 3:
		      }
		      str5 = "".toString().trim();
		      if ("current_observation".equals(null))
		        if (localCurrentConditionsBuilder1 != null);
	    }
	    while (true)
	    {
	     
	      if ((localCurrentConditionsBuilder1 == null) || (str5.length() == 0))
	        break;
	      if ("data_source".equals(null))
	      {
	        CurrentConditionsBuilder localCurrentConditionsBuilder3 = localCurrentConditionsBuilder1.withDataSource(str5);
	        break;
	      }
	      if ("location".equals(null))
	      {
	        CurrentConditionsBuilder localCurrentConditionsBuilder4 = localCurrentConditionsBuilder1.withLocation(str5);
	        break;
	      }
	      if ("station_id".equals(null))
	      {
	        CurrentConditionsBuilder localCurrentConditionsBuilder5 = localCurrentConditionsBuilder1.withAirportICAOCode(str5);
	        break;
	      }
	      if ("latitude".equals(null))
	      {
	        CurrentConditionsBuilder localCurrentConditionsBuilder6 = localCurrentConditionsBuilder1.withLatitude(str5);
	        break;
	      }
	      if ("longitude".equals(null))
	      {
	        CurrentConditionsBuilder localCurrentConditionsBuilder7 = localCurrentConditionsBuilder1.withLongitude(str5);
	        break;
	      }
	      if ("observation_time_rfc822".equals(null))
	        break;
	      if ("observation_time".equals(null))
	      {
	        String[] arrayOfString = str5.split(" ");
	        if (arrayOfString.length != 2)
	          throw new RuntimeException("Unrecognized time format");
	        int k = Integer.parseInt(arrayOfString[0]);
	        CurrentConditionsBuilder localCurrentConditionsBuilder8 = localCurrentConditionsBuilder1.withDateUTC(k);
	        int m = Integer.parseInt(arrayOfString[1]);
	        CurrentConditionsBuilder localCurrentConditionsBuilder9 = localCurrentConditionsBuilder1.withTimeUTC(m);
	        break;
	      }
	      if ("metar".equals(null))
	      {
	        CurrentConditionsBuilder localCurrentConditionsBuilder10 = localCurrentConditionsBuilder1.withMetar(str5);
	        break;
	      }
	      if ("temp_f".equals(null))
	      {
	        int n = Integer.parseInt(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder11 = localCurrentConditionsBuilder1.withTempF(n);
	        break;
	      }
	      if ("temp_c".equals(null))
	      {
	        int i1 = Integer.parseInt(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder12 = localCurrentConditionsBuilder1.withTempC(i1);
	        break;
	      }
	      if ("relative_humidity".equals(null))
	      {
	        float f1 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder13 = localCurrentConditionsBuilder1.withRelativeHumidityPercents(f1);
	        break;
	      }
	      if ("wind_degrees".equals(null))
	      {
	        CurrentConditionsBuilder localCurrentConditionsBuilder14 = localCurrentConditionsBuilder1.withWindDirDegrees(str5);
	        break;
	      }
	      if ("wind_mph".equals(null))
	      {
	        float f2 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder15 = localCurrentConditionsBuilder1.withWindSpeedMph(f2);
	        break;
	      }
	      if ("wind_knots".equals(null))
	      {
	        float f3 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder16 = localCurrentConditionsBuilder1.withWindSpeedKnots(f3);
	        break;
	      }
	      if ("wind_mps".equals(null))
	      {
	        float f4 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder17 = localCurrentConditionsBuilder1.withWindSpeedMps(f4);
	        break;
	      }
	      if ("wind_kph".equals(null))
	      {
	        float f5 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder18 = localCurrentConditionsBuilder1.withWindSpeedKmph(f5);
	        break;
	      }
	      if ("pressure_in".equals(null))
	      {
	        float f6 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder19 = localCurrentConditionsBuilder1.withPressureIn(f6);
	        break;
	      }
	      if ("pressure_mm".equals(null))
	      {
	        float f7 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder20 = localCurrentConditionsBuilder1.withPressureMm(f7);
	        break;
	      }
	      if ("pressure_hpa".equals(null))
	      {
	        float f8 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder21 = localCurrentConditionsBuilder1.withPressureHpa(f8);
	        break;
	      }
	      if ("pressure_atm".equals(null))
	      {
	        float f9 = Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder22 = localCurrentConditionsBuilder1.withPressureAtm(f9);
	        break;
	      }
	      if ("dewpoint_f".equals(null))
	      {
	        int i2 = Integer.parseInt(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder23 = localCurrentConditionsBuilder1.withDewPointF(i2);
	        break;
	      }
	      if ("dewpoint_c".equals(null))
	      {
	        int i3 = Integer.parseInt(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder24 = localCurrentConditionsBuilder1.withDewPointC(i3);
	        break;
	      }
	      if ("heat_index_f".equals(null))
	      {
	        int i4 = Integer.parseInt(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder25 = localCurrentConditionsBuilder1.withHeatIndexF(i4);
	        break;
	      }
	      if ("heat_index_c".equals(null))
	      {
	        int i5 = Integer.parseInt(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder26 = localCurrentConditionsBuilder1.withHeatIndexC(i5);
	        break;
	      }
	      if ("windchill_f".equals(null))
	      {
	        int i6 = Integer.parseInt(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder27 = localCurrentConditionsBuilder1.withWindChillF(i6);
	        break;
	      }
	      if ("windchill_c".equals(null))
	      {
	        int i7 = Integer.parseInt(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder28 = localCurrentConditionsBuilder1.withWindChillC(i7);
	        break;
	      }
	      if (("visibility_mi".equals(null)) || ("visibility_ft".equals(null)) || ("visibility_km".equals(null)) || ("visibility_meter".equals(null)))
	        break;
	      if ("humidex_f".equals(null))
	      {
	        int i8 = (int)Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder29 = localCurrentConditionsBuilder1.withHumidexF(i8);
	        break;
	      }
	      if ("humidex_c".equals(null))
	      {
	        int i9 = (int)Float.parseFloat(str5);
	        CurrentConditionsBuilder localCurrentConditionsBuilder30 = localCurrentConditionsBuilder1.withHumidexC(i9);
	        break;
	      }
	      if ("weather".equals(null))
	      {
	        CurrentConditionsBuilder localCurrentConditionsBuilder31 = localCurrentConditionsBuilder1.withWeather(str5);
	        break;
	      }
	      if (!"sky_icon".equals(null))
	        break;
	      int i10 = Integer.parseInt(str5);
	     
	      if (localCurrentConditionsBuilder1 == null)
	        continue;
	      localObject = localCurrentConditionsBuilder1.build();
	    }
	    return (CurrentConditions) localObject;

	  }



	protected Object parseResponse(InputStream inputstream, Object obj)
		throws Exception
	{
		return parseResponse(inputstream, (Integer)obj);
	}

	static 
	{
		String as[] = new String[4];
		as[0] = "http://www.spbtraveler.com/weather/data/current/";
		as[1] = "http://www.spbtraveler1.com/weather/data/current/";
		as[2] = "http://www.spbtraveler2.com/weather/data/current/";
		as[3] = "http://www.spbtraveler3.com/weather/data/current/";
		SERVER_URLS = as;
	}
}
