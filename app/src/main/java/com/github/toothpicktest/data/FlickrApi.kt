package com.github.toothpicktest.data

import com.github.toothpicktest.data.interceptor.FlickrAuthInterceptor
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FlickrApi {

    @GET("rest")
    @Headers(FlickrAuthInterceptor.FLICKR_AUTH)
    fun getImages(
            @Query("method") method: String = "flickr.photos.search",
            @Query("format") format: String = "json",
            @Query("nojsoncallback") noJsonCallback: Int = 1,
            @Query("tags") tags: String = "car,cars,image",
            @Query("page") page: String = "1"
    ): Single<Response<String>>
}