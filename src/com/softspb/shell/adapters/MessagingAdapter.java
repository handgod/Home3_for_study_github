package com.softspb.shell.adapters;

public abstract class MessagingAdapter extends Adapter
{
  public static final int NO_MESSAGE = 255;
  public static final String SMS_MMS_ACCOUNT_NAME = "Messaging";

  public MessagingAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void addListener(int paramInt);

  public abstract int getCount();

  public abstract int getLastMessage(int paramInt);

  public abstract void getMessageById(int paramInt, long paramLong);

  public abstract long[] getMessageList(int paramInt);

  public abstract int getUnreadCount();

  public abstract void onInboxFolderCreated(String paramString, int paramInt);

  public abstract void openMessageThread(long paramLong);

  public abstract void openSmsMmsAccount();

  public abstract void openSmsMmsActivity();

  public abstract void removeListener(int paramInt);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.MessagingAdapter
 * JD-Core Version:    0.6.0
 */