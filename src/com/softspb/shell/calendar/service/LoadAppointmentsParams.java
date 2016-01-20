// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.calendar.service;

import android.os.Parcel;
import android.os.Parcelable;

public class LoadAppointmentsParams
	implements Parcelable
{

	public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

		public LoadAppointmentsParams createFromParcel(Parcel parcel)
		{
			return new LoadAppointmentsParams(parcel);
		}

//		public volatile Object createFromParcel(Parcel parcel)
//		{
//			return createFromParcel(parcel);
//		}

		public LoadAppointmentsParams[] newArray(int i)
		{
			return new LoadAppointmentsParams[i];
		}

//		public volatile Object[] newArray(int i)
//		{
//			return newArray(i);
//		}

	};
	public final long searchEndDate;
	public final long searchStartDate;
	public final int token;

	public LoadAppointmentsParams(int i, long l, long l1)
	{
		token = i;
		searchStartDate = l;
		searchEndDate = l1;
	}

	LoadAppointmentsParams(Parcel parcel)
	{
		token = parcel.readInt();
		searchStartDate = parcel.readLong();
		searchEndDate = parcel.readLong();
	}

	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeInt(token);
		parcel.writeLong(searchStartDate);
		parcel.writeLong(searchEndDate);
	}

}
