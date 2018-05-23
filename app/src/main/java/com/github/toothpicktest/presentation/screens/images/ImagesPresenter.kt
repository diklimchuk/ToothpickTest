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
        private const val IMAGE_BLOCK_SIZE = 100

        private const val LOAD_THRESHOLD = 20

        private val MAX_DATE = Date(Long.MAX_VALUE)
    }

    private var lastRequestedPage = 0

    private val pageRequests = PublishSubject.create<PageRequest>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observePageRequests()
        pageRequests.onNext(createPageRequest(1, MAX_DATE))
    }

    fun onScolledToPosition(
            itemUploadDate: Date,
            margin: Int
    ) {
        if (margin < LOAD_THRESHOLD) {
            val nextPage = lastRequestedPage
            pageRequests.onNext(createPageRequest(lastRequestedPage, itemUploadDate))
            lastRequestedPage = nextPage
        }
    }

    private fun observePageRequests() {
        pageRequests
                .concatMapSingle { request ->
                    repo.getImages(request.page, IMAGE_BLOCK_SIZE)
                            .map { it.filter { it.uploadDate < request.getMaxUploadDate() } }
                }
                .filter { it.isNotEmpty() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ viewState.showImages(it) }, { Timber.e(it) })
    }

    private fun PageRequest.getMaxUploadDate() = Date(orderValueLt)

    private fun createPageRequest(
            page: Int,
            maxUploadDate: Date
    ) = PageRequest(page, maxUploadDate.time)
}