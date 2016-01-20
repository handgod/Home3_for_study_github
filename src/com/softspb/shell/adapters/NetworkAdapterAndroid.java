package com.softspb.shell.adapters;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.softspb.shell.opengl.NativeCallbacks;
import com.spb.programlist.ProgramsUtil;

public class NetworkAdapterAndroid extends NetworkAdapter
{
  private static final boolean DEBUG = false;
  private Context context;
  private Thread receiverThread;
  private Thread receiverThreadTcp;
  private Runnable runnable;
  private Runnable runnableTcp;

  class NetworkAdapterAndroid$1  implements Runnable
  { 
	  public void run()
	  {
		try
		{
			DatagramSocket datagramsocket = new DatagramSocket(18093);
			byte abyte0[] = new byte[65535];
			do
			{
				DatagramPacket datagrampacket = new DatagramPacket(abyte0, abyte0.length);
				datagramsocket.receive(datagrampacket);
				NetworkAdapterAndroid.onCmd(new String(datagrampacket.getData(), datagrampacket.getOffset(), datagrampacket.getLength()));
			} while (true);
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	 }
  }
  class NetworkAdapterAndroid$2
  implements Runnable
{
  public void run()
  {
    while (true)
    {
      char[] arrayOfChar;
      StringBuilder localStringBuilder1;
      int i;
      try
      {
        ServerSocket localServerSocket = new ServerSocket(18093);
        arrayOfChar = new char[32000];
        Socket localSocket = localServerSocket.accept();
        InputStream localInputStream = localSocket.getInputStream();
        InputStreamReader localInputStreamReader = new InputStreamReader(localInputStream);
        BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
        OutputStream localOutputStream = localSocket.getOutputStream();
        PrintWriter localPrintWriter = new PrintWriter(localOutputStream);
        localStringBuilder1 = new StringBuilder();
        i = localBufferedReader.read(arrayOfChar);
        if (i <= 0)
          continue;
        if (i > 0)
        {
          int j = i + -1;
          if (arrayOfChar[j] == 0)
          {
            int k = i + -1;
            StringBuilder localStringBuilder2 = localStringBuilder1.append(arrayOfChar, 0, k);
            String str = NetworkAdapterAndroid.onCmd(localStringBuilder1.toString());
            if ((str == null) || (str.length() <= 0))
              continue;
            localPrintWriter.println(str);
            localPrintWriter.flush();
            localSocket.close();
            continue;
          }
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        return;
      }
      int m = 0;
      StringBuilder localStringBuilder3 = localStringBuilder1.append(arrayOfChar, m, i);
    }
  }
}
  public NetworkAdapterAndroid(AdaptersHolder paramAdaptersHolder)
  {
    super(paramAdaptersHolder);
    init();
  }

  private boolean haveUIBuilder()
  {
	  boolean flag = false;
    try
    {
      InputStream localInputStream = this.context.getAssets().open("skins/skins/uibuilder.dat");
      if (localInputStream != null)
      {
    	  localInputStream.close();
  		  flag = true;
      }
      return flag;
    }
    catch (Throwable localThrowable)
    {
    	 return flag;
    }
  }
	
  private void init()
  {
    if (this.runnable == null)
    {
      NetworkAdapterAndroid$1 local1 = new NetworkAdapterAndroid$1();
      this.runnable = local1;
    }
    if (this.runnableTcp == null)
    {
      NetworkAdapterAndroid$2 local2 = new NetworkAdapterAndroid$2();
      this.runnableTcp = local2;
    }
  }

  private static void logd(String paramString)
  {
  }

  public static native String onCmd(String paramString);

  protected void onCreate(Context paramContext, NativeCallbacks paramNativeCallbacks)
  {
    this.context = paramContext;
    startServer();
  }

  protected void onDestroy(Context paramContext)
  {
    stopServer();
  }
public void openBrowser(String s)
{
	if (s != null)
	{
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setData(Uri.parse(s));
		ProgramsUtil.startActivitySafely(context, intent);
	}
}

  public void startServer()
  {
    if (haveUIBuilder())
    {
      Runnable localRunnable1 = this.runnable;
      Thread localThread1 = new Thread(localRunnable1);
      this.receiverThread = localThread1;
      this.receiverThread.start();
      Runnable localRunnable2 = this.runnableTcp;
      Thread localThread2 = new Thread(localRunnable2);
      this.receiverThreadTcp = localThread2;
      this.receiverThreadTcp.start();
    }
  }

  public void stopServer()
  {
    if (haveUIBuilder())
    {
      this.receiverThread.interrupt();
      this.receiverThreadTcp.interrupt();
    }
  }
}

/* Location:           D:\MyEclipse10\Home3\classes.dex.dex2jar.jar
 * Qualified Name:     com.softspb.shell.adapters.NetworkAdapterAndroid
 * JD-Core Version:    0.6.0
 */