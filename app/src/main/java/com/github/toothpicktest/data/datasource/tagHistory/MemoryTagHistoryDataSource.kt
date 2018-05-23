package com.github.toothpicktest.data.datasource.tagHistory

import io.reactivex.Completable
import io.reactivex.Single
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton

@Singleton
class MemoryTagHistoryDataSource : TagHistoryDataSource {

    private val tags: MutableSet<String> = Collections
            .newSetFromMap(ConcurrentHashMap<String, Boolean>())

    override fun addTag(tag: String): Completable = Completable.fromRunnable { tags.add(tag) }

    override fun getAll(): Single<List<String>> = Single.fromCallable { tags.toList() }

    override fun removeTag(tag: String): Completable = Completable.fromRunnable { tags.remove(tag) }
}