package com.classtune.app.schoolapp.networking;


import android.content.Context;
import android.util.Log;

import com.classtune.app.schoolapp.utils.ApplicationSingleton;
import com.classtune.app.schoolapp.utils.URLHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;

public class AppRestClient {
	

	private static final int DEFAULT_TIMEOUT = 100 * 1000;

	private static AsyncHttpClient client;

	public static boolean isObjectDeleted()
	{
		if(client!=null)
			return false;
		else
			return true;
	}
	
	public static void init()
	  {
		  client=new AsyncHttpClient();
		  setTimeOutTime();
		  setCookiesStore();
	  }
	
	private static void setCookiesStore()
	  {
		  PersistentCookieStore myCookieStore = new PersistentCookieStore(ApplicationSingleton.getInstance());
		  client.setCookieStore(myCookieStore);
	  }
	 
	public static void setTimeOutTime() {
		client.setTimeout(DEFAULT_TIMEOUT);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		if(isObjectDeleted())
			init();
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		if(isObjectDeleted())
			init();
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void postCmart(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		if(isObjectDeleted())
			init();
		client.post(url, params, responseHandler);
	}
	
	public static void postFreeUser(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		if(isObjectDeleted())
			init();
		client.post(getFreeUserAbsoluteUrl(url), params, responseHandler);
	}
	
	private static String getFreeUserAbsoluteUrl(String relativeUrl) {
			return URLHelper.URL_BASE + relativeUrl;
	}

	public static String getAbsoluteUrl(String relativeUrl) {
		
		Log.e("URL", URLHelper.URL_BASE + relativeUrl);
		return URLHelper.URL_BASE + relativeUrl;
	}

	public static void put(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler)
	{
		
		client.put(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void post(Context con, String url, HttpEntity entity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		if(isObjectDeleted())
			init();
		client.post(con, getAbsoluteUrl(url), entity, contentType,
				responseHandler);
	}
}
