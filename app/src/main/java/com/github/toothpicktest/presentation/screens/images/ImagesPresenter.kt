package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.domain.usecase.GetHistoryTagsUseCase
import com.github.toothpicktest.domain.usecase.RemoveHistoryTagUseCase
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
        private val getHistoryTags: GetHistoryTagsUseCase,
        private val removeHistoryTag: RemoveHistoryTagUseCase
) : BasePresenter<ImagesView>() {

    companion object {
        private const val INITIAL_PAGE_VALUE = 0
        private const val LOAD_THRESHOLD = 20
    }

    /**
     * Last page that was loaded by [paginator] for the last tag sent to [tagRequests].
     * Equals to [INITIAL_PAGE_VALUE] if no pages were loaded yet.
     */
    private var lastLoadedPage = INITIAL_PAGE_VALUE
    /**
     * Paginator assumes some order for images.
     * This variable equals to the order value of the last item on the [lastLoadedPage]
     */
    private var lastItemOrderValue = ImagePaginator.MAX_ORDER_VALUE
    /**
     * Emits tags
     */
    private val tagRequests = PublishSubject.create<ImageFilter>()
    /**
     * Emits page numbers
     */
    private val pageRequests = PublishSubject.create<Int>()
    /**
     * Emits tag suggestions filters
     */
    private val searchTagSuggestionsRequests = CompositeDisposable()

    /**
     * Adds [Disposable] to the list of running [searchTagSuggestionsRequests]
     */
    private fun Disposable.bindHistoryTagsRequest() = searchTagSuggestionsRequests.add(this)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observePageRequests()
        viewState.hideHistoryTags()
        searchRecent()
    }

    /*
     * Search
     */
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

    fun onSearchQueryChanged(query: String) {
        cancelHistoryTagsRequests()
        loadHistoryTags(query)
    }

    fun onQuerySubmitted(query: String) {
        viewState.hideHistoryTags()
        if (query.isBlank()) {
            searchRecent()
        } else {
            searchByQuery(query)
        }
    }

    fun onSearchSuggestionRemoveClick(tag: String) {
        removeHistoryTag.execute(tag)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, this::showError)
                .bind()
    }

    fun onSearchSuggestionSelected(suggestion: String) {
        viewState.showSearchQuery(suggestion)
        viewState.hideHistoryTags()
        searchByQuery(suggestion)
    }

    private fun loadHistoryTags(query: String = "") {
        getHistoryTags.execute(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.replaceHistoryTags(it) }, this::showError)
                .bind()
                .bindHistoryTagsRequest()
    }

    private fun cancelHistoryTagsRequests() {
        searchTagSuggestionsRequests.clear()
    }

    private fun searchRecent() {
        tagRequests.onNext(ImageFilter.recent())
        requestNextPage()
    }

    private fun searchByQuery(tag: String) {
        tagRequests.onNext(ImageFilter.forTag(tag))
        requestNextPage()
    }

    /*
     * Images
     */

    fun onRefresh() {
        clearProgress()
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

    private fun clearProgress() {
        viewState.clearImages()
        lastLoadedPage = INITIAL_PAGE_VALUE
        lastItemOrderValue = ImagePaginator.MAX_ORDER_VALUE
    }

    private fun observePageRequests() {
        tagRequests
                .distinctUntilChanged()
                .switchMap { filter ->
                    clearProgress()
                    pageRequests.map { Pair(it, filter) }
                            .distinctUntilChanged()
                            .observeOn(AndroidSchedulers.mainThread())
                            .concatMapMaybe { (page, filter) ->
                                 paginator.handle(ImagePageRequest(page, lastItemOrderValue, filter))
                                        .filter { /* Could drop images */ it.page > lastLoadedPage }
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSuccess { lastLoadedPage = it.page }
                                        .filter { it.images.isNotEmpty() }
                                        .doOnSuccess { lastItemOrderValue = it.minOrderValue }
                            }
                }
                .subscribe({
                    viewState.hideSwipeRefresh()
                    viewState.showImages(it.images)
                }, this::showError)
                .bind()
    }

    /*
     * Other
     */

    private fun showError(t: Throwable) {
        Timber.e(t)
        viewState.showError(t.message ?: "Error")
    }
}