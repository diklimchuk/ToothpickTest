package com.github.toothpicktest.data.repo

import com.github.toothpicktest.data.datasource.ImageDataSource
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.domain.repo.IImageRepo
import io.reactivex.Single
import javax.inject.Inject

class ImageRepo @Inject constructor(
        private val network: ImageDataSource
) : IImageRepo {
    override fun getImages(
            page: Int,
            quantity: Int
    ): Single<List<Image>> = network.getImages(page, quantity)

    override fun getImagesWithTag(
            tag: String,
            page: Int,
            quantity: Int
    ): Single<List<Image>> = network.getImagesWithTag(tag, page, quantity)
}