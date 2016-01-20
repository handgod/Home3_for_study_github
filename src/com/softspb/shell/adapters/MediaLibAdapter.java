package com.softspb.shell.adapters;

import android.content.Context;

public abstract class MediaLibAdapter extends AbstractContentAdapter<MediaLibAdapterAndroid.Album>
{
  public MediaLibAdapter(Context paramContext)
  {
    super(paramContext);
  }

  public native void addAlbum(int paramInt1, int paramInt2, String paramString1, String paramString2);

  public abstract boolean playAlbum(int paramInt);

  public native void removeAlbum(int paramInt1, int paramInt2);

  public native void updateAlbum(int paramInt1, int paramInt2, String paramString1, String paramString2);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.MediaLibAdapter
 * JD-Core Version:    0.6.0
 */