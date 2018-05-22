package com.github.toothpicktest.data.network.entity.image

import com.github.toothpicktest.domain.entity.Image

fun JsonImage.toModel() = Image(
        id,
        "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
)