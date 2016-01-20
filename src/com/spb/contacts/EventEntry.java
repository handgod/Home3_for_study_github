// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.text.format.Time;

// Referenced classes of package com.spb.contacts:
//			DataEntry

class EventEntry extends DataEntry
{

	Time date;
	int type;

	EventEntry(int i, int j, Time time, int k)
	{
		super(i, j);
		date = time;
		type = k;
	}

	private boolean hasSameContent(EventEntry evententry)
	{
		boolean flag = false;
		if (type != evententry.type)
		{
			return flag;
		}
		else
		{
			if (type != 3) 
				{
				if (Time.compare(date, evententry.date) == 0)
					flag = true;
				}
			else 
				{
				flag = sameDayAndMonth(evententry);
				}
		}
		return flag;
	}

	 public boolean equals(Object paramObject)
	  {
		 boolean flag;
			flag = false;
	    if (paramObject == null)
	    {
	    	
	    }
	    else
	    {   
	      Class localClass1 = paramObject.getClass();
	      Class localClass2 = getClass();
	      if (localClass1 == localClass2)
	      {
	      EventEntry localEventEntry = (EventEntry)paramObject;
	      int j = this.id;
	      int k = localEventEntry.id;
	      if (!((j != k) || (!hasSameContent(localEventEntry))))
	      {
	      flag = true;
	      }
	      }
	    }
	    return flag;
	  }
	boolean isDuplicate(DataEntry dataentry)
	{
		boolean flag;
		flag = false;
		if (dataentry != null && dataentry.getClass() == getClass())
			flag = hasSameContent((EventEntry)dataentry);
		return flag;
	}

	boolean sameDayAndMonth(EventEntry evententry)
	{
		boolean flag;
		if (date.month == evententry.date.month && date.monthDay == evententry.date.monthDay)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public String toString()
	{
		String s;
		if (type == 3)
			s = "B";
		else
		if (type == 1)
			s = "A";
		else
			s = "O";
		return (new StringBuilder()).append("Event[id=").append(id).append(" ").append(date.format3339(true)).append(" ").append(s).append("]").toString();
	}
}
