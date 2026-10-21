package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.InvestmentFund
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Acceso a fondos de inversión. */
interface InvestmentRepository {
    val funds: Flow<List<InvestmentFund>>

    // Registra un fondo
    suspend fun add(
        name: String,
        amountCents: Long,
        ratePercent: Double,
        frequency: Frequency,
        registeredAt: LocalDate
    )

    // Elimina un fondo
    suspend fun delete(id: String)
}
