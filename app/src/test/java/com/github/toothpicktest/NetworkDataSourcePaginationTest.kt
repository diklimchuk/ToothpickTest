package com.github.toothpicktest

import com.github.toothpicktest.data.datasource.NetworkImageDataSource
import com.github.toothpicktest.data.network.FlickrApi
import com.github.toothpicktest.data.network.entity.image.JsonImage
import com.github.toothpicktest.data.network.entity.image.toModel
import com.github.toothpicktest.data.network.response.JsonImagesResponse
import com.github.toothpicktest.data.network.response.JsonImagesResponseContent
import com.github.toothpicktest.data.repo.ImageRepo
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import java.util.Date
import java.util.Random
import org.junit.Assert.*

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

    private fun getRandomImage(): JsonImage = JsonImage(
            random.nextLong(),
            random.nextString(10),
            random.nextString(10),
            random.nextLong(),
            random.nextString(11),
            random.nextInt(),
            Date().time
    )

    private fun getRandomApiResponse(
            page: Int,
            quantity: Int
    ): JsonImagesResponse = JsonImagesResponse(
            JsonImagesResponseContent(page, 100, quantity, quantity * 100, (0..10).map{ getRandomImage() }),
            "ok"
    )

    @Test
    fun firstPageIsLoadedAsIs() {
        val response = getRandomApiResponse(1, 10)
        `when`(api.recentImages()).thenReturn(Single.just(response))
        val dataSource = NetworkImageDataSource(api)
        dataSource.getImages(Date(), 10)
                .test()
                .assertResult(response.photos.photo.map(JsonImage::toModel))
    }
}