package com.github.toothpicktest.domain.repo

import com.github.toothpicktest.domain.entity.Image
import io.reactivex.Single
import java.util.Date

interface IImageRepo {

    fun getImages(
            page: Int,
            quantity: Int
    ): Single<List<Image>>

    fun getImagesWithTag(
            tag: String,
            page: Int,
            quantity: Int
    ): Single<List<Image>>
}