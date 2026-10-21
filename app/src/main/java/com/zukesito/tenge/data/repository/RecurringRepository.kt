package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.Recurring
import com.zukesito.tenge.data.model.RecurringType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Acceso a ingresos y gastos recurrentes. */
interface RecurringRepository {
    val recurrings: Flow<List<Recurring>>

    // Registra un recurrente
    suspend fun add(
        name: String,
        amountCents: Long,
        type: RecurringType,
        frequency: Frequency,
        startDate: LocalDate
    )

    // Elimina un recurrente
    suspend fun delete(id: String)
}
