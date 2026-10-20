package com.zukesito.tenge

import android.app.Application
import com.zukesito.tenge.core.di.AppContainer
import com.zukesito.tenge.core.di.DefaultAppContainer

/** Punto de arranque de la app */
class TengeApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}