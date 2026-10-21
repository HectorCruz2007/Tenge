package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.datasource.SeedDataSource
import kotlinx.coroutines.flow.first

class SeedRepositoryImpl(
    private val appState: AppStateDataSource,
    private val seed: SeedDataSource
) : SeedRepository {

    override suspend fun seedIfNeeded() {
        // Evita leer el archivo si ya se cargó antes
        if (appState.state.first().isSeeded) return
        val data = seed.load()
        appState.update { state ->
            // Se revisa otra vez dentro de la transacción por seguridad
            if (state.isSeeded) state
            else state.copy(categories = data.categories, templates = data.templates, isSeeded = true)
        }
    }
}