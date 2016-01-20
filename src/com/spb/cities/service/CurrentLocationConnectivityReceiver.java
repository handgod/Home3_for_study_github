// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.service;

import android.content.*;
import android.net.NetworkInfo;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.location.CurrentLocationPreferences;

// Referenced classes of package com.spb.cities.service:
//			CurrentLocationClient

public class CurrentLocationConnectivityReceiver extends BroadcastReceiver
{

	private static Logger logger = Loggers.getLogger(CurrentLocationConnectivityReceiver.class);

	public CurrentLocationConnectivityReceiver()
	{
	}

	public void onReceive(Context context, Intent intent)
	{
		CurrentLocationClient currentlocationclient;
		logger.d("onReceive >>>");
		currentlocationclient = CurrentLocationClient.getInstance(context);
		if (currentlocationclient.hasListeners()) 
		{
			CurrentLocationPreferences currentlocationpreferences = new CurrentLocationPreferences(context);
			NetworkInfo networkinfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
			if (networkinfo.isAvailable() && (networkinfo.getType() == 1 || !currentlocationpreferences.isUseOnlyWifi()))
				currentlocationclient.considerUpdateCurrentLocation(true);
			currentlocationpreferences.dispose();
		}
		else 
		{
			logger.d("onReceive <<< no registered current location listeners");
		}
		return;
	}

}
