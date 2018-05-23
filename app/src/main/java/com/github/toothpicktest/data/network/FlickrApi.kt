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
            @Query("per_page") pageSize: Int = 100,
            @Query("method") method: String = "flickr.photos.search",
            @Query("sort") order: String = "date-taken-desc",
            @Query("extras") extras: String = "last_update",
            @Query("format") format: String = "json",
            @Query("nojsoncallback") noJsonCallback: Int = 1,
            @Query("page") page: Int = 1
    ): Single<JsonImagesResponse>

    @GET("rest")
    @Headers(FlickrAuthInterceptor.FLICKR_AUTH)
    fun recentImages(
            @Query("per_page") pageSize: Int = 100,
            @Query("method") method: String = "flickr.photos.getRecent",
            @Query("extras") extras: String = "last_update",
            @Query("format") format: String = "json",
            @Query("nojsoncallback") noJsonCallback: Int = 1,
            @Query("page") page: Int = 1
    ): Single<JsonImagesResponse>
}