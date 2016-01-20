package com.softspb.shell.adapters.alarms;

import android.content.Context;
import android.database.Cursor;

import com.softspb.shell.adapters.alarms.AlarmsAdapterAndroid.Alarm;
import com.softspb.util.DateChangedObserver;
import com.softspb.util.DateChangedObserver.DateChangedListener;

public class BaseAlarmsAdapter extends AlarmsAdapter
  implements DateChangedObserver.DateChangedListener
{
	
//  public BaseAlarmsAdapter(int paramInt, Context paramContext)
//  {
//    super(paramInt, paramContext);
//  }
	public BaseAlarmsAdapter(int i, Context context)
	{
		super(i, context);
	}

//	protected  ContentItem createContentItem(int i, Cursor cursor)
//	{
//		return createContentItem(i, cursor);
//	}

	protected Alarm createContentItem(int i, Cursor cursor)
	{
		return null;
	}

  protected int getContentItemId(Cursor paramCursor)
  {
    return 0;
  }

  public void onDateChanged()
  {
    int i = this.nativePeer;
    onDateChanged(i);
  }

  public void onStart()
  {
    super.onStart();
    DateChangedObserver.getInstance().registerListener(this);
  }

  public void onStop()
  {
    super.onStop();
    DateChangedObserver.getInstance().unregisterListener(this);
  }

  protected Cursor query()
  {
    return null;
  }

@Override
protected void addNativeContentItem(ContentItem paramT) {
	// TODO Auto-generated method stub
	
}


}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.alarms.BaseAlarmsAdapter
 * JD-Core Version:    0.6.0
 */