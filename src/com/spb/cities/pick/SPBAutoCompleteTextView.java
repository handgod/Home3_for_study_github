// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.pick;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import com.softspb.kana.Characters;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class SPBAutoCompleteTextView extends AutoCompleteTextView
{
	private static class KeyInterceptor
		implements android.view.View.OnKeyListener
	{

		public boolean onKey(View view, int i, KeyEvent keyevent)
		{
			SPBAutoCompleteTextView.logger.d((new StringBuilder()).append("Intercepted key: keyCode=").append(i).append(" event=").append(keyevent).append(" view=").append(view).toString());
			return false;
		}

		private KeyInterceptor()
		{
		}
	}

	public static interface OnBackKeyListener
	{

		public abstract void onBackKey();
	}


	static Logger logger = Loggers.getLogger("City");
	private CharSequence filterText;
	OnBackKeyListener mBackKeyListener;
	KeyInterceptor mKeyInterceptor;

	public SPBAutoCompleteTextView(Context context)
	{
		super(context);
	}

	public SPBAutoCompleteTextView(Context context, AttributeSet attributeset)
	{
		super(context, attributeset);
	}

	public SPBAutoCompleteTextView(Context context, AttributeSet attributeset, int i)
	{
		super(context, attributeset, i);
	}

	private static String logUnicode(String s)
	{
		String s1;
		if (s == null)
		{
			s1 = "null";
		} else
		{
			int i = s.length();
			int j = 0;
			StringBuilder stringbuilder = new StringBuilder();
			for (; j < i; j = s.offsetByCodePoints(j, 1))
				stringbuilder.append("\\u").append(Integer.toHexString(s.codePointAt(j)));

			s1 = stringbuilder.toString();
		}
		return s1;
	}

	public void dismissDropDown()
	{
		StackTraceElement astacktraceelement[];
		logger.d("dismissDropDown");
		Exception exception = new Exception();
		exception.fillInStackTrace();
		astacktraceelement = exception.getStackTrace();
		if (astacktraceelement.length < 2) 
		{
			super.dismissDropDown();
		}
		else 
		{
			StackTraceElement stacktraceelement = astacktraceelement[1];
			if (!"android.widget.AutoCompleteTextView".equals(stacktraceelement.getClassName()) || !"onWindowFocusChanged".equals(stacktraceelement.getMethodName())) 
				super.dismissDropDown();
			else 
				logger.d("Ignoring dismissDropDown");

		}
		return;
	}

	public boolean enoughToFilter()
	{
		return true;
	}

	CharSequence getFilterText()
	{
		return filterText;
	}

	public boolean onKeyPreIme(int i, KeyEvent keyevent)
	{
		boolean flag = true;
		logger.d((new StringBuilder()).append("SPBAutoCompleteTextView.onKeyPreIme: keyCode=").append(i).append(" event=").append(keyevent).toString());
		if (i == 4)
		{
			if (keyevent.getAction() == 1 && mBackKeyListener != null)
				mBackKeyListener.onBackKey();
		} else
		{
			flag = super.onKeyPreIme(i, keyevent);
		}
		return flag;
	}

	public void onWindowFocusChanged(boolean flag)
	{
		logger.d((new StringBuilder()).append("AutoCompleteTextView.onWindowFocusChanged: hasWindowFocus=").append(flag).toString());
		super.onWindowFocusChanged(flag);
		if (flag)
			showDropDown();
	}

	protected void performFiltering(CharSequence charsequence, int i)
	{
		CharSequence charsequence1 = Characters.fullWidthLatinToAsciiLatin(charsequence);
		logger.d((new StringBuilder()).append("City: performFiltering text=").append(logUnicode(charsequence1.toString())).append(" keyCode=").append(i).toString());
		filterText = charsequence1;
		super.performFiltering(charsequence1, i);
	}

	public void setOnBackKeyListener(OnBackKeyListener onbackkeylistener)
	{
		mBackKeyListener = onbackkeylistener;
	}

}
