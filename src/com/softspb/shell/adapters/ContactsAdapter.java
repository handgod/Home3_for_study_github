package com.softspb.shell.adapters;

import android.graphics.Bitmap;

public abstract class ContactsAdapter extends Adapter
{
  static final int NATIVE_ADDR_TYPE_EMAIL = 1;
  static final int NATIVE_ADDR_TYPE_IM = 2;
  static final int NATIVE_ADDR_TYPE_OTHER = 3;
  static final int NATIVE_ADDR_TYPE_PHONE = 0;
  static final int NATIVE_EVENT_TYPE_ANNIVERSARY = 1;
  static final int NATIVE_EVENT_TYPE_BIRTHDAY = 0;
  static final int NATIVE_EVENT_TYPE_OTHER = 2;
  static final int NATIVE_KIND_ALL_CONTACTS = 0;
  static final int NATIVE_KIND_FAVORITE_CONTACTS = 1;
  static final int NATIVE_LOC_TYPE_HOME = 1;
  static final int NATIVE_LOC_TYPE_MOBILE = 0;
  static final int NATIVE_LOC_TYPE_OTHER = 3;
  static final int NATIVE_LOC_TYPE_WORK = 2;

  public ContactsAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract boolean addToFavorites(int paramInt);

  public abstract void call(String paramString, boolean paramBoolean);

  public abstract void disposeContactsAdapter();

  public abstract int getContactByPhone(String paramString);

  public abstract Bitmap getContactPic(int paramInt, String paramString);

  public abstract int getLastMessage(int paramInt);

  public abstract void initContactsAdapter(int paramInt);

  public abstract void openContactCard(int paramInt, String paramString);

  public abstract void reloadBirthdays(int paramInt);

  public abstract void reloadContact(int paramInt);

  public abstract void reloadContacts(int paramInt);

  public abstract boolean removeFromFavorites(int paramInt);

  public abstract void showAddToFavoritesDialog();

  public abstract void showPickContactDialog(int paramInt);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.ContactsAdapter
 * JD-Core Version:    0.6.0
 */