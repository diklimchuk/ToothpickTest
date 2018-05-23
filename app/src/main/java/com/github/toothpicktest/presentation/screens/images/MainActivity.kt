package com.github.toothpicktest.presentation.screens.images

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.toothpicktest.R
import com.github.toothpicktest.di.DiScope
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.mvp.BaseActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import kotlinx.android.synthetic.main.activity_main.images
import kotlinx.android.synthetic.main.activity_main.search
import kotlinx.android.synthetic.main.activity_main.toolbar
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule

class MainActivity : BaseActivity(), ImagesView {

    private val scope: Scope by lazy {
        Toothpick.openScopes(DiScope.APP, DiScope.IMAGES)
                .apply { bindScopeAnnotation(ImagesScope::class.java) }
    }

    @InjectPresenter
    internal lateinit var presenter: ImagesPresenter

    @ProvidePresenter
    fun providePresenter(): ImagesPresenter = scope.getInstance(ImagesPresenter::class.java)

    private val adapter = ImagesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, scope)
        scope.installModules(SmoothieSupportActivityModule(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        initSearchView()
    }

    private fun initSearchView() {
        search.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                presenter.onSearchFocused()
            } else {
                presenter.onSearchLostFocus()
            }
        }
        RxSearchView.queryTextChanges(search)
                .skipInitialValue()
                .subscribe { presenter.onQueryChanged(it.toString()) }
                .bindActive()
    }

    private fun initRecyclerView() {
        images.adapter = adapter
        images.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int,
                    dy: Int
            ) {
                val lastVisiblePosition = (recyclerView.layoutManager as GridLayoutManager)
                        .findLastVisibleItemPosition()
                val margin = adapter.itemCount - lastVisiblePosition
                presenter.onScrolledToPosition(margin)
            }
        })
    }

    override fun hideHistoryTags() {

    }

    override fun showHistoryTags(tags: List<String>) {
        Timber.e("TAGS + $tags")
    }

    override fun clearImages() {
        adapter.clear()
    }

    override fun showImages(images: Collection<Image>) {
        adapter.addImages(images)
    }
}
