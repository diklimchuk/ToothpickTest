package com.github.toothpicktest.presentation.screens.images

import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.mvp.AddToEndSingleByTagStateStrategy
import com.github.toothpicktest.presentation.mvp.BaseView

interface ImagesView : BaseView {

    @StateStrategyType(AddToEndStrategy::class)
    fun showImages(images: Collection<Image>)

    @StateStrategyType(AddToEndStrategy::class)
    fun clearImages()

    @StateStrategyType(SkipStrategy::class)
    fun showSearchQuery(tag: String)

    @StateStrategyType(AddToEndSingleByTagStateStrategy::class, tag = "tagHistory")
    fun hideHistoryTags()

    @StateStrategyType(AddToEndSingleByTagStateStrategy::class, tag = "tagHistory")
    fun showHistoryTags(tags: List<String>)
}