// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.*;
import android.text.TextUtils;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.*;

// Referenced classes of package com.spb.programlist:
//			Pattern, ProgramsUtil

class IntentPattern extends Pattern
{
	static class PatternBuilder
	{

		final Intent intent;
		boolean isAntiPattern;
		boolean matchPackage;

		final PatternBuilder antiPattern(boolean flag)
		{
			isAntiPattern = flag;
			return this;
		}

		Pattern create()
		{
			return new IntentPattern(intent, matchPackage, isAntiPattern);
		}

		final PatternBuilder matchPackage(boolean flag)
		{
			matchPackage = flag;
			return this;
		}

		PatternBuilder(Intent intent1)
		{
			matchPackage = false;
			isAntiPattern = false;
			intent = intent1;
		}
	}


	static String DUMMY_PACKAGE_NAME = ".spb.intent";
	static final String RESOLVER_ACTIVITY_CLASS_NAME = "com.android.internal.app.ResolverActivity";
	static final String RESOLVER_ACTIVITY_PACKAGE_NAME = "android";
	private static Logger logger = Loggers.getLogger(IntentPattern.class);
	private Intent intent;
	private final boolean isAntiPattern;
	private boolean matchPackage;

	IntentPattern(Intent intent1, boolean flag, boolean flag1)
	{
		intent = intent1;
		matchPackage = flag;
		isAntiPattern = flag1;
	}

	private boolean checkInfoExact(String s, String s1, String s2, ResolveInfo resolveinfo)
	{
		boolean flag = false;
		if (resolveinfo != null && s != null && s1 != null)
		{
			if (s2 == null)
				s2 = "";
			ActivityInfo activityinfo = resolveinfo.activityInfo;
			if (s.equals(activityinfo.packageName) && (s1.equals(activityinfo.name) || s2.equals(activityinfo.name)))
				flag = true;
		}
		else 
		{
			return flag;
		}
		return flag;
	}

	private boolean checkInfoPackage(String s, String s1, ResolveInfo resolveinfo)
	{
		boolean flag;
		if (resolveinfo.activityInfo.packageName.equals(s))
			flag = true;
		else
			flag = false;
		return flag;
	}

	 private ActivityInfo findSystemOrDefault(PackageManager paramPackageManager)
	  {
	    Intent localIntent1 = this.intent;
	    ActivityInfo localActivityInfo;
	    ResolveInfo localResolveInfo1 = paramPackageManager.resolveActivity(localIntent1, 0);
	    ResolveInfo localResolveInfo2 = null;
	    if (localResolveInfo1 != null)
	    {
	      localActivityInfo = localResolveInfo1.activityInfo;
	      if (localActivityInfo != null)
	      {
	        String str1 = localActivityInfo.name;
	        if ("com.android.internal.app.ResolverActivity".equals(str1))
	        {
	          String str2 = localActivityInfo.packageName;
	          if ("android".equals(str2))
	          {
	            Intent localIntent2 = this.intent;
	            Iterator localIterator = paramPackageManager.queryIntentActivities(localIntent2, 65536).iterator();
	            do
	            {
	              if (!localIterator.hasNext())
	                break;
	              localResolveInfo2 = (ResolveInfo)localIterator.next();
	            }
	            while (!ProgramsUtil.isSystem(localResolveInfo2.activityInfo));
	          }
	        }
	      }
	    }
	    for (localActivityInfo = localResolveInfo2.activityInfo; ; localActivityInfo = null)
	      return localActivityInfo;
	  }
	ActivityInfo getActivityInfo(PackageManager packagemanager)
	{
		ActivityInfo activityinfo;
		if (packagemanager.resolveActivity(intent, 0) == null)
		{
			activityinfo = null;
		} else
		{
			activityinfo = new ActivityInfo(findSystemOrDefault(packagemanager));
			activityinfo.packageName = DUMMY_PACKAGE_NAME;
			activityinfo.name = intent.toUri(0).toString();
		}
		return activityinfo;
	}

	boolean isAntiPattern()
	{
		return isAntiPattern;
	}

	 boolean isDefault(PackageManager paramPackageManager, ActivityInfo paramActivityInfo)
	  {
	    int i = 1;
	    Logger localLogger1 = logger;
	    Object[] arrayOfObject = new Object[2];
	    Intent localIntent = this.intent;
	    arrayOfObject[0] = localIntent;
	    arrayOfObject[i] = paramActivityInfo;
	    localLogger1.d(">>>IntentPattern.isDefault intent=%s info=%s", arrayOfObject);
	    ActivityInfo localActivityInfo;
	    ComponentName localComponentName = this.intent.getComponent();
	    String str1 = null;
	    String str2 = null;
	    String str6;
	    boolean bool = false;
	    if (localComponentName != null)
	    {
	      str1 = localComponentName.getPackageName();
	      str2 = localComponentName.getClassName();
	      if (str2.equals(""))
	      {
	        String str3 = paramActivityInfo.packageName;
	        bool = TextUtils.equals(str1, str3);
	      }
	    }
	    int j;
	   
	    while (true)
	    {
	   
	      if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)))
	      {
	        String str4 = paramActivityInfo.packageName;
	        if (str1.equals(str4))
	        {
	          String str5 = paramActivityInfo.name;
	          if (str2.equals(str5))
	            continue;
	        }
	        j = 0;
	        continue;
	      }
	      localActivityInfo = findSystemOrDefault(paramPackageManager);
	      if (localActivityInfo != null)
	        break;
	      j = 0;
	    }
	     str6 = paramActivityInfo.packageName;
	     str2 = paramActivityInfo.name;
	    if ((TextUtils.equals(localActivityInfo.packageName, str6)) && (TextUtils.equals(localActivityInfo.name, str2)));
	    int m;
	    for (int k = 1; ; m = 0)
	    {
	      Logger localLogger2 = logger;
	      String str7 = "<<<IntentPattern.isDefault result " + k;
	      localLogger2.d(str7);
	      j = k;
	      break;
	    }
	    return bool;
	  }
	

	  boolean match(PackageManager paramPackageManager, ActivityInfo paramActivityInfo)
	  {
	    boolean bool = true;
	    ComponentName localComponentName = this.intent.getComponent();
	    String str1 = null;
	    String str2 = null;
	    if (localComponentName != null)
	    {
	      str1 = localComponentName.getPackageName();
	      str2 = localComponentName.getClassName();
	      if (str2.equals(""))
	      {
	        String str3 = paramActivityInfo.packageName;
	        bool = TextUtils.equals(str1, str3);
	      }
	    }
	    else
	    {
	      if ((!TextUtils.isEmpty(str1)) && (!TextUtils.isEmpty(str2)))
	      {
	        String str4 = paramActivityInfo.packageName;
	        if (str1.equals(str4))
	        {
	          String str5 = paramActivityInfo.name;
	          if (str2.equals(str5))
	        	  bool = false;
	        }
	       
	        	bool = false;
	        	return bool;
	        }
	      }
	      str1 = paramActivityInfo.packageName;
	      str2 = paramActivityInfo.name;
	      String str6 = paramActivityInfo.targetActivity;
	      Intent localIntent = this.intent;
	      List localList = resolveIntent(paramPackageManager, localIntent);
	      Iterator localIterator = localList.iterator();
	      
	      while (localIterator.hasNext())
	        {
	          ResolveInfo localResolveInfo1 = (ResolveInfo)localIterator.next();
	          if (!checkInfoExact(str1, str2, str6, localResolveInfo1))
	            continue;
	        }
	      if(this.matchPackage)
	      {
	        localIterator = localList.iterator();
	        while (localIterator.hasNext())
	          {
	            ResolveInfo localResolveInfo2 = (ResolveInfo)localIterator.next();
	            if (!checkInfoPackage(str1, str2, localResolveInfo2))
	              continue;
	            break;
	          }
	      }
	      return bool;
	   }
	   
	  
	  
	List resolveIntent(PackageManager packagemanager, Intent intent1)
	{
		Object obj = packagemanager.queryIntentActivities(intent1, 0);
		if (obj == null)
			obj = new LinkedList();
		return ((List) (obj));
	}

}
