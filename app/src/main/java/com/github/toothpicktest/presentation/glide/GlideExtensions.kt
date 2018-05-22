package com.github.toothpicktest.presentation.glide

import android.app.Activity
import android.support.v4.app.Fragment
import android.widget.ImageView

fun ImageView.load(activity: Activity, imageUrl: String) {
    GlideApp.with(activity).load(imageUrl).into(this)
}

fun ImageView.load(fragment: Fragment, imageUrl: String) {
    GlideApp.with(fragment).load(imageUrl).into(this)
}

fun ImageView.clear(fragment: Fragment) {
    GlideApp.with(fragment).clear(this)
}