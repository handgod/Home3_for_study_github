// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;


// Referenced classes of package com.spb.programlist:
//			ProgramInfo

public interface ProgramListListener
{

	public abstract void onAdded(ProgramInfo programinfo);

	public abstract void onDeleted(String s);
}
