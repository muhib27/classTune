package com.classtune.app.schoolapp.networking;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by BLACK HAT on 23-Jun-16.
 */
public class VolleyRestClient {


    private Context context;
    private RequestQueue mRequestQueue;
    private CloseableHttpClient httpClient;

    public VolleyRestClient(){

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public void post(int method, final Map<String, String> params, final String url, Response.Listener<String> responseListener, Response.ErrorListener errorListener){
        //int method, String url, Listener<String> listener, ErrorListener errorListener

        StringRequest sr = new StringRequest(method, url, responseListener, errorListener) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }

                return Response.success(parsed, parseCacheHeaders(response));
            }

        };

        /*DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(retryPolicy);
        getRequestQueue().getCache().invalidate(url, false);*/
        getRequestQueue().add(sr);

    }


   /* @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }

        //checkSessionCookie(response.headers);
        return Response.success(parsed, parseIgnoreCacheHeaders(response));
    }*/


    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     * Cache-control headers are ignored. SoftTtl == 3 mins, ttl == 24 hours.
     * @param response The network response to parse headers from
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
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

        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
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


    public static Cache.Entry parseCacheHeaders(NetworkResponse response) {
        long now = System.currentTimeMillis();
        Map headers = response.headers;
        long serverDate = 0L;
        long serverExpires = 0L;
        long softExpire = 0L;
        long maxAge = 0L;
        boolean hasCacheControl = false;
        String serverEtag = null;
        String headerValue = (String)headers.get("Date");
        if(headerValue != null) {
            serverDate = parseDateAsEpoch(headerValue);
        }

        headerValue = (String)headers.get("Cache-Control");
        if(headerValue != null) {
            hasCacheControl = true;
            String[] entry = headerValue.split(",");

            for(int i = 0; i < entry.length; ++i) {
                String token = entry[i].trim();
                if(token.equals("no-cache") || token.equals("no-store")) {
                    //return null;
                }

                if(token.startsWith("max-age=")) {
                    try {
                        maxAge = Long.parseLong(token.substring(8));
                    } catch (Exception var19) {
                        ;
                    }
                } else if(token.equals("must-revalidate") || token.equals("proxy-revalidate")) {
                    maxAge = 0L;
                }
            }
        }

        headerValue = (String)headers.get("Expires");
        if(headerValue != null) {
            serverExpires = parseDateAsEpoch(headerValue);
        }

        serverEtag = (String)headers.get("ETag");
        if(hasCacheControl) {
            softExpire = now + maxAge * 1000L;
        } else if(serverDate > 0L && serverExpires >= serverDate) {
            softExpire = now + (serverExpires - serverDate);
        }

        Cache.Entry var20 = new Cache.Entry();
        var20.data = response.data;
        var20.etag = serverEtag;
        var20.ttl = now+72*60*60*1000;
        var20.serverDate = serverDate;
        var20.responseHeaders = headers;
        return var20;
    }

    public static long parseDateAsEpoch(String dateStr) {
        try {
            return DateUtils.parseDate(dateStr).getTime();
        } catch (DateParseException var2) {
            return 0L;
        }
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            httpClient = HttpClients.custom()
                    .setConnectionManager(new PoolingHttpClientConnectionManager())
                    .setDefaultCookieStore(new PersistentCookieStore(context))
                    .build();
            mRequestQueue = Volley.newRequestQueue(context, new HttpClientStack(httpClient));
        }
        return mRequestQueue;
    }

}
