package com.github.toothpicktest.di.provider

import com.github.toothpicktest.di.FlickrApiInterceptor
import com.github.toothpicktest.di.LoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpClientProvider @Inject constructor(
        @LoggingInterceptor private val loggingInterceptor: Interceptor,
        @FlickrApiInterceptor private val flickrApiInterceptor: Interceptor
) : Provider<OkHttpClient> {
    override fun get(): OkHttpClient = with(OkHttpClient.Builder()) {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        addNetworkInterceptor(loggingInterceptor)
        addNetworkInterceptor(flickrApiInterceptor)
        build()
    }
}