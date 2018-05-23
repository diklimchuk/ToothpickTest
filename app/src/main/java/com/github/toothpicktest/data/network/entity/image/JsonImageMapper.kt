package com.github.toothpicktest.data.network.entity.image

import com.github.toothpicktest.domain.entity.Image
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

fun JsonImage.toModel() = Image(
        id,
        "https://farm$farm.staticflickr.com/$server/${id}_${secret}_m.jpg",
        Date(lastupdate * 1000),
        tags.split(" ")
)
