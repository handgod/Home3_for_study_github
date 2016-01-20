// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.util;

import android.content.*;
import java.lang.ref.WeakReference;
import java.util.*;

public class CrossProcessusPreferences
	implements SharedPreferences
{
	class EditorNotifier
		implements android.content.SharedPreferences.Editor
	{

		HashSet changedKeys;
		android.content.SharedPreferences.Editor mEditor;
		final CrossProcessusPreferences this$0;

		public void apply()
		{
			throw new UnsupportedOperationException();
		}

		public android.content.SharedPreferences.Editor clear()
		{
			mEditor.clear();
			changedKeys.addAll(getAll().keySet());
			return this;
		}

		public boolean commit()
		{
			boolean flag = mEditor.commit();
			if (flag)
				broadcastPrefsChanged(changedKeys);
			return flag;
		}

		public android.content.SharedPreferences.Editor putBoolean(String s, boolean flag)
		{
			mEditor.putBoolean(s, flag);
			changedKeys.add(s);
			return this;
		}

		public android.content.SharedPreferences.Editor putFloat(String s, float f)
		{
			mEditor.putFloat(s, f);
			changedKeys.add(s);
			return this;
		}

		public android.content.SharedPreferences.Editor putInt(String s, int i)
		{
			mEditor.putInt(s, i);
			changedKeys.add(s);
			return this;
		}

		public android.content.SharedPreferences.Editor putLong(String s, long l)
		{
			mEditor.putLong(s, l);
			changedKeys.add(s);
			return this;
		}

		public android.content.SharedPreferences.Editor putString(String s, String s1)
		{
			mEditor.putString(s, s1);
			changedKeys.add(s);
			return this;
		}

		public android.content.SharedPreferences.Editor putStringSet(String s, Set set)
		{
			throw new UnsupportedOperationException();
		}

		public android.content.SharedPreferences.Editor remove(String s)
		{
			mEditor.remove(s);
			changedKeys.add(s);
			return this;
		}

		public EditorNotifier(android.content.SharedPreferences.Editor editor)
		{
			super();
			this$0 = CrossProcessusPreferences.this;
			changedKeys = new HashSet();
			mEditor = editor;
		}
	}

	static class PrefsChangedReceiver extends BroadcastReceiver
	{

		WeakReference ref;

		public void onReceive(Context context, Intent intent)
		{
			String as[];
			CrossProcessusPreferences crossprocessuspreferences = (CrossProcessusPreferences)ref.get();
			if (crossprocessuspreferences != null) 
				{
				if ((as = intent.getStringArrayExtra("changed_keys")) != null)
				{
					int i = as.length;
					int j = 0;
					while (j < i) 
					{
						crossprocessuspreferences.notifyPrefsChanged(as[j]);
						j++;
					}
				}
				}
			
			return;
		}

		public PrefsChangedReceiver(CrossProcessusPreferences crossprocessuspreferences)
		{
			ref = new WeakReference(crossprocessuspreferences);
		}
	}


	private static final String CHANGED_KEYS = "changed_keys";
	protected String mAction;
	private final LinkedList mListeners = new LinkedList();
	protected String mPrefsName;
	protected Context targetContext;
	PrefsChangedReceiver updatePrefsReceiver;

	public CrossProcessusPreferences(Context context, String s, String s1)
	{
		updatePrefsReceiver = new PrefsChangedReceiver(this);
		try
		{
			targetContext = context.createPackageContext(s, 0);
		}
		catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception)
		{
			targetContext = context;
		}
		mPrefsName = s1;
		mAction = (new StringBuilder()).append(targetContext.getPackageName()).append(".action.UPDATE_PREFS.").append(mPrefsName).toString();
		targetContext.registerReceiver(updatePrefsReceiver, new IntentFilter(mAction));
	}

	private SharedPreferences getSharedPreferences()
	{
		return targetContext.getSharedPreferences(mPrefsName, 0);
	}

	void broadcastPrefsChanged(HashSet hashset)
	{
		if (targetContext != null)
		{
			Intent intent = new Intent(mAction);
			intent.putExtra("changed_keys", (String[])hashset.toArray(new String[hashset.size()]));
			targetContext.sendBroadcast(intent);
		}
	}

	public boolean contains(String s)
	{
		SharedPreferences sharedpreferences = getSharedPreferences();
		boolean flag;
		if (sharedpreferences != null && sharedpreferences.contains(s))
			flag = true;
		else
			flag = false;
		return flag;
	}

	public void dispose()
	{
		targetContext.unregisterReceiver(updatePrefsReceiver);
	}

	public android.content.SharedPreferences.Editor edit()
	{
		return new EditorNotifier(getSharedPreferences().edit());
	}

	public Map getAll()
	{
		SharedPreferences sharedpreferences = getSharedPreferences();
		Map map;
		if (sharedpreferences != null)
			map = sharedpreferences.getAll();
		else
			map = Collections.emptyMap();
		return map;
	}

	public boolean getBoolean(String s, boolean flag)
	{
		SharedPreferences sharedpreferences = getSharedPreferences();
		if (sharedpreferences != null)
			flag = sharedpreferences.getBoolean(s, flag);
		return flag;
	}

	public float getFloat(String s, float f)
	{
		SharedPreferences sharedpreferences = getSharedPreferences();
		if (sharedpreferences != null)
			f = sharedpreferences.getFloat(s, f);
		return f;
	}

	public int getInt(String s, int i)
	{
		SharedPreferences sharedpreferences = getSharedPreferences();
		if (sharedpreferences != null)
			i = sharedpreferences.getInt(s, i);
		return i;
	}

	public long getLong(String s, long l)
	{
		
		SharedPreferences sharedpreferences;
		sharedpreferences = getSharedPreferences();
		if (sharedpreferences == null)
		{
		long l1 = sharedpreferences.getLong(s, l);
		l = l1;
	
		}
		return l;
	}

	public String getString(String s, String s1)
	{
		SharedPreferences sharedpreferences = getSharedPreferences();
		if (sharedpreferences != null)
			s1 = sharedpreferences.getString(s, s1);
		return s1;
	}

	public Set getStringSet(String s, Set set)
	{
		throw new UnsupportedOperationException();
	}

	void notifyPrefsChanged(String s)
	{
		getAll().get(s);
		LinkedList linkedlist = mListeners;
		synchronized (linkedlist) {
			for (Iterator iterator = mListeners.iterator(); iterator.hasNext(); ((android.content.SharedPreferences.OnSharedPreferenceChangeListener)iterator.next()).onSharedPreferenceChanged(this, s));
		}		
	}

	public void registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener onsharedpreferencechangelistener)
	{
		SharedPreferences sharedpreferences = getSharedPreferences();
		if (sharedpreferences != null)
		{
			sharedpreferences.registerOnSharedPreferenceChangeListener(onsharedpreferencechangelistener);
			LinkedList linkedlist = mListeners;
			synchronized (linkedlist) {
				if (!mListeners.contains(onsharedpreferencechangelistener))
					mListeners.add(onsharedpreferencechangelistener);
			}
		}
	}

	public void unregisterOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener onsharedpreferencechangelistener)
	{
		SharedPreferences sharedpreferences = getSharedPreferences();
		if (sharedpreferences == null)
		{
			sharedpreferences.unregisterOnSharedPreferenceChangeListener(onsharedpreferencechangelistener);
			LinkedList linkedlist = mListeners;
			synchronized (linkedlist) {
				mListeners.remove(onsharedpreferencechangelistener);	
			}
		}
		
		
	}
}
