// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

import android.content.Context;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

// Referenced classes of package com.android.vending.licensing:
//			Policy, PreferenceObfuscator, ResponseData, Obfuscator

public class ServerManagedPolicy
	implements Policy
{

	private static final String DEFAULT_MAX_RETRIES = "0";
	private static final String DEFAULT_RETRY_COUNT = "0";
	private static final String DEFAULT_RETRY_UNTIL = "0";
	private static final String DEFAULT_VALIDITY_TIMESTAMP = "0";
	private static final long MILLIS_PER_MINUTE = 60000L;
	private static final String PREFS_FILE = "com.android.vending.licensing.ServerManagedPolicy";
	private static final String PREF_LAST_RESPONSE = "lastResponse";
	private static final String PREF_MAX_RETRIES = "maxRetries";
	private static final String PREF_RETRY_COUNT = "retryCount";
	private static final String PREF_RETRY_UNTIL = "retryUntil";
	private static final String PREF_VALIDITY_TIMESTAMP = "validityTimestamp";
	private static Logger logger = Loggers.getLogger(com.android.vending.licensing.ServerManagedPolicy.class);
	private Policy.LicenseResponse mLastResponse;
	private long mLastResponseTime;
	private long mMaxRetries;
	private PreferenceObfuscator mPreferences;
	private long mRetryCount;
	private long mRetryUntil;
	private long mValidityTimestamp;

	public ServerManagedPolicy(Context context, Obfuscator obfuscator)
	{
		mLastResponseTime = 0L;
		mPreferences = new PreferenceObfuscator(context.getSharedPreferences("com.android.vending.licensing.ServerManagedPolicy", 0), obfuscator);
		mLastResponse = Policy.LicenseResponse.valueOf(mPreferences.getString("lastResponse", Policy.LicenseResponse.RETRY.toString()));
		mValidityTimestamp = Long.parseLong(mPreferences.getString("validityTimestamp", "0"));
		mRetryUntil = Long.parseLong(mPreferences.getString("retryUntil", "0"));
		mMaxRetries = Long.parseLong(mPreferences.getString("maxRetries", "0"));
		mRetryCount = Long.parseLong(mPreferences.getString("retryCount", "0"));
	}

	private Map decodeExtras(String s)
	{
		HashMap hashmap = new HashMap();
		try
		{
			NameValuePair namevaluepair;
			for (Iterator iterator = URLEncodedUtils.parse(new URI((new StringBuilder()).append("?").append(s).toString()), "UTF-8").iterator(); iterator.hasNext(); hashmap.put(namevaluepair.getName(), namevaluepair.getValue()))
				namevaluepair = (NameValuePair)iterator.next();

		}
		catch (URISyntaxException urisyntaxexception)
		{
			logger.w("Invalid syntax error while decoding extras data from server.");
		}
		return hashmap;
	}

	private void setLastResponse(Policy.LicenseResponse licenseresponse)
	{
		logger.d((new StringBuilder()).append("setLastResponse: mLastResponse=").append(licenseresponse).toString());
		mLastResponseTime = System.currentTimeMillis();
		mLastResponse = licenseresponse;
		mPreferences.putString("lastResponse", licenseresponse.toString());
	}
	private void setMaxRetries(String maxRetries) {  
		Long lMaxRetries;   
		try { 
			lMaxRetries = Long.parseLong(maxRetries);   
			} catch (NumberFormatException e) {    
				// No response or not parsable, expire immediately    
				logger.w("Licence retry count (GR) missing, grace period disabled");   
				maxRetries = "0";   
				lMaxRetries = 0l;    
				}     
		mMaxRetries = lMaxRetries; 
		mPreferences.putString(PREF_MAX_RETRIES, maxRetries); 
		}   
	
	private void setRetryCount(long l)
	{
		mRetryCount = l;
		mPreferences.putString("retryCount", Long.toString(l));
	}

	private void setRetryUntil(String retryUntil) {  
		Long lRetryUntil;    
		try {       
			lRetryUntil = Long.parseLong(retryUntil); 
			} catch (NumberFormatException e) { 
				// No response or not parsable, expire immediately    
				logger.w("License retry timestamp (GT) missing, grace period disabled");  
				retryUntil = "0";        
				lRetryUntil = 0l;   
				}        
		mRetryUntil = lRetryUntil;   
		mPreferences.putString(PREF_RETRY_UNTIL, retryUntil);
		}   
	private void setValidityTimestamp(String validityTimestamp) { 
		Long lValidityTimestamp;    
		try {        
			lValidityTimestamp = Long.parseLong(validityTimestamp);   
			} catch (NumberFormatException e) {  
				// No response or not parsable, expire in one minute.     
				logger.w("License validity timestamp (VT) missing, caching for a minute");    
				lValidityTimestamp = System.currentTimeMillis() + MILLIS_PER_MINUTE;  
				validityTimestamp = Long.toString(lValidityTimestamp);    
				}              mValidityTimestamp = lValidityTimestamp;     
				mPreferences.putString(PREF_VALIDITY_TIMESTAMP, validityTimestamp);     
		}   
	public boolean allowAccess()
	{
		logger.d((new StringBuilder()).append("mLastResponse=").append(mLastResponse).toString());
		boolean flag;
		if (mLastResponse != Policy.LicenseResponse.NOT_LICENSED)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public long getMaxRetries()
	{
		return mMaxRetries;
	}

	public long getRetryCount()
	{
		return mRetryCount;
	}

	public long getRetryUntil()
	{
		return mRetryUntil;
	}

	public long getValidityTimestamp()
	{
		return mValidityTimestamp;
	}

	 public void processServerResponse(LicenseResponse response, ResponseData rawData) { 
		 // Update retry counter  
		 if (response != LicenseResponse.RETRY) { 
			 setRetryCount(0);    
			 } else {    
				 setRetryCount(mRetryCount + 1); 
				 }     
		 if (response == LicenseResponse.LICENSED) { 
			 // Update server policy data   
			 Map<String, String> extras = decodeExtras(rawData.extra);    
			 mLastResponse = response;        
			 setValidityTimestamp(extras.get("VT"));     
			 setRetryUntil(extras.get("GT"));        
			 setMaxRetries(extras.get("GR"));   
			 } else if (response == LicenseResponse.NOT_LICENSED) { 
				 // Clear out stale policy data      
				 setValidityTimestamp(DEFAULT_VALIDITY_TIMESTAMP);   
				 setRetryUntil(DEFAULT_RETRY_UNTIL);        
				 setMaxRetries(DEFAULT_MAX_RETRIES);     
				 }      
		 setLastResponse(response);   
		 mPreferences.commit();  
		 }      
}
