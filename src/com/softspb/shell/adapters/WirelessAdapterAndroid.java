// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import com.softspb.shell.Home;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

// Referenced classes of package com.softspb.shell.adapters:
//			WirelessAdapter

public class WirelessAdapterAndroid extends WirelessAdapter
	implements com.softspb.shell.Home.PauseResumeListener
{

	private static final String AIRPLANE_EXTRA_STATE = "state";
	private static final Logger logger = Loggers.getLogger(WirelessAdapterAndroid.class.getName());
	private BluetoothAdapter bluetoothAdapter;
	private ConnectivityManager connectivityManager;
	private Home context;
	private boolean isRegistered;
	private Map oldStates;
	private BroadcastReceiver receiver;
	private WifiManager wifiManager;

	public WirelessAdapterAndroid(int i, Home home)
	{
		super(i);
		receiver = new BroadcastReceiver() {

			final WirelessAdapterAndroid this$0;

			public void onReceive(Context context1, Intent intent)
			{
				int j = 1;
				WirelessAdapterAndroid.logger.d(">>>onReceive");
				Bundle bundle = intent.getExtras();
				NetworkInfo networkinfo;
				WirelessAdapterAndroid wirelessadapterandroid;
				if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED"))
				{
					int k1 = bundle.getInt("wifi_state");
					notifyChange(j, convertWifiState(k1));
				}
				if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED"))
				{
					int j1 = bundle.getInt("android.bluetooth.adapter.extra.STATE");
					notifyChange(2, convertBtState(j1));
				}
				if (intent.getAction().equals("android.intent.action.AIRPLANE_MODE"))
				{
					int k = getWirelessState(j);
					int l = getWirelessState(2);
					notifyChange(j, k);
					notifyChange(2, l);
					if (bundle != null)
					{
						boolean flag = bundle.getBoolean("state");
						WirelessAdapterAndroid wirelessadapterandroid1 = WirelessAdapterAndroid.this;
					
					
						int i1;
						if (flag)
							i1 = j;
						else
							i1 = 0;
						wirelessadapterandroid1.notifyChange(4, i1);
					}
				}
				if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE"))
				{
					networkinfo = connectivityManager.getActiveNetworkInfo();
					if (networkinfo != null && networkinfo.getType() == j)
					{
						wirelessadapterandroid = WirelessAdapterAndroid.this;
						if (!networkinfo.isConnected())
							j = 0;
						wirelessadapterandroid.notifyChange(5, j);
					}
				}
				WirelessAdapterAndroid.logger.d("<<<onReceive");
			}

			
			{
				this$0 = WirelessAdapterAndroid.this;
//				super();
			}
		};
		isRegistered = false;
		oldStates = new HashMap() {

			final WirelessAdapterAndroid this$0;

			
			{
				this$0 = WirelessAdapterAndroid.this;
//				super();
				put(Integer.valueOf(1), Integer.valueOf(-1));
				put(Integer.valueOf(2), Integer.valueOf(-1));
				put(Integer.valueOf(4), Integer.valueOf(-1));
				put(Integer.valueOf(5), Integer.valueOf(-1));
				put(Integer.valueOf(3), Integer.valueOf(-1));
				put(Integer.valueOf(0), Integer.valueOf(-1));
			}
		};
		context = home;
		logger.d((new StringBuilder()).append("constructor nativePeer=").append(i).append(" context=").append(home).toString());
	}

	private int convertApState(int i)
	{
		
		int j = 0;
		switch (i) {
		case 0:
			
			break;
		case 1:
			j = 1;
			break;

		default:
			logger.w((new StringBuilder()).append("AP state=").append(i).append(" is not supported - check native to java constants mapping").toString());
			break;
		}
		return j;
	}

	private int convertBtState(int i)
	{
		int j = 0;
		switch (i) {
		case 10:
			
			break;
		case 11:
			j = 2;
			break;
		case 12:
			j = 1;	
			break;
		case 13:
			j = 2;
			break;
		default:
			logger.w((new StringBuilder()).append("bluetooth state=").append(i).append(" is not supported - check native to java constants mapping").toString());
		}
		return j;
	}

	private int convertWifiState(int i)
	{
		int j = 0;
		switch (i) {
			case 0:
				j = 2;
				break;
			case 1:
						
				break;
			case 2:
				j = 2;
				break;
			case 3:
				j = 1;
				break;
			case 4:
				
				break;
			default:	
				logger.w((new StringBuilder()).append("wifi state=").append(i).append(" is not supported - check native to java constants mapping").toString());
				break;
		}
		return j;
		
	}

	private void registerReceiver()
	{
		context.registerReceiver(receiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
		context.registerReceiver(receiver, new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"));
		context.registerReceiver(receiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
		context.registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		isRegistered = true;
	}

	public int getWirelessState(int i)
	{
		int j = 0;
		logger.d((new StringBuilder()).append(">>>getWirelessState: type=").append(i).toString());
		switch (i) {
		case 1:
			j = convertWifiState(wifiManager.getWifiState());
			break;
		case 2:
			if (bluetoothAdapter == null)
				j = 0;
			else
				j = convertBtState(bluetoothAdapter.getState());
			break;
		case 3:
			j = 0;
			logger.w((new StringBuilder()).append("wireless type=").append(i).append(" is not supported - check native to java constants mapping").toString());

			break;
		case 4:
			j = convertApState(android.provider.Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", -1));
			break;
		case 5:
			NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
			if (networkinfo != null && networkinfo.getType() == 1 && networkinfo.isConnected())
				j = 1;
			else
				j = 0;
			break;
		default:
			
			logger.w((new StringBuilder()).append("wireless type=").append(i).append(" is not supported - check native to java constants mapping").toString());
		}
		logger.d((new StringBuilder()).append("<<<getWirelessState: result=").append(j).toString());
		
		return j;
	}

	public boolean isBtDiscoverableSupported()
	{
		return false;
	}

	public boolean isWirelessSupported(int i)
	{
		boolean flag = false;
		switch (i) {
		case 0:
			
			break;
		case 1:
			flag = true;		
			break;
		
		case 2:
			flag = true;
			break;
		case 3:
			
			break;
		case 4:
			try
			{
				android.provider.Settings.System.getInt(context.getContentResolver(), "airplane_mode_on");
			}
			catch (android.provider.Settings.SettingNotFoundException settingnotfoundexception)
			{
				logger.w("flight mode switch is not available", settingnotfoundexception);		
			}
			flag = true;
			break;

		default:
			logger.w((new StringBuilder()).append("wireless type=").append(i).append(" is not supported - check native to java constants mapping").toString());
			break;
		}
		return flag;	
	}

	public void notifyChange(int i, int j)
	{
		logger.d((new StringBuilder()).append(">>>notifyChange: type=").append(i).append(", state=").append(j).append(", old state:").append(oldStates.get(Integer.valueOf(i))).toString());
		if (((Integer)oldStates.get(Integer.valueOf(i))).intValue() == j)
			logger.d((new StringBuilder()).append("state for ").append(i).append(" not changed, skipping").toString());
		else
			super.notifyChange(i, j);
		oldStates.put(Integer.valueOf(i), Integer.valueOf(j));
		logger.d("<<<notifyChange");
	}

	/**
	 * @deprecated Method onPause is deprecated
	 */

	public synchronized void onPause()
	{
		
		if (isRegistered)
		{
			context.unregisterReceiver(receiver);
			isRegistered = false;
		}
		
	}

	/**
	 * @deprecated Method onResume is deprecated
	 */

/** @deprecated */
  public synchronized void onResume()
  {
   
    try
    {
      if (!this.isRegistered)
      {
        registerReceiver();
        int i = getWirelessState(2);
        notifyChange(2, i);
        int j = getWirelessState(4);
        notifyChange(4, j);
      }
    
      return;
    }
    finally
    {
     
    }
  
  }

	/**
	 * @deprecated Method onStart is deprecated
	 */

	public synchronized void onStart()
	{
		
		logger.d(">>>onCreate");
		super.onStart();
		(new Handler(Looper.getMainLooper())).post(new Runnable() {

			final WirelessAdapterAndroid this$0;

			public void run()
			{
				bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				oldStates.put(Integer.valueOf(2), Integer.valueOf(getWirelessState(2)));
			}

			
			{
				this$0 = WirelessAdapterAndroid.this;
//				super();
			}
		});
		wifiManager = (WifiManager)context.getSystemService("wifi");
		connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
		oldStates.put(Integer.valueOf(1), Integer.valueOf(getWirelessState(1)));
		oldStates.put(Integer.valueOf(5), Integer.valueOf(getWirelessState(5)));
		oldStates.put(Integer.valueOf(4), Integer.valueOf(getWirelessState(4)));
		registerReceiver();
		context.setOnPauseResumeListener(this);
		logger.d("<<<onCreate");	
	}

	/**
	 * @deprecated Method onStop is deprecated
	 */

	public synchronized void onStop()
	{
		
		logger.d(">>>onDestroy");
		super.onStop();
		isRegistered = false;
		context.removeOnPauseResumeListener(this);
		if (isRegistered)
			context.unregisterReceiver(receiver);
		logger.d("<<<onDestroy");
	
	}
	 public boolean switchWirelessState(int paramInt1, int paramInt2)
	  {
		 boolean flag = false;
		    int j;
		    Intent localIntent1 = null;
	    	boolean bool1;
	    Logger localLogger1 = logger;
	    String str1 = ">>>switchWirelessState: type=" + paramInt1 + ", state=" + paramInt2;
	    localLogger1.d(str1);
	    switch (paramInt1)
	    {
	    case 1:
	    	switch (paramInt2)
		      {
		     
				case 0:
					flag = this.wifiManager.setWifiEnabled(false);
				  break;
		      case 1:	    	 
			        Method[] arrayOfMethod = this.wifiManager.getClass().getDeclaredMethods();
			        int k = arrayOfMethod.length;
			        int m = 0;
		      
			          if (m < k)
			          {
			            Method localMethod = arrayOfMethod[m];
			            if (localMethod.getName().equals("setWifiApEnabled"));
			            try
			            {
			              WifiManager localWifiManager = this.wifiManager;
			              Object[] arrayOfObject = new Object[2];
			              arrayOfObject[0] = 0;
			              Boolean localBoolean = Boolean.valueOf("0");
			              arrayOfObject[1] = localBoolean;
			              Object localObject = localMethod.invoke(localWifiManager, arrayOfObject);
			              m += 1;
			            }
			            catch (Throwable localThrowable)
			            {     
			                logger.d("wireless AP disable failed:", localThrowable);
			            }
			          }
			          flag = this.wifiManager.setWifiEnabled(true);
					break;
 			  default:
		        Logger localLogger2 = logger;
		        String str2 = "wireless state=" + paramInt2 + " is not supported for type=" + paramInt1 + " - check native to java constants mapping";
		        localLogger2.w(str2);
		        flag = false;
		      
		      }
			  break;
	    case 2:

			switch (paramInt2)
	        {
		      
		       
 				case 0:
 					flag = this.bluetoothAdapter.disable();
				  break;
		        case 1:
		        	flag = this.bluetoothAdapter.enable();
		          break;
		         case 2:
				logger.w((new StringBuilder()).append("wireless state=").append(paramInt2).append(" is not supported for type=").append(paramInt1).append(" - check native to java constants mapping").toString());
		          break;
		        case 3:
		        	flag = false;
 				default:
		          Logger localLogger4 = logger;
		          String str4 = "wireless state=" + paramInt2 + " is not supported for type=" + paramInt1 + " - check native to java constants mapping";
		          localLogger4.w(str4);
		          break;
	        }
	    	 localIntent1 = new Intent("android.intent.action.AIRPLANE_MODE");
			break;
		case 3:
		 	break;
	    case 4:
			Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
	    	switch (paramInt2)
		      {
				case 0:
					Settings.System.putInt(this.context.getContentResolver(), "airplane_mode_on", 0);
					Intent localIntent3 = localIntent1.putExtra("state", 0);
				  case 1:
					android.provider.Settings.System.putInt(context.getContentResolver(), "airplane_mode_on", 1);
					intent.putExtra("state", true);
				default:
				   logger.w((new StringBuilder()).append("wireless state=").append(paramInt2).append(" is not supported for type=").append(paramInt2).append(" - check native to java constants mapping").toString());
							break;
				}
	    	context.sendBroadcast(intent);
			flag = true;
			break;
				
	    }
		
	    return flag;
	
	  }

/*
	static BluetoothAdapter access$402(WirelessAdapterAndroid wirelessadapterandroid, BluetoothAdapter bluetoothadapter)
	{
		wirelessadapterandroid.bluetoothAdapter = bluetoothadapter;
		return bluetoothadapter;
	}

*/

}
