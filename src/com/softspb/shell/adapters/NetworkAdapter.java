package com.softspb.shell.adapters;

public abstract class NetworkAdapter extends Adapter
{
  public NetworkAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void openBrowser(String paramString);

  public abstract void startServer();

  public abstract void stopServer();
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.NetworkAdapter
 * JD-Core Version:    0.6.0
 */