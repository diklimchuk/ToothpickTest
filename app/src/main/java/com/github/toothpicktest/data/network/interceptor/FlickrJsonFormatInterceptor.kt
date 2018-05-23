package com.github.toothpicktest.data.network.interceptor

import com.github.toothpicktest.di.ApiBaseUrl
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class FlickrJsonFormatInterceptor @Inject constructor(
        @ApiBaseUrl baseUrl: String
) : Interceptor {

    companion object {
        private const val FLICKR_JSON_FORMAT_HEADER = "FlickrAuth"
        private const val FLICKR_JSON_FORMAT_HEADER_VALUE = "true"
        private const val FLICKR_FORMAT_ARG = "format"
        private const val FLICKR_JSON_FORMAT = "json"
        private const val FLICKR_NO_JSON_CALLBACK_ARG = "nojsoncallback"
        private const val FLICKR_NO_JSON_CALLBACK_TRUE = "1"

        /**
         * Pass this header to a network request to flickr to use json format.
         */
        const val USE_FLICKR_JSON_FORMAT = "$FLICKR_JSON_FORMAT_HEADER: $FLICKR_JSON_FORMAT_HEADER_VALUE"
    }

    override fun intercept(chain: Chain): Response {
        val originalRequest = chain.request()

        val authorizedRequest = if (shouldUseJsonFormat(originalRequest)) {
            originalRequest.newBuilder()
                    .url(urlWithQueryArgs(originalRequest))
                    .removeHeader(FLICKR_JSON_FORMAT_HEADER)
                    .build()
        } else {
            originalRequest
        }

        return chain.proceed(authorizedRequest)
    }

    private fun urlWithQueryArgs(request: Request): HttpUrl {
        return request.url().newBuilder()
                .addQueryParameter(FLICKR_FORMAT_ARG, FLICKR_JSON_FORMAT)
                .addQueryParameter(FLICKR_NO_JSON_CALLBACK_ARG, FLICKR_NO_JSON_CALLBACK_TRUE)
                .build()
    }

    private fun shouldUseJsonFormat(request: Request): Boolean {
        return request.headers()
                .values(FLICKR_JSON_FORMAT_HEADER)
                .contains(FLICKR_JSON_FORMAT_HEADER_VALUE)
    }
}
