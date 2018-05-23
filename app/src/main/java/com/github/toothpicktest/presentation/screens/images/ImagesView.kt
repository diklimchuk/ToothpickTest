package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.mvp.BaseView

interface ImagesView : BaseView {

    @StateStrategyType(AddToEndStrategy::class)
    fun showImages(images: Collection<Image>)

    @StateStrategyType(AddToEndStrategy::class)
    fun clearImages()
}