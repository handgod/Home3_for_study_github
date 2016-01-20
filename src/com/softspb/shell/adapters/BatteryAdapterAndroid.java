package com.softspb.shell.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class BatteryAdapterAndroid extends BatteryAdapter
{
  private static final Logger logger = Loggers.getLogger(BatteryAdapterAndroid.class.getName());
  private BroadcastReceiver receiver;

  class BatteryAdapterAndroid$1 extends BroadcastReceiver
  {
    public void onReceive(Context context, Intent intent)
	{
		BatteryAdapterAndroid.logger.d(">>>onReceive");
		int key = intent.getIntExtra("status", -1);
		int i;
		switch (key) {
		case 2:
			i=2;
			break;
		case 3:
			i = 3;
			break;
		case 4:
			i = 4;
			break;
		case 5:
			i = 5;
			break;
		default:
			i = 1;
		}
		int j = intent.getIntExtra("level", 0);
		int k = intent.getIntExtra("scale", 0);
		notifyChange(i, j, k);
		BatteryAdapterAndroid.logger.d("<<<onReceive");
		return;
	}
  }
  public BatteryAdapterAndroid(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
    BatteryAdapterAndroid$1 local1 = new BatteryAdapterAndroid$1();
    this.receiver = local1;
    logger.d("constructor");
  }

  private void notifyChange(int paramInt1, int paramInt2, int paramInt3)
  {
    Logger localLogger = logger;
    String str = ">>>notifyChange: pluggedState=" + paramInt1 + ", level=" + paramInt2 + ", maxLevel=" + paramInt3;
    localLogger.d(str);
    setStatus(paramInt1, paramInt2, paramInt3);
    logger.d("<<<notifyChange");
  }

  public static native void setStatus(int paramInt1, int paramInt2, int paramInt3);

  protected void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    Logger localLogger = logger;
    String str = ">>>onCreate: context=" + paramContext;
    localLogger.d(str);
    super.onCreate(paramContext, paramNativeCallbacks);
    BroadcastReceiver localBroadcastReceiver = this.receiver;
    IntentFilter localIntentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    Intent localIntent = paramContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
    logger.d("<<<onCreate");
  }

  protected void onDestroy(Context paramContext)
  {
    Logger localLogger = logger;
    String str = ">>>onDestroy: context=" + paramContext;
    localLogger.d(str);
    BroadcastReceiver localBroadcastReceiver = this.receiver;
    paramContext.unregisterReceiver(localBroadcastReceiver);
    logger.d("<<<onDestroy");
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.BatteryAdapterAndroid
 * JD-Core Version:    0.6.0
 */