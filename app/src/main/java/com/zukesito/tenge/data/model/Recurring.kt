package com.zukesito.tenge.data.model

import java.time.LocalDate

/**
 * Ingreso o gasto que se repite con cierta frecuencia.
 */
data class Recurring(
    val id: String,
    val name: String,
    // Monto en centavos para evitar errores de redondeo
    val amountCents: Long,
    // Ingreso o gasto
    val type: RecurringType,
    // Diaria, semanal o mensual
    val frequency: Frequency,
    // Fecha de la primera ocurrencia
    val startDate: LocalDate,
    // Última ocurrencia atendida; null si aún no hay ninguna
    val lastHandledDate: LocalDate? = null
)