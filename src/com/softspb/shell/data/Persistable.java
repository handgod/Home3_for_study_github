package com.softspb.shell.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public abstract interface Persistable
{
  public abstract Uri getUri(Context paramContext);

  public abstract ContentValues toContentValues();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.data.Persistable
 * JD-Core Version:    0.6.0
 */