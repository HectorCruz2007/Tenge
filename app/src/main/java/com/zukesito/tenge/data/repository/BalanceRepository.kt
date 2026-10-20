package com.zukesito.tenge.data.repository

import kotlinx.coroutines.flow.Flow

// Acceso al saldo disponible
interface BalanceRepository {
    // Saldo en centavos
    val balanceCents: Flow<Long>

    // Aumenta o reduce el saldo manualmente, lanza InsufficientBalanceException si el saldo quedaría negativo
    suspend fun adjust(deltaCents: Long)
}