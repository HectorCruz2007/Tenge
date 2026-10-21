package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.datasource.AppStateDataSource
import com.zukesito.tenge.data.model.BalanceAdjustment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID

class BalanceRepositoryImpl(
    private val dataSource: AppStateDataSource
) : BalanceRepository {

    override val balanceCents: Flow<Long> =
        dataSource.state.map { it.balanceCents }.distinctUntilChanged()

    override suspend fun adjust(deltaCents: Long, note: String?, date: LocalDate) {
        val adjustment = BalanceAdjustment(
            id = UUID.randomUUID().toString(),
            deltaCents = deltaCents,
            // Nota vacía se guarda como null
            note = note?.trim()?.takeIf { it.isNotEmpty() },
            date = date
        )
        // Saldo y registro del ajuste en la misma transacción
        dataSource.update { state ->
            state.withBalanceChange(deltaCents).copy(adjustments = state.adjustments + adjustment)
        }
    }
}
