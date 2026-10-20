@file:UseSerializers(LocalDateSerializer::class)

package com.zukesito.tenge.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDate

/**
 * Gasto registrado por el usuario.
 */
@Serializable
data class Expense(
    val id: String,
    // Monto en centavos para evitar errores de redondeo
    val amountCents: Long,
    // Referencia a Category.id
    val categoryId: String,
    val date: LocalDate,
    // Nota opcional
    val note: String? = null
)