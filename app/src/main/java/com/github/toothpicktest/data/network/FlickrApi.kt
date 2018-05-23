package com.github.toothpicktest.data.network

import com.github.toothpicktest.data.network.interceptor.FlickrAuthInterceptor
import com.github.toothpicktest.data.network.interceptor.FlickrJsonFormatInterceptor
import com.github.toothpicktest.data.network.response.JsonImagesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FlickrApi {

    @GET("rest")
    @Headers(
            FlickrAuthInterceptor.USE_FLICKR_AUTH,
            FlickrJsonFormatInterceptor.USE_FLICKR_JSON_FORMAT
    )
    fun searchImages(
            @Query("tags") tags: String,
            @Query("per_page") pageSize: Int = 100,
            @Query("method") method: String = "flickr.photos.search",
            @Query("extras") extras: String = "last_update,tags,date_upload",
            @Query("page") page: Int = 1
    ): Single<JsonImagesResponse>

    @GET("rest")
    @Headers(
            FlickrAuthInterceptor.USE_FLICKR_AUTH,
            FlickrJsonFormatInterceptor.USE_FLICKR_JSON_FORMAT
    )
    fun recentImages(
            @Query("per_page") pageSize: Int = 100,
            @Query("method") method: String = "flickr.photos.getRecent",
            @Query("sort") order: String = "date-posted-desc",
            @Query("extras") extras: String = "last_update,tags,date_upload",
            @Query("page") page: Int = 1
    ): Single<JsonImagesResponse>
}