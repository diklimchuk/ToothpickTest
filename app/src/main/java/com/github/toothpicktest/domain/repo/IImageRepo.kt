package com.github.toothpicktest.domain.repo

import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import io.reactivex.Single
import java.util.Date

interface IImageRepo {
    fun getImages(
            filter: ImageFilter,
            page: Int,
            quantity: Int
    ): Single<List<Image>>
}