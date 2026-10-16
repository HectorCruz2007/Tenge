package com.zukesito.tenge.data.model

import java.time.LocalDate

/**
 * Plan de ahorro con una meta y un plazo.
 */
data class SavingsPlan(
    val id: String,
    // Nombre del objetivo
    val name: String,
    // Monto meta en centavos
    val goalCents: Long,
    // Cantidad de periodos del plazo
    val termCount: Int,
    // Unidad del plazo y frecuencia de la cuota
    val termUnit: Frequency,
    // Fecha en que inicia el plan
    val startDate: LocalDate,
    // Monto ahorrado hasta ahora, en centavos
    val accumulatedCents: Long = 0L,
    // Última cuota atendida, null si aún no hay ninguna
    val lastHandledDate: LocalDate? = null
)