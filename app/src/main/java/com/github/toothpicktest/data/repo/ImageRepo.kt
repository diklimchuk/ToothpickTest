package com.github.toothpicktest.data.repo

import com.github.toothpicktest.data.datasource.image.ImageDataSource
import com.github.toothpicktest.di.MemoryImageDataSourceQualifier
import com.github.toothpicktest.di.NetworkImageDataSourceQualifier
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.domain.repo.IImageRepo
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import io.reactivex.Single
import javax.inject.Inject

class ImageRepo @Inject constructor(
        @NetworkImageDataSourceQualifier private val network: ImageDataSource,
        @MemoryImageDataSourceQualifier private val memory: ImageDataSource
) : IImageRepo {

    override fun getImages(
            filter: ImageFilter,
            page: Int,
            quantity: Int
    ): Single<List<Image>> {
        return network.getImages(filter, page, quantity)
    }
}