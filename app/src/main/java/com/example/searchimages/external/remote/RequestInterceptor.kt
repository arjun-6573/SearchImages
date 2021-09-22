package com.example.searchimages.external.remote

import com.example.searchimages.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY).build()
        val requestBuilder = request()
            .newBuilder()
        requestBuilder.method(request().method, request().body)
            .url(url)
        proceed(requestBuilder.build())
    }
}