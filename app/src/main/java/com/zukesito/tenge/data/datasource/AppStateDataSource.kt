package com.zukesito.tenge.data.datasource

import kotlinx.coroutines.flow.Flow

/** Fuente de datos local de la app. */
interface AppStateDataSource {
    // Estado actual; emite de nuevo con cada cambio
    val state: Flow<AppState>

    /** Aplica transform como una sola transacción atómica, si transform lanza una excepción, no se guarda nada. */
    suspend fun update(transform: (AppState) -> AppState): AppState
}