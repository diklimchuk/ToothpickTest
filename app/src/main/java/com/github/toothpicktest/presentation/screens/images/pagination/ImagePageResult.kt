package com.github.toothpicktest.presentation.screens.images.pagination

import com.github.toothpicktest.domain.entity.Image
import java.util.Date

data class ImagePageResult(
        val page: Int,
        val images: List<Image>
) {
    companion object {
        private val DEFAULT_DATE = Date(0)
    }

    val minDate = images.minBy { it.updateDate }?.updateDate ?: DEFAULT_DATE
    val hasMinDate = minDate != DEFAULT_DATE
}