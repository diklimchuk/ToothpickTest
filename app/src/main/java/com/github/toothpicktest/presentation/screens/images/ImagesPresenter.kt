package com.github.toothpicktest.presentation.screens.images

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.data.network.FlickrApi
import com.github.toothpicktest.presentation.mvp.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@InjectViewState
@ImagesScope
class ImagesPresenter @Inject constructor(
        private val api: FlickrApi
) : BasePresenter<ImagesView>() {

    override fun attachView(view: ImagesView?) {
        super.attachView(view)
        getImages()
    }

    private fun getImages() {
        api.getImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("tmp", it.toString())
                }, {
                    Log.e("tmp", "", it)
                })
    }
}