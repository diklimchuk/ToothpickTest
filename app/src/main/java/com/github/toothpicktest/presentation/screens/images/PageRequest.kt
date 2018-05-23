package com.github.toothpicktest.presentation.screens.images

/**
 * @param page Number of page to load
 * @param orderValueLt Assuming all the items are ordered by some value, values on the page should
 * be less than this.
 */
data class PageRequest(
        val page: Int,
        val orderValueLt: Long
)