// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.updateservice.spb;

import android.content.Context;
import com.softspb.updateservice.DownloadClient;
import com.softspb.weather.updateservice.ForecastUpdateService;

// Referenced classes of package com.softspb.weather.updateservice.spb:
//			GismeteoClient

public class SPBForecastUpdateService extends ForecastUpdateService
{

	public SPBForecastUpdateService()
	{
	}

	protected DownloadClient createDownloadClient(Context context)
	{
		return new GismeteoClient(context);
	}
}
