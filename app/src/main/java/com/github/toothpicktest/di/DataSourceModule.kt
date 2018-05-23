package com.github.toothpicktest.di

import com.github.toothpicktest.data.datasource.image.ImageDataSource
import com.github.toothpicktest.data.datasource.image.MemoryImageDataSource
import com.github.toothpicktest.data.datasource.image.NetworkImageDataSource
import com.github.toothpicktest.data.datasource.tagHistory.MemoryTagHistoryDataSource
import com.github.toothpicktest.data.datasource.tagHistory.TagHistoryDataSource
import toothpick.config.Module

class DataSourceModule : Module() {
    init {
        bind(ImageDataSource::class.java)
                .withName(NetworkImageDataSourceQualifier::class.java)
                .to(NetworkImageDataSource::class.java)
        bind(ImageDataSource::class.java)
                .withName(MemoryImageDataSourceQualifier::class.java)
                .to(MemoryImageDataSource::class.java)
        bind(TagHistoryDataSource::class.java).to(MemoryTagHistoryDataSource::class.java)
    }
}