// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.wallpaper;

import android.app.WallpaperInfo;
import android.graphics.Bitmap;
import com.softspb.shell.adapters.AdaptersHolder;

// Referenced classes of package com.softspb.shell.adapters.wallpaper:
//			AbstractWallpaperAdapter

public class LiteWallpaperAndroid extends AbstractWallpaperAdapter
{

	public LiteWallpaperAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
	}

	protected Bitmap loadWallpaper()
	{
		return super.loadWallpaper();
	}

	public void onWallpaperChange(WallpaperInfo wallpaperinfo, WallpaperInfo wallpaperinfo1)
	{
		reloadWallpaper();
	}
}
