// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.view;

import android.view.View;
import android.view.ViewGroup;
import com.softspb.util.Conditions;

public class ViewUtils
{

	private ViewUtils()
	{
	}

	public static void clearAnimation(View view)
	{
		((View)Conditions.checkNotNull(view)).clearAnimation();
		if (view instanceof ViewGroup)
		{
			ViewGroup viewgroup = (ViewGroup)view;
			for (int i = 0; i < viewgroup.getChildCount(); i++)
				clearAnimation(viewgroup.getChildAt(i));

		}
	}

	public static boolean hasAnimation(View view)
	{
		boolean flag ;
		if (((View)Conditions.checkNotNull(view)).getAnimation() == null)
			{
			if (view instanceof ViewGroup)
			{
				ViewGroup viewgroup = (ViewGroup)view;
				int i = 0;
				do
				{
					if (i >= viewgroup.getChildCount())
						break;
					if (hasAnimation(viewgroup.getChildAt(i)))
					{
						flag = true;
						continue; /* Loop/switch isn't completed */
					}
					i++;
				} while (true);
			}
			flag = false;
			}
		else
			{
			 flag = true;
			}
		return flag;
	}
}
