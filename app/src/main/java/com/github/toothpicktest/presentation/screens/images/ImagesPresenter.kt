package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.domain.usecase.GetHistoryTagsUseCase
import com.github.toothpicktest.presentation.mvp.BasePresenter
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import com.github.toothpicktest.presentation.screens.images.pagination.ImagePageRequest
import com.github.toothpicktest.presentation.screens.images.pagination.ImagePaginator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject


@InjectViewState
@ImagesScope
class ImagesPresenter @Inject constructor(
        private val paginator: ImagePaginator,
        private val getHistoryTags: GetHistoryTagsUseCase
) : BasePresenter<ImagesView>() {

    companion object {

        private const val LOAD_THRESHOLD = 20

        private const val MAX_DATE = Long.MAX_VALUE
    }

    private var lastLoadedPage = 0
    private var lastVisibleItemOrderValue = MAX_DATE

    private val tagRequests = PublishSubject.create<ImageFilter>()
    private val pageRequests = PublishSubject.create<Int>()
    private val searchTagsRequests = CompositeDisposable()
    private fun Disposable.bindHistoryTagsRequest() = searchTagsRequests.add(this)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observePageRequests()
        tagRequests.onNext(ImageFilter.recent())
        requestNextPage()
    }

    fun onSearchFocused() {
        cancelHistoryTagsRequests()
        getHistoryTags.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.showHistoryTags(it) }, { /* TODO: */ })
                .bind()
                .bindHistoryTagsRequest()
    }

    fun onSearchLostFocus() {
        cancelHistoryTagsRequests()
        viewState.hideHistoryTags()
    }

    private fun cancelHistoryTagsRequests() {
        searchTagsRequests.clear()
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

                                paginator.handle(
                                        ImagePageRequest(page, lastVisibleItemOrderValue, filter))
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