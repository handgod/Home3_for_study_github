// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;


// Referenced classes of package com.android.vending.licensing:
//			ResponseData

public interface Policy
{
	 /** * Result of a license check.        */  
	public enum LicenseResponse {    
		/**            * User is licensed to use the app.            */  
		LICENSED,	/**            * User is not licensed to use the app.            */    
		NOT_LICENSED,  /*** Retryable error. e.g. no network or application is over request * quota.   */   
		RETRY,   
		}     
	/**     
	 *    * Provide results from contact with the license server. Retry counts are incremented if  
	 *          * the current value of response is RETRY. Results will be used for any future policy   
	 *               * decisions.
	 *    *        * @param response the result from validating the server response   
	 *    * @param rawData the raw server response data, can be null for RETRY        */   
	void processServerResponse(LicenseResponse response, ResponseData rawData);     
	/** * Check if the user should be allowed access to the application.        */    
	boolean allowAccess();   
}
