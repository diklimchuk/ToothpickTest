package com.github.toothpicktest.domain.entity

import java.util.Date

data class Image(
        val id: Long,
        val url: String,
        val updateDate: Date,
        val uploadDate: Date,
        val tags: Collection<String>
)