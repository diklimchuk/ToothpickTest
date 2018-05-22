package com.github.toothpicktest.data.network

import com.github.toothpicktest.data.network.interceptor.FlickrAuthInterceptor
import com.github.toothpicktest.data.network.response.JsonImagesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FlickrApi {

    @GET("rest")
    @Headers(FlickrAuthInterceptor.FLICKR_AUTH)
    fun searchImages(
            @Query("tags") tags: String,
            @Query("method") method: String = "flickr.photos.search",
            @Query("format") format: String = "json",
            @Query("nojsoncallback") noJsonCallback: Int = 1,
            @Query("page") page: String = "1"
    ): Single<JsonImagesResponse>

    @GET("rest")
    @Headers(FlickrAuthInterceptor.FLICKR_AUTH)
    fun recentImages(
            @Query("method") method: String = "flickr.photos.getRecent",
            @Query("format") format: String = "json",
            @Query("nojsoncallback") noJsonCallback: Int = 1,
            @Query("page") page: String = "1"
    ): Single<JsonImagesResponse>
}