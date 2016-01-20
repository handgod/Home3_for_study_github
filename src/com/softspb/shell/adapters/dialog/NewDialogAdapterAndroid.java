// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.dialog;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import com.softspb.shell.adapters.AdaptersHolder;
import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.lang.ref.WeakReference;
import java.util.*;

// Referenced classes of package com.softspb.shell.adapters.dialog:
//			NewDialogAdapter, IShellDialog, ShellDatePickerDialog, ShellDialog

public class NewDialogAdapterAndroid extends NewDialogAdapter
{

	private static Logger logger = Loggers.getLogger(NewDialogAdapterAndroid.class);
	private int adapterAddress;
	Context context;
	final List shellDialogs = new LinkedList();
	Handler uiHandler;

	public NewDialogAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
	}

	private void cleanTrash()
	{
		Iterator iterator = shellDialogs.iterator();
		do
		{
			if (!iterator.hasNext())
				break;
			if ((IShellDialog)((WeakReference)iterator.next()).get() == null)
				iterator.remove();
		} while (true);
	}

	private static void logd(String s)
	{
		Logger logger1 = logger;
		Object aobj[] = new Object[1];
		aobj[0] = s;
		logger1.d("NewDialogAdapterAndroid", aobj);
	}

	/**
	 * @deprecated Method closeAllDialogs is deprecated
	 */

	public synchronized void closeAllDialogs()
	{
	
		for (Iterator iterator = shellDialogs.iterator(); iterator.hasNext(); iterator.remove())
		{
			IShellDialog ishelldialog = (IShellDialog)((WeakReference)iterator.next()).get();
			if (ishelldialog != null)
				ishelldialog.dismiss();
		}
	}

	/**
	 * @deprecated Method newShellDatePickerDialog is deprecated
	 */

	public synchronized ShellDatePickerDialog newShellDatePickerDialog(int i)
	{
		ShellDatePickerDialog shelldatepickerdialog;
		shelldatepickerdialog = new ShellDatePickerDialog(context, uiHandler.getLooper(), adapterAddress, i);
		cleanTrash();
		WeakReference weakreference = new WeakReference(shelldatepickerdialog);
		shellDialogs.add(weakreference);
		return shelldatepickerdialog;
	}

	/**
	 * @deprecated Method newShellDialog is deprecated
	 */

	public synchronized ShellDialog newShellDialog(int i)
	{
		
		ShellDialog shelldialog;
		shelldialog = new ShellDialog(context, uiHandler.getLooper(), adapterAddress, i);
		cleanTrash();
		WeakReference weakreference = new WeakReference(shelldialog);
		shellDialogs.add(weakreference);
		return shelldialog;
	}

	protected void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
//		onCreate(context1, nativecallbacks);
		context = context1;
	}

	protected void onStart(int i)
	{
		logd((new StringBuilder()).append("onStart: adapterAddress=0x").append(Integer.toHexString(i)).toString());
		adapterAddress = i;
	}

	protected void onStartInUIThread()
	{
		onStartInUIThread();
		uiHandler = new Handler();
	}

	public void showPopupMessage(final String message)
	{
		logd((new StringBuilder()).append("showPopupMessage: message=\"").append(message).append("\"").toString());
		uiHandler.post(new Runnable() {

			final NewDialogAdapterAndroid this$0;
			final String val$message;

			public void run()
			{
				Toast.makeText(context, message, 1).show();
			}

			
			{
				this$0 = NewDialogAdapterAndroid.this;
				val$message = message ;
//				Object();
			}
		});
	}

}
