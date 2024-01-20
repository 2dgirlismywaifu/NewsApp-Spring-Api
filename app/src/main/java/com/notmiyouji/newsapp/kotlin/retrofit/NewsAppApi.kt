/*
 * Copyright By @2dgirlismywaifu (2023) .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.notmiyouji.newsapp.kotlin.retrofit

import android.annotation.SuppressLint
import android.util.Base64
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NewsAppApi {
    init {
        System.loadLibrary("keys")
    }

    private external fun getNewsAppHeader(): String?
    private external fun getNewsAppKey(): String?

    private const val BASE_URL = "https://notelysiaserver.ddns.net:3000/"
    private val newsAppHeader = String(Base64.decode(getNewsAppHeader(), Base64.DEFAULT))
    private val newsAppKey = String(Base64.decode(getNewsAppKey(), Base64.DEFAULT))
    var retrofit: Retrofit? = null
    @JvmStatic
    val apiClient: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    private val httpClient: OkHttpClient.Builder
        get() =//HTTPS Client
            try {
                // Create a trust manager that does not validate certificate chains
                @SuppressLint("CustomX509TrustManager") val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @SuppressLint("TrustAllX509TrustManager")
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                //Create Header Authenticator
                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _: String?, _: SSLSession? -> true }
                builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request().newBuilder().addHeader(
                        newsAppHeader, newsAppKey
                    ).build()
                    chain.proceed(request)
                })
                builder
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
}
