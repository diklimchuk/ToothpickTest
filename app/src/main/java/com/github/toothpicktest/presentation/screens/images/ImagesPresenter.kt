package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.domain.usecase.GetHistoryTagsUseCase
import com.github.toothpicktest.domain.usecase.RemoveHistoryTagUseCase
import com.github.toothpicktest.presentation.mvp.BasePresenter
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter
import com.github.toothpicktest.presentation.screens.images.filter.ImageFilter.Companion
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
        private val getHistoryTags: GetHistoryTagsUseCase,
        private val removeHistoryTag: RemoveHistoryTagUseCase
) : BasePresenter<ImagesView>() {

    companion object {

        private const val LOAD_THRESHOLD = 20

        private const val MAX_DATE = Long.MAX_VALUE
    }

    private var lastLoadedPage = 0
    private var lastItemOrderValue = MAX_DATE

    private val tagRequests = PublishSubject.create<ImageFilter>()
    private val pageRequests = PublishSubject.create<Int>()
    private val searchTagsRequests = CompositeDisposable()
    private fun Disposable.bindHistoryTagsRequest() = searchTagsRequests.add(this)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observePageRequests()
        tagRequests.onNext(ImageFilter.recent())
        requestNextPage()
        viewState.hideHistoryTags()
    }

    fun onSearchCloseClick() {
        viewState.hideHistoryTags()
        cancelHistoryTagsRequests()
        tagRequests.onNext(ImageFilter.recent())
    }

    fun onSearchFocused() {
        viewState.showHistoryTags()
        cancelHistoryTagsRequests()
        loadHistoryTags()
    }

    fun onSearchLostFocus() {
        cancelHistoryTagsRequests()
        viewState.hideHistoryTags()
    }

    private fun cancelHistoryTagsRequests() {
        searchTagsRequests.clear()
    }

    fun onQueryChanged(query: String) {
        searchTagsRequests.clear()
        loadHistoryTags(query)
    }

    private fun loadHistoryTags(query: String = "") {
        getHistoryTags.execute(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.replaceHistoryTags(it) }, this::showError)
                .bind()
                .bindHistoryTagsRequest()
    }

    fun onSuggestionRemoveClick(tag: String) {
        removeHistoryTag.execute(tag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, this::showError)
                .bind()
    }

    fun onHistoryTagSelected(tag: String) {
        viewState.showSearchQuery(tag)
        viewState.hideHistoryTags()
        searchByQuery(tag)
    }

    fun onQuerySubmitted(query: String) {
        viewState.hideHistoryTags()
        searchByQuery(query)
    }

    private fun searchByQuery(query: String) {
        val filter = if (query.isBlank()) {
            ImageFilter.recent()
        } else {
            ImageFilter.forTag(query)
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

    private fun showError(t: Throwable) {
        Timber.e(t)
        viewState.showError(t.message ?: "Error")
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
                    lastItemOrderValue = MAX_DATE
                    pageRequests.map { Pair(it, filter) }
                            .distinctUntilChanged()
                            .observeOn(AndroidSchedulers.mainThread())
                            .concatMapMaybe { (page, filter) ->
                                paginator.handle(ImagePageRequest(page, lastItemOrderValue, filter))
                                        .filter { /* Could drop images */ it.page > lastLoadedPage }
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSuccess { lastLoadedPage = it.page }
                                        .filter { it.images.isNotEmpty() }
                                        .doOnSuccess { lastItemOrderValue = it.orderValue }
                            }
                }
                .subscribe({ viewState.showImages(it.images) }, this::showError)
                .bind()
    }
}