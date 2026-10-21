package com.zukesito.tenge.data.repository

import com.zukesito.tenge.data.model.PendingNotice
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Avisos de recurrentes y cuotas de ahorro vencidos. */
interface NoticeRepository {
    // Aviso más antiguo pendiente de cada recurrente y plan, hasta today
    fun pendingNotices(today: LocalDate): Flow<List<PendingNotice>>

    // Aplica el aviso al saldo (y al plan si es cuota)
    suspend fun confirm(notice: PendingNotice)

    // Marca el aviso como atendido sin tocar el saldo
    suspend fun ignore(notice: PendingNotice)
}
