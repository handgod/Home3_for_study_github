// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.pick;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.*;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import com.spb.cities.location.CurrentLocationPreferences;
import com.spb.cities.location.LocationClient;
import java.lang.ref.WeakReference;

// Referenced classes of package com.spb.cities.pick:
//			SPBAutoCompleteTextView, CitiesAutoCompleteAdapter, NearestAutoCompletionAdapter

public class CitySelectionActivity extends Activity
	implements android.widget.AdapterView.OnItemClickListener, android.content.DialogInterface.OnCancelListener
{
	class UIHandler extends Handler
		implements android.content.DialogInterface.OnCancelListener
	{

		private static final int MSG_HIDE_PROGRESS_DIALOG = 2;
		private static final int MSG_SHOW_NEAREST_CITIES = 4;
		private static final int MSG_SHOW_PROGRESS_DIALOG = 1;
		private static final int MSG_SHOW_TOAST = 3;
		private Dialog dialog;
		final CitySelectionActivity this$0;

		private void hideProgressDialog()
		{
			if (dialog != null)
			{
				dialog.dismiss();
				dialog = null;
			}
		}

		private void showNearestCities(com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem aresponseitem[], int i)
		{
			CitySelectionActivity cityselectionactivity = CitySelectionActivity.this;
			boolean flag;
			NearestAutoCompletionAdapter nearestautocompletionadapter;
			if (i < 20)
				flag = true;
			else
				flag = false;
			nearestautocompletionadapter = new NearestAutoCompletionAdapter(cityselectionactivity, aresponseitem, 3, flag);
			cityNameInput.setAdapter(nearestautocompletionadapter);
			cityNameInput.showDropDown();
			showingNearestCities = true;
		}

		private void showProgressDialog()
		{
			hideProgressDialog();
			dialog = ProgressDialog.show(CitySelectionActivity.this, getString(com.spb.cities.R.string.searching_city_title), getString(com.spb.cities.R.string.searching_city_progress), true, true, this);
		}

		private void showToast(int i)
		{
			Toast.makeText(CitySelectionActivity.this, i, 1).show();
		}

		private void showToast(String s)
		{
			Toast.makeText(CitySelectionActivity.this, s, 1).show();
		}

		void finish()
		{
			hideProgressDialog();
		}

		public void handleMessage(Message message)
		{
			switch (message.what) {
			case 1:
				showProgressDialog();
				break;
			case 2:
				hideProgressDialog();			
				break;
			case 3:
				if (message.obj != null)
					showToast((String)message.obj);
				else
					showToast(message.arg1);
				break;
			case 4:
				showNearestCities((com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem[])(com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem[])message.obj, message.arg1);
				break;
			default:
				break;
			}
			return;
		}

		public void onCancel(DialogInterface dialoginterface)
		{
			locClient.abort();
			hideProgressDialog();
			onFinishedNearestCityLocation(0);
		}

		void postHideProgressDialog()
		{
			if (!hasMessages(2))
			{
				removeMessages(1);
				sendMessage(Message.obtain(this, 2));
			}
		}

		void postShowNearestCities(com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem aresponseitem[], int i)
		{
			Message message = Message.obtain(this, 4, aresponseitem);
			message.arg1 = i;
			sendMessage(message);
		}

		void postShowProgressDialog()
		{
			if (!hasMessages(1))
			{
				removeMessages(2);
				sendMessage(Message.obtain(this, 1));
			}
		}

		void postShowToast(int i)
		{
			Message message = Message.obtain(this, 3);
			message.arg1 = i;
			sendMessage(message);
		}

		void postShowToast(String s)
		{
			sendMessage(Message.obtain(this, 3, s));
		}

		UIHandler()
		{
			super();
			this$0 = CitySelectionActivity.this;
		}
	}

	class DataHandler extends Handler
	{

		private static final int MSG_QUERY_NEAREST_CITIES = 1;
		final CitySelectionActivity this$0;

		private void onQueryNearestCitiesResult(Location location, Cursor cursor, int i)
		{
			if (!locationNearestCitiesInProgress)
				return;
			onFinishedNearestCityLocation(-1);
			if (location != null && cursor != null)
				return;
			if (uiHandler != null)
				uiHandler.postShowToast(com.spb.cities.R.string.city_location_failed);
			Exception exception;
			com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem aresponseitem[];
			int j;
			CurrentLocationPreferences currentlocationpreferences;
			if (cursor != null)
				try
				{
					cursor.close();
				}
				catch (Exception exception3) { }
			if (uiHandler != null)
				uiHandler.postHideProgressDialog();
			else
			{
				return;
			}
			aresponseitem = new com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem[cursor.getCount()];
			cursor.moveToFirst();
			for (j = 0; !cursor.isAfterLast(); j++)
			{
				aresponseitem[j] = new com.spb.cities.nearestcity.NearestCitiesClient.ResponseItem(cursor.getInt(1), cursor.getString(2));
				cursor.moveToNext();
			}

			if (aresponseitem.length > 0)
			{
				currentlocationpreferences = new CurrentLocationPreferences(CitySelectionActivity.this);
				currentlocationpreferences.setLastKnownLocation(location, aresponseitem[0].getCityId());
				currentlocationpreferences.dispose();
			}
			if (!isFinishing() && uiHandler != null)
				uiHandler.postShowNearestCities(aresponseitem, i);
			if (cursor != null)
				try
				{
					cursor.close();
				}
				catch (Exception exception2) { }
			if (uiHandler != null)
				uiHandler.postHideProgressDialog();
			
		}

		private void queryNearestCities(int i)
		{
			Location location = locClient.obtainLocation();
			CitySelectionActivity.logger.d((new StringBuilder()).append("obtained location: ").append(location).toString());
			if (location == null)
			{
				onQueryNearestCitiesResult(null, null, i);
			} else
			{
				com.spb.cities.nearestcity.NearestCitiesClient.QueryParams queryparams = new com.spb.cities.nearestcity.NearestCitiesClient.QueryParams(location, i);
				Uri uri = com.spb.cities.provider.CitiesContract.Cities.buildNearestQueryUri(CitySelectionActivity.this, queryparams.getLat(), queryparams.getLon(), queryparams.getLimit());
				CitySelectionActivity.logger.d((new StringBuilder()).append("Querying uri=").append(uri.toString()).toString());
				Cursor cursor = getContentResolver().query(uri, com.spb.cities.provider.CitiesContract.Cities.NEAREST_PROJECTION, null, null, null);
				int j;
				if (cursor == null)
					j = 0;
				else
					j = cursor.getCount();
				CitySelectionActivity.logger.d((new StringBuilder()).append("Nearest cities count: ").append(j).toString());
				onQueryNearestCitiesResult(location, cursor, i);
			}
		}

		void finish()
		{
		}

		public void handleMessage(Message message)
		{
			switch (message.what) {
			case 1:
				queryNearestCities(message.arg1);
				break;
			
			default:
				super.handleMessage(message);
				break;
			}
		}

		void postQueryNearestCities(int i)
		{
			Message message = Message.obtain(this, 1);
			message.arg1 = i;
			sendMessage(message);
		}

		public DataHandler(Looper looper)
		{
			super(looper);
			this$0 = CitySelectionActivity.this;
		}
	}

	private static class OnInputBackKeyListener
		implements SPBAutoCompleteTextView.OnBackKeyListener
	{

		WeakReference ref;

		public void onBackKey()
		{
			CitySelectionActivity cityselectionactivity = (CitySelectionActivity)ref.get();
			if (cityselectionactivity != null && !cityselectionactivity.isFinishing())
				cityselectionactivity.onInputBackKey();
		}

		public OnInputBackKeyListener(CitySelectionActivity cityselectionactivity)
		{
			ref = new WeakReference(cityselectionactivity);
		}
	}


	private static final int AUTOCOMPLETION_THRESHOLD = 3;
	public static final boolean DO_BROADCAST_RESULT_DEFAULT = false;
	public static final String INTENT_PARAM_DO_BROADCAST_RESULT = "do-broadcast-result";
	public static final String INTENT_PARAM_WEATHER_ADAPTER_TOKEN = "weather-adapter-token";
	public static final String INTENT_PARAM_WEATHER_PROVIDER_TOKEN = "weather-provider-token";
	private static final int MAX_NEAREST_CITY_COUNT = 20;
	public static final String SELECT_CITY_RESULT_ACTION = "com.softspb.toshiba.weather.action.SELECT_CITY_RESULT";
	private static final Logger logger = Loggers.getLogger(CitySelectionActivity.class.getName());
	CitiesAutoCompleteAdapter allCitiesAdapter;
	SPBAutoCompleteTextView cityNameInput;
	DataHandler dataHandler;
	private boolean doBroadcastResult;
	Intent incomingIntent;
	private boolean isShowingDialog;
	LocationClient locClient;
	private boolean locationNearestCitiesInProgress;
	private long locationNearestOperationId;
	private int nearestCityCount;
	private final Runnable postShowDropDown = new Runnable() {

		final CitySelectionActivity this$0;

		public void run()
		{
			cityNameInput.showDropDown();
		}

			
			{
//				super();
				this$0 = CitySelectionActivity.this;
			}
	};
	private boolean showingNearestCities;
	UIHandler uiHandler;

	public CitySelectionActivity()
	{
		nearestCityCount = 5;
		locationNearestCitiesInProgress = false;
		locationNearestOperationId = -1L;
		isShowingDialog = false;
	}

	private void forceShowOnscreenKbd()
	{
		logger.d("Posting show soft input...");
		cityNameInput.post(new Runnable() {

			final CitySelectionActivity this$0;

			public void run()
			{
				boolean flag = ((InputMethodManager)getSystemService("input_method")).showSoftInput(cityNameInput, 0);
				CitySelectionActivity.logger.d((new StringBuilder()).append("showSoftInput returned ").append(flag).toString());
			}

			
			{
//				super();
				this$0 = CitySelectionActivity.this;
			}
		});
	}

	private void onCitySelected(int i, String s)
	{
		logger.d((new StringBuilder()).append("onCitySelected: cityId=").append(i).append(" cityName=").append(s).toString());
		Intent intent = new Intent();
		intent.putExtra("city_id", i);
		intent.putExtra("city_name", s);
		intent.putExtras(incomingIntent);
		setResult(-1, intent);
		onResult(i, s);
		finish();
	}

	private void onCurrentLocationSelected()
	{
		onCitySelected(-1024, null);
		cityNameInput.setText(cityNameInput.getFilterText());
	}

	private void onEnableLocationSelected()
	{
		startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
		cityNameInput.setText(cityNameInput.getFilterText());
	}

	private void onFinishedNearestCityLocation(int i)
	{
		locationNearestCitiesInProgress = false;
	}

	private void onLocateNearestSelected(int i)
	{
		logger.d("onMyLocationSelected");
		cityNameInput.setText("");
		locationNearestCitiesInProgress = true;
		if (uiHandler != null)
			uiHandler.postShowProgressDialog();
		if (dataHandler != null)
			dataHandler.postQueryNearestCities(i);
	}

	private void onNothingSelected()
	{
		logger.d("onNothingSelected");
		setResult(0, new Intent());
		onResult(0x80000000, null);
		finish();
	}

	private void onResult(int i, String s)
	{
		if (doBroadcastResult)
		{
			logger.d("broadcasting result...");
			Intent intent = new Intent("com.softspb.toshiba.weather.action.SELECT_CITY_RESULT");
			intent.putExtra("city_id", i);
			if (s != null)
				intent.putExtra("city_name", s);
			intent.putExtras(incomingIntent);
			sendBroadcast(intent);
		} else
		{
			logger.d("NOT broadcasting result...");
		}
	}

	private void showAllCities()
	{
		logger.d("showAllCities");
		showingNearestCities = false;
		boolean flag = locClient.isLocationPossible();
		if (allCitiesAdapter != null)
		{
			allCitiesAdapter.setLocationPossible(flag);
			cityNameInput.setAdapter(allCitiesAdapter);
			cityNameInput.showDropDown();
			allCitiesAdapter.getFilter().filter(cityNameInput.getText());
		} else
		{
			allCitiesAdapter = new CitiesAutoCompleteAdapter(this, 3, com.spb.cities.provider.CitiesContract.Cities.getContentUri(this), com.spb.cities.provider.CitiesContract.Cities.DEFAULT_PROJECTION, "city_name", flag);
			cityNameInput.setAdapter(allCitiesAdapter);
		}
	}

	public void onBackPressed()
	{
		logger.d("onBackPressed");
		super.onBackPressed();
	}

	public void onCancel(DialogInterface dialoginterface)
	{
	}

	protected void onCreate(Bundle bundle)
	{
		logger.d("onCreate >>>");
		super.onCreate(bundle);
		setContentView(com.spb.cities.R.layout.weather_city_selection);
		cityNameInput = (SPBAutoCompleteTextView)findViewById(com.spb.cities.R.id.weather_city_name_input);
		cityNameInput.setThreshold(0);
		cityNameInput.setOnBackKeyListener(new OnInputBackKeyListener(this));
		locClient = new LocationClient(this);
		Intent intent = getIntent();
		incomingIntent = intent;
		doBroadcastResult = intent.getBooleanExtra("do-broadcast-result", false);
		logger.d((new StringBuilder()).append("doBroadcastResult=").append(doBroadcastResult).toString());
		cityNameInput.setOnItemClickListener(this);
		cityNameInput.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

			final CitySelectionActivity this$0;

			public void onFocusChange(View view, boolean flag)
			{
				CitySelectionActivity.logger.d((new StringBuilder()).append("onFocusChange: hasFocus=").append(flag).append(" isShowingDialog").toString());
				if (!isShowingDialog && hasWindowFocus())
					cityNameInput.showDropDown();
			}

			
			{
//				super();
				this$0 = CitySelectionActivity.this;
			}
		});
		cityNameInput.setOnTouchListener(new android.view.View.OnTouchListener() {

			final CitySelectionActivity this$0;

			public boolean onTouch(View view, MotionEvent motionevent)
			{
				CitySelectionActivity.logger.d((new StringBuilder()).append("onTouch: hasFocus=").append(cityNameInput.hasFocus()).toString());
				if (cityNameInput.hasFocus())
					cityNameInput.showDropDown();
				return false;
			}

			
			{
				this$0 = CitySelectionActivity.this;
//				super();
			}
		});
		HandlerThread handlerthread = new HandlerThread("CitySelection-Data");
		handlerthread.start();
		dataHandler = new DataHandler(handlerthread.getLooper());
		uiHandler = new UIHandler();
		logger.d("onCreate <<<");
	}

	protected void onDestroy()
	{
		logger.d("onDestroy >>>");
		locClient.dispose();
		dataHandler.getLooper().quit();
		dataHandler.finish();
		dataHandler = null;
		uiHandler.finish();
		uiHandler = null;
		super.onDestroy();
		logger.d("onDestroy <<<");
	}

	void onInputBackKey()
	{
		if (showingNearestCities)
			showAllCities();
		else
			onNothingSelected();
	}

	public void onItemClick(AdapterView adapterview, View view, int i, long l)
	{
		if (l == 0x3b9c5104L)
			onLocateNearestSelected(nearestCityCount);
		else
		if (l == 0x3b9c5168L)
			onCurrentLocationSelected();
		else
		if (l == 0x3b9c51ccL)
			onEnableLocationSelected();
		else
		if (l == 0x7738a208L)
		{
			nearestCityCount = Math.min(2 * nearestCityCount, 20);
			onLocateNearestSelected(nearestCityCount);
		} else
		{
			Cursor cursor = (Cursor)cityNameInput.getAdapter().getItem(i);
			onCitySelected(cursor.getInt(1), cursor.getString(2));
		}
	}

	public boolean onKeyDown(int i, KeyEvent keyevent)
	{
		logger.d((new StringBuilder()).append("onKeyDown: keyCode=").append(i).append(" event=").append(keyevent).toString());
		return super.onKeyDown(i, keyevent);
	}

	public boolean onKeyUp(int i, KeyEvent keyevent)
	{
		logger.d((new StringBuilder()).append("onKeyDown: keyCode=").append(i).append(" event=").append(keyevent).toString());
		return super.onKeyUp(i, keyevent);
	}

	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		doBroadcastResult = intent.getBooleanExtra("do-broadcast-result", false);
		incomingIntent = intent;
	}

	protected void onPause()
	{
		logger.d("onPause >>>");
		super.onPause();
		((InputMethodManager)getSystemService("input_method")).hideSoftInputFromWindow(cityNameInput.getWindowToken(), 0);
		logger.d("onPause <<<");
	}

	protected void onResume()
	{
		logger.d("onResume >>>");
		super.onResume();
		showAllCities();
		logger.d("onResume <<<");
	}







/*
	static boolean access$402(CitySelectionActivity cityselectionactivity, boolean flag)
	{
		cityselectionactivity.showingNearestCities = flag;
		return flag;
	}

*/
}
