package com.classtune.app.schoolapp.networking;

import android.content.Context;

import com.classtune.app.schoolapp.utils.URLHelper;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Extreme_Piash on 1/4/2017.
 */

public class ApiClient {
    private static Retrofit sRetrofit;
    private static Retrofit sRetrofitSSL;
    private static Retrofit sRetrofitCatImage;

    private ApiClient() {
    }

    public synchronized static Retrofit getInstance(final Context context) {
        if (sRetrofit == null) {
            createRetrofit(context);
        }
        return sRetrofit;
    }

    public synchronized static Retrofit getInstanceSSL(final Context context) {
        if (sRetrofitSSL == null) {
            createRetrofitSSL(context);
        }
        return sRetrofitSSL;
    }

    public synchronized static Retrofit getInstanceCatImage(final Context context) {
        if (sRetrofitCatImage == null) {
            createRetrofitCatImage(context);
        }
        return sRetrofitCatImage;
    }

    private static void createRetrofitCatImage(final Context context) {


        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(50, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();


        sRetrofitCatImage = new Retrofit.Builder()
                .client(okHttpClient)
                //.client(getUnsafeOkHttpClient(context))
                .baseUrl(URLHelper.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static void createRetrofitSSL(final Context context) {


        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(50, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();


        sRetrofitSSL = new Retrofit.Builder()
                .client(okHttpClient)
                //.client(getUnsafeOkHttpClient(context))
                .baseUrl(URLHelper.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static void createRetrofit(final Context context) {


        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(50, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();


        sRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                //.client(getUnsafeOkHttpClient(context))
                .baseUrl(URLHelper.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient getUnsafeOkHttpClient(final Context context) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
            builder.readTimeout(120, TimeUnit.SECONDS)
                    .cookieJar(cookieJar);

            OkHttpClient okHttpClient = builder.build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
