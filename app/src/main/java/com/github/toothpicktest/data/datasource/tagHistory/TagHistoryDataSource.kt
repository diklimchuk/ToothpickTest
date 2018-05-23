package com.github.toothpicktest.data.datasource.tagHistory

import io.reactivex.Completable
import io.reactivex.Single

interface TagHistoryDataSource {
    fun addTag(tag: String): Completable

    fun getAll(): Single<List<String>>
}