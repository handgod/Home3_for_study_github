// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.shell3d.market;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import com.softspb.shell.ShellApplication;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.spb.shell3d.market:
//			SpbMarketLicenseService

public class Shell3dApplicationMarket extends ShellApplication
{

	private static final List components = new ArrayList() {

			
			{
				add(SpbMarketLicenseService.class);
			}
	};

	public Shell3dApplicationMarket()
	{
	}

	public List getLicenseComponents()
	{
		return components;
	}

	public void onLicenseCheckFailed(final Activity activity, Intent intent)
	{
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
		builder.setMessage(0x7f060060);
		builder.setNegativeButton(0x7f060062, null);
		builder.setPositiveButton(0x7f060061, new android.content.DialogInterface.OnClickListener() {

			final Shell3dApplicationMarket this$0;
			final Activity val$activity;

			public void onClick(DialogInterface dialoginterface, int i)
			{
				Intent intent1 = new Intent("android.intent.action.VIEW", Uri.parse((new StringBuilder()).append("market://details?id=").append(getPackageName()).toString()));
				activity.startActivity(intent1);
			}

			
			{
				this$0 = Shell3dApplicationMarket.this;
				val$activity = activity;
//				super();
			}
		});
		builder.setCancelable(false);
		builder.create().show();
	}

	public void startLicensingProcess(Activity activity)
	{
		startLicenseService(SpbMarketLicenseService.class);
	}

}
