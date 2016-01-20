// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.spb.cities.nearestcity;

import com.softspb.util.log.Logger;
import com.softspb.util.log.Loggers;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class DownloadClient
{

	public static final int COMM_TIMEOUT_MILLIS = 5000;
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
		logger.d((new StringBuilder()).append("download: param=").append(obj).append(" serverUri=").append(s).toString());
		req = new HttpGet(createUrl(s, obj));
		res = new Object[1];
		StringBuilder stringbuilder = (new StringBuilder()).append("DownloadClient-Request-");
		int i = 1 + reqCount;
		reqCount = i;
		final_s = stringbuilder.append(i).toString();
		(new Thread() {

			final DownloadClient this$0;
			final HttpClient val$httpClient;
			final Object val$param;
			final HttpGet val$req;
			final Object val$res[];

			public void run()
			{
				res[0] = sendRequest(httpClient, req, param);
				Object obj2 = stateChangedMonitor;
				synchronized (stateChangedMonitor) {
					stateChangedMonitor.notify();
				}				
				return;
			}

			
			{
//				super(final_s);
				this$0 = DownloadClient.this;
				val$res = res ;
				val$httpClient = httpClient ;
				val$req = req ;
				val$param= param ;
			}
		}).start();
		synchronized (stateChangedMonitor)
		{
			try {
				stateChangedMonitor.wait(5000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (req != null)
			req.abort();
		return res[0];
	}

	private Object sendRequest(HttpClient httpclient, HttpGet httpget, Object obj)
	{
		Object obj1;
		Object obj2 = null;
		HttpEntity httpentity;
		obj1 = null;
		httpentity = null;
		HttpResponse httpresponse = null;
		logger.d((new StringBuilder()).append("sendRequest: using httpClient=").append(httpclient).toString());
		logger.d((new StringBuilder()).append("sendRequest: Sending HTTP request: ").append(httpget.getURI()).toString());
		try {
			httpresponse = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StatusLine statusline = httpresponse.getStatusLine();
		if (statusline.getStatusCode() == 200)
			return obj1;
		logger.w((new StringBuilder()).append("sendRequest: Received error HTTP status code: ").append(statusline.getStatusCode()).toString());


		logger.d("sendRequest: Received status OK");
		httpentity = httpresponse.getEntity();
		if (httpentity == null)
		{	
		logger.d("sendRequest: Received NULL content");
		}
		else if (httpentity != null)
			try
			{
				httpentity.consumeContent();
			}
			catch (IOException ioexception3) { }
	
		InputStream inputstream = null;
		try {
			inputstream = httpentity.getContent();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.d("sendRequest: Parsing the received content...");
		try {
			obj2 = parseResponse(inputstream, obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj1 = obj2;
		return obj1;
	}

	public void abort()
	{
		stopFlag = true;
	}

	protected abstract String createUrl(String s, Object obj);

	public Object download(Object obj)
	{
		int i;
		String as[];
		Object obj1;
		HttpClient httpclient;
		int j;
		logger.d((new StringBuilder()).append("Attempting to download data for param=").append(obj).toString());
		i = serverCount;
		as = serverUrls;
		obj1 = null;
		stopFlag = false;
		httpclient = obtainHttpClient();
		j = 0;
		
		while (j <i)
		{
			Object obj2;
			if (!stopFlag)
			{
				obj2 = download(httpclient, obj, as[j]);
				obj1 = obj2;
			}
			if (obj1 != null)
			{
				releaseHttpClient(httpclient);
				return obj1;
			}
			j++;
		}
		return obj1;
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
