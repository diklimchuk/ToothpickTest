package com.github.toothpicktest.presentation

import android.app.Application
import android.content.Context
import com.github.toothpicktest.BuildConfig
import com.github.toothpicktest.di.DiScope
import com.github.toothpicktest.di.NetworkModule
import com.github.toothpicktest.di.RepoModule
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

class App : Application() {

    private val appModule = object : Module() {
        init {
            bind(Context::class.java)
                    .toInstance(this@App)
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
            FactoryRegistryLocator.setRootRegistry(com.github.toothpicktest.FactoryRegistry())
            MemberInjectorRegistryLocator
                    .setRootRegistry(com.github.toothpicktest.MemberInjectorRegistry())
        }

        val appScope = Toothpick.openScope(DiScope.APP)
        appScope.installModules(
                appModule,
                NetworkModule(BuildConfig.API_BASE_URL, BuildConfig.FLICKR_API_KEY),
                RepoModule()
        )
    }
}