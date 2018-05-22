package com.github.toothpicktest.presentation.glide

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class ToothpickTestGlideAppModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean = false
}