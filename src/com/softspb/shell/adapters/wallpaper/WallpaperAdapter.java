// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import com.softspb.shell.adapters.Adapter;
import com.softspb.shell.adapters.AdaptersHolder;

public abstract class WallpaperAdapter extends Adapter
{

	private static String USE_LIVE_WALLPAPER = "key_use_livewallpaper";

	public WallpaperAdapter(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
	}

	public static boolean isLiveWallpaper(Context context)
	{
		boolean flag;
		if (WallpaperManager.getInstance(context).getWallpaperInfo() != null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static native void notifyChange();

	public static void setUseLiveWallpapers(Context context, boolean flag)
	{
		PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(USE_LIVE_WALLPAPER, flag).commit();
	}

	public static boolean useLiveWallpapers(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(USE_LIVE_WALLPAPER, false);
	}

	public abstract void checkWallpaperChange();

	public abstract Bitmap getWallpaper();

	public abstract String getWallpaperSkin();

	public abstract boolean isLiveWallpaper();

	public abstract void openSetWallPaperDialog();

	public abstract void reloadWallpaper();

	public abstract void setWallpaperFromFile(String s);

}
