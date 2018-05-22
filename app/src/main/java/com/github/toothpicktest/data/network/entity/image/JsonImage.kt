package com.github.toothpicktest.data.network.entity.image

data class JsonImage(
        val id: Long,
        val owner: String,
        val secret: String,
        val server: Long,
        val title: String,
        val farm: Int,
        val date_upload: Long
)