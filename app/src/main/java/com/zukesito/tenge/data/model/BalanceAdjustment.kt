@file:UseSerializers(LocalDateSerializer::class)

package com.zukesito.tenge.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

/**
 * Ajuste manual del saldo con su nota opcional.
 */
@Serializable
data class BalanceAdjustment(
    val id: String,
    // Positivo si aumenta el saldo, negativo si lo reduce
    val deltaCents: Long,
    val note: String? = null,
    val date: LocalDate
)
