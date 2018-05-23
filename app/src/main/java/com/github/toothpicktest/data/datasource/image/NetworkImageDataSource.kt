package com.github.toothpicktest.data.datasource.image

import com.github.toothpicktest.data.network.FlickrApi
import com.github.toothpicktest.data.network.entity.image.JsonImage
import com.github.toothpicktest.data.network.entity.image.toModel
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import com.github.toothpicktest.presentation.screens.images.filter.ImageOrder.UPDATE_DATE
import com.github.toothpicktest.presentation.screens.images.filter.ImageOrder.UPLOAD_DATE
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NetworkImageDataSource @Inject constructor(
        private val api: FlickrApi
) : ImageDataSource {
    override fun getImages(
            filter: ImageFilter,
            page: Int,
            quantity: Int
    ): Single<List<Image>> = if (filter.hasTag) {
        if (filter.order == UPDATE_DATE) throw UnsupportedOperationException("Can't sort images without tag by update date")
        getImagesWithTag(filter.tag, page, quantity)
    } else {
        if (filter.order == UPLOAD_DATE) throw UnsupportedOperationException("Can't sort images with tag by upload date")
        getImages(page, quantity)
    }

    private fun getImages(
            page: Int,
            quantity: Int
    ): Single<List<Image>> = api
            .recentImages(page = page, pageSize = quantity)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { it.photos.photo.map(JsonImage::toModel) }
            .map { it.sortedByDescending { it.updateDate.time } }

    private fun getImagesWithTag(
            tag: String,
            page: Int,
            quantity: Int
    ): Single<List<Image>> = api
            .searchImages(page = page, pageSize = quantity, tags = tag)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { it.photos.photo.map(JsonImage::toModel) }
            .map { it.sortedByDescending { it.uploadDate.time } }
}