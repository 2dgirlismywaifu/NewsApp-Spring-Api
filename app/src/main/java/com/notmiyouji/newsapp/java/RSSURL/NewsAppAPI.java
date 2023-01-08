package com.notmiyouji.newsapp.java.RSSURL;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsAppAPI {
    public static final String BASE_URL = "https://newsandroidrest.azurewebsites.net/";
    private static final String NEWS_APP_HEADER = new String(Base64.decode(geNewsAPPHeader(), Base64.DEFAULT));
    private static final String NEWS_APP_KEY = new String(Base64.decode(getNewsAPPKey(), Base64.DEFAULT));
    public static Retrofit retrofit;

    static {
        System.loadLibrary("keys");
    }

    public static native String geNewsAPPHeader();

    public static native String getNewsAPPKey();

    public static Retrofit getAPIClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(getHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static OkHttpClient.Builder getHttpClient() {
        //HTTPS Client
        try {
            // Create a trust manager that does not validate certificate chains
            @SuppressLint("CustomX509TrustManager") final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
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
            //Create Header Authenticator


            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

            builder.hostnameVerifier((hostname, session) -> true);
            builder.addInterceptor(chain -> {
                Request request = chain.request().newBuilder().addHeader(NEWS_APP_HEADER, NEWS_APP_KEY).build();
                return chain.proceed(request);
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
