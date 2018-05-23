package com.github.toothpicktest.domain.usecase

import com.github.toothpicktest.domain.repo.ITagHistoryRepo
import io.reactivex.Completable
import javax.inject.Inject

class RemoveHistoryTagUseCase @Inject constructor(
        private val repo: ITagHistoryRepo
) {
    fun execute(tag: String): Completable = repo.removeHistoryTag(tag)
}