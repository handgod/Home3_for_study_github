// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;

import android.content.Intent;
import java.util.Collection;

// Referenced classes of package com.spb.programlist:
//			Tags, TagInfo

class TagSources
{
	static interface TagSource
	{

		public abstract Collection getTags(ITagFactory itagfactory);
	}

	static interface ITagFactory
	{

		public abstract TagInfo create(String s, Collection collection);

		public abstract IntentPattern.PatternBuilder getPatternBuilder(Intent intent);
	}


	private TagSources()
	{
	}

	static Collection getTags(ITagFactory itagfactory)
	{
		return (new Tags()).getTags(itagfactory);
	}
}
