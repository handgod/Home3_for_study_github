// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import android.content.Context;
import android.telephony.*;
import android.text.TextUtils;
import com.softspb.shell.Home;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.softspb.shell.adapters:
//			OperatorAdapter, AdaptersHolder

public class OperatorAdapterAndroid extends OperatorAdapter
	implements com.softspb.shell.Home.PauseResumeListener
{

	private static final Logger logger = Loggers.getLogger(OperatorAdapterAndroid.class.getName());
	private boolean isRegistered;
	private PhoneStateListener listener;
	private int oldLevel;
	private int oldMaxLevel;
	private int oldNetworkType;
	private String oldOperator;
	private int oldOperatorState;
	private int oldPhoneType;
	private int oldSimState;
	private TelephonyManager telephonyManager;

	public OperatorAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		listener = new PhoneStateListener() {

			private static final int maxLevel = 4;
			private int level;
			int mPhoneState;
			ServiceState mServiceState;
			SignalStrength mSignalStrength;
			private int networkType;
			private String operator;
			private int operatorState;
			private int phoneType;
			private int simState;
			final OperatorAdapterAndroid this$0;

			private int getCdmaLevel()
			{
				int i = mSignalStrength.getCdmaDbm();
				int j = mSignalStrength.getCdmaEcio();
				byte byte0;
				byte byte1;
				if (i >= -75)
					byte0 = 4;
				else
				if (i >= -85)
					byte0 = 3;
				else
				if (i >= -95)
					byte0 = 2;
				else
				if (i >= -100)
					byte0 = 1;
				else
					byte0 = 0;
				if (j >= -90)
					byte1 = 4;
				else
				if (j >= -110)
					byte1 = 3;
				else
				if (j >= -130)
					byte1 = 2;
				else
				if (j >= -150)
					byte1 = 1;
				else
					byte1 = 0;
				if (byte0 >= byte1)
					byte0 = byte1;
				return byte0;
			}

			private int getEvdoLevel()
			{
				int i = mSignalStrength.getEvdoDbm();
				int j = mSignalStrength.getEvdoSnr();
				byte byte0;
				byte byte1;
				if (i >= -65)
					byte0 = 4;
				else
				if (i >= -75)
					byte0 = 3;
				else
				if (i >= -90)
					byte0 = 2;
				else
				if (i >= -105)
					byte0 = 1;
				else
					byte0 = 0;
				if (j >= 7)
					byte1 = 4;
				else
				if (j >= 5)
					byte1 = 3;
				else
				if (j >= 3)
					byte1 = 2;
				else
				if (j >= 1)
					byte1 = 1;
				else
					byte1 = 0;
				if (byte0 >= byte1)
					byte0 = byte1;
				return byte0;
			}

			private boolean isCdma()
			{
				boolean flag;
				if (mSignalStrength != null && !mSignalStrength.isGsm())
					flag = true;
				else
					flag = false;
				return flag;
			}

			private boolean isEvdo()
			{
				int i = telephonyManager.getNetworkType();
				boolean flag;
				if (mServiceState != null && (i == 5 || i == 6))
					flag = true;
				else
					flag = false;
				return flag;
			}

			private void notifyChangeInternal()
			{
				OperatorAdapterAndroid.logger.d(">>>notifyChangeInternal");
				notifyChange(operatorState, simState, phoneType, networkType, level, 4, operator);
				OperatorAdapterAndroid.logger.d("<<<notifyChangeInternal");
			}

			private void updateSignalStrength()
			{
				if (!isCdma())
				{
					int i = mSignalStrength.getGsmSignalStrength();
					if (i <= 2 || i == 99)
						level = 0;
					else
					if (i >= 12)
						level = 4;
					else
					if (i >= 8)
						level = 3;
					else
					if (i >= 5)
						level = 2;
					else
						level = 1;
				} else
				if (mPhoneState == 0 && isEvdo())
					level = getEvdoLevel();
				else
					level = getCdmaLevel();
				notifyChangeInternal();
			}

			public void onCallStateChanged(int i, String s)
			{
				mPhoneState = i;
				if (isCdma())
					updateSignalStrength();
			}

			public void onServiceStateChanged(ServiceState servicestate)
			{
				mServiceState = servicestate;
				operatorState = convertToOperatorState(servicestate.getState());
				simState = convertToSimState(telephonyManager.getSimState());
				phoneType = convertToPhoneType(telephonyManager.getPhoneType());
				networkType = convertToNetworkType(telephonyManager.getNetworkType());
				if (operatorState == 4)
					operator = telephonyManager.getNetworkOperatorName();
				else
					operator = null;
				notifyChangeInternal();
			}

			public void onSignalStrengthsChanged(SignalStrength signalstrength)
			{
				mSignalStrength = signalstrength;
				updateSignalStrength();
			}

			
			{
				this$0 = OperatorAdapterAndroid.this;
//				super();
				mPhoneState = 0;
			}
		};
		isRegistered = false;
		logger.d("constructor");
	}

	private int convertToNetworkType(int i)
	{
		byte byte0 ;
		switch (i) {
		case 1:
			byte0 = 5;
			break;
		case 2:
			byte0 = 2;
			break;

		case 3:
			byte0 = 10;
			break;

		case 4:
			byte0 = 1;
			break;

		case 5:
			byte0 = 3;
			break;
		case 6:
			byte0 = 4;
			break;
		case 7:
			byte0 = 0;
			break;
		case 8:
			byte0 = 6;
			break;
		case 9:
			byte0 = 8;
			break;
		case 10:
			byte0 = 7;
			break;
		case 11:
			byte0 = 9;
			break;
		default:
			byte0 = 11;
		}
		return byte0;
	}

	private int convertToOperatorState(int i)
	{
		int j = 0;
		switch (i) {
		case 0:
			j = 4;
			break;
		case 1:
			j = 3;		
			break;
		case 2:
			j = 1;
			break;
		case 3:
			break;
		default:
			logger.w((new StringBuilder()).append("operatorState=").append(i).append("  is not supported - check native to java constants mapping").toString());
			break;
		}
		return j;
	}

	private int convertToPhoneType(int i)
	{
			int j ;
	    switch (i)
	    {
	    
	    case 1:
			j = 1;
			break;
	    case 2:
		 	j = 0;
			break;
		default:
	      j = 2;
	    }
		 return j;
	   
	}

	private int convertToSimState(int i)
	{ 
		int j ;
    	switch (i)
    	{
   
	    case 1:
		  j = 0;
		  break;
	    case 2:
			  j = 2;
		  break;
	    case 3:
			  j = 3;
		  break;
	   case 4:
		  j = 1;
		  break;
	    case 5:
		  j = 4;
		  break;
		 default:
	      j = 5;
    	}
	  return j;
	}

	private void notifyChange(int i, int j, int k, int l, int i1, int j1, String s)
	{
		if (oldOperatorState == i && oldSimState == j && oldPhoneType == k && oldNetworkType == l && oldLevel == i1 && oldMaxLevel == j1 && TextUtils.equals(oldOperator, s))
		{
			logger.d("notifyChange: skipped!");
		} else
		{
			oldOperatorState = i;
			oldSimState = j;
			oldPhoneType = k;
			oldNetworkType = l;
			oldLevel = i1;
			oldMaxLevel = j1;
			oldOperator = s;
			logger.d((new StringBuilder()).append("notifyChange: operatorState = ").append(i).append(", ").append("simState = ").append(j).append(", ").append("phoneType = ").append(k).append(", ").append("networkType = ").append(l).append(", ").append("level = ").append(i1).append(", ").append("maxLevel = ").append(j1).append(", ").append("operator = ").append(s).toString());
			setStatus(i, j, k, l, i1, j1, s);
		}
	}

	public static native void setStatus(int i, int j, int k, int l, int i1, int j1, String s);

	public boolean isOperatorSupported()
	{
		logger.d(">>>isOperatorSupported");
		logger.d((new StringBuilder()).append("<<<isOperatorSupported: result=").append(true).toString());
		return true;
	}

	/**
	 * @deprecated Method onCreate is deprecated
	 */

	protected synchronized void onCreate(Context paramContext, NativeCallbacks nativecallbacks)
	{

	    try
	    {
	      Logger localLogger = logger;
	      String str = ">>>onCreate: context=" + paramContext;
	      localLogger.d(str);
	      this.isRegistered = true;
	      TelephonyManager localTelephonyManager1 = (TelephonyManager)paramContext.getSystemService("phone");
	      this.telephonyManager = localTelephonyManager1;
	      TelephonyManager localTelephonyManager2 = this.telephonyManager;
	      PhoneStateListener localPhoneStateListener = this.listener;
	      localTelephonyManager2.listen(localPhoneStateListener, 289);
	      ((Home)paramContext).setOnPauseResumeListener(this);
	      logger.d("<<<onCreate");
	
	      return;
	    }
	    finally
	    {

	    }
	 
	}

	/**
	 * @deprecated Method onDestroy is deprecated
	 */

	protected synchronized void onDestroy(Context paramContext)
	{
	    try
	    {
	      Logger localLogger = logger;
	      String str = ">>>onDestroy: context=" + paramContext;
	      localLogger.d(str);
	      ((Home)paramContext).removeOnPauseResumeListener(this);
	      TelephonyManager localTelephonyManager = this.telephonyManager;
	      PhoneStateListener localPhoneStateListener = this.listener;
	      localTelephonyManager.listen(localPhoneStateListener, 0);
	      logger.d("<<<onDestroy");

	      return;
	    }
	    finally
	    {
	  
	    }

	}

	/**
	 * @deprecated Method onPause is deprecated
	 */

	public synchronized void onPause()
	{
	    try
	    {
	      if (this.isRegistered)
	      {
	        this.isRegistered = false;
	        TelephonyManager localTelephonyManager = this.telephonyManager;
	        PhoneStateListener localPhoneStateListener = this.listener;
	        localTelephonyManager.listen(localPhoneStateListener, 0);
	      }
	
	      return;
	    }
	    finally
	    {
	
	    }

	}

	/**
	 * @deprecated Method onResume is deprecated
	 */

	public synchronized void onResume()
	{
	    try
	    {
	      if (!this.isRegistered)
	      {
	        this.isRegistered = true;
	        TelephonyManager localTelephonyManager = this.telephonyManager;
	        PhoneStateListener localPhoneStateListener = this.listener;
	        localTelephonyManager.listen(localPhoneStateListener, 289);
	      }
	  
	      return;
	    }
	    finally
	    {
	
	    }

	}








}
