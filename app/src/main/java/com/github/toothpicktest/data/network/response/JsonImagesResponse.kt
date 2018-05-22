package com.github.toothpicktest.data.network.response

import com.github.toothpicktest.data.network.entity.image.JsonImage

data class JsonImagesResponse(
        val photos: JsonImagesResponseContent,
        val stat: String
)

data class JsonImagesResponseContent(
        val page: Int,
        val pages: Int,
        val perpage: Int,
        val total: Int,
        val photo: List<JsonImage>
)