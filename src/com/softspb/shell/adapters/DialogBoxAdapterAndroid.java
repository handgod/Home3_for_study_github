// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.shell.adapters;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.softspb.shell.opengl.NativeCallbacks;
import com.softspb.util.CollectionFactory;
import com.spb.shell3d.R;

// Referenced classes of package com.softspb.shell.adapters:
//			DialogBoxAdapter, AdaptersHolder

public class DialogBoxAdapterAndroid extends DialogBoxAdapter
{
	protected  class DialogBoxInstance
		implements android.content.DialogInterface.OnClickListener, android.content.DialogInterface.OnDismissListener, android.content.DialogInterface.OnCancelListener, android.widget.CompoundButton.OnCheckedChangeListener, TextWatcher
	{

		static  final boolean $assertionsDisabled =false;
		private final int buttons;
		private final int checkboxFlags[];
		private final int checkboxIds[];
		private final String checkboxTitles[];
		private CheckBox checkboxes[];
		private boolean closed;
		private final String defaultValue;
		private AlertDialog dialog;
		private EditText editView;
		private final boolean forbidEmpty;
		private final String greyedText;
		private final int icon;
		private final boolean isInput;
		private final int nativeId;
		private final String text;
		final DialogBoxAdapterAndroid this$0;
		private final String title;

		private boolean isInputDisabled()
		{
			boolean flag = false;
			int i = 0;
			do
			{
label0:
				{
					if (i < checkboxes.length)
					{
						if (!checkboxes[i].isChecked() || (1 & checkboxFlags[i]) == 0)
							break label0;
						flag = true;
					}
					return flag;
				}
				i++;
			} while (true);
		}

		private void updateButtonState()
		{
			if (forbidEmpty && dialog != null && dialog.getButton(-1) != null)
			{
				boolean flag;
				if (isInputDisabled() || !editView.getText().toString().equals(""))
					flag = true;
				else
					flag = false;
				dialog.getButton(-1).setEnabled(flag);
			}
		}

		private void updateCheckState()
		{
			boolean flag = false;
			boolean flag1 = isInputDisabled();
			if (flag1 && editView.isEnabled())
			{
				editView.setTag(editView.getText().toString());
				editView.setText(greyedText);
			}
			if (!flag1 && !editView.isEnabled())
			{
				String s = (String)editView.getTag();
				editView.setText(s);
			}
			if (flag1)
				((InputMethodManager)context.getSystemService("input_method")).hideSoftInputFromWindow(editView.getWindowToken(), 0);
			EditText edittext = editView;
			if (!flag1)
				flag = true;
			edittext.setEnabled(flag);
		}

		public void afterTextChanged(Editable editable)
		{
		}

		public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
		{
		}

		public void onCancel(DialogInterface dialoginterface)
		{
			if (!$assertionsDisabled && (8 & buttons) == 0)
			{
				throw new AssertionError();
			} else
			{
				sendDialogResult(8);
				return;
			}
		}

		public void onCheckedChanged(CompoundButton compoundbutton, boolean flag)
		{
			updateCheckState();
		}

		public void onClick(DialogInterface dialoginterface, int i)
		{
			byte byte0;
			if (i == -2 || i == -1) 
			{
				byte0 = 8;
				switch (i)
				{
			

				case -1: 
					break; /* Loop/switch isn't completed */

				case -2: 
					break;
					
				default:
					break;
				}
				sendDialogResult(byte0);
			}
			else
			{
				return;
			}
			
			if ((2 & buttons) != 0)
				byte0 = 2;
			else
				byte0 = 1;
			
			sendDialogResult(byte0);
			
			if ((8 & buttons) != 0)
				byte0 = 8;
			else
				byte0 = 4;
			sendDialogResult(byte0);
		}

		public void onDismiss(DialogInterface dialoginterface)
		{
			closed = true;
			list.remove(this);
			dialog = null;
		}

		public void onTextChanged(CharSequence charsequence, int i, int j, int k)
		{
			updateButtonState();
		}

		public void sendDialogResult(int i)
		{
			list.remove(this);
			if (!closed)
			{
				String s = "";
				int j;
				if (isInput)
				{
					if (!$assertionsDisabled && editView == null)
						throw new AssertionError();
					int k;
					if (editView.isEnabled())
					{
						s = editView.getText().toString();
					} else
					{
						String s1 = (String)editView.getTag();
						if (s1 != null)
							s = s1;
						else
							s = "";
					}
				}
				j = 0;
				for (int k = 0; k < checkboxes.length; k++)
					if (checkboxes[k].isChecked())
						j |= 1 << checkboxIds[k];

				closed = true;
				onDialogResult(nativeId, i, s, j);
			}
		}
		class DialogBoxAdapterAndroid$DialogBoxInstance$1
		  implements InputFilter
		{
		  public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
		  {
		    Object localObject;
		   
		    {
		     
		      if (paramCharSequence.length() < 1)
		      {
		        localObject = paramSpanned.subSequence(paramInt3, paramInt4);
		        return (CharSequence)localObject;
		      }
		      localObject = "";
		    }
		    return (CharSequence)localObject;
		  }
		}
		void show()
	    {
	      int i = 0;
	      assert (this.dialog == null);
	      Context localContext = DialogBoxAdapterAndroid.this.context;
	      AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localContext);
	      int j = 0;
	      View localView = null;
	      EditText localEditText5;
	      switch (this.icon)
	      {
	      default:
	        String str1 = this.title;
	        AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(str1);
	        if (j != 0)
	        	localBuilder1.setIcon(j);
	        LayoutInflater localLayoutInflater = LayoutInflater.from(DialogBoxAdapterAndroid.this.context);
	        int k = R.layout.generic_dialog_box;
	        localView = localLayoutInflater.inflate(k, null);
	        int m = R.id.dialog_box_prompt;
	        TextView localTextView = (TextView)localView.findViewById(m);
	        String str2 = this.text;
	        localTextView.setText(str2);
	        if (!this.text.equals(""))
	          break;
	        int n = 8;
	        localTextView.setVisibility(n);
	        int i2 = R.id.dialog_box_input;
	        EditText localEditText1 = (EditText)localView.findViewById(i2);
	        this.editView = localEditText1;
	        EditText localEditText2 = this.editView;
	        String str3 = this.defaultValue;
	        localEditText2.setText(str3);
	        EditText localEditText3 = this.editView;
	        InputFilter[] arrayOfInputFilter = new InputFilter[1];
	        DialogBoxAdapterAndroid$DialogBoxInstance$1 local1 = new DialogBoxAdapterAndroid$DialogBoxInstance$1();
	        arrayOfInputFilter[0] = local1;
	        localEditText3.setFilters(arrayOfInputFilter);
	        EditText localEditText4 = this.editView;
	        int i3 = this.editView.getText().length();
	        localEditText4.setSelection(i3);
	        this.editView.addTextChangedListener(this);
	        localEditText5 = this.editView;
	        if (this.isInput);
	      case 1:
	      case 2:
	      case 3:
	      }
//	      for (int n = 8; ; n = 0)
//	      {
//	        localEditText5.setVisibility(n);
//	        if ($assertionsDisabled)
//	          break label381;
//	        int i4 = this.checkboxIds.length;
//	        int i5 = DialogBoxAdapterAndroid.checkboxViewIds.length;
//	        if (i4 < i5)
//	          break label381;
//	        throw new AssertionError();
//	        j = 17301659;
//	        break;
//	        j = 17301543;
//	        break;
//	        n = 0;
//	        break label176;
//	      }
//	      label381: //CheckBox[] arrayOfCheckBox = new CheckBox[this.checkboxIds.length];
//	      this.checkboxes = new CheckBox[this.checkboxIds.length];
	      int i6 = 0;
	      int i7 = this.checkboxIds.length;
	      CheckBox localCheckBox;
	      int i1;
	      if (i6 < i7)
	      {
	        int i8 = DialogBoxAdapterAndroid.checkboxViewIds[i6];
	        localCheckBox = (CheckBox)localView.findViewById(i8);
	        if ((this.checkboxFlags[i6] & 0x2) != 0);
	        for (int n = 1; ; i1 = 0)
	        {
	          localCheckBox.setChecked(true);
	          String str4 = this.checkboxTitles[i6];
	          localCheckBox.setText(str4);
	          localCheckBox.setOnCheckedChangeListener(this);
	          this.checkboxes[i6] = localCheckBox;
	          i6 += 1;
	          break;
	        }
	      }
	      i6 = 0;
	      int i9 = DialogBoxAdapterAndroid.checkboxViewIds.length;
	      if (i6 < i9)
	      {
	        int i10 = DialogBoxAdapterAndroid.checkboxViewIds[i6];
	        localCheckBox = (CheckBox)localView.findViewById(i10);
	        int i11 = this.checkboxIds.length;
	        if (i6 >= i11);
	        for (i1 = 8; ; i1 = 0)
	        {
	          localCheckBox.setVisibility(i1);
	          i6 += 1;
	          break;
	        }
	      }
	      AlertDialog.Builder localBuilder4 = localBuilder1.setView(localView);
	      if ((this.buttons & 0xC) != 0)
	        i = 1;
	      boolean flg;
	      if(i == i)
	      {
	    	  flg =true;
	      }
	      else
	      {
	    	  flg = false;
	      }
	      AlertDialog.Builder localBuilder5 = localBuilder1.setCancelable(flg);
	      if ((this.buttons & 0x2) != 0)
	      {
	        int i12 = R.string.yes;
	        AlertDialog.Builder localBuilder6 = localBuilder1.setPositiveButton(i12, this);
	      }
	      if ((this.buttons & 0x1) != 0)
	        localBuilder1.setPositiveButton(17039370, this);
	      if ((this.buttons & 0x8) != 0)
	        localBuilder1.setNegativeButton(17039360, this);
	      if ((this.buttons & 0x4) != 0)
	        localBuilder1.setNegativeButton(17039369, this);
	      AlertDialog localAlertDialog = localBuilder1.create();
	      this.dialog = localAlertDialog;
	      this.dialog.setInverseBackgroundForced(true);
	      this.dialog.setOnDismissListener(this);
	      this.dialog.setOnCancelListener(this);
	      this.dialog.show();
	      updateCheckState();
	    }
		 
		{
			boolean flag;
			if (!DialogBoxAdapterAndroid.class.desiredAssertionStatus())
				flag = true;
			else
				flag = false;
//			$assertionsDisabled = flag;
		}



		DialogBoxInstance(int i, String s, String s1, String s2, int j, int k, 
				boolean flag, boolean flag1, String s3, int ai[], String as[], int ai1[])
		{
			this$0 = DialogBoxAdapterAndroid.this;
//			super();
			closed = false;
			nativeId = i;
			title = s;
			text = s1;
			defaultValue = s2;
			buttons = j;
			icon = k;
			isInput = flag;
			forbidEmpty = flag1;
			greyedText = s3;
			checkboxIds = ai;
			checkboxTitles = as;
			checkboxFlags = ai1;
			if (!$assertionsDisabled && (ai.length != as.length || ai.length != ai1.length))
				throw new AssertionError();
			else
				return;
		}
	}


	private static final int checkboxViewIds[];
	private Context context;
	private List list;
	private Handler myUiHandler;

	public DialogBoxAdapterAndroid(AdaptersHolder adaptersholder)
	{
		super(adaptersholder);
		list = CollectionFactory.newArrayList();
	}

	public void closeAllDialogs()
	{
		int i = -1 + list.size();
		while (i >= 0) 
		{
			try
			{
				AlertDialog alertdialog = ((DialogBoxInstance)list.get(i)).dialog;
				if (alertdialog != null)
					alertdialog.dismiss();
			}
			catch (Exception exception) { }
			i--;
		}
	}

	protected void onCreate(Context context1, NativeCallbacks nativecallbacks)
	{
		super.onCreate(context1, nativecallbacks);
		context = context1;
	}

	protected void onStartInUIThread()
	{
		myUiHandler = new Handler();
	}

	public boolean startDialogBox(int i, String s, String s1, String s2, int j, int k, boolean flag, 
			boolean flag1, String s3, int ai[], String as[], int ai1[])
	{
		final DialogBoxInstance inst = new DialogBoxInstance(i, s, s1, s2, j, k, flag, flag1, s3, ai, as, ai1);
		list.add(inst);
		myUiHandler.post(new Runnable() {

			final DialogBoxAdapterAndroid this$0;
			final DialogBoxInstance val$inst;

			public void run()
			{
				inst.show();
			}

			
			{
				this$0 = DialogBoxAdapterAndroid.this;
				val$inst = inst;
//				super();
			}
		});
		return true;
	}

	static 
	{
		int ai[] = new int[4];
		ai[0] = com.spb.shell3d.R.id.dialog_box_checkbox01;
		ai[1] = com.spb.shell3d.R.id.dialog_box_checkbox02;
		ai[2] = com.spb.shell3d.R.id.dialog_box_checkbox03;
		ai[3] = com.spb.shell3d.R.id.dialog_box_checkbox04;
		checkboxViewIds = ai;
	}



}
