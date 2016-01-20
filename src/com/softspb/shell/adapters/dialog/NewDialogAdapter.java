package com.softspb.shell.adapters.dialog;

import com.softspb.shell.adapters.Adapter;
import com.softspb.shell.adapters.AdaptersHolder;

public abstract class NewDialogAdapter extends Adapter
{
  public NewDialogAdapter(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
  }

  public abstract void closeAllDialogs();

  public abstract ShellDatePickerDialog newShellDatePickerDialog(int paramInt);

  public abstract ShellDialog newShellDialog(int paramInt);

  public abstract void showPopupMessage(String paramString);
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.dialog.NewDialogAdapter
 * JD-Core Version:    0.6.0
 */