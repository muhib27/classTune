package com.classtune.app.schoolapp.networking;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SchoolAppPostRequest extends StringRequest {

	public SchoolAppPostRequest(int method, String url,
			Listener<String> listener, ErrorListener errorListener,
			Map<String, String> params) {
		super(method, url, listener, errorListener);
		// TODO Auto-generated constructor stub
		this.mParams = params;
	}

	private Map<String, String> mParams;

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/x-www-form-urlencoded");
		return params;
	}

	public void SetPostParam(String strParam, String strValue) {
		mParams.put(strParam, strValue);
	}

	@Override
	public Map<String, String> getParams() {
		return mParams;
	}
	
	public void disableCaching(){
		this.setShouldCache(false);
	}
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				SchoolAppPostRequest.parseIgnoreCacheHeaders(response));
	}

	public static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
		long now = System.currentTimeMillis();

		Map<String, String> headers = response.headers;
		long serverDate = 0;
		String serverEtag = null;
		String headerValue;

		headerValue = headers.get("Date");
		if (headerValue != null) {
			serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
		}

		serverEtag = headers.get("ETag");

		final long cacheHitButRefreshed = 1 * 60 * 60 * 1000; // in 3 minutes cache
															// will be hit, but
															// also refreshed on
															// background
		final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache
														// entry expires
														// completely
		final long softExpire = now + cacheHitButRefreshed;
		final long ttl = now + cacheExpired;

		Cache.Entry entry = new Cache.Entry();
		entry.data = response.data;
		entry.etag = serverEtag;
		entry.softTtl = softExpire;
		entry.ttl = ttl;
		entry.serverDate = serverDate;
		entry.responseHeaders = headers;
		

		return entry;
	}

	@Override
	public String getCacheKey() {
		String temp = super.getCacheKey();
		for (Map.Entry<String, String> entry : mParams.entrySet())
			temp += entry.getKey() + "=" + entry.getValue();
		return temp;
	}

}
