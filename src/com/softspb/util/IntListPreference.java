// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

// Referenced classes of package com.softspb.util:
//			IIntListPreference

public class IntListPreference extends ListPreference
	implements IIntListPreference
{

	int mValue;

	public IntListPreference(Context context)
	{
		super(context);
	}

	public IntListPreference(Context context, AttributeSet attributeset)
	{
		super(context, attributeset);
	}

	protected void onSetInitialValue(boolean flag, Object obj)
	{
		if (flag)
			setValue(getPersistedInt(mValue));
		else
			setValue((String)obj);
	}

	protected boolean persistString(String s)
	{
		return true;
	}

	public void setValue(int i)
	{
		setValue(Integer.toString(i));
	}

	public void setValue(String s)
	{
		super.setValue(s);
		mValue = Integer.parseInt(s);
		persistInt(mValue);
	}
}
