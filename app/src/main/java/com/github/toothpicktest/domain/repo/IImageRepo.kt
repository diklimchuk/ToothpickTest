package com.github.toothpicktest.domain.repo

import com.github.toothpicktest.domain.entity.Image
import io.reactivex.Single

interface IImageRepo {
    fun getImages(): Single<List<Image>>
}