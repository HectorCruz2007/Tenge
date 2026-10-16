package com.zukesito.tenge.data.model

/**
 * Plantilla precargada para crear un plan de ahorro.
 * Se carga desde un JSON semilla en el primer arranque.
 */
data class SavingsTemplate(
    // Nombre sugerido del objetivo
    val name: String,
    // Meta sugerida en centavos
    val goalCents: Long,
    // Cantidad de periodos sugerida
    val termCount: Int,
    // Unidad del plazo
    val termUnit: Frequency
)