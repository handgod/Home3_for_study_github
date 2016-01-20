// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdateStatus
	implements Parcelable
{

	public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

		public UpdateStatus createFromParcel(Parcel parcel)
		{
			return new UpdateStatus(parcel);
		}

//		public volatile Object createFromParcel(Parcel parcel)
//		{
//			return createFromParcel(parcel);
//		}

		public UpdateStatus[] newArray(int i)
		{
			return new UpdateStatus[i];
		}

//		public volatile Object[] newArray(int i)
//		{
//			return newArray(i);
//		}

	};
	public static final int UPDATE_STATUS_FAILED = 999;
	public static final int UPDATE_STATUS_OK = 1;
	public static final int UPDATE_STATUS_UNKNOWN = 0;
	public static final int UPDATE_STATUS_UPDATING = 2;
	public int cityId;
	public int currentConditionsStatus;
	public int forecastStatus;
	public long latestCurrentConditionsTimestamp;
	public long latestForecastTimestamp;
	public long latestSuccessfulCurrentConditionsTimestamp;
	public long latestSuccessfulForecastTimestamp;
	public int positioningStatus;

	public UpdateStatus(int i)
	{
		cityId = i;
	}

	private UpdateStatus(Parcel parcel)
	{
		cityId = parcel.readInt();
		latestCurrentConditionsTimestamp = parcel.readLong();
		latestForecastTimestamp = parcel.readLong();
		latestSuccessfulCurrentConditionsTimestamp = parcel.readLong();
		latestSuccessfulForecastTimestamp = parcel.readLong();
		currentConditionsStatus = parcel.readInt();
		forecastStatus = parcel.readInt();
		positioningStatus = parcel.readInt();
	}


	public UpdateStatus(UpdateStatus updatestatus, int i)
	{
		cityId = i;
		latestCurrentConditionsTimestamp = updatestatus.latestCurrentConditionsTimestamp;
		latestForecastTimestamp = updatestatus.latestForecastTimestamp;
		latestSuccessfulForecastTimestamp = updatestatus.latestSuccessfulForecastTimestamp;
		latestSuccessfulCurrentConditionsTimestamp = updatestatus.latestSuccessfulCurrentConditionsTimestamp;
		currentConditionsStatus = updatestatus.currentConditionsStatus;
		forecastStatus = updatestatus.forecastStatus;
		positioningStatus = updatestatus.positioningStatus;
	}

	public int describeContents()
	{
		return 0;
	}

	public long getLatestSuccessfulUpdateTimestamp()
	{
		long l;
		if (latestSuccessfulCurrentConditionsTimestamp > latestSuccessfulForecastTimestamp)
			l = latestSuccessfulCurrentConditionsTimestamp;
		else
			l = latestSuccessfulForecastTimestamp;
		return l;
	}

	public int getOverallStatus()
	{
		char c = '\001';
		if (forecastStatus != 999 && currentConditionsStatus != 999 && positioningStatus != 999)
			{
			if (forecastStatus == 2 || currentConditionsStatus == 2 || positioningStatus == 2)
				c = '\002';
			else
			if (forecastStatus != c && currentConditionsStatus != c && positioningStatus != c)
				c = '\0';
			}
		else 
			{
			c = '\u03E7';
			}
		return c;
	}

	public String toString()
	{
		return (new StringBuilder()).append("UpdateStatus[cityId=").append(cityId).append(" lct=").append(latestCurrentConditionsTimestamp).append(" lft=").append(latestForecastTimestamp).append(" lsct=").append(latestSuccessfulCurrentConditionsTimestamp).append(" lsft=").append(latestSuccessfulForecastTimestamp).append(" cs=").append(currentConditionsStatus).append(" fs=").append(forecastStatus).append(" ps=").append(positioningStatus).append("]").toString();
	}

	public void writeToParcel(Parcel parcel, int i)
	{
		parcel.writeInt(cityId);
		parcel.writeLong(latestCurrentConditionsTimestamp);
		parcel.writeLong(latestForecastTimestamp);
		parcel.writeLong(latestSuccessfulCurrentConditionsTimestamp);
		parcel.writeLong(latestSuccessfulForecastTimestamp);
		parcel.writeInt(currentConditionsStatus);
		parcel.writeInt(forecastStatus);
		parcel.writeInt(positioningStatus);
	}

}
