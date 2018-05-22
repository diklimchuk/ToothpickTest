package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.InjectViewState
import com.github.toothpicktest.presentation.mvp.BasePresenter
import javax.inject.Inject


@InjectViewState
@MainActivity.Presenter
class ImagesPresenter @Inject constructor(

) : BasePresenter<ImagesView>() {

    override fun attachView(view: ImagesView?) {
        super.attachView(view)
    }
}