package com.zukesito.tenge.data.model

import java.time.LocalDate

/** Ocurrencia vencida y no atendida de un recurrente o cuota de ahorro */
data class PendingNotice(
    // Id del Recurring o SavingsPlan
    val sourceId: String,
    val source: NoticeSource,
    // Nombre a mostrar
    val name: String,
    // Monto en centavos
    val amountCents: Long,
    // Fecha de la ocurrencia
    val dueDate: LocalDate
)