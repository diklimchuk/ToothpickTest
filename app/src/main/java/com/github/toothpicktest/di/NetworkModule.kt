package com.github.toothpicktest.di

import com.github.toothpicktest.BuildConfig
import com.github.toothpicktest.data.FlickrApi
import com.github.toothpicktest.data.interceptor.FlickrAuthInterceptor
import com.github.toothpicktest.di.provider.FlickrApiProvider
import com.github.toothpicktest.di.provider.OkHttpClientProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module

class NetworkModule(
         apiBaseUrl: String
) : Module() {

    init {
        bind(String::class.java).withName(ApiBaseUrl::class.java).toInstance(apiBaseUrl)
        bind(String::class.java).withName(FlickrApiKey::class.java).toInstance(apiBaseUrl)
        bind(Interceptor::class.java)
                .withName(LoggingInterceptor::class.java)
                .toInstance(createHttpLoggingInterceptor())
        bind(Interceptor::class.java)
                .withName(FlickrApiInterceptor::class.java)
                .to(FlickrAuthInterceptor::class.java)

        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java)
        bind(FlickrApi::class.java).toProvider(FlickrApiProvider::class.java)
    }

    private fun createHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}