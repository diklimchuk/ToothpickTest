package com.github.toothpicktest.data.datasource.image

import android.util.LruCache
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MemoryImageDataSource @Inject constructor() : ImageDataSource {

    private data class Key(
            val filter: ImageFilter,
            val page: Int,
            val pageSize: Int
    )

    companion object {
        private const val MAX_CACHE_SIZE = 100
    }

    private val cache = LruCache<Key, List<Image>>(MAX_CACHE_SIZE)

    override fun saveImages(
            filter: ImageFilter,
            page: Int,
            pageSize: Int,
            images: List<Image>
    ): Completable = Completable.fromRunnable {
        cache.put(Key(filter, page, pageSize), images)
    }

    override fun hasImages(
            filter: ImageFilter,
            page: Int,
            quantity: Int
    ): Completable = Completable.fromRunnable {
        if (cache.get(Key(filter, page, quantity)) == null) {
            throw Exception("No cached value")
        }
    }

    override fun getImages(
            filter: ImageFilter,
            page: Int,
            quantity: Int
    ): Single<List<Image>> = Single.fromCallable {
        cache.get(Key(filter, page, quantity)) ?: throw Exception("No cached value")
    }
}