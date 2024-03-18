package com.example.github_search_api_demo.ui

import android.app.Application
import com.example.github_search_api_demo.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        intance = this
        startKoin {
            androidContext(this@MainApplication)
            AppModule().run {
                val list = listOf(appModule, vmModule, repoModule)
                modules(list)
            }
        }
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        private lateinit var intance: MainApplication
        fun getApplication() = intance
    }
}