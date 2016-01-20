// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.android.vending.licensing;

import android.text.TextUtils;
import com.android.vending.licensing.util.Base64;
import com.android.vending.licensing.util.Base64DecoderException;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.security.*;

// Referenced classes of package com.android.vending.licensing:
//			LicenseCheckerCallback, Policy, ResponseData, DeviceLimiter

class LicenseValidator
{

	private static final int ERROR_CONTACTING_SERVER = 257;
	private static final int ERROR_INVALID_PACKAGE_NAME = 258;
	private static final int ERROR_NON_MATCHING_UID = 259;
	private static final int ERROR_NOT_MARKET_MANAGED = 3;
	private static final int ERROR_OVER_QUOTA = 5;
	private static final int ERROR_SERVER_FAILURE = 4;
	private static final int LICENSED = 0;
	private static final int LICENSED_OLD_KEY = 2;
	private static final int NOT_LICENSED = 1;
	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	private static Logger logger = Loggers.getLogger(com.android.vending.licensing.LicenseValidator.class);
	private final LicenseCheckerCallback mCallback;
	private final DeviceLimiter mDeviceLimiter;
	private final int mNonce;
	private final String mPackageName;
	private final Policy mPolicy;
	private final String mVersionCode;

	LicenseValidator(Policy policy, DeviceLimiter devicelimiter, LicenseCheckerCallback licensecheckercallback, int i, String s, String s1)
	{
		mPolicy = policy;
		mDeviceLimiter = devicelimiter;
		mCallback = licensecheckercallback;
		mNonce = i;
		mPackageName = s;
		mVersionCode = s1;
	}

	private void handleApplicationError(LicenseCheckerCallback.ApplicationErrorCode applicationerrorcode)
	{
		mCallback.applicationError(applicationerrorcode);
	}

	private void handleInvalidResponse()
	{
		mCallback.dontAllow();
	}

	private void handleResponse(Policy.LicenseResponse licenseresponse, ResponseData responsedata)
	{
		mPolicy.processServerResponse(licenseresponse, responsedata);
		if (mPolicy.allowAccess())
		{
			LicenseCheckerCallback licensecheckercallback = mCallback;
			boolean flag;
			if (licenseresponse != Policy.LicenseResponse.RETRY)
				flag = true;
			else
				flag = false;
			licensecheckercallback.allow(flag);
		} else
		{
			mCallback.dontAllow();
		}
	}

	public LicenseCheckerCallback getCallback()
	{
		return mCallback;
	}

	public int getNonce()
	{
		return mNonce;
	}

	public String getPackageName()
	{
		return mPackageName;
	}

	public void verify(PublicKey publickey, int i, String s, String s1)
	{
label0:
		{
			String s2 = null;
			ResponseData responsedata = null;
			if (i == 0 || i == 1 || i == 2)
			{
				try
				{
					Signature signature = Signature.getInstance("SHA1withRSA");
					signature.initVerify(publickey);
					if (s == null)
					{
						logger.e("Signed data is null");
						handleInvalidResponse();
						break label0;
					}
					signature.update(s.getBytes());
					if (!signature.verify(Base64.decode(s1)))
					{
						logger.e("Signature verification failed.");
						handleInvalidResponse();
						break label0;
					}
				}
				catch (NoSuchAlgorithmException nosuchalgorithmexception)
				{
					throw new RuntimeException(nosuchalgorithmexception);
				}
				catch (InvalidKeyException invalidkeyexception)
				{
					handleApplicationError(LicenseCheckerCallback.ApplicationErrorCode.INVALID_PUBLIC_KEY);
					break label0;
				}
				catch (SignatureException signatureexception)
				{
					throw new RuntimeException(signatureexception);
				}
				catch (Base64DecoderException base64decoderexception)
				{
					logger.e("Could not Base64-decode signature.");
					handleInvalidResponse();
					break label0;
				}
				ResponseData responsedata1;
				try
				{
					responsedata1 = ResponseData.parse(s);
				}
				catch (IllegalArgumentException illegalargumentexception)
				{
					logger.e("Could not parse response.");
					handleInvalidResponse();
					break label0;
				}
				responsedata = responsedata1;
				if (responsedata.responseCode != i)
				{
					logger.e("Response codes don't match.");
					handleInvalidResponse();
					break label0;
				}
				if (responsedata.nonce != mNonce)
				{
					logger.e("Nonce doesn't match.");
					handleInvalidResponse();
					break label0;
				}
				if (!responsedata.packageName.equals(mPackageName))
				{
					logger.e("Package name doesn't match.");
					handleInvalidResponse();
					break label0;
				}
				if (!responsedata.versionCode.equals(mVersionCode))
				{
					logger.e("Version codes don't match.");
					handleInvalidResponse();
					break label0;
				}
				s2 = responsedata.userId;
				if (TextUtils.isEmpty(s2))
				{
					logger.e("User identifier is empty.");
					handleInvalidResponse();
					break label0;
				}
			}
			switch (i)
			{
			default:
				logger.e("Unknown response code for license check.");
				handleInvalidResponse();
				break;

			case 0: // '\0'
			case 2: // '\002'
				handleResponse(mDeviceLimiter.isDeviceAllowed(s2), responsedata);
				break;

			case 1: // '\001'
				handleResponse(Policy.LicenseResponse.NOT_LICENSED, responsedata);
				break;

			case 257: 
				logger.w("Error contacting licensing server.");
				handleResponse(Policy.LicenseResponse.RETRY, responsedata);
				break;

			case 4: // '\004'
				logger.w("An error has occurred on the licensing server.");
				handleResponse(Policy.LicenseResponse.RETRY, responsedata);
				break;

			case 5: // '\005'
				logger.w("Licensing server is refusing to talk to this device, over quota.");
				handleResponse(Policy.LicenseResponse.RETRY, responsedata);
				break;

			case 258: 
				handleApplicationError(LicenseCheckerCallback.ApplicationErrorCode.INVALID_PACKAGE_NAME);
				break;

			case 259: 
				handleApplicationError(LicenseCheckerCallback.ApplicationErrorCode.NON_MATCHING_UID);
				break;

			case 3: // '\003'
				handleApplicationError(LicenseCheckerCallback.ApplicationErrorCode.NOT_MARKET_MANAGED);
				break;
			}
		}
	}

}
