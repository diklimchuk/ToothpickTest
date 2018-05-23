package com.github.toothpicktest.presentation.screens.images.filter

import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.screens.images.filter.ImageOrder.UPDATE_DATE
import com.github.toothpicktest.presentation.screens.images.filter.ImageOrder.UPLOAD_DATE

class ImageFilter private constructor(
        val tag: String,
        val order: ImageOrder
) {
    val hasTag = tag.isNotBlank()

    companion object {
        private const val NO_TAG = ""

        fun forTag(
                tag: String,
                order: ImageOrder = UPLOAD_DATE
        ): ImageFilter {
            if (tag.isBlank()) throw IllegalArgumentException("Can't create tag filter with blank tag")
            return ImageFilter(tag, order)
        }

        fun recent(
                order: ImageOrder = UPDATE_DATE
        ) = ImageFilter(NO_TAG, order)
    }

    override fun equals(other: Any?): Boolean {
        return other is ImageFilter && this.tag == other.tag && this.order == other.order
    }

    override fun hashCode(): Int = tag.hashCode() + order.hashCode() * 31

    fun getImageOrderValue(image: Image): Long = when (order) {
        UPDATE_DATE -> image.updateDate.time
        UPLOAD_DATE -> image.uploadDate.time
    }
}

