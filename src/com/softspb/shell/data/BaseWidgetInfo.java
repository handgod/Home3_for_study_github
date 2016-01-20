package com.softspb.shell.data;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import com.softspb.shell.util.orm.ann.DataSetter;
import com.softspb.shell.util.orm.ann.PrimaryKey;

public class BaseWidgetInfo
  implements BaseColumns, WidgetsContract.BaseWidgetInfoColumns, Persistable
{
  private int appWidgetId;
  private int id = -1;
  private int minHeight;
  private int minWidth;

  public static BaseWidgetInfo createInstance(int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    BaseWidgetInfo localBaseWidgetInfo = new BaseWidgetInfo();
    localBaseWidgetInfo.appWidgetId = paramInt;
    int i = paramAppWidgetProviderInfo.minWidth;
    localBaseWidgetInfo.minWidth = i;
    int j = paramAppWidgetProviderInfo.minHeight;
    localBaseWidgetInfo.minHeight = j;
    return localBaseWidgetInfo;
  }

  	public boolean equals(Object obj)
	{
		boolean flag = true;
		if (this != obj) 
		{
			if (obj == null)
					flag = false;
				else
				if (getClass() != obj.getClass())
				{
					flag = false;
				} else
				{
					BaseWidgetInfo basewidgetinfo = (BaseWidgetInfo)obj;
					if (id != basewidgetinfo.id)
						flag = false;
				}
		} 
		return flag;
	}

  public int getAppWidgetId()
  {
    return this.appWidgetId;
  }

  public int getId()
  {
    return this.id;
  }

  public int getMinHeight()
  {
    return this.minHeight;
  }

  public int getMinWidth()
  {
    return this.minWidth;
  }

 	public Uri getUri(Context context)
	{
		Uri uri;
		if (id == -1)
			uri = WidgetsContract.BaseWidgetInfoContract.getContentUri(context);
		else
			uri = ContentUris.withAppendedId(WidgetsContract.BaseWidgetInfoContract.getContentUri(context), id);
		return uri;
	}

  public int hashCode()
  {
    return this.id + 31;
  }


  public void setAppWidgetId(int paramInt)
  {
    this.appWidgetId = paramInt;
  }

  public void setId(int paramInt)
  {
    this.id = paramInt;
  }

  public void setMinHeight(int paramInt)
  {
    this.minHeight = paramInt;
  }

  public void setMinWidth(int paramInt)
  {
    this.minWidth = paramInt;
  }

  public ContentValues toContentValues()
  {
    ContentValues localContentValues = new ContentValues();
    Integer localInteger = Integer.valueOf(this.appWidgetId);
    localContentValues.put("app_widget_id", localInteger);
    return localContentValues;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("BaseWidgetInfo [id=");
    int i = this.id;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(i).append(", appWidgetId= ");
    int j = this.appWidgetId;
    return j + "]";
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.data.BaseWidgetInfo
 * JD-Core Version:    0.6.0
 */