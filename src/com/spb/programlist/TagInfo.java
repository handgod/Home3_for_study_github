// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;

import android.content.*;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.softspb.util.CollectionFactory;
import com.softspb.util.Conditions;
import java.net.URISyntaxException;
import java.util.*;

// Referenced classes of package com.spb.programlist:
//			Pattern, ProgramList, ProgramsUtil

class TagInfo
{

	static final int ADDED = 0;
	static final int ALREADY_EXIST = 1;
	static final int DOESNT_MATCH = 2;
	Collection activities;
	private Set addedActivities;
	Collection antiPatterns;
	private ActivityInfo defaultActivity;
	private final String name;
	Collection patterns;

	TagInfo(String s, Collection collection)
	{
		super();
		boolean flag = true;
		addedActivities = CollectionFactory.newHashSet();
		boolean flag1;
		if (!TextUtils.isEmpty(s))
			flag1 = flag;
		else
			flag1 = false;
		Conditions.checkArgument(flag1);
		name = s;
		antiPatterns = CollectionFactory.newLinkedList();
		patterns = CollectionFactory.newLinkedList();
		activities = CollectionFactory.newLinkedList();
		for (Iterator iterator = collection.iterator(); iterator.hasNext();)
		{
			Pattern pattern = (Pattern)iterator.next();
			if (pattern.isAntiPattern())
				antiPatterns.add(pattern);
			else
				patterns.add(pattern);
		}

		if (patterns.isEmpty())
			flag = false;
		Conditions.checkArgument(flag, "You must specify at least one pattern");
	}

	final int add(ActivityInfo activityinfo, PackageManager packagemanager)
	{
		byte byte0;
		String s;
		byte0 = 2;
		Conditions.checkNotNull(activityinfo);
		Conditions.checkNotNull(packagemanager);
		s = ProgramList.getUnique(activityinfo);
		if (!addedActivities.contains(s)) 
		{
			for (Iterator iterator = antiPatterns.iterator(); iterator.hasNext();)
				if (((Pattern)iterator.next()).match(packagemanager, activityinfo))
					continue; /* Loop/switch isn't completed */

			Iterator iterator1 = patterns.iterator();
			do
				if (!iterator1.hasNext())
					continue; /* Loop/switch isn't completed */
			while (!((Pattern)iterator1.next()).match(packagemanager, activityinfo));
			activities.add(activityinfo);
			addedActivities.add(s);
			byte0 = 0;
		}
		else
		{
			byte0 = 1;
		}
		return byte0;
	}

	ActivityInfo find(Context context)
	{
		PackageManager packagemanager;
		ActivityInfo activityinfo;
	
		Conditions.checkNotNull(context);
		packagemanager = context.getPackageManager();
		try
		{
			packagemanager.getActivityInfo(new ComponentName(defaultActivity.packageName, defaultActivity.name), 0);
		}
		catch (Exception exception)
		{
			defaultActivity = null;
		}
		if (defaultActivity == null) 
		{
			for (Iterator iterator = patterns.iterator(); iterator.hasNext();)
			{
				activityinfo = ((Pattern)iterator.next()).getActivityInfo(packagemanager);
				if (activityinfo != null)
					continue; /* Loop/switch isn't completed */
			}

			activityinfo = null;
		}
		else
		{
			activityinfo = defaultActivity;
		}
		return activityinfo;
	}

	Intent findIntent(PackageManager packagemanager)
	{
		Iterator iterator;
		Intent intent;
		Conditions.checkNotNull(packagemanager);
		iterator = patterns.iterator();
		ActivityInfo activityinfo = null;
		do
		{
			if (!iterator.hasNext())
				break;
			activityinfo = ((Pattern)iterator.next()).getActivityInfo(packagemanager);
		} while (activityinfo == null);
		Intent intent1 = null;
		try {
			intent1 = Intent.parseUri(activityinfo.name, 0);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		intent = intent1;
		return intent;

	}

	List getAllPrograms(Context context, PackageManager packagemanager)
	{
		boolean flag;
		LinkedList linkedlist;
		Iterator iterator;
		flag = false;
		linkedlist = new LinkedList();
		iterator = activities.iterator();
		ProgramInfo programinfo;

		while (iterator.hasNext()) 
			{
				ActivityInfo activityinfo = (ActivityInfo)iterator.next();
				if (iterator.hasNext() || flag) 
					{
						if (!flag)
						{
							Iterator iterator1 = patterns.iterator();
							do
							{
								if (!iterator1.hasNext())
									break;
								flag |= ((Pattern)iterator1.next()).isDefault(packagemanager, activityinfo);
								if (!flag)
									continue;
								defaultActivity = activityinfo;
								break;
							} while (true);
							programinfo = ProgramsUtil.addFromActivity(context, packagemanager, activityinfo, name, flag);
							continue; /* Loop/switch isn't completed */
						}
						programinfo = ProgramsUtil.addFromActivity(context, packagemanager, activityinfo, name, false);
						if (programinfo != null)
							linkedlist.add(programinfo);
					}
				else
					{
				
					defaultActivity = activityinfo;
					programinfo = ProgramsUtil.addFromActivity(context, packagemanager, activityinfo, name, true);
					}
			}
		{
			return linkedlist;
		}
	}

		String getName()
		{
			return name;
		}
	  void remove(String paramString, PackageManager paramPackageManager)
	  {
	    if (paramString == null)
	      return;
	    else
	    {
	      int i = 0;
	      Iterator localIterator1 = this.activities.iterator();
	      ActivityInfo localActivityInfo = null;
	      while (localIterator1.hasNext())
	      {
	        localActivityInfo = (ActivityInfo)localIterator1.next();
	        String str1 = localActivityInfo.packageName;
	        if (!paramString.equals(str1))
	          continue;
	        if (this.defaultActivity != null)
	        {
	          String str2 = this.defaultActivity.packageName;
	          if (paramString.equals(str2))
	            i = 1;
	        }
	        Set localSet = this.addedActivities;
	        String str3 = ProgramList.getUnique(localActivityInfo);
	        boolean bool = localSet.remove(str3);
	        localIterator1.remove();
	      }
	  
	      localIterator1 = this.activities.iterator();
	      Iterator localIterator2;
	   
	      do
	      {
	        if (!localIterator1.hasNext())
	          break ;
	        localActivityInfo = (ActivityInfo)localIterator1.next();
	        if (!localIterator1.hasNext())
	        {
	          this.defaultActivity = localActivityInfo;
	          break ;
	        }
	        localIterator2 = this.patterns.iterator();
	        if (!localIterator2.hasNext())
	          break;
	      }
	      while (!((Pattern)localIterator2.next()).isDefault(paramPackageManager, localActivityInfo));
	      this.defaultActivity = localActivityInfo;
	    }
	  }
	
}
