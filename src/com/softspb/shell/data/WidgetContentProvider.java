// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.data;

import android.content.*;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;
import android.net.Uri;
import android.text.TextUtils;
import com.softspb.shell.util.orm.DataBuilder;
import com.softspb.util.CollectionFactory;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.util.*;

// Referenced classes of package com.softspb.shell.data:
//			BaseWidgetInfo, ShortcutInfo, WidgetsContract

public class WidgetContentProvider extends ContentProvider
{
	class DBHelper extends SQLiteOpenHelper
	{

		private static final String DB_NAME = "shell.db";
		private static final int DB_VERSION = 33;
		final WidgetContentProvider this$0;

		public void onCreate(SQLiteDatabase sqlitedatabase)
		{
			logger.d("DBHelper.onCreate");
			sqlitedatabase.execSQL(baseWidgetInfoBuilder.createTableSQL("BaseWidget"));
			sqlitedatabase.execSQL(shortcutInfoBuilder.createTableSQL("Shortcut"));
		}

		public void onUpgrade(SQLiteDatabase sqlitedatabase, int i, int j)
		{
			logger.d((new StringBuilder()).append("DBHelper.onUpgrade: ").append(i).append("->").append(j).toString());
			sqlitedatabase.execSQL("DROP TABLE IF EXISTS widget_info");
			sqlitedatabase.execSQL("DROP TABLE IF EXISTS Shortcut");
			sqlitedatabase.execSQL("DROP TABLE IF EXISTS BaseWidget");
			onCreate(sqlitedatabase);
		}

		public DBHelper(Context context)
		{
			super(context, "shell.db", null, 33);
			this$0 = WidgetContentProvider.this;
		}
	}

	static interface Tables
	{

		public static final String BASE_WIDGETS = "BaseWidget";
		public static final String SHORTCUTS = "Shortcut";
		public static final String WIDGET_INFO = "widget_info";
	}


	private static Map BASE_WIDGETS_PROJECTION;
	private static final HashMap CODE2TYPE;
	private static final int MATCH_BASE_WIDGETS_DIR = 3;
	private static final int MATCH_BASE_WIDGETS_ITEM = 4;
	private static final int MATCH_SHORTCUTS_DIR = 5;
	private static final int MATCH_SHORTCUTS_ITEM = 6;
	private static Map SHORTCUTS_PROJECTION;
	private final DataBuilder baseWidgetInfoBuilder = DataBuilder.createBuilder(BaseWidgetInfo.class);
	private Uri baseWidgetsContentUri;
	private DBHelper dbHelper;
	private Logger logger;
	private final DataBuilder shortcutInfoBuilder = DataBuilder.createBuilder(ShortcutInfo.class);
	private Uri shortcutsContentUri;
	private final UriMatcher uriMatcher = new UriMatcher(-1);

	public WidgetContentProvider()
	{
		logger = Loggers.getLogger(WidgetContentProvider.class.getName());
	}

	private String createExceptionMessage(ArrayList arraylist)
	{
		ListIterator listiterator = arraylist.listIterator();
		StringBuilder stringbuilder = new StringBuilder("Missed Fields:");
		for (; listiterator.hasNext(); stringbuilder.append((String)listiterator.next()))
			if (listiterator.hasPrevious())
				stringbuilder.append(",");

		return stringbuilder.toString();
	}

	private static String createWhereStatement(String s, int i)
	{
		StringBuilder stringbuilder = (new StringBuilder()).append("_id=").append(i);
		String s1;
		if (!TextUtils.isEmpty(s))
			s1 = (new StringBuilder()).append(" AND (").append(s).append(')').toString();
		else
			s1 = "";
		return stringbuilder.append(s1).toString();
	}

	private static int getId(Uri uri)
	{
		return (new Integer((String)uri.getPathSegments().get(1))).intValue();
	}

	private void initProjection()
	{
		if (BASE_WIDGETS_PROJECTION == null)
			BASE_WIDGETS_PROJECTION = baseWidgetInfoBuilder.createProjectionMap();
		if (SHORTCUTS_PROJECTION == null)
			SHORTCUTS_PROJECTION = shortcutInfoBuilder.createProjectionMap();
	}

	private void initUriMatcher(String s)
	{
		uriMatcher.addURI(s, "basewidgets", 3);
		uriMatcher.addURI(s, "basewidgets/#", 4);
		uriMatcher.addURI(s, "shortcuts", 5);
		uriMatcher.addURI(s, "shortcuts/#", 6);
	}

	private void validateValues(ContentValues contentvalues, DataBuilder databuilder)
	{
		ArrayList arraylist = databuilder.checkForMissedValues(contentvalues);
		if (!arraylist.isEmpty())
			throw new IllegalStateException(createExceptionMessage(arraylist).toString());
		else
			return;
	}

	public void attachInfo(Context context, ProviderInfo providerinfo)
	{
		super.attachInfo(context, providerinfo);
		String s = WidgetsContract.getAuthority(context);
		if (!providerinfo.authority.equals(s))
			throw new RuntimeException((new StringBuilder()).append("Unexpected authority: ").append(providerinfo.authority).append(", must be: ").append(s).toString());
		String s1 = getClass().getName();
		String s2 = (new StringBuilder()).append(context.getPackageName()).append(".widgets.provider.WidgetsProvider").toString();
		if (!s1.equals(s2))
		{
			throw new RuntimeException((new StringBuilder()).append("Unexpected class name: ").append(s1).append(", must be: ").append(s2).toString());
		} else
		{
			initUriMatcher(s);
			shortcutsContentUri = WidgetsContract.ShortcutInfoContract.getContentUri(context);
			baseWidgetsContentUri = WidgetsContract.BaseWidgetInfoContract.getContentUri(context);
			return;
		}
	}

	public int delete(Uri uri, String s, String as[])
	{
		int i;
		int k = 0;
		SQLiteDatabase sqlitedatabase;
		i = uriMatcher.match(uri);
		sqlitedatabase = dbHelper.getWritableDatabase();
		switch (i) {
		case 3:
			int l = sqlitedatabase.delete("BaseWidget", s, as);
			k = l;
			break;
		case 4:
			k = sqlitedatabase.delete("BaseWidget", createWhereStatement(s, (new Integer((String)uri.getPathSegments().get(1))).intValue()), as);

			break;
		case 5:
			k = sqlitedatabase.delete("Shortcut", s, as);

			break;
		case 6:
			int j = sqlitedatabase.delete("Shortcut", createWhereStatement(s, getId(uri)), as);
			k = j;
			break;
		default:

			break;
		}
		sqlitedatabase.close();
		return k;
	}

	public String getType(Uri uri)
	{
		int i = uriMatcher.match(uri);
		if (i != -1)
			return (String)CODE2TYPE.get(Integer.valueOf(i));
		else
			throw new IllegalArgumentException((new StringBuilder()).append("Unknown URI ").append(uri).toString());
	}

	public Uri insert(Uri uri, ContentValues contentvalues)
	{
		Uri uri2 = null;
		String s;
		Uri uri1;
		DataBuilder databuilder;
		switch (uriMatcher.match(uri)) {
		case 3:
			
			s = "BaseWidget";
			uri1 = baseWidgetsContentUri;
			databuilder = baseWidgetInfoBuilder;
			break;
		case 5:
			s = "Shortcut";
			uri1 = shortcutsContentUri;
			databuilder = shortcutInfoBuilder;
			break;
		case 4:	
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Unknown URI:").append(uri).toString());

		}
		
		validateValues(contentvalues, databuilder);
		
		long l = dbHelper.getWritableDatabase().insert(s, null, contentvalues);
		if (l > 0L)
		{
		uri2 = ContentUris.withAppendedId(uri1, l);
		getContext().getContentResolver().notifyChange(uri2, null);
		}
		return uri2;
		
	}

	public boolean onCreate()
	{
		dbHelper = new DBHelper(getContext());
		initProjection();
		return true;
	}

	public Cursor query(Uri uri, String as[], String s, String as1[], String s1)
	{
		SQLiteQueryBuilder sqlitequerybuilder = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case 3:
			sqlitequerybuilder.setTables("BaseWidget");
			sqlitequerybuilder.setProjectionMap(BASE_WIDGETS_PROJECTION);
			break;
		case 4:
			sqlitequerybuilder.appendWhere((new StringBuilder()).append("_id=").append((String)uri.getPathSegments().get(1)).toString());
			break;
		case 5:
			sqlitequerybuilder.setTables("Shortcut");
			sqlitequerybuilder.setProjectionMap(SHORTCUTS_PROJECTION);
			break;
		case 6:
			sqlitequerybuilder.appendWhere((new StringBuilder()).append("_id=").append((String)uri.getPathSegments().get(1)).toString());
			break;
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("unknown uri:").append(uri).toString());
		}
		Cursor cursor = sqlitequerybuilder.query(dbHelper.getReadableDatabase(), as, s, as1, null, null, s1);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	public int update(Uri uri, ContentValues contentvalues, String s, String as[])
	{
		int i = 0;
	
		switch (uriMatcher.match(uri)) {
		case 4:
			int j = dbHelper.getWritableDatabase().update("BaseWidget", contentvalues, (new StringBuilder()).append("_id=").append(uri.getLastPathSegment()).toString(), null);
			i = j;
			break;

		default:
			i = 0;
		}
		return i;
	}

	static 
	{
		CODE2TYPE = CollectionFactory.newHashMap();
		CODE2TYPE.put(Integer.valueOf(3), "vnd.android.cursor.dir/vnd.softspb.basewidget");
		CODE2TYPE.put(Integer.valueOf(4), "vnd.android.cursor.item/vnd.softspb.basewidget");
		CODE2TYPE.put(Integer.valueOf(5), WidgetsContract.ShortcutInfoContract.CONTENT_TYPE);
		CODE2TYPE.put(Integer.valueOf(6), WidgetsContract.ShortcutInfoContract.CONTENT_ITEM_TYPE);
	}



}
