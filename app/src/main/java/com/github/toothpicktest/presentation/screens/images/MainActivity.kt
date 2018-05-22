package com.github.toothpicktest.presentation.screens.images

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.toothpicktest.R.layout
import com.github.toothpicktest.presentation.di.DiScope
import com.github.toothpicktest.presentation.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_main.images
import kotlinx.android.synthetic.main.activity_main.toolbar
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

class MainActivity : BaseActivity(), ImagesView {

    private val scope: Scope by lazy {
        Toothpick.openScopes(DiScope.APP, DiScope.IMAGES)
                .apply { bindScopeAnnotation(Presenter::class.java) }
    }

    @InjectPresenter
    internal lateinit var presenter: ImagesPresenter

    @ProvidePresenter
    fun providePresenter() = scope.getInstance(ImagesPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, scope)
        scope.installModules(SmoothieSupportActivityModule(this))
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setSupportActionBar(toolbar)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        images.adapter = ImagesAdapter(this)
    }


    @javax.inject.Scope
    @Target(CLASS)
    @Retention(RUNTIME)
    annotation class Presenter
}