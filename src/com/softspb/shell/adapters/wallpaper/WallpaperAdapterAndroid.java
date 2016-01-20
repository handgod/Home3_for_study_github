// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.wallpaper;

import android.app.WallpaperInfo;
import com.softspb.shell.Home;
import com.softspb.shell.adapters.AdaptersHolder;

// Referenced classes of package com.softspb.shell.adapters.wallpaper:
//			AbstractWallpaperAdapter

public class WallpaperAdapterAndroid extends AbstractWallpaperAdapter
{

	public static final String ACTION_KILL_SHELL = "com.spb.shell3d.action.pifpaf";

	public WallpaperAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
	}

	public boolean isLiveWallpaper()
	{
		boolean flag;
		if (useLiveWallpapers(context) && super.isLiveWallpaper())
			flag = true;
		else
			flag = false;
		return flag;
	}

	public void onWallpaperChange(WallpaperInfo wallpaperinfo, WallpaperInfo wallpaperinfo1)
	{
		boolean flag = true;
		if (useLiveWallpapers(context))
		{
			boolean flag1;
			if (wallpaperinfo == null)
				flag1 = flag;
			else
				flag1 = false;
			if (wallpaperinfo1 != null)
				flag = false;
			if (flag ^ flag1)
				((Home)context).restartShell();
			else
			if (flag)
				super.reloadWallpaper();
		}
		else 
		{
			reloadWallpaper();
		}
		return;
	}

	public void reloadWallpaper()
	{
		if (useLiveWallpapers(context) && isLiveWallpaper())
			notifyChange();
		else
			super.reloadWallpaper();
	}
}
