// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.pick;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

// Referenced classes of package com.spb.cities.pick:
//			AutoCompletionAdapter

public class NearestAutoCompletionAdapter extends AutoCompletionAdapter
{

	public static final long MORE_CITIES_ID = 0x7738a208L;
	com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem cities[];
	Context context;
	boolean showMoreCities;

	public NearestAutoCompletionAdapter(Context context1, com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem aresponseitem[], int i, boolean flag)
	{
		super(context1, toCursor(context1, aresponseitem, flag), i);
		context = context1;
		cities = aresponseitem;
		showMoreCities = flag;
	}

	private static MatrixCursor toCursor(Context context1, com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem aresponseitem[], boolean flag)
	{
		MatrixCursor matrixcursor = new MatrixCursor(com.spb.cities.provider.CitiesContract.Cities.DEFAULT_PROJECTION);
		int i = aresponseitem.length;
		for (int j = 0; j < i; j++)
		{
			com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem responseitem = aresponseitem[j];
			Integer integer = Integer.valueOf(responseitem.getCityId());
			Object aobj1[] = new Object[5];
			aobj1[0] = integer;
			aobj1[1] = integer;
			aobj1[2] = responseitem.getCityName();
			aobj1[3] = null;
			aobj1[4] = null;
			matrixcursor.addRow(aobj1);
		}

		if (flag)
		{
			Object aobj[] = new Object[5];
			aobj[0] = Long.valueOf(0x7738a208L);
			aobj[1] = Long.valueOf(0x7738a208L);
			aobj[2] = context1.getString(com.spb.cities.R.string.weather_more_cities);
			aobj[3] = null;
			aobj[4] = null;
			matrixcursor.addRow(aobj);
		}
		return matrixcursor;
	}

//	public  CharSequence convertToString(Cursor cursor)
//	{
//		return convertToString(cursor);
//	}

	public String convertToString(Cursor cursor)
	{
		return cursor.getString(2);
	}

	public Cursor runQueryOnBackgroundThread(CharSequence charsequence)
	{
		MatrixCursor matrixcursor = new MatrixCursor(com.spb.cities.provider.CitiesContract.Cities.DEFAULT_PROJECTION);
		String s = charsequence.toString().toUpperCase();
		com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem aresponseitem[] = cities;
		int i = aresponseitem.length;
		for (int j = 0; j < i; j++)
		{
			com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem responseitem = aresponseitem[j];
			String s1 = responseitem.getCityName();
			if (s1.toUpperCase().startsWith(s))
			{
				Integer integer = Integer.valueOf(responseitem.getCityId());
				Object aobj1[] = new Object[5];
				aobj1[0] = integer;
				aobj1[1] = integer;
				aobj1[2] = s1;
				aobj1[3] = null;
				aobj1[4] = null;
				matrixcursor.addRow(aobj1);
			}
		}

		if (showMoreCities)
		{
			Object aobj[] = new Object[5];
			aobj[0] = Long.valueOf(0x7738a208L);
			aobj[1] = Long.valueOf(0x7738a208L);
			aobj[2] = context.getString(com.spb.cities.R.string.weather_more_cities);
			aobj[3] = null;
			aobj[4] = null;
			matrixcursor.addRow(aobj);
		}
		return matrixcursor;
	}
}
