package com.softspb.shell.adapters.imageviewer;

import android.graphics.Bitmap;
import com.softspb.shell.adapters.Adapter;
import com.softspb.shell.adapters.AdaptersHolder;

public abstract class ImageViewerAdapter extends Adapter
{
  public ImageViewerAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void getFolderIds(int paramInt);

  public abstract Bitmap getImage(String paramString);

  public abstract void getImageList(int paramInt, String paramString);

  public abstract void openImage(String paramString);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.imageviewer.ImageViewerAdapter
 * JD-Core Version:    0.6.0
 */