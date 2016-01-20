// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.os.Process;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.softspb.shell.Home;
import com.softspb.shell.ShellApplication;
import com.softspb.shell.adapters.AdaptersHolder;
import com.softspb.shell.adapters.AppAdapter;
import com.softspb.shell.adapters.AppAdapterAndroid;
import com.softspb.shell.adapters.ApptListAdapter;
import com.softspb.shell.adapters.ApptListAdapterAndroid;
import com.softspb.shell.adapters.BatteryAdapter;
import com.softspb.shell.adapters.BatteryAdapterAndroid;
import com.softspb.shell.adapters.BookmarksAdapter;
import com.softspb.shell.adapters.BookmarksAdapterAndroid;
import com.softspb.shell.adapters.CallLogAdapter;
import com.softspb.shell.adapters.CallLogAdapterAndroid;
import com.softspb.shell.adapters.CitiesAdapter;
import com.softspb.shell.adapters.CitiesAdapterAndroid;
import com.softspb.shell.adapters.ContactsAdapter;
import com.softspb.shell.adapters.ContactsAdapterAndroid;
import com.softspb.shell.adapters.DialogBoxAdapter;
import com.softspb.shell.adapters.DialogBoxAdapterAndroid;
import com.softspb.shell.adapters.GSensorAdapter;
import com.softspb.shell.adapters.GSensorAdapterAndroid;
import com.softspb.shell.adapters.ImageAdapter;
import com.softspb.shell.adapters.ImageAdapterAndroid;
import com.softspb.shell.adapters.MediaLibAdapter;
import com.softspb.shell.adapters.MediaLibAdapterAndroid;
import com.softspb.shell.adapters.MessagingAdapter;
import com.softspb.shell.adapters.MessagingAdapterAndroid;
import com.softspb.shell.adapters.NetworkAdapter;
import com.softspb.shell.adapters.NetworkAdapterAndroid;
import com.softspb.shell.adapters.OperatorAdapter;
import com.softspb.shell.adapters.OperatorAdapterAndroid;
import com.softspb.shell.adapters.ProgramListAdapter;
import com.softspb.shell.adapters.ShortcutAdapter;
import com.softspb.shell.adapters.ShortcutAdapterAndroid;
import com.softspb.shell.adapters.SoundProfileAdapter;
import com.softspb.shell.adapters.SoundProfileAdapterAndroid;
import com.softspb.shell.adapters.TimeAdapter;
import com.softspb.shell.adapters.TimeAdapterAndroid;
import com.softspb.shell.adapters.WeatherAdapter;
import com.softspb.shell.adapters.WeatherAdapterAndroid;
import com.softspb.shell.adapters.WirelessAdapter;
import com.softspb.shell.adapters.WirelessAdapterAndroid;
import com.softspb.shell.adapters.YandexAdapterAndroid;
import com.softspb.shell.adapters.YandexSearchAdapter;
import com.softspb.shell.adapters.alarms.AlarmsAdapter;
import com.softspb.shell.adapters.alarms.AlarmsAdapterAndroid;
import com.softspb.shell.adapters.alarms.AlarmsAdapterAndroid2;
import com.softspb.shell.adapters.backlight.BacklightAdapter;
import com.softspb.shell.adapters.backlight.BacklightAdapterAndroidApi7;
import com.softspb.shell.adapters.backlight.BacklightAdapterAndroidApi8;
import com.softspb.shell.adapters.dialog.NewDialogAdapter;
import com.softspb.shell.adapters.dialog.NewDialogAdapterAndroid;
import com.softspb.shell.adapters.dialog.ShellDatePickerDialog;
import com.softspb.shell.adapters.dialog.ShellDialog;
import com.softspb.shell.adapters.imageviewer.ImageViewerAdapter;
import com.softspb.shell.adapters.imageviewer.ImageViewerAdapterAndroid;
import com.softspb.shell.adapters.program.adapter.ProgramListAdapterAndroid2;
import com.softspb.shell.adapters.simplemedia.SimpleMediaAdapter;
import com.softspb.shell.adapters.simplemedia.SimpleMediaAdapterAndroid;
import com.softspb.shell.adapters.wallpaper.WallpaperAdapter;
import com.softspb.shell.adapters.wallpaper.WallpaperAdapterAndroid;
import com.softspb.shell.calendar.service.KillCalendarService;
import com.softspb.shell.util.DebugUtil;
import com.softspb.shell.view.WidgetController2;
import com.softspb.util.CollectionFactory;
import com.softspb.util.IOHelper;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.contacts.KillContactsService;

// Referenced classes of package com.softspb.shell.opengl:
//			NativeCalls

public class NativeCallbacks extends NativeCalls
{
	private static class NativeMenuItem
	{

		String icon;
		int id;
		String str;

		NativeMenuItem(int i, String s, String s1)
		{
			id = i;
			str = s;
			icon = s1;
		}
	}


	public static final int ANDROID_FROM = 0;
	public static final int ANDROID_TO = 1440;
	private static final long FIRST_DELAY = 0xdbba0L;
	private static final String INDEX_XML_FILENAME = "index.xml";
	private static final String KEY_SCREEN_FORMAT = "_key_screen_format";
	public static final int MAJOR_ID = 1;
	private static final ExecutorService NATIVE_EXECUTOR;
	private static final int NO_ICON = -1;
	public static final int PRODUCT_ID = 174;
	private static final long REPEAT_DELAY = 0x36ee80L;
	private static final int SCREEN_COUNT = 3;
	private static final int SCREEN_SIZE = 480;
	private static final Logger logger = Loggers.getLogger(NativeCallbacks.class.getName());
	private AdaptersHolder adaptersHolder;
	private AppAdapter appAdapter;
	private ShellApplication application;
	private final ApptListAdapter apptAdapter;
	private BacklightAdapter backlightAdapter;
	private BatteryAdapter batteryAdapter;
	private CitiesAdapter citiesAdapter;
	private ContactsAdapter contactsAdapter;
	private Home context;
	private Set delayedWidgets;
	private DialogBoxAdapter dialogBoxAdapter;
	private GSensorAdapter gSensorAdapter;
	private ImageAdapter imageAdapter;
	private ImageViewerAdapter imageViwerAdapter;
	private boolean isLoadStarted;
	final List menuItems = new ArrayList();
	private MessagingAdapter messagingAdapter;
	private NetworkAdapter networkAdapter;
	private NewDialogAdapter newDialogAdapter;
	private OperatorAdapter operatorAdapter;
	private ProgramListAdapter programListAdapter;
	private ShortcutAdapter shortcutAdapter;
	private SimpleMediaAdapter simpleMediaAdapter;
	private SoundProfileAdapter soundProfileAdapter;
	private TimeAdapter timeAdapter;
	private Vibrator vibrator;
	private View view;
	private WallpaperAdapter wallpaperAdapter;
	private WeatherAdapter weatherAdapter;
	private WidgetController2 widgetController;
	private WallpaperManager wm;
	private YandexAdapterAndroid yandexSearchAdapter;

	public NativeCallbacks(Home home, WidgetController2 widgetcontroller2, View view1)
	{
		delayedWidgets = CollectionFactory.newHashSet();
		context = home;
		application = (ShellApplication)home.getApplication();
		view = view1;
		wm = WallpaperManager.getInstance(home);
		adaptersHolder = new AdaptersHolder();
		programListAdapter = new ProgramListAdapterAndroid2(adaptersHolder);
		apptAdapter = new ApptListAdapterAndroid(adaptersHolder);
		messagingAdapter = new MessagingAdapterAndroid(adaptersHolder);
		imageAdapter = new ImageAdapterAndroid(adaptersHolder);
		gSensorAdapter = new GSensorAdapterAndroid(adaptersHolder);
		contactsAdapter = new ContactsAdapterAndroid(adaptersHolder);
		weatherAdapter = new WeatherAdapterAndroid(adaptersHolder);
		imageViwerAdapter = new ImageViewerAdapterAndroid(adaptersHolder);
		if (android.os.Build.VERSION.SDK_INT > 8)
			backlightAdapter = new BacklightAdapterAndroidApi8(adaptersHolder);
		else
			backlightAdapter = new BacklightAdapterAndroidApi7(adaptersHolder);
		soundProfileAdapter = new SoundProfileAdapterAndroid(adaptersHolder);
		batteryAdapter = new BatteryAdapterAndroid(adaptersHolder);
		timeAdapter = new TimeAdapterAndroid(adaptersHolder);
		appAdapter = new AppAdapterAndroid(adaptersHolder);
		appAdapter.create(home, this);
		operatorAdapter = new OperatorAdapterAndroid(adaptersHolder);
		dialogBoxAdapter = new DialogBoxAdapterAndroid(adaptersHolder);
		newDialogAdapter = new NewDialogAdapterAndroid(adaptersHolder);
		citiesAdapter = new CitiesAdapterAndroid(adaptersHolder);
		simpleMediaAdapter = new SimpleMediaAdapterAndroid(adaptersHolder);
		shortcutAdapter = new ShortcutAdapterAndroid(adaptersHolder);
		networkAdapter = new NetworkAdapterAndroid(adaptersHolder);
		wallpaperAdapter = new WallpaperAdapterAndroid(adaptersHolder);
		yandexSearchAdapter = new YandexAdapterAndroid(adaptersHolder);
		wallpaperAdapter.create(home, this);
		messagingAdapter.create(home, this);
		contactsAdapter.create(home, this);
		contactsAdapter.start();
		networkAdapter.create(home, this);
		shortcutAdapter.create(home, this);
		weatherAdapter.create(home, this);
		apptAdapter.create(home, this);
		imageAdapter.create(home, this);
		imageViwerAdapter.create(home, this);
		citiesAdapter.create(home, this);
		backlightAdapter.create(home, this);
		backlightAdapter.start();
		dialogBoxAdapter.create(home, this);
		dialogBoxAdapter.start();
		newDialogAdapter.create(home, this);
		timeAdapter.create(home, this);
		yandexSearchAdapter.create(home, this);
		widgetController = widgetcontroller2;
		vibrator = (Vibrator)home.getSystemService("vibrator");
	}

	private InputStream getAssetStream(String s)
	{
		InputStream inputstream;
		String s1;
		logger.d((new StringBuilder()).append(">>>NativeCallbacks.getAssetStream: '").append(s).append("'").toString());
		if (s == null) 
		{
			inputstream = null;
			return inputstream;
		}
		else 
		{
			s1 = s.toLowerCase();
			inputstream = null;
			InputStream inputstream1 = null;
			try {
				inputstream1 = context.getAssets().open(s1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			inputstream = inputstream1;
			return inputstream;
		}
	}

	private int getIconByString(String s)
	{
		int i = -1;
		if (!TextUtils.isEmpty(s))
		{
			if (s.equals("Add"))
				i = com.spb.shell3d.R.drawable.ic_menu_add;
			else
			if (s.equals("AddFolder"))
				i = com.spb.shell3d.R.drawable.ic_menu_add_folders;
			else
			if (s.equals("RenamePanel"))
				i = com.spb.shell3d.R.drawable.ic_menu_edit;
			else
			if (s.equals("DebugTools"))
				i = com.spb.shell3d.R.drawable.ic_menu_manage;
			else
			if (s.equals("About"))
				i = com.spb.shell3d.R.drawable.ic_menu_info_details;
			else
			if (s.equals("Exit"))
				i = com.spb.shell3d.R.drawable.ic_menu_close_clear_cancel;
			else
			if (s.equals("WeatherChangeCity"))
				i = com.spb.shell3d.R.drawable.ic_menu_change;
			else
			if (s.equals("SettingsWnd"))
				i = com.spb.shell3d.R.drawable.ic_menu_preferences;
			else
			if (s.equals("Update"))
				i = com.spb.shell3d.R.drawable.ic_menu_refresh;
			else
			if (s.equals("ManagePanels"))
				i = com.spb.shell3d.R.drawable.ic_shell_edit_panels;
			else
			if (s.equals("Wallpaper"))
				i = com.spb.shell3d.R.drawable.ic_menu_gallery;
			else
			if (s.equals("Uninstall"))
				i = com.spb.shell3d.R.drawable.ic_menu_clear_playlist;
			else
			if (s.equals("FreeLayout"))
				i = com.spb.shell3d.R.drawable.ic_menu_sort_by_size;
			else
			if (s.equals("ShellSettings"))
				i = com.spb.shell3d.R.drawable.ic_shell_settings;
		}
		return i;
	}

	public static String getMemoryInfo()
	{
		logger.d("getMemoryInfo() ");
		StringBuffer stringbuffer = new StringBuffer();
		long l = Debug.getNativeHeapSize();
		stringbuffer.append((new StringBuilder()).append("nhs = ").append(l).toString());
		long l1 = Debug.getNativeHeapAllocatedSize();
		stringbuffer.append((new StringBuilder()).append("nas = ").append(l1).toString());
		long l2 = Debug.getNativeHeapFreeSize();
		stringbuffer.append((new StringBuilder()).append("nfs = ").append(l2).toString());
		return stringbuffer.toString();
	}

	public static long pack(long l, long l1)
	{
		return l1 | l << 32;
	}

	public static void postToNative(Runnable runnable)
	{
		NATIVE_EXECUTOR.execute(runnable);
	}

	public static void printMemory()
	{
		logger.d("printMemory() ");
	}

	public void AddSMSListener(int i)
	{
		messagingAdapter.addListener(i);
	}

	public Bitmap CreateImage(String s)
	{
		logger.d((new StringBuilder()).append("Bitmap CreateImage ").append(s).toString());
		BookmarksAdapter bookmarksadapter = BookmarksAdapter.instance();
		Bitmap bitmap;
		if (s.startsWith("bookmark-image") && bookmarksadapter != null)
			bitmap = bookmarksadapter.getImage(s);
		else
			bitmap = imageAdapter.getByPath(s);
		return bitmap;
	}

	public Bitmap CreateImage(ByteBuffer bytebuffer)
	{
		return imageAdapter.getByBuffer(bytebuffer);
	}

	public Bitmap CreateImage(ByteBuffer bytebuffer, int i, int j)
	{
		return imageAdapter.getByBuffer(bytebuffer, i, j);
	}

	public void DeinitWeatherAdapter()
	{
		getWeatherAdapter().stop();
		return;
	}

	void EnableGSensor(boolean flag)
	{
		gSensorAdapter.EnableGSensor(flag);
	}

	public String GetAMPM(long l)
	{
		return timeAdapter.getAMPMString(l);
	}

	public int GetAndroidVersion()
	{
		return appAdapter.GetAndroidVersion();
	}

	public void GetContact(int i)
	{
		getContactsAdapter().reloadContact(i);
	}

	public int GetContactByPhone(String s)
	{
		return getContactsAdapter().getContactByPhone(s);
	}

	public Bitmap GetContactPic(String s)
	{
		Bitmap bitmap = null;
		Bitmap bitmap1 = getContactsAdapter().getContactPic(0, s);
		bitmap = bitmap1;
		return bitmap;
	}

	public int GetCount()
	{
		logger.d("GetCount()");
		return messagingAdapter.getCount();
	}

	public String GetDeviceId()
	{
		return appAdapter.getDeviceId();
	}

	public String GetDeviceModel()
	{
		return appAdapter.getDeviceModel();
	}

	public String GetKey()
	{
		return appAdapter.getKey();
	}

	public int GetLastMessageByContact(int i)
	{
		return messagingAdapter.getLastMessage(i);
	}

	public void GetMessage(int i, long l)
	{
		Logger logger1 = logger;
		Object aobj[] = new Object[1];
		aobj[0] = Integer.valueOf(i);
		logger1.d(String.format("%08x", aobj));
		messagingAdapter.getMessageById(i, l);
	}

	int GetPanelCount()
	{
		return 1;
	}

	public String GetStorageCard()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public int GetUnreadCount()
	{
		logger.d("GetUnreadCount()");
		return messagingAdapter.getUnreadCount();
	}

	public void GetVersionInfo(int i)
	{
		appAdapter.getVersionInfo(i);
	}

	public void GetWidgets(int i)
	{
		logger.d("GetWidgets!!");
		context.setWidgetToken(i);
		context.restoreWidgetsInNative();
		return;
	}

	public void HideWidgets()
	{
		widgetController.hideWidgets();
	}

	public void InitWeatherAdapter(int i)
	{
		getWeatherAdapter().start(i);
	}

	public void LoadCityInfo(int i)
	{
		citiesAdapter.loadCityInfo(i);
	}

	public void MakeCall(String s, boolean flag)
	{
		getContactsAdapter().call(s, flag);
	}

	ShellDatePickerDialog NewDatePickerDialog(int i)
	{
		return newDialogAdapter.newShellDatePickerDialog(i);
	}

	ShellDialog NewShellDialog(int i)
	{
		return newDialogAdapter.newShellDialog(i);
	}

	public void OnInboxFolderCreated(String s, int i)
	{
		messagingAdapter.onInboxFolderCreated(s, i);
	}

	public void OpenContactCard(int i, String s)
	{
		getContactsAdapter().openContactCard(i, s);
	}

	public void OpenSelectCity(int i)
	{
		getWeatherAdapter().openCitySelect(i);
	}

	public void PostRestart()
	{
		context.restartShell();
	}

	public void ReadApptList(int i, long l, long l1)
	{
		apptAdapter.getApptList(i, l, l1);
	}

	public long[] ReadMessages(int i)
	{
		logger.d("ReadMessages()");
		return messagingAdapter.getMessageList(i);
	}

	public void ReloadBirthdays(int i)
	{
		getContactsAdapter().reloadBirthdays(i);
	}

	public void ReloadContacts(int i)
	{
		getContactsAdapter().reloadContacts(i);
	}

	public void RemoveSMSListener(int i)
	{
		messagingAdapter.removeListener(i);
	}

	public boolean SaveCompressed(ByteBuffer bytebuffer, int i, int j, int k, String s)
	{
		return imageAdapter.SaveCompressed(bytebuffer, i, j, k, s);
	}

	public long SelectCityForCTA()
	{
		return citiesAdapter.openCitySelect();
	}

	public void SendEmail(String s, String s1)
	{
		logger.d((new StringBuilder()).append("SendEmail: displayName=").append(s).append(" email=").append(s1).toString());
		Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", s1, null));
		context.startActivity(intent);
	}

	public void SendSMS(String s, String s1)
	{
		logger.d((new StringBuilder()).append("SendSMS: displayName=").append(s).append(" phoneNumber=").append(s1).toString());
		Intent intent = new Intent("android.intent.action.SENDTO", Uri.fromParts("smsto", s1, null));
		context.startActivity(intent);
	}

	public void SetWeatherUpdateRate(int i)
	{
		getWeatherAdapter().setUpdateRate(i);
	}

	public void SetWeatherUseOnlyWifi(boolean flag)
	{
		getWeatherAdapter().setUseOnlyWifi(flag);
	}

	public boolean ShowWidget(int i, int j, int k)
	{
		boolean flag = false;
		boolean flag1;
		if (isLoadStarted)
		{
			Set set = delayedWidgets;
			int ai[] = new int[3];
			ai[0] = i;
			ai[1] = j;
			ai[2] = k;
			set.add(ai);
			return flag;
		}
		logger.d((new StringBuilder()).append("ShowWidget ").append(i).toString());
		flag1 = widgetController.showWidget(i, j, k);
		flag = flag1;
		return flag;
	}

	public int SoundProfileGetRingMode()
	{
		return soundProfileAdapter.getRingMode();
	}

	public int SoundProfileGetRingVolume()
	{
		return soundProfileAdapter.getRingVolume();
	}

	public void SoundProfileNotifyChange()
	{
		soundProfileAdapter.notifyChange();
	}

	public void SoundProfileOpenSettings()
	{
		soundProfileAdapter.openSettings();
	}

	public void SoundProfileSetRingMode(int i)
	{
		soundProfileAdapter.setRingMode(i);
	}

	/**
	 * @deprecated Method Start is deprecated
	 */

	public synchronized void Start()
	{	
		logger.d("Start");
		logger.d("Init");
		onShow();
		nativeInit(this);	
		return;
	}

	public void StartCitiesAdapter(int i)
	{
		citiesAdapter.start(i);
	}

	public void StartListeningCurrentLocationForCTA()
	{
		citiesAdapter.startListeningCurrentLocation();
	}

	public void StartMessagingAdapter(int i)
	{
		messagingAdapter.start(i);
	}

	void StartNewDialogAdapter(int i)
	{
		newDialogAdapter.start(i);
	}

	public void StartTimeAdapter(int i)
	{
		timeAdapter.start(i);
	}

	public synchronized void Stop()
	{
		logger.d("Stop");
		onHide();
		nativeDone(this);
		adaptersHolder.closeHeldAdapters(context);
		logger.d("<<Stop");
		return;	
	}

	public void StopCitiesAdapter()
	{
		citiesAdapter.stop();
	}

	public void StopListeningCurrentLocationForCTA()
	{
		citiesAdapter.stopListeningCurrentLocation();
	}

	public void UpdateCurrentLocation()
	{
		citiesAdapter.updateCurrentLocation();
	}

	public void WaitForShowWidgets()
	{
		if (!isLoadStarted)
			widgetController.waitForShowWidgets();
	}

	public void addMenuItem(int i, String s, String s1)
	{
		logger.d((new StringBuilder()).append("addMenuItem, id=").append(i).append(", title=").append(s).append(", icon=").append(s1).toString());
		List list = menuItems;
		synchronized (list) {
			menuItems.add(new NativeMenuItem(i, s, s1));
		}
		return;
	}

	public boolean addToFavorites(int i)
	{
		return getContactsAdapter().addToFavorites(i);
	}

	public void askEvents()
	{
		if (gSensorAdapter.isPresent())
			gSensorAdapter.askEvents();
	}

	public boolean canChangeInterfaceMode()
	{
		return appAdapter.canChangeInterfaceMode();
	}

	public boolean changeWidgetSize(int i, int j, int k)
	{
		boolean flag1 = widgetController.changeWidgetSize(i, j, k);
		boolean flag = flag1;
		return flag;
	}

	public void checkWallpaperChange()
	{
		wallpaperAdapter.checkWallpaperChange();
	}

	public AlarmsAdapter createAlarmsAdapter(int i)
	{
		Object obj;
		if (android.os.Build.VERSION.SDK_INT >= 8)
			obj = new AlarmsAdapterAndroid2(i, context);
		else
			obj = new AlarmsAdapterAndroid(i, context);
		return ((AlarmsAdapter) (obj));
	}

	public BookmarksAdapter createBookmarksAdapter(int i)
	{
		return new BookmarksAdapterAndroid(i, context);
	}

	public CallLogAdapter createCallLogAdapter(int i)
	{
		return new CallLogAdapterAndroid(i, context, this);
	}

	public MediaLibAdapter createMediaLibAdapter(int i)
	{
		return new MediaLibAdapterAndroid(i, context);
	}

	public WirelessAdapter createWirelessAdapter(int i)
	{
		return new WirelessAdapterAndroid(i, context);
	}

	public boolean deinitSimpleMediaAdapter()
	{
		logger.d("NativeCallbacks.deinitSimpleMediaAdapter");
		simpleMediaAdapter.stop();
		return true;
	}

	public void disposeContactsAdapter()
	{
		getContactsAdapter().disposeContactsAdapter();
	}

	public void doCloseLoadingDialog()
	{
		widgetController.hideWidgets();
		int ai[];
		for (Iterator iterator = delayedWidgets.iterator(); iterator.hasNext(); widgetController.showWidget(ai[0], ai[1], ai[2]))
			ai = (int[])iterator.next();

		delayedWidgets.clear();
		widgetController.waitForShowWidgets();
		context.closeLoadingDialog();
		return;
	}

	public void doShowLoadingDialog()
	{
		context.runOnUiThread(new Runnable() {

			final NativeCallbacks this$0;

			public void run()
			{
				context.showLoadingDialog();
			}

			
			{
				this$0 = NativeCallbacks.this;
//				super();
			}
		});
	}

	public void emptyMenu()
	{
		List list = menuItems;
		synchronized (list) {
			menuItems.clear();	
		}
		return;
	}

	public void enableHTCStereo3D(boolean flag)
	{
		appAdapter.enableHTCStereo3D(flag);
	}

	public void exit()
	{
		logger.d(">>Exit");
		if (!DebugUtil.isMonkey())
			context.runOnUiThread(new Runnable() {

				final NativeCallbacks this$0;

				public void run()
				{
					NativeCallbacks.logger.d("Finish!!!");
					context.finish();
				}

			
			{
				this$0 = NativeCallbacks.this;
//				super();
			}
			});
		else
			logger.d("NativeCallbacks.exit ignored - user is a monkey");
		logger.d("<<Exit");
	}

	public boolean fillMenu(Menu menu)
	{
		boolean flag = false;
		List list = menuItems;
		
		synchronized (list) {
			
			if (menuItems.size() == 0)
				return flag;
			Iterator iterator = menuItems.iterator();
			do
			{
				if (!iterator.hasNext())
					break;
				NativeMenuItem nativemenuitem = (NativeMenuItem)iterator.next();
				MenuItem menuitem = menu.add(0, nativemenuitem.id, 0, nativemenuitem.str);
				int i = getIconByString(nativemenuitem.icon);
				if (i != -1)
					menuitem.setIcon(i);
			} while (true);
			flag = true;
		}
		return flag;
	}

	public void findByTag(String s)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.findByTag: tag=").append(s).toString());
		programListAdapter.findByTag(s);
	}

	public void forceUpdateWeather(int i)
	{
		getWeatherAdapter().forceUpdate(i);
	}

	public String formatDateTime(long l, int i)
	{
		String s1 = timeAdapter.format(l, i);
		String s = s1;
		return s;
	}

	public byte[] getAsset(String s)
	{
		byte abyte0[];
		InputStream inputstream;
		logger.d((new StringBuilder()).append("getAsset, path:'").append(s).append("'").toString());
		abyte0 = null;
		inputstream = null;
		inputstream = getAssetStream(s);
		if (inputstream == null)
		{
			logger.d((new StringBuilder()).append("Asset not found: ").append(s).toString());
			IOHelper.closeSilent(inputstream);
			
		}
		else 
		{
			logger.d((new StringBuilder()).append("Reading asset: '").append(s).append("'").toString());
			try {
				abyte0 = new byte[inputstream.available()];
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				inputstream.read(abyte0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IOHelper.closeSilent(inputstream);
		}
		return abyte0;
	}

	public int getAssetSize(String s)
	{
		InputStream inputstream;
		int i;
		inputstream = null;
		i = 0;
		int j = 0;
		inputstream = getAssetStream(s);
		if (inputstream == null)
			return i;
		try {
			j = inputstream.available();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		i = j;
		IOHelper.closeSilent(inputstream);
		return i;
	}

	public int getBacklightLevel()
	{
		logger.d("NativeCallbacks.getBacklightLevel");
		return backlightAdapter.getLevel();
	}

	public int getBacklightMaxLevelsCount()
	{
		logger.d("NativeCallbacks.getBacklightMaxLevelsCount");
		return backlightAdapter.getMaxLevelsCount();
	}

	public Bitmap getBitmapFromResourceDrawable(String s)
	{
		return imageAdapter.getByPath(s);
	}

	public String getCacheDir()
	{
		return context.getCacheDir().getPath();
	}

	public CitiesAdapter getCitiesAdapter()
	{
		return citiesAdapter;
	}

	public ContactsAdapter getContactsAdapter()
	{
		return contactsAdapter;
	}

	public String getCountryISOCode()
	{
		String s = Locale.getDefault().getCountry();
		logger.d((new StringBuilder()).append("getCountryISOCode: lang=").append(s).toString());
		return s;
	}

	public long getCurrentTimeUTC()
	{
		long l = System.currentTimeMillis();
		return l - (long)TimeZone.getDefault().getOffset(l);
	}

	public String getDataDir()
	{
		return context.getApplicationInfo().dataDir;
	}

	public DialogBoxAdapter getDialogBoxAdapter()
	{
		return dialogBoxAdapter;
	}

	public int getFirstDayOfWeek()
	{
		return timeAdapter.getFirstDayOfWeek();
	}

	public void getFolderIds(int i)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.getFolderIds: ").append(i).toString());
		imageViwerAdapter.getFolderIds(i);
	}

	public Bitmap getImage(String s)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.getImage: imageUri=").append(s).toString());
		BookmarksAdapter bookmarksadapter = BookmarksAdapter.instance();
		Bitmap bitmap;
		if (s.startsWith("content://"))
			bitmap = imageAdapter.getByPath(s);
		else
		if (s.startsWith("bookmark-image") && bookmarksadapter != null)
			bitmap = bookmarksadapter.getImage(s);
		else
			bitmap = imageViwerAdapter.getImage(s);
		return bitmap;
	}

	public ImageAdapter getImageAdapter()
	{
		return imageAdapter;
	}

	public void getImageList(int i, String s)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.getImageList: ").append(i).append(", ").append(s).toString());
		imageViwerAdapter.getImageList(i, s);
	}

	public String getLanguageISOCode()
	{
		String s = Locale.getDefault().getLanguage();
		logger.d((new StringBuilder()).append("getLanguageISOCode: lang=").append(s).toString());
		return s;
	}

	void getLock()
	{
		widgetController.getLock();
	}

	public NewDialogAdapter getNewDialogAdapter()
	{
		return newDialogAdapter;
	}

	public ProgramListAdapter getProgramListAdapter()
	{
		logger.d("ProgramListAdapter getProgramListAdapter()");
		return programListAdapter;
	}

	public boolean getScreenMode()
	{
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("_key_screen_format", false);
	}

	public ShortcutAdapter getShortcutAdapter()
	{
		return shortcutAdapter;
	}

	public SimpleMediaAdapter getSimpleMediaAdapter()
	{
		return simpleMediaAdapter;
	}

	public String getSourceDir()
	{
		return context.getApplicationInfo().sourceDir;
	}

	public int getSystemDpi()
	{
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return (int)(160F * displaymetrics.density);
	}

	public boolean getUseLiveWallpaperPreference()
	{
		return isLiveWallpaper();
	}

	public Bitmap getWallpaper()
	{
		return wallpaperAdapter.getWallpaper();
	}

	public String getWallpaperSkin()
	{
		return wallpaperAdapter.getWallpaperSkin();
	}

	public WeatherAdapter getWeatherAdapter()
	{
		return weatherAdapter;
	}

	public Bitmap getWidgetIcon(int i)
	{
		Uri uri = context.getIconUri(i);
		Bitmap bitmap = null;
		if (uri != null) 
		{
			Bitmap bitmap1 = imageAdapter.getByPath(uri.toString());
			bitmap = bitmap1;
		}
		else 
		{
			bitmap = BitmapFactory.decodeResource(context.getResources(), com.spb.shell3d.R.drawable.icon);
		}
		return bitmap;
	}

	public String getWidgetName(int i)
	{
		return context.getWidgetName(i);
	}

	public Bitmap getWidgetScreenshot(int i)
	{
		Bitmap bitmap = null;
		Bitmap bitmap1 = widgetController.getWidgetScreenShot(i);
		bitmap = bitmap1;
		return bitmap;
	}

	public YandexSearchAdapter getYandexSearchAdapter()
	{
		return yandexSearchAdapter;
	}

	public boolean hasKeyboard()
	{
		return appAdapter.hasKeyboard();
	}

	public boolean hasMenuKey()
	{
		boolean flag;
		flag = false;
		DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
		boolean flag1;
		if ((float)displaymetrics.widthPixels >= 600F * displaymetrics.density)
			flag1 = true;
		else
			flag1 = false;
		
		if ((!flag1 || android.os.Build.VERSION.SDK_INT < 14) && (android.os.Build.VERSION.SDK_INT < 11 || android.os.Build.VERSION.SDK_INT >= 14))
			flag = true;
		return flag;
	}

	public boolean hasTelephony()
	{
		return appAdapter.hasTelephony();
	}

	public void hideMenu()
	{
		context.runOnUiThread(new Runnable() {

			final NativeCallbacks this$0;

			public void run()
			{
				context.closeOptionsMenu();
			}

			
			{
				this$0 = NativeCallbacks.this;
//				super();
			}
		});
	}

	public void init()
	{
		logger.d(">>>NativeCallbacks.init");
		logger.d("NativeCallbacks.init1");
		programListAdapter.create(context, this);
		programListAdapter.start();
		logger.d("NativeCallbacks.init2");
		logger.d("NativeCallbacks.init3");
		logger.d("NativeCallbacks.init4");
		logger.d("NativeCallbacks.init5");
		soundProfileAdapter.create(context, this);
		logger.d("NativeCallbacks.init6");
		simpleMediaAdapter.create(context, this);
		logger.d("NativeCallbacks.init7");
		logger.d("NativeCallbacks.init8");
		batteryAdapter.create(context, this);
		operatorAdapter.create(context, this);
		logger.d("<<<NativeCallbacks.init");
	}

	public void initContactsAdapter(int i)
	{
		getContactsAdapter().initContactsAdapter(i);
	}

	public boolean initSimpleMediaAdapter(int i)
	{
		logger.d(">>>NativeCallbacks.initSimpleMediaAdapter");
		simpleMediaAdapter.start(i);
		logger.d("<<<NativeCallbacks.initSimpleMediaAdapter");
		return true;
	}

	public boolean isBacklightAutoSupported()
	{
		logger.d("NativeCallbacks.isBacklightAutoBupported");
		return backlightAdapter.isAutolevelSupported();
	}

	public boolean isBacklightSupported()
	{
		logger.d("NativeCallbacks.isBacklightBupported");
		return backlightAdapter.isSupported();
	}

	public boolean isLiveWallpaper()
	{
		return wallpaperAdapter.isLiveWallpaper();
	}

	public boolean isLoadStarted()
	{
		return isLoadStarted;
	}

	public boolean isMenuEmpty()
	{
		boolean flag ;
		List list = menuItems;
		synchronized (list) {
			flag = menuItems.isEmpty();
		}
		return flag;
	}

	public boolean isMipmapEnabled()
	{
		return appAdapter.isMipmapEnabled();
	}

	public boolean isVoiceSearchAvailable()
	{
		return yandexSearchAdapter.isVoiceSearchAvailable();
	}

	public boolean isYandexCountry()
	{
		return yandexSearchAdapter.isYandexCountry();
	}

	public void kill()
	{
		Process.killProcess(Process.myPid());
	}

	public void killCalendar()
	{
		logger.d("ApptListAdapter.killCalendar");
		Intent intent = new Intent(context, KillCalendarService.class);
		context.startService(intent);
	}

	public void killContacts()
	{
		Intent intent = new Intent(context, KillContactsService.class);
		context.startService(intent);
	}

	public boolean launch(String s)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.launch: tag=").append(s).toString());
		return programListAdapter.launch(s);
	}

	public boolean launch(String s, String s1)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.launch: packageName=").append(s).append(" activityClassName=").append(s1).toString());
		return programListAdapter.launch(s, s1);
	}

	public void launchShortcut(int i, int j, int k, int l, int i1)
	{
		shortcutAdapter.launch(i, j, k, l, i1);
	}

	public void listShortcuts(int i)
	{
		shortcutAdapter.start(i);
	}

	public void loadCityName(int i)
	{
		getWeatherAdapter().loadCityName(i);
	}

	public void loadConditions(int i)
	{
		getWeatherAdapter().loadConditions(i);
	}

	public void loadForecast(int i)
	{
		getWeatherAdapter().loadForecast(i);
	}

	public void loadUpdateTime(int i)
	{
		getWeatherAdapter().loadUpdateStatus(i);
	}

	public void moveBackground(float f)
	{
		wm.setWallpaperOffsets(view.getWindowToken(), f, 0.0F);
	}

	public void notifyListener(int i)
	{
	}

	public void onHide()
	{
		gSensorAdapter.stop();
	}

	public void onHomeLoadStarted()
	{
		isLoadStarted = true;
	}

	public void onHomeLoaded()
	{
		logger.d((new StringBuilder()).append("onHomeLoaded() ").append(isLoadStarted).toString());
		try
		{
			isLoadStarted = false;
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		application.startLicensingProcess(context);
	}

	public int onIdle()
	{
		int i = widgetController.onIdle();
		logger.d((new StringBuilder()).append("widgetS onIdle = ").append(i).toString());
		return i;
	}

	public void onPositionChanged(int i)
	{
	}

	public void onShow()
	{
		gSensorAdapter.create(context, this);
		gSensorAdapter.start();
		return;
	}

	public void onUnhandledTouch(final int x, final int y)
	{
		context.runOnUiThread(new Runnable() {

			final NativeCallbacks this$0;
			final int val$x;
			final int val$y;

			public void run()
			{
				context.sendEventToLiveWallpaper(x, y);
			}

			
			{
				this$0 = NativeCallbacks.this;
				val$x = x ;
				val$y = y ;
//				super();
			}
		});
	}

	public void onWeatherProviderDeleted(int i)
	{
		getWeatherAdapter().onWeatherProviderDeleted(i);
	}

	public void openAppt(long l, long l1, long l2)
	{
		apptAdapter.openAppt(l, l1, l2);
	}

	public void openBrowser(String s)
	{
		networkAdapter.openBrowser(s);
	}

	public void openCalendar(long l)
	{
		logger.d("NativeCallbacks.openCalendar");
		apptAdapter.openCalendar(l);
	}

	public boolean openCreateApptDialog(long l)
	{
		logger.d("NativeCallbacks.openCreateApptDialog");
		return apptAdapter.openCreateApptDialog(l);
	}

	public void openImage(String s)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.openImage: ").append(s).toString());
		imageViwerAdapter.openImage(s);
	}

	public void openMessageThread(long l)
	{
		messagingAdapter.openMessageThread(l);
	}

	public void openSetWallpaperDialog()
	{
		context.runOnUiThread(new Runnable() {

			final NativeCallbacks this$0;

			public void run()
			{
				wallpaperAdapter.openSetWallPaperDialog();
			}

			
			{
				this$0 = NativeCallbacks.this;
//				super();
			}
		});
	}

	public void openSmsMmsActivity()
	{
		messagingAdapter.openSmsMmsActivity();
	}

	public int prepareNewWidget(String s)
	{
		int j;
		StringTokenizer stringtokenizer = new StringTokenizer(s, "/");
		String s1 = stringtokenizer.nextToken();
		String s2 = stringtokenizer.nextToken();
		j = context.launcherWidgetInit(s1, s2);
		int i = j;
		return i;
	}

	void releaseLock()
	{
		widgetController.releaseLock();
	}

	public void reloadWallpaper()
	{
		wallpaperAdapter.reloadWallpaper();
	}

	public boolean removeFromFavorites(int i)
	{
		return getContactsAdapter().removeFromFavorites(i);
	}

	public void removeShortcut(int i)
	{
		shortcutAdapter.remove(i);
	}

	public void selectCity(int i, int j)
	{
		logger.d((new StringBuilder()).append("selectCity: weatherProviderToken=0x").append(Integer.toHexString(i)).append(" cityId=").append(j).toString());
		getWeatherAdapter().selectCity(i, j);
	}

	public void setBacklightLevel(int i)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.setBacklightLevel, level=").append(i).toString());
		backlightAdapter.setLevel(i);
	}

	public void setGlRenderer(String s)
	{
	}

	public void setOrientation(int i)
	{
		appAdapter.setOrientation(i);
	}

	public void setUseLiveWallpaperPreference(boolean flag)
	{
		WallpaperAdapter.setUseLiveWallpapers(context, flag);
		if (WallpaperAdapter.isLiveWallpaper(context))
			context.restartShell();
	}

	public void setWallpaperFromFile(String s)
	{
		wallpaperAdapter.setWallpaperFromFile(s);

		return;

	}

	public void showAddToFavoritesDialog()
	{
		getContactsAdapter().showAddToFavoritesDialog();
	}

	public boolean showContactPicker(int i)
	{
		getContactsAdapter().showPickContactDialog(i);
		return true;
	}

	public void showLegalNoticeDialog()
	{
		context.runOnUiThread(new Runnable() {

			final NativeCallbacks this$0;

			public void run()
			{
				WebView webview = new WebView(context);
				webview.loadUrl("file:///android_asset/files/licenses/copyright.txt");
				webview.getSettings().setDefaultTextEncodingName("UTF-8");
				webview.getSettings().setUseWideViewPort(true);
				webview.getSettings().setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.NORMAL);
				(new android.app.AlertDialog.Builder(context)).setPositiveButton(0x104000a, null).setView(webview).setIcon(0x108009b).setTitle(com.spb.shell3d.R.string.legal_notices_title).create().show();
			}

			
			{
				this$0 = NativeCallbacks.this;
//				super();
			}
		});
	}

	public void showMenu(final int anchorX, final int anchorY)
	{
		System.out.println((new StringBuilder()).append("showMenu ").append(anchorX).append(" ").append(anchorY).toString());
		context.runOnUiThread(new Runnable() {

			final NativeCallbacks this$0;
			final int val$anchorX;
			final int val$anchorY;

			public void run()
			{
				context.getMenuController().openMenuFromNative(anchorX, anchorY);
			}

			
			{
				this$0 = NativeCallbacks.this;
				val$anchorX = anchorX ;
				val$anchorY = anchorY ;
//				super();
			}
		});
	}

	public void showMessage(final String title, final String message)
	{
		context.runOnUiThread(new Runnable() {

			final NativeCallbacks this$0;
			final String val$message;
			final String val$title;

			public void run()
			{
				(new android.app.AlertDialog.Builder(context)).setMessage(message).setTitle(title).setPositiveButton(0x104000a, null).create().show();
			}

			
			{
				this$0 = NativeCallbacks.this;
				val$message = message;
				val$title = title ;
//				super();
			}
		});
	}

	void showPopupMessage(String s)
	{
		newDialogAdapter.showPopupMessage(s);
	}

	public void startSystemSearch()
	{
		context.startSearch(null, false, null, true);
	}

	public void startVoiceSearch()
	{
		yandexSearchAdapter.startVoiceSearch();
	}

	public void startYandexSearch(int i)
	{
		yandexSearchAdapter.startSearch(i);
	}

	public void switchScreen()
	{
		boolean flag = false;
		SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (!sharedpreferences.getBoolean("_key_screen_format", false))
			flag = true;
		final String formatName;
		if (flag)
			formatName = "8888";
		else
			formatName = "default";
		context.runOnUiThread(new Runnable() {

			final NativeCallbacks this$0;
			final String val$formatName;

			public void run()
			{
				Home home = context;
				Object aobj[] = new Object[1];
				aobj[0] = formatName;
				Toast.makeText(home, String.format("Shell will be restarted with %s screen format", aobj), 1).show();
			}

			
			{
				this$0 = NativeCallbacks.this;
				val$formatName = formatName ;
//				super();
			}
		});
		sharedpreferences.edit().putBoolean("_key_screen_format", flag).commit();
		widgetController.postDelayed(new Runnable() {

			final NativeCallbacks this$0;

			public void run()
			{
				context.restartShell();
			}

			
			{
				this$0 = NativeCallbacks.this;
//				super();
			}
		}, 5000L);
	}

	public void uninstall(String s)
	{
		logger.i((new StringBuilder()).append("uninstall package: ").append(s).toString());
		programListAdapter.uninstall(s);
	}

	public boolean uninstallProgram(String s)
	{
		logger.d((new StringBuilder()).append("NativeCallbacks.uninstallProgram: packageName=").append(s).toString());
		programListAdapter.uninstall(s);
		return true;
	}

	public void vibrate(long l)
	{
		vibrator.vibrate(l);
		logger.d((new StringBuilder()).append("OOps, I vibrated for ").append(l).append(" ms.").toString());
	}

	public void vibrateKeyPress()
	{
		view.performHapticFeedback(1);
	}

	public void vibrateLongPress()
	{
		view.performHapticFeedback(0);
	}

	public void widgetAdd(int i)
	{
		context.showAddDialog(i);
	}

	public void widgetDelete(int i)
	{
		logger.d((new StringBuilder()).append("widgetDelete").append(i).toString());
		context.widgetDelete(i);
	}

	public boolean widgetDrop(int i, int j, int k, int l)
	{
		return false;
	}

	static 
	{
		NATIVE_EXECUTOR = new ThreadPoolExecutor(1, 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue());
	}



}
