package com.zukesito.tenge

import android.app.Application
import com.zukesito.tenge.core.di.AppContainer
import com.zukesito.tenge.core.di.DefaultAppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Punto de arranque de la app */
class TengeApplication : Application() {

    lateinit var container: AppContainer
        private set

    // Scope que vive lo mismo que la app, para tareas de arranque
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        appScope.launch { container.seedRepository.seedIfNeeded() }
    }
}