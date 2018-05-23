package com.github.toothpicktest.data.datasource.image

import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import io.reactivex.Single

interface ImageDataSource {
    fun getImages(
            filter: ImageFilter,
            page: Int,
            quantity: Int
    ): Single<List<Image>>

}