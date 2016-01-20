package com.softspb.shell.data;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

public final class WidgetsContract
{
  private static final String AUTHORITY_SUFFIX = ".widgets";
  private static volatile String authority;

  public static String getAuthority(Context paramContext)
  {
    if (authority == null);
    synchronized (WidgetsContract.class)
    {
      if (authority == null)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        String str = paramContext.getPackageName();
        authority = str + ".widgets";
      }
      return authority;
    }
  }

  public final static class ShortcutInfoContract
    implements BaseColumns, WidgetsContract.ShortcutInfoColumns
  {
    public static String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.softspb.shortcut"; ;
    public static final String CONTENT_PATH = "shortcuts";
    public static final  String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.shortcut";
    private volatile static Uri contentUri;

    public static Uri getContentUri(Context paramContext)
    {
      if (contentUri == null);
      synchronized (ShortcutInfoContract.class)
      {
        if (contentUri == null)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("content://");
          String str = WidgetsContract.getAuthority(paramContext);
          contentUri = Uri.parse(str + "/" + "shortcuts");
        }
        return contentUri;
      }
    }
  }

  public abstract interface ShortcutInfoColumns
  {
    public static final String ICON = "icon";
    public static final String ICON_TYPE = "icon_type";
    public static final String INTENT_DESCRIPTION = "intent_descr";
    public static final String PACKAGE_NAME = "package_name";
    public static final String RESOURCE_NAME = "resource_name";
    public static final String TITLE = "title";
  }

  public final static class BaseWidgetInfoContract
  {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.softspb.basewidget";
    public static final String CONTENT_PATH = "basewidgets";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.softspb.basewidget";
    private volatile static Uri contentUri;

    public static Uri getContentUri(Context paramContext)
    {
      if (contentUri == null);
      synchronized (BaseWidgetInfoContract.class)
      {
        if (contentUri == null)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("content://");
          String str = WidgetsContract.getAuthority(paramContext);
          contentUri = Uri.parse(str + "/" + "basewidgets");
        }
        return contentUri;
      }
    }
  }

  public abstract interface BaseWidgetInfoColumns
  {
    public static final String APP_WIDGET_ID = "app_widget_id";
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.data.WidgetsContract
 * JD-Core Version:    0.6.0
 */