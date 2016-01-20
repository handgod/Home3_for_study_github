// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.shell3d.market;

import android.app.*;
import android.content.Intent;
import android.os.IBinder;
import com.android.vending.licensing.*;
import com.softspb.shell.Home;
import com.softspb.shell.ShellApplication;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class SpbMarketLicenseService extends Service
{
	private class MyLicenseCheckerCallback
		implements LicenseCheckerCallback
	{

		final SpbMarketLicenseService this$0;

		public void allow(boolean flag)
		{
			SpbMarketLicenseService.logger.d("Licensing : allow");
			finishLicenseCheck(true, flag);
		}

		public void applicationError(com.android.vending.licensing.LicenseCheckerCallback.ApplicationErrorCode applicationerrorcode)
		{
			SpbMarketLicenseService.logger.d((new StringBuilder()).append("Licensing : applicationError - ").append(applicationerrorcode).toString());
			finishLicenseCheck(false, true);
		}

		public void dontAllow()
		{
			SpbMarketLicenseService.logger.d("Licensing : don't allow");
			finishLicenseCheck(false, true);
		}

		private MyLicenseCheckerCallback()
		{
			this$0 = SpbMarketLicenseService.this;
//			super();
		}

	}


	private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm2cr1jVg1ed+6Rp7w58eysKeOBkRuDpCQdxh4bHyV3pj5rCHmuFGaJ5Bky/9B36naGilFT6XXrvhFNAaY/Ik7tFxS3sZxnRHi/ZyG5hm9uQTW9F9yANTyX/mEE6nInyfgzwRnPogcyJSqH4M7j7CgfdbMbS1tQ+CynhVJPlaQ3alnTROP2OpVgBDeJXqQ46p4W61kVBQwk/DozbA2lMmfj/2ZJ9c++SIdqW5wzg3AC1Gm0GK9vJ3KBkYymNfEG3Bo+qr5B/QFvQAcNvX+PFY+VhQy6tF+xsYdsqajWfyTkikFtR5A+5u2mCMs/8Kw13jnjY5u82hF07w0JRcB4sGXQIDAQAB";
	private static final byte SALT[];
	private static Logger logger = Loggers.getLogger(SpbMarketLicenseService.class);
	private LicenseChecker mChecker;
	private LicenseCheckerCallback mLicenseCheckerCallback;

	public SpbMarketLicenseService()
	{
	}

	private void finishLicenseCheck(boolean flag, boolean flag1)
	{
		if (flag)
		{
			if (flag1)
				((AlarmManager)getSystemService("alarm")).cancel(PendingIntent.getService(this, 0, new Intent(this,SpbMarketLicenseService.class), 0x8000000));
		} else
		{
			sendBroadcast(new Intent(Home.makeLicenseCheckFailedAction(this)), (new StringBuilder()).append(getPackageName()).append(".spb.permission").toString());
			((ShellApplication)getApplication()).disableHome();
		}
		stopSelf();
	}

	protected void makeCheck(LicenseCheckerCallback licensecheckercallback)
	{
		mChecker = new LicenseChecker(this, new ServerManagedPolicy(this, new AESObfuscator(SALT, getPackageName(), "android_id")), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm2cr1jVg1ed+6Rp7w58eysKeOBkRuDpCQdxh4bHyV3pj5rCHmuFGaJ5Bky/9B36naGilFT6XXrvhFNAaY/Ik7tFxS3sZxnRHi/ZyG5hm9uQTW9F9yANTyX/mEE6nInyfgzwRnPogcyJSqH4M7j7CgfdbMbS1tQ+CynhVJPlaQ3alnTROP2OpVgBDeJXqQ46p4W61kVBQwk/DozbA2lMmfj/2ZJ9c++SIdqW5wzg3AC1Gm0GK9vJ3KBkYymNfEG3Bo+qr5B/QFvQAcNvX+PFY+VhQy6tF+xsYdsqajWfyTkikFtR5A+5u2mCMs/8Kw13jnjY5u82hF07w0JRcB4sGXQIDAQAB");
		mLicenseCheckerCallback = licensecheckercallback;
		logger.d("LicenseService : checking ...");
		mChecker.checkAccess(mLicenseCheckerCallback);
	}

	public IBinder onBind(Intent intent)
	{
		logger.d((new StringBuilder()).append("LicenseService.onBind() - ").append(intent).toString());
		return null;
	}

	public void onDestroy()
	{
		logger.d("LicenseService.onDestroy()");
		super.onDestroy();
		if (mChecker != null)
			mChecker.onDestroy();
	}

	public int onStartCommand(Intent intent, int i, int j)
	{
		logger.d((new StringBuilder()).append("LicenseService.onStartCommand() - ").append(Thread.currentThread().getName()).toString());
		super.onCreate();
		Thread thread = new Thread() {

			final SpbMarketLicenseService this$0;

			public void run()
			{
				mLicenseCheckerCallback = new MyLicenseCheckerCallback();
				makeCheck(mLicenseCheckerCallback);
			}

			
			{
				this$0 = SpbMarketLicenseService.this;
//				super();
			}
		};
		thread.setDaemon(true);
		thread.setPriority(1);
		thread.start();
		return 2;
	}

	static 
	{
		byte abyte0[] = new byte[20];
		abyte0[0] = -36;
		abyte0[1] = -55;
		abyte0[2] = 127;
		abyte0[3] = 28;
		abyte0[4] = 13;
		abyte0[5] = -5;
		abyte0[6] = 104;
		abyte0[7] = -84;
		abyte0[8] = 57;
		abyte0[9] = 99;
		abyte0[10] = 95;
		abyte0[11] = 35;
		abyte0[12] = 88;
		abyte0[13] = -11;
		abyte0[14] = -6;
		abyte0[15] = -43;
		abyte0[16] = 56;
		abyte0[17] = 87;
		abyte0[18] = -14;
		abyte0[19] = 98;
		SALT = abyte0;
	}





/*
	static LicenseCheckerCallback access$202(SpbMarketLicenseService spbmarketlicenseservice, LicenseCheckerCallback licensecheckercallback)
	{
		spbmarketlicenseservice.mLicenseCheckerCallback = licensecheckercallback;
		return licensecheckercallback;
	}

*/
}
