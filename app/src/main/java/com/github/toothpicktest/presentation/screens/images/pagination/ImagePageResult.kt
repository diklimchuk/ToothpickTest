package com.github.toothpicktest.presentation.screens.images.pagination

import com.github.toothpicktest.domain.entity.Image
import java.util.Date

data class ImagePageResult(
        val page: Int,
        val images: List<Image>,
        val orderValue: Long
)