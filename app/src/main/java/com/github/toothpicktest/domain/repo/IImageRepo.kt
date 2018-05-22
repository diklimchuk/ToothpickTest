package com.github.toothpicktest.domain.repo

import com.github.toothpicktest.domain.entity.Image
import io.reactivex.Single
import java.util.Date

interface IImageRepo {

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