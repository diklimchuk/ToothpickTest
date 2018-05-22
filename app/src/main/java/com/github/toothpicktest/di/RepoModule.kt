package com.github.toothpicktest.di

import com.github.toothpicktest.data.repo.ImageRepo
import com.github.toothpicktest.domain.repo.IImageRepo
import toothpick.config.Module

class RepoModule : Module() {
    init {
        bind(IImageRepo::class.java).to(ImageRepo::class.java)
    }
}