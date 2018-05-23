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

        private const val MAX_ORDER_VALUE = Long.MAX_VALUE
    }

    fun handle(request: ImagePageRequest): Single<ImagePageResult> {
        return getInput(request)
                .observeOn(Schedulers.computation())
                .map { ensureResultDoesntOverlapWithPrevious(request, it) }
                .map { ImagePageResult(request.page, it, getOrderValue(request, it)) }
    }

    private fun getOrderValue(
            request: ImagePageRequest,
            result: List<Image>
    ): Long {
        return if (request.hasTag) {
            result.minBy { it.uploadDate }?.uploadDate?.time ?: MAX_ORDER_VALUE
        } else {
            result.minBy { it.updateDate }?.updateDate?.time ?: MAX_ORDER_VALUE
        }
    }

    private fun ensureResultDoesntOverlapWithPrevious(
            request: ImagePageRequest,
            result: List<Image>
    ): List<Image> = if (request.hasTag) {
        result.filter { it.uploadDate.time < request.orderValueLt }
    } else {
        result.filter { it.updateDate.time < request.orderValueLt }
    }

    private fun getInput(request: ImagePageRequest): Single<List<Image>> {
        return if (request.hasTag) {
            repo.getImagesWithTag(request.tag, request.page, IMAGE_BLOCK_SIZE)
        } else {
            repo.getImages(request.page, IMAGE_BLOCK_SIZE)
        }
    }
}