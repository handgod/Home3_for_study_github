// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.view;

import android.app.AlertDialog;
import android.content.*;
import android.content.pm.*;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.*;
import android.widget.*;
import java.util.*;

public class FilterPickDialogBuilder
{
	private static class PickAdapter extends BaseAdapter
	{
		static class Item
		{

			String className;
			CharSequence extendedInfo;
			Drawable icon;
			CharSequence label;
			String packageName;

			Intent getIntent(Intent intent1)
			{
				Intent intent2 = new Intent(intent1);
				if (packageName != null && className != null)
				{
					intent2.setClassName(packageName, className);
				} else
				{
					intent2.setAction("android.intent.action.CREATE_SHORTCUT");
					intent2.putExtra("android.intent.extra.shortcut.NAME", label);
				}
				return intent2;
			}

			void setExtendedInfo(CharSequence charsequence)
			{
				extendedInfo = charsequence;
			}

			Item(PackageManager packagemanager, ResolveInfo resolveinfo)
			{
				label = resolveinfo.loadLabel(packagemanager);
				if (label == null && resolveinfo.activityInfo != null)
					label = resolveinfo.activityInfo.name;
				icon = resolveinfo.loadIcon(packagemanager);
				if (resolveinfo.activityInfo != null)
				{
					packageName = resolveinfo.activityInfo.applicationInfo.packageName;
					className = resolveinfo.activityInfo.name;
				}
			}
		}


		LayoutInflater inflater;
		List items;

		public int getCount()
		{
			return items.size();
		}

		public Object getItem(int i)
		{
			return items.get(i);
		}

		public long getItemId(int i)
		{
			return (long)i;
		}

		public View getView(int i, View view, ViewGroup viewgroup)
		{
			if (view == null)
				view = inflater.inflate(com.spb.shell3d.R.layout.shortcut_dialog_pick_item, viewgroup, false);
			Item item = (Item)getItem(i);
			TextView textview = (TextView)view.findViewById(0x1020014);
			TextView textview1 = (TextView)view.findViewById(0x1020015);
			if (item.extendedInfo != null)
			{
				textview1.setText(item.extendedInfo);
				textview1.setVisibility(0);
			} else
			{
				textview1.setVisibility(8);
			}
			((ImageView)view.findViewById(com.spb.shell3d.R.id.icon)).setImageDrawable(item.icon);
			textview.setText(item.label);
			return view;
		}

		PickAdapter(List list, Context context1)
		{
			items = list;
			inflater = (LayoutInflater)context1.getSystemService("layout_inflater");
		}
	}

	private static class ClickListener
		implements android.content.DialogInterface.OnClickListener
	{

		final Adapter adapter;
		final Intent intent;
		final DialogResult result;

		public void onClick(DialogInterface dialoginterface, int i)
		{
			PickAdapter.Item item = (PickAdapter.Item)adapter.getItem(i);
			dialoginterface.dismiss();
			if (result != null)
				result.onResult(item.getIntent(intent));
		}

		public ClickListener(DialogResult dialogresult, Adapter adapter1, Intent intent1)
		{
			result = dialogresult;
			adapter = adapter1;
			intent = intent1;
		}
	}

	public static interface DialogResult
	{

		public abstract void onResult(Intent intent1);
	}

	public static interface IFilter
	{

		public abstract boolean filter(ResolveInfo resolveinfo);
	}


	private PickAdapter adapter;
	private final Context context;
	private DialogResult dialogResult;
	private IFilter filter;
	private final Intent intent;
	private CharSequence title;

	public FilterPickDialogBuilder(Context context1, Intent intent1)
	{
		context = context1;
		intent = intent1;
	}

	private List getIntentItems(Context context1, Intent intent1, IFilter ifilter)
	{
		ArrayList arraylist = new ArrayList();
		PackageManager packagemanager = context1.getPackageManager();
		List list = packagemanager.queryIntentActivities(intent1, 0);
		Collections.sort(list, new android.content.pm.ResolveInfo.DisplayNameComparator(packagemanager));
		if (list != null)
		{
			Iterator iterator = list.iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				ResolveInfo resolveinfo = (ResolveInfo)iterator.next();
				if (ifilter != null && !ifilter.filter(resolveinfo))
					iterator.remove();
			} while (true);
			PickAdapter.Item item = null;
			PickAdapter.Item item1;
			int i;
			if (list.size() > 0)
				item1 = new PickAdapter.Item(packagemanager, (ResolveInfo)list.get(0));
			else
				item1 = null;
			i = 0;
			while (i < list.size()) 
			{
				Object obj = item;
				item = item1;
				if (i + 1 == list.size())
					item1 = null;
				else
					item1 = new PickAdapter.Item(packagemanager, (ResolveInfo)list.get(i + 1));
				if (obj != null && item.label.equals(((PickAdapter.Item) (obj)).label) || item1 != null && item.label.equals(item1.label))
					item.setExtendedInfo(((ResolveInfo)list.get(i)).activityInfo.applicationInfo.loadLabel(packagemanager));
				arraylist.add(item);
				i++;
			}
		}
		return arraylist;
	}

	public AlertDialog create()
	{
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
		adapter = new PickAdapter(getIntentItems(context, intent, filter), context);
		builder.setAdapter(adapter, new ClickListener(dialogResult, adapter, intent));
		builder.setTitle(title);
		return builder.create();
	}

	public FilterPickDialogBuilder setDialogResult(DialogResult dialogresult)
	{
		dialogResult = dialogresult;
		return this;
	}

	public FilterPickDialogBuilder setFilter(IFilter ifilter)
	{
		filter = ifilter;
		return this;
	}

	public FilterPickDialogBuilder setTitle(int i)
	{
		title = context.getResources().getString(i);
		return this;
	}

	public FilterPickDialogBuilder setTitle(CharSequence charsequence)
	{
		title = charsequence;
		return this;
	}
}
