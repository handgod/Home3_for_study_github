// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.programlist.ProgramsUtil;
import java.util.*;
//import ru.yandex.searchplugin.MainActivity;
//import ru.yandex.searchplugin.Utils;

// Referenced classes of package com.softspb.shell.adapters:
//			YandexSearchAdapter, AdaptersHolder

public class YandexAdapterAndroid extends YandexSearchAdapter
{

	private static final String SEARCH_URI_TEMPLATE = "searchWidget://search/?source=%s";
	private static final Map SOURCE_MAP = Collections.unmodifiableMap(new HashMap() {

			
			{
				put(Integer.valueOf(YandexSearchAdapter.SOURCE_SEARCH_BTN), "sbutton");
				put(Integer.valueOf(YandexSearchAdapter.SOURCE_WIDGET), "widget");
			}
	});
	private static final Set YANDEX_ISO_COUNTRIES = Collections.unmodifiableSet(new HashSet() {

			
			{
				add("RU");
				add("UA");
				add("BY");
				add("KZ");
				add("TR");
			}
	});
	private static final Set YANDEX_ISO_LANGS = Collections.unmodifiableSet(new HashSet() {

			
			{
				add("ru");
				add("uk");
				add("kk");
				add("be");
				add("tr");
			}
	});
	private static String YANDEX_SEARCH_PACKAGE = "ru.yandex.searchplugin";
	private static final Logger logger = Loggers.getLogger(YandexAdapterAndroid.class);
	private static BroadcastReceiver packageInstall = new BroadcastReceiver() {

		public void onReceive(Context context1, Intent intent)
		{
			String s = intent.getData().getSchemeSpecificPart();
			if (YandexAdapterAndroid.YANDEX_SEARCH_PACKAGE.equals(s))
			{
				byte byte0;
				if ("android.intent.action.PACKAGE_ADDED".equals(intent.getAction()))
					byte0 = 2;
				else
					byte0 = 1;
				YandexAdapterAndroid.setYComponentState(context1, byte0);
			}
		}

	};
	private Context context;

	public YandexAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
	}

	private Intent getYandexIntent(String s, String s1)
	{
//		ComponentName componentname = new ComponentName(context, MainActivity.class.getName());
//		String s2;
//		if (context.getPackageManager().getComponentEnabledSetting(componentname) == 2)
//			s2 = YANDEX_SEARCH_PACKAGE;
//		else
//			s2 = context.getPackageName();
//		return (new Intent(s)).addCategory(s1).setClassName(s2, ru.yandex.searchplugin.MainActivity.class.getName());
		return null;
	}

	private static boolean isYSearchInstalled(Context context1)
	{
//		boolean flag = false;
//		PackageManager packagemanager = context1.getPackageManager();
//		Intent intent = new Intent();
//		intent.setClassName(YANDEX_SEARCH_PACKAGE, ru.yandex.searchplugin.MainActivity.class.getName());
//		if (!packagemanager.queryIntentActivities(intent, 0).isEmpty())
//			flag = true;
//		return flag;
		return false;
	}

	private static void setYComponentState(Context context1, int i)
	{
//		Logger logger1 = logger;
//		Object aobj[] = new Object[1];
//		aobj[0] = Integer.valueOf(i);
//		logger1.d("setYcomponent state: %d", aobj);
//		context1.getPackageManager().setComponentEnabledSetting(new ComponentName(context1, MainActivity.class.getName()), i, 1);
	}

	public boolean isVoiceSearchAvailable()
	{
//		return Utils.voiceSearchV8APIAvailability(context);
		return false;
	}

	public boolean isYandexCountry()
	{
		boolean flag = false;
		String s = Locale.getDefault().getLanguage();
		String s1 = Locale.getDefault().getCountry();
		Logger logger1 = logger;
		Object aobj[] = new Object[2];
		aobj[0] = s;
		aobj[1] = s1;
		logger1.d("lang: %s, country: %s", aobj);
		if (YANDEX_ISO_COUNTRIES.contains(s1) || YANDEX_ISO_LANGS.contains(s))
			flag = true;
		return flag;
	}

	protected void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		context = context1;
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction("android.intent.action.PACKAGE_ADDED");
		intentfilter.addAction("android.intent.action.PACKAGE_REMOVED");
		intentfilter.addAction("android.intent.action.PACKAGE_CHANGED");
		intentfilter.addDataScheme("package");
		context1.registerReceiver(packageInstall, intentfilter);
		if (isYSearchInstalled(context1) || !isYandexCountry())
			setYComponentState(context1, 2);
		else
			setYComponentState(context1, 1);
	}

	protected void onDestroy(Context context1)
	{
		super.onDestroy(context1);
		context1.unregisterReceiver(packageInstall);
	}

	public void startSearch(int i)
	{
		if (!SOURCE_MAP.containsKey(Integer.valueOf(i)))
		{
			logger.e((new StringBuilder()).append("Starting search from unknown source ").append(i).toString());
		} else
		{
			Intent intent = getYandexIntent("android.intent.action.MAIN", "android.intent.category.DEFAULT");
			Object aobj[] = new Object[1];
			aobj[0] = SOURCE_MAP.get(Integer.valueOf(i));
			intent.setData(Uri.parse(String.format("searchWidget://search/?source=%s", aobj)));
			ProgramsUtil.startActivitySafely(context, intent);
		}
	}

	public void startVoiceSearch()
	{
		Intent intent = getYandexIntent("android.intent.action.MAIN", "android.intent.category.LAUNCHER");
		Object aobj[] = new Object[1];
		aobj[0] = SOURCE_MAP.get(Integer.valueOf(SOURCE_WIDGET));
		String s = String.format("searchWidget://search/?source=%s", aobj);
		intent.setData(Uri.parse((new StringBuilder()).append(s).append("&voice=true").toString()));
		ProgramsUtil.startActivitySafely(context, intent);
	}



}
