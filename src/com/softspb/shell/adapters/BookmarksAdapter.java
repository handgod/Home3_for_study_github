package com.softspb.shell.adapters;

import android.graphics.Bitmap;
import java.lang.ref.WeakReference;

public abstract class BookmarksAdapter
  implements Adapter2
{
  public static final String SCHEMA_BOOKMARK_IMAGE = "bookmark-image";
  private static WeakReference<BookmarksAdapter> someInstance;

  public BookmarksAdapter()
  {
    someInstance = new WeakReference(this);
  }

  public static BookmarksAdapter instance()
  {
    if (someInstance != null);
    for (BookmarksAdapter localBookmarksAdapter = (BookmarksAdapter)someInstance.get(); ; localBookmarksAdapter = null)
      return localBookmarksAdapter;
  }

  public native void addBookmark(int paramInt1, int paramInt2, String paramString1, String paramString2);

  public abstract Bitmap getImage(String paramString);

  public abstract boolean openBookmark(String paramString);

  public native void removeBookmark(int paramInt1, int paramInt2);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.BookmarksAdapter
 * JD-Core Version:    0.6.0
 */