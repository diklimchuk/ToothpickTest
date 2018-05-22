package com.github.toothpicktest.data.repo

import com.github.toothpicktest.data.network.FlickrApi
import com.github.toothpicktest.data.network.entity.image.JsonImage
import com.github.toothpicktest.data.network.entity.image.toModel
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.domain.repo.IImageRepo
import io.reactivex.Single
import javax.inject.Inject

class ImageRepo @Inject constructor(
        private val api: FlickrApi
) : IImageRepo {
    override fun getImages(): Single<List<Image>> = api
            .recentImages()
            .map { it.photos.photo.map(JsonImage::toModel) }

    override fun getImages(tag: String): Single<List<Image>> = api
            .searchImages(tag)
            .map { it.photos.photo.map(JsonImage::toModel) }
}