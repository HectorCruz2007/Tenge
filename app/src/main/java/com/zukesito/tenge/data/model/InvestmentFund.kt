@file:UseSerializers(LocalDateSerializer::class)

package com.zukesito.tenge.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

/**
 * Fondo de inversión
 */
@Serializable
data class InvestmentFund(
    val id: String,
    val name: String,
    // Monto invertido
    val amountCents: Long,
    // Rendimiento porcentual
    val ratePercent: Double,
    // Cada cuánto se aplica el rendimiento
    val frequency: Frequency,
    // Fecha de registro
    val registeredAt: LocalDate
)