package com.softspb.shell.adapters;

import android.content.Intent;

public abstract class ShortcutAdapter extends Adapter
{
  public ShortcutAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void addShortcut(Intent paramIntent, boolean paramBoolean);

  public abstract void launch(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

  public abstract void remove(int paramInt);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.ShortcutAdapter
 * JD-Core Version:    0.6.0
 */