package com.softspb.shell.adapters;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import com.softspb.shell.Home;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.DeviceUtil;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class AppAdapterAndroid extends AppAdapter
{
  private static Logger logger = Loggers.getLogger("AppAdapterAndroid");
  private String build = "-1";
  private Context context;
  private String version = "unknown";

  public AppAdapterAndroid(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  private String getDeviceId(Context paramContext)
  {
    return DeviceUtil.getDeviceId(paramContext);
  }

  private native void setVersionInfo(int paramInt, String paramString1, String paramString2, String paramString3);

  public int GetAndroidVersion()
  {
    return Build.VERSION.SDK_INT;
  }

  public boolean canChangeInterfaceMode()
  {
		boolean flag;
		if (android.os.Build.VERSION.SDK_INT >= 11 && android.os.Build.VERSION.SDK_INT <= 13)
			flag = true;
		else
			flag = false;
		return flag;
  }
	
  public void enableHTCStereo3D(boolean paramBoolean)
  {
    ((Home)this.context).enableHTCStereo3D(paramBoolean);
  }

  public String getDeviceId()
  {
    Context localContext = this.context;
    return getDeviceId(localContext);
  }

  public String getDeviceModel()
  {
    return Build.DEVICE;
  }

  public String getKey()
  {
	String str3;
    try
    {
      Context localContext = this.context;
      Class localClass = getClass();
      ComponentName localComponentName = new ComponentName(localContext, localClass);
      PackageManager localPackageManager = this.context.getPackageManager();
      String str1 = localComponentName.getPackageName();
      String str2 = localPackageManager.getPackageInfo(str1, 64).signatures[0].toCharsString();
      str3 = str2;
      return str3;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
       str3 = "Unknown key";
    }
    return str3;
  }

  public void getVersionInfo(int paramInt)
  {
    String str1 = this.version;
    String str2 = this.build;
    setVersionInfo(paramInt, str1, str2, "Standard Edition [Beta]");
  }
  public boolean hasKeyboard()
	{
		boolean flag;
		flag = false;
		if (!"cdma_venus2".equals(Build.DEVICE) && context.getResources().getConfiguration().keyboard == 2)
			flag = true;
		return flag;
	}
	
  public boolean hasTelephony()
  {
    return this.context.getPackageManager().hasSystemFeature("android.hardware.telephony");
  }

  public boolean isHTCStereo3DEnabled()
  {
    return ((Home)this.context).isHTCStereo3DEnabled();
  }

  public boolean isMipmapEnabled()
  {
		boolean flag;
		if (android.os.Build.VERSION.SDK_INT > 8)
			flag = true;
		else
			flag = false;
		return flag;
  }


  protected void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    this.context = paramContext;
    super.onCreate(paramContext, paramNativeCallbacks);
    String str1 = paramContext.getPackageName();
    PackageManager localPackageManager = paramContext.getPackageManager();
    try
    {
      PackageInfo localPackageInfo = localPackageManager.getPackageInfo(str1, 0);
      String str2 = String.valueOf(localPackageInfo.versionCode);
      this.build = str2;
      String str3 = localPackageInfo.versionName;
      this.version = str3;
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
        localNameNotFoundException.printStackTrace();
    }
  }
	public void setOrientation(int i)
	{
		byte byte0;
		byte0 = 4;
		logger.i((new StringBuilder()).append("setOrientation:").append(String.valueOf(i)).toString());
		switch (i)
		{
			case 0:
				byte0 = 1;
				return;
			case 1:
				byte0 = 0;
				return;
			case 2:
				byte0 = 4;
				return;
			case 3:
				byte0 = 5;	
				return;
			case 4:
				byte0 = -1;
				return;	
		}
		((Activity)context).setRequestedOrientation(byte0);
		
		return;
	}

}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.AppAdapterAndroid
 * JD-Core Version:    0.6.0
 */