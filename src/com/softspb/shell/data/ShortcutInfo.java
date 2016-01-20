package com.softspb.shell.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import com.softspb.shell.util.orm.ann.DataSetter;
import com.softspb.shell.util.orm.ann.PrimaryKey;
import com.spb.shell3d.R.drawable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class ShortcutInfo
  implements BaseColumns, WidgetsContract.ShortcutInfoColumns, Persistable
{
  public static final int ICON_TYPE_BITMAP = 2;
  public static final int ICON_TYPE_RESOURCE = 1;
  public static final int SHORTCUT_TYPE = 1;
  private int baseWidgetId;
  private Bitmap icon;
  private Intent.ShortcutIconResource iconResource;
  private int iconType;
  private int id;
  private Intent intent;
  private String title;

  public ShortcutInfo()
  {
    Intent.ShortcutIconResource localShortcutIconResource = new Intent.ShortcutIconResource();
    this.iconResource = localShortcutIconResource;
    this.id = -1;
  }

  public ShortcutInfo(String paramString, Intent paramIntent)
  {
    Intent.ShortcutIconResource localShortcutIconResource = new Intent.ShortcutIconResource();
    this.iconResource = localShortcutIconResource;
    this.id = -1;
    this.title = paramString;
    this.intent = paramIntent;
  }

 	static byte[] flattenBitmap(Bitmap bitmap)
	{
		byte abyte0[] = null;
		if (bitmap != null)
		 {
		 	ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(4 * (bitmap.getWidth() * bitmap.getHeight()));
		byte abyte1[];
		bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, bytearrayoutputstream);
		try {
			bytearrayoutputstream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bytearrayoutputstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		abyte1 = bytearrayoutputstream.toByteArray();
		abyte0 = abyte1;
		return abyte0;
		 }
		 else 
		  return abyte0;

	}

  public Bitmap getIcon()
  {
    return this.icon;
  }

  public Intent.ShortcutIconResource getIconResource()
  {
    return this.iconResource;
  }

  public int getIconType()
  {
    return this.iconType;
  }

  public int getId()
  {
    return this.id;
  }
	public Uri getImageUri(Context context)
	{
		Uri uri;
		switch (iconType) {
		case 1:
			Uri uri1;
			int i = 0;
			try {
				i = context.getPackageManager().getResourcesForApplication(iconResource.packageName).getIdentifier(iconResource.resourceName, null, null);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object aobj1[] = new Object[3];
			aobj1[0] = "android.resource";
			aobj1[1] = iconResource.packageName;
			aobj1[2] = Integer.valueOf(i);
			uri1 = Uri.parse(String.format("%s://%s/%d", aobj1));
			uri = uri1;
			return uri;
		case 2:
			uri = getUri(context);
			break;
		default:	
			Object aobj[] = new Object[3];
			aobj[0] = "android.resource";
			aobj[1] = context.getPackageName();
			aobj[2] = Integer.valueOf(com.spb.shell3d.R.drawable.dummy_icon);
			uri = Uri.parse(String.format("%s://%s/%d", aobj));
			break;
		}
		return uri;
	}

  public Intent getIntent()
  {
    return this.intent;
  }

  public String getTitle()
  {
    return this.title;
  }

 	public Uri getUri(Context context)
	{
		Uri uri;
		if (id == -1)
			uri = WidgetsContract.ShortcutInfoContract.getContentUri(context);
		else
			uri = ContentUris.withAppendedId(WidgetsContract.ShortcutInfoContract.getContentUri(context), id);
		return uri;
	}

  public void setIcon(Bitmap paramBitmap)
  {
    this.iconType = 2;
    this.icon = paramBitmap;
  }
	public void setIcon(byte abyte0[])
	{
		if (abyte0 != null)
			icon = BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length);
	}

	public void setIconResource(android.content.Intent.ShortcutIconResource shortcuticonresource)
	{
		iconType = 1;
		iconResource = shortcuticonresource;
	}



  public void setIconType(int paramInt)
  {
    this.iconType = paramInt;
  }


  public void setId(int paramInt)
  {
    this.id = paramInt;
  }


  public void setIntent(String paramString)
  {
    try
    {
      Intent localIntent = Intent.parseUri(paramString, 0);
      this.intent = localIntent;
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
             int i = Log.e("Shortcut Info", "broken uri");
    }
  }

 public void setPackageName(String s)
	{
		if (s != null)
			iconResource.packageName = s;
	}

  	public void setResourceName(String s)
	{
		if (s != null)
			iconResource.resourceName = s;
	}

	public void setTitle(String s)
	{
		title = s;
	}

 	public ContentValues toContentValues()
	{
		String s = null;
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("title", title);
		contentvalues.put("intent_descr", intent.toUri(0));
		contentvalues.put("icon_type", Integer.valueOf(iconType));
		String s1;
		if (iconResource == null)
			s1 = null;
		else
			s1 = iconResource.resourceName;
		contentvalues.put("resource_name", s1);
		if (iconResource != null)
			s = iconResource.packageName;
		contentvalues.put("package_name", s);
		contentvalues.put("icon", flattenBitmap(icon));
		return contentvalues;
	}
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.data.ShortcutInfo
 * JD-Core Version:    0.6.0
 */