// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.calendar.service;

import android.os.Parcel;
import android.os.Parcelable;

public class Appointment
	implements Parcelable
{

	public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

		public Appointment createFromParcel(Parcel parcel)
		{
			return new Appointment(parcel);
		}

//		public volatile Object createFromParcel(Parcel parcel)
//		{
//			return createFromParcel(parcel);
//		}

		public Appointment[] newArray(int i)
		{
			return new Appointment[i];
		}

//		public volatile Object[] newArray(int i)
//		{
//			return newArray(i);
//		}

	};
	public final long end;
	public final long id;
	public final boolean isAllDay;
	public final boolean isRecurring;
	public final String location;
	public final long originalEnd;
	public final long originalStart;
	public final long start;
	public final int status;
	public final String subject;
	public final int token;

	public Appointment(int i, String s, String s1, long l, long l1, 
			long l2, long l3, boolean flag, boolean flag1, int j, 
			long l4)
	{
		token = i;
		subject = s;
		location = s1;
		start = l;
		end = l1;
		originalStart = l2;
		originalEnd = l3;
		isAllDay = flag;
		isRecurring = flag1;
		status = j;
		id = l4;
	}

	Appointment(Parcel parcel)
	{
		super();
		
		boolean flag = true;
		token = parcel.readInt();
		subject = parcel.readString();
		location = parcel.readString();
		start = parcel.readLong();
		end = parcel.readLong();
		originalStart = parcel.readLong();
		originalEnd = parcel.readLong();
		boolean flag1;
		if (parcel.readInt() != 0)
			flag1 = flag;
		else
			flag1 = false;
		isAllDay = flag1;
		if (parcel.readInt() == 0)
			flag = false;
		isRecurring = flag;
		status = parcel.readInt();
		id = parcel.readLong();
	}

	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel parcel, int i)
	{
		int j = 1;
		parcel.writeInt(token);
		parcel.writeString(subject);
		parcel.writeString(location);
		parcel.writeLong(start);
		parcel.writeLong(end);
		parcel.writeLong(originalStart);
		parcel.writeLong(originalEnd);
		int k;
		if (isAllDay)
			k = j;
		else
			k = 0;
		parcel.writeInt(k);
		if (!isRecurring)
			j = 0;
		parcel.writeInt(j);
		parcel.writeInt(status);
		parcel.writeLong(id);
	}

}
