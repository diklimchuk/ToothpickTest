package com.github.toothpicktest.domain.usecase

import com.github.toothpicktest.domain.repo.ITagHistoryRepo
import io.reactivex.Single
import javax.inject.Inject

class GetHistoryTagsUseCase @Inject constructor(
        private val repo: ITagHistoryRepo
) {
    fun execute(): Single<List<String>> = repo.getHistoryTags()
}