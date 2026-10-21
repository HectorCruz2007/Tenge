package com.zukesito.tenge.data.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

// Acceso al saldo disponible
interface BalanceRepository {
    // Saldo en centavos
    val balanceCents: Flow<Long>

    // Aumenta o reduce el saldo manualmente y guarda el ajuste con su nota,
    // lanza InsufficientBalanceException si el saldo quedaría negativo
    suspend fun adjust(deltaCents: Long, note: String?, date: LocalDate)
}
