package com.github.toothpicktest.presentation.screens.images.pagination

import com.github.toothpicktest.domain.entity.Image

/**
 * @param page Index of loaded page
 * @param images Loaded images
 * @param minOrderValue Min order value of all images.
 *
 * @see
 */
data class ImagePageResult(
        val page: Int,
        val images: List<Image>,
        val minOrderValue: Long
)