package com.github.toothpicktest.data.datasource

import com.github.toothpicktest.data.network.FlickrApi
import com.github.toothpicktest.data.network.entity.image.JsonImage
import com.github.toothpicktest.data.network.entity.image.toModel
import com.github.toothpicktest.domain.entity.Image
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NetworkImageDataSource @Inject constructor(
        private val api: FlickrApi
) : ImageDataSource {
    override fun getImages(
            page: Int,
            quantity: Int
    ): Single<List<Image>> = api.recentImages(page = page, pageSize = quantity)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { it.photos.photo.map(JsonImage::toModel) }
            .map { it.sortedByDescending { it.updateDate.time } }

    override fun getImagesWithTag(
            tag: String,
            page: Int,
            quantity: Int
    ): Single<List<Image>> = api.searchImages(page = page, pageSize = quantity, tags = tag)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { it.photos.photo.map(JsonImage::toModel) }
            .map { it.sortedByDescending { it.uploadDate.time } }
}