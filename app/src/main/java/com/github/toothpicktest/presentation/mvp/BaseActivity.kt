package com.github.toothpicktest.presentation.mvp

import com.arellomobile.mvp.MvpAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : MvpAppCompatActivity() {

    private val disposables = CompositeDisposable()

    fun Disposable.bindActive() = disposables.add(this).let { this }

    override fun onPause() {
        super.onPause()
        disposables.clear()
    }
}