// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.contacts;

import android.text.format.Time;
import android.util.TimeFormatException;
import com.softspb.util.log.Logger;
import java.util.TimeZone;

final class ContactsUtils
{

	private static final int HALF_DAY = 31200;
	private static final int SECS_IN_DAY = 0x15180;

	private ContactsUtils()
	{
	}

	 static Time parseBirthdayDate(String paramString, int paramInt, TimeZone paramTimeZone, Logger paramLogger)
			    throws IllegalArgumentException
			  {
			    int i = paramString.length();
			    if (i >= 10);
			    Object localObject1 = null;
			    StringBuilder localStringBuilder12;
			    int i13;
			    int i14;
			    int i15;
			    char c;
			    return (Time)localObject1;
//			    while (true)
//			    {
//			      try
//			      {
//			        localObject1 = new Time();
//			        Object localObject2 = localObject1;
//			        String str1 = paramString;
//			        if (!localObject2.parse3339(str1))
//			          return TimelocalObject1;
//			        int j = ((Time)localObject1).hour * 3600;
//			        int k = ((Time)localObject1).minute * 60;
//			        int m = j + k;
//			        int n = ((Time)localObject1).second;
//			        int i1 = m + n;
//			        if (i1 == 0)
//			          continue;
//			        Time localTime1 = new Time();
//			        Time localTime2 = localTime1;
//			        Object localObject3 = localObject1;
//			        localTime2.<init>(localObject3);
//			        int i2 = paramInt;
//			        localTime1.year = i2;
//			        long l1 = localTime1.normalize(0);
//			        long l2 = localTime1.toMillis(0);
//			        TimeZone localTimeZone = paramTimeZone;
//			        long l3 = l2;
//			        int i3 = localTimeZone.getOffset(l3) / 1000;
//			        int i4 = 86400 - i3;
//			        if (i1 != i4)
//			          continue;
//			        StringBuilder localStringBuilder1 = new StringBuilder().append("parseBirthdayDate: correcting input by local offset: ");
//			        String str2 = paramString;
//			        String str3 = str2;
//			        paramLogger.d(str3);
//			        if (i3 <= 0)
//			          continue;
//			        int i5 = ((Time)localObject1).monthDay + 1;
//			        ((Time)localObject1).monthDay = i5;
//			        ((Time)localObject1).second = 0;
//			        ((Time)localObject1).minute = 0;
//			        ((Time)localObject1).hour = 0;
//			        ((Time)localObject1).allDay = 1;
//			        long l4 = ((Time)localObject1).normalize(1);
//			        ((Time)localObject1).second = 0;
//			        ((Time)localObject1).minute = 0;
//			        ((Time)localObject1).hour = 0;
//			        StringBuilder localStringBuilder2 = new StringBuilder().append("parseBirthdayDate: done: input=\"");
//			        String str4 = paramString;
//			        StringBuilder localStringBuilder3 = localStringBuilder2.append(str4).append("\" year=");
//			        int i6 = ((Time)localObject1).year;
//			        StringBuilder localStringBuilder4 = localStringBuilder3.append(i6).append(" month=");
//			        int i7 = ((Time)localObject1).month;
//			        StringBuilder localStringBuilder5 = localStringBuilder4.append(i7).append(" day=");
//			        int i8 = ((Time)localObject1).monthDay;
//			        StringBuilder localStringBuilder6 = localStringBuilder5.append(i8).append(" allDay=");
//			        boolean bool = ((Time)localObject1).allDay;
//			        StringBuilder localStringBuilder7 = localStringBuilder6.append(bool).append(" hour=");
//			        int i9 = ((Time)localObject1).hour;
//			        StringBuilder localStringBuilder8 = localStringBuilder7.append(i9).append(" minute=");
//			        int i10 = ((Time)localObject1).minute;
//			        StringBuilder localStringBuilder9 = localStringBuilder8.append(i10).append(" sec=");
//			        int i11 = ((Time)localObject1).second;
//			        String str5 = i11;
//			        paramLogger.d(str5);
//			        return localObject1;
//			        if (i1 + -31200 < 0)
//			          break label621;
//			        StringBuilder localStringBuilder10 = new StringBuilder().append("parseBirthdayDate: correcting input to next day: ");
//			        String str6 = paramString;
//			        String str7 = str6;
//			        paramLogger.d(str7);
//			        int i12 = ((Time)localObject1).monthDay + 1;
//			        ((Time)localObject1).monthDay = i12;
//			        continue;
//			      }
//			      catch (TimeFormatException localTimeFormatException)
//			      {
//			        StringBuilder localStringBuilder11 = new StringBuilder().append("Failed to parse3339: ");
//			        String str8 = paramString;
//			        String str9 = str8;
//			        paramLogger.w(str9);
//			      }
//			      localStringBuilder12 = new StringBuilder();
//			      i13 = paramInt;
//			      i14 = 0;
//			      while (true)
//			      {
//			        if (i14 >= i)
//			          break label1129;
//			        i15 = i14 + 1;
//			        c = paramString.charAt(i14);
//			        if (Character.isDigit(c))
//			        {
//			          StringBuilder localStringBuilder13 = localStringBuilder12.append(c);
//			          i14 = i15;
//			          continue;
//			          label621: StringBuilder localStringBuilder14 = new StringBuilder().append("parseBirthdayDate: correcting input to this day: ");
//			          String str10 = paramString;
//			          String str11 = str10;
//			          paramLogger.d(str11);
//			          break;
//			          label659: StringBuilder localStringBuilder15 = new StringBuilder().append("parseBirthdayDate: not correcting the input (unknown timezone): ");
//			          String str12 = paramString;
//			          String str13 = str12;
//			          paramLogger.d(str13);
//			          break;
//			        }
//			      }
//			      if (localStringBuilder12.length() != 0)
//			        break;
//			      i15 += 1;
//			    }
//			    while (true)
//			    {
//			      try
//			      {
//			        while (true)
//			        {
//			          int i16 = Integer.parseInt(localStringBuilder12.toString());
//			          i13 = i16;
//			          localStringBuilder12.setLength(0);
//			          i14 = i15;
//			          while (true)
//			            if (i14 < i)
//			            {
//			              i15 = i14 + 1;
//			              c = paramString.charAt(i14);
//			              if (Character.isDigit(c))
//			              {
//			                StringBuilder localStringBuilder16 = localStringBuilder12.append(c);
//			                i14 = i15;
//			                continue;
//			              }
//			              if (localStringBuilder12.length() == 0)
//			                i15 += 1;
//			              int i17;
//			              try
//			              {
//			                i16 = Integer.parseInt(localStringBuilder12.toString());
//			                i17 = i16 + -1;
//			                localStringBuilder12.setLength(0);
//			                for (i14 = i15; i14 < i; i14 = i15)
//			                {
//			                  i15 = i14 + 1;
//			                  c = paramString.charAt(i14);
//			                  if (!Character.isDigit(c))
//			                    break label918;
//			                  StringBuilder localStringBuilder17 = localStringBuilder12.append(c);
//			                }
//			              }
//			              catch (NumberFormatException localNumberFormatException1)
//			              {
//			                StringBuilder localStringBuilder18 = new StringBuilder().append("Failed to parse date=\"");
//			                String str14 = paramString;
//			                String str15 = str14 + "\"";
//			                throw new IllegalArgumentException(str15, localNumberFormatException1);
//			              }
//			              int i18 = i14;
//			              try
//			              {
//			                label918: i16 = Integer.parseInt(localStringBuilder12.toString());
//			                int i19 = i16;
//			                Time localTime3 = new Time();
//			                localTime3.set(0, 0, 0, i19, i17, i13);
//			                long l5 = localTime3.normalize(1);
//			                StringBuilder localStringBuilder19 = new StringBuilder().append("parseBirthdayDate: input=\"");
//			                String str16 = paramString;
//			                StringBuilder localStringBuilder20 = localStringBuilder19.append(str16).append("\" year=");
//			                int i20 = localTime3.year;
//			                StringBuilder localStringBuilder21 = localStringBuilder20.append(i20).append(" month=");
//			                int i21 = localTime3.month;
//			                StringBuilder localStringBuilder22 = localStringBuilder21.append(i21).append(" day=");
//			                int i22 = localTime3.monthDay;
//			                String str17 = i22;
//			                paramLogger.d(str17);
//			                localObject1 = localTime3;
//			              }
//			              catch (NumberFormatException localNumberFormatException2)
//			              {
//			                StringBuilder localStringBuilder23 = new StringBuilder().append("Failed to parse date=\"");
//			                String str18 = paramString;
//			                String str19 = str18 + "\"";
//			                throw new IllegalArgumentException(str19, localNumberFormatException2);
//			              }
//			            }
//			        }
//			      }
//			      catch (NumberFormatException localNumberFormatException3)
//			      {
//			        continue;
//			        i15 = i14;
//			        continue;
//			      }
//			      label1129: i15 = i14;
//			    }
			  }

			  static Time parseEventDate(String paramString, Time paramTime)
			  {
			    StringBuilder localStringBuilder1 = new StringBuilder();
			    int i = paramString.length();
			    int j = 0;
			    int k = 0;
			    char c;
			  
			      if (j < i)
			      {
			        k = j + 1;
			        c = paramString.charAt(j);
			        if (Character.isDigit(c))
			        {
			          StringBuilder localStringBuilder2 = localStringBuilder1.append(c);
			          j = k;
			        
			        }
			        if (localStringBuilder1.length() == 0)
			        
			        k += 1;
			      }
		
			    {
			      int m;
			      int n;
			      try
			      {
			        m = Integer.parseInt(localStringBuilder1.toString());
			        n = m;
			        localStringBuilder1.setLength(0);
			        j = k;
			        if (j < i)
			        {
			          k = j + 1;
			          c = paramString.charAt(j);
			          if (Character.isDigit(c))
			          {
			          StringBuilder localStringBuilder3 = localStringBuilder1.append(c);
			          j = k;
			          }
			        }
			      }
			      catch (NumberFormatException localNumberFormatException1)
			      {
			        n = 0;
			   
			        if (localStringBuilder1.length() == 0)
			          k += 1;
			      }
			      while (true)
			      {
			        int i1 = 0;
			        try
			        {
			          m = Integer.parseInt(localStringBuilder1.toString());
			          i1 = m + -1;
			          localStringBuilder1.setLength(0);
			          j = k;
			          if (j < i)
			          {
			            k = j + 1;
			            c = paramString.charAt(j);
			            if (!Character.isDigit(c))
			            	return paramTime;
			            StringBuilder localStringBuilder4 = localStringBuilder1.append(c);
			            j = k;
			            continue;
			          }
			        }
			        catch (NumberFormatException localNumberFormatException2)
			        {
			        }
			        while (true)
			        {
			         
			          int i2 = j;
			          try
			          {
			            label234: m = Integer.parseInt(localStringBuilder1.toString());
			            int i3 = m;
			            Time localTime = new Time();
			            int i4 = 0;
			            int i5 = 0;
			            localTime.set(0, i4, i5, i3, i1, n);
			            paramTime = localTime;
			          }
			          catch (NumberFormatException localNumberFormatException3)
			          {
			          }
			        }

			      }
	
			    }
			  
			  }
}
