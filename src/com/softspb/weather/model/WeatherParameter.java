// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;

import java.util.Formatter;

import android.content.Context;
import android.content.res.TypedArray;

import com.softspb.weather.core.R;

// Referenced classes of package com.softspb.weather.model:
//			WeatherParameterValue

public abstract class WeatherParameter
{
	static class TemperatureParameter extends WeatherParameter
	{
		
		Number convert(Number number, int i, int j)
		{
			float f = number.floatValue();
			switch (i) {
			case 0:
				switch (j) {
				case 0:
					
					break;
				case 1:
					number = Integer.valueOf((int)(32F + (f * 9F) / 5F));
					break;
				case 2:
					number = Integer.valueOf((int)(f + 273.15F));
					break;
				default:
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported temperature units: ").append(j).toString());	
				
				}
				
				break;
			case 1:
				switch (j)
				{
				default:
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported temperature units: ").append(j).toString());

				case 0: // '\0'
					number = Integer.valueOf((int)((5F * (f - 32F)) / 9F));
					break;

				case 2: // '\002'
					number = Integer.valueOf((int)(273.15F + (5F * (f - 32F)) / 9F));
					break;

				case 1: // '\001'
					break;
				}
				break;
			case 2:
				switch (j)
				{
				default:
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported temperature units: ").append(j).toString());

				case 0: // '\0'
					number = Integer.valueOf((int)(f - 273.15F));
					break;

				case 1: // '\001'
					number = Integer.valueOf((int)(32F + (9F * (f - 273.15F)) / 5F));
					break;

				case 2: // '\002'
					break;
				}
				break;
			default:
				throw new IllegalArgumentException((new StringBuilder()).append("Unsupported temperature units: ").append(i).toString());
			
			}
			return number;
	
		}

		 Object convert(Object obj, int i, int j)
		{
			return convert((Number)obj, i, j);
		}

		TemperatureParameter(int i, int j)
		{
			super("temperature-weather-param", i, j);
		}

		TemperatureParameter(int i, int j, int k, int l, int i1, int j1, int k1, 
				int l1)
		{
			super("temperature-weather-param", i, j, k, l, i1, j1, k1, l1);
		}
	}


	public static final int DEFAULT_HUMIDITY_UNITS = 0;
	public static final int DEFAULT_PRESSURE_UNITS = 0;
	public static final int DEFAULT_TEMPERATURE_UNITS = 0;
	public static final int DEFAULT_WIND_DIRECTION_UNITS = 1;
	public static final int DEFAULT_WIND_SPEED_UNITS = 3;
	public static final WeatherParameter DEW_POINT;
	private static final double HPA_PER_ATM = 1013.25D;
	public static final int HUMIDITY_PERCENTS_UNITS = 0;
	private static final double IN_PER_ATM = 29.921299999999999D;
	private static final double KNOT_TO_KMPH = 1.8520000000000001D;
	private static final int MASK_INFLATED_STYLE = 0x80000000;
	private static final double MM_PER_ATM = 760.00099999999998D;
	private static final double MPH_TO_KMPH = 1.6093440000000001D;
	private static final double MPH_TO_MPS = 0.44703999999999999D;
	private static final double MPS_TO_KMPH = 3.6000000000000001D;
	public static final WeatherParameter PRESSURE;
	public static final int PRESSURE_ATM_UNITS = 2;
	public static final int PRESSURE_HPA_UNITS = 3;
	public static final int PRESSURE_IN_UNITS = 1;
	public static final int PRESSURE_MM_UNITS = 0;
	public static final WeatherParameter RELATIVE_HUMIDITY;
	public static final WeatherParameter TEMPERATURE;
	public static final int TEMPERATURE_CELSIUS_UNITS = 0;
	public static final int TEMPERATURE_FAHRENHEIT_UNITS = 1;
	public static final int TEMPERATURE_KELVIN_UNITS = 2;
	public static final WeatherParameter WIND_DIRECTION;
	public static final int WIND_DIRECTION_1_RHUMB_UNITS = 3;
	public static final int WIND_DIRECTION_2_RHUMBS_UNITS = 1;
	public static final int WIND_DIRECTION_4_RHUMBS_UNITS = 0;
	public static final int WIND_DIRECTION_DEGREES_UNITS = 2;
	public static final WeatherParameter WIND_SPEED;
	private static final double WIND_SPEED_CONVERSION_TABLE[][];
	public static final int WIND_SPEED_KMPH_UNITS = 3;
	public static final int WIND_SPEED_KNOTS_UNITS = 1;
	private static final int WIND_SPEED_MAX_UNIT = 3;
	private static final int WIND_SPEED_MIN_UNIT = 0;
	public static final int WIND_SPEED_MPH_UNITS = 0;
	public static final int WIND_SPEED_MPS_UNITS = 2;
	protected int defaultUnits;
	protected int formatsId;
	ThreadLocal formatter;
	private int inflatedFlag;
	protected int initialUnitsId;
	protected int largeIconId;
	private String name;
	protected int rangeFormatsId;
	protected int smallIconId;
	protected int styleId;
	protected int titleId;
	protected int unitValuesId;
	protected int unitsId;
	protected int unitsTitleId;

	WeatherParameter(String s, int i, int j)
	{
		inflatedFlag = 0;
		formatter = new ThreadLocal();
		name = s;
		styleId = i;
		defaultUnits = j;
	}

	WeatherParameter(String s, int i, int j, int k, int l, int i1, int j1, 
			int k1, int l1)
	{
		inflatedFlag = 0;
		formatter = new ThreadLocal();
		name = s;
		titleId = i;
		unitsTitleId = j;
		unitsId = k;
		unitValuesId = l;
		initialUnitsId = i1;
		formatsId = j1;
		rangeFormatsId = k1;
		defaultUnits = l1;
	}

	abstract Object convert(Object obj, int i, int j);

	protected void ensureInflatedStyle(Context context)
	{
		if ((0x80000000 & (-1 ^ inflatedFlag)) != 0 && styleId != 0)
		{
			TypedArray typedarray = context.obtainStyledAttributes(styleId, com.softspb.weather.core.R.styleable.WeatherParameter);
			titleId = typedarray.getResourceId(0, 0);
			unitsTitleId = typedarray.getResourceId(1, 0);
			unitsId = typedarray.getResourceId(2, 0);
			unitValuesId = typedarray.getResourceId(3, 0);
			formatsId = typedarray.getResourceId(4, 0);
			rangeFormatsId = typedarray.getResourceId(5, 0);
			smallIconId = typedarray.getResourceId(6, 0);
			largeIconId = typedarray.getResourceId(7, 0);
			initialUnitsId = typedarray.getResourceId(8, 0);
			typedarray.recycle();
			inflatedFlag = 0x80000000 | inflatedFlag;
		}
	}

	public String format(Object obj, int i, Context context)
	{
		ensureInflatedStyle(context);
		String s = context.getResources().getStringArray(formatsId)[i];
		Formatter formatter1 = (Formatter)formatter.get();
		if (formatter1 == null)
		{
			formatter1 = new Formatter();
			formatter.set(formatter1);
		}
		Object aobj[] = new Object[1];
		aobj[0] = obj;
		Appendable appendable = formatter1.format(s, aobj).out();
		String s1 = appendable.toString();
		((StringBuilder)appendable).setLength(0);
		return s1;
	}

	public String formatRange(WeatherParameterValue weatherparametervalue, WeatherParameterValue weatherparametervalue1, int i, Context context)
	{
		return formatRange(weatherparametervalue.getValue(i), weatherparametervalue1.getValue(i), i, context);
	}

	public String formatRange(Object obj, Object obj1, int i, Context context)
	{
		ensureInflatedStyle(context);
		String s = context.getResources().getStringArray(rangeFormatsId)[i];
		Formatter formatter1 = (Formatter)formatter.get();
		if (formatter1 == null)
		{
			formatter1 = new Formatter();
			formatter.set(formatter1);
		}
		Object aobj[] = new Object[2];
		aobj[0] = obj;
		aobj[1] = obj1;
		Appendable appendable = formatter1.format(s, aobj).out();
		String s1 = appendable.toString();
		((StringBuilder)appendable).setLength(0);
		return s1;
	}

	public int getDefaultUnits()
	{
		return defaultUnits;
	}

	public String getInitialUnits(Context context)
	{
		ensureInflatedStyle(context);
		return context.getResources().getString(initialUnitsId);
	}

	public int getLargeIconResId(Context context)
	{
		ensureInflatedStyle(context);
		return largeIconId;
	}

	public String getName()
	{
		return name;
	}

	public int getSmallIconResId(Context context)
	{
		ensureInflatedStyle(context);
		return smallIconId;
	}

	public String getTitle(Context context)
	{
		ensureInflatedStyle(context);
		return context.getString(titleId);
	}

	public String[] getUnitValues(Context context)
	{
		ensureInflatedStyle(context);
		return context.getResources().getStringArray(unitValuesId);
	}

	public String[] getUnits(Context context)
	{
		ensureInflatedStyle(context);
		return context.getResources().getStringArray(unitsId);
	}

	public String getUnitsTitle(Context context)
	{
		ensureInflatedStyle(context);
		return context.getString(unitsTitleId);
	}

	static 
	{
		TEMPERATURE = new TemperatureParameter(R.style.WeatherParameter_Temperature, 0);
		DEW_POINT = new TemperatureParameter(R.style.WeatherParameter_DewPoint, 0);
		PRESSURE = new WeatherParameter("pressure-weather-param", R.style.WeatherParameter_Pressure, 0) {

			Number convert(Number number, int i, int j)
			{
				double d;
				switch (i) {
				case 0:
					d = 0.0013157877423845496D;
					return d;
				case 1:
					d = 0.033421007777068509D;
					break;
				case 2:
					d = 1.0D;
					break;
				case 3:
					d = 0.00098692326671601278D;
					break;
				default:
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported pressure units: ").append(i).toString());
				
				}
				
				switch (j) {
				case 0:
					d *= 760.00099999999998D;
					break;
				case 1:
					d *= 29.921299999999999D;
					break;
				case 2:
					return Double.valueOf(d * number.doubleValue());
				
				case 3:
					d *= 1013.25D;
					break;
				default:
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported pressure units: ").append(j).toString());
				}		
				return d;
			}

			Object convert(Object obj, int i, int j)
			{
				return convert((Number)obj, i, j);
			}

		};
		WIND_SPEED = new WeatherParameter("weather-param-wind-speed", com.softspb.weather.core.R.style.WeatherParameter_WindSpeed, 3) {

			Number convert(Number number, int i, int j)
			{
				if (i < 0 || i > 3)
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported wind speed units: ").append(i).toString());
				if (j < 0 || j > 3)
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported wind speed units: ").append(i).toString());
				else
					return Double.valueOf(number.doubleValue() * WeatherParameter.WIND_SPEED_CONVERSION_TABLE[i][j]);
			}

			Object convert(Object obj, int i, int j)
			{
				return convert((Number)obj, i, j);
			}

		};
		RELATIVE_HUMIDITY = new WeatherParameter("weather-parameter-humidity", com.softspb.weather.core.R.style.WeatherParameter_RelativeHumidity, 0) {

			Number convert(Number number, int i, int j)
			{
				if (i != 0)
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported relative humidity units: ").append(i).toString());
				if (j != 0)
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported relative humidity units: ").append(j).toString());
				else
					return number;
			}

			Object convert(Object obj, int i, int j)
			{
				return convert((Number)obj, i, j);
			}

		};
		WIND_DIRECTION = new WeatherParameter("weather-parameter-wind-direction", com.softspb.weather.core.R.style.WeatherParameter_WindDirection, 1) {

			private boolean isAllowedUnits(int i)
			{
				boolean flag = true;
				if (i != 2 && i != 3 && i != 1 && i != 0)
					flag = false;
				return flag;
			}

			Number convert(Number number, int i, int j)
			{
				if (!isAllowedUnits(i))
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported relative humidity units: ").append(i).toString());
				if (!isAllowedUnits(j))
					throw new IllegalArgumentException((new StringBuilder()).append("Unsupported relative humidity units: ").append(j).toString());
				else
					return number;
			}

			Object convert(Object obj, int i, int j)
			{
				return convert((Number)obj, i, j);
			}

			public String format(Number number, int i, Context context)
			{
				String s ;
				double d = number.doubleValue();
				if (d != (0.0D / 0.0D))
				{
					int j;
					for (; d < 0.0D; d += 360D);
					for (; d >= 360D; d -= 360D);
					if (i == 2)
					{
						s = Double.toString(d);
					
					}
					j = 0;
					switch (i) {
					case 0:
						j = com.softspb.weather.core.R.array.weather_wind_directions;
						break;
					case 1:
						j = com.softspb.weather.core.R.array.weather_wind_directions_finer;
						break;
					case 2:
					
						break;
					case 3:
						j = com.softspb.weather.core.R.array.weather_wind_directions_marine;
						break;
					default:
						
						break; /* Loop/switch isn't completed */
					}
					String as[];
					int l;
					as = context.getResources().getStringArray(j);
					int k = as.length;
					for (l = (int)Math.round(d / (360D / (double)k)); l >= k; l -= k);
					
					s = as[l];
				}
				else 
				{
					s = context.getString(com.softspb.weather.core.R.string.weather_wind_direction_variable);
				}
				
				ensureInflatedStyle(context);
				String s1 = context.getResources().getStringArray(formatsId)[i];
				Formatter formatter1 = (Formatter)formatter.get();
				if (formatter1 == null)
				{
					formatter1 = new Formatter();
					formatter.set(formatter1);
				}
				Object aobj[] = new Object[1];
				aobj[0] = s;
				Appendable appendable = formatter1.format(s1, aobj).out();
				String s2 = appendable.toString();
				((StringBuilder)appendable).setLength(0);
				return s2;
			}

			public  String format(Object obj, int i, Context context)
			{
				return format((Number)obj, i, context);
			}

		};
		double ad[][] = new double[4][];
		double ad1[] = new double[4];
		ad1[0] = 1.0D;
		ad1[1] = 0.86897624190064793D;
		ad1[2] = 0.44703999999999999D;
		ad1[3] = 1.6093440000000001D;
		ad[0] = ad1;
		double ad2[] = new double[4];
		ad2[0] = 1.1507794480235425D;
		ad2[1] = 1.0D;
		ad2[2] = 0.51444444444444448D;
		ad2[3] = 1.8520000000000001D;
		ad[1] = ad2;
		double ad3[] = new double[4];
		ad3[0] = 2.2369362920544025D;
		ad3[1] = 1.9438444924406046D;
		ad3[2] = 1.0D;
		ad3[3] = 3.6000000000000001D;
		ad[2] = ad3;
		double ad4[] = new double[4];
		ad4[0] = 0.62137119223733395D;
		ad4[1] = 0.5399568034557235D;
		ad4[2] = 0.27777777777777779D;
		ad4[3] = 1.0D;
		ad[3] = ad4;
		WIND_SPEED_CONVERSION_TABLE = ad;
	}

}
