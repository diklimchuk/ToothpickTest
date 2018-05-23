package com.github.toothpicktest.presentation.screens.images.pagination

/**
 * @param page Number of page to load
 * @param orderValueLt Assuming images are ordered by some value, all returned values should be
 * less than this.
 */
data class ImagePageRequest(
        val page: Int,
        val orderValueLt: Long,
        val tag: String = ""
) {
    val hasTag = tag.isNotBlank()
}