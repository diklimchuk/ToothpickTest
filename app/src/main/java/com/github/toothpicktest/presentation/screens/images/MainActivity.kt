package com.github.toothpicktest.presentation.screens.images

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.toothpicktest.R
import com.github.toothpicktest.di.DiScope
import com.github.toothpicktest.domain.entity.Image
import com.github.toothpicktest.presentation.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_main.images
import kotlinx.android.synthetic.main.activity_main.search
import kotlinx.android.synthetic.main.activity_main.suggestions
import kotlinx.android.synthetic.main.activity_main.toolbar
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
    private val suggestionsAdapter = SuggestionsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, scope)
        scope.installModules(SmoothieSupportActivityModule(this))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initImagesRecycler()
    }

    override fun onResume() {
        super.onResume()
        initSearchView()
        initSuggestionsRecycler()
    }

    private fun initSuggestionsRecycler() {
        suggestionsAdapter.getSuggestionClicks()
                .subscribe {
                    search.clearFocus()
                    presenter.onHistoryTagSelected(it)
                }
                .bindActive()
        suggestionsAdapter.getRemoveSuggestionClicks()
                .subscribe { presenter.onSuggestionRemoveClick(it) }
                .bindActive()
        suggestions.adapter = suggestionsAdapter
    }

    private fun initSearchView() {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.onQuerySubmitted(query)
                search.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.onQueryChanged(newText)
                return true
            }
        })
        search.setOnCloseListener {
            presenter.onSearchCloseClick()
            false
        }
        search.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                presenter.onSearchFocused()
            } else {
                presenter.onSearchLostFocus()
            }
        }
    }

    private fun initImagesRecycler() {
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
        suggestions.visibility = View.GONE
    }

    override fun showSearchQuery(tag: String) {
        search.setQuery(tag, false)
    }

    override fun showHistoryTags() {
        suggestions.visibility = View.VISIBLE
    }

    override fun replaceHistoryTags(tags: List<String>) {
        suggestionsAdapter.replaceItems(tags)
    }

    override fun clearImages() {
        adapter.clear()
    }

    override fun showImages(images: Collection<Image>) {
        adapter.addImages(images)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
