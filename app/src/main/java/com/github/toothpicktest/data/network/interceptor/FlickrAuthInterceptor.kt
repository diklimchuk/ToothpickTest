package com.github.toothpicktest.data.network.interceptor

import com.github.toothpicktest.di.FlickrApiKey
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject


/**
 * Interceptor that will provide authentication for Flickr api
 */
class FlickrAuthInterceptor @Inject constructor(
        @FlickrApiKey private val flickrApiKey: String
) : Interceptor {

    companion object {
        private const val FLICKR_AUTH_HEADER = "FlickrAuth"
        private const val FLICKR_AUTH_HEADER_VALUE = "true"
        private const val FLICKR_AUTH_ARG = "api_key"

        /**
         * Pass this header to a network request to trigger authorization
         */
        const val USE_FLICKR_AUTH = "$FLICKR_AUTH_HEADER: $FLICKR_AUTH_HEADER_VALUE"
    }

    override fun intercept(chain: Chain): Response {
        val originalRequest = chain.request()

        val authorizedRequest = if (isAuthRequired(originalRequest)) {
            originalRequest.newBuilder()
                    .url(urlWithAuthQuery(originalRequest))
                    .removeHeader(USE_FLICKR_AUTH)
                    .build()
        } else {
            originalRequest
        }

        return chain.proceed(authorizedRequest)
    }

    private fun urlWithAuthQuery(request: Request): HttpUrl {
        return request.url().newBuilder()
                .addQueryParameter(FLICKR_AUTH_ARG, flickrApiKey)
                .build()
    }

    private fun isAuthRequired(request: Request): Boolean {
        return request.headers()
                .values(FLICKR_AUTH_HEADER)
                .contains(FLICKR_AUTH_HEADER_VALUE)
    }
}