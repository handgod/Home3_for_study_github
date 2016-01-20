// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.core;

import android.app.Activity;
import android.view.View;
import android.view.ViewParent;

public class Util
{

	public Util()
	{
	}

	public static Activity lookForActivity(View view)
	{
		Activity activity = null;
		Activity activity1;
		if (!(view.getContext() instanceof Activity)) 
			{
				ViewParent viewparent = view.getParent();
				do
				{
				label0:
					{
						if (viewparent != null)
						{
							if (!(viewparent instanceof View))
								break label0;
							View view1 = (View)viewparent;
							if (!(view1.getContext() instanceof Activity))
								break label0;
							activity = (Activity)view1.getContext();
						}
						activity1 = activity;
					}
					if (true)
						continue;
					viewparent = viewparent.getParent();
				} while (true);
			}
		else 
			{
				activity1 = (Activity)view.getContext();
			}
		return activity1;
	}
}
