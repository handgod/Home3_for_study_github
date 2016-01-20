// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.programlist;

import android.app.Application;
import android.content.*;
import android.content.pm.*;
import android.net.Uri;
import com.softspb.util.CollectionFactory;
import com.softspb.util.Conditions;
import com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;

// Referenced classes of package com.spb.programlist:
//			TagSources, TagInfo, ProgramListListener, ProgramsUtil, 
//			ProgramInfo, IntentPattern, AlarmTagInfo, Pattern

public class ProgramList
{
	private class TagFactory
		implements TagSources.ITagFactory
	{

		final ProgramList this$0;

		public TagInfo create(String s, Collection collection)
		{
			Object obj;
			if ("alarm".equals(s))
				obj = new AlarmTagInfo(collection);
			else
				obj = new TagInfo(s, collection);
			return ((TagInfo) (obj));
		}

		public IntentPattern.PatternBuilder getPatternBuilder(Intent intent)
		{
			return new PatternBuilder(intent);
		}

		private TagFactory()
		{
			super();
			this$0 = ProgramList.this;
		}

	}

	private class CachedIntentPattern extends IntentPattern
	{

		final ProgramList this$0;

		public List resolveIntent(PackageManager packagemanager, Intent intent)
		{
			List list;
			if (useCache)
			{
				list = (List)resolvedIntents.get(intent);
				if (list == null)
					list = super.resolveIntent(packagemanager, intent);
				resolvedIntents.put(intent, list);
			} else
			{
				list = super.resolveIntent(packagemanager, intent);
			}
			return list;
		}

		private CachedIntentPattern(Intent intent, boolean flag, boolean flag1)
		{
			super(intent, flag, flag1);
			this$0 = ProgramList.this;
		}

	}

	private class PatternBuilder extends IntentPattern.PatternBuilder
	{

		final ProgramList this$0;

		public Pattern create()
		{
			return new CachedIntentPattern(intent, matchPackage, isAntiPattern);
		}

		public PatternBuilder(Intent intent)
		{
			super(intent);
			this$0 = ProgramList.this;
		}
	}


	public static final String CATEGORY_SPB_LAUNCHER = "com.spb.shell3d.intent.category.SPB_LAUNCHER";
	private static Logger logger = Loggers.getLogger(ProgramList.class.getName());
	private Context context;
	private DecoratedBroadcastReceiver externalAppReceiver;
	private final List listeners = CollectionFactory.newLinkedList();
	private BroadcastReceiver packageChangeReceiver;
	private PackageManager pm;
	private Map resolvedIntents;
	private Map tags;
	private boolean useCache;
	private Collection withoutTag;
	private Set woTagAdded;

	public ProgramList(Context context1)
	{
		packageChangeReceiver = new BroadcastReceiver() {

			final ProgramList this$0;

			public void onReceive(Context context2, Intent intent)
			{
				String s = intent.getAction();
				String s1 = intent.getData().getSchemeSpecificPart();
				if ("android.intent.action.PACKAGE_REMOVED".equals(s) || "android.intent.action.PACKAGE_CHANGED".equals(s))
					remove(s1);
				if ("android.intent.action.PACKAGE_ADDED".equals(s) || "android.intent.action.PACKAGE_CHANGED".equals(s))
					reload(s1);
			}

			
			{
				this$0 = ProgramList.this;
//				super();
			}
		};
		useCache = false;
		resolvedIntents = CollectionFactory.newHashMap();
		withoutTag = CollectionFactory.newLinkedList();
		woTagAdded = CollectionFactory.newHashSet();
		externalAppReceiver = new DecoratedBroadcastReceiver();
		context = context1;
		tags = new HashMap();
		TagInfo taginfo;
		for (Iterator iterator = TagSources.getTags(new TagFactory()).iterator(); iterator.hasNext(); tags.put(taginfo.getName(), taginfo))
			taginfo = (TagInfo)iterator.next();

		pm = context.getPackageManager();
	}

	public static ProgramList getInstance(Context context1)
	{
		Context context2 = context1.getApplicationContext();
		if (!(context2 instanceof Application))
			throw new IllegalArgumentException((new StringBuilder()).append("Application context is not an Application instance: ").append(context2).toString());
		Application application = (Application)context2;
		Class class1 = application.getClass();
		ProgramList programlist;
		try
		{
			Class aclass[] = new Class[1];
			aclass[0] = Context.class;
			Method method = class1.getMethod("getProgramList", aclass);
			Object aobj[] = new Object[1];
			aobj[0] = context1;
			programlist = (ProgramList)method.invoke(application, aobj);
		}
		catch (NoSuchMethodException nosuchmethodexception)
		{
			throw new IllegalArgumentException("Application class must define getProgramList() method", nosuchmethodexception);
		}
		catch (Exception exception)
		{
			throw new IllegalArgumentException("Failed to invoke getProgramList() method", exception);
		}
		return programlist;
	}

	private List getLauncherActivities(String s)
	{
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		Intent intent1 = new Intent("android.intent.action.MAIN");
		intent1.addCategory("com.spb.shell3d.intent.category.SPB_LAUNCHER");
		if (s != null)
		{
			intent.setPackage(s);
			intent1.setPackage(s);
		}
		Object obj = pm.queryIntentActivities(intent, 0);
		List list = pm.queryIntentActivities(intent1, 0);
		if (obj == null)
			obj = new LinkedList();
		if (list != null)
			((List) (obj)).addAll(list);
		return ((List) (obj));
	}

	static String getUnique(ActivityInfo activityinfo)
	{
		return (new StringBuilder()).append(activityinfo.packageName).append("..").append(activityinfo.name).toString();
	}

	private static void logTrace()
	{
		Exception exception = new Exception();
		exception.fillInStackTrace();
		StackTraceElement astacktraceelement[] = exception.getStackTrace();
		for (int i = 1; i < astacktraceelement.length; i++)
			logger.d((new StringBuilder()).append("|  ").append(astacktraceelement[i]).toString());

	}

	private void notifyOnAdded(ProgramInfo programinfo)
	{
		List list = listeners;
		synchronized (list) {
			for (Iterator iterator = listeners.iterator(); iterator.hasNext(); ((ProgramListListener)iterator.next()).onAdded(programinfo));	
		}
	}

	private void notifyOnDeleted(String s)
	{
		List list = listeners;
		synchronized (list) {
			for (Iterator iterator = listeners.iterator(); iterator.hasNext(); ((ProgramListListener)iterator.next()).onDeleted(s));	
		}
	}

	/**
	 * @deprecated Method reload is deprecated
	 */
	 private synchronized void reload(String paramString)
	  {
		 int i = 1;
	    Iterator localIterator1;
	    Iterator localIterator2;
	    while (true)
	    {
	      TagInfo localTagInfo1;
	      int j;
	      try
	      {
	        Logger localLogger1 = logger;
	        String str1 = "reload >>> packageName=" + paramString;
	        localLogger1.d(str1);
	        logTrace();
	        logger.d("reload: querying activities...");
	        localIterator1 = getLauncherActivities(paramString).iterator();
	        if (!localIterator1.hasNext())
	          break;
	        ResolveInfo localResolveInfo = (ResolveInfo)localIterator1.next();
	        i = 0;
	        Logger localLogger2 = logger;
	        StringBuilder localStringBuilder1 = new StringBuilder().append("reload: found activity: ");
	        String str2 = localResolveInfo.activityInfo.packageName;
	        StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(":");
	        String str3 = localResolveInfo.activityInfo.name;
	        String str4 = str3;
	        localLogger2.d(str4);
	        localIterator2 = this.tags.values().iterator();
	        if (!localIterator2.hasNext())
	          continue;
	        localTagInfo1 = (TagInfo)localIterator2.next();
	        ActivityInfo localActivityInfo1 = localResolveInfo.activityInfo;
	        PackageManager localPackageManager1 = this.pm;
	        j = localTagInfo1.add(localActivityInfo1, localPackageManager1);
	        if (j == 0)
	        {
	          Logger localLogger3 = logger;
	          StringBuilder localStringBuilder3 = new StringBuilder().append("Matching against tag \"");
	          String str5 = localTagInfo1.getName();
	          String str6 = str5 + "\": ADDED";
	          localLogger3.d(str6);
	          i = 1;
	          if (paramString == null)
	            continue;
	          Context localContext1 = this.context;
	          PackageManager localPackageManager2 = this.pm;
	          ActivityInfo localActivityInfo2 = localResolveInfo.activityInfo;
	          String str7 = localTagInfo1.getName();
	          ProgramInfo localProgramInfo1 = ProgramsUtil.addFromActivity(localContext1, localPackageManager2, localActivityInfo2, str7, false);
	          if (localProgramInfo1 == null)
	            continue;
	          notifyOnAdded(localProgramInfo1);
	          String str8 = getUnique(localResolveInfo.activityInfo);
	          if ((i != 0) || (this.woTagAdded.contains(str8)))
	            continue;
	          Context localContext2 = this.context;
	          PackageManager localPackageManager3 = this.pm;
	          ActivityInfo localActivityInfo3 = localResolveInfo.activityInfo;
	          localProgramInfo1 = ProgramsUtil.addFromActivity(localContext2, localPackageManager3, localActivityInfo3, null, false);
	          if (localProgramInfo1 == null)
	            continue;
	          notifyOnAdded(localProgramInfo1);
	          boolean bool1 = this.woTagAdded.add(str8);
	          Collection localCollection = this.withoutTag;
	          ActivityInfo localActivityInfo4 = localResolveInfo.activityInfo;
	          boolean bool2 = localCollection.add(localActivityInfo4);
	          continue;
	        }
	      }
	      finally
	      {
	      
	      }
	      if (j != 1)
	        continue;
	      Logger localLogger4 = logger;
	      StringBuilder localStringBuilder4 = new StringBuilder().append("Matching against tag \"");
	      String str9 = localTagInfo1.getName();
	      String str10 = str9 + "\": ALREADY EXISTS";
	      localLogger4.d(str10);
	     
	    }
	    if (paramString == null)
	    {
	      localIterator1 = this.tags.values().iterator();
	      while (localIterator1.hasNext())
	      {
	        TagInfo localTagInfo2 = (TagInfo)localIterator1.next();
	        Context localContext3 = this.context;
	        PackageManager localPackageManager4 = this.pm;
	        localIterator2 = localTagInfo2.getAllPrograms(localContext3, localPackageManager4).iterator();
	        while (localIterator2.hasNext())
	        {
	          ProgramInfo localProgramInfo2 = (ProgramInfo)localIterator2.next();
	          notifyOnAdded(localProgramInfo2);
	        }
	      }
	    }
	    logger.d("reload <<<");
	  }
	private void reloadAll()
	{
		logger.d("reloadAll >>>");
		reload(null);
		logger.d("reloadAll <<<");
	}

	/**
	 * @deprecated Method remove is deprecated
	 */
	 private synchronized void remove(String paramString)
	  {
	    
	    try
	    {
	      notifyOnDeleted(paramString);
	      Iterator localIterator1 = this.tags.values().iterator();
	      while (localIterator1.hasNext())
	      {
	        TagInfo localTagInfo = (TagInfo)localIterator1.next();
	        PackageManager localPackageManager = this.pm;
	        localTagInfo.remove(paramString, localPackageManager);
	      }
	    }
	    finally
	    {
	      
	    }
	    Iterator localIterator2 = this.withoutTag.iterator();
	    while (localIterator2.hasNext())
	    {
	      ActivityInfo localActivityInfo = (ActivityInfo)localIterator2.next();
	      String str1 = localActivityInfo.packageName;
	      if (!paramString.equals(str1))
	        continue;
	      Set localSet = this.woTagAdded;
	      String str2 = getUnique(localActivityInfo);
	      boolean bool = localSet.remove(str2);
	      localIterator2.remove();
	    }
	    
	  }
	 private boolean startActivity(Intent paramIntent)
	  {
		 boolean flag = false;
	    try
	    {
	      String str = paramIntent.getAction();
	      if ((!"android.settings.MANAGE_APPLICATIONS_SETTINGS".equals(str)) && (!"android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS".equals(str)))
	    	  	paramIntent.setFlags(270532608);
	      this.context.startActivity(paramIntent);
	      flag = true;
	      return flag;
	    }
	    catch (ActivityNotFoundException localActivityNotFoundException)
	    { 
	    	return flag;
	    }
	  }

	/**
	 * @deprecated Method findByTag is deprecated
	 */

	public synchronized ProgramInfo findByTag(String s)
	{

		ProgramInfo programinfo = null;
		TagInfo taginfo = (TagInfo)tags.get(Conditions.checkNotNull(s));
		if (taginfo != null)
		{
			ActivityInfo activityinfo = taginfo.find(context);
			if (activityinfo != null)
			{
				programinfo = ProgramsUtil.addFromActivity(context, pm, activityinfo, s, false, false);
				if (programinfo != null)
					notifyOnAdded(programinfo);
			}
		}
		return programinfo;
	}

	/**
	 * @deprecated Method getIntent is deprecated
	 */

	public synchronized Intent getIntent(String s)
	{
		Intent intent;
		TagInfo taginfo = (TagInfo)tags.get(Conditions.checkNotNull(s));
		if (taginfo == null) 
		{
			intent = null;
		}
		else 
		{
			Intent intent1 = taginfo.findIntent(pm);
			intent = intent1;
		}
		return intent;

	}

	/**
	 * @deprecated Method launch is deprecated
	 */

	public synchronized boolean launch(String s)
	{
		boolean flag;
		boolean flag1;
		TagInfo taginfo = (TagInfo)tags.get(Conditions.checkNotNull(s));
		flag = false;
		if (taginfo != null)
		{
			Intent intent = taginfo.findIntent(pm);
			if (intent != null)
			{
				flag1 = startActivity(intent);
				flag = flag1;
			}
		}
		return flag;
	}

	/**
	 * @deprecated Method launch is deprecated
	 */

	public synchronized boolean launch(String s, String s1)
	{
		Intent intent ;
		boolean flag2 = false;
		boolean flag = s.equals(IntentPattern.DUMMY_PACKAGE_NAME);
		if (!flag)
		{
			intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");
			intent.setComponent(new ComponentName(s, s1));
			boolean flag1 = startActivity(intent);
			flag2 = flag1;
		}
		else
		{
			Intent intent1 = null;
			try {
				intent1 = Intent.getIntent(s1);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			intent = intent1;
		}
		return flag2;
	}

	public void registerListener(ProgramListListener programlistlistener)
	{
		logger.d((new StringBuilder()).append("registerListener: l=").append(programlistlistener).toString());
		List list = listeners;
		synchronized (list) {	
			if (!listeners.contains(programlistlistener))
				listeners.add(programlistlistener);
		}
		return;
	}

	public void start()
	{
		logger.d("start >>>");
		useCache = true;
		reloadAll();
		useCache = false;
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction("android.intent.action.PACKAGE_ADDED");
		intentfilter.addAction("android.intent.action.PACKAGE_REMOVED");
		intentfilter.addAction("android.intent.action.PACKAGE_CHANGED");
		intentfilter.addDataScheme("package");
		externalAppReceiver.addActionListener("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final ProgramList this$0;

			public void onAction(Context context1, Intent intent)
			{
				String as[] = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
				int i = as.length;
				for (int j = 0; j < i; j++)
				{
					String s = as[j];
					reload(s);
				}

			}

			
			{
//				super();
				this$0 = ProgramList.this;
			}
		});
		externalAppReceiver.addActionListener("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE", new com.softspb.util.broadcastreceiver.DecoratedBroadcastReceiver.IActionListener() {

			final ProgramList this$0;

			public void onAction(Context context1, Intent intent)
			{
				String as[] = intent.getStringArrayExtra("android.intent.extra.changed_package_list");
				int i = as.length;
				for (int j = 0; j < i; j++)
				{
					String s = as[j];
					remove(s);
				}

			}

			
			{
				this$0 = ProgramList.this;
//				super();
			}
		});
		context.registerReceiver(packageChangeReceiver, intentfilter);
		context.registerReceiver(externalAppReceiver, externalAppReceiver.getIntentFilter());
		logger.d("start <<<");
	}

	public void stop()
	{
		context.unregisterReceiver(packageChangeReceiver);
		context.unregisterReceiver(externalAppReceiver);
	}

	public void uninstall(String s)
	{
		Intent intent = new Intent("android.intent.action.DELETE");
		intent.setData(Uri.parse((new StringBuilder()).append("package:").append((String)Conditions.checkNotNull(s)).toString()));
		ProgramsUtil.startActivitySafely(context, intent);
	}

	public void unregisterListener(ProgramListListener programlistlistener)
	{
		logger.d((new StringBuilder()).append("unregisterListener: l=").append(programlistlistener).toString());
		List list = listeners;
		synchronized (list) {
			listeners.remove(programlistlistener);	
		}
		return;
	}





}
