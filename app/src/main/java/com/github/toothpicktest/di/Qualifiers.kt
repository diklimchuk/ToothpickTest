package com.github.toothpicktest.di

import javax.inject.Qualifier

@Qualifier annotation class LoggingInterceptor
@Qualifier annotation class FlickrApiInterceptor
@Qualifier annotation class ApiBaseUrl
@Qualifier annotation class FlickrApiKey
@Qualifier annotation class MemoryImageDataSourceQualifier
@Qualifier annotation class NetworkImageDataSourceQualifier
