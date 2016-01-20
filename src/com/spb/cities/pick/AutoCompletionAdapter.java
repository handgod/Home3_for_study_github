// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.pick;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.view.*;
import android.widget.*;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.spb.cities.pick:
//			CitySelectionActivity, CursorFilter

public abstract class AutoCompletionAdapter extends CursorAdapter
	implements Filterable, CursorFilter.CursorFilterClient
{

	protected static final Logger logger = Loggers.getLogger(CitySelectionActivity.class.getName());
	protected ContentResolver mContentResolver;
	protected CursorFilter mCursorFilter;
	protected int threshold;

	public AutoCompletionAdapter(Context context, Cursor cursor, int i)
	{
		super(context, cursor, false);
		mContentResolver = context.getContentResolver();
		threshold = i;
	}

	protected static String getCountryText(Cursor cursor)
	{
		String s = null;
		String s1 = null;
		int i = cursor.getColumnIndex("country_short_name");
		if (i != -1)
			s = cursor.getString(i);
		int j = cursor.getColumnIndex("state_short_name");
		if (j != -1)
			s1 = cursor.getString(j);
		StringBuilder stringbuilder = new StringBuilder();
		if (s != null)
		{
			stringbuilder.append(s);
			if (s1 != null)
				stringbuilder.append(" (").append(s1).append(')');
		}
		return stringbuilder.toString();
	}

	public void bindView(View view, Context context, Cursor cursor)
	{
		TextView textview = (TextView)view.findViewById(com.spb.cities.R.id.weather_city_dropdown_item_name);
		TextView textview1 = (TextView)view.findViewById(com.spb.cities.R.id.weather_city_dropdown_item_country);
		textview.setText(cursor.getString(2));
		textview1.setText(getCountryText(cursor));
	}

	public Filter getFilter()
	{
		if (mCursorFilter == null)
			mCursorFilter = new CursorFilter(this);
		return mCursorFilter;
	}

	protected void logd(String s)
	{
		long l = Thread.currentThread().getId();
		StringBuilder stringbuilder = new StringBuilder();
		String s1 = Long.toString(l);
		stringbuilder.append('(');
		int i = s1.length();
		do
		{
			int j = i + 1;
			if (i < 4)
			{
				stringbuilder.append(' ');
				i = j;
			} else
			{
				stringbuilder.append(s1);
				stringbuilder.append(") ");
				stringbuilder.append(s);
				logger.d(stringbuilder.toString());
				return;
			}
		} while (true);
	}

	public View newView(Context context, Cursor cursor, ViewGroup viewgroup)
	{
		View view = LayoutInflater.from(context).inflate(com.spb.cities.R.layout.weather_city_dropdown_list_item2, viewgroup, false);
		bindView(view, context, cursor);
		return view;
	}

	public abstract Cursor runQueryOnBackgroundThread(CharSequence charsequence);

}
