package com.github.toothpicktest.di.provider

import com.github.toothpicktest.data.network.FlickrApi
import com.github.toothpicktest.di.ApiBaseUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class FlickrApiProvider @Inject constructor(
        private val okHttpClient: OkHttpClient,
        private val moshi: MoshiConverterFactory,
        @ApiBaseUrl private val apiBaseUrl: String
) : Provider<FlickrApi> {
    override fun get(): FlickrApi = Retrofit.Builder()
            .addConverterFactory(moshi)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(apiBaseUrl)
            .build()
            .create(FlickrApi::class.java)
}