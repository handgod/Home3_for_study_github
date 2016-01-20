package com.softspb.shell.widget;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

import com.softspb.shell.Home;
import com.softspb.shell.Home.MovementController;

public class ShellAppWidgetHost extends AppWidgetHost
{
  private Home.MovementController controller;

  public ShellAppWidgetHost(Context paramContext, int paramInt)
  {
    super(paramContext, paramInt);
  }

  protected AppWidgetHostView onCreateView(Context paramContext, int paramInt, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    ShellWidgetHostView localShellWidgetHostView = new ShellWidgetHostView(paramContext);
    Home.MovementController localMovementController = this.controller;
    localShellWidgetHostView.setMovementController(localMovementController);
    return localShellWidgetHostView;
  }

  public void setMovementController(Home.MovementController paramMovementController)
  {
    this.controller = paramMovementController;
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.widget.ShellAppWidgetHost
 * JD-Core Version:    0.6.0
 */