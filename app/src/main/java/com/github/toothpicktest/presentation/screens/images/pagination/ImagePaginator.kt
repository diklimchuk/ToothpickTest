package com.github.toothpicktest.presentation.screens.images.pagination

import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.domain.usecase.GetImagesUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImagePaginator @Inject constructor(
        private val getImages: GetImagesUseCase
) {

    companion object {
        private const val IMAGE_BLOCK_SIZE = 30

        private const val MAX_ORDER_VALUE = Long.MAX_VALUE
    }

    fun handle(
            request: ImagePageRequest
    ): Single<ImagePageResult> = getInput(request)
            .observeOn(Schedulers.computation())
            .map { ensureResultDoesntOverlapWithPrevious(request, it) }
            .map { ImagePageResult(request.page, it, getOrderValue(request, it)) }

    private fun getOrderValue(
            request: ImagePageRequest,
            result: List<Image>
    ): Long = result.minBy { request.filter.getImageOrderValue(it) }
                      ?.let { request.filter.getImageOrderValue(it) } ?: MAX_ORDER_VALUE


    private fun ensureResultDoesntOverlapWithPrevious(
            request: ImagePageRequest,
            result: List<Image>
    ): List<Image> = result
            .filter { request.filter.getImageOrderValue(it) < request.orderValueLt }

    private fun getInput(
            request: ImagePageRequest
    ): Single<List<Image>> = getImages
            .execute(request.filter, request.page, IMAGE_BLOCK_SIZE)
}