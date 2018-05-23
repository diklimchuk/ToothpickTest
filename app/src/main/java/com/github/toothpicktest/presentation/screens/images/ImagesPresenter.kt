package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.presentation.mvp.BasePresenter
import com.github.toothpicktest.presentation.screens.images.pagination.ImagePageRequest
import com.github.toothpicktest.presentation.screens.images.pagination.ImagePaginator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.Date
import javax.inject.Inject


@InjectViewState
@ImagesScope
class ImagesPresenter @Inject constructor(
        private val paginator: ImagePaginator
) : BasePresenter<ImagesView>() {

    companion object {

        private const val LOAD_THRESHOLD = 20

        private val MAX_DATE = Date(Long.MAX_VALUE)
    }

    private var lastLoadedPage = 0
    private var lastVisibleItemUploadDate = MAX_DATE
    private var currentTag: String? = null

    private val pageRequests = PublishSubject.create<ImagePageRequest>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observePageRequests()
        requestNextPage()
    }

    fun onQueryChanged(newQuery: String) {
        viewState.clearImages()
        currentTag = newQuery
        lastLoadedPage = 0
        lastVisibleItemUploadDate = MAX_DATE
        requestNextPageWithTag()
    }

    fun onScrolledToPosition(
            margin: Int
    ) {
        if (margin < LOAD_THRESHOLD) {
            if (currentTag == null) {
                requestNextPage()
            } else {
                requestNextPageWithTag()
            }
        }
    }

    /**
     * Ignores request if [currentTag] is null.
     */
    private fun requestNextPageWithTag() {
        if (currentTag == null) return
        val nextPage = lastLoadedPage + 1
        val pageRequest = ImagePageRequest(nextPage, lastVisibleItemUploadDate, currentTag!!)
        pageRequests.onNext(pageRequest)
    }

    private fun requestNextPage() {
        val nextPage = lastLoadedPage + 1
        val pageRequest = ImagePageRequest(nextPage, lastVisibleItemUploadDate)
        pageRequests.onNext(pageRequest)
    }

    private fun observePageRequests() {
        pageRequests
                .observeOn(Schedulers.computation())
                .distinctUntilChanged()
                .concatMapSingle(paginator::handle)
                .observeOn(AndroidSchedulers.mainThread())
                .filter { /* Could drop images */ it.page > lastLoadedPage }
                .doOnNext { lastLoadedPage = it.page }
                .filter { it.images.isNotEmpty() }
                .doOnNext { if (it.hasMinDate) lastVisibleItemUploadDate = it.minDate }
                .subscribe({ viewState.showImages(it.images) }, { Timber.e(it) })
    }
}