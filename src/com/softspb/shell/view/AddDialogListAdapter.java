// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.view;

import android.content.Context;
import android.util.Pair;
import android.view.*;
import android.widget.*;

public class AddDialogListAdapter extends ArrayAdapter
{

	private Context context;

	public AddDialogListAdapter(Context context1)
	{
		super(context1, com.spb.shell3d.R.layout.add_dialog_list_item);
		context = context1;
	}

	public View getView(int i, View view, ViewGroup viewgroup)
	{
		View view1 = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(com.spb.shell3d.R.layout.add_dialog_list_item, viewgroup, false);
		TextView textview = (TextView)view1.findViewById(0x1020014);
		ImageView imageview = (ImageView)view1.findViewById(0x1020006);
		Pair pair = (Pair)getItem(i);
		textview.setText((CharSequence)pair.first);
		imageview.setImageResource(((Integer)pair.second).intValue());
		return view1;
	}
}
