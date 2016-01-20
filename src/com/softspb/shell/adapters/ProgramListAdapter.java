package com.softspb.shell.adapters;

import com.spb.programlist.ProgramListTags;

public abstract class ProgramListAdapter extends Adapter
  implements ProgramListTags
{
  public static final String RESOLVER_ACTIVITY_CLASS_NAME = "com.android.internal.app.ResolverActivity";
  public static final String RESOLVER_ACTIVITY_PACKAGE_NAME = "android";

  public ProgramListAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public static native void reinit();

  public abstract void findByTag(String paramString);

  public abstract boolean launch(String paramString);

  public abstract boolean launch(String paramString1, String paramString2);

  public abstract void uninstall(String paramString);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.ProgramListAdapter
 * JD-Core Version:    0.6.0
 */