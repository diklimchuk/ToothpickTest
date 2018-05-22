package com.github.toothpicktest.di

import com.github.toothpicktest.BuildConfig
import com.github.toothpicktest.data.network.FlickrApi
import com.github.toothpicktest.data.network.interceptor.FlickrAuthInterceptor
import com.github.toothpicktest.di.provider.FlickrApiProvider
import com.github.toothpicktest.di.provider.OkHttpClientProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory
import toothpick.config.Module

class NetworkModule(
        apiBaseUrl: String,
        flickrApiKey: String
) : Module() {

    init {
        bind(String::class.java).withName(ApiBaseUrl::class.java).toInstance(apiBaseUrl)
        bind(String::class.java).withName(FlickrApiKey::class.java).toInstance(flickrApiKey)
        bind(Interceptor::class.java)
                .withName(LoggingInterceptor::class.java)
                .toInstance(createHttpLoggingInterceptor())
        bind(Interceptor::class.java)
                .withName(FlickrApiInterceptor::class.java)
                .to(FlickrAuthInterceptor::class.java)
        bind(MoshiConverterFactory::class.java).toInstance(MoshiConverterFactory.create())

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