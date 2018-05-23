package com.github.toothpicktest.domain.usecase

import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.domain.repo.IImageRepo
import com.github.toothpicktest.domain.repo.ITagHistoryRepo
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
        private val repo: IImageRepo,
        private val tagsRepo: ITagHistoryRepo
) {
    fun execute(
            filter: ImageFilter,
            page: Int,
            quantity: Int
    ): Single<List<Image>> = if (filter.hasTag) {
        tagsRepo.addHistoryTag(filter.tag)
    } else {
        Completable.complete()
    }.andThen(repo.getImages(filter, page, quantity))
}