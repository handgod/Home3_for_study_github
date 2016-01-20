// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

import android.content.SharedPreferences;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.android.vending.licensing:
//			ValidationException, Obfuscator

public class PreferenceObfuscator
{

	private static Logger logger = Loggers.getLogger(com.android.vending.licensing.PreferenceObfuscator.class);
	private android.content.SharedPreferences.Editor mEditor;
	private final Obfuscator mObfuscator;
	private final SharedPreferences mPreferences;

	public PreferenceObfuscator(SharedPreferences sharedpreferences, Obfuscator obfuscator)
	{
		mPreferences = sharedpreferences;
		mObfuscator = obfuscator;
		mEditor = null;
	}

	public void commit()
	{
		if (mEditor != null)
		{
			mEditor.commit();
			mEditor = null;
		}
	}
	
	public String getString(String key, String defValue)
	{    
		String result;    
		String value = mPreferences.getString(key, null);    
		if (value != null)
		{           
			try {  
				result = mObfuscator.unobfuscate(value); 
				} catch (ValidationException e) {  
					// Unable to unobfuscate, data corrupt or tampered     
					logger.w("Validation error while reading preference: " + key);  
					result = defValue;    
					}    
			} else {      
				// Preference not found  
				result = defValue;    
				}   
		return result;    
		}   
	public void putString(String s, String s1)
	{
		if (mEditor == null)
			mEditor = mPreferences.edit();
		String s2 = mObfuscator.obfuscate(s1);
		mEditor.putString(s, s2);
	}

}

