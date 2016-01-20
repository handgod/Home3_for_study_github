// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.opengl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
public class MyGlSurfaceView extends SurfaceView
	implements android.view.SurfaceHolder.Callback
{
	class SurfaceChangedCallback
		implements Runnable
	{

		int format;
		int h;
		SurfaceHolder holder;
		final MyGlSurfaceView this$0;
		int w;

		public void run()
		{
			if (awaitingSurfaceChange)
			{
				holder = getHolder();
				DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
				if (getResources().getDisplayMetrics().widthPixels == w)
				{
					System.out.println((new StringBuilder()).append("surfaceChanged (").append(w).append(" ").append(h).append(") (").append(displaymetrics.widthPixels).append(" ").append(displaymetrics.heightPixels).append(")").toString());
					nContextSetSurface(w, h, holder.getSurface(), format, displaymetrics.widthPixels, displaymetrics.heightPixels);
					awaitingSurfaceChange = false;
				} else
				{
					handler.postDelayed(this, 100L);
				}
			}
		}

		public SurfaceChangedCallback(int i, int j, int k)
		{
//			super();
			this$0 = MyGlSurfaceView.this;
			w = i;
			h = j;
			format = k;
		}
	}


	private static String LOG_TAG = "From java";
	volatile boolean awaitingSurfaceChange;
	Handler handler;
	private boolean mEnabled;
	private SurfaceHolder mSurfaceHolder;

	public MyGlSurfaceView(Context context)
	{
		this(context, null);
	}

	public MyGlSurfaceView(Context context, AttributeSet attributeset)
	{
		super(context, attributeset);
		mEnabled = false;
		nContextCreate();
		init();
	}

	private void init()
	{
		SurfaceHolder surfaceholder = getHolder();
		surfaceholder.addCallback(this);
		surfaceholder.setFormat(4);
		handler = new Handler();
	}

	public void destroy()
	{
		nContextDestroy();
	}

	public void enableS3D(boolean flag)
	{
		boolean flag1;
		mEnabled = flag;
		Log.i("enableS3D", (new StringBuilder()).append("Device model: ").append(Build.DEVICE).toString());
		if (!Build.DEVICE.equals("p920"))
		{
			
			Surface surface;
			Log.i("enableS3D", "Trying HTC S3D");
			flag1 = true;
			surface = getHolder().getSurface();
			Method method = null;
			int i = 0;
			int j = 0;
			Object aobj[];
			Class class1 = null;
			try {
				class1 = Class.forName("com.htc.view.DisplaySetting");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Class aclass[] = new Class[2];
			aclass[0] = Surface.class;
			aclass[1] = Integer.TYPE;
			try {
				method = class1.getMethod("setStereoscopic3DFormat", aclass);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				i = class1.getField("STEREOSCOPIC_3D_FORMAT_SIDE_BY_SIDE").getInt(null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				j = class1.getField("STEREOSCOPIC_3D_FORMAT_OFF").getInt(null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			aobj = new Object[2];
			aobj[0] = surface;
			boolean flag2 = false;
			if (!flag)
				i = j;
			aobj[1] = Integer.valueOf(i);
			try {
				flag2 = ((Boolean)method.invoke(null, aobj)).booleanValue();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flag1 = flag2;
		}
		else 
		{
			return;
		}
		
		Log.i("enableS3D", (new StringBuilder()).append("HTC return value:").append(flag1).toString());
		if (!flag1)
			Log.i("enableS3D", "S3D format not supported");
		return;

	}

	public boolean isEnableS3D()
	{
		return mEnabled;
	}

	native void nContextCreate();

	native void nContextDestroy();

	native void nContextPause();

	native void nContextResume();

	native void nContextSetPriority(int i);

	native void nContextSetSurface(int i, int j, Surface surface, int k, int l, int i1);

	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();
	}

	public void onPause()
	{
		nContextPause();
	}

	public void onResume()
	{
		nContextResume();
		if (mEnabled)
			enableS3D(mEnabled);
	}

	public void queueEvent(Runnable runnable)
	{
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k)
	{
		DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
		if (android.os.Build.VERSION.SDK_INT == 13 || android.os.Build.VERSION.SDK_INT > 13 && (float)displaymetrics.widthPixels >= 600F * displaymetrics.density || displaymetrics.heightPixels != k)
			if (getResources().getDisplayMetrics().widthPixels == j)
			{
				System.out.println((new StringBuilder()).append("surfaceChanged (").append(j).append(" ").append(k).append(") (").append(displaymetrics.widthPixels).append(" ").append(displaymetrics.heightPixels).append(")").toString());
				nContextSetSurface(j, k, surfaceholder.getSurface(), i, displaymetrics.widthPixels, displaymetrics.heightPixels);
				awaitingSurfaceChange = false;
			} else
			{
				awaitingSurfaceChange = true;
				handler.postDelayed(new SurfaceChangedCallback(j, k, i), 100L);
			}
	}

	public void surfaceCreated(SurfaceHolder surfaceholder)
	{
		mSurfaceHolder = surfaceholder;
	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder)
	{
		nContextSetSurface(0, 0, null, 0, 0, 0);
	}

}
