// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.util.orm.ann;

import java.lang.annotation.Annotation;

public interface PrimaryKey
	extends Annotation
{

	public abstract boolean autoincrement();
}
