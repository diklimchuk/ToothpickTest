package com.github.toothpicktest.presentation.mvp

import com.arellomobile.mvp.MvpAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : MvpAppCompatActivity() {

    private val disposables = CompositeDisposable()

    fun Disposable.bind() = disposables.add(this).let { this }
}