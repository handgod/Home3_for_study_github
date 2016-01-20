package com.softspb.shell.adapters;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;

public abstract class ImageAdapter extends Adapter
{
  public ImageAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract boolean SaveCompressed(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, String paramString);

  public abstract Bitmap getByBuffer(ByteBuffer paramByteBuffer);

  public abstract Bitmap getByBuffer(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);

  public abstract Bitmap getByPath(String paramString);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.ImageAdapter
 * JD-Core Version:    0.6.0
 */