// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import android.os.Parcel;
import android.os.Parcelable;

public class BrowserConfiguration
	implements Parcelable
{

	public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

		public BrowserConfiguration createFromParcel(Parcel parcel)
		{
			return new BrowserConfiguration(parcel);
		}

//		public volatile Object createFromParcel(Parcel parcel)
//		{
//			return createFromParcel(parcel);
//		}

		public BrowserConfiguration[] newArray(int i)
		{
			return new BrowserConfiguration[i];
		}
//
//		public volatile Object[] newArray(int i)
//		{
//			return newArray(i);
//		}

	};
	public final boolean isHtcBrowser;

	private BrowserConfiguration(Parcel parcel)
	{
		boolean flag;
		if (parcel.readInt() != 0)
			flag = true;
		else
			flag = false;
		isHtcBrowser = flag;
	}


	BrowserConfiguration(boolean flag)
	{
		isHtcBrowser = flag;
	}

	public int describeContents()
	{
		return 0;
	}

	public void writeToParcel(Parcel parcel, int i)
	{
		int j;
		if (isHtcBrowser)
			j = 1;
		else
			j = 0;
		parcel.writeInt(j);
	}

}
