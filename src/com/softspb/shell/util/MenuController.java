// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util;

import android.graphics.Point;
import android.view.*;
//import android.widget.PopupMenu;
import com.softspb.shell.Home;
import com.softspb.shell.opengl.NativeCallbacks;

public class MenuController
{

//	PopupMenu currentMenu;
	private Home home;
//	android.widget.PopupMenu.OnMenuItemClickListener menuItemClickListener;
	boolean menuReady;
	private NativeCallbacks nc;

	public MenuController(Home home1)
	{
//		menuReady = false;
//		home = home1;
//		if (android.os.Build.VERSION.SDK_INT >= 11)
//			menuItemClickListener = new android.widget.PopupMenu.OnMenuItemClickListener() {
//
//				final MenuController this$0;
//
//				public boolean onMenuItemClick(MenuItem menuitem)
//				{
//					currentMenu = null;
//					return onOptionsItemSelected(menuitem);
//				}
//
//			
//			{
////				super();
//				this$0 = MenuController.this;
//			}
//			};
	}

	public static native void onMenuClosed();

	public static native void onMenuItemSelected(int i);

	private void openPopupMenu(int i, int j)
	{
//		PopupMenu popupmenu;
//		View view = home.getMenuAnchorView();
//		com.softspb.shell.opengl.MyGlSurfaceView myglsurfaceview = home.getSurfaceView();
//		Point point;
//		if (i != -1 && j != -1)
//			point = new Point(i + myglsurfaceview.getLeft(), j + myglsurfaceview.getTop());
//		else
//			point = new Point(myglsurfaceview.getRight(), myglsurfaceview.getBottom());
//		view.layout(point.x, point.y, point.x, point.y);
//		popupmenu = new PopupMenu(home, view);
//		break MISSING_BLOCK_LABEL_90;
//		if (prepareOptionsMenu(popupmenu.getMenu()) && onMenuOpened(0, popupmenu.getMenu()))
//		{
//			currentMenu = popupmenu;
//			popupmenu.setOnMenuItemClickListener(menuItemClickListener);
//			popupmenu.show();
//		}
//		return;
	}

	public static native boolean requestMenu();

	public void onCreate()
	{
		nc = home.getNativeCallbacks();
	}

	public void onDestroy()
	{
	}

	public boolean onMenuOpened(int i, Menu menu)
	{
		boolean flag = false;
		if (i != 0) 
		{
			flag = onMenuOpened(i, menu);
			return flag;
		}
		else
		{
			if (menuReady) 
			{
				menuReady = false;
				if (!nc.isMenuEmpty())
					flag = true;
			}
			else 
			{
				requestMenu();
			}
		}
		return flag;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem)
	{
		onMenuItemSelected(menuitem.getItemId());
		return true;
	}

	public void onOptionsMenuClosed(Menu menu)
	{
		onMenuClosed();
	}

	public void onRelayout()
	{
//		if (currentMenu != null)
//			 currentMenu.dismiss();
	}

	public void openCompatibleMenu(int i, int j)
	{
		if (android.os.Build.VERSION.SDK_INT < 11)
			home.openClassicOptionsMenu();
		else
			openPopupMenu(i, j);
	}

	public void openMenuFromNative(int i, int j)
	{
		menuReady = true;
		openCompatibleMenu(i, j);
	}

	public boolean prepareOptionsMenu(Menu menu)
	{
		menu.clear();
		nc.fillMenu(menu);
		if (menu.size() == 0)
			menu.add(0, 0, 0, "Menu unintialized");
		return true;
	}
}
