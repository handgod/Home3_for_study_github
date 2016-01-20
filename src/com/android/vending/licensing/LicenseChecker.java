// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.*;
import android.util.Log;

import com.android.vending.licensing.util.Base64;
import com.android.vending.licensing.util.Base64DecoderException;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

// Referenced classes of package com.android.vending.licensing:
//			Policy, LicenseValidator, LicenseCheckerCallback, ILicensingService, 
//			NullDeviceLimiter

public class LicenseChecker
	implements ServiceConnection
{
	private class ResultListener extends ILicenseResultListener.Stub
	{

		private Runnable mOnTimeout;
		private final LicenseValidator mValidator;
		final LicenseChecker this$0;

		private void clearTimeout()
		{
			LicenseChecker.logger.i("Clearing timeout.");
			mHandler.removeCallbacks(mOnTimeout);
		}

		private void startTimeout()
		{
			LicenseChecker.logger.i("Start monitoring timeout.");
			mHandler.postDelayed(mOnTimeout, 10000L);
		}

		// Runs in IPC thread pool. Post it to the Handler, so we can guarantee  
		// either this or the timeout runs.   
		public void verifyLicense(final int responseCode, final String signedData, final String signature)
		{   
			 mHandler.post(new Runnable() {public void run(){
				 LicenseChecker.logger.i("Received response.");
				 // Make sure it hasn't already timed out. 
				 if (mChecksInProgress.contains(mValidator)) { 
					 clearTimeout(); 
					 mValidator.verify(mPublicKey, responseCode, signedData, signature); 
					 finishCheck(mValidator); 
					 } 
				 } 
			 });   
		}
			
		 public ResultListener(LicenseValidator validator) 
		 {    
			 this$0 = LicenseChecker.this;
			 mValidator = validator; 
			 mOnTimeout = new Runnable()
			 {    
				 public void run()
				 {   
					 LicenseChecker.logger.i("Check timed out.");
					 handleServiceConnectionError(mValidator);   
					 finishCheck(mValidator);  
					 }       
				 };       
				 startTimeout();    
		 }   

	}


	private static final String KEY_FACTORY_ALGORITHM = "RSA";
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final int TIMEOUT_MS = 10000;
	private static Logger logger = Loggers.getLogger(com.android.vending.licensing.LicenseChecker.class);
	private final Set mChecksInProgress = new HashSet();
	private final Context mContext;
	private Handler mHandler;
	private final String mPackageName;
	private final Queue mPendingChecks = new LinkedList();
	private final Policy mPolicy;
	private PublicKey mPublicKey;
	private ILicensingService mService;
	private final String mVersionCode;

	public LicenseChecker(Context context, Policy policy, String s)
	{
		mContext = context;
		mPolicy = policy;
		mPublicKey = generatePublicKey(s);
		mPackageName = mContext.getPackageName();
		mVersionCode = getVersionCode(context, mPackageName);
		HandlerThread handlerthread = new HandlerThread("background thread");
		handlerthread.start();
		mHandler = new Handler(handlerthread.getLooper());
	}

	private void cleanupService()
	{
		if (mService != null)
		{
			try
			{
				mContext.unbindService(this);
			}
			catch (IllegalArgumentException illegalargumentexception)
			{
				logger.e("Unable to unbind from licensing service (already unbound)");
			}
			mService = null;
		}
	}

	/**
	 * @deprecated Method finishCheck is deprecated
	 */

	

	 private synchronized void finishCheck(LicenseValidator validator)
	 {    
		 mChecksInProgress.remove(validator);   
		 if (mChecksInProgress.isEmpty()) {   
			 cleanupService();   
			 }   
	 }
	private int generateNonce()
	{
		return RANDOM.nextInt();
	}

	private static PublicKey generatePublicKey(String s)
	{
		PublicKey publickey;
		try
		{
			byte abyte0[] = Base64.decode(s);
			publickey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(abyte0));
		}
		catch (NoSuchAlgorithmException nosuchalgorithmexception)
		{
			throw new RuntimeException(nosuchalgorithmexception);
		}
		catch (Base64DecoderException base64decoderexception)
		{
			logger.e("Could not decode from Base64.");
			throw new IllegalArgumentException(base64decoderexception);
		}
		catch (InvalidKeySpecException invalidkeyspecexception)
		{
			logger.e("Invalid key specification.");
			throw new IllegalArgumentException(invalidkeyspecexception);
		}
		return publickey;
	}

	/**  Get version code for the application package name.      
	 **  @param context  
	 **  @param packageName application package name   
	 **  @return the version code or empty string if package not found  
     */ 
	private static String getVersionCode(Context context, String packageName)
	{       
		try {  
			return String.valueOf(context.getPackageManager().getPackageInfo(packageName, 0).versionCode); 
			} 
		catch (NameNotFoundException e)
		{        
			logger.e("Package not found. could not get version code.");       
			return "";     
			}   
	}   
	
	/**        * Generates policy response for service connection errors, as a result of 
	 *        * disconnections or timeouts. 
	 *               */   
	private synchronized void handleServiceConnectionError(LicenseValidator validator) 
	{     
		mPolicy.processServerResponse(Policy.LicenseResponse.RETRY, null);   
		if (mPolicy.allowAccess()) {  
			validator.getCallback().allow(false);  
			}
		else { 
			validator.getCallback().dontAllow(); 
			}  
	}   
	
	private void runChecks()
	{
		do
		{
			LicenseValidator licensevalidator = (LicenseValidator)mPendingChecks.poll();
			if (licensevalidator != null)
				try
				{
					logger.i((new StringBuilder()).append("Calling checkLicense on service for ").append(licensevalidator.getPackageName()).toString());
					mService.checkLicense(licensevalidator.getNonce(), licensevalidator.getPackageName(), new ResultListener(licensevalidator));
					mChecksInProgress.add(licensevalidator);
				}
				catch (RemoteException remoteexception)
				{
					logger.w("RemoteException in checkLicense call.", remoteexception);
					handleServiceConnectionError(licensevalidator);
				}
			else
				return;
		} while (true);
	}

	/**
	 * @deprecated Method checkAccess is deprecated
	 */
	/**        * Checks if the user should have access to the app.   
	 *      *        * @param callback        */ 
	public synchronized void checkAccess(LicenseCheckerCallback callback)
	{ 
		// If we have a valid recent LICENSED response, we can skip asking   
		// Market. 
	
			LicenseValidator validator = new LicenseValidator(mPolicy, new NullDeviceLimiter(),callback, generateNonce(), mPackageName, mVersionCode); 
			if (mService == null)
			{ 
				logger.i("Binding to licensing service.");
				try {
						boolean bindResult = mContext.bindService(new Intent(ILicensingService.class.getName()), this,
								// ServiceConnection.  
								Context.BIND_AUTO_CREATE);    
						if (bindResult) 
						{   
							mPendingChecks.offer(validator);   
						} 
						else 
						{      
							logger.e("Could not bind to service.");
							handleServiceConnectionError(validator); 
						}  
					} 
				catch (SecurityException e)
				{  
					callback.applicationError(LicenseCheckerCallback.ApplicationErrorCode.MISSING_PERMISSION);    
				}      
			}
			else {    
					mPendingChecks.offer(validator); 
					runChecks();   
			}      
	}    
	  
		
	/**
	 * @deprecated Method onDestroy is deprecated
	 */
	public synchronized void onDestroy() {  
		cleanupService();   
		mHandler.getLooper().quit(); 
		} 

	public synchronized void onServiceConnected(ComponentName name, IBinder service)
	{    
		mService = ILicensingService.Stub.asInterface(service);  
		runChecks();   
	}   
	
	/**
	 * @deprecated Method onServiceDisconnected is deprecated
	 */
	 public synchronized void onServiceDisconnected(ComponentName name)
	 {   
		 // Called when the connection with the service has been    
		 // unexpectedly disconnected. That is, Market crashed.  
		 // If there are any checks in progress, the timeouts will handle them.   
		 logger.w("Service unexpectedly disconnected.");
		 mService = null;  
	 }   






}
