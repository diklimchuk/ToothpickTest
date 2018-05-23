package com.github.toothpicktest.di

import com.github.toothpicktest.data.repo.ImageRepo
import com.github.toothpicktest.data.repo.TagHistoryRepo
import com.github.toothpicktest.domain.repo.IImageRepo
import com.github.toothpicktest.domain.repo.ITagHistoryRepo
import toothpick.config.Module

class RepoModule : Module() {
    init {
        bind(IImageRepo::class.java).to(ImageRepo::class.java)
        bind(ITagHistoryRepo::class.java).to(TagHistoryRepo::class.java)
    }
}