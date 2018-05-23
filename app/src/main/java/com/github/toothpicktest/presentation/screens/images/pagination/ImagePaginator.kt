package com.github.toothpicktest.presentation.screens.images.pagination

import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.domain.repo.IImageRepo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImagePaginator @Inject constructor(
        private val repo: IImageRepo
) {

    companion object {
        private const val IMAGE_BLOCK_SIZE = 30
    }

    fun handle(request: ImagePageRequest): Single<ImagePageResult> {
        return getInput(request)
                .observeOn(Schedulers.computation())
                .map { it.filter { it.updateDate < request.maxUploadDate } }
                .map { ImagePageResult(request.page, it) }
    }

    private fun getInput(request: ImagePageRequest): Single<List<Image>> {
        return if (request.hasTag) {
            repo.getImages(request.page, IMAGE_BLOCK_SIZE)
        } else {
            repo.getImagesWithTag(request.tag, request.page, IMAGE_BLOCK_SIZE)
        }
    }
}