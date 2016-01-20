package com.softspb.shell.adapters;

import android.content.Context;
import com.softspb.util.CollectionFactory;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AdaptersHolder
{
  private boolean closing;
  private Set<Adapter> createdAdapters;
  private Set<Adapter> startedAdapters;

  public AdaptersHolder()
  {
    HashSet localHashSet1 = CollectionFactory.newHashSet();
    this.createdAdapters = localHashSet1;
    HashSet localHashSet2 = CollectionFactory.newHashSet();
    this.startedAdapters = localHashSet2;
    this.closing = false;
  }

  public void addCreated(Adapter paramAdapter)
  {
    if (!this.createdAdapters.add(paramAdapter))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Adapter ");
      String str1 = paramAdapter.toString();
      String str2 = str1 + "already has been created";
    }
  }

  public void addStarted(Adapter paramAdapter)
  {
    if (!this.createdAdapters.contains(paramAdapter))
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Adapter ");
      String str1 = paramAdapter.toString();
      String str2 = str1 + " hasn't been created";
      throw new IllegalStateException(str2);
    }
    if (!this.startedAdapters.add(paramAdapter))
    {
      StringBuilder localStringBuilder2 = new StringBuilder().append("Adapter ");
      String str3 = paramAdapter.toString();
      String str4 = str3 + " already has been started";
      throw new IllegalStateException(str4);
    }
  }

  public void closeHeldAdapters(Context paramContext)
  {
    this.closing = true;
    Iterator localIterator = this.startedAdapters.iterator();
    while (localIterator.hasNext())
      ((Adapter)localIterator.next()).stop();
    this.startedAdapters.clear();
    localIterator = this.createdAdapters.iterator();
    while (localIterator.hasNext())
      ((Adapter)localIterator.next()).destroy(paramContext);
    this.createdAdapters.clear();
    this.closing = false;
  }

	public void destroyed(Adapter adapter)
	{
		while (closing || createdAdapters.remove(adapter)) 
			return;
		throw new IllegalStateException((new StringBuilder()).append("Adapter ").append(adapter.toString()).append(" hasn't been created").toString());
	}
	public void stopped(Adapter adapter)
	{
		while (closing || startedAdapters.remove(adapter)) 
			return;
		throw new IllegalStateException((new StringBuilder()).append("Adapter ").append(adapter.toString()).append(" hasn't been started").toString());
	}
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.AdaptersHolder
 * JD-Core Version:    0.6.0
 */