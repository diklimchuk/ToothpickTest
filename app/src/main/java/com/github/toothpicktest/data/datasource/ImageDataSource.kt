package com.github.toothpicktest.data.datasource

import com.github.toothpicktest.domain.entity.Image
import io.reactivex.Single
import java.util.Date

interface ImageDataSource {
    fun getImages(
            afterDate: Date,
            quantity: Int
    ): Single<List<Image>>

    fun getImagesWithTag(
            tag: String,
            afterDate: Date,
            quantity: Int
    ): Single<List<Image>>
}