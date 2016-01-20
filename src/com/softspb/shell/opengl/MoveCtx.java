// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.opengl;


public class MoveCtx
{

	public boolean bFirst;
	boolean bMove;
	public boolean bStop;
	public boolean bUp;
	public long dt;
	private long mTime;
	public int ptFromX;
	public int ptFromY;
	public int ptStartX;
	public int ptStartY;
	public int ptToX;
	public int ptToY;
	public int vx;
	public int vy;

	public MoveCtx()
	{
	}

	public void down(int i, int j)
	{
		bMove = true;
		ptStartX = i;
		ptStartY = j;
		ptFromX = i;
		ptFromY = j;
		ptToX = i;
		ptToY = j;
		bFirst = true;
		bStop = false;
		bUp = false;
		vx = 0;
		vy = 0;
		mTime = System.currentTimeMillis();
	}

	public void move(int i, int j)
	{
		if (!bMove)
		{
			ptFromX = ptToX;
			ptFromY = ptToY;
			bMove = true;
		}
		ptToX = i;
		ptToY = j;
	}

	void speedRecalc()
	{
		long l = System.currentTimeMillis();
		dt = l - mTime;
		if (dt < 5L)
			dt = 5L;
		if (dt > 200L)
		{
			vx = (int)((long)(1000 * (ptToX - ptFromX)) / dt);
			vy = (int)((long)(1000 * (ptToY - ptFromY)) / dt);
		} else
		{
			vx = (int)(((long)vx * (200L - dt) + (long)(1000 * (ptToX - ptFromX))) / 200L);
			vy = (int)(((long)vy * (200L - dt) + (long)(1000 * (ptToY - ptFromY))) / 200L);
		}
		mTime = l;
	}

	public void up(int i, int j)
	{
		if (!bMove)
		{
			ptFromX = ptToX;
			ptFromY = ptToY;
			bMove = true;
		}
		ptToX = i;
		ptToY = j;
		bUp = true;
	}
}
