// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

public interface LicenseCheckerCallback {
	/**        * Allow use. App should proceed as normal.        */  
	public void allow(boolean flag);
	/**        * Don't allow use. App should inform user and take appropriate action.        */ 
	public void dontAllow();      
	/** Application error codes. */   
	public enum ApplicationErrorCode {    
		/** Package is not installed. */  
		INVALID_PACKAGE_NAME,	/** Requested for a package that is not the current app. */    
		NON_MATCHING_UID,   /** Market does not know about the package. */   
		NOT_MARKET_MANAGED,           /** A previous check request is already in progress.            * Only one check is allowed at a time. */   
		CHECK_IN_PROGRESS,           /** Supplied public key is invalid. */     
		INVALID_PUBLIC_KEY,           /** App must request com.android.vending.CHECK_LICENSE permission. */       
		MISSING_PERMISSION,  
		}  
	/**        * Error in application code. Caller did not call or set up license  
	 *       * checker correctly. Should be considered fatal.        */    
	public void applicationError(ApplicationErrorCode errorCode); 
	}   
