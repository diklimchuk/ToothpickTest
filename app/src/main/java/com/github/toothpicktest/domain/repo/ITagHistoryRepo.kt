package com.github.toothpicktest.domain.repo

import io.reactivex.Completable
import io.reactivex.Single

interface ITagHistoryRepo {
    fun addHistoryTag(tag: String): Completable
    fun getHistoryTags(): Single<List<String>>
}