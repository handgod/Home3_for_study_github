// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.opengl;


// Referenced classes of package com.softspb.shell.opengl:
//			NativeCallbacks

public class NativeCalls
{

	public NativeCalls()
	{
	}

	public static native void AddWidget(int i, boolean flag, int j, int k, int l, String s, String s1, int i1);

	public static native void DeleteWidget(int i, int j);

	public static native void InitWidget(int i, int j, int k, int l, String s, String s1, int i1);

	public static native void UpdatePanel(int i);

	public static native void UpdateWidget(int i);

	public static native void handleSearchRequest();

	public static native void nativeDone(NativeCallbacks nativecallbacks);

	public static native void nativeInit(NativeCallbacks nativecallbacks);

	public static native void notifyContactsUpdate(int i, int j, int k);

	public static native void onHomePressed();
}
