package com.softspb.shell.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.softspb.shell.adapters.AbstractContentAdapter.ContentItem;
import com.softspb.util.DelayedHandler;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public abstract class AbstractContentAdapter<T extends ContentItem>
  implements Adapter2
{
  protected final SparseArray<T> contentItems;
  private ContentObserver contentObserver;
  protected ContentResolver contentResolver;
  protected Uri[] contentUris;
  protected Context context;
  private final SparseIntArray deletedIds;
  protected final Logger logger;
  protected DelayedHandler observerHandler;
  protected HandlerThread observerThread;
  protected final Runnable reloadCallback;
  protected final Object reloadLock;

  class AbstractContentAdapter$1
  implements Runnable
{
  public void run()
  {
    reload(true);
  }
}
  public AbstractContentAdapter(Context paramContext)
  {
    AbstractContentAdapter$1 local1 = new AbstractContentAdapter$1();
    this.reloadCallback = local1;
    Object localObject = new Object();
    this.reloadLock = localObject;
    SparseIntArray localSparseIntArray = new SparseIntArray();
    this.deletedIds = localSparseIntArray;
    SparseArray localSparseArray = new SparseArray();
    this.contentItems = localSparseArray;
    Logger localLogger = Loggers.getLogger(getClass().getName());
    this.logger = localLogger;
    this.context = paramContext;
    ContentResolver localContentResolver = paramContext.getContentResolver();
    this.contentResolver = localContentResolver;
  }

  private void processDeletedIds(boolean paramBoolean)
  {
    int i = this.deletedIds.size();
    int j = 0;
    while (j < i)
    {
      int k = this.deletedIds.keyAt(j);
      deleteContentItem(k);
      j += 1;
    }
  }

  private void resetDeletedIds()
  {
    SparseArray localSparseArray = this.contentItems;
    SparseIntArray localSparseIntArray = this.deletedIds;
    localSparseIntArray.clear();
    int i = localSparseArray.size();
    int j = 0;
    while (j < i)
    {
      int k = localSparseArray.keyAt(j);
      localSparseIntArray.put(k, 1);
      j += 1;
    }
  }

  protected void addContentItem(T paramT)
  {
    SparseArray localSparseArray = this.contentItems;
    int i = paramT.id;
    localSparseArray.put(i, paramT);
    addNativeContentItem(paramT);
  }

  protected abstract void addNativeContentItem(T paramT);

  protected abstract T createContentItem(int paramInt, Cursor paramCursor);

  protected AbstractContentAdapter<T>.AbstractContentObserver createContentObserver(DelayedHandler paramDelayedHandler)
  {
    return new AbstractContentObserver(paramDelayedHandler);
  }

  protected void deleteContentItem(int paramInt)
  {
    ContentItem localContentItem = (ContentItem)this.contentItems.get(paramInt);
    if (localContentItem != null)
    {
      this.contentItems.delete(paramInt);
      Logger localLogger = this.logger;
      String str = "Deleted: " + localContentItem;
      localLogger.d(str);
    }
    removeNativeContentItem((T) localContentItem);
  }

  protected abstract int getContentItemId(Cursor paramCursor);

  public void onStart()
	{
		logger.d("onStart");
		if (contentObserver == null)
		{
			observerThread = new HandlerThread((new StringBuilder()).append(getClass().getSimpleName()).append("-Observer").toString());
			observerThread.start();
			observerHandler = new DelayedHandler(observerThread.getLooper(), 1000L);
			contentObserver = createContentObserver(observerHandler);
			if (contentUris != null)
			{
				Uri auri[] = contentUris;
				int i = auri.length;
				int j = 0;
				while (j < i) 
				{
					Uri uri = auri[j];
					context.getContentResolver().registerContentObserver(uri, true, contentObserver);
					j++;
				}
			}
		}
		else
		{
			logger.w("already started");
		}
		return;
	}
  
  public void onStop()
  {
    this.logger.d("onStop");
    if (this.contentObserver != null)
    {
      ContentResolver localContentResolver = this.context.getContentResolver();
      ContentObserver localContentObserver = this.contentObserver;
      localContentResolver.unregisterContentObserver(localContentObserver);
      this.contentObserver = null;
      this.observerThread.getLooper().quit();
      this.observerHandler.removeCallbacksAndMessages(null);
    }
  }

  protected boolean processRow(Cursor paramCursor, boolean paramBoolean)
  {
    int i = getContentItemId(paramCursor);
    Logger localLogger1 = this.logger;
    String str1 = "processRow: id=" + i;
    localLogger1.d(str1);
    this.deletedIds.delete(i);
    ContentItem localContentItem1 = (ContentItem)this.contentItems.get(i);
    if (localContentItem1 == null)
    {
      ContentItem localContentItem2 = createContentItem(i, paramCursor);
      Logger localLogger2 = this.logger;
      String str2 = "processRow: New content item: " + localContentItem2;
      localLogger2.d(str2);
      addContentItem((T) localContentItem2);
    }
    else
    {
      if (localContentItem1.update(paramCursor))
      {
      Logger localLogger3 = this.logger;
      String str3 = "processRow: Updated content item: " + localContentItem1;
      localLogger3.d(str3);
      updateContentItem((T) localContentItem1);
      }
    }
    return true;
  }

  protected abstract Cursor query();

  // ERROR //
  public void reload(boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 51	com/softspb/shell/adapters/AbstractContentAdapter:reloadLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 79	com/softspb/shell/adapters/AbstractContentAdapter:logger	Lcom/softspb/util/log/Logger;
    //   11: astore_3
    //   12: new 141	java/lang/StringBuilder
    //   15: dup
    //   16: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   19: ldc_w 259
    //   22: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   25: iload_1
    //   26: invokevirtual 262	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   29: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   32: astore 4
    //   34: aload_3
    //   35: aload 4
    //   37: invokevirtual 160	com/softspb/util/log/Logger:d	(Ljava/lang/String;)V
    //   40: aload_0
    //   41: invokevirtual 264	com/softspb/shell/adapters/AbstractContentAdapter:query	()Landroid/database/Cursor;
    //   44: astore 5
    //   46: aload 5
    //   48: ifnull +41 -> 89
    //   51: aload_0
    //   52: invokespecial 266	com/softspb/shell/adapters/AbstractContentAdapter:resetDeletedIds	()V
    //   55: aload 5
    //   57: invokeinterface 272 1 0
    //   62: istore 6
    //   64: aload 5
    //   66: invokeinterface 275 1 0
    //   71: ifne +13 -> 84
    //   74: aload_0
    //   75: aload 5
    //   77: iload_1
    //   78: invokevirtual 277	com/softspb/shell/adapters/AbstractContentAdapter:processRow	(Landroid/database/Cursor;Z)Z
    //   81: ifne +33 -> 114
    //   84: aload_0
    //   85: iload_1
    //   86: invokespecial 279	com/softspb/shell/adapters/AbstractContentAdapter:processDeletedIds	(Z)V
    //   89: aload 5
    //   91: ifnull +10 -> 101
    //   94: aload 5
    //   96: invokeinterface 282 1 0
    //   101: aload_0
    //   102: getfield 79	com/softspb/shell/adapters/AbstractContentAdapter:logger	Lcom/softspb/util/log/Logger;
    //   105: ldc_w 284
    //   108: invokevirtual 160	com/softspb/util/log/Logger:d	(Ljava/lang/String;)V
    //   111: aload_2
    //   112: monitorexit
    //   113: return
    //   114: aload 5
    //   116: invokeinterface 287 1 0
    //   121: istore 7
    //   123: goto -59 -> 64
    //   126: astore 8
    //   128: aload 5
    //   130: ifnull +10 -> 140
    //   133: aload 5
    //   135: invokeinterface 282 1 0
    //   140: aload_0
    //   141: getfield 79	com/softspb/shell/adapters/AbstractContentAdapter:logger	Lcom/softspb/util/log/Logger;
    //   144: ldc_w 284
    //   147: invokevirtual 160	com/softspb/util/log/Logger:d	(Ljava/lang/String;)V
    //   150: aload 8
    //   152: athrow
    //   153: astore 9
    //   155: aload_2
    //   156: monitorexit
    //   157: aload 9
    //   159: athrow
    //   160: astore 10
    //   162: goto -61 -> 101
    //   165: astore 11
    //   167: goto -27 -> 140
    //
    // Exception table:
    //   from	to	target	type
    //   40	89	126	finally
    //   114	123	126	finally
    //   7	40	153	finally
    //   94	101	153	finally
    //   101	113	153	finally
    //   133	140	153	finally
    //   140	153	153	finally
    //   94	101	160	java/lang/Exception
    //   133	140	165	java/lang/Exception
  }

  protected abstract void removeNativeContentItem(T paramT);

  public void setContentUris(Uri[] paramArrayOfUri)
  {
    this.contentUris = paramArrayOfUri;
  }

  protected void updateContentItem(T paramT)
  {
    updateNativeContentItem(paramT);
  }

  protected abstract void updateNativeContentItem(T paramT);

  class AbstractContentObserver extends ContentObserver
  {
    DelayedHandler handler;

    AbstractContentObserver(DelayedHandler arg2)
    {
      super(arg2);
      this.handler = arg2;
    }

    public void onChange(boolean paramBoolean)
    {
      AbstractContentAdapter.this.logger.d("ContentObserver.onChange");
      DelayedHandler localDelayedHandler = this.handler;
      Runnable localRunnable = AbstractContentAdapter.this.reloadCallback;
      localDelayedHandler.handleDelayed(localRunnable);
    }
  }

  public static  abstract class ContentItem
  {
    int id;

    public ContentItem(int i,Cursor arg2)
    {
      this.id = i;
    }

    protected void formatFields(StringBuilder paramStringBuilder)
    {
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      String str = getClass().getSimpleName();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append("[id=");
      int i = this.id;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(i);
      formatFields(localStringBuilder3);
      return "]";
    }

    public boolean update(Cursor paramCursor)
    {
      return false;
    }
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.AbstractContentAdapter
 * JD-Core Version:    0.6.0
 */