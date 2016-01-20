package com.softspb.shell.adapters;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public class GSensorAdapterAndroid extends GSensorAdapter
  implements SensorEventListener
{
  private static final Object lock = new Object();
  private static Logger logger = Loggers.getLogger(GSensorAdapterAndroid.class.getName());
  private float aX;
  private float aY;
  private float aZ;
  private Display display;
  private boolean enabled = false;
  boolean isAccurate = true;
  private boolean listening = false;
  private Sensor sensor;
  private SensorManager sm;

  public GSensorAdapterAndroid(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public static native void sensorMove(float paramFloat1, float paramFloat2, float paramFloat3, int paramInt);

  public void EnableGSensor(boolean paramBoolean)
  {
    this.enabled = paramBoolean;
    if (paramBoolean)
      onStart();
    else
    {
      onStop();
    }
  }

  public void askEvents()
  {
//		if (android.os.Build.VERSION.SDK_INT >= 8)
//			sensorMove(aX, aY, aZ, display.getRotation());
//		else
			sensorMove(aX, aY, aZ, display.getOrientation());
  }

  public boolean isPresent()
  {
   	boolean flag;
		if (sensor != null)
			flag = true;
		else
			flag = false;
		return flag;
  }

	public void onAccuracyChanged(Sensor sensor1, int i)
	{
		switch (i) {
		case 0:
			isAccurate = false;
			break;
		case 1:
			isAccurate = true;
			break;
		case 2:
			isAccurate = true;	
			break;
		case 3:
			isAccurate = true;		
			break;
		default:
			break;
		}
		return;
	}

  public void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    SensorManager localSensorManager = (SensorManager)paramContext.getSystemService("sensor");
    this.sm = localSensorManager;
    Sensor localSensor = this.sm.getDefaultSensor(1);
    this.sensor = localSensor;
    this.enabled = true;
    Display localDisplay = ((Activity)paramContext).getWindowManager().getDefaultDisplay();
    this.display = localDisplay;
  }

	public void onSensorChanged(SensorEvent sensorevent)
	{
		aX = sensorevent.values[0];
		aY = sensorevent.values[1];
		aZ = sensorevent.values[2];
		askEvents();
	}

	protected void onStart()
	{
		logger.d("GSensorAdapterAndroid::onStart()");
		if (enabled && !listening && isPresent())
		{
			sm.registerListener(this, sensor, 1);
			listening = true;
		}
	}

  protected void onStop()
  {
    logger.d("GSensorAdapterAndroid::onStop()");
    if ((this.listening) && (isPresent()))
    {
      this.sm.unregisterListener(this);
      this.listening = false;
    }
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.GSensorAdapterAndroid
 * JD-Core Version:    0.6.0
 */