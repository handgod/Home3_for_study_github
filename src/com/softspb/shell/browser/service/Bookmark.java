// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.browser.service;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Bookmark
	implements Parcelable
{

	public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

		public Bookmark createFromParcel(Parcel parcel)
		{
			return new Bookmark(parcel);
		}

//		public volatile Object createFromParcel(Parcel parcel)
//		{
//			return createFromParcel(parcel);
//		}

		public Bookmark[] newArray(int i)
		{
			return new Bookmark[i];
		}

//		public volatile Object[] newArray(int i)
//		{
//			return newArray(i);
//		}

	};
	private static final int PRIME = 0x5f5b9f1;
	long date;
	int favIconLength;
	final int id;
	int sumValue;
	String title;
	String url;
	byte xorValue;

	Bookmark(int i, Cursor cursor)
	{
		favIconLength = -1;
		xorValue = 0;
		sumValue = 0;
		id = i;
		update(cursor);
	}

	private Bookmark(Parcel parcel)
	{
		favIconLength = -1;
		xorValue = 0;
		sumValue = 0;
		id = parcel.readInt();
		title = parcel.readString();
		url = parcel.readString();
		date = parcel.readLong();
	}


	private void formatFields(StringBuilder stringbuilder)
	{
		stringbuilder.append(" title=").append(title).append(" url=").append(url).append(" date=").append(date);
	}

	private static int getSumValue(byte abyte0[])
	{
		int i;
		if (abyte0 == null)
		{
			i = 0;
		} else
		{
			i = 0;
			int j = abyte0.length;
			int k = 0;
			while (k < j) 
			{
				i = (i + abyte0[k]) % 0x5f5b9f1;
				k++;
			}
		}
		return i;
	}

	private static byte getXorValue(byte abyte0[])
	{
		byte byte0;
		if (abyte0 == null)
		{
			byte0 = 0;
		} else
		{
			byte0 = 0;
			int i = abyte0.length;
			int j = 0;
			while (j < i) 
			{
				byte0 ^= abyte0[j];
				j++;
			}
		}
		return byte0;
	}

	public int describeContents()
	{
		return 0;
	}

	public long getDate()
	{
		return date;
	}

	public int getId()
	{
		return id;
	}

	public String getTitle()
	{
		return title;
	}

	public String getUrl()
	{
		return url;
	}

	public String toString()
	{
		StringBuilder stringbuilder = (new StringBuilder()).append(getClass().getSimpleName()).append("[id=").append(id);
		formatFields(stringbuilder);
		return stringbuilder.append("]").toString();
	}

	boolean update(Cursor cursor)
	{
		boolean flag = false;
		long l = cursor.getLong(3);
		String s = cursor.getString(5);
		String s1 = cursor.getString(1);
		byte abyte0[] = cursor.getBlob(6);
		int i;
		byte byte0;
		int j;
		if (abyte0 == null)
			i = -1;
		else
			i = abyte0.length;
		byte0 = getXorValue(abyte0);
		j = getSumValue(abyte0);
		if (!TextUtils.equals(title, s))
		{
			title = s;
			flag = true;
		}
		if (!TextUtils.equals(url, s1))
		{
			url = s1;
			flag = true;
		}
		if (date != l)
		{
			date = l;
			flag = true;
		}
		if (favIconLength != i || sumValue != j || xorValue != byte0)
		{
			favIconLength = i;
			sumValue = j;
			xorValue = byte0;
			flag = true;
		}
		return flag;
	}

	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeInt(id);
		parcel.writeString(title);
		parcel.writeString(url);
		parcel.writeLong(date);
	}

}
