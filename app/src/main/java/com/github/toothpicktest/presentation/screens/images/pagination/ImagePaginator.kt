package com.github.toothpicktest.presentation.screens.images.pagination

import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.domain.usecase.GetImagesUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Handles image pagination.
 *
 * Uses [ImagePageRequest.filter] and [ImagePageRequest.page] to load image page.
 * Uses [ImagePageRequest.orderValueLt] to filter out images which could be loaded on any previous
 * page
 *
 * Paginator assumes that all the images can be sorted and applied a unique value in that order.
 * @see [getOrderValue]
 * It returns minimum order value in [ImagePageResult.minOrderValue] to be provided with the next
 * page request.
 *
 * Current implementation only supports descending ordering.
 */
class ImagePaginator @Inject constructor(
        private val getImages: GetImagesUseCase
) {

    companion object {
        private const val IMAGE_BLOCK_SIZE = 30
        /**
         * Max value which image order can be
         */
        const val MAX_ORDER_VALUE = Long.MAX_VALUE
    }

    fun handle(
            request: ImagePageRequest
    ): Single<ImagePageResult> = getImages
            .execute(request.filter, request.page, IMAGE_BLOCK_SIZE)
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
}