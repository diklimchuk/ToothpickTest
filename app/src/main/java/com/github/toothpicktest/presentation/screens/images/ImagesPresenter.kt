package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.domain.repo.IImageRepo
import com.github.toothpicktest.presentation.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


@InjectViewState
@ImagesScope
class ImagesPresenter @Inject constructor(
        private val repo: IImageRepo
) : BasePresenter<ImagesView>() {

    override fun attachView(view: ImagesView?) {
        super.attachView(view)
        getImages()
    }

    private fun getImages() {
        repo.getImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.e(it.toString())
                }, {
                    Timber.e(it)
                })
    }
}