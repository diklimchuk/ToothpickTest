package com.github.toothpicktest

import com.github.toothpicktest.data.datasource.image.NetworkImageDataSource
import com.github.toothpicktest.data.network.FlickrApi
import com.github.toothpicktest.data.network.entity.image.JsonImage
import com.github.toothpicktest.data.network.entity.image.toModel
import com.github.toothpicktest.data.network.response.JsonImagesResponse
import com.github.toothpicktest.data.network.response.JsonImagesResponseContent
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import java.util.Date
import java.util.Random

class NetworkDataSourcePaginationTest {

    @Mock
    lateinit var api: FlickrApi

    @Rule
    @JvmField
    val mockitoRule = MockitoJUnit.rule()

    private val random = Random()

    private fun Random.nextString(length: Int): String {
        val bytes = ByteArray(length)
        this.nextBytes(bytes)
        return bytes.contentToString()
    }

    private fun getRandomImage(
            timestamp: Long = Date().time
    ): JsonImage = JsonImage(
            random.nextLong(),
            random.nextString(10),
            random.nextString(10),
            random.nextLong(),
            random.nextString(11),
            random.nextInt(),
            timestamp
    )

    @Test
    fun firstPageIsLoadedAsIs() {
        val jsonImages = listOf(getRandomImage(), getRandomImage())
        val response = JsonImagesResponse(
                JsonImagesResponseContent(1, 100, 2, 200, jsonImages),
                "ok"
        )
        `when`(api.recentImages(pageSize = 2)).thenReturn(Single.just(response))

        val dataSource = NetworkImageDataSource(api)
        val images = jsonImages.map(JsonImage::toModel)
        dataSource.getImages(1, 2)
                .test()
                .assertResult(images)
    }

    @Test
    fun returnAllIfLastPageIsNotFilled() {
        val jsonImages = listOf(getRandomImage(), getRandomImage(), getRandomImage())
        val response = JsonImagesResponse(
                JsonImagesResponseContent(3, 3, 10, 23, jsonImages),
                "ok"
        )
        `when`(api.recentImages(pageSize = 10)).thenReturn(Single.just(response))

        val dataSource = NetworkImageDataSource(api)
        val images = jsonImages.map(JsonImage::toModel)
        dataSource.getImages(3, 10)
                .test()
                .assertResult(images)
    }
}