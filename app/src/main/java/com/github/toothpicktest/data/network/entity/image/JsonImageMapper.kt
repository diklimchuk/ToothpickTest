package com.github.toothpicktest.data.network.entity.image

import com.github.toothpicktest.domain.entity.Image
import java.util.Date

fun JsonImage.toModel() = Image(
        id,
        "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg",
        Date(date_upload)
)
