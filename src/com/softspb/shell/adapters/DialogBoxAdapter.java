package com.softspb.shell.adapters;

public abstract class DialogBoxAdapter extends Adapter
{
  public static final int BTN_CANCEL = 8;
  public static final int BTN_NO = 4;
  public static final int BTN_OK = 1;
  public static final int BTN_YES = 2;
  public static final int CHECK_DISABLE_INPUT = 1;
  public static final int CHECK_MARKED = 2;
  public static final int ICON_DEFAULT = 0;
  public static final int ICON_ERROR = 3;
  public static final int ICON_INFO = 1;
  public static final int ICON_WARNING = 2;

  public DialogBoxAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void closeAllDialogs();

  protected native void onDialogResult(int paramInt1, int paramInt2, String paramString, int paramInt3);

  public abstract boolean startDialogBox(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, String paramString4, int[] paramArrayOfInt1, String[] paramArrayOfString, int[] paramArrayOfInt2);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.DialogBoxAdapter
 * JD-Core Version:    0.6.0
 */