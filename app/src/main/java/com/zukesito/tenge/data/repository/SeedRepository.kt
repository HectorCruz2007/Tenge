package com.zukesito.tenge.data.repository

/** Carga inicial de catálogos. */
interface SeedRepository {
    // Carga categorías y plantillas solo en el primer arranque
    suspend fun seedIfNeeded()
}