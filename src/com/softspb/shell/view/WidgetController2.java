// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.view;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.*;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.*;
import com.softspb.shell.Home;
import com.softspb.shell.data.BaseWidgetInfo;
import com.softspb.shell.opengl.NativeCalls;
import com.softspb.shell.util.ConcurrentUtil;
import com.softspb.shell.widget.ShellAppWidgetHost;
import com.softspb.util.CollectionFactory;
import com.softspb.util.Conditions;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Referenced classes of package com.softspb.shell.view:
//			ViewUtils

public class WidgetController2 extends ViewGroup
{
	public static class LayoutParams extends android.view.ViewGroup.LayoutParams
	{

		private final int appWidgetId;
		private Bitmap bitmap;
		private int id;
		private boolean initialised;
		private int x;
		private int y;

		public int getAppWidgetId()
		{
			return appWidgetId;
		}

		public int getId()
		{
			return id;
		}

		public boolean isInitialised()
		{
			return initialised;
		}

		public void setId(int i)
		{
			initialised = true;
			id = i;
			x = -1000 * (i + 1);
		}

		public String toString()
		{
			return (new StringBuilder()).append("LayoutParams [id=").append(id).append(", height=").append(height).append(", width=").append(width).append("]").toString();
		}




/*
		static int access$102(LayoutParams layoutparams, int i)
		{
			layoutparams.x = i;
			return i;
		}

*/



/*
		static int access$202(LayoutParams layoutparams, int i)
		{
			layoutparams.y = i;
			return i;
		}

*/



/*
		static Bitmap access$402(LayoutParams layoutparams, Bitmap bitmap1)
		{
			layoutparams.bitmap = bitmap1;
			return bitmap1;
		}

*/


		public LayoutParams(int i, int j, int k)
		{
			super(i, j);
			initialised = false;
			id = 0;
			appWidgetId = k;
		}
	}

	public static interface MixIdStrategy
	{

		public abstract int generalIdBySpecId(int i, int j);

		public abstract int getType(int i);

		public abstract int specIdByGeneral(int i);
	}


	private static final long AWAIT_TIME = 500L;
	private static final int INVISIBLE_UP = -10000;
	public static final int TYPE_PANEL = 2;
	public static final int TYPE_WIDGET = 1;
	private static Logger logger = Loggers.getLogger(WidgetController2.class.getName());
	public static MixIdStrategy mixIdStrategy = new MixIdStrategy() {

		private static final int MAX_WIDGETS = 0xf4240;

		public int generalIdBySpecId(int i, int j)
		{
			switch (j)
			{
			default:
				throw new IllegalArgumentException((new StringBuilder()).append("We dont have type = ").append(j).toString());

			case 1: // '\001'
				if (i >= 0xf4240)
					throw new IllegalArgumentException("Widget should have id less than 1000000");
				break;

			case 2: // '\002'
				i += 16960;
				break;
			}
			return i;
		}

		public int getType(int i)
		{
			byte byte0;
			if (i >= 0xf4240)
				byte0 = 2;
			else
				byte0 = 1;
			return byte0;
		}

		public int specIdByGeneral(int i)
		{
			switch (getType(i))
			{
			default:
				throw new IllegalStateException();

			case 2: // '\002'
				i -= 16960;
				// fall through

			case 1: // '\001'
				return i;
			}
		}

	};
	private List changesTrack;
	private Lock childLock;
	private Bitmap gEmptyBitmap;
	private Bitmap gOffscreen;
	private Canvas gOffscreenCanvas;
	private Handler handler;
	Home home;
	private SparseArray ids2widgets;
	private Set justAdded;
	private float lastMotionX;
	private float lastMotionY;
	Runnable layoutAction = new Runnable() {

		final WidgetController2 this$0;

		public void run()
		{
			WidgetController2.logger.i("!!!!!!!! layoutAction requestLayout()");
			requestLayout();
		}

			
			{
				this$0 = WidgetController2.this;
//				super();
			}
	};
	private View mainView;
	private com.softspb.shell.Home.MovementController movementController;
	private Map offscreens;
	private boolean stopped;
	private Object touchLock;
	private int touchSlop;
	public Runnable updateScreenshots = new Runnable() {

		final WidgetController2 this$0;

		public void run()
		{
			WidgetController2.logger.i((new StringBuilder()).append("updateScreenshot ").append(updatedWidgets.size()).toString());
			if (updatedWidgets.isEmpty())
			{
				WidgetController2.logger.i("updatedWidgets.isEmpty()");
			} else
			{
				int j = 0;
				while (j < ids2widgets.size()) 
				{
					View view = (View)ids2widgets.valueAt(j);
					updateScreenshot(view, true);
					j++;
				}
			}
		}

			
			{
				this$0 = WidgetController2.this;
//				super();
			}
	};
	private Set updatedWidgets;
	private boolean wasShown;
	ShellAppWidgetHost widgetHost;

	public WidgetController2(Context context)
	{
		this(context, null);
	}

	public WidgetController2(Context context, AttributeSet attributeset)
	{
		this(context, attributeset, 0);
	}

	public WidgetController2(Context context, AttributeSet attributeset, int i)
	{
		super(context, attributeset, i);
		stopped = false;
		changesTrack = CollectionFactory.newLinkedList();
		touchLock = "widgetController";
		justAdded = CollectionFactory.newHashSet();
		wasShown = false;
		updatedWidgets = CollectionFactory.newHashSet();
		offscreens = CollectionFactory.newHashMap();
		childLock = new ReentrantLock();
		ids2widgets = CollectionFactory.newSparseArray();
		initOffscreen();
		handler = new Handler();
	}

	private void childLayout(View view)
	{
		boolean flag = true;
		LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
		logger.i((new StringBuilder()).append("Layouting child with lp:").append(layoutparams.toString()).toString());
		int i = layoutparams.x;
		int j = layoutparams.y;
		int k = layoutparams.width;
		int l = layoutparams.height;
		Logger logger1 = logger;
		Object aobj[] = new Object[5];
		aobj[0] = Integer.valueOf(i);
		aobj[1] = Integer.valueOf(j);
		aobj[2] = Integer.valueOf(i + k);
		aobj[3] = Integer.valueOf(j + l);
		if (view.getVisibility() != 0)
			flag = false;
		aobj[4] = Boolean.valueOf(flag);
		logger1.d(String.format("layout{cL=%d,cT=%d,cR=%d,cB=%d,isVisible=%b}", aobj));
		view.layout(i, j, i + k, j + l);
	}

	private void doMeasure(View view, LayoutParams layoutparams)
	{
		view.measure(android.view.View.MeasureSpec.makeMeasureSpec(layoutparams.width, 0x40000000), android.view.View.MeasureSpec.makeMeasureSpec(layoutparams.height, 0x40000000));
	}

	private void initOffscreen()
	{
		gEmptyBitmap = Bitmap.createBitmap(1, 1, android.graphics.Bitmap.Config.ARGB_8888);
		gOffscreen = Bitmap.createBitmap(1, 1, android.graphics.Bitmap.Config.ARGB_8888);
		gOffscreenCanvas = new Canvas(gOffscreen);
	}

	private void postUpdateScreenshots()
	{
		handler.removeCallbacks(updateScreenshots);
		handler.post(updateScreenshots);
	}

	private void updateWidget(int i)
	{
		switch (mixIdStrategy.getType(i)) {
		case 1:
			NativeCalls.UpdateWidget(mixIdStrategy.specIdByGeneral(i));
			break;
		case 2:
			NativeCalls.UpdatePanel(mixIdStrategy.specIdByGeneral(i));
			break;
			
		default:
			break;
		}
		return;
	}

	public void addWidget(View view, LayoutParams layoutparams)
	{
		Conditions.checkNotNull(view);
		Conditions.checkArgument(((LayoutParams)Conditions.checkNotNull(layoutparams)).isInitialised(), "");
		boolean flag;
		if (ids2widgets.get(layoutparams.id) == null)
			flag = true;
		else
			flag = false;
		Conditions.checkArgument(flag, "View with the same id already exists");
		view.setDrawingCacheEnabled(false);
		ids2widgets.append(layoutparams.getId(), view);
		addView(view, layoutparams);
		justAdded.add(Integer.valueOf(layoutparams.id));
	}

	public boolean changeWidgetSize(int i, int j, int k)
	{
		boolean flag = false;
		boolean flag1;
		LayoutParams layoutparams;
		if (ids2widgets.get(i) != null)
			flag1 = true;
		else
			flag1 = false;
		Conditions.checkArgument(flag1, (new StringBuilder()).append("View with id = ").append(i).append("doesn't exist").toString());
		layoutparams = (LayoutParams)((View)ids2widgets.get(i)).getLayoutParams();
		if (layoutparams.width != j || layoutparams.height != k)
		{
			wasShown = true;
			layoutparams.width = j;
			layoutparams.height = k;
			flag = true;
		}
		return flag;
	}

	public LayoutParams createLayoutParams(View view, int i, int j, int k)
	{
		LayoutParams layoutparams = new LayoutParams(i, j, k);
		doMeasure(view, layoutparams);
		return layoutparams;
	}

	protected boolean drawChild(Canvas canvas, View view, long l)
	{
		boolean flag;
		if (!updateScreenshot(view, true))
			flag = super.drawChild(canvas, view, l);
		else
			flag = super.drawChild(gOffscreenCanvas, view, l);
		return flag;
	}

	public void getLock()
	{
		childLock.lock();
	}

	public Bitmap getWidgetScreenShot(int i)
	{
		View view = (View)ids2widgets.get(i);
		boolean flag;
		LayoutParams layoutparams;
		Bitmap bitmap;
		if (view != null)
			flag = true;
		else
			flag = false;
		Conditions.checkArgument(flag);
		layoutparams = (LayoutParams)view.getLayoutParams();
		if (layoutparams.bitmap == null)
			bitmap = gEmptyBitmap;
		else
			bitmap = layoutparams.bitmap;
		return bitmap;
	}

	public void hideWidgets()
	{
		logger.i("!!!!!!!!hideWidgets");
		for (int i = 0; i < ids2widgets.size(); i++)
		{
			LayoutParams layoutparams = (LayoutParams)((View)ids2widgets.valueAt(i)).getLayoutParams();
			layoutparams.x = -1000 * (i + 1);
			layoutparams.y = -10000;
			logger.i((new StringBuilder()).append("hideWidget ").append(layoutparams).toString());
		}

		post(layoutAction);
	}

	public ViewParent invalidateChildInParent(int ai[], Rect rect)
	{
		logger.i((new StringBuilder()).append("invalidateChildInParent ").append(rect.toShortString()).toString());
		Rect rect1 = new Rect(ai[0] + rect.left, ai[1] + rect.top, ai[0] + rect.right, ai[1] + rect.bottom);
		for (int i = 0; i < ids2widgets.size(); i++)
		{
			View view = (View)ids2widgets.valueAt(i);
			Rect rect2 = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
			logger.i((new StringBuilder()).append("intersect with ").append(rect.toShortString()).toString());
			if (Rect.intersects(rect2, rect1))
				updateChild(view);
		}

		return super.invalidateChildInParent(ai, rect);
	}

	public LayoutParams makeLayoutParams(View view, AppWidgetProviderInfo appwidgetproviderinfo, int i)
	{
		Conditions.checkNotNull(view);
		Conditions.checkNotNull(appwidgetproviderinfo);
		return createLayoutParams(view, appwidgetproviderinfo.minWidth, appwidgetproviderinfo.minHeight, i);
	}

	public int onIdle()
	{
		return 0;
	}

	public boolean onInterceptTouchEvent(MotionEvent motionevent)
	{
		boolean flag = false;
		if (movementController != null) 
			{
				int i;
				float f;
				float f1;
				boolean flag1;
				if (movementController.isProcessedByOther(touchLock))
				{
					lastMotionX = 3.402823E+038F;
				}
				i = motionevent.getAction();
				f = motionevent.getX();
				f1 = motionevent.getY();
				flag1 = false;
				switch (i) {
				case 0:
					
					break;
				case 1:
					
					break;
				case 2:
					if ((int)Math.abs(f - lastMotionX) > touchSlop)
						flag1 = true;
					else
						flag1 = false;
					logger.i((new StringBuilder()).append("returning :").append(flag1).toString());
					flag = flag1;
					  
					break;
				default:
					break;
				}
				return flag;
			}
		else 
			{
			logger.e("movementController isn't initialized");
			}
		return flag;
	}

	protected void onLayout(boolean flag, int i, int j, int k, int l)
	{
		if (mainView == null)
		{
			logger.e("trying to layout before GLview were set");
		} else
		{
			mainView.layout(0, 0, k - i, l - j);
			int i1 = 0;
			while (i1 < ids2widgets.size()) 
			{
				childLayout((View)ids2widgets.valueAt(i1));
				i1++;
			}
		}
	}

	protected void onMeasure(int i, int j)
	{
		super.onMeasure(i, j);
		for (int k = 0; k < ids2widgets.size(); k++)
		{
			View view = (View)ids2widgets.valueAt(k);
			doMeasure(view, (LayoutParams)view.getLayoutParams());
		}

	}

	public boolean onTouchEvent(MotionEvent motionevent)
	{
		if (lastMotionX != 3.402823E+038F)
		{
			Home.sendMouseEvent(0, (int)lastMotionX, (int)lastMotionY);
			movementController.process(touchLock);
			lastMotionX = 3.402823E+038F;
		}
		movementController.processTouch(touchLock, motionevent, mainView);
		return true;
	}

	public void recreateWidgets()
	{
		logger.i("qqqq recreateWidgets ");
		getContext().sendBroadcast(new Intent("sec.android.intent.action.HOME_RESUME"));
		AppWidgetManager appwidgetmanager = AppWidgetManager.getInstance(getContext());
		int i = 0;
		while (i < ids2widgets.size()) 
		{
			View view = (View)ids2widgets.valueAt(i);
			LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
			if (mixIdStrategy.getType(layoutparams.getId()) != 2)
			{
				AppWidgetProviderInfo appwidgetproviderinfo = appwidgetmanager.getAppWidgetInfo(layoutparams.appWidgetId);
				if (appwidgetproviderinfo != null)
				{
					removeView(view);
					android.appwidget.AppWidgetHostView appwidgethostview = widgetHost.createView(home, layoutparams.appWidgetId, appwidgetproviderinfo);
					doMeasure(appwidgethostview, layoutparams);
					addView(appwidgethostview, layoutparams);
					ids2widgets.setValueAt(i, appwidgethostview);
				}
			}
			i++;
		}
		logger.i("qqqq <<recreateWidgets ");
	}

	public void releaseLock()
	{
		childLock.unlock();
	}

	public LayoutParams removeWidget(final int id)
	{
		return (LayoutParams)ConcurrentUtil.runSynchronouslyInUiThread(new Callable() {

			final WidgetController2 this$0;
			final int val$id;

			public LayoutParams call()
				throws Exception
			{
				View view = (View)ids2widgets.get(id);
				LayoutParams layoutparams;
				if (view == null)
				{
					layoutparams = null;
				} else
				{
					ids2widgets.delete(id);
					updatedWidgets.remove(Integer.valueOf(id));
					layoutparams = (LayoutParams)view.getLayoutParams();
					requestLayout();
					removeView(view);
				}
				return layoutparams;
			}
			{
				this$0 = WidgetController2.this;
				val$id = id ;
//				super();
			}
		}, 500L);
	}

	public List removeWidgets(String s)
	{
		Conditions.checkNotNull(s);
		java.util.LinkedList linkedlist = CollectionFactory.newLinkedList();
		AppWidgetManager appwidgetmanager = AppWidgetManager.getInstance(getContext());
		for (int i = 0; i < ids2widgets.size(); i++)
		{
			View view = (View)ids2widgets.valueAt(i);
			LayoutParams layoutparams1 = (LayoutParams)view.getLayoutParams();
			AppWidgetProviderInfo appwidgetproviderinfo = appwidgetmanager.getAppWidgetInfo(layoutparams1.appWidgetId);
			if (appwidgetproviderinfo == null || s.equals(appwidgetproviderinfo.provider.getPackageName()))
			{
				linkedlist.add(layoutparams1);
				Integer integer = Integer.valueOf(layoutparams1.id);
				updatedWidgets.remove(integer);
				removeView(view);
			}
		}

		if (!linkedlist.isEmpty())
		{
			LayoutParams layoutparams;
			for (Iterator iterator = linkedlist.iterator(); iterator.hasNext(); ids2widgets.remove(layoutparams.id))
				layoutparams = (LayoutParams)iterator.next();

			requestLayout();
		}
		logger.i((new StringBuilder()).append("removed appWidget ").append(linkedlist.size()).toString());
		return linkedlist;
	}

	public void restoreWidget(View view, BaseWidgetInfo basewidgetinfo)
	{
		logger.i((new StringBuilder()).append("restoreWidget ").append(basewidgetinfo.getAppWidgetId()).append(", w=").append(basewidgetinfo.getMinWidth()).append(", h=").append(basewidgetinfo.getMinHeight()).toString());
		Conditions.checkNotNull(view);
		Conditions.checkNotNull(basewidgetinfo);
		LayoutParams layoutparams = createLayoutParams(view, basewidgetinfo.getMinWidth(), basewidgetinfo.getMinHeight(), basewidgetinfo.getAppWidgetId());
		layoutparams.setId(basewidgetinfo.getId());
		addWidget(view, layoutparams);
		justAdded.clear();
	}

	public void setMainView(View view, com.softspb.shell.Home.MovementController movementcontroller)
	{
		touchSlop = ViewConfiguration.getTouchSlop();
		mainView = (View)Conditions.checkNotNull(view);
		movementController = (com.softspb.shell.Home.MovementController)Conditions.checkNotNull(movementcontroller);
	}

	public void setWidgetHost(ShellAppWidgetHost shellappwidgethost, Home home1)
	{
		widgetHost = shellappwidgethost;
		home = home1;
	}

	public boolean showWidget(int i, int j, int k)
	{
		boolean flag = false;
		boolean flag1;
		LayoutParams layoutparams;
		if (ids2widgets.get(i) != null)
			flag1 = true;
		else
			flag1 = false;
		Conditions.checkArgument(flag1, (new StringBuilder()).append("View with id = ").append(i).append("doesn't exist").toString());
		layoutparams = (LayoutParams)((View)ids2widgets.get(i)).getLayoutParams();
		logger.i((new StringBuilder()).append("showWidget (").append(layoutparams.x).append(", ").append(layoutparams.y).append(") -> (").append(j).append(", ").append(k).append("), [").append(layoutparams.width).append(", ").append(layoutparams.height).append("]").toString());
		if (layoutparams.x != j || layoutparams.y != k)
		{
			wasShown = true;
			layoutparams.x = j;
			layoutparams.y = k;
			flag = true;
		}
		return flag;
	}

	public void start()
	{
		stopped = false;
	}

	public void stop()
	{
		stopped = true;
	}

	public void updateChild(View view)
	{
		LayoutParams layoutparams = (LayoutParams)view.getLayoutParams();
		if (layoutparams != null)
		{
			if (layoutparams.x < 0)
				ViewUtils.clearAnimation(view);
			updatedWidgets.add(Integer.valueOf(layoutparams.getId()));
			logger.i((new StringBuilder()).append("updateChild").append(layoutparams.getId()).toString());
			if (!stopped)
				postUpdateScreenshots();
		}
		else 
		{
			return;
		}
	}

	public boolean updateScreenshot(View view, boolean flag)
	{
		boolean flag1;
		Point point;
		android.view.ViewGroup.LayoutParams layoutparams;
		flag1 = true;
		logger.i("updateScreenshot");
		point = new Point(view.getWidth(), view.getHeight());
		layoutparams = view.getLayoutParams();
		if ((layoutparams instanceof LayoutParams) && point.x > 0 && point.y > 0) 
			{
			LayoutParams layoutparams1 = (LayoutParams)layoutparams;
			if (updatedWidgets.remove(Integer.valueOf(layoutparams1.getId())))
			{
				logger.i((new StringBuilder()).append("Draw child Bitmap: ").append(layoutparams1.getId()).append(" (").append(point.x).append(",").append(point.y).append(")").toString());
				Bitmap bitmap = (Bitmap)offscreens.get(point);
				Bitmap bitmap1;
				if (bitmap == null)
				{
					logger.i("Bitmap.createBitmap");
					bitmap = Bitmap.createBitmap(point.x, point.y, android.graphics.Bitmap.Config.ARGB_8888);
				} else
				{
					bitmap.eraseColor(0);
				}
				view.draw(new Canvas(bitmap));
				getLock();
				bitmap1 = layoutparams1.bitmap;
				layoutparams1.bitmap = bitmap;
				releaseLock();
				if (flag)
					updateWidget(layoutparams1.getId());
				if (bitmap1 == null)
				{
					offscreens.remove(point);
				} else
				{
					Point point1 = new Point(bitmap1.getWidth(), bitmap1.getHeight());
					if (point.equals(point1))
					{
						offscreens.put(point, bitmap1);
					} else
					{
						offscreens.remove(point);
						bitmap1.recycle();
						logger.i((new StringBuilder()).append("<<Change size ").append(point).append(" ").append(point1).toString());
					}
				}
			}
			}
		else 
			{
				flag1 = false;
			}
		return flag1;
	}

	public void waitForShowWidgets()
	{
		logger.i("!!!!!!!!waitForShowWidgets");
		if (!wasShown)
		{
			logger.i("!!!!!!!! NOSHOW");
		} else
		{
			wasShown = false;
			post(layoutAction);
		}
	}




}
