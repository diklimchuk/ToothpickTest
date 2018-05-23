package com.github.toothpicktest.data.repo

import com.github.toothpicktest.data.datasource.ImageDataSource
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.domain.repo.IImageRepo
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import io.reactivex.Single
import javax.inject.Inject

class ImageRepo @Inject constructor(
        private val network: ImageDataSource
) : IImageRepo {
    override fun getImages(
            filter: ImageFilter,
            page: Int,
            quantity: Int
    ): Single<List<Image>> {
        return network.getImages(filter, page, quantity)
    }
}