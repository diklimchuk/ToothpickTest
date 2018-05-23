package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.domain.repo.IImageRepo
import com.github.toothpicktest.presentation.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.Date
import javax.inject.Inject


@InjectViewState
@ImagesScope
class ImagesPresenter @Inject constructor(
        private val repo: IImageRepo
) : BasePresenter<ImagesView>() {

    companion object {
        private const val IMAGE_BLOCK_SIZE = 30

        private const val LOAD_THRESHOLD = 20

        private val MAX_DATE = Date(Long.MAX_VALUE)
    }

    private var lastLoadedPage = 0
    private var lastVisibleItemUploadDate = MAX_DATE

    private val pageRequests = PublishSubject.create<PageRequest>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observePageRequests()
        requestNextPage()
    }

    fun onQueryChanged(newQuery: String) {

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
        val pageRequest = createPageRequest(nextPage, lastVisibleItemUploadDate)
        pageRequests.onNext(pageRequest)
    }

    private fun observePageRequests() {
        pageRequests
                .observeOn(Schedulers.computation())
                .distinctUntilChanged()
                .concatMapSingle { request ->
                    repo.getImages(request.page, IMAGE_BLOCK_SIZE)
                            .observeOn(Schedulers.computation())
                            .map { it.filter { it.updateDate < request.getMaxUploadDate() } }
                            .map { Pair(request.page, it) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (page, images) ->
                    lastLoadedPage = page
                    images.minBy { it.updateDate }
                            ?.updateDate
                            ?.let { lastVisibleItemUploadDate = it }
                    viewState.showImages(images)
                }, { Timber.e(it) })
    }

    private fun PageRequest.getMaxUploadDate() = Date(orderValueLt)

    private fun createPageRequest(
            page: Int,
            maxUploadDate: Date
    ) = PageRequest(page, maxUploadDate.time)
}