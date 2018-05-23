package com.github.toothpicktest.presentation.screens.images.pagination

import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter

/**
 * @param page Number of page to load
 * @param orderValueLt Assuming images are ordered by some value, all returned values should be
 * less than this.
 * @param filter Filter to apply to images.
 */
data class ImagePageRequest(
        val page: Int,
        val orderValueLt: Long,
        val filter: ImageFilter
)