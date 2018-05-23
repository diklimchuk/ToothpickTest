package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.presentation.mvp.BasePresenter
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter.Companion
import com.github.toothpicktest.presentation.screens.images.pagination.ImagePageRequest
import com.github.toothpicktest.presentation.screens.images.pagination.ImagePaginator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject


@InjectViewState
@ImagesScope
class ImagesPresenter @Inject constructor(
        private val paginator: ImagePaginator
) : BasePresenter<ImagesView>() {

    companion object {

        private const val LOAD_THRESHOLD = 20

        private const val MAX_DATE = Long.MAX_VALUE
    }

    private var lastLoadedPage = 0
    private var lastVisibleItemOrderValue = MAX_DATE

    private val tagRequests = PublishSubject.create<ImageFilter>()
    private val pageRequests = PublishSubject.create<Int>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observePageRequests()
        tagRequests.onNext(ImageFilter.recent())
        requestNextPage()
    }

    fun onQueryChanged(newQuery: String) {
        val filter = if (newQuery.isBlank()) {
            ImageFilter.recent()
        } else {
            ImageFilter.forTag(newQuery)
        }
        tagRequests.onNext(filter)
        requestNextPage()
    }

    fun onScrolledToPosition(
            margin: Int
    ) {
        if (margin < LOAD_THRESHOLD) {
            requestNextPage()
        }
    }

    private fun requestNextPage() {
        val nextPage = lastLoadedPage + 1
        pageRequests.onNext(nextPage)
    }

    private fun observePageRequests() {
        tagRequests
                .distinctUntilChanged()
                .switchMap { filter ->
                    viewState.clearImages()
                    lastLoadedPage = 0
                    lastVisibleItemOrderValue = MAX_DATE
                    pageRequests.map { Pair(it, filter) }
                            .distinctUntilChanged()
                            .observeOn(AndroidSchedulers.mainThread())
                            .concatMapMaybe { (page, filter) ->

                                paginator.handle(ImagePageRequest(page, lastVisibleItemOrderValue, filter))
                                        .filter { /* Could drop images */ it.page > lastLoadedPage }
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSuccess { lastLoadedPage = it.page }
                                        .filter { it.images.isNotEmpty() }
                                        .doOnSuccess { lastVisibleItemOrderValue = it.orderValue }
                            }
                }
                .subscribe({ viewState.showImages(it.images) }, { Timber.e(it) })
                .bind()
    }
}