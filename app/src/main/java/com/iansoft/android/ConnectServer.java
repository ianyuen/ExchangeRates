package com.iansoft.android;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.HttpParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.HttpResponse;

import java.io.UnsupportedEncodingException;

import android.app.Activity;

public class ConnectServer {
	private AsyncCaller caller = null;
	private String responseString = null;
	private HttpPost httpPost = null;
	private HttpContext localContext = null;
	private HttpResponse responseHttp = null;
	private DefaultHttpClient httpClient = null;

	public ConnectServer(Activity activity) {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);

		httpClient = new DefaultHttpClient(params);
		localContext = new BasicHttpContext();

		caller = new AsyncCaller();
	}

	public void sendPost(String url, String data) {
        sendPost(url, data, null);
    }

	public void sendPost(final String url, final String data, final String contentType) {
		caller.execute(new Runnable() {
			@Override
			public void run() {
				httpPost = new HttpPost(url);
				httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
				httpPost.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*;q=0.5");
				if (contentType != null) {
					httpPost.setHeader("Content-Type", contentType);
				} else {
					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				}

				try {
					StringEntity entity = new StringEntity(data, "UTF-8");
					httpPost.setEntity(entity);
					httpClient.execute(httpPost, localContext);
				} catch (Exception e) {
					Log.print(e.toString());
				}
			}
		});
	}
}