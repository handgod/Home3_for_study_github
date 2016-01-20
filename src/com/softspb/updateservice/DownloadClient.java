// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.softspb.updateservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;

import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;

public abstract class DownloadClient
{
	static class DownloadException extends Exception
	{

		public DownloadException()
		{
		}

		public DownloadException(String s)
		{
			super(s);
		}

		public DownloadException(String s, Throwable throwable)
		{
			super(s, throwable);
		}

		public DownloadException(Throwable throwable)
		{
			super(throwable);
		}
	}


	public static final int COMM_TIMEOUT_MILLIS = 0x1d4c0;
	private static final Object TIMEOUT = new Object();
	protected Logger logger;
	private int reqCount;
	private int serverCount;
	protected String serverUrls[];
	private final Object stateChangedMonitor = new Object();
	private boolean stopFlag;

	public DownloadClient(String as[])
	{
		stopFlag = false;
		reqCount = 0;
		logger = Loggers.getLogger(getClass().getName());
		serverUrls = as;
		serverCount = as.length;
	}

	private Object download(final HttpClient httpClient, Object obj, String s)
	{
		final HttpGet req;
		final Object res[];
		final String final_s;
		final Object param = null;
		logger.d((new StringBuilder()).append("download >>> param=").append(obj).append(" serverUri=").append(s).toString());
		req = new HttpGet(createUrl(s, obj));
		res = new Object[2];
		StringBuilder stringbuilder = (new StringBuilder()).append("DownloadClient-Request-");
		int i = 1 + reqCount;
		reqCount = i;
		final_s = stringbuilder.append(i).toString();
		res[1] = TIMEOUT;
		(new Thread() {

			final DownloadClient this$0;
			final HttpClient val$httpClient;
			final Object val$param;
			final HttpGet val$req;
			final Object val$res[];
			
			public void run()
			{
				try
				{
					res[0] = sendRequest(httpClient, req, param);
				}
				catch (Exception exception3)
				{
					logger.e((new StringBuilder()).append("Error occurred while processing HTTP request: ").append(exception3).toString(), exception3);
				}
				synchronized (stateChangedMonitor)
				{
					res[1] = null;
					stateChangedMonitor.notify();
				}
				return;
				
			}

			
			{
//				super(final_s);
			
				this$0 = DownloadClient.this;
				val$res = res ;
				val$httpClient=httpClient;
				val$req =req ;

				val$param=param;
			}
		}).start();
		synchronized (stateChangedMonitor)
		{
			try {
				stateChangedMonitor.wait(0x1d4c0L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (res[1] == TIMEOUT)
			logger.w("TIMEOUT expired, aborting request...");
		if (req != null)
			req.abort();

		return res[0];
		
	}


	  private Object sendRequest(HttpClient paramHttpClient, HttpGet paramHttpGet, Object paramParamType)
	    throws Exception
	  {
		 Object localObject2 = null ;
	    HttpEntity localHttpEntity = null;
	    HttpResponse localHttpResponse;
	    try
	    {
	      Logger localLogger1 = this.logger;
	      String str1 = "sendRequest: using httpClient=" + paramHttpClient;
	      localLogger1.d(str1);
	      Logger localLogger2 = this.logger;
	      StringBuilder localStringBuilder1 = new StringBuilder().append("sendRequest: Sending HTTP request: ");
	      URI localURI = paramHttpGet.getURI();

	      localHttpResponse = paramHttpClient.execute(paramHttpGet);
	      StatusLine localStatusLine = localHttpResponse.getStatusLine();
	      if (localStatusLine.getStatusCode() != 200)
	      {
	        StringBuilder localStringBuilder2 = new StringBuilder().append("sendRequest: Received error HTTP status code: ");
	        int i = localStatusLine.getStatusCode();
	      }
	    }
	    finally
	    {
	      if (localHttpEntity == null);
	    }
	    try
	    {
	      localHttpEntity.consumeContent();

	      this.logger.d("sendRequest: Received status OK");
	      localHttpEntity = localHttpResponse.getEntity();
	      if (localHttpEntity == null)
	        throw new DownloadException();
	      InputStream localInputStream = localHttpEntity.getContent();
	      this.logger.d("sendRequest: Parsing the received content...");
	      localObject2 = parseResponse(localInputStream, paramParamType);
	      if (localObject2 == null)
	        throw new DownloadException();
	      if (localHttpEntity != null);
	      try
	      {
	        localHttpEntity.consumeContent();
	         return localObject2;
	      }
	      catch (IOException localIOException1)
	      {
	       
	      }
	    }
	    catch (IOException localIOException2)
	    {
	     
	    }
	    return localObject2;
	  }
	 

	public void abort()
	{
		stopFlag = true;
	}

	protected abstract String createUrl(String s, Object obj);

	public Object download(Object paramParamType)
	  {
	    Logger localLogger = this.logger;
	    String str1 = "Attempting to download data for param=" + paramParamType;
	    localLogger.d(str1);
	    int i = this.serverCount;
	    String[] arrayOfString = this.serverUrls;
	    Object localObject1 = null;
	    this.stopFlag = false;
	    HttpClient localHttpClient = obtainHttpClient();
	    int j = 0;
	    while (true)
	    {
	      if (j < i);
	      try
	      {
	        if (!this.stopFlag)
	        {
	          String str2 = arrayOfString[j];
	          Object localObject2 = download(localHttpClient, paramParamType, str2);
	          localObject1 = localObject2;
	          if (localObject1 == null);
	        }
	        else
	        {
	          return localObject1;
	        }
	        j += 1;
	      }
	      finally
	      {
	        releaseHttpClient(localHttpClient);
	      }
	    }
	  }
	
	protected HttpClient obtainHttpClient()
	{
		return new DefaultHttpClient();
	}

	protected abstract Object parseResponse(InputStream inputstream, Object obj)
		throws Exception;

	protected void releaseHttpClient(HttpClient httpclient)
	{
		ClientConnectionManager clientconnectionmanager = httpclient.getConnectionManager();
		if (clientconnectionmanager != null)
			clientconnectionmanager.shutdown();
	}



}
