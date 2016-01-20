// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.*;
import android.widget.*;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;

// Referenced classes of package com.softspb.shell.adapters.dialog:
//			IShellDialog

public class ShellDialog extends IShellDialog
	implements android.content.DialogInterface.OnCancelListener, android.content.DialogInterface.OnDismissListener, android.content.DialogInterface.OnClickListener
{
	class SpinnerListener implements android.widget.AdapterView.OnItemSelectedListener
	{

		private final String key;
		final ShellDialog this$0;

		public void onItemSelected(AdapterView adapterview, View view, int i, long l)
		{
			Object obj = setValueMonitor;			
			synchronized (obj) {
				if (!key.equals(settingValueKey))
				{
					logd((new StringBuilder()).append("post notify onValueChanged: key=").append(key).toString());
					notifyOnValueChanged(key);
				}
			}
			return;
		}

		public void onNothingSelected(AdapterView adapterview)
		{
		}

		SpinnerListener(String s)
		{
			this$0 = ShellDialog.this;
//			Object();
			key = s;
		}
	}

	class CheckBoxListener	implements android.widget.CompoundButton.OnCheckedChangeListener
	{

		private final String key;
		final ShellDialog this$0;

		public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
		{
			Object obj = setValueMonitor;
		
			synchronized (obj) {
			if (!key.equals(settingValueKey))
			{
				logd((new StringBuilder()).append("post notify onValueChanged: key=").append(key).toString());
				notifyOnValueChanged(key);
			}
			}
			
			return;
		}

		CheckBoxListener(String s)
		{
			this$0 = ShellDialog.this;
//			Object();
			key = s;
		}
	}

	class TextChangedListener
		implements TextWatcher
	{

		private final String key;
		final ShellDialog this$0;

		public void afterTextChanged(Editable editable)
		{
			Object obj = setValueMonitor;
			
			synchronized (obj) {
				if (!ShellDialog.stripEOLs(editable) && !key.equals(settingValueKey))
				{
					logd((new StringBuilder()).append("post notify onValueChanged: key=").append(key).toString());
					notifyOnValueChanged(key);
				}
			}
			
			return;
		}

		public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
		{
		}

		public void onTextChanged(CharSequence charsequence, int i, int j, int k)
		{
		}

		TextChangedListener(String s)
		{
			this$0 = ShellDialog.this;
//			Object();
			key = s;
		}
	}

	class UIHandler extends IShellDialog.UIHandler
	{

		private static final int MSG_SET_CHECK_BOX_VALUE = 3;
		private static final int MSG_SET_COMBO_BOX_VALUE = 5;
		private static final int MSG_SET_FOCUS = 6;
		private static final int MSG_SET_INPUT_ENABLED = 4;
		private static final int MSG_SET_TEXT_VALUE = 2;
		final ShellDialog this$0;

		private void setCheckBoxValue(String s, boolean flag)
		{
			Object obj = setValueMonitor;

			synchronized (obj) {
				settingValueKey = s;
				logd((new StringBuilder()).append("setCheckBoxValue >>> key=").append(s).append(" isChecked=").append(flag).toString());
				((CheckBox)inputs.get(s)).setChecked(flag);
				logd("setCheckBoxValue <<<");
				settingValueKey = null;
			}
			return;
		}

		private void setComboBoxValue(String s, int i)
		{
			Object obj = setValueMonitor;

			synchronized (obj) {
				settingValueKey = s;
				logd((new StringBuilder()).append("setComboBoxValue >>> key=").append(s).append(" value=").append(i).toString());
				((Spinner)inputs.get(s)).setSelection(i);
				logd("setComboBoxValue <<<");
				settingValueKey = null;
			}
			
			return;
		}

		private void setFocus(String s)
		{
			logd((new StringBuilder()).append("setFocus >>> key=").append(s).toString());
			((View)inputs.get(s)).requestFocus();
			logd("setFocus <<<");
		}

		private void setInputEnabled(String s, boolean flag)
		{
			logd((new StringBuilder()).append("setInputEnabled >>> key=").append(s).append(" isEnabled=").append(flag).toString());
			((View)inputs.get(s)).setEnabled(flag);
			logd("setInputEnabled <<<");
		}

		private void setTextValue(String s, String s1)
		{
			Object obj = setValueMonitor;			
			synchronized (obj) {
				settingValueKey = s;
				logd((new StringBuilder()).append("setTextValue >>> key=").append(s).append(" value=").append(s1).toString());
				EditText edittext = (EditText)inputs.get(s);
				edittext.setText(s1);
				edittext.setSelection(s1.length());
				logd("setTextValue <<<");
				settingValueKey = null;
			}
			return;
		}

		public void handleMessage(Message message)
		{
			boolean flag = true;
			switch (message.what) {
			case 2:
				Pair pair = (Pair)message.obj;
				setTextValue((String)pair.first, (String)pair.second);
				break;
			case 3:
				String s1 = (String)message.obj;
				if (message.arg1 == 0)
					flag = false;
				setCheckBoxValue(s1, flag);
				break;
			case 4:
				String s = (String)message.obj;
				if (message.arg1 == 0)
					flag = false;
				setInputEnabled(s, flag);
				break;
			case 5:
				setComboBoxValue((String)message.obj, message.arg1);
				break;
			case 6:
				setFocus((String)message.obj);
				break;
			default:
				super.handleMessage(message);
				break;
			}

			return;
			

		}

		void postSetCheckBoxValue(String s, boolean flag)
		{
			int i;
			if (flag)
				i = 1;
			else
				i = 0;
			sendMessage(Message.obtain(this, 3, i, 0, s));
		}

		void postSetComboBoxValue(String s, int i)
		{
			sendMessage(Message.obtain(this, 5, i, 0, s));
		}

		void postSetFocus(String s)
		{
			sendMessage(Message.obtain(this, 6, s));
		}

		void postSetInputEnabled(String s, boolean flag)
		{
			int i;
			if (flag)
				i = 1;
			else
				i = 0;
			sendMessage(Message.obtain(this, 4, i, 0, s));
		}

		void postSetTextValue(String s, String s1)
		{
			sendMessage(Message.obtain(this, 2, new Pair(s, s1)));
		}

		protected void show()
		{
			logd("show");
			dialogBuilder.setCancelable(true);
			dialogBuilder.setPositiveButton(0x104000a, ShellDialog.this);
			dialogBuilder.setOnCancelListener(ShellDialog.this);
			dialog = dialogBuilder.create();
			dialogBuilder = null;
			dialog.setOnDismissListener(ShellDialog.this);
			dialog.setInverseBackgroundForced(true);
			dialog.show();
		}

		UIHandler(Looper looper)
		{
			super(looper);
			this$0 = ShellDialog.this;
		}
	}


	public static final int BUTTON_CANCEL = 8;
	public static final int BUTTON_NO = 4;
	public static final int BUTTON_OK = 1;
	public static final int BUTTON_YES = 2;
	public static final int ICON_DEFAULT = 0;
	public static final int ICON_ERROR = 3;
	public static final int ICON_INFO = 1;
	public static final int ICON_WARNING = 2;
	private ViewGroup contentView;
	private android.app.AlertDialog.Builder dialogBuilder;
	private final HashMap inputs = new HashMap();
	LayoutInflater layoutInflater;
	private int negativeButton;
	private int positiveButton;
	final Object setValueMonitor = new Object();
	String settingValueKey;

	public ShellDialog(Context context, Looper looper, int i, int j)
	{
		super(context, looper, i, j);
		logd((new StringBuilder()).append("Ctor: adapterAddress=0x").append(Integer.toHexString(i)).append(" dialogToken=").append(j).toString());
		layoutInflater = LayoutInflater.from(context);
		dialogBuilder = new android.app.AlertDialog.Builder(context);
	}

	private ViewGroup getContentView()
	{
		if (contentView == null)
		{
			contentView = (ViewGroup)layoutInflater.inflate(com.spb.shell3d.R.layout.dialog_content_view, null);
			dialogBuilder.setView(contentView);
		}
		return contentView;
	}

	private native void onButtonClicked(int i, int j, int k);

	private native void onDismiss(int i, int j);

	private native void onValueChanged(int i, int j, String s);

	private static boolean stripEOLs(Editable editable)
	{
		boolean flag = false;
		int i = editable.length();
		char ac[] = new char[i];
		editable.getChars(0, i, ac, 0);
		for (int j = 0; j < i; j++)
			if (ac[j] == '\n')
			{
				flag = true;
				ac[j] = ' ';
			}

		if (flag)
			editable.replace(0, i, CharBuffer.wrap(ac));
		return flag;
	}

	public void addCheckBox(String s, String s1)
	{
		logd((new StringBuilder()).append("addCheckBox >>> key=").append(s).append(" title=").append(s1).toString());
		ViewGroup viewgroup = getContentView();
		CheckBox checkbox = (CheckBox)layoutInflater.inflate(com.spb.shell3d.R.layout.dialog_check_box, viewgroup, false);
		checkbox.setText(s1);
		checkbox.setOnCheckedChangeListener(new CheckBoxListener(s));
		viewgroup.addView(checkbox);
		inputs.put(s, checkbox);
		logd("addCheckBox <<<");
	}

	public void addComboBox(String s, String as[], int i)
	{
		logd((new StringBuilder()).append("addComboBox >>> key=").append(s).append(" choices=").append(Arrays.toString(as)).append(" initialChoice=").append(i).toString());
		ViewGroup viewgroup = getContentView();
		Spinner spinner = (Spinner)layoutInflater.inflate(com.spb.shell3d.R.layout.dialog_spinner, viewgroup, false);
		ArrayAdapter arrayadapter = new ArrayAdapter(context, 0x1090008, as);
		arrayadapter.setDropDownViewResource(0x1090009);
		spinner.setAdapter(arrayadapter);
		spinner.setSelection(i);
		viewgroup.addView(spinner);
		spinner.setOnItemSelectedListener(new SpinnerListener(s));
		inputs.put(s, spinner);
		logd("addComboBox <<<");
	}

	public void addTextInput(String s)
	{
		logd((new StringBuilder()).append("addTextInput >>> key=").append(s).toString());
		ViewGroup viewgroup = getContentView();
		EditText edittext = (EditText)layoutInflater.inflate(com.spb.shell3d.R.layout.dialog_text_input, viewgroup, false);
		edittext.addTextChangedListener(new TextChangedListener(s));
		viewgroup.addView(edittext);
		inputs.put(s, edittext);
		logd("addTextInput <<<");
	}

	protected IShellDialog.UIHandler createUIHandler(Looper looper)
	{
		return new UIHandler(looper);
	}

	public void dismiss()
	{
		super.dismiss();
	}

	public boolean getCheckBoxValue(String s)
	{
		logd((new StringBuilder()).append("getCheckBoxValue >>> key=").append(s).toString());
		boolean flag = ((CheckBox)inputs.get(s)).isChecked();
		logd((new StringBuilder()).append("getCheckBoxValue <<< return ").append(flag).toString());
		return flag;
	}

	public int getComboBoxValue(String s)
	{
		logd((new StringBuilder()).append("getComboBoxValue >>> key=").append(s).toString());
		int i = ((Spinner)inputs.get(s)).getSelectedItemPosition();
		logd((new StringBuilder()).append("getComboBoxValue <<< return ").append(i).toString());
		return i;
	}

	public String getTextValue(String s)
	{
		logd((new StringBuilder()).append("getTextValue >>> key=").append(s).toString());
		String s1 = ((EditText)inputs.get(s)).getText().toString();
		logd((new StringBuilder()).append("getTextValue <<< return ").append(s1).toString());
		return s1;
	}

	void notifyOnValueChanged(String s)
	{
		onValueChanged(adapterAddress, dialogToken, s);
	}

	public void onCancel(DialogInterface dialoginterface)
	{
		logd("onCancel");
	}

	public void onClick(DialogInterface dialoginterface, int i)
	{
		logd((new StringBuilder()).append("onClick: which=").append(i).toString());
		if (i != -1) 
		{
			if (i == -2)
				onButtonClicked(adapterAddress, dialogToken, negativeButton);
		}
		else
		{
			onButtonClicked(adapterAddress, dialogToken, positiveButton);
		}
		return;
	}

	public void onDismiss(DialogInterface dialoginterface)
	{
		logd("onDismiss");
		onDismiss(adapterAddress, dialogToken);
	}

	public void setButtons(int i)
	{
		if ((i & 2) != 0)
		{
			dialogBuilder.setPositiveButton(0x1040013, this);
			positiveButton = 2;
		}
		if ((i & 4) != 0)
		{
			dialogBuilder.setNegativeButton(0x1040009, this);
			positiveButton = 4;
		}
		if ((i & 1) != 0)
		{
			dialogBuilder.setPositiveButton(0x104000a, this);
			positiveButton = 1;
		}
		if ((i & 8) != 0)
		{
			dialogBuilder.setNegativeButton(0x1040000, this);
			negativeButton = 8;
		}
	}

	public void setCheckBoxValue(String s, boolean flag)
	{
		((UIHandler)uiHandler).postSetCheckBoxValue(s, flag);
	}

	public void setComboBoxValue(String s, int i)
	{
		((UIHandler)uiHandler).postSetComboBoxValue(s, i);
	}

	public void setFocus(String s)
	{
		((UIHandler)uiHandler).postSetFocus(s);
	}

	public void setIcon(int i)
	{
		int j =0;
		logd((new StringBuilder()).append("setIcon: icon=").append(i).toString());
		switch (i) {
		case 0:
		case 1:
			j = 0x108009b;
			break;
		case 2:	
		case 3:
			j = 0x1080027;
			break;
		default:
			throw new IllegalArgumentException((new StringBuilder()).append("Illegal icon: ").append(i).toString());
		}
		dialogBuilder.setIcon(j);
		return;
	}

	public void setInputEnabled(String s, boolean flag)
	{
		((UIHandler)uiHandler).postSetInputEnabled(s, flag);
	}

	public void setMessage(String s)
	{
		logd((new StringBuilder()).append("setMessage: ").append(s).toString());
		dialogBuilder.setMessage(s);
	}

	public void setTextValue(String s, String s1)
	{
		((UIHandler)uiHandler).postSetTextValue(s, s1);
	}

	public void setTitle(String s)
	{
		logd((new StringBuilder()).append("setTitle: title=").append(s).toString());
		dialogBuilder.setTitle(s);
	}

	public void show()
	{
		super.show();
	}



/*
	static android.app.AlertDialog.Builder access$002(ShellDialog shelldialog, android.app.AlertDialog.Builder builder)
	{
		shelldialog.dialogBuilder = builder;
		return builder;
	}

*/


}
