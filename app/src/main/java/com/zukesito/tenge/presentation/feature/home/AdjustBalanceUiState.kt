package com.zukesito.tenge.presentation.feature.home

import com.zukesito.tenge.presentation.feature.common.AmountError

/** Estado del formulario para modificar el saldo. */
data class AdjustBalanceUiState(
    // true = aumentar, false = reducir
    val increase: Boolean,
    val amountText: String = "",
    val note: String = "",
    val currentBalanceCents: Long = 0L,
    // Ajuste con signo y saldo resultante para el resumen
    val adjustmentCents: Long = 0L,
    val newBalanceCents: Long = 0L,
    val amountError: AmountError? = null,
    // Se intentó guardar; a partir de aquí se muestran todos los errores
    val submitted: Boolean = false,
    val saveEnabled: Boolean = true,
    val isSaved: Boolean = false
)
