// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.widget;

import android.appwidget.AppWidgetHostView;
import android.content.Context;
import android.graphics.Rect;
import android.view.*;
import android.widget.RemoteViews;
import com.softspb.shell.Home;
import com.softspb.shell.view.WidgetController2;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class ShellWidgetHostView extends AppWidgetHostView
{
	private class CheckForLongPress
		implements Runnable
	{

		private int originalWindowAttachCount;
		final ShellWidgetHostView this$0;

		public void rememberWindowAttachCount()
		{
			originalWindowAttachCount = getWindowAttachCount();
		}

		public void run()
		{
			if (movementController.getDelayed() && !movementController.isProcessedByOther(lock) && getParent() != null && hasWindowFocus() && originalWindowAttachCount == getWindowAttachCount() && !hasPerformedLongPress)
			{
				hasPerformedLongPress = true;
				movementController.process(lock);
				Home.sendMouseEvent(0, lastX, lastY);
			}
		}

		private CheckForLongPress()
		{
			this$0 = ShellWidgetHostView.this;
//			super();
		}

	}


	private static final Logger logger;
	private boolean hasPerformedLongPress;
	private int lastDownX;
	private int lastDownY;
	private int lastX;
	private int lastY;
	private Object lock;
	private com.softspb.shell.Home.MovementController movementController;
	private CheckForLongPress pendingCheckForLongPress;
	private int touchSlop;

	public ShellWidgetHostView(Context context)
	{
		super(context);
		lock = new Object();
	}

	public ShellWidgetHostView(Context context, int i, int j)
	{
		super(context, i, j);
		lock = new Object();
	}

	private void postCheckForLongClick()
	{
		hasPerformedLongPress = false;
		movementController.setDelayed();
		if (pendingCheckForLongPress == null)
			pendingCheckForLongPress = new CheckForLongPress();
		pendingCheckForLongPress.rememberWindowAttachCount();
		postDelayed(pendingCheckForLongPress, ViewConfiguration.getLongPressTimeout());
	}

	private void removeLongPressCallback()
	{
		hasPerformedLongPress = false;
		if (pendingCheckForLongPress != null)
			removeCallbacks(pendingCheckForLongPress);
	}

	private void tryToFreeLock(MotionEvent motionevent)
	{
		if (motionevent.getAction() == 1 || motionevent.getAction() == 3)
		{
			movementController.free(lock);
			hasPerformedLongPress = false;
		}
	}

	public void cancelLongPress()
	{
		super.cancelLongPress();
		hasPerformedLongPress = false;
		if (pendingCheckForLongPress != null)
			removeCallbacks(pendingCheckForLongPress);
	}

	protected View getErrorView()
	{
		return ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(com.spb.shell3d.R.layout.appwidget_error, this, false);
	}

	public ViewParent invalidateChildInParent(int ai[], Rect rect)
	{
		logger.i((new StringBuilder()).append("ShellAppWidgetHost invalidateChildInParent ").append(rect).append(" ").append(ai).toString());
		return super.invalidateChildInParent(ai, rect);
	}

	public boolean onInterceptTouchEvent(MotionEvent motionevent)
	{
		boolean flag;
		flag = true;
		lastX = (int)motionevent.getRawX();
		lastY = (int)motionevent.getRawY();
		if (!hasPerformedLongPress) 
		{
			switch (motionevent.getAction()) {
			case 0:
				
				break;
			case 1:
				
				break;
			case 2:
				boolean flag1;
				boolean flag2;
				if (Math.abs(lastDownX - lastX) > touchSlop)
					flag1 = flag;
				else
					flag1 = false;
				if (Math.abs(lastDownY - lastY) > touchSlop)
					flag2 = flag;
				else
					flag2 = false;
				if (flag1 || flag2)
				{
					removeLongPressCallback();
					flag = false;
				}
				lastDownX = lastX;
				lastDownY = lastY;
				postCheckForLongClick();
				break;
			case 3:
		
				break;
			default:
				break;
			}	
		}
		else
		{
			tryToFreeLock(motionevent);
			if (motionevent.getAction() == 1)
				Home.sendMouseEvent(motionevent.getAction(), (int)motionevent.getRawX(), (int)motionevent.getRawY());
		}
		
	return flag;

		
	}

	public boolean onTouchEvent(MotionEvent motionevent)
	{
		lastX = (int)motionevent.getRawX();
		lastY = (int)motionevent.getRawY();
		boolean flag;
		if (hasPerformedLongPress)
		{
			tryToFreeLock(motionevent);
			if (motionevent.getAction() != 3)
				Home.sendMouseEvent(motionevent.getAction(), (int)motionevent.getRawX(), (int)motionevent.getRawY());
			flag = true;
		} else
		{
			flag = super.onTouchEvent(motionevent);
		}
		return flag;
	}

	public void setMovementController(com.softspb.shell.Home.MovementController movementcontroller)
	{
		movementController = movementcontroller;
		touchSlop = ViewConfiguration.getTouchSlop();
	}

	public void updateAppWidget(RemoteViews remoteviews)
	{
		super.updateAppWidget(remoteviews);
		WidgetController2 widgetcontroller2 = (WidgetController2)getParent();
		if (widgetcontroller2 != null)
			widgetcontroller2.updateChild(this);
	}

	static 
	{
		logger = Loggers.getLogger(ShellWidgetHostView.class.getName());
		logger.enableThreadLog();
	}






/*
	static boolean access$302(ShellWidgetHostView shellwidgethostview, boolean flag)
	{
		shellwidgethostview.hasPerformedLongPress = flag;
		return flag;
	}

*/



}
