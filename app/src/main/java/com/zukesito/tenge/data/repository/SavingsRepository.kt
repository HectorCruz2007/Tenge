package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.SavingsPlan
import com.zukesito.tenge.data.model.SavingsTemplate
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Acceso a planes de ahorro y sus plantillas. */
interface SavingsRepository {
    val plans: Flow<List<SavingsPlan>>

    // Plantillas precargadas del JSON semilla
    val templates: Flow<List<SavingsTemplate>>

    // Crea un plan de ahorro
    suspend fun add(name: String, goalCents: Long, termCount: Int, termUnit: Frequency, startDate: LocalDate)

    // Elimina un plan
    suspend fun delete(id: String)
}
