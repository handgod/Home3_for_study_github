// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.format.Time;
import android.widget.DatePicker;

// Referenced classes of package com.softspb.shell.adapters.dialog:
//			IShellDialog

public class ShellDatePickerDialog extends IShellDialog
	implements android.app.DatePickerDialog.OnDateSetListener, android.content.DialogInterface.OnCancelListener, android.content.DialogInterface.OnDismissListener
{
	class UIHandler extends IShellDialog.UIHandler
	{

		final ShellDatePickerDialog this$0;

		protected void show()
		{
			logd("show");
			DatePickerDialog datepickerdialog = new DatePickerDialog(context, ShellDatePickerDialog.this, year, month, day);
			dialog = datepickerdialog;
			datepickerdialog.setCancelable(true);
			datepickerdialog.setOnCancelListener(ShellDatePickerDialog.this);
			datepickerdialog.setOnDismissListener(ShellDatePickerDialog.this);
			datepickerdialog.setInverseBackgroundForced(true);
			datepickerdialog.show();
		}

		UIHandler(Looper looper)
		{
			super(looper);
			this$0 = ShellDatePickerDialog.this;
		}
	}


	private int day;
	private int month;
	private int year;

	public ShellDatePickerDialog(Context context, Looper looper, int i, int j)
	{
		super(context, looper, i, j);
		logd((new StringBuilder()).append("Ctor: adapterAddress=0x").append(Integer.toHexString(i)).append(" dialogToken=").append(j).toString());
		Time time = new Time();
		time.setToNow();
		year = time.year;
		month = time.month;
		day = time.monthDay;
	}

	private native void onDateSet(int i, int j, int k, int l, int i1);

	private native void onDismiss(int i, int j);

	protected IShellDialog.UIHandler createUIHandler(Looper looper)
	{
		return new UIHandler(looper);
	}

	public void dismiss()
	{
		dismiss();
	}

	public void onCancel(DialogInterface dialoginterface)
	{
		logd("onCancel");
	}

	public void onDateSet(DatePicker datepicker, int i, int j, int k)
	{
		logd((new StringBuilder()).append("onDateSet: year=").append(i).append(" month=").append(j).append(" day=").append(k).toString());
		onDateSet(adapterAddress, dialogToken, i, j + 1, k);
	}

	public void onDismiss(DialogInterface dialoginterface)
	{
		logd("onDismiss");
		onDismiss(adapterAddress, dialogToken);
	}

	public void setDate(int i, int j, int k)
	{
		year = i;
		month = j - 1;
		day = k;
	}

	public void show()
	{
		show();
	}



}
