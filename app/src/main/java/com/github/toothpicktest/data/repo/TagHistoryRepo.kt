package com.github.toothpicktest.data.repo

import com.github.toothpicktest.data.datasource.tagHistory.TagHistoryDataSource
import com.github.toothpicktest.domain.repo.ITagHistoryRepo
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class TagHistoryRepo @Inject constructor(
        private val memory: TagHistoryDataSource
) : ITagHistoryRepo {

    override fun addHistoryTag(tag: String): Completable = memory.addTag(tag)

    override fun getHistoryTags(): Single<List<String>> = memory.getAll()
}