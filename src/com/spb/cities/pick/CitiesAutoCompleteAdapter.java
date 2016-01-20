// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.pick;

import android.content.ContentResolver;
import android.content.Context;
import android.database.*;
import android.net.Uri;
import android.widget.FilterQueryProvider;
import com.softspb.util.log.Logger;

// Referenced classes of package com.spb.cities.pick:
//			AutoCompletionAdapter

public class CitiesAutoCompleteAdapter extends AutoCompletionAdapter
{

	public static final long CURRENT_LOCATION = 0x3b9c5168L;
	public static final long ENABLE_LOCATION = 0x3b9c51ccL;
	private static final int JP_CHARACTER_LOWER_BOUND = 1000;
	public static final long LOCATE_NEAREST = 0x3b9c5104L;
	Uri contentUri;
	String filteredColumn;
	Context mContext;
	String projection[];
	boolean showCurrentLocation;
	boolean showEnableLocation;
	boolean showLocateNearest;

	public CitiesAutoCompleteAdapter(Context context, int i, Uri uri, String as[], String s, boolean flag)
	{
		super(context, createDefaultRows(context, flag, flag, flag), i);
		boolean flag1 = true;
		boolean flag2;
		if (!flag)
			flag2 = flag1;
		else
			flag2 = false;
		contentUri = uri;
		projection = as;
		filteredColumn = s;
		showLocateNearest = flag;
		showCurrentLocation = flag;
		if (flag)
			flag1 = false;
		showEnableLocation = flag1;
		mContext = context;
	}

	private static void addDummyRow(Context context, MatrixCursor matrixcursor, int i, long l)
	{
		Object aobj[] = new Object[5];
		aobj[0] = Long.valueOf(l);
		aobj[1] = Integer.valueOf(0);
		aobj[2] = context.getString(i);
		aobj[3] = null;
		aobj[4] = null;
		matrixcursor.addRow(aobj);
	}

	private boolean constraintEnoughForFiltering(CharSequence charsequence)
	{
		boolean flag = false;
		if (charsequence.length() >= threshold || charsequence.length() > 0 && charsequence.charAt(0) > '\u03E8')
			flag = true;
		return flag;
	}

	private static MatrixCursor createDefaultRows(Context context, boolean flag, boolean flag1, boolean flag2)
	{
		MatrixCursor matrixcursor = new MatrixCursor(com.spb.cities.provider.CitiesContract.Cities.DEFAULT_PROJECTION);
		if (flag)
			addDummyRow(context, matrixcursor, com.spb.cities.R.string.weather_locate_nearest_city, 0x3b9c5104L);
		if (flag1)
			addDummyRow(context, matrixcursor, com.spb.cities.R.string.weather_current_location, 0x3b9c5168L);
		if (flag2)
			addDummyRow(context, matrixcursor, com.spb.cities.R.string.weather_enable_location, 0x3b9c51ccL);
		return matrixcursor;
	}

//	public CharSequence convertToString(Cursor cursor)
//	{
//		return convertToString(cursor);
//	}

	public String convertToString(Cursor cursor)
	{
		return cursor.getString(2);
	}

	public Cursor runQueryOnBackgroundThread(CharSequence charsequence)
	{
		Cursor cursor;
		logd("runQueryOnBackgroundThread");
		cursor = null;
		if (charsequence == null || !constraintEnoughForFiltering(charsequence)) 
		{
			Object obj = createDefaultRows(mContext, showLocateNearest, showCurrentLocation, showEnableLocation);
			return ((Cursor) (obj));
		}
		else 
		{
			long l;
			Uri uri;
			if (getFilterQueryProvider() != null)
				cursor = getFilterQueryProvider().runQuery(charsequence);
			l = System.currentTimeMillis();
			logd((new StringBuilder()).append("Querying for cities name: constraint=").append(charsequence).toString());
			uri = Uri.withAppendedPath(com.spb.cities.provider.CitiesContract.Cities.getContentFilterUri(mContext), charsequence.toString());
			Cursor cursor1 = mContentResolver.query(uri, projection, null, null, null);
			cursor = cursor1;
		}
		return cursor;
	}

	public void setLocationPossible(boolean flag)
	{
		showCurrentLocation = flag;
		boolean flag1;
		if (!flag)
			flag1 = true;
		else
			flag1 = false;
		showEnableLocation = flag1;
		showLocateNearest = flag;
	}
}
