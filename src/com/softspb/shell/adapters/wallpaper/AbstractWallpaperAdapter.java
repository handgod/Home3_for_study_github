// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.wallpaper;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.AssetManager;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.softspb.shell.adapters.AdaptersHolder;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.shell.util.BitmapHelper;
import com.softspb.shell.view.FilterPickDialogBuilder;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.programlist.ProgramsUtil;
import java.io.*;

// Referenced classes of package com.softspb.shell.adapters.wallpaper:
//			WallpaperAdapter

public abstract class AbstractWallpaperAdapter extends WallpaperAdapter
{
	private static class Size
	{

		public int cx;
		public int cy;

		public Size(int i, int j)
		{
			cx = i;
			cy = j;
		}
	}


	private static final String DEFAULT_PANEL_NAME = "wallpapers/default";
	private static final String DEFAULT_WALLPAPER = "files/wallpaper.jpg";
	private static final String FILE_NAME = "wallpaper.png";
	private static final String KEY_PANEL_NAME = "key_panel_name";
	private static final String WALLPAPERS_PATH = "wallpapers";
	private static final Logger logger = Loggers.getLogger(AbstractWallpaperAdapter.class.getName());
	protected Context context;
	private Runnable loadImage;
	private Bitmap mWallpaper;
	private SharedPreferences preferences;
	private DecoratedBroadcastReceiver receiver;
	protected WallpaperInfo wallpaperInfo;
	private WallpaperManager wm;

	public AbstractWallpaperAdapter(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		receiver = new DecoratedBroadcastReceiver("android.intent.action.WALLPAPER_CHANGED", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final AbstractWallpaperAdapter this$0;

			public void onAction(Context context1, Intent intent)
			{
				setNewWallpaperInfo(null);
			}

			
			{
				this$0 = AbstractWallpaperAdapter.this;
//				super();
			}
		});
		loadImage = new Runnable() {

			final AbstractWallpaperAdapter this$0;

			public void run()
			{
				mWallpaper = loadWallpaper();
				WallpaperAdapter.notifyChange();
			}

			
			{
				this$0 = AbstractWallpaperAdapter.this;
//				super();
			}
		};
	}

	private Size getWallpaperSize(Drawable drawable)
	{
		Size size = new Size(drawable.getMinimumWidth(), drawable.getMinimumHeight());
		if (size.cx <= 0 || size.cy <= 0)
		{
			Display display = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
			DisplayMetrics displaymetrics = new DisplayMetrics();
			display.getMetrics(displaymetrics);
			size.cy = displaymetrics.heightPixels;
			size.cx = displaymetrics.widthPixels;
		}
		return size;
	}

	private void setNewWallpaperInfo(WallpaperInfo wallpaperinfo)
	{
		WallpaperInfo wallpaperinfo1 = wallpaperInfo;
		wallpaperInfo = wm.getWallpaperInfo();
		onWallpaperChange(wallpaperinfo1, wallpaperInfo);
	}

	private static boolean wallpaperInfoEqual(WallpaperInfo wallpaperinfo, WallpaperInfo wallpaperinfo1)
	{
		boolean flag = true;
		if ((wallpaperinfo == null || wallpaperinfo1 != null) && (wallpaperinfo != null || wallpaperinfo1 == null)) 
			{
			if (wallpaperinfo != null && (!wallpaperinfo.getPackageName().equals(wallpaperinfo1.getPackageName()) || !wallpaperinfo.getServiceName().equals(wallpaperinfo1.getServiceName())))
				flag = false;
			}
		else{
			flag = false;
		}
		return flag;
	}

	public void checkWallpaperChange()
	{
		if (!wallpaperInfoEqual(wm.getWallpaperInfo(), wallpaperInfo))
			setNewWallpaperInfo(wm.getWallpaperInfo());
	}

	public Bitmap getWallpaper()
	{
		return mWallpaper;
	}

	public String getWallpaperSkin()
	{
		String s = preferences.getString("key_panel_name", "wallpapers/default");
		logger.i((new StringBuilder()).append("getWallpaper skin").append(s).toString());
		return s;
	}

	public boolean isLiveWallpaper()
	{
		boolean flag;
		if (wm.getWallpaperInfo() != null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	protected final Bitmap loadDefaultSpbWallpaper()
	{
		Bitmap bitmap1 = null;
		try {
			bitmap1 = BitmapFactory.decodeStream(context.getAssets().open("files/wallpaper.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = bitmap1;

		return bitmap;
	
	}

	protected final Bitmap loadSavedWallpaper()
	{
		File file = new File(context.getFilesDir(), "wallpaper.png");
		Bitmap bitmap1 = null;
		try {
			bitmap1 = BitmapFactory.decodeStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = bitmap1;

		return bitmap;
		
	}

	protected Bitmap loadWallpaper()
	{
		final Drawable wallpaper = wm.getDrawable();
		Bitmap bitmap;
		if (wallpaper instanceof BitmapDrawable) 
		{
			Bitmap bitmap1 = ((BitmapDrawable)wallpaper).getBitmap();
			if (!(bitmap1 != null && !bitmap1.isRecycled()))
			{
				bitmap = loadSavedWallpaper();
			}
		}
		 
		{
			if (wallpaper == null)
			{
				bitmap = loadDefaultSpbWallpaper();
			} else
			{
				Size size = getWallpaperSize(wallpaper);
				Rect rect = new Rect(wallpaper.getBounds());
				wallpaper.setBounds(0, 0, size.cx, size.cy);
				android.graphics.Bitmap.Config config;
				Canvas canvas;
				if (wallpaper.getOpacity() != -1)
					config = android.graphics.Bitmap.Config.ARGB_8888;
				else
					config = android.graphics.Bitmap.Config.RGB_565;
				bitmap = Bitmap.createBitmap(size.cx, size.cy, config);
				canvas = new Canvas(bitmap);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(4, 0));
				wallpaper.draw(canvas);
				wallpaper.setBounds(rect);
				(new Thread(new Runnable() {

					final AbstractWallpaperAdapter this$0;
					final Drawable val$wallpaper;

					public void run()
					{
						if (wallpaper instanceof BitmapDrawable)
						{
							Bitmap bitmap2 = ((BitmapDrawable)wallpaper).getBitmap();
							if (bitmap2 != null && !bitmap2.isRecycled())
							{
								BitmapHelper.writeBitmap(bitmap2, "wallpaper.png", context.getFilesDir());
								bitmap2.recycle();
							}
						}
					}

				
				{
					this$0 = AbstractWallpaperAdapter.this;
					val$wallpaper = wallpaper;
//					super();
				}
				})).start();
			}
		}
	return 	bitmap;
	}

	protected void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		context = context1;
		wm = (WallpaperManager)context1.getSystemService("wallpaper");
		wallpaperInfo = wm.getWallpaperInfo();
		context1.registerReceiver(receiver, receiver.getIntentFilter());
		preferences = PreferenceManager.getDefaultSharedPreferences(context1);
	}

	protected void onDestroy(Context context1)
	{
		context1.unregisterReceiver(receiver);
	}

	public abstract void onWallpaperChange(WallpaperInfo wallpaperinfo, WallpaperInfo wallpaperinfo1);

	public void openSetWallPaperDialog()
	{
		FilterPickDialogBuilder filterpickdialogbuilder = new FilterPickDialogBuilder(context, new Intent("android.intent.action.SET_WALLPAPER"));
		filterpickdialogbuilder.setFilter(new com.softspb.shell.view.FilterPickDialogBuilder.IFilter() {

			final AbstractWallpaperAdapter this$0;

			public boolean filter(ResolveInfo resolveinfo)
			{
				boolean flag = false;
				if (WallpaperAdapter.useLiveWallpapers(context) || resolveinfo.activityInfo == null)
					{
						flag = true;
					}
				else 
					{
						if (!"com.android.wallpaper.livepicker".equals(resolveinfo.activityInfo.packageName)) 
						{
							boolean flag1 = "com.htc.AddProgramWidget".equalsIgnoreCase(resolveinfo.activityInfo.packageName);
							boolean flag2 = "com.htc.AddProgramWidget.WallpaperLivePicker".equalsIgnoreCase(resolveinfo.activityInfo.name);
							if (flag1 && flag2 || (1 & resolveinfo.activityInfo.applicationInfo.flags) != 1)
								flag = true;
						}
						else
						{
							return flag;
						}
					}
				return flag;
				
			}

			
			{
				this$0 = AbstractWallpaperAdapter.this;
//				super();
			}
		});
		filterpickdialogbuilder.setDialogResult(new com.softspb.shell.view.FilterPickDialogBuilder.DialogResult() {

			final AbstractWallpaperAdapter this$0;

			public void onResult(Intent intent)
			{
				ProgramsUtil.startActivitySafely(context, intent);
			}

			
			{
				this$0 = AbstractWallpaperAdapter.this;
//				super();
			}
		});
		filterpickdialogbuilder.setTitle(com.spb.shell3d.R.string.chooser_wallpaper);
		filterpickdialogbuilder.create().show();
	}

	public void reloadWallpaper()
	{
		(new Thread(loadImage)).start();
	}

	public void setWallpaperFromFile(String s)
	{
		File file = new File(s);
		try {
			wm.setStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
		
	}




/*
	static Bitmap access$102(AbstractWallpaperAdapter abstractwallpaperadapter, Bitmap bitmap)
	{
		abstractwallpaperadapter.mWallpaper = bitmap;
		return bitmap;
	}

*/
}
