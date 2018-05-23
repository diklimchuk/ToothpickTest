package com.github.toothpicktest.presentation.screens.images.pagination

import java.util.Date

/**
 * @param page Number of page to load
 * @param maxUploadDate All images should have upload date lte than this value.
 */
data class ImagePageRequest(
        val page: Int,
        val maxUploadDate: Date,
        val tag: String = ""
) {
    val hasTag = tag.isNotBlank()
}